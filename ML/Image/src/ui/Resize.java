package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

public class Resize extends JDialog {

    private final BufferedImage image;
    private BufferedImage croppedImage;
    private final Rectangle cropRect = new Rectangle(50, 50, 400, 300);
    private Point lastPoint;
    private boolean resizing = false;
    private final int cornerSize = 10;

    public Resize(JFrame parent, BufferedImage image) {
        super(parent, "Resize", true);
        this.image = image;

        setSize(800, 600);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        JPanel imagePanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(image, 0, 0, null);
                g.setColor(new Color(255, 255, 255, 150));
                g.fillRect(cropRect.x, cropRect.y, cropRect.width, cropRect.height);

                int numLines = 3;
                for (int i = 1; i <= numLines; i++) {
                    int x = cropRect.x + (cropRect.width * i) / (numLines + 1);
                    int y = cropRect.y + (cropRect.height * i) / (numLines + 1);
                    g.drawLine(x, cropRect.y, x, cropRect.y + cropRect.height);
                    g.drawLine(cropRect.x, y, cropRect.x + cropRect.width, y);
                }
            }
        };

        MouseAdapter adapter = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                lastPoint = e.getPoint();
                if (lastPoint.distance(cropRect.getMaxX(), cropRect.getMaxY()) < cornerSize) {
                    resizing = true;
                }
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                int deltaX = e.getX() - lastPoint.x;
                int deltaY = e.getY() - lastPoint.y;

                if (resizing) {
                    cropRect.setSize(cropRect.width + deltaX, cropRect.height + deltaY);
                } else if (cropRect.contains(lastPoint)) {
                    cropRect.translate(deltaX, deltaY);
                }

                lastPoint = e.getPoint();
                imagePanel.repaint();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                resizing = false;
            }
        };

        imagePanel.addMouseListener(adapter);
        imagePanel.addMouseMotionListener(adapter);
        add(imagePanel, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel();
        JButton confirmButton = new JButton("Confirm");
        confirmButton.addActionListener(e -> {
            croppedImage = image.getSubimage(cropRect.x, cropRect.y, cropRect.width, cropRect.height);
            setVisible(false);
        });
        bottomPanel.add(confirmButton);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    public BufferedImage getCroppedImage() {
        return croppedImage;
    }
}
