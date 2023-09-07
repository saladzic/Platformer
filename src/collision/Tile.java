package collision;

import utils.Size;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;

public class Tile {
    private final BufferedImage content;
    private final Color parseColor;
    private BoundingBox collision = null;
    private Size size;

    public Tile(BufferedImage image, Color color, Size size) {
        content = image;
        parseColor = color;
        this.size = size;
    }

    public void render(Graphics2D g2d, int positionX, int positionY) {
        g2d.drawImage(content, (int) (positionX * size.x), (int) (positionY * size.y), null);
    }

    public BufferedImage getContent() {
        return content;
    }

    public BoundingBox getCollision() {
        return collision;
    }

    public Color getColor() {
        return parseColor;
    }

    public void setCollision(int minPosX, int minPosY) {
        int maxPosX = 1 + minPosX; // As 4loop starts with 0
        int maxPosY = 1 + minPosY;
        collision = new BoundingBox(
                new Size(minPosY, minPosX), new Size(minPosY + size.y,minPosX + size.x)
        );
    }
}
