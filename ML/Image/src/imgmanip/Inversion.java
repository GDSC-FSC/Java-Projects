package imgmanip;

import java.awt.image.BufferedImage;

public class Inversion implements ImageManipulator {

    @Override
    public BufferedImage manipulate(BufferedImage original) {
        int width = original.getWidth();
        int height = original.getHeight();
        BufferedImage inverted = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int rgb = original.getRGB(x, y);
                int alpha = (rgb >> 24) & 0xff;
                int red = (rgb >> 16) & 0xff;
                int green = (rgb >> 8) & 0xff;
                int blue = rgb & 0xff;
                red = 255 - red;
                green = 255 - green;
                blue = 255 - blue;
                int invertedRGB = (alpha << 24) | (red << 16) | (green << 8) | blue;
                inverted.setRGB(x, y, invertedRGB);
            }
        }

        return inverted;
    }
}
