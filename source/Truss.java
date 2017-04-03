package mit4s48;

/*************************************************************************
 *  Compilation:  javac Truss.java
 *  Execution:    java Truss
 *  Dependencies: TrussGraph.java  Jama.Matrix  
 *                Misc.java  processing.core.PApplet
 *
 *  A Truss structure with N number of nodes or joints and E number of edges 
 *  or members. Geometry is represented as a list of nodes and a list of 
 *  members and Topology is represented as a directed graph.
 *  The primary operations are: calculate the performance of the Truss
 *  according to the forces over each of its members. 
 *  
 *  Includes methods for displaying Geometry using the PApplet object,
 *  which is the standard Processing API for graphics.
 *  
 *  @author Alexandros Charidis, Digital Structures, MIT
 *************************************************************************/

import java.util.ArrayList;

import Jama.Matrix;
import processing.core.PApplet;

public class Truss {

    private ArrayList<Node> N_;         // nodes or joints
    private ArrayList<TrussElement> E_; // edges or members
    private TrussGraph T_;              // digraph represents topology
    private Matrix F_;                  // member forces
    private Matrix R_;                  // reaction forces
    private float sFL = 0;              // performance
	
    /**
     *  Default constructor. Initializes an empty Truss.
     */
    public Truss() {
        N_  = new ArrayList<Node>(); 
	E_  = new ArrayList<TrussElement>();
	T_  = new TrussGraph(0);
    }
	
    /**
     *  Initializes a Truss using the input list of nodes N, an empty
     *  list of edges and an input Truss graph T.
     */
    public Truss(final ArrayList<Node> N, final TrussGraph T) {
        N_ = N;
	E_  = new ArrayList<TrussElement>();
	T_ = T;
    }
	
    // G E T T E R S
	
    public ArrayList<Node> nodes()    {  return N_;  }
    public TrussGraph      topology() {  return T_;  }
    public int             numNodes() {  return N_.size();  }
    public int          numElements() {  return this.elements().size();  }
    public float            sigmaFL() {  return sFL;  }
    public ArrayList<TrussElement> elements() {
        assert(!N_.isEmpty());
	assert(T_.N()>0);
	E_  = new ArrayList<TrussElement>();
	for (int v=0; v<N_.size(); v++) {
	    for (int w: T_.adj(v)) {
	        Node v_token = N_.get(v);
		Node w_token = N_.get(w);
		TrussElement e = new TrussElement(v_token, w_token);
		E_.add(e);
	    }
	}
	return E_;
    }
	
    // S E T T E R S
	
    public void setGeometry(ArrayList<Node> N) {  N_ = N;  }
    public void setTopology(TrussGraph T)      {  T_ = T;  }
    public void setForces(Matrix F)            {  F_ = F;  }
    public void setRForces(Matrix R)           {  R_ = R;  }
	
    /**
     *  Performance index:
     *  δ = Sum (􏰥|fi|·li / L), where fi the force on each element, li the length of each element, 
     *  and L the characteristic length of each element equal to unity (assuming all elements 
     *  have the same characteristic length). 
     */
    public void computePerformance() {
        if (!(F_.getRowDimension()>0)) {
            System.out.print("Compute forces first..\n");
	} else {
            ArrayList<TrussElement> tmp = elements();
	    // int num = tmp.size();
	    int count = 0;
	    float sigmafl = 0;
	    for (TrussElement te: tmp) {
	        float length = te.length();
		float fi = (float) F_.get(count++, 0);
		    sigmafl += length*Math.abs(fi);
	    }
		//System.out.print(sigmafl);
		sFL = sigmafl;
	}
    }
	
    // draw on screen using given Processing Applet
    public void draw(PApplet p5, boolean nodes, int scale) {
        for (int v=0; v<N_.size(); v++) {
	    for (int w: T_.adj(v)) {
	        Node v_token = N_.get(v);
		Node w_token = N_.get(w);
	        TrussElement e = new TrussElement(v_token, w_token);
		e.draw(p5, scale);
		if (nodes) {
		    v_token.draw(p5, scale);
		    w_token.draw(p5, scale);
		}
	    }
	}
    }
	
    // draw forces as text on each element
    public void drawForces(PApplet p5, int scale) {
        ArrayList<TrussElement> tmp = elements();
	// int num = tmp.size();
	int count = 0;
	for (TrussElement te: tmp) {
	    float f = (float) F_.get(count++, 0);
	    String txt = String.valueOf((int)f) + " kN";
	    float fx = te.fromN().x();
	    float fy = te.fromN().y();
	    float tx = te.toN().x();
	    float ty = te.toN().y();
	    float mx = (fx+tx)/2*scale;
	    float my = (fy+ty)/2*scale;
			
	    Misc.displaytext(p5, txt, p5.createFont("Times", 14, true), 10, 255, 'C', (int)mx, (int)my); 
	}
    }
	
    /**
     *  Test client and sample execution.
     */
    public static void main(String[] args) {}	
	
}
