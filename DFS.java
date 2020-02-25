/** Starter code for SP5
 *  @author henil
 */


package nxg180023;

import nxg180023.Graph.Vertex;
import nxg180023.Graph.Edge;
import nxg180023.Graph.GraphAlgorithm;
import nxg180023.Graph.Factory;
import nxg180023.Graph.Timer;

import java.io.File;
import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Scanner;

// This class is used to the depth first search in the graph
public class DFS extends GraphAlgorithm<DFS.DFSVertex> {
	List<Vertex> topologicalOrdering;
    public static class DFSVertex implements Factory{
		int status;   //0 for new vertex, 1 for active vertex, 2 for finished vertex
        int cno;
		public DFSVertex(Vertex u) {
			status = 0;
		}
		public DFSVertex make(Vertex u) { return new DFSVertex(u); }
    }

    /*
    * Creates Vertex objects and a new array in which the topologically sorted vertexes will be stored.
    * @param : Graph g
    */
    public DFS(Graph g) {
    	super(g, new DFSVertex(null));
    	topologicalOrdering = new ArrayList<Vertex>();
    }
    
    
    /*
    * Executes the dfs for a vertex
    * @param : source It takes source as a parameter
    * @return : boolean 
    */
    public boolean dfs(Vertex source) {
    	this.store.get(source).status = 1;
    	
    	for(Graph.Edge e: g.outEdges(source)){
    		Vertex v = e.otherEnd(source);
    		if(this.store.get(v).status==1) {
    			System.out.println("Traversal stops: found a cycle");
    			return false;
    		}
    		if(this.store.get(v).status==0) {
    			if(!dfs(v))
    				return false;
    		}
    	} 
    	this.store.get(source).status = 2;
    	this.topologicalOrdering.add(source);
    	return true;
    }

    /*
    * Executes dfs for all the vertex one by one
    * @param : Graph g
    * @return : Boolean
    */
    public boolean dfsAll(Graph g) {
    	if (!g.isDirected()) return false;               //if Graph g is undirected graph, return false
    	Vertex[] vertexes = g.getVertexArray();
    	for(Vertex v:vertexes) {
			store.get(v).status = 0;                            
		}	
    	for(Vertex v:vertexes) {
			if (store.get(v).status==0) {				 //for all the vertexes that were not visited before, visit it
				if(!dfs(v))
					return false;
			}
		}	
		
		return true;
    }
   
    /*
    * Member function to find topological order
    * @param : Graph g
    * @return : List<vertex> list in which the the vertexes are stored in topological order.
    */
    public static List<Vertex> topologicalOrder1(Graph g) {
    	DFS d = new DFS(g);
    	if(!d.dfsAll(g)) {
    		System.out.println();
    		System.out.println("There is a cycle in the graph. Topological ordering is only possible if Graph is a Directed Acyclic Graph.");
    		return null;
    	}
    	
    	return d.topologicalOrdering;
    }


    public static void main(String[] args) throws Exception {
		String string = "6 6   5 6 2   4 6 3   2 3 5   3 1 4   5 2 1   4 1 1";
        // String string = "7 8   1 2 2   1 3 3   2 4 5   3 4 4   4 5 1   5 1 7   6 7 1   7 6 1 0";

		Scanner in;
		// If there is a command line argument, use it as file from which
		// input is read, otherwise use input from string.
		in = args.length > 0 ? new Scanner(new File(args[0])) : new Scanner(string);
		
		// Read graph from input
	    Graph g = Graph.readGraph(in, true);
		g.printGraph(false);
		
		
		//topological sort
		List<Vertex> topolog = topologicalOrder1(g);
		
		//display topological order
		if(topolog!=null) {
			System.out.println();
			System.out.println("Topological Order:");
			for (int i = topolog.size()-1; i>=0; i--) {
				System.out.print(" "+topolog.get(i).getName());
			}
		}
	
    }
}