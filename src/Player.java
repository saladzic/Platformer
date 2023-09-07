import collision.BoundingBox;
import collision.Tile;
import utils.Size;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Player {
    private final List<BufferedImage> animation;
    private BoundingBox collision;
    private int currentState;
    private float positionX;
    private float positionY;
    private boolean facingLeft;
    private final float movementSpeed;
    private final Size playerSize = new Size(70, 70);

    public Player() {
        collision = new BoundingBox(new Size(0, 0), playerSize);
        animation = new ArrayList<>();
        currentState = 0;
        positionX = 0f;
        positionY = 0f;
        facingLeft = false;
        movementSpeed = 15f;
        try {
            Files.list(Paths.get("assets/Player/p1_walk/PNG"))
                    .forEach(p -> {
                        try {
                            animation.add(ImageIO.read(new File(p.toString())));
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public List<BufferedImage> move(int deltaX, int deltaY, Tile[][] levelParts) {
        return move(deltaX, deltaY, movementSpeed, levelParts);
    }

    public List<BufferedImage> move(int deltaX, int deltaY, float speed, Tile[][] levelParts) {
        currentState++;

        if (currentState >= animation.size())
            currentState = 0;
        if (deltaX < 0) {
            facingLeft = true;

            BoundingBox futureCollision = new BoundingBox(
                    new Size(positionY, positionX - speed),
                    new Size(positionY + playerSize.y, positionX - speed + playerSize.x)
            );

            Tile collisionWith = checkCollision(levelParts, futureCollision);
            if (Objects.isNull(collisionWith)) {
                positionX = positionX - speed;
            } else {
                positionX = collisionWith.getCollision().min.x - 1;
            }

        } else if (deltaX > 0) {
            facingLeft = true;

            BoundingBox futureCollision = new BoundingBox(
                    new Size(positionY, positionX + speed),
                    new Size(positionY + playerSize.y, positionX - speed + playerSize.x)
            );

            Tile collisionWith = checkCollision(levelParts, futureCollision);
            if (Objects.isNull(collisionWith)) {
                positionX = positionX + speed;
            } else {
                positionX = collisionWith.getCollision().max.x - 1;
            }

        }

        if (deltaY < 0) {
            positionY = positionY - speed;
        } else if (deltaY > 0) {
            positionY = positionY + speed;
        }
        return animation;
    }

    private Tile checkCollision(Tile[][] levelParts, BoundingBox collision) {
        int test = (int) Math.ceil(((double) positionX) / 1000);
        for (Tile tile : levelParts[(int) Math.ceil(((double) positionX) / 1000)]) {
            if (!(Objects.isNull(tile)) && collision.intersect(tile.getCollision()))
                return tile;
        }
        return null;
    }

    public BufferedImage getImage() {
        BufferedImage b = animation.get(currentState);
        if (facingLeft) {
            AffineTransform tx = AffineTransform.getScaleInstance(-1, 1);
            tx.translate(-b.getWidth(null), 0);
            AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
            b = op.filter(b, null);
        }
        return b;
    }

    public Point.Float getPosition() {
        return new Point2D.Float(positionX, positionY);
    }

    public Point.Float transformToAbsolutePosition(int levelHeight) {
        return new Point2D.Float(positionX, levelHeight - positionY - animation.get(0).getHeight());
    }

    public float getSpeed() {
        return movementSpeed;
    }

    public int getPlayerWidth() {
        return animation.get(0).getWidth();
    }
}
