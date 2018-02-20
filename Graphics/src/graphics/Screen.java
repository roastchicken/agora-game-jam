package graphics;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.ArrayList;
import java.util.List;

public class Screen {

	private int width, height;
	private BufferStrategy bs;
	private Graphics g;
	private BufferedImage image;
	int[] pixels;
	int interpolation;

	private final Font DEFAULT_FONT = new Font("Comic Sans MS", Font.PLAIN, 24);
	private final Color DEFAULT_COLOR = Color.WHITE;

	private class RenderableString {

		String string;
		int x, y;
		Font font;
		Color color;

		RenderableString(String string, int x, int y, Font font, Color color) {
			this.string = string;
			this.x = x;
			this.y = y;
			this.font = font;
			this.color = color;
		}
	}

	private List<RenderableString> stringBuffer = new ArrayList<RenderableString>();

	public Screen(int width, int height, BufferStrategy bs) {
		this.width = width;
		this.height = height;
		this.bs = bs;

		image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
	}

	public void clear() {
		for (int i = 0; i < width * height; i++) {
			pixels[i] = 0;
		}
	}

	public void clear(int color) {
		for (int i = 0; i < width * height; i++) {
			pixels[i] = color;
		}
	}

	int lx = 500;
	int ly = 500;

	@SuppressWarnings("unused")
	private int clamp(int value, int min, int max) {
		if (value < min)
			value = min;
		if (value > max)
			value = max;
		return value;
	}

	public void drawRect(int x, int y, int width, int height, int color) {
		for (int yy = y; yy < y + height; yy++) {
			if (yy < 0 || yy >= this.height)
				continue;
			if (x > 0 && x < this.width)
				pixels[x + yy * this.width] = color;
			if ((x + width) < this.width)
				pixels[x + width + yy * this.width] = color;
		}

		for (int xx = x; xx < x + width; xx++) {
			if (xx < 0 || xx >= this.width || y < 0 || y >= this.height)
				continue;
			pixels[xx + y * this.width] = color;
			if ((y + height) < this.height)
				pixels[xx + (y + height) * this.width] = color;
		}
		if ((x + width) > 0 && (y + height) > 0 && (y + height) < this.height && ((x + width) < this.width))
			pixels[x + width + (y + height) * this.width] = color;
	}

	public void fillRectBlend(int x, int y, int width, int height, int color) {
		for (int yy = y; yy < y + height; yy++) {
			for (int xx = x; xx < x + width; xx++) {
				if (xx < 0 || yy < 0 || xx >= this.width || yy >= this.height)
					continue;
				int pixel = color;

				if (pixels[xx + yy * this.width] == 0) {
					pixels[xx + yy * this.width] = pixel;
					continue;
				}
				int finalColor = blend(pixels[xx + yy * this.width], color);
				pixels[xx + yy * this.width] = finalColor;
			}
		}
	}

	public void fillRect(int x, int y, int width, int height, int color) {
		for (int yy = y; yy < y + height; yy++) {
			for (int xx = x; xx < x + width; xx++) {
				if (xx < 0 || yy < 0 || xx >= this.width || yy >= this.height)
					continue;
				int pixel = color;

				if (pixels[xx + yy * this.width] == 0) {
					pixels[xx + yy * this.width] = pixel;
					continue;
				}
				pixels[xx + yy * this.width] = color;
			}
		}
	}

	private int blend(int baseColor, int topColor) {
		float br = ((baseColor & 0xff0000) >> 16) / 255.0f;
		float bg = ((baseColor & 0xff00) >> 8) / 255.0f;
		float bb = (baseColor & 0xff) / 255.0f;

		float tr = ((topColor & 0xff0000) >> 16) / 255.0f;
		float tg = ((topColor & 0xff00) >> 8) / 255.0f;
		float tb = (topColor & 0xff) / 255.0f;

		float fr = 0.0f;
		if (br < 0.5f)
			fr = 2.0f * br * tr;
		else
			fr = 1 - 2 * (1 - br) * (1 - tr);

		float fg = 0.0f;
		if (bg < 0.5f)
			fg = 2.0f * bg * tg;
		else
			fg = 1 - 2 * (1 - bg) * (1 - tg);

		float fb = 0.0f;
		if (bb < 0.5f)
			fb = 2.0f * bb * tb;
		else
			fb = 1 - 2 * (1 - bb) * (1 - tb);

		int finalColor = (int) (fr * 255.0f) << 16 | (int) (fg * 255.0f) << 8 | (int) (fb * 255.0f);
		return finalColor;
	}

	public void drawString(String string, int x, int y) {
		stringBuffer.add(new RenderableString(string, x, y, DEFAULT_FONT, DEFAULT_COLOR));
	}

	public void drawString(String string, int x, int y, Font font, Color color) {
		stringBuffer.add(new RenderableString(string, x, y, font, color));
	}

	public void drawString(String string, Rectangle rect) {
		FontMetrics metrics = g.getFontMetrics(DEFAULT_FONT);
		int tx = rect.x + (rect.width - metrics.stringWidth(string)) / 2;
		int ty = rect.y + ((rect.height - metrics.getHeight()) / 2) + metrics.getAscent();
		stringBuffer.add(new RenderableString(string, tx, ty, DEFAULT_FONT, DEFAULT_COLOR));
	}

	public void drawString(String string, Font font, Color color, Rectangle rect) {
		if (font == null)
			font = DEFAULT_FONT;
		if (color == null)
			color = DEFAULT_COLOR;
		FontMetrics metrics = g.getFontMetrics(font);
		int tx = rect.x + (rect.width - metrics.stringWidth(string)) / 2;
		int ty = rect.y + ((rect.height - metrics.getHeight()) / 2) + metrics.getAscent();
		stringBuffer.add(new RenderableString(string, tx, ty, font, color));
	}

	public void drawString(String string, Font font, Color color, Rectangle rect, int y) {
		if (font == null)
			font = DEFAULT_FONT;
		if (color == null)
			color = DEFAULT_COLOR;
		FontMetrics metrics = g.getFontMetrics(font);
		int tx = rect.x + (rect.width - metrics.stringWidth(string)) / 2;
		int ty = rect.y + ((rect.height - metrics.getHeight()) / 2) + metrics.getAscent();
		stringBuffer.add(new RenderableString(string, tx, ty + y, font, color));
	}

	public void drawTexture(int x, int y, Texture texture) {
		drawTexture(x, y, texture, false);
	}

	public void drawTexture(int x, int y, Texture texture, boolean flipX) {
		for (int yy = 0; yy < texture.getHeight(); yy++) {
			int yo = y + yy;
			for (int xx = 0; xx < texture.getWidth(); xx++) {
				int xo = x + xx;
				// Bound checking goes here!
				if (yo >= height || xo >= width || xo < 0 || yo < 0)
					continue;

				// Flip x if needed
				int xf = xx;
				if (flipX)
					xf = texture.getWidth() - xx - 1;

				int color = texture.pixels[xf + yy * texture.getWidth()];
				if (color == 0xffff00ff)
					continue;
				pixels[xo + yo * this.width] = color;
			}
		}
	}

	private void renderStrings() {
		for (RenderableString string : stringBuffer) {
			g.setColor(string.color);
			g.setFont(string.font);
			g.drawString(string.string, string.x, string.y);

		}
		stringBuffer.clear();
	}

	public void flush() {
		g = bs.getDrawGraphics();
		g.drawImage(image, 0, 0, width, height, null);
		renderStrings();
		g.dispose();
		bs.show();
	}
}
