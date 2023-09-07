package collision;

import utils.Size;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;

public class Tile {
    private final BufferedImage content;
    private final Color parseColor;
    private final BoundingBox collision;
    private Size size;

    public Tile(BufferedImage image, Color color, BoundingBox collision, Size size) {
        content = image;
        parseColor = color;
        this.collision = collision;
        this.size = size;
    }

    public BoundingBox getCollision() {
        return collision;
    }

    public Color getColor() {
        return parseColor;
    }

    public void render(Graphics2D g2d, int positionX, int positionY) {
        g2d.drawImage(content, (int) (positionX * size.x), (int) (positionY * size.y), null);
    }
}
