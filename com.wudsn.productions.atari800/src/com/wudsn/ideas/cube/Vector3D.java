package com.wudsn.ideas.cube;

public final class Vector3D {

    /**
     * Array indxes.
     */
    public static final int X = 0;
    public static final int Y = 1;
    public static final int Z = 2;

    public static double[] create() {
	return new double[] { 0, 0, 0, 1 };
    }

    public static double[][] createArray(int length) {
	double[][] result;
	result = new double[length][];
	for (int i = 0; i < length; i++) {
	    result[i] = create();
	}
	return result;
    }

    public static void crossProduct(double[] vector1, double[] vector2,
	    double resultVector[]) {
	resultVector[X] = vector1[Y] * vector2[Z] - vector1[Z] * vector2[Y];
	resultVector[Y] = vector1[X] * vector2[Z] - vector1[Z] * vector2[X];
	resultVector[Z] = vector1[X] * vector2[Y] - vector1[Y] * vector2[Z];
    }

    public static void normalVector(double[] vector1, double[] vector2,
	    double resultVector[]) {
	double resultVectorX;
	double resultVectorY;
	double resultVectorZ;
	double absoluteValue;

	resultVectorX = vector1[Y] * vector2[Z] - vector1[Z] * vector2[Y];
	resultVectorY = vector1[X] * vector2[Z] - vector1[Z] * vector2[X];
	resultVectorZ = vector1[X] * vector2[Y] - vector1[Y] * vector2[X];

	absoluteValue = Math.sqrt(resultVectorX * resultVectorX + resultVectorY
		* resultVectorY + resultVectorZ * resultVectorZ);

	resultVector[X] = resultVectorX / absoluteValue;
	resultVector[Y] = resultVectorY / absoluteValue;
	resultVector[Z] = resultVectorZ / absoluteValue;
    }

    public static double dotProduct(double[] vector1, double[] vector2) {
	if (vector1 == null) {
	    throw new IllegalArgumentException(
		    "Parameter 'vector1' must not be null.");
	}
	if (vector2 == null) {
	    throw new IllegalArgumentException(
		    "Parameter 'vector2' must not be null.");
	}
	return (vector1[X] * vector2[X]) + (vector1[Y] * vector2[Y])
		+ (vector1[Z] * vector2[Z]);
    }

    public static double absolteValue(double[] vector) {
	return Math.sqrt(dotProduct(vector, vector));
    }

    public static void subtract(double[] vector1, double[] vector2,
	    double[] resultVector) {
	resultVector[X] = vector1[X] - vector2[X];
	resultVector[Y] = vector1[Y] - vector2[Y];
	resultVector[Z] = vector1[Z] - vector2[Z];
	resultVector[3] = vector1[3] - vector2[3];

    }

    public static String toString(double[] vector) {
	return "(" + toString(vector[X]) + "," + toString(vector[Y]) + ","
		+ toString(vector[Z])+ ","
		+ toString(vector[3]) + ")";
    }

    private static String toString(double value) {
	String padding = "                               ";
	String result = Double.toString(value);
	result = result + padding.substring(padding.length() - result.length());
	return result;
    }
}