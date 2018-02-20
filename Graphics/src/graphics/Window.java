package graphics;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.image.BufferStrategy;

import javax.swing.JFrame;

public class Window extends Canvas {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int width, height;

	private BufferStrategy bs;
	private JFrame frame;
	private Screen screen;

	/**
	 * Creates a new blank window
	 * 
	 * @param title
	 *            Title of the window
	 * @param width
	 *            Width of the window
	 * @param height
	 *            Height of the window
	 */
	public Window(String title, int width, int height) {
		this.width = width;
		this.height = height;
		setSize(new Dimension(width, height));

		frame = new JFrame(title);
		frame.setResizable(false);
		frame.add(this);
		frame.pack();
		frame.setSize(width, height);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setFocusable(true);

		init();
	}

	private void init() {
		createBufferStrategy(3);
		bs = getBufferStrategy();
		screen = new Screen(width, height, bs);
	}

	/**
	 * Makes window visible
	 * 
	 */
	public void show() {
		frame.setVisible(true);
	}

	/**
	 * Gets the screen of the window
	 * 
	 * @return Screen
	 */
	public Screen getScreen() {
		return screen;
	}

	/**
	 * Sets window size
	 * 
	 * @param width
	 *            Width of window
	 * @param height
	 *            Height of window
	 */
	public void setWindowSize(int width, int height) {
		setSize(width, height);
		frame.pack();
		this.width = width;
		this.height = height;
		init();
	}

	@SuppressWarnings("unused")
	private boolean isValidBuffer() {
		if (bs == null) {
			createBufferStrategy(3);
			return false;
		}
		return true;
	}

	/**
	 * Update graphics
	 */
	public void update() {
		screen.flush();

	}

}
