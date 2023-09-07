package utils;

import collision.BoundingBox;
import collision.Tile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class Tiles {
    private static final BoundingBox standartCollision = new BoundingBox(
            new Size(70, 70), new Size(70, 70)
    );
    private static final Size standartSize = new Size(70, 70);
    private static Tile tileLiquidWater = null;

    static {
        try {
            tileLiquidWater = new Tile(ImageIO.read(
                    new File("./assets/Tiles/liquidWaterTop_mid.png")),
                    Color.BLUE, standartCollision, standartSize
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static Tile tileLiquidBlock = null;

    static {
        try {
            tileLiquidBlock = new Tile(ImageIO.read(
                    new File("./assets/Tiles/liquidWater.png")),
                    new Color(0, 150, 255), standartCollision, standartSize
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static Tile tileGrass = null;

    static {
        try {
            tileGrass = new Tile(ImageIO.read(
                    new File("./assets/Tiles/grassMid.png")),
                    new Color(0, 255, 0), standartCollision, standartSize
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static Tile tileDirt = null;

    static {
        try {
            tileDirt = new Tile(ImageIO.read(
                    new File("./assets/Tiles/grassMid.png")),
                    Color.BLACK, standartCollision, standartSize
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Tile[] getTiles() {
        return new Tile[]{tileDirt, tileGrass, tileLiquidBlock, tileLiquidWater};
    }
}
