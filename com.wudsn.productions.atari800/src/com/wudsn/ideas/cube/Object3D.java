package com.wudsn.ideas.cube;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public final class Object3D {

    private final class Face3DZComparator implements Comparator<Face3D> {

	private Object3D object;

	Face3DZComparator(Object3D object) {
	    if (object == null) {
		throw new IllegalArgumentException(
			"Parameter 'object' must not be null.");
	    }
	    this.object = object;
	}

	@Override
	public int compare(Face3D o1, Face3D o2) {

	    return Double.compare(getAverageZ(o2), getAverageZ(o1));
	}

	private double getAverageZ(Face3D face) {
	    int[] vertexes;
	    double sum;
	    vertexes = face.getVertexes();
	    sum = 0;
	    for (int vertex : vertexes) {
		sum += object.transformedVertexes[vertex][Vector3D.Z];
	    }
	    return sum;
	}

    }

    double[][] vertexes;
    double transformedVertexes[][];
    private List<Face3D> faces;

    public Object3D(double[][] vertexes) {
	if (vertexes == null) {
	    throw new IllegalArgumentException(
		    "Parameter 'vertexes' must not be null.");
	}

	this.vertexes = vertexes;
	transformedVertexes = Vector3D.createArray(vertexes.length);

	faces = new ArrayList<Face3D>();

    }

    public double[][] getVertexes() {
	return vertexes;
    }

    public double[][] getTransformedVertexes() {
	return transformedVertexes;
    }

    public void transform(Matrix3D A) {
	if (A == null) {
	    throw new IllegalArgumentException(
		    "Parameter 'A' must not be null.");
	}

	for (int i = 0; i < vertexes.length; i++)
	    A.transformVector(vertexes[i], transformedVertexes[i]);
    }

    public void addFace(Face3D face) {
	if (face == null) {
	    throw new IllegalArgumentException(
		    "Parameter 'face' must not be null.");
	}
	faces.add(face);
    }

    public List<Face3D> getFaces() {
	return faces;
    }

    public List<Face3D> getFacesInZOrder() {
	List<Face3D> result = new ArrayList<Face3D>(faces);
	Collections.sort(result, new Face3DZComparator(this));
	return result;
    }
}
