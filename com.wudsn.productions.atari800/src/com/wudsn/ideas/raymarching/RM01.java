package com.wudsn.ideas.raymarching;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

final class RGB {

    public static final int UNDEFINED = -1;
    public static final int BLACK = 0x000000;
    public static final int GREY = 0x808080;
    public static final int WHITE = 0xffffff;

    public static int getGrey(double c) {
	int bright = (int) (c * 255);
	return bright << 16 | bright << 8 | bright;
    }

}

final class DistanceColor {
    public double distance;
    public int color;
}

final class V {
    public final double x;
    public final double y;
    public final double z;

    public V(double x, double y, double z) {
	this.x = x;
	this.y = y;
	this.z = z;
    }

    public V plus(V v) {
	return new V(x + v.x, y + v.y, z + v.z);
    }

    public double len() {

	return Math.sqrt(x * x + y * y + z * z);
    }

    public V normalize() {
	double l = len();
	return new V(x / l, y / l, z / l);
    }

    public V times(double d) {
	return new V(d * x, d * y, d * z);
    }
}

public final class RM01 {
    public static void main(String[] args) {
	new RM01();
    }

    int swidth = 320, sheight = swidth * 5 / 8;

    private JFrame frame;
    private BufferedImage simage;
    private ImageIcon imageIcon;
    private JLabel imageLabel;

    final static double INFINITE_DISTANCE = 1e10;
    final double minDist = 1e-4;
    final int maxIter = 100;
    final double overstep = 1.0;
    final double perspective = 0.75;
    final V cameraLocation = new V(0, 0, -0.4);
    final double zoom = 2.0;
    double time = 0;

    RM01() {
	simage = new BufferedImage(swidth, sheight, BufferedImage.TYPE_INT_RGB);
	imageIcon = new ImageIcon();
	imageIcon.setImage(simage);
	imageLabel = new JLabel();
	imageLabel.setIcon(imageIcon);
	imageLabel.setSize(swidth, sheight);

	paint();

	JButton button = new JButton("Next frame");
	button.addActionListener(new ActionListener() {
	    @Override
	    public void actionPerformed(ActionEvent e) {
		step();
		paint();
	    }
	});

	frame = new JFrame("Raymarching");
	frame.getContentPane().setLayout(new BorderLayout());
	frame.getContentPane().add(BorderLayout.CENTER, imageLabel);
	frame.getContentPane().add(BorderLayout.SOUTH, button);
	frame.pack();
	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	frame.setVisible(true);
    }

    void step() {
	time+=0.5;
	frame.setTitle("Raymarching " + time);
    }

    void paint() {
	for (int sy = 0; sy < sheight; sy++) {
	    for (int sx = 0; sx < swidth; sx++) {
		int color = renderPoint(sx, sy);
		simage.setRGB(sx, sy, color);
	    }
	}

	imageLabel.setIcon(null);
	imageLabel.setIcon(imageIcon);
    }

    private int renderPoint(int sx, int sy) {
	double x = (double) (sx - swidth / 2) / (swidth / 2);
	double y = (double) (sy - sheight / 2) / (swidth / 2);
	x /= zoom;
	y /= zoom;
	V v = new V(x, y, 0).plus(cameraLocation);
	V direction = new V(x * perspective, y * perspective, 1).normalize();
	int color = RGB.UNDEFINED;
	DistanceColor distanceColor = new DistanceColor();

	for (int iter = 0; iter < maxIter; iter++) {
	    distanceColor.distance = 0;
	    distanceColor.color = RGB.UNDEFINED;

	    lookupDistance(v, distanceColor);
	    if (distanceColor.color != RGB.UNDEFINED) {
		color = distanceColor.color;
	    }
	    if (distanceColor.distance == INFINITE_DISTANCE) {
		return RGB.WHITE;
	    } else if (distanceColor.distance < minDist) {
		// System.out.println(iter);
		return color == RGB.UNDEFINED ? RGB.BLACK : color;
	    } else {
		// System.out.println(dist);
		v = v.plus(direction.times(distanceColor.distance * overstep));
	    }
	}
	return RGB.GREY;
    }

    private void lookupDistance(V v, DistanceColor distanceColor) {
	// return square(v);
	// return cube(v, 0.5);
	// blackCube(rotateZ(Math.PI * 0.125, v), 0.5, distanceColor);
	// return cube(rotateX(Math.PI*0.125, v), 0.25);
	blackToWhiteCube(rotateY(Math.PI * 0.125 * time, v), 0.25,
		distanceColor);
    }

//    private V rotateX(double angle, V v) {
//	double sin = Math.sin(angle), cos = Math.cos(angle);
//	return new V(v.x, v.y * cos - v.z * sin, v.y * sin + v.z * cos);
//    }

    private static V rotateY(double angle, V v) {
	double sin = Math.sin(angle), cos = Math.cos(angle);
	return new V(v.x * cos - v.z * sin, v.y, v.x * sin + v.z * cos);
    }

//    private V rotateZ(double angle, V v) {
//	double sin = Math.sin(angle), cos = Math.cos(angle);
//	return new V(v.x * cos - v.y * sin, v.x * sin + v.y * cos, v.z);
//    }

//    private double square(V v) {
//	if (Math.abs(v.x) < 0.5 && Math.abs(v.y) < 0.5) {
//	    return 0.0;
//	}
//	return INFINITE_DISTANCE;
//    }

//    private void blackCube(V v, double size, DistanceColor distanceColor) {
//	distanceColor.color = 0x000000;
//	distanceColor.distance = cube(v, size);
//    }

    private static void blackToWhiteCube(V v, double size, DistanceColor distanceColor) {
	double c = ((Math.max(-size, Math.min(size, v.y)) / size) + 1) / 2;
	distanceColor.color = RGB.getGrey(c);
	distanceColor.distance = cube(v, size);
	// distanceColor.distance = square(v);
    }

    /* a cube centered at 0,0,0 with a length of totalRecordCount*2 in all 3 dimensions */
    private static double cube(V v, double size) {
	double x = Math.abs(v.x) - size, y = Math.abs(v.y) - size, z = Math
		.abs(v.z) - size;
	return new V(Math.max(x, 0), Math.max(y, 0), Math.max(z, 0)).len();
    }
}