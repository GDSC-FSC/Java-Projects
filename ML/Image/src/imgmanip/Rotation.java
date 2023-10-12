package imgmanip;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class Rotation implements ImageManipulator {

    private final double angle;

    public Rotation(double angle) {
        this.angle = angle;
    }

    @Override
    public BufferedImage manipulate(BufferedImage original) {
        int width = original.getWidth();
        int height = original.getHeight();

        BufferedImage rotated = new BufferedImage(width, height, original.getType());
        Graphics2D g2d = rotated.createGraphics();

        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        AffineTransform at = new AffineTransform();
        at.translate(width / 2.0, height / 2.0);
        at.rotate(Math.toRadians(angle));
        at.translate(-width / 2.0, -height / 2.0);

        g2d.setTransform(at);
        g2d.drawImage(original, 0, 0, null);
        g2d.dispose();

        return rotated;
    }
}
