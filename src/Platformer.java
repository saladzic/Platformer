import collision.Tile;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.Serial;

public class Platformer extends JFrame {
	@Serial
	private static final long serialVersionUID = 5736902251450559962L;
	private static final boolean DEBUG = true;
	private int offsetX;
	private final int maxOffsetX;
	private final Level level;
	private BufferedImage currentLevelImage;
	private final Player player;

	BufferedImage levelImg;

	public Platformer() {
		//exit program when window is closed
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});

		File selectedFile;
		if (!DEBUG) {
			JFileChooser fc = new JFileChooser();
			fc.setCurrentDirectory(new File("./"));
			fc.setDialogTitle("Select input image");
			FileFilter filter = new FileNameExtensionFilter("Level image (.bmp)", "bmp");
			fc.setFileFilter(filter);
			int result = fc.showOpenDialog(this);
			selectedFile = new File("");

			if (result == JFileChooser.APPROVE_OPTION) {
				selectedFile = fc.getSelectedFile();
				System.out.println("Selected file: " + selectedFile.getAbsolutePath());
			} else {
				dispose();
				System.exit(0);
			}
		} else {
			selectedFile = new File("./level1.bmp");
		}

		try {
			levelImg = ImageIO.read(selectedFile);
			level = new Level(levelImg);
			player = new Player();
			currentLevelImage = level.getLevelImage();
			offsetX = 0;
			maxOffsetX = currentLevelImage.getWidth() - 1000;

			Tile[][] levelParts = level.getLevelParts();

			this.setBounds(0, 0, 1000, currentLevelImage.getHeight());
			this.setResizable(false);
			this.setVisible(true);

			this.addKeyListener(new KeyAdapter() {
				@Override
				public void keyPressed(KeyEvent e) {
					int levelWidth = currentLevelImage.getWidth();
					int levelHeight = currentLevelImage.getHeight();

					if (e.getKeyCode() == KeyEvent.VK_LEFT) {
						// check if player moves out of map
						if (player.getPosition().x - player.getSpeed() > 0)
							player.move(-1, 0, levelParts).forEach(i -> repaint()); // is not out of map after next move
						else if (player.getPosition().x > 0)
							player.move(-1, 0, player.getPosition().x, levelParts).forEach(i -> repaint());
					} else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
						// check if player moves out of map
						if (player.getPosition().x + player.getPlayerWidth() + player.getSpeed() <= levelWidth)
							player.move(1, 0, levelParts).forEach(i -> repaint());
						else if (player.getPosition().x + player.getPlayerWidth() < levelWidth)
							player.move(1, 0, levelWidth - player.getPosition().x - player.getPlayerWidth(), levelParts).forEach(i -> repaint());
					} else if (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_SPACE) {
						// check if player moves out of map
						if (player.getPosition().y + player.getSpeed() <= levelWidth)
							player.move(0, 1, levelParts).forEach(i -> repaint());
						else if (player.getPosition().y < levelHeight)
							player.move(0, 1, levelHeight - player.getPosition().y, levelParts).forEach(i -> repaint());
					}
				}
			});
		} catch (IOException e) {
			System.out.println("Level could not be loaded");
			throw new RuntimeException(e);
		}
	}

	@Override
	public void paint(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		currentLevelImage = level.getLevelImage();
		try {
			ImageIO.write(currentLevelImage, "jpg", new File("./debug.jpg"));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		Point.Float absolutePlayerPosition = player.transformToAbsolutePosition(currentLevelImage.getHeight());

		Graphics2D levelGraphics2D = currentLevelImage.createGraphics();
		levelGraphics2D.drawImage(player.getImage(), (int) absolutePlayerPosition.x, (int) absolutePlayerPosition.y, this);

		offsetX = (int) absolutePlayerPosition.x - 500;
		if (offsetX < 0) offsetX = 0;
		else if (offsetX > maxOffsetX) offsetX = maxOffsetX;

		BufferedImage currentLevelFrame = currentLevelImage.getSubimage(
				offsetX,
				0,
				1000,
				currentLevelImage.getHeight()
		);

		g2d.drawImage(currentLevelFrame, 0, 0, this);
		g2d.dispose();
	}
}