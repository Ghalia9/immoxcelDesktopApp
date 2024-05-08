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
    public String compare(String imagePath ) throws IOException {

        String cleanedImagePath = cleanImagePath(imagePath);

        BufferedImage img1 = ImageIO.read(new File("C:\\Users\\Alice\\IdeaProjects\\MyJavaFxApp\\src\\main\\resources\\com\\example\\myjavafxapp\\Pictures\\Doc1.jpeg"));
        BufferedImage img2 = ImageIO.read(new File(cleanedImagePath));
        int w1 = img1.getWidth();
        int w2 = img2.getWidth();
        int h1 = img1.getHeight();
        int h2 = img2.getHeight();
        if ((w1 != w2) || (h1 != h2)) {
            System.out.println("Both images should have same dimention");
            Resultat= "Both images should have same dimention";

        } else {
            long diff = 0;
            for (int j = 0; j < h1; j++) {
                for (int i = 0; i < w1; i++) {
                    //Getting the RGB values of a pixel
                    int pixel1 = img1.getRGB(i, j);
                    Color color1 = new Color(pixel1, true);
                    int r1 = color1.getRed();
                    int g1 = color1.getGreen();
                    int b1 = color1.getBlue();
                    int pixel2 = img2.getRGB(i, j);
                    Color color2 = new Color(pixel2, true);
                    int r2 = color2.getRed();
                    int g2 = color2.getGreen();
                    int b2 = color2.getBlue();
                    //sum of differences of RGB values of the two images
                    long data = Math.abs(r1 - r2) + Math.abs(g1 - g2) + Math.abs(b1 - b2);
                    diff = diff + data;
                }
            }
            double avg = diff / (w1 * h1 * 3);
            double percentage = (avg / 255) * 100;
            System.out.println("Difference: " + percentage);
            Resultat="DDifference: " + percentage;


        }
        return Resultat;
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
