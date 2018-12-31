package com.wudsn.ideas.cube;

public final class Matrix3D {
    // read as row,col
    private double matrix[][] = new double[4][4];

    public Matrix3D() {
	makeIdentity();
    }

    public void set(int i, int j, double value) {
	matrix[i][j] = value;
    }

    public double get(int i, int j) {
	return matrix[i][j];
    }

    public void copyFrom(Matrix3D source) {
	if (source == null) {
	    throw new IllegalArgumentException(
		    "Parameter 'source' must not be null.");
	}
	for (int i = 0; i < 4; i++) {
	    for (int j = 0; j < 4; j++) {
		matrix[i][j] = source.get(i, j);
	    }
	}
    }

    public void makeIdentity() {
	for (int i = 0; i < 4; i++) {
	    for (int j = 0; j < 4; j++) {
		if (i == j) {
		    matrix[i][j] = 1;
		} else {
		    matrix[i][j] = 0;
		}
	    }
	}
    }

    private void makeTranslationMatrix(double a, double b, double c) {
	matrix[0][3] = a;
	matrix[1][3] = b;
	matrix[2][3] = c;
    }

    private void makeScaleMatrix(double a, double b, double c) {
	matrix[0][0] = a;
	matrix[1][1] = b;
	matrix[2][2] = c;
    }

    private void makeRotateXMatrix(double theta) {
	matrix[1][1] = Math.cos(theta);
	matrix[2][1] = Math.sin(theta);
	matrix[1][2] = -1 * Math.sin(theta);
	matrix[2][2] = Math.cos(theta);
    }

    private void makeRotateYMatrix(double theta) {
	matrix[0][0] = Math.cos(theta);
	matrix[0][2] = Math.sin(theta);
	matrix[2][0] = -1 * Math.sin(theta);
	matrix[2][2] = Math.cos(theta);
    }

    private void makeRotateZMatrix(double theta) {
	matrix[0][0] = Math.cos(theta);
	matrix[1][0] = Math.sin(theta);
	matrix[0][1] = -1 * Math.sin(theta);
	matrix[1][1] = Math.cos(theta);
    }

    private void multiply(Matrix3D B) {
	double sum;
	Matrix3D A = new Matrix3D();
	A.copyFrom(this);

	for (int i = 0; i < 4; i++) {
	    for (int j = 0; j < 4; j++) {
		sum = 0;
		for (int k = 0; k < 4; k++) {
		    sum += (A.get(i, k) * B.get(k, j));
		}

		matrix[i][j] = sum;
	    }
	}
    }

    public void translate(double a, double b, double c) {
	Matrix3D temp = new Matrix3D();
	temp.makeTranslationMatrix(a, b, c);
	multiply(temp);
    }

    public void scale(double a, double b, double c) {
	Matrix3D temp = new Matrix3D();
	temp.makeScaleMatrix(a, b, c);
	multiply(temp);
    }

    public void rotateX(double theta) {
	Matrix3D temp = new Matrix3D();
	temp.makeRotateXMatrix(theta);
	multiply(temp);
    }

    public void rotateY(double theta) {
	Matrix3D temp = new Matrix3D();
	temp.makeRotateYMatrix(theta);
	multiply(temp);
    }

    public void rotateZ(double theta) {
	Matrix3D temp = new Matrix3D();
	temp.makeRotateZMatrix(theta);
	multiply(temp);
    }

    public void transformVector(double sourceVector[],
	    double destinationVector[]) {
	if (sourceVector == null) {
	    throw new IllegalArgumentException(
		    "Parameter 'sourceVector' must not be null.");
	}
	if (destinationVector == null) {
	    throw new IllegalArgumentException(
		    "Parameter 'destinationVector' must not be null.");
	}
	destinationVector[0] = (matrix[0][0] * sourceVector[0])
		+ (matrix[0][1] * sourceVector[1])
		+ (matrix[0][2] * sourceVector[2])
		+ (matrix[0][3] * sourceVector[3]);
	destinationVector[1] = (matrix[1][0] * sourceVector[0])
		+ (matrix[1][1] * sourceVector[1])
		+ (matrix[1][2] * sourceVector[2])
		+ (matrix[1][3] * sourceVector[3]);
	destinationVector[2] = (matrix[2][0] * sourceVector[0])
		+ (matrix[2][1] * sourceVector[1])
		+ (matrix[2][2] * sourceVector[2])
		+ (matrix[2][3] * sourceVector[3]);
	destinationVector[3] = (matrix[3][0] * sourceVector[0])
		+ (matrix[3][1] * sourceVector[1])
		+ (matrix[3][2] * sourceVector[2])
		+ (matrix[3][3] * sourceVector[3]);
    }

}