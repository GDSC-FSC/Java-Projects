package ui;

import imgmanip.BlackAndWhite;
import imgmanip.ImageManipulator;
import imgmanip.Inversion;
import imgmanip.Rotation;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageManipulatorUI {
    private static final int FRAME_WIDTH = 800;
    private static final int FRAME_HEIGHT = 600;

    private final JFrame frame;
    private BufferedImage loadedImage, originalImage;
    private final JLabel imageLabel;
    private final JPanel manipButtonsPanel;
    private JSlider rotationSlider;
    private final JScrollPane scrollPane;
    private boolean isBlackAndWhite = false;

    public ImageManipulatorUI() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        frame = new JFrame("Image Manipulator");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        JButton loadImageButton = new JButton("Load Image");
        loadImageButton.addActionListener(e -> loadImage());

        imageLabel = new JLabel();
        manipButtonsPanel = createManipulationPanel();

        frame.add(loadImageButton, BorderLayout.NORTH);
        frame.add(imageLabel, BorderLayout.CENTER);
        frame.add(manipButtonsPanel, BorderLayout.SOUTH);

        manipButtonsPanel.setVisible(false);
        frame.setSize(FRAME_WIDTH, FRAME_HEIGHT);

        scrollPane = new JScrollPane(imageLabel);
        frame.add(scrollPane, BorderLayout.CENTER);
        frame.setLocation(0, 0);

        frame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ImageManipulatorUI::new);
    }

    private JPanel createManipulationPanel() {
        JPanel panel = new JPanel();

        JButton invertButton = new JButton("Invert");
        invertButton.addActionListener(e -> manipulateImage(new Inversion()));
        panel.add(invertButton);

        JButton blackAndWhiteButton = new JButton("Black and White");
        blackAndWhiteButton.addActionListener(e -> {
            if (!isBlackAndWhite) {
                loadedImage = new BlackAndWhite().manipulate(originalImage);
            } else {
                loadedImage = originalImage;
            }
            isBlackAndWhite = !isBlackAndWhite;
            displayImage(loadedImage);
        });
        panel.add(blackAndWhiteButton);

        rotationSlider = new JSlider(0, 360, 0);
        rotationSlider.addChangeListener(e -> {
            if (originalImage != null) {
                loadedImage = new Rotation(rotationSlider.getValue()).manipulate(originalImage);
                displayImage(loadedImage);
            }
        });
        panel.add(rotationSlider);

        JButton saveImageButton = new JButton("Save Image");
        saveImageButton.addActionListener(e -> saveImage());
        panel.add(saveImageButton);

        JButton resizeButton = new JButton("Resize");
        resizeButton.addActionListener(e -> {
            Resize dialog = new Resize(frame, originalImage);
            dialog.setVisible(true);
            BufferedImage cropped = dialog.getCroppedImage();
            if (cropped != null) {
                loadedImage = cropped;
                displayImage(loadedImage);
            }
        });
        panel.add(resizeButton);

        return panel;
    }

    private void loadImage() {
        JFileChooser chooser = new JFileChooser();
        int returnValue = chooser.showOpenDialog(frame);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();
            try {
                originalImage = ImageIO.read(file);
                loadedImage = originalImage;
                displayImage(loadedImage);
                manipButtonsPanel.setVisible(true);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void manipulateImage(ImageManipulator manipulator) {
        if (loadedImage != null) {
            loadedImage = manipulator.manipulate(loadedImage);
            displayImage(loadedImage);
        }
    }

    private void displayImage(BufferedImage img) {
        if (img != null) {
            ImageIcon icon = new ImageIcon(img);
            imageLabel.setIcon(icon);
        } else {
            JOptionPane.showMessageDialog(frame, "Failed to load the image.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void saveImage() {
        if (loadedImage != null) {
            JFileChooser chooser = new JFileChooser();
            chooser.setDialogTitle("Save Image");
            int userSelection = chooser.showSaveDialog(frame);

            if (userSelection == JFileChooser.APPROVE_OPTION) {
                File fileToSave = chooser.getSelectedFile();
                try {
                    String filename = fileToSave.getName();
                    String extension = filename.contains(".") ? filename.substring(filename.lastIndexOf(".") + 1) : "png";
                    ImageIO.write(loadedImage, extension, fileToSave);
                } catch (IOException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(frame, "Failed to save the image.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }
}
