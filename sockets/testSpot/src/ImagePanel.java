/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author xuandat7
 */
import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

public class ImagePanel extends JPanel {
    List<DifferenceRegion> differenceRegions; // List of difference regions
    List<DifferenceRegion> foundDifferences;  // List of found regions
    BufferedImage leftImage;  // Original image on the left
    BufferedImage rightImage; // Image with differences on the right
    int panelWidth;
    int panelHeight;

    public ImagePanel() {
        differenceRegions = new ArrayList<>();
        foundDifferences = new ArrayList<>();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        panelWidth = getWidth();
        panelHeight = getHeight();
        int halfWidth = panelWidth / 2;

        // Draw original image (on the left)
        if (leftImage != null) {
            g.drawImage(leftImage, 0, 0, halfWidth, panelHeight, null);
        }

        // Draw image with differences (on the right)
        if (rightImage != null) {
            g.drawImage(rightImage, halfWidth, 0, halfWidth, panelHeight, null);
        }

        // Draw vertical separator in the middle
        g.setColor(Color.BLACK);
        g.fillRect(halfWidth - 2, 0, 4, panelHeight); // Separator bar 4px thick

        // Draw difference regions on the right image
        drawDifferences(g, halfWidth);
    }

    // Draw grid and difference regions (if not found) on the right image
    private void drawDifferences(Graphics g, int offsetX) {
        for (DifferenceRegion region : differenceRegions) {
            if (!foundDifferences.contains(region)) {
                region.draw(g, offsetX);  // Adjust position for the right image
            }
        }
    }

    // Check if player clicks on a difference region
    public void checkDifference(Point clickPoint) {
        // Only process clicks on the right image (right side of the window)
        int halfWidth = panelWidth / 2;
        if (clickPoint.x < halfWidth) {
            return; // Do not process clicks on the left (original image)
        }

        // Adjust click coordinates for the right image
        Point adjustedClickPoint = new Point(clickPoint.x - halfWidth, clickPoint.y);

        // Check if click hits any difference region
        for (DifferenceRegion region : differenceRegions) {
            if (region.contains(adjustedClickPoint) && !foundDifferences.contains(region)) {
                foundDifferences.add(region);
                repaint();
                break;
            }
        }
    }

    // Check if all differences are found
    public boolean allDifferencesFound() {
        return foundDifferences.size() == differenceRegions.size();
    }

    // Load level including images and difference points
    public void loadLevel(int level) throws IOException {
        foundDifferences.clear();
        differenceRegions.clear();

        // Load level 0 tutorial
        if (level == 0) {
            leftImage = null; // No left image
            rightImage = null; // No right image
            repaint();
            return;
        }

        // Load original image (left)
        String leftImageFilename = "src/images/level" + level + "_left.png";
        leftImage = ImageIO.read(new File(leftImageFilename));

        // Load image with differences (right)
        String rightImageFilename = "src/images/level" + level + "_right.png";
        rightImage = ImageIO.read(new File(rightImageFilename));

        // Load difference regions
        String differenceFilename = "src/levels/level" + level + ".txt";  // Read from file containing difference regions
        try (Scanner scanner = new Scanner(new File(differenceFilename))) {
            while (scanner.hasNextInt()) {
                int x = scanner.nextInt();
                int y = scanner.nextInt();
                int size = scanner.nextInt();
                differenceRegions.add(new DifferenceRegion(x, y, size));  // Add difference region to the list
            }
        }

        repaint();
    }
}
