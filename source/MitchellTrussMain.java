/*************************************************************************
 *  Compilation:  javac MitchellTrussMain.java
 *  Execution:    java MitchellTrussMain
 *  Dependencies: Node.java  Truss.java  TrussElement.java  TrussGraph.java
 *                StructuralAnalysis.java  Misc.java  Jama.Matrix
 *                processing.*  toxi.geom.Vec2D  controlP5.* 
 *
 *  This is a standalone application for exploring optimum Mitchell trusses
 *  written in Java using the Processing API for graphic elements, geometry, 
 *  rendering and GUI. The application builds upon a geometric solution of
 *  discrete Mitchell trusses in their simple symmetric form: a discrete
 *  optimum truss supported by two fixed points (on the same vertical line)
 *  and a vertical point load positioned in between the supports. For more
 *  information on the geometric solution, see Mazurek, A., Baker, W. F.,
 *  Tort, C. "Geometrical aspects of optimum truss like structures," 
 *  <em>Structural and Multidisciplinary Optimization</em>, 43 (2), 2011.
 *  
 *  Please refer to the accompanying <b>pdf</b> "Exploring Mitchell Structures" 
 *  which explains this implementation.
 *  
 *  This is written in Java 8 and Processing 2.2.1 using Eclipse Luna 4.*. 
 *  Processing can be used within the Eclipse IDE by following this tutorial:
 *  https://processing.org/tutorials/eclipse/
 *  
 *  @author Alexandros Haridis, Digital Structures, MIT
 *************************************************************************/

import java.util.ArrayList;
import java.util.LinkedList;

import processing.pdf.*;
import processing.core.PApplet;
import processing.core.PFont;
import toxi.geom.Vec2D;
import Jama.Matrix;
import controlP5.*;

public class MitchellTrussMain extends PApplet {
	
    final float DSG_EPS = 1e-6f;
    final float toRad = (float)Math.PI/180f;
    final float toDeg = 180f/(float)Math.PI;
	
    Truss michell;
	
    ControlP5 cp5;
	
    boolean record = false;
    boolean drawForces = false;
	
    // Fonts
    PFont signatureFont, font;
	
    /* Global parametric variables */
    int h = 4;
    int nel = 2;
    int L = 40;
	
    public void setup() {
		
        size(500, 700);
	smooth();
		
	cp5 = new ControlP5(this);
		
	// create font for signature text
	signatureFont = createFont("Courier-48", 14, true);
	font = createFont("Times", 14, true);
		
	michell = new Truss();
	MichellStructure(nel, h, L);
	analyzeMichell();
		
	/**
	 *  GUI objects
	 */
		
	cp5.addSlider("h")
	    .setColorValueLabel(color(100, 100, 150))
	    .setPosition(300+30, height - 340)
	    .setSize(100, 15)
	    .setRange(2,0) // values can range from big to small as well
	    .setColorActive(color(0, 255, 0))
	    .setColorBackground(color(100, 100, 150))
	    .setColorForeground(color(140))
	    .setNumberOfTickMarks(3)
	    .setSliderMode(Slider.FLEXIBLE)
	    .setDecimalPrecision(0);
		
	cp5.addSlider("num")
            .setColorValueLabel(color(100))
	    .setPosition(40, height - 340)
	    .setSize(130, 15)
	    .setRange(6,0) // values can range from big to small as well
	    .setColorActive(color(0, 255, 0))
	    .setColorBackground(color(100))
	    .setColorForeground(color(140))
	    .setNumberOfTickMarks(7)
	    .setSliderMode(Slider.FLEXIBLE)
	    .setDecimalPrecision(0);

    }

    public void draw() {
		
        if (record) {
            // #### will be replaced with the frame number.
            beginRecord(PDF, "data/michell-####.pdf"); 
	}
		
	background(10);
		
	pushMatrix();
	translate(50, height/2-150);
	scale(1, -1);
	Misc.drawOrigin(this);
	Misc.drawXX(this, true, width);
	michell.draw(this, true, 10); // draws elements scaled by 10 (for visualization only)
	if (drawForces) {
            scale(1, -1);
            michell.drawForces(this, 10);
	}
	popMatrix();
		
	Misc.displaytext(this, "Sym.", font, 10, 230, 'L', 7, height/2-155);
	Misc.displaytext(this, "L: 40 ft", font, 10, 230, 'L', 7+425, height/2-155);
	Misc.displayMark(this, signatureFont, width/2-200, height-200, 255);
	Misc.displaytext(this, String.valueOf((int)michell.sigmaFL()), font, 12, 230, 'L', 7+425, height/2-55);
		
	cp5.draw();
		
	if (record) {
	    endRecord();
            record = false;
	    println("Screen captured as PDF");
	}
		
    }
	
    public void MichellStructure(int ne, float h, float L) {
		
        if (ne == 2) {

	    // build geometry
	    ArrayList<Node> nodes = new ArrayList<Node>();
	    nodes.add(new Node(0, h/2)); // 0
	    nodes.add(new Node(0, -h/2)); // 1
	    nodes.add(new Node(L, 0)); // 2

	    // build topology
	    TrussGraph graph = new TrussGraph(3);
	    graph.addEdge(0, 2);
	    graph.addEdge(1, 2);

	    float gama = (float)Math.atan((h/2)/L) * 2;

	    /** Uncomment the following lines to print parameters' values **/
	    // println("For a Michell structure with:\n");
	    // println("ne = ", ne, " h = ", h, "ft and L = ", L, "ft.");
	    // println("The optimized gama found is: ", gama*toDeg, " degrees.\n");

	    // build truss
	    michell.setGeometry(nodes);
	    michell.setTopology(graph);

        } else {
			
	ArrayList<Node> nodes;
	TrussGraph graph;
			
	/*
	 *  Optimize gama for L = Lp where Lp is the point
	 *  on x axis (symmetry axis) where the point load is applied
	 *  and which satisfies the verticality constraint (see code).
	 *  
	 *  Note: I have used a very crude linear search method that tries
	 *  to break down 300 by a given step. I then see which of these
	 *  works better so that L =~ Lp with an epsilon approximation.
	 *  There are of course much better ways to do this.
	 */
			
			int attempts = 0;
			float step = 89f/300f;
			float gama = step;
			
			while (attempts < 300) {	
				
				// initialize geometry / topology
				nodes = new ArrayList<Node>();
				
				// Build Geometry
				
				// support nodes
				Node sup1 = new Node(0, h/2);
				nodes.add(sup1); // 0
				Node sup2 = new Node(0, -h/2); // symmetric (xx')
				nodes.add(sup2); // 1
				
				// Michell structure standard angles
				float kapa = 90f-gama;
				float lamda = 90f-gama/2f;
				
				int numNodes = 2;
				
				float Lp = DSG_EPS;
				
				int na = (int)Math.sqrt(ne/2);
				Node previous = new Node(sup1);
				while (na > 0) {
					
					float initAngle = lamda;
					float lm = (float)(Math.tan( (90+initAngle)*toRad ));
					
			    	Lp = previous.x()-previous.y()/(lm+DSG_EPS);
			    	Node xx = new Node(Lp, 0);
			    	nodes.add(xx);
			    	numNodes++;
			    	
			    	previous = new Node(xx);
			    	float side_prev = previous.y();
			    	if (na>1) {
			    		float current_thetam, previous_thetam = 0;
			    		ArrayList<Node> stride = new ArrayList<Node>();
			    		for (int i=0; i<na-1; i++) {
			    			
			    			float l2m = -1f/(lm+DSG_EPS);
			    			float thetam = (float)Math.atan( Math.abs(l2m) );			    			
			    			current_thetam = (float)Math.toDegrees(thetam);
			    			float bm = (float)Math.sqrt(side_prev*side_prev+Lp*Lp);
			    			float mm = bm*( (float)Math.tan( kapa*toRad) );
			    			
			    			// construct cartesian from polar given the angle
					    	float xm;
							if (current_thetam<previous_thetam) {  // flip along y axis
								xm = previous.x() - mm*(float)Math.cos(thetam);
							} else {
								xm = previous.x() + mm*(float)Math.cos(thetam);
							}
							float ym = previous.y() + mm*(float)Math.sin(thetam);
							
							Node nn = new Node(xm, ym); 
							Node nn_s = new Node(xm, -ym); // symmetric (xx')
							stride.add(nn);

							nodes.add(nn); numNodes++;
							nodes.add(nn_s); numNodes++;
							
							// update variables
							initAngle = initAngle + kapa;
							lm = (float)(Math.tan( (90 + initAngle)*toRad ));
							previous_thetam = current_thetam;
							Lp = mm;
							side_prev = bm;
							previous = new Node(nn);
							
			    		}
			    		previous = (Node) stride.get(0);
			    	}
			  
			    	na--;
				}
				
			    // Build Topology
				
			    graph = new TrussGraph(numNodes);
				
			    LinkedList<Integer> stride = new LinkedList<Integer>();
			    LinkedList<Integer> stride_sym = new LinkedList<Integer>();
			    stride.push(0);
			    stride_sym.push(1);
			    int crntN = 1;
			    
			    na = (int)Math.sqrt(ne/2);
			    
			    // index iterator
			    int off1 = 0;
			    while (na>0) {
				    ++crntN;
				    graph.addEdge(stride.getFirst(), crntN);
				    graph.addEdge(stride_sym.getFirst(), crntN);
				    
				    // index iterator
				    int off2 = 1;
				    
				    // dummy containers
				    LinkedList<Integer> strideTMP = new LinkedList<Integer>();
			    	LinkedList<Integer> stride_symTMP = new LinkedList<Integer>();
			    	if (na>1) {
			    		
			    		for (int i=0; i<na-1; i++) {
						int token1 = 0;
						if (!stride.isEmpty())
						    token1 = stride.pop();
						    
						    ++crntN; strideTMP.add(crntN);
						    graph.addEdge(token1+off1, crntN);
						    graph.addEdge(crntN-off2, crntN);
						    
						    int token2 = 1;
						    if (!stride_sym.isEmpty())
						    	token2 = stride_sym.pop();
						    
						    ++crntN; stride_symTMP.add(crntN);
						    graph.addEdge(token2+off1, crntN);
							graph.addEdge(crntN-2, crntN);
						    off2 = 2;
						    
			    		}
			    		off1 = 2;
			    	}
			    	stride = strideTMP;
			    	stride_sym = stride_symTMP;
			    	na--;
			    }			    			    
			    
				// Compare desired L with computed Lp
				if ( (Lp-L)<DSG_EPS) {
					
					/** Uncomment the following lines to print parameters' values **/
					
					// println("** Stopped after ", attempts, " iterations. **");
					// println();
					// println("For a Michell structure with:\n");
					// println("ne = ", ne, " h = ", h, "ft and L = ", L, "ft.");
					// println("The optimized gama found is: ", gama, " degrees.\n");
					
					// build truss
					michell.setGeometry(nodes);
					michell.setTopology(graph);
					
					break;
				}
				else { // step angle gama until L ~= Lp
					gama = gama + step;
					attempts++;
				}
			
			}
				
		}
	}
	
	public void analyzeMichell() {
            // Structural Analysis
	    
	    /*  
	     *  Build matrices for: Node coordinates, topology, 
	     *  support and load definitions
	     */
	    
	    int NN = michell.nodes().size();
	    double[][] nm = new double[NN][2];
	    int countN = 0;
	    for (Node n: michell.nodes()) {
	    	double[] tmp = {n.x(), n.y()};
	    	nm[countN++] = tmp;
	    }
	    
	    Matrix N = new Matrix(nm);
	    //Misc.printMatrix(N);
	    //println();
	    
	    double[][] tm = new double[michell.elements().size()][2];
	    int countT = 0;
	    for (int v=0; v<NN; v++) {
 	    	for (Integer w: michell.topology().adj(v)) {
	    	    double[] tmp = {v, w};
	    	    tm[countT++] = tmp;
	    	}
	    }
	    
	    Matrix T = new Matrix(tm);	    
	    //Misc.printMatrix(T);
	    //println();
		
	    double[][] sm = new double[4][2];
	    sm[0] = new double[]{0, 1};
	    sm[1] = new double[]{0, 2};
	    sm[2] = new double[]{1, 1};
	    sm[3] = new double[]{1, 2};
		
	    Matrix S = new Matrix(sm);
	    //Misc.printMatrix(S);
	    //println();
		
	    double[][] lm = new double[1][3];
	    lm[0] = new double[]{NN-1, 0, 80};
	    Matrix L = new Matrix(lm);
	    //Misc.printMatrix(L);
	    //println();		
		
	    Matrix[] fr = StructuralAnalysis.JointMethod(N, T, S, L);
	    //Misc.printMatrix(fr[0]);
	    //println();
	    //Misc.printMatrix(fr[1]);
	    //println();
		
	    michell.setForces(fr[0]);
	    michell.setRForces(fr[1]);
	    michell.computePerformance();
	}
	
	// Control listener for real time update of parametric variables
	public void controlEvent(ControlEvent theEvent) {
		
	    if (theEvent.isFrom(cp5.getController("h"))) {
	        int token = (int) theEvent.getController().getValue();
	        if (token == 0) {
	            h = 4;
	        } else if (token == 1) {
	            h = 8;
	        } else if (token == 2) {
	            h = 16;
	        }
	        michell = new Truss();
	        MichellStructure(nel, h, L);
	        analyzeMichell();
	    }
		
	    if (theEvent.isFrom(cp5.getController("num"))) {
	        int token = (int) theEvent.getController().getValue();
	        
	        if (token == 0) {
	            nel = 2;
	        } else if (token == 1) {
	            nel = 8;
	        } else if (token == 2) {
	            nel = 18;
	        } else if (token == 3) {
	            nel = 32;
	        } else if (token == 4) {
	            nel = 50;
	        } else if (token == 5) {
	            nel = 72;
	        } else if (token == 6) {
	            nel = 98;
	        } 
	        
	        michell = new Truss();
	        MichellStructure(nel, h, L);
	        analyzeMichell();
	    }	
	}
	
	public void keyPressed() {
	    if (key == 'p') // pdf export
	        record = true;
	    if (key == 'f') // display forces
		drawForces = !drawForces;
	}
	
	/**
	 *  Test client and sample execution.
	 */
	public static void main(String[] args) {}
	
}
