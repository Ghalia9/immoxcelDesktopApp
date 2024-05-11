package tn.esprit.controllers;

import tn.esprit.services.ServiceSupplier;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class ComparingImage {
    private ServiceSupplier supplierServ = new ServiceSupplier();

    public String image;
    public String Resultat="";
    public ComparingImage(){}
    public double compare(String imagePath ) throws IOException {

        String cleanedImagePath = cleanImagePath(imagePath);

        BufferedImage img1 = ImageIO.read(new File("C:\\Users\\moham\\Desktop\\Esprit\\3 eme A\\S2\\JAVA\\immoxcelRemote\\src\\main\\resources\\Doc1.jpeg"));
        BufferedImage img2 = ImageIO.read(new File(cleanedImagePath));
        BufferedImage resizedImg = resizeImage(img2, img1.getWidth(), img1.getHeight());

        long diff = 0;
        int width = img1.getWidth();
        int height = img1.getHeight();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int rgb1 = img1.getRGB(x, y);
                int rgb2 = resizedImg.getRGB(x, y);
                // Comparer les composants RGB
                int redDiff = Math.abs((rgb1 >> 16) & 0xFF - (rgb2 >> 16) & 0xFF);
                int greenDiff = Math.abs((rgb1 >> 8) & 0xFF - (rgb2 >> 8) & 0xFF);
                int blueDiff = Math.abs((rgb1) & 0xFF - (rgb2) & 0xFF);
                // Calculer la différence totale
                diff += redDiff + greenDiff + blueDiff;
            }
        }
        double avgDiff = diff / (width * height * 3.0); // Divisé par 3 car nous avons trois composants de couleur (R, G, B)
        double percentage = (avgDiff / 255) * 100; // Convertir en pourcentage
        return  percentage;
    }

    private BufferedImage resizeImage(BufferedImage originalImage, int targetWidth, int targetHeight) {
        BufferedImage resizedImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = resizedImage.createGraphics();
        g.drawImage(originalImage, 0, 0, targetWidth, targetHeight, null);
        g.dispose();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        return resizedImage;
    }
    private String cleanImagePath(String imagePath) {
        String cleanedImagePath = imagePath;
        try {
            URI uri = new URI(imagePath);
            if (uri.getScheme() != null && uri.getScheme().equals("file")) {
                cleanedImagePath = uri.getPath();
            }
        } catch (URISyntaxException e) {
            // Handle the URISyntaxException if necessary
            e.printStackTrace();
        }
        return cleanedImagePath;
    }
}
