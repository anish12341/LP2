/**
 * PERT
 * This class implements a Project Evaluation and Review Technique(PERT) Algorithm
 * 
 * @author Anish Patel amp190005
 * @author Henil Doshi hxd180025
 * @author Ishan Shah ixs180019
 * @author Neel Gotecha nxg180023
 * @version 1.0
 * @since 2020-02-03
 *
 */

package amp190005;

import amp190005.Graph.Vertex;
import amp190005.Graph.GraphAlgorithm;
import amp190005.Graph.Factory;
import amp190005.TopOrder;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Scanner;

/**
 * This class executes the PERT Algorithm. It uses the classes Graph, TopOrder and PERTVertex.
 */
public class PERT extends GraphAlgorithm<PERT.PERTVertex> {
	List<Vertex> topOrderSort; 	//List of vertices to store the topological order
	
	/**
	 * This class creates a vertex as to be used by the PERT Algorithm
	 */
	public static class PERTVertex implements Factory {
    	int es,ls,ef,lf,duration,slack;
    	
    	/**
    	 * Constructor for PERTVertex which sets earliest start time to 0
    	 * @param u PERT vertex whose earliest start time is being set
    	 */
    	public PERTVertex(Vertex u) {
    		this.es = 0;
    	}
    	
    	/**
    	 * This method makes a PERT vertex
    	 * @param u Vertex who will be used by PERTVertex 
    	 * @return PERTVertex which got created
    	 */
    	public PERTVertex make(Vertex u) { 
    		return new PERTVertex(u); 
		}
    }
	
	/**
	 * Constructor for class PERT
	 * @param g Graph on which PERT algorithm will run
	 */
    public PERT(Graph g) {
    	super(g, new PERTVertex(null));
    }
    
    /**
     * This is method sets duration of a Vertex
     * @param u Vertex whose duration is being set
     * @param d duration of vertex
     * @return nothing
     */
    public void setDuration(Vertex u, int d) {
    	store.get(u).duration = d;
    }
    
    /**
     * This method is used to reverse a list. This method is used in pert method reverse the topOrderSort
     * @param list list which will get reversed
     * @return List<Vertex> which got reversed
     */
    public static List<Vertex> reverseList(List<Vertex> list) {
    		List<Vertex> reverse = new ArrayList<>(list.size());
    		ListIterator<Vertex> itr = list.listIterator(list.size());
			while (itr.hasPrevious()) {
				reverse.add(itr.previous());
			}
    	return reverse;
	}
    
    /**
     * This method implements the PERT algorithm on the graph, only if Graph is a DAG,
     * else PERT algorithm cannot be applied on the given graph
     * @return boolean returns true if graph is not a DAG, else it returns false which means graph is a DAG
     */
    public boolean pert() {
    	//Adding edges from source(s) to all vertices and from all vertices to sink(t)
    	for (int i=2; i<g.size();i++) {
    		g.addEdge(g.getVertex(1), g.getVertex(i), 1, 1);
    		g.addEdge(g.getVertex(i), g.getVertex(g.size()), 1, 1);
    	}

    	//topological sort
    	topOrderSort = TopOrder.topologicalOrder1(g);

    	//display topological order
    	if(topOrderSort!=null) {
    	    topOrderSort = reverseList(topOrderSort);
    	    System.out.println();
    	    System.out.println("******After adding the edges*******");
    	    g.printGraph(false);
    		System.out.println();
 			System.out.println("Topological Order:");
    		for (int i = 0; i<topOrderSort.size(); i++) {
  				System.out.print(" "+topOrderSort.get(i).getName());
    		}
    		System.out.println();
    		for (Vertex u : topOrderSort) {
    			store.get(u).ef = store.get(u).es + store.get(u).duration;
    			for(Graph.Edge e: g.outEdges(u)) {
    				if (store.get(e.otherEnd(u)).es < store.get(u).ef) {
    					store.get(e.otherEnd(u)).es = store.get(u).ef;
    				}
    			}
    		}
    	
    		for(Vertex u : g) {
    			store.get(u).lf = store.get(topOrderSort.get(topOrderSort.size()-1)).ef;
    		}
    		
    		for(Vertex u : reverseList(topOrderSort)) {
    			this.store.get(u).ls = this.store.get(u).lf - this.store.get(u).duration;
    			this.store.get(u).slack = this.store.get(u).lf - this.store.get(u).ef;
    			for (Graph.Edge e: g.inEdges(u)) {
    				if(store.get(e.otherEnd(u)).lf > store.get(u).ls) {
    					store.get(e.otherEnd(u)).lf = store.get(u).ls;
    				}
    			}
    		}
    		return false;
    	}
    	else
    		return true;
    }
    
    /**
     * This method returns earliest finish time of a node
     * @param u Vertex of graph
     * @return int earliest finish time
     */
    public int ec(Vertex u) {
    	return store.get(u).ef;
    }
    
    /**
     * This method returns latest finish time of a node
     * @param u Vertex of graph
     * @return int latest finish time
     */
    public int lc(Vertex u) {
    	return store.get(u).lf;
    }
    
    /**
     * This method returns slack of a node
     * @param u Vertex of graph
     * @return int slack
     */
    public int slack(Vertex u) {
    	return store.get(u).slack;
    }
    
    /**
     * This method returns latest finish time of sink(t)
     * @param u Vertex of graph
     * @return int latest finish time of sink
     */
    public int criticalPath() {
    	return store.get(g.getVertex(g.size())).lf;
	}
   	
    /**
     * This method checks if vertex is critical(slack = 0) or not
     * @param u Vertex of graph
     * @return boolean returns true if vertex is critical else, return false
     */
    public boolean critical(Vertex u) {
    	if(store.get(u).slack==0)
    		return true;
    	else
    		return false;
    }
    
    /**
     * This method returns number of critical vertices in a graph
     * @return int number(count) of critical vertices
     */
    public int numCritical() {
    	int count = 0;
    	for(Vertex u : g) {
    		if (this.slack(u) == 0) {
    			count++;
    		}
    	}
    	return count;
    }

    /**
     * This method creates a PERT graph with the array of time durations given
     * @param g Graph
     * @param duration array which contains durations of all PERT Vertices
     * @return PERT Object
     */
    public static PERT pert(Graph g, int[] duration) {
    	PERT pert = new PERT(g);
    	for(Vertex u : g) {
    		pert.setDuration(u, duration[u.getIndex()]);
    	}
    	if(pert.pert()) {
    		return pert;
    	}
    	else {
    		return null;
    	}
    }
    
    /**
     * This is the main method which executes PERT Algorithm
     * @param args If there is a command line argument, use it as file from which
	 * input is read, otherwise use input from string.
     * @throws Exception
     * @return nothing
     */
    public static void main(String[] args) throws Exception {
	String graph = "11 12   2 4 1   2 5 1   3 5 1   3 6 1   4 7 1   5 7 1   5 8 1   6 8 1   6 9 1   7 10 1   8 10 1   9 10 1      0 3 2 3 2 1 3 2 4 1 0";
    Scanner in;
	// If there is a command line argument, use it as file from which
	// input is read, otherwise use input from string.
	in = args.length > 0 ? new Scanner(new File(args[0])) : new Scanner(graph);
	Graph g = Graph.readDirectedGraph(in);
	g.printGraph(false);
	
	PERT p = new PERT(g);
	for(Vertex u: g) {
	    p.setDuration(u, in.nextInt());
	}
	
	// Run PERT algorithm.  Returns null if g is not a DAG
	if(p.pert()) {
	    System.out.println("Invalid graph: not a DAG");
	} 
	else {
		System.out.println();
	    System.out.println("Number of critical vertices: " + p.numCritical());
	    System.out.println("Critical Path Length: " + p.criticalPath());
	    System.out.println();
	    System.out.println("u\tEC\tLC\tSlack\tCritical");
	    for(Vertex u: g) {
	    	System.out.println(u + "\t" + p.ec(u) + "\t" + p.lc(u) + "\t" + p.slack(u) + "\t" + p.critical(u));
	    }
	}
  }
}
