package com.wudsn.ideas;

import java.awt.Graphics2D;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class MandelBrot {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		JFrame f = new JFrame("ImageDrawing");
		f.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});

		BufferedImage bi = new BufferedImage(320, 192,
				BufferedImage.TYPE_INT_RGB);

		ImageIcon imageIcon = new ImageIcon();
		imageIcon.setImage(bi);
		JLabel label = new JLabel();
		label.setIcon(imageIcon);

		f.add("Center", label);
		f.pack();
		f.setVisible(true);

		Graphics2D g2d = (Graphics2D) label.getGraphics();

		AffineTransform tx = new AffineTransform();
		tx.setToScale(2, 2);
		g2d.setTransform(tx);

		int start = 0;
		while (true) {
			start++;
			MandelBrot.test(bi, start);

			imageIcon.setImage(bi);
			label.setIcon(imageIcon);
			label.repaint();
		}
	}

	private static void test(BufferedImage bi, int start) {
		final int X_SIZE = 256;
		final int Y_SIZE = 192;

		if (bi == null) {
			throw new IllegalArgumentException(
					"Parameter 'bi' must not be null.");
		}

		int factor = 256;
		int a = Math.round(-2.2f * factor);
		int s = Math.round(-2.0f * factor);
		int d = Math.round((1.0f / 52) * factor);
		int f = Math.round((1.0f / 40) * factor);
		for (int sX = 0; sX < X_SIZE; sX++) {

			int dx = a + sX * d;
			for (int sY = 0; sY < Y_SIZE; sY++) {

				int dy = s + sY * f;
				int betrag_sqr = 0;
				int iter = 0;
				int _x = sX;
				int _y = sY;

				while (betrag_sqr <= 4 * factor && iter < 32) {
					int xt = ((_x * _x) / factor) - ((_y * _y) / factor) + dx;
					int yt = ((2 * _x * _y) / factor) + dy;
					_x = xt;
					_y = yt;
					iter++;
					betrag_sqr = ((_x * _x) / factor) + ((_y * _y) / factor);
				}
				int lum = iter * 64;
				bi.setRGB(sX, sY, 0x111 * lum + start);
			}
		}
	}

}
