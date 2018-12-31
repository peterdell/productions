package com.wudsn.ideas.cube;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;

public abstract class BufferedApplet extends java.applet.Applet {

    /**
     * Runnable for periodic repaint.
     */
    private final class Repainter implements Runnable {
	private BufferedApplet applet;

	public Repainter(BufferedApplet applet) {
	    if (applet == null) {
		throw new IllegalArgumentException(
			"Parameter 'applet' must not be null.");
	    }
	    this.applet = applet;
	}

	@Override
	public void run() {
	    try {
		while (true) {
		    applet.repaint();
		    // Repaint every 30 msecs = 60 Hz
		    Thread.sleep(30);
		}
	    } catch (InterruptedException ignore) {
	    }
	}

    }

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private int width, height; // Current applet totalRecordCount
    private Image bufferImage; // Image for the double buffer
    private Graphics bufferGraphics; // Canvas for double buffer
    private Rectangle bufferBounds; // Double buffer bounds
    private Thread repainterThread; // Background thread for rendering

    private boolean damaged;
    private boolean animating;

    protected BufferedApplet() {

	bufferBounds = new Rectangle(0, 0, 0, 0);
	damaged = true;
	animating = false;
    }

    /**
     * Extend the start,stop,run methods to implement double buffering.
     */
    @Override
    public final void start() {
	if (repainterThread == null) {
	    repainterThread = new Thread(new Repainter(this));
	    repainterThread.start();
	}
    }

    @Override
    public final void stop() {
	if (repainterThread != null) {
	    repainterThread = null;
	}
    }

    /*
     * Update(Graphics) is called by repaint() - the system adds canvas.
     * 
     * @see java.awt.Container#update(java.awt.Graphics) Extend update method to
     * create a double buffer whenever necessary.
     */
    @Override
    public final void update(Graphics g) {
	if (g == null) {
	    throw new IllegalArgumentException(
		    "Parameter 'g' must not be null.");
	}
	width = getBounds().width;
	height = getBounds().height;
	if (bufferBounds.width != width || bufferBounds.height != height) {
	    bufferImage = createImage(width, height);
	    bufferGraphics = bufferImage.getGraphics(); // Applet totalRecordCount changed

	    bufferBounds = getBounds(); // Make double buffer
	    damaged = true; // Tell application
	}

	if (damaged) {

	    bufferGraphics.setColor(Color.white);
	    bufferGraphics.fillRect(0, 0, width, height);
	    render(bufferGraphics); // Ask application to render to buffer
	    paint(g); // paste buffered image onto the applet
	}
	damaged = animating;
    }

    @Override
    public void init() {

    }

    /**
     * Separate paint method for application to extend if needed.
     */
    @Override
    public final void paint(Graphics g) {
	if (g == null) {
	    throw new IllegalArgumentException(
		    "Parameter 'g' must not be null.");
	}
	if (bufferImage != null) {
	    // Paste result of render().
	    g.drawImage(bufferImage, 0, 0, this);

	}
    }

    protected final void setAnimating(boolean animating) {
	this.animating = animating;
    }

    /*
     * Application defined render method
     */
    protected abstract void render(Graphics g);

    protected final int getScreenX(double t) {
	return width / 2 + (int) (t * width / 4);
    }

    protected final int getScreenY(double t) {
	return height / 2 - (int) (t * width / 4);
    }

}