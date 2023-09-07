import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Level {
    private BufferedImage levelImage;
    private BufferedImage renderImage;
    private BufferedImage backgroundImage;
    private BufferedImage tileLiquidWater;
    private BufferedImage tileLiquidBlock;
    private BufferedImage tileGrass;
    private BufferedImage tileDirt;
    private int renderWidth;
    private int renderHeight;

    public Level(BufferedImage levelImage) {
        try {
            this.levelImage = levelImage;
            tileLiquidWater = ImageIO.read(new File("./assets/Tiles/liquidWaterTop_mid.png"));
            tileLiquidBlock = ImageIO.read(new File("./assets/Tiles/liquidWater.png"));
            tileGrass = ImageIO.read(new File("./assets/Tiles/grassMid.png"));
            tileDirt = ImageIO.read(new File("./assets/Tiles/grassCenter.png"));
            renderWidth = levelImage.getWidth() * tileLiquidWater.getWidth();
            renderHeight = levelImage.getHeight() * tileLiquidWater.getHeight();
            backgroundImage = Background.loadBackgroundImage(renderWidth, renderHeight);
            renderImage = new BufferedImage(
                    levelImage.getWidth() * tileLiquidWater.getWidth(),
                    levelImage.getHeight() * tileLiquidWater.getHeight(),
                    BufferedImage.TYPE_INT_RGB
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void render() {
        int width = levelImage.getWidth();
        int height = levelImage.getHeight();

        Graphics2D g2d = renderImage.createGraphics();
        g2d.drawImage(backgroundImage, 0, 0, null);

        int tileWidth = tileLiquidWater.getWidth();
        int tileHeight = tileLiquidWater.getHeight();
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                Color pixelColor = new Color(levelImage.getRGB(x, y));
                if (pixelColor.equals(Color.BLUE)) {
                    // Set water block
                    g2d.drawImage(tileLiquidBlock, x * tileWidth, y * tileHeight, null);
                } else if (pixelColor.equals(new Color(0, 150, 255))) {
                    // Set water top block
                    g2d.drawImage(tileLiquidWater, x * tileWidth, y * tileHeight, null);
                } else if (pixelColor.equals(new Color(0, 255, 0))) {
                    // Set tileGrass
                    g2d.drawImage(tileDirt, x * tileWidth, y * tileHeight, null);
                } else if (pixelColor.equals(Color.BLACK)) {
                    // Set tileGrass
                    g2d.drawImage(tileGrass, x * tileWidth, y * tileHeight, null);
                }
            }
        }
        g2d.dispose();
    }

    public BufferedImage getLevelImage() {
        render();
        return renderImage;
    }
}
