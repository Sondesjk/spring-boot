package tn.projetdemo.demo.services;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import tn.projetdemo.demo.entities.QR;
import tn.projetdemo.demo.entities.Voiture;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
public class QRCodeService {

    private static final int QR_CODE_SIZE = 350;

    public byte[] generateQrCodeImage(Voiture voiture) throws IOException {
        String content = "ID: " + voiture.getId() + " | Matricule: " + voiture.getMatricule();
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        Map<EncodeHintType, Object> hintsMap = new HashMap<>();
        hintsMap.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        BitMatrix bitMatrix;
        try {
            bitMatrix = qrCodeWriter.encode(content, BarcodeFormat.QR_CODE, QR_CODE_SIZE, QR_CODE_SIZE, hintsMap);
        } catch (WriterException e) {
            throw new RuntimeException("Failed to generate QR code image.", e);
        }

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        BufferedImage qrImage = toBufferedImage(bitMatrix);
        ImageIO.write(qrImage, "png", outputStream);

        return outputStream.toByteArray();
    }

    private BufferedImage toBufferedImage(BitMatrix matrix) {
        int width = matrix.getWidth();
        int height = matrix.getHeight();
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                image.setRGB(x, y, matrix.get(x, y) ? 0xFF000000 : 0xFFFFFFFF);
            }
        }
        return image;
    }
}
