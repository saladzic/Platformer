import collision.Tile;
import utils.Tiles;

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
    private int renderWidth;
    private int renderHeight;
    private Tile[][] levelParts;

    public Level(BufferedImage levelImage) {
        this.levelImage = levelImage;

        renderWidth = levelImage.getWidth() * Tiles.tileLiquidWater.getContent().getWidth();
        renderHeight = levelImage.getHeight() * Tiles.tileLiquidWater.getContent().getHeight();
        backgroundImage = Background.loadBackgroundImage(renderWidth, renderHeight);
        renderImage = new BufferedImage(
                levelImage.getWidth() * Tiles.tileLiquidWater.getContent().getWidth(),
                levelImage.getHeight() * Tiles.tileLiquidWater.getContent().getHeight(),
                BufferedImage.TYPE_INT_RGB
        );

        float partSize = 1000;
        float defaultBlockTileSize = 70;
        levelParts = new Tile[(int) Math.ceil(((double) levelImage.getWidth()) / partSize)][500];
    }

    private void render() {
        int width = levelImage.getWidth();
        int height = levelImage.getHeight();

        Graphics2D g2d = renderImage.createGraphics();
        g2d.drawImage(backgroundImage, 0, 0, null);

        Tile[] tiles = Tiles.getTiles();
        for (int x = 0; x < width; x++) {
            int tilesCounter = 0;
            for (int y = 0; y < height; y++) {
                Color pixelColor = new Color(levelImage.getRGB(x, y));
                if (pixelColor.equals(Color.WHITE))
                    continue;

                for (Tile tile : tiles) {
                    if (pixelColor.equals(tile.getColor())) {
                        levelParts[(int) Math.floor((double) x / 1000)][tilesCounter] = tile;
                        tile.render(g2d, x, y);
                        tile.setCollision(x, y);
                        tilesCounter++;
                    }
                }
            }
        }

        g2d.dispose();
    }

    public BufferedImage getLevelImage() {
        render();
        return renderImage;
    }

    public Tile[][] getLevelParts() {
        return levelParts;
    }
}
