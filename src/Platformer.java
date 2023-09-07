import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.Serial;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

public class Platformer extends JFrame {
	@Serial
	private static final long serialVersionUID = 5736902251450559962L;
	private static final boolean DEBUG = true;
	private int scrollX = 0;
	private static final int SCROLL_SPEED = 15;
	private final Level level;
	private BufferedImage currentLevelImage;
	private Player player;

	BufferedImage levelImg;
	public Platformer() {
		//exit program when window is closed
		this.addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e){
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

			this.setBounds(0, 0, 1000, currentLevelImage.getHeight());
			this.setResizable(false);
			this.setVisible(true);

			this.addKeyListener(new KeyAdapter() {
				@Override
				public void keyPressed(KeyEvent e) {
					int levelWidth = currentLevelImage.getWidth();
					int levelHeight = currentLevelImage.getHeight();
					/*int maxScrollX = levelWidth - getWidth();
					if (e.getKeyCode() == KeyEvent.VK_LEFT) {
						if (scrollX - SCROLL_SPEED >= 0)
							scrollX -= SCROLL_SPEED;
						else
							scrollX = 0;
					} else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
						if (scrollX + SCROLL_SPEED <= maxScrollX)
							scrollX += SCROLL_SPEED;
						else
							scrollX = maxScrollX;
					}
					repaint();*/

					if (e.getKeyCode() == KeyEvent.VK_LEFT) {
						// check if player moves out of map
						if (player.getPosition().x - player.getSpeed() > 0)
							player.move(-1, 0).forEach(i -> repaint()); // is not out of map after next move
						else if (player.getPosition().x > 0)
							player.move(-1, 0, player.getPosition().x).forEach(i -> repaint());
					} else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
						// check if player moves out of map
						if (player.getPosition().x + player.getSpeed() <= levelWidth)
							player.move(1, 0).forEach(i -> repaint());
						else if (player.getPosition().x < levelWidth)
							player.move(-1, 0, levelWidth - player.getPosition().x).forEach(i -> repaint());
					} else if (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_SPACE) {
						// check if player moves out of map
						if (player.getPosition().y + player.getSpeed() <= levelWidth)
							player.move(0, 1).forEach(i -> repaint());
						else if (player.getPosition().y < levelHeight)
							player.move(0, 1, levelHeight - player.getPosition().y).forEach(i -> repaint());
					}
				}
			});
		} catch (IOException e) {
			System.out.println("Level could not be loaded");
			throw new RuntimeException(e);
		}
	}
	/*
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
		int maxScrollWidth = currentLevelImage.getWidth() - getWidth();
		int centerXPositionOfPlayer = (int) absolutePlayerPosition.x + (getWidth() / 2);
		if (centerXPositionOfPlayer < 0)
			centerXPositionOfPlayer = 0;
		else if (centerXPositionOfPlayer > currentLevelImage.getWidth())
			centerXPositionOfPlayer = maxScrollWidth;
		System.out.println("camera x: " + centerXPositionOfPlayer);
		System.out.println("player x: " + absolutePlayerPosition.x);

		BufferedImage bi = currentLevelImage.getSubimage(centerXPositionOfPlayer, 0, 1000, currentLevelImage.getHeight());
		g2d.drawImage(bi, 0, 0, this);
		g2d.drawImage(player.getImage(), (int) absolutePlayerPosition.x, (int) absolutePlayerPosition.y, this);

		// g2d.drawImage(currentLevelImage, -scrollX, 0, null);
		// add player to level
		//Point.Float absolutePlayerPosition = player.transformToAbsolutePosition(currentLevelImage.getHeight());
		//g2d.drawImage(player.getImage(), (int) absolutePlayerPosition.x, (int) absolutePlayerPosition.y, null);
		g2d.dispose();
	}
}

	 */

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
		int maxScrollWidth = currentLevelImage.getWidth() - getWidth();
		int centerXPositionOfPlayer = (int) absolutePlayerPosition.x + (getWidth() / 2);
		if (centerXPositionOfPlayer < 0)
			centerXPositionOfPlayer = 0;
		else if (centerXPositionOfPlayer > currentLevelImage.getWidth())
			centerXPositionOfPlayer = maxScrollWidth;
		if(!(absolutePlayerPosition.x > maxScrollWidth)){
			if(absolutePlayerPosition.x > (float) getWidth() / 2) {
				absolutePlayerPosition.x = (int)((float) getWidth() / 2);
			}
		}

		System.out.println("camera x: " + centerXPositionOfPlayer);
		System.out.println("player x: " + absolutePlayerPosition.x);

		BufferedImage bi = currentLevelImage.getSubimage(centerXPositionOfPlayer, 0, 1000, currentLevelImage.getHeight());
		g2d.drawImage(bi, 0, 0, this);
		g2d.drawImage(player.getImage(), (int) absolutePlayerPosition.x, (int) absolutePlayerPosition.y, this);

		// g2d.drawImage(currentLevelImage, -scrollX, 0, null);
		// add player to level
		//Point.Float absolutePlayerPosition = player.transformToAbsolutePosition(currentLevelImage.getHeight());
		//g2d.drawImage(player.getImage(), (int) absolutePlayerPosition.x, (int) absolutePlayerPosition.y, null);
		g2d.dispose();
	}
}