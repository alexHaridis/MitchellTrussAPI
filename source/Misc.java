/*************************************************************************
 *  Compilation:  javac Misc.java
 *  Execution:    java Misc
 *  Dependencies: processing.core.PApplet  processing.core.PFont
 *                Jama.Matrix
 *
 *  Helper functions primarily for displaying geometry, text, and matrices
 *  in Processing API and Java.
 *  
 *  @author Alexandros Charidis, MIT
 *************************************************************************/

import Jama.Matrix;
import processing.core.PApplet;
import processing.core.PFont;

public class Misc {

    public static void printMatrix(final Matrix m) {
        for (int i=0; i<m.getRowDimension(); i++) {
	    System.out.print("| ");
	    if (m.getColumnDimension() == 1) {
	        System.out.print(m.get(i, 0));
	    } else {
	        for (int j=0; j<m.getColumnDimension(); j++) {
		    System.out.print(m.get(i, j) + ", ");
		}	
	    }
	    System.out.print(" |");
	    System.out.println();
	}
    }

    public static void drawXX(final PApplet p5, boolean dashed, int w) {
        p5.stroke(240);
	p5.strokeWeight(0);
	p5.line(-w, 0, w, 0);
    }

    public static void drawOrigin(final PApplet p5) {
        p5.fill(160, 0, 0);
	p5.strokeWeight(1);
	p5.stroke(200);
	p5.line(0, 0, 20, 0);
	p5.line(0, 0, 0, 20);
	p5.line(0, 0, -20, 0);
	p5.line(0, 0, 0, -20);
	p5.noStroke();
	p5.ellipse(0, 0, 7, 7);
	p5.noFill();
    }

    /**
     *  Helper function for displaying texts on screen
     */
    public static void displaytext(PApplet p5, String _text, PFont _font, int _size, int _color, char _align, int _x, int _y){
        switch (_align){
	    case 'C':
	        p5.textAlign(p5.CENTER);
		break;
	    case 'L':
		p5.textAlign(p5.LEFT);
		break;
	    case 'R':
		p5.textAlign(p5.RIGHT);
		break;
	}
	p5.textFont(_font, _size);
	p5.fill(_color); // blue letters with oppacity
	p5.text(_text, _x, _y);
    }

    /**
     * draws information about the current sketch
     */
    public static void displayMark(PApplet p5, PFont font, int x, int y, int color) { 
        String string0 = "E x p l o r i n g   M i c h e l l  S t r u c t u r e s";
	String string1 = "Alexandros Charidis, MIT";
	String string2 = "charidis@mit.edu";
	String string3 = "4.s48 Computational Structural Design & Optimization";
	String string4 = "Spring 2015";

	String string5 = "Built with Processing";
	String string6 = "Using: ControlP5 and Jama";

	int off = 20;

	displaytext(p5, string0, font, 12, color, 'L', x, y);
	displaytext(p5, string1, font, 12, color, 'L', x, y + off);
	displaytext(p5, string2, font, 12, color, 'L', x, y + 2*off);
	displaytext(p5, string3, font, 10, color, 'L', x, y + 3*off + 10);
	displaytext(p5, string4, font, 10, color, 'L', x, y + 4*off + 10);
	displaytext(p5, string5, font, 10, color, 'L', x, y + 6*off + 10);
	//displaytext(p5, string6, font, 10, color, 'L', x, y + 7*off + 10);
    }
	
}
