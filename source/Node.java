/*************************************************************************
 *  Compilation:  javac Node.java
 *  Execution:    java Node
 *  Dependencies: processing.core.PApplet
 *
 *  A Node object represented as a two-dimensional point with x and y 
 *  coordinates.
 *  
 *  Includes methods for displaying Geometry using the PApplet object,
 *  which is the standard Processing API for graphics.
 *  
 *  @author Alexandros Haridis, Digital Structures, MIT
 *************************************************************************/

import processing.core.PApplet;

public class Node {
	
    private float x_, y_;
	
    /**
     *  Default constructor. Initializes a Node with x = 0, y = 0.
     */
    public Node() {
        x_ = 0;
        y_ = 0;
    }
	
    /**
     *  Initializes a Node given x and y as inputs.
     */
    public Node(float x, float y) {
        x_ = x;
        y_ = y;
    }
	
    /**
     *  Initializes a Node by copying x and y from an input Node.
     */
    public Node(final Node in) {
        x_ = in.x();
        y_ = in.y();
    }
	
    public float x() {  return x_;  }
    public float y() {  return y_;  }
	
    // draw on screen using given Processing Applet
    public void draw(PApplet p5, int scale) {
        p5.fill(190);
        p5.strokeWeight(1);
        p5.stroke(0);
        p5.ellipse(x_*scale, y_*scale, 5, 5);
    }
	
}
