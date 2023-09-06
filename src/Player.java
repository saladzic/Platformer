import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Player {
    private List<BufferedImage> animation;

    public Player() {
        animation = new ArrayList<>();
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

    public void move(int x) {

    }
}
