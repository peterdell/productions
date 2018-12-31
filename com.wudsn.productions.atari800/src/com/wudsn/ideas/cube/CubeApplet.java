package com.wudsn.ideas.cube;

/*
 * 	move.w	(a0)+,d0		;x,y,z_from_Body
 move.w	(a0)+,d1
 move.w	(a0)+,d2

 ;	********************
 ;	*	X-Achse	   *
 ;	********************

 move.w	d1,d5			;rette y,z
 move.w	d2,d6

 muls	CosX,d1			;y*cos(x)
 muls	SinX,d2			;z*sin(x)
 sub.l	d2,d1
 asl.l	#1,d1
 swap	d1			;new y

 muls	SinX,d5			;y*sin(x)
 muls	CosX,d6			;z*cos(x)
 add.l	d6,d5
 asl.l	#1,d5
 swap	d5			;new z
 move.w	d5,d2

 y2 =  y*cos(x)-z*sin(x)
 z2=   y*sin(x)+z*cos(x)

 sin(-15) = 0,25881904510252076234889883762405
 cos(-15) = 0,9659258262890682867497431997289

 ;	********************
 ;	*	Y-Achse	   *
 ;	********************

 move.w	d0,d5			;rette x,z
 move.w	d2,d6

 swap	d3			;Sin(y)/Cos(y) <=> Sin(z)/Cos(z)
 swap	d4

 muls	d4,d0			;x*cos(y)
 muls	d3,d2			;z*sin(y)
 add.l	d2,d0
 add.l	d0,d0			;*2
 swap	d0			;new x

 muls	d3,d5			;
 muls	d4,d6			;z*cos(y)
 sub.l	d5,d6
 add.l	d6,d6			;*2
 swap	d6			;new z

 x3 = + x*cos(y) + z*sin(y)
 z3 = - x*sin(y) + z*cos(y)

 ;	********************
 ;	*	Z-Achse	   *
 ;	********************

 move.w	d0,d5			;rette x,y
 move.w	d1,d2

 swap	d3			;Sin(y)/Cos(y) <=> Sin(z)/Cos(z)
 swap	d4

 muls	d4,d0			;x*cos(z)
 muls	d3,d1			;y*sin(z)
 sub.l	d1,d0
 add.l	d0,d0			;*2
 swap	d0			;new x

 muls	d3,d5			;x*sin(x)
 muls	d4,d2			;y*cos(x)
 add.l	d2,d5
 add.l	d5,d5			;*2
 swap	d5			;new y

 ;d0 = x
 ;d5 = y
 ;d6 = z


 */
//Draw a simple cube 
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

public final class CubeApplet extends BufferedApplet implements ActionListener {

    private static final Face3D face4 = new Face3D(new int[] { 2, 3, 7, 6 },
	    Color.green);
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private static double ry = 1.0d;

    private Object3D cube;

    private double degree;

    private Timer timer;
    private Matrix3D A;

    private OutputFile outputFile;

    @Override
    public void init() {
	A = new Matrix3D();
	timer = new Timer(20, this);
	setAnimating(true);
	degree = 0;
	timer.start();
	cube = new Object3D(new double[][] { { -1, ry, 1, 1 }, { 1, ry, 1, 1 },
		{ 1, ry, -1, 1 }, { -1, ry, -1, 1 }, { -1, -ry, 1, 1 },
		{ 1, -ry, 1, 1 }, { 1, -ry, -1, 1 }, { -1, -ry, -1, 1 } });
	cube.addFace(new Face3D(new int[] { 0, 3, 2, 1 }, Color.red));
	cube.addFace(new Face3D(new int[] { 4, 5, 6, 7 }, Color.blue));
	cube.addFace(new Face3D(new int[] { 0, 1, 5, 4 }, Color.cyan));
	cube.addFace(face4);
	cube.addFace(new Face3D(new int[] { 1, 2, 6, 5 }, Color.yellow));
	cube.addFace(new Face3D(new int[] { 0, 4, 7, 3 }, Color.lightGray));

	outputFile = new OutputFile();
	outputFile
		.open("C:\\jac\\system\\Atari2600\\Programming\\Demos\\Temp\\Cube.bin");
    }

    @Override
    public void render(Graphics graphics) {

	double[] directionVector1;
	double[] directionVector2;
	double[] normalVector;
	double[] viewVector;

	double[][] arrowVectors;

	directionVector1 = Vector3D.create();
	directionVector2 = Vector3D.create();
	normalVector = Vector3D.create();
	viewVector = Vector3D.create();
	viewVector[Vector3D.Z] = -1;

	arrowVectors = Vector3D.createArray(2);

	for (Face3D face : cube.getFaces()) {
	    int[] vertexes = face.getVertexes();
	    double[][] transformedVertexes = cube.getTransformedVertexes();
	    double[] vector1 = transformedVertexes[vertexes[0]];
	    double[] vector2 = transformedVertexes[vertexes[1]];
	    double[] vector3 = transformedVertexes[vertexes[2]];
	    Vector3D.subtract(vector1, vector2, directionVector1);
	    Vector3D.subtract(vector3, vector2, directionVector2);
	    Vector3D.normalVector(directionVector1, directionVector2,
		    normalVector);
	    double dotProduct = Vector3D.dotProduct(normalVector, viewVector);
	    // showStatus(Vector3D.toString(normalVector) + "\t" + dotProduct);
	    if (dotProduct > 0.1) {

		int size = vertexes.length;
		double[][] vectors = new double[size][];
		for (int v = 0; v < size; v++) {
		    vectors[v] = transformedVertexes[vertexes[v]];
		}

		int r = (int) (face.getColor().getRed() * dotProduct);
		int g = (int) (face.getColor().getGreen() * dotProduct);
		int b = (int) (face.getColor().getBlue() * dotProduct);
		graphics.setColor(new Color(r, g, b));
		drawPolygon(graphics, vectors, true, 0);
//		drawPolygon(graphics, vectors, true, 1);

		if (face == face4) {
		    arrowVectors[0] = vector1;
		    arrowVectors[1] = Vector3D.create();
		    graphics.setColor(Color.red);
		    drawPolygon(graphics, arrowVectors, false, 0);
		}
		//
		// arrowVectors[0] = Vector3D.create();
		// arrowVectors[1] = directionVector1;
		// graphics.setColor(Color.red);
		// drawPolygon(graphics, arrowVectors, false);
		//
		// arrowVectors[0] = Vector3D.create();
		// arrowVectors[1] = directionVector2;
		// graphics.setColor(Color.green);
		// drawPolygon(graphics, arrowVectors, false);
		//
		// arrowVectors[0] = Vector3D.create();
		// arrowVectors[1] = normalVector;
		// graphics.setColor(Color.blue);
		// drawPolygon(graphics, arrowVectors, false);

	    }
	}
    }

    private void drawPolygon(Graphics g, double[][] vectors, boolean fill,
	    int yoffset) {
	int size = vectors.length;
	double focalLength = 10;
	int[] sx = new int[size];
	int[] sy = new int[size];
	for (int i = 0; i < size; i++) {
	    double perspectiveFactor = (vectors[i][Vector3D.Z] + focalLength)
		    / focalLength;
	    sx[i] = getScreenX(vectors[i][Vector3D.X] / perspectiveFactor);
	    sy[i] = getScreenY(vectors[i][Vector3D.Y] / perspectiveFactor
		    + yoffset);
	}
	if (fill) {
	    g.fillPolygon(sx, sy, size);
	} else {
	    g.drawPolygon(sx, sy, size);

	}
	if (outputFile.isOpen()) {
	    for (int i = 0; i < size; i++) {
		double perspectiveFactor = (vectors[i][Vector3D.Z] + focalLength)
			/ focalLength;
		outputFile
			.writeByte((int) (128 * vectors[i][Vector3D.X] / perspectiveFactor));
		outputFile
			.writeByte((int) (128 * vectors[i][Vector3D.Y] / perspectiveFactor));
	    }
	}
    }

    @Override
    public void actionPerformed(ActionEvent e) {

	A.makeIdentity();
	A.rotateX(Math.toRadians(15));
	A.rotateY(Math.toRadians(45));
	A.rotateZ(Math.toRadians(45));
	// A.rotateZ(Math.toRadians(-15 + degree * 1.5));

	cube.transform(A);
	degree += (90 / 64);

	if (degree >= 90) {
	    outputFile.close();
	    degree = degree - 90;
	}
    }

}