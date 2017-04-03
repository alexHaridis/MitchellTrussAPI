/*************************************************************************
 *  Compilation:  javac TrussGraph.java
 *  Execution:    java TrussGraph
 *  Dependencies: None
 *
 *  A digraph, which represents a truss structure with N number of nodes
 *  or joints, and K number of edges or members.
 *  
 *  This implementation uses an adjacency-lists representation, which 
 *  is a vertex-indexed ArrayList of ArrayList objects. The primary
 *  operations are: add directed edge to the truss graph, iterate over
 *  all the nodes adjacent to a given node.
 *  
 *  @author Alexandros Charidis, Digital Structures, MIT
 *************************************************************************/

import java.util.ArrayList;

public class TrussGraph {

    private ArrayList<Integer>[] adj_; // adjacency list, e.g. adj_[v] = adjacency list for vertex v
    private final int V_;              // number of nodes (truss joints)
    private int       E_;              // number of edges (truss members)
    
    /**
     *  Initializes an empty Truss Graph with V nodes.
     *
     *  @throws RuntimeException if V < 0
     */
    public TrussGraph(int V) {
    	if (V < 0) throw new RuntimeException("Number of nodes must be nonnegative");
    	this.V_ = V;
    	this.E_ = 0;
    	adj_ = (ArrayList<Integer>[]) new ArrayList[V];
    	for (int i=0; i<V; i++) {
    	    adj_[i] = new ArrayList<Integer>();
    	}
    }
	
    /**
     *  Validate that v is a valid index
     *
     *  @throws IndexOutOfBoundsException unless (0 <= v < V_)
     */
    private void validate(int v) {
        if (v < 0 || v >= V_) {
            throw new IndexOutOfBoundsException("index " + v + " is not between 0 and " + V_);
        }
    }
    
    // adds directed edge v→w
    public void addEdge(int v, int w) {
    	validate(v);
        validate(w);
        adj_[v].add(w);
        E_++;
    }
    
    // G E T T E R S
    public int N() {  return V_;  }
    public int E() {  return E_;  }
    
    // Returns the nodes adjacent from node v in this graph.
    public Iterable<Integer> adj(int v) {
    	validate(v);
        return adj_[v];
    }
    
    /**
     *  Returns a string representation of the graph.
     *
     *  @return the number of nodes, followed by the number of edges,  
     *          followed by the adjacency lists of each node
     */
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(V_ + " vertices, " + E_ + " edges \n");
        for (int v = 0; v < V_; v++) {
            s.append(String.format("%d: ", v));
            for (int w : adj_[v]) {
                s.append(String.format("%d ", w));
            }
            s.append("\n");
        }
        return s.toString();
    }
    
    /**   Client test and sample execution.  */
    public static void main(String[] args) {}
	
}
