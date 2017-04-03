/*************************************************************************
 *  Compilation:  javac TrussElement.java
 *  Execution:    java TrussElement
 *  Dependencies: Node.java  processing.core.PApplet
 *
 *  A TrussElement object represented as a directed edge between two Nodes
 *  
 *  Includes methods for displaying Geometry using the PApplet object,
 *  which is the standard Processing API for graphics.
 *  
 *  @author Alexandros Charidis, Digital Structures, MIT
 *************************************************************************/

import processing.core.PApplet;

public class TrussElement {

	private Node from_n;
	private Node to_n;
	
	TrussElement() { /* default constructor */ }
	TrussElement(final Node fv3, final Node tv3) {
		from_n  = fv3;
		to_n    = tv3;
	}
	
	// getters
	public Node fromN()    {  return from_n;  }
	public Node toN()      {  return to_n;    }

	public float length()  {
		float xx = to_n.x()-from_n.x();
		float yy = to_n.y()-from_n.y();
		return ( (float)Math.sqrt(xx*xx + yy*yy) );
	}
	
	// setters
	public void setFromN(final Node f) {  from_n = f;  }
	public void setToN(final Node t)   {  to_n   = t;  }
	
	// draw on screen using given Processing Applet
	public void draw(PApplet p5, int scale) {
		p5.stroke(160);
		p5.strokeWeight(1);
		p5.line(from_n.x()*scale, from_n.y()*scale, to_n.x()*scale, to_n.y()*scale);
	}
	
	/**
	 *  @return
	 */
	public String toString() {
		return "{ From: " + from_n.x() + ", " + from_n.y() + "\n" +
        		" To: " + to_n.x() + ", " + to_n.y() + " }\n";
	}
	
	/**
	 *  Test client and sample execution.
	 */
	public static void main(String[] args) {
	}
	
}
