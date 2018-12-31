package com.wudsn.ideas.cube;
import java.awt.Color;
public final class Face3D {

    private int[] vertexes;
    private Color color;
    
    public Face3D(int[] vertexes, Color color){
	if (vertexes == null) {
	    throw new IllegalArgumentException("Parameter 'vertexes' must not be null.");
	}
	if (color == null) {
	    throw new IllegalArgumentException("Parameter 'color' must not be null.");
	}
	this.vertexes=vertexes;
	this.color=color;
    }

    public int[] getVertexes() {
	return vertexes;
    }
    
    public Color getColor(){
	return color;
    }
}
