package tn.projetdemo.demo.controllers;

import com.google.zxing.WriterException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.projetdemo.demo.entities.Post;
import tn.projetdemo.demo.entities.User;
import tn.projetdemo.demo.entities.Voiture;
import tn.projetdemo.demo.services.QRCodeService;
import tn.projetdemo.demo.services.UserServiceImpl;
import tn.projetdemo.demo.services.VoitureServiceInter;

import java.io.IOException;
import java.util.List;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")

@RequestMapping("/voiture")

public class VoitureController {

    private final QRCodeService qrCodeService;


    @Autowired
    private VoitureServiceInter voitureService;

    public VoitureController(QRCodeService qrCodeService) {
        this.qrCodeService = qrCodeService;
    }


    @PostMapping("/add")
    public ResponseEntity<Voiture> addVoiture(@RequestBody Voiture voiture) {
        Voiture addedVoiture = voitureService.saveVoiture(voiture);
        try {
            byte[] qrCodeImage = qrCodeService.generateQrCodeImage(addedVoiture);
            // You can return the QR code image as a byte array or save it to a file
            return ResponseEntity.status(HttpStatus.CREATED).body(addedVoiture);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{voitureId}/qrcode")
    public ResponseEntity<byte[]> getQRCodeImage(@PathVariable Long voitureId) {
        Voiture voiture = voitureService.getVoitureById(voitureId);
        if (voiture == null) {
            return ResponseEntity.notFound().build();
        }

        try {
            byte[] qrCodeImage = qrCodeService.generateQrCodeImage(voiture);
            return ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(qrCodeImage);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteVoiture(@PathVariable Long id) {
        voitureService.deleteVoiture(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Voiture> updateVoiture(@PathVariable Long id, @RequestBody Voiture voiture) {
        Voiture updatedVoiture = voitureService.updateVoiture(id, voiture);
        return ResponseEntity.ok(updatedVoiture);
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<Voiture> getVoitureById(@PathVariable Long id) {
        Voiture voiture = voitureService.getVoitureById(id);
        return ResponseEntity.ok(voiture);
    }
    @GetMapping("/getAllvoitures")
    public List<Voiture> getAllVoitures() {
       return voitureService.getAllVoitures();

    }



    @GetMapping("/recherche/{marque}")
    public List<Voiture> getVoituresByModele(@PathVariable String marque) {
        return voitureService.findByMarque(marque);
    }
}


