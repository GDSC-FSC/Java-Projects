package imgmanip;

import java.awt.image.BufferedImage;

public class BlackAndWhite implements ImageManipulator {

    @Override
    public BufferedImage manipulate(BufferedImage original) {
        int width = original.getWidth();
        int height = original.getHeight();
        BufferedImage blackAndWhite = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int rgb = original.getRGB(x, y);
                int alpha = (rgb >> 24) & 0xff;
                int red = (rgb >> 16) & 0xff;
                int green = (rgb >> 8) & 0xff;
                int blue = rgb & 0xff;
                int luminance = (int) (0.299 * red + 0.587 * green + 0.114 * blue);
                int grayscale = (alpha << 24) | (luminance << 16) | (luminance << 8) | luminance;
                blackAndWhite.setRGB(x, y, grayscale);
            }
        }

        return blackAndWhite;
    }
}
