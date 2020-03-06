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
	List<Set<Vertex>> strongComponents;
	
    public static class DFSVertex implements Factory{
		int state;   //0-new, 1-active or 2-finished
		boolean inComponent = false;

		public DFSVertex(Vertex u) {
			state = 0;
		}
		public DFSVertex make(Vertex u) { return new DFSVertex(u); }
    }

    public DFS(Graph g) {
    	super(g, new DFSVertex(null));
		strongComponents = new ArrayList<Set<Vertex>>();
    }
    
    
	public boolean dfs(Vertex src, List<Vertex> reachabilityList) {
		this.store.get(src).state = 1;
    	
    	for(Graph.Edge e: g.outEdges(src)){
    		Vertex v = e.otherEnd(src);
    		if(this.store.get(v).state==0) {
				reachabilityList.add(v);
    			if(!dfs(v, reachabilityList))
    				return false;
    		}
    	} 
    	
    	this.store.get(src).state = 2;
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
			if (!store.get(v).inComponent) {
				makeAllZero(g);
				List<Vertex> reachabiList = new LinkedList<>();
				List<Vertex> reachabiListReverse = new LinkedList<>();

				if (!dfs(v, reachabiList))
					return false;
				makeAllZero(g);
				g.reverseGraph();

				if(!dfs(v, reachabiListReverse))
					return false;
				System.out.println("Reachability of " + v.getName());
				for (Vertex i: reachabiList) {
					System.out.print(i.getName() + " ");
				}
				System.out.println();
				System.out.println("Reachability sdh of " + v.getName() + " in reverse");
				for (Vertex i: reachabiListReverse) {
					System.out.print(i.getName() + " ");
				}
				System.out.println();
				Set<Vertex> reachableHashSet = convertToSet(reachabiList);
				Set<Vertex> reachableReverseHashSet = convertToSet(reachabiListReverse);
				reachableHashSet.retainAll(reachableReverseHashSet);
				System.out.println("Common components of " + v.getName());
				putInComp(reachableHashSet);
				// if (reachableHashSet.size() == 0) {
				reachableHashSet.add(v);
				
				strongComponents.add(reachableHashSet);
				g.reverseGraph();
			}
		}	
		
		return true;
	}

	public void putInComp(Set<Vertex> vertexSet) {
		for (Vertex setCon : vertexSet) {
			System.out.print(setCon.getName() + " ");
			store.get(setCon).inComponent = true;
		}
		System.out.println();
	}
	
	public HashSet<Vertex> convertToSet(List<Vertex> list) {
		return new HashSet<Vertex>(list);
	}

	public void makeAllZero(Graph g) {
		for(Vertex v:g.getVertexArray()) {
			store.get(v).state = 0;
		}
	}
   
    
    public static List<Set<Vertex>> getStrongComponents(Graph g) {
    	DFS d = new DFS(g);
    	if(!d.dfsAll(g)) {
    		System.out.println();
    		System.out.println("Given graph is not a Directed Acyclic Graph. Topological ordering is not possible.");
    		return null;
    	}
    	return d.strongComponents;
    }


    public static void main(String[] args) throws Exception {
		// String string = "16 24   1 2 1   2 6 1   3 8 1   4 3 1   8 4 1   8 12 1   7 3 1   7 11 1   7 1 1   6 7 1   6 12 1   5 6 1   5 9 1   9 14 1   10 11 1   10 13 1   11 8 1   11 12 1   12 15 1   12 16 1   13 9 1   14 10 1   14 15 1   15 11 1";
		// String string = "6 13   1 2 1   1 3 1   2 1 1   2 3 1   3 2 1   3 1 1   3 4 1   4 5 1  4 6 1   5 4 1   5 6 1   6 4 1   3 5 1";
		String string = "8 12   1 2 1   2 6 1   2 3 1   3 1 1   4 2 1   4 5 1   5 8 1   5 7 1   6 4 1   6 5 1   7 6 1   8 4 1";
		Scanner in;
		// If there is a command line argument, use it as file from which
		// input is read, otherwise use input from string.
		in = args.length > 0 ? new Scanner(new File(args[0])) : new Scanner(string);
		
		// Read graph from input
	    Graph g = Graph.readGraph(in, true);
		g.printGraph(false);
		
		//Get all strong components
		List<Set<Vertex>> strongComponents = getStrongComponents(g);
		
		for(Set<Vertex> s: strongComponents) {
			System.out.println("New component:");
			for (Vertex v: s) {
				System.out.print(v.getName() + " ");
			}
			System.out.println();
		}
    }
}