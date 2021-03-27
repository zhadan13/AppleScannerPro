package com.project;

import javax.swing.*;
import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.*;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import javax.imageio.ImageIO;
import javax.swing.border.Border;

import net.coobird.thumbnailator.Thumbnails;

public class application extends JFrame {

    private static BufferedImage[] image = new BufferedImage[5];
    private static BufferedImage[] image_icon = new BufferedImage[5];
    private static int length = 0;
    private static int[] color = new int[3];
    private String answer;

    private application() throws Exception {
        UIManager.setLookAndFeel("com.jtattoo.plaf.aluminium.AluminiumLookAndFeel"); // Set theme of application
        AtomicInteger current_theme = new AtomicInteger(1); // Theme type identifier

        this.setTitle("Apple Scanner Pro 1.1");

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Default close operation

        final Border solidBorder = BorderFactory.createLineBorder(Color.GRAY, 3); // Create border

        final Container container = this.getContentPane(); // Create container with all elements of interface
        container.setLayout(new BorderLayout());

        // Create and setting buttons
        final JPanel buttons_Panel = new JPanel(new FlowLayout()); // Create buttons panel
        buttons_Panel.setBorder(BorderFactory.createTitledBorder("Let's go"));
        final JButton button_open = new JButton("Open photos"); // First button
        final JButton button_start = new JButton("Start scanning"); // Second button
        button_open.setBackground(new Color(0, 73, 83)); // Set colors
        button_start.setBackground(new Color(186, 12, 46));
        button_open.setForeground(Color.WHITE); // Set text color on buttons
        button_start.setForeground(Color.WHITE);
        button_open.setFont(new Font("SansSerif", Font.PLAIN, 13)); // Set text view
        button_start.setFont(new Font("SansSerif", Font.PLAIN, 13));
        button_open.setPreferredSize(new Dimension(150, 30)); // Buttons size
        button_start.setPreferredSize(new Dimension(150, 30));
        button_start.setEnabled(false); // Get off "start" button
        button_open.setFocusPainted(false); // Get off frames of buttons
        button_start.setFocusPainted(false);
        buttons_Panel.add(button_open);
        buttons_Panel.add(button_start);
        container.add(buttons_Panel, BorderLayout.NORTH); // Add buttons panel to container

        final JPanel photos_Panel = new JPanel(new FlowLayout()); // Create photos panel
        photos_Panel.setBorder(BorderFactory.createTitledBorder("Uploaded Photos"));
        container.add(photos_Panel, BorderLayout.CENTER); // Add photos panel to container

        Font font = new Font("SansSerif", Font.PLAIN, 13);
        final JPanel information_Panel = new JPanel(); // Create info panel
        information_Panel.setBorder(BorderFactory.createTitledBorder("Information"));
        String text = "<html><center><h2> APPLE SCANNER PRO </h2><br>" +
                " Professional program for industries that sorted apples &#127822 <br>" +
                "<br>" +
                "<hr>" +
                " Copyright © 2019 Zhadan Artem. All rights reserved. </center></html>";
        JLabel html_text_Label = new JLabel();
        html_text_Label.setText(text);
        html_text_Label.setFont(font);
        information_Panel.add(html_text_Label); // Add text to info panel
        final JButton button_theme = new JButton("Theme"); // Create button to change theme
        button_theme.setBackground(new Color(150, 150, 255));
        button_theme.setForeground(Color.WHITE);
        button_theme.setFont(new Font("SansSerif", Font.PLAIN, 12)); // Set text view
        button_theme.setPreferredSize(new Dimension(90, 20)); // Button size
        button_theme.setFocusPainted(false); // Get off frame of button
        information_Panel.add(button_theme); // Add button to info panel
        container.add(information_Panel, BorderLayout.SOUTH); // Add info panel to container

        // Action if "theme change" button clicked
        button_theme.addActionListener(e -> {
            if (current_theme.get() == 1) {
                try {
                    UIManager.setLookAndFeel("com.jtattoo.plaf.noire.NoireLookAndFeel"); // Change theme of application
                } catch (ClassNotFoundException | UnsupportedLookAndFeelException | InstantiationException | IllegalAccessException ex) {
                    ex.printStackTrace();
                }
                button_theme.setText("Bright theme"); // Change text on button
                button_theme.setForeground(Color.WHITE); // Change text color on button
                current_theme.set(0); // Change theme identifier
            } else {
                try {
                    UIManager.setLookAndFeel("com.jtattoo.plaf.aluminium.AluminiumLookAndFeel"); // Change theme of application
                } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
                    ex.printStackTrace();
                }
                button_theme.setText("Dark theme"); // Change text on button
                button_theme.setForeground(Color.BLACK); // Change text color on button
                current_theme.set(1); // Change theme identifier
            }
            SwingUtilities.updateComponentTreeUI(container); // Update container
        });

        // Action if "open" button clicked
        button_open.addActionListener(e -> {
            JFileChooser file_open = new JFileChooser(); // Import file system
            file_open.setMultiSelectionEnabled(true); // Activate multiple selection of files
            int ret = file_open.showOpenDialog(null);
            if (ret == JFileChooser.APPROVE_OPTION) {
                File[] files = file_open.getSelectedFiles(); // Read files
                int i = 0;
                final int l = files.length; // Remember number of files
                length = l;
                // Show ERROR if number of images more than 3
                if (length > 3) {
                    SwingUtilities.invokeLater(() -> {
                        final JLabel error = new JLabel();
                        String error_text = "<html> INPUT ERROR! You should open no more than three photos! </html>";
                        error.setText(error_text);
                        Toolkit.getDefaultToolkit().beep(); // Default sound of system
                        JOptionPane.showMessageDialog(null, error);
                    });
                } else {
                    // Remember images that we read
                    while (i < l) {
                        BufferedImage bi = null;
                        try {
                            bi = ImageIO.read(files[i]);
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                        assert bi != null;
                        int w = bi.getWidth(); // Get image parameters
                        int h = bi.getHeight();
                        int w_ph = (photos_Panel.getWidth() - 30) / 3; // Get width for each image
                        int h_ph = photos_Panel.getHeight() - 50; // Get height for each image
                        int compress = Math.max(w / w_ph + 1, h / h_ph + 1); // Set compress of images
                        w /= compress; // Compress image
                        h /= compress;
                        try {
                            image[i] = bi; // Create array of images to analysis
                            image_icon[i] = Thumbnails.of(bi).size(w, h).asBufferedImage(); // Create array of icons to show them in application
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                        final int final_i = i;
                        // Add images to panel on screen
                        SwingUtilities.invokeLater(() -> {
                            final JLabel l1 = new JLabel(new ImageIcon(image_icon[final_i]), SwingConstants.CENTER); // Create label with image
                            l1.setBorder(solidBorder); // Set border of images
                            photos_Panel.add(l1); // Add images to images panel
                            SwingUtilities.updateComponentTreeUI(container); // Update container
                        });
                        i++;
                    }
                }
            }
            // Check number of images and enable/disable buttons
            if (length != 0 && length <= 3) {
                button_open.setEnabled(false);
                button_start.setEnabled(true);
            }
        });

        // Action if "start" button clicked
        button_start.addActionListener(e -> {
            int k = 0;
            double[] lab0 = {0}; // LAB color arrays
            double[] lab1 = {0};
            double[] lab2 = {0};
            int[] rgb0; // RGB color arrays
            int[] rgb1;
            int[] rgb2;

            // Start loop by images
            while (k < length) {
                int height = image[k].getHeight(); // Get image parameters
                int width = image[k].getWidth();
                Map<Integer, Integer> m = new HashMap<>();
                // Start loop by one image
                for (int i = 0; i < width; i++) {
                    for (int j = 0; j < height; j++) {
                        int rgb = image[k].getRGB(i, j); // Get pixel RGB coordinates
                        int[] rgbArr = getRGBArr(rgb); // Transform coordinates to array to simplify program
                        // If color isn't one of white/gray/black colors
                        if (!isGray(rgbArr)) {
                            Integer counter = m.get(rgb); // Get counter of this color
                            if (counter == null) // If map hasn't this color - create him
                                counter = 0;
                            counter++;
                            m.put(rgb, counter); // Put color in map
                        }
                    }
                }

                // Fill arrays of most common colors
                if (k == 0) {
                    rgb0 = getMostCommonColourLikeArray(m);
                    lab0 = rgb2lab(rgb0[0], rgb0[1], rgb0[2]);
                }
                if (k == 1) {
                    rgb1 = getMostCommonColourLikeArray(m);
                    lab1 = rgb2lab(rgb1[0], rgb1[1], rgb1[2]);
                }
                if (k == 2) {
                    rgb2 = getMostCommonColourLikeArray(m);
                    lab2 = rgb2lab(rgb2[0], rgb2[1], rgb2[2]);
                }
                k++;
            }

            // Calculate the answer depending on the color and number of images
            if (length == 3) {
                color[0] = what(lab0[1], lab0[2]); // Get 0(green) or 1(yellow) or 2(red)
                color[1] = what(lab1[1], lab1[2]);
                color[2] = what(lab2[1], lab2[2]);
                if (color[0] + color[1] + color[2] >= 5) {
                    answer = "Ripe Apple!";
                } else {
                    if (color[0] + color[1] + color[2] >= 2) {
                        answer = "Medium Ripe Apple!";
                    } else
                        answer = "WARNING! Unripe Apple!";
                }
            }
            if (length == 2) {
                color[0] = what(lab0[1], lab0[2]);
                color[1] = what(lab1[1], lab1[2]);
                if (color[0] + color[1] == 4) {
                    answer = "Ripe Apple!";
                } else {
                    if (color[0] + color[1] >= 2) {
                        answer = "Medium Ripe Apple!";
                    } else
                        answer = "WARNING! Unripe Apple!";
                }
            }
            if (length == 1) {
                color[0] = what(lab0[1], lab0[2]);
                if (color[0] == 2) {
                    answer = "Ripe Apple!";
                } else {
                    if (color[0] == 1) {
                        answer = "Medium Ripe Apple!";
                    } else
                        answer = "WARNING! Unripe Apple!";
                }
            }

            // Play sound and show answer
            SwingUtilities.invokeLater(() -> {
                // Set custom sounds
                File sound = new File("voice.wav"); // Set path to sound
                if (answer.equals("Ripe Apple!")) {
                    sound = new File("voice3.wav");
                }
                if (answer.equals("Medium Ripe Apple!")) {
                    sound = new File("voice2.wav");
                }
                URL url = null;
                if (sound.canRead()) {
                    try {
                        url = sound.toURI().toURL();
                    } catch (MalformedURLException ex) {
                        ex.printStackTrace();
                    }
                }
                AudioClip clip = Applet.newAudioClip(url);
                clip.play(); // Play sound
                button_start.setEnabled(false);
                JOptionPane.showMessageDialog(null, answer); // Show answer
                SwingUtilities.updateComponentTreeUI(container);
                System.exit(0);
            });
        });

        this.setPreferredSize(new Dimension(1000, 500)); // Set size of application frame
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    public static void main(String[] args) throws Exception {
        new application(); // start application
    }

    // Method to get most common color of image
    private static int[] getMostCommonColourLikeArray(Map<Integer, Integer> map) {
        LinkedList<Map.Entry<Integer, Integer>> colorList = new LinkedList<>(map.entrySet());
        colorList.sort((o1, o2) -> ((Comparable<Integer>) o1.getValue()).compareTo(o2.getValue()));
        Map.Entry<Integer, Integer> mapEntry = colorList.get(colorList.size() - 1);
        int[] rgb = getRGBArr(mapEntry.getKey());
        return new int[]{rgb[0], rgb[1], rgb[2]};
    }

    // Method that translate pixel to RGB array
    private static int[] getRGBArr(int pixel) {
        int red = (pixel >> 16) & 0xff;
        int green = (pixel >> 8) & 0xff;
        int blue = (pixel) & 0xff;
        return new int[]{red, green, blue};
    }

    // Method that throws out white/gray/black colors
    private static boolean isGray(int[] rgbArr) {
        int rgDiff = rgbArr[0] - rgbArr[1]; // Difference between red and green coordinate
        int rbDiff = rgbArr[0] - rgbArr[2]; // Difference between red and blue coordinate
        int tolerance = 20; // Acceptable difference
        if (rgDiff > tolerance || rgDiff < -tolerance)
            return rbDiff <= tolerance && rbDiff >= -tolerance;
        return true;
    }

    // Method that convert RGB color to LAB color
    private static double[] rgb2lab(int R, int G, int B) {
        double r, g, b, X, Y, Z, xr, yr, zr, Ls, as, bs;
        // RGB to XYZ
        r = R / 255.0;
        g = G / 255.0;
        b = B / 255.0;
        if (r <= 0.04045)
            r = r / 12.92;
        else
            r = Math.pow((r + 0.055) / 1.055, 2.4);
        if (g <= 0.04045)
            g = g / 12.92;
        else
            g = Math.pow((g + 0.055) / 1.055, 2.4);
        if (b <= 0.04045)
            b = b / 12.92;
        else
            b = Math.pow((b + 0.055) / 1.055, 2.4);
        r *= 100.;
        g *= 100.;
        b *= 100.;
        X = 0.4124 * r + 0.3576 * g + 0.1805 * b;
        Y = 0.2126 * r + 0.7152 * g + 0.0722 * b;
        Z = 0.0193 * r + 0.1192 * g + 0.9505 * b;
        // XYZ to LAB
        xr = X / 95.047;
        yr = Y / 100.000;
        zr = Z / 108.883;
        if (xr > 0.008856)
            xr = Math.pow(xr, (1. / 3.));
        else
            xr = (7.787 * xr) + (16. / 116.);
        if (yr > 0.008856)
            yr = Math.pow(yr, (1. / 3.));
        else
            yr = (7.787 * yr) + (16. / 116.);
        if (zr > 0.008856)
            zr = Math.pow(zr, (1. / 3.));
        else
            zr = (7.787 * zr) + (16. / 116.);
        Ls = (116. * yr) - 16.;
        as = 500. * (xr - yr);
        bs = 200. * (yr - zr);
        // Cast to int type
        Ls = (int) Ls;
        as = (int) as;
        bs = (int) bs;

        return new double[]{Ls, as, bs};
    }

    // Method that by LAB coordinates define green/yellow/red color
    private static int what(double color1, double color2) {
        // Green
        if (color2 <= color1 * (-3) && color2 >= color1 && color1 < 0) {
            return 0;
        }
        // Yellow
        if (color2 >= color1 * 2 && color2 >= color1 * (-3) && color2 > 0) {
            return 1;
        }
        // Red
        if (color2 <= color1 * 2 && color2 >= color1 * (-1) && color1 > 0) {
            return 2;
        }
        return 0;
    }
}
