import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Background {
    private static BufferedImage scaleImage(BufferedImage originalImage, int height) {
        double scaleFactor = (double) height / originalImage.getHeight();
        int newWidth = (int) (originalImage.getWidth() * scaleFactor);
        int newHeight = height;
        // scale the image
        BufferedImage scaledImage = new BufferedImage(newWidth, newHeight, originalImage.getType());
        Graphics2D g2d = scaledImage.createGraphics();
        g2d.drawImage(originalImage, 0, 0, newWidth, newHeight, null);
        g2d.dispose();
        return scaledImage;
    }

    private static BufferedImage mirrorImage(BufferedImage image) {
        // create transformation for image to mirrored image
        AffineTransform at = new AffineTransform();
        at.concatenate(AffineTransform.getScaleInstance(-1, 1));
        // mirror image
        BufferedImage mirroredImage = new BufferedImage(image.getWidth(), image.getHeight(),
                BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = mirroredImage.createGraphics();
        g2d.transform(at);
        g2d.drawImage(image, -image.getWidth(), 0, null);
        g2d.dispose();
        return mirroredImage;
    }

    public static BufferedImage loadBackgroundImage(int width, int height) {
        try {
            // load background image
            BufferedImage originalImage = ImageIO.read(new File("./assets/background0.png"));
            BufferedImage scaledImage = scaleImage(originalImage, height);
            BufferedImage mirroredImage = mirrorImage(scaledImage);

            // create background image
            BufferedImage background = new BufferedImage(
                    width,
                    height,
                    BufferedImage.TYPE_INT_RGB
            );
            Graphics2D g2d = background.createGraphics();

            // fill the background image with order
            int widthIndex = 0;
            boolean mirrored = false;
            while (widthIndex < width) {
                if (mirrored) {
                    g2d.drawImage(mirroredImage, widthIndex, 0, null);
                } else {
                    g2d.drawImage(scaledImage, widthIndex, 0, null);
                }
                mirrored = !mirrored;
                widthIndex += scaledImage.getWidth();
            }
            g2d.dispose();
            return background;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
