package graphics;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Texture {

	private String name;
	private int width, height;
	public int[] pixels;
	
	
	
	
	
	
	

	public Texture(String name, String path) {
		this.name = name;
		load(path);
	}

	public Texture(Image i, int w, int h) {
		load(i, w, h);

	}

	public Texture(String name, String path, int w, int h) {
		this.name = name;

		load(path, w, h);
	}

	public Texture(String path, int w, int h) {
		this.name = path;
		load(path, w, h);
	}

	public Texture(int[] pixels, int width, int height) {
		this.pixels = new int[width * height];
		this.width = width;
		this.height = height;

		System.arraycopy(pixels, 0, this.pixels, 0, width * height);
	}

	private void load(Image i, int w, int h) {
		BufferedImage image = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
		Graphics2D g2 = image.createGraphics();
		g2.drawImage(i, 0, 0, w, h, null);
		g2.dispose();

		width = w;
		height = h;
		pixels = new int[width * height];
		image.getRGB(0, 0, width, height, pixels, 0, width);
		img = image;
	}

	private void load(String path) {

		try {
			BufferedImage image = ImageIO.read(this.getClass().getResourceAsStream(path));
			int w = image.getWidth();
			int h = image.getHeight();
			load(path, w, h);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void load(String path, int w, int h) {
		try {
			Image i = ImageIO.read(this.getClass().getResourceAsStream(path));
			// Image i = ImageIO.read(new File(path));
			BufferedImage image = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
			Graphics2D g2 = image.createGraphics();
			g2.drawImage(i, 0, 0, w, h, null);
			g2.dispose();

			width = w;
			height = h;
			pixels = new int[width * height];
			image.getRGB(0, 0, width, height, pixels, 0, width);
			img = image;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public Image img;

	public String getName() {
		return name;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}
}
