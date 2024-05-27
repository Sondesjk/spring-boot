package tn.projetdemo.demo.services;

import com.google.zxing.WriterException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.projetdemo.demo.entities.*;
import tn.projetdemo.demo.repository.PostRepository;
import tn.projetdemo.demo.repository.UserRepository;
import tn.projetdemo.demo.repository.VoitureRepository;

import java.io.IOException;
import java.util.*;


@Service
public class VoitureServiceImpl implements VoitureServiceInter {

	@Autowired
	private VoitureRepository voitureRepository;

	private final QRCodeService qrCodeService;

    public VoitureServiceImpl(QRCodeService qrCodeService) {
        this.qrCodeService = qrCodeService;
    }


	public Voiture saveVoiture(Voiture voiture) {
		Voiture savedVoiture = voitureRepository.save(voiture);
		try {
			byte[] qrCodeImage = qrCodeService.generateQrCodeImage(savedVoiture);
			QR qr = new QR();
			qr.setIdQr(savedVoiture.getId());
			qr.setImage(qrCodeImage);
			qr.setVersion("1.0");
			qr.setContent("ID: " + savedVoiture.getId() + " | Matricule: " + savedVoiture.getMatricule());
			savedVoiture.setQr(qr);
			voitureRepository.save(savedVoiture);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return savedVoiture;
	}
	@Override
	public void deleteVoiture(Long id) {
		voitureRepository.deleteById(id);
	}

	@Override
	public Voiture updateVoiture(Long id, Voiture newVoiture) {
		Optional<Voiture> optionalVoiture = voitureRepository.findById(id);
		if (optionalVoiture.isPresent()) {
			Voiture existingVoiture = optionalVoiture.get();
			existingVoiture.setMarque(newVoiture.getMarque());
			existingVoiture.setAnnee(newVoiture.getAnnee());
			existingVoiture.setPrix(newVoiture.getPrix());
			existingVoiture.setMatricule(newVoiture.getMatricule());
			existingVoiture.setNumDeChoussi(newVoiture.getNumDeChoussi());
			return voitureRepository.save(existingVoiture);
		} else {
			throw new IllegalArgumentException("Voiture not found with id: " + id);
		}
	}

	@Override
	public Voiture getVoitureById(Long id) {
		Optional<Voiture> optionalVoiture = voitureRepository.findById(id);
		if (optionalVoiture.isPresent()) {
			return optionalVoiture.get();
		} else {
			throw new IllegalArgumentException("Voiture not found with id: " + id);
		}
	}
	//recherche
	@Override
	public List<Voiture> findByMarque(String marque) {
		return voitureRepository.findByMarque(marque);
	}

	@Override
	public List<Voiture> findByModele(String modele) {
		return voitureRepository.findByModele(modele);
	}

	@Override
	public List<Voiture> getAllVoitures() {
		return voitureRepository.findAll();
	}

}