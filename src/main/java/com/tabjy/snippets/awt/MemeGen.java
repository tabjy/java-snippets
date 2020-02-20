package com.tabjy.snippets.awt;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.net.URL;

public class MemeGen {
	public static void main(String[] args) throws IOException {
		Frame f = new Frame("Canvas Example");

		f.add(new CompositionCanvas());
		f.setLayout(null);
		f.setSize(600, 908);
		f.setVisible(true);
	}

	public static class CompositionCanvas extends Canvas {
		private Image template;
		private boolean painted = false;

		public CompositionCanvas() throws IOException {
			setBackground(Color.GRAY);
			setSize(600, 908);

			URL url = new URL("https://imgflip.com/s/meme/Two-Buttons.jpg");
			template = ImageIO.read(url);
		}

		public void paint(Graphics g) {
			if (painted) {
				return;
			}
			painted = true;

			g.drawImage(template, 0, 0, (img, infoflags, x, y, width, height) -> true);

			Graphics2D g2d = (Graphics2D) g;

			g2d.rotate(30);;
			g2d.setFont(calculateFont("Test 1", 188, 91, g2d, getFont()));
			g2d.drawString("Text 1", 56, 85);
		}

		private Font calculateFont(String str, int width, int height, Graphics g, Font f) {
			g = g.create();
			float size = 1f;
			Rectangle2D b = new Rectangle(0, 0, 1, 1);
			Rectangle2D bound = new Rectangle(0, 0, width, height);
			while (bound.contains(b)) {
				f = f.deriveFont(size++);
				g.setFont(f);
				FontMetrics fm = g.getFontMetrics();
				b.setRect(0, 0, fm.stringWidth(str), str.split("\n").length * fm.getHeight());
			}

			return f.deriveFont(size - 1);
		}
	}
}
