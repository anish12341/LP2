/** Starter code for SP5
 *  @author rbk
 */

// change to your netid
package amp190005;

import amp190005.Graph.Vertex;
import amp190005.Graph.Edge;
import amp190005.Graph.GraphAlgorithm;
import amp190005.Graph.Factory;
import amp190005.Graph.Timer;

import java.io.File;
import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.Set;
import java.util.HashMap;
import java.util.HashSet;

public class DFS extends GraphAlgorithm<DFS.DFSVertex> {
	List<Vertex> topPostOrder;
    
    public static class DFSVertex implements Factory{
		int state;   //0-new, 1-active or 2-finished
		
		public DFSVertex(Vertex u) {
			state = 0;
		}
		public DFSVertex make(Vertex u) { return new DFSVertex(u); }
    }

    public DFS(Graph g) {
    	super(g, new DFSVertex(null));
    	// time = 0;
    	topPostOrder = new ArrayList<Vertex>();
    }
    
    
    public boolean dfs(Vertex src, List<Integer> reachabilityList) {
		this.store.get(src).state = 1;
    	
    	for(Graph.Edge e: g.outEdges(src)){
    		Vertex v = e.otherEnd(src);
    		if(this.store.get(v).state==1) { // Cycle found in the graph
    			return false;
    		}
    		if(this.store.get(v).state==0) {
				reachabilityList.add(v.getName());
    			if(!dfs(v, reachabilityList))
    				return false;
    		}
    	} 
    	
    	this.store.get(src).state = 2;
    	// this.topPostOrder.add(src);
    	return true;
	}
	
	public boolean dfsReverse(Vertex src, List<Integer> reachabilityList) {
		this.store.get(src).state = 1;
    	
    	for(Graph.Edge e: g.inEdges(src)){
			Vertex v = e.otherEnd(src);
			// System.out.println("The other end: "+ v.getName() + " from source: " + src.getName());
    		if(this.store.get(v).state==1) { // Cycle found in the graph
    			return false;
    		}
    		if(this.store.get(v).state==0) {
				// System.out.println("The other end 2nd: "+ v.getName() + " from source: " + src.getName());
				reachabilityList.add(v.getName());
    			if(!dfsReverse(v, reachabilityList))
    				return false;
    		}
    	} 
    	
    	this.store.get(src).state = 2;
    	// this.topPostOrder.add(src);
    	return true;
    }

    public boolean dfsAll(Graph g) {
    	if (!g.isDirected()) {
			return false;
		}
    	
    	Vertex[] vertexes = g.getVertexArray();
    	for(Vertex v:vertexes) {
			store.get(v).state = 0;                            
		}	
    	
		for(Vertex v:vertexes) {
			// if (store.get(v).state==0) {
				makeAllZero(g);
				List<Integer> reachabiList = new LinkedList<>();
				List<Integer> reachabiListReverse = new LinkedList<>();

				if(!dfs(v, reachabiList))
					return false;
				makeAllZero(g);
				if(!dfsReverse(v, reachabiListReverse))
					return false;
				System.out.println("Reachability of " + v.getName());
				for (Integer i: reachabiList) {
					System.out.print(i + " ");
				}
				System.out.println();
				System.out.println("Reachability of " + v.getName() + " in reverse");
				for (Integer i: reachabiListReverse) {
					System.out.print(i + " ");
				}
				System.out.println();
				Set<Integer> reachableHashSet = convertToSet(reachabiList);
				Set<Integer> reachableReverseHashSet = convertToSet(reachabiListReverse);
				reachableHashSet.retainAll(reachabiListReverse);

				System.out.println("Common components of " + v.getName());
				for (Integer setCon : reachableHashSet) {
					System.out.print(setCon + " ");
				}
				System.out.println();
			// }
		}	
		
		return true;
	}
	
	public HashSet<Integer> convertToSet(List<Integer> list) {
		return new HashSet<Integer>(list);
	}

	public void makeAllZero(Graph g) {
		for(Vertex v:g.getVertexArray()) {
			store.get(v).state = 0;
		}
	}
   
    
    public static List<Vertex> topologicalOrder1(Graph g) {
    	DFS d = new DFS(g);
    	if(!d.dfsAll(g)) {
    		System.out.println();
    		System.out.println("Given graph is not a Directed Acyclic Graph. Topological ordering is not possible.");
    		return null;
    	}
    	return d.topPostOrder;
    }


    public static void main(String[] args) throws Exception {
		String string = "6 6   5 6 2   4 6 3   2 3 5   3 1 4   5 2 1   4 1 1";
		Scanner in;
		// If there is a command line argument, use it as file from which
		// input is read, otherwise use input from string.
		in = args.length > 0 ? new Scanner(new File(args[0])) : new Scanner(string);
		
		// Read graph from input
	    Graph g = Graph.readGraph(in, true);
		g.printGraph(false);
		
		//topological sort
		List<Vertex> topOrderSort = topologicalOrder1(g);
		
		//display topological order
		if(topOrderSort!=null) {
			System.out.println();
			System.out.println("Topological Order:");
			for (int i = topOrderSort.size()-1; i>=0; i--) {
				System.out.print(" "+topOrderSort.get(i).getName());
			}
			System.out.println();
		}	
    }
}