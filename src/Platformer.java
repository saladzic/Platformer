import java.awt.Graphics;
import java.awt.Graphics2D;
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
	private Level level;
	private BufferedImage currentLevelImage;

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
			selectedFile = new File("./level2.bmp");
		}

		try {
			levelImg = ImageIO.read(selectedFile);
			level = new Level(levelImg);

			this.setBounds(0, 0, 1000, 350);
			this.setResizable(false);
			this.setVisible(true);

			this.addKeyListener(new KeyAdapter() {
				@Override
				public void keyPressed(KeyEvent e) {
					int maxScrollX = currentLevelImage.getWidth() - getWidth();
					if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
						if (scrollX + SCROLL_SPEED <= maxScrollX)
							scrollX += SCROLL_SPEED;
						else
							scrollX = maxScrollX;
						repaint();
					} else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
						if (scrollX - SCROLL_SPEED >= 0)
							scrollX -= SCROLL_SPEED;
						else
							scrollX = 0;
						repaint();
					}
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void paint(Graphics g) {
		Graphics2D g2d = (Graphics2D)g;
		currentLevelImage = level.getLevelImage();
		try {
			ImageIO.write(currentLevelImage, "jpg", new File("./debug.jpg"));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		this.setBounds(0, 0, 1000, currentLevelImage.getHeight());
		g2d.drawImage(currentLevelImage, -scrollX, 0, null);
	}
}
