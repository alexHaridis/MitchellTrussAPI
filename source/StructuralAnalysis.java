/*************************************************************************
 *  Compilation:  javac StructuralAnalysis.java
 *  Execution:    java StructuralAnalysis
 *  Dependencies: Jama.Matrix
 *
 *  A static method for structural analysis of trusses represented as matrices.
 *   
 *  Main routines include (in progress; last update April 2, 2017):
 *  1. An implementation of the method of joints for calculating unknown forces
 *     in a simple truss structure. Calculates element forces and reaction
 *     forces. 
 *  
 *  @author Alexandros Charidis, Digital Structures, MIT
 *************************************************************************/

import Jama.Matrix;

public class StructuralAnalysis {

	/**
	 * 
	 * @param N = node coordinates:
	 *        (number of nodes)-by-2 matrix with N(n,0) and N(n,1) the X and
	 *        Y coordinates of node n
	 * @param T = truss topology
	 *        (number of elements)-by-2 matrix with T(e,0) and T(e,1) the indices of
	 *        the starting and ending nodes of element e
	 * @param S = support definition
	 *        (number of fixities)-by-2 matrix with S(s,0) the index of a node and
	 *        S(s,1) = 1 or 2 depending on whether the fixed DOF is in the X or Y direction
	 * @param L = load definition
	 *        (number of loaded nodes)-by-3 matrix with L(n,0) the index of a loaded
	 *        node and L(n,1) and L(n,2) the loads applied to that node in the X and
	 *        Y directions
	 * @return array 2x1 containing two Jama Matricies:
	 *        0: F = element forces
	 *           (number of elements)-by-1 matrix with F(e,1) the force in element e
	 *        1: R = support reactions
	 *           (number of fixities)-by-1 matrix with R(f,1) the reaction developed 
	 *           by fixity f
	 */
	public static Matrix[] JointMethod(Matrix N, Matrix T, Matrix S, Matrix L) {
		int nNodes    = N.getRowDimension();
		int nFixities = S.getRowDimension();
		int nElements = T.getRowDimension();
		int dofs      = nNodes*2;
		if (dofs != nElements+nFixities)
			throw new IllegalArgumentException("The truss is indeterminate");
        Matrix Q = new Matrix(dofs, 1); // load vector
        int nLoadedNodes = L.getRowDimension(); // number of loaded nodes
        for (int i=0; i<nLoadedNodes; i++) {
        	int n = (int)L.get(i, 0); // node index
        	double Qx = L.get(i, 1);  // load in x direction
        	double Qy = L.get(i, 2);  // load in y direction
        	Q.set(2*n, 0, Qx);
        	Q.set(2*n+1, 0, Qy);
        }
        
        // initialize the force projection matrix A of size
        // (number of DOFs)-by-(number of DOFs):
        Matrix A = new Matrix(dofs, dofs);
        for (int i=0; i<nElements; i++) {
        	int n1 = (int)T.get(i, 0);
        	int n2 = (int)T.get(i, 1);
        	double n1x = N.get(n1, 0);
        	double n1y = N.get(n1, 1);
        	double n2x = N.get(n2, 0);
        	double n2y = N.get(n2, 1);
        	double dist = Math.pow(((n2x-n1x)*(n2x-n1x) + (n2y-n1y)*(n2y-n1y)), 0.5);
        	double cosa = (n2x-n1x)/dist;
        	double sina = (n2y-n1y)/dist;
        	A.set(2*n1, i, cosa);
        	A.set(2*n1+1, i, sina);
        	A.set(2*n2, i, -cosa);
        	A.set(2*n2+1, i, -sina);
        }
        
        for (int i=0; i<nFixities; i++) {
        	int n = (int)S.get(i, 0);
        	if ((int)S.get(i, 1)==1)
        		A.set(2*n, nElements+i, 1);
        	else if ((int)S.get(i, 1)==2)
        		A.set(2*n+1, nElements+i, 1);
        }
           
        Matrix result = A.solve(Q.uminus());
        // element forces
        Matrix F = result.getMatrix(0, nElements-1, 0, 0);
        // reaction forces
        Matrix R = result.getMatrix(nElements, result.getRowDimension()-1, 0, 0);
        
        // Return
	    Matrix[] out = new Matrix[2];
	    out[0] = F;
	    out[1] = R;
	    return out;
	}
	
	public static void main(String[] args) {}

}
