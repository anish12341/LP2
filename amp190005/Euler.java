/** Starter code for LP2
 *  @author rbk ver 1.0
 *  @author SA ver 1.1
 */

// change to your netid
package idsa;

import idsa.Graph.Vertex;
import idsa.Graph.Edge;
import idsa.Graph.GraphAlgorithm;
import idsa.Graph.Factory;
import idsa.Graph.Timer;

import java.util.Iterator;
import java.io.File;
import java.util.List;
import java.util.LinkedList;
import java.util.Scanner;


import java.io.File;
import java.util.*;

public class Euler extends GraphAlgorithm<Euler.EulerVertex> {
    static int VERBOSE = 1;
    int counter;
    Vertex start;
    List<Vertex> tour;
    static Graph globalGraph;
    // int strongComponents = 0;
    List<Set<Vertex>> strongComponents;
    // You need this if you want to store something at each node
    static class EulerVertex implements Factory {
        int state;
        Iterator<Edge> edgeIterator = null;

        EulerVertex(Vertex u) {
            state = 0;
            edgeIterator = null;
        }
        public EulerVertex make(Vertex u) { return new EulerVertex(u); }

    }

    // To do: function to find an Euler tour
    public Euler(Graph g, Vertex start) {
        super(g, new EulerVertex(null));
        this.start = start;
        this.strongComponents = new ArrayList<Set<Vertex>>();
        Euler.globalGraph = g;
        tour = new LinkedList<>();
        counter = 0;
    }

    public boolean dfs(Vertex src, List<Vertex> reachabilityList, Iterable<Edge> edgesToUse, boolean isReverse) {

        if(isReverse){
            dfsReverse(src,reachabilityList);
        }

        Stack<Vertex> stack = new Stack<>();
        // Push the current source node
        stack.push(src);

        while (stack.empty() == false){
            Vertex v = stack.pop();

            if(store.get(v).state ==  0){
                store.get(v).state = 1;
                if (!isEdgesSame(src)) {
                    return false;
                }
                reachabilityList.add(v);
            }

            for(Edge e: g.outEdges(v)){
                Vertex ver = e.otherEnd(v);
                if(store.get(ver).state == 0){
                    stack.push(ver);
                }
            }
        }

        this.store.get(src).state = 2;
        return true;
    }

    public boolean dfsReverse(Vertex src, List<Vertex> reachabilityList){
        if (!isEdgesSame(src)) {
            return false;
        }

        Stack<Vertex> stack = new Stack<>();
        // Push the current source node
        stack.push(src);

        while (stack.empty() == false){
            Vertex v = stack.pop();

            if(store.get(v).state ==  0){
                store.get(v).state = 1;
                if (!isEdgesSame(src)) {
                    return false;
                }
                reachabilityList.add(v);
            }

            for(Edge e: g.inEdges(v)){
                Vertex ver = e.otherEnd(v);
                if(store.get(ver).state == 0){
                    stack.push(ver);
                }
            }
        }

        this.store.get(src).state = 2;
        return true;
    }

    public boolean isEdgesSame(Vertex src) {
        if (g.directed) {
            if (src.inDegree() == src.outDegree()) {
                return true;
            }
        } else {
            if (src.outDegree()%2==0) {
                return true;
            }
        }
        return false;
    }

    public boolean dfsAll(Graph g) {
        Vertex[] vertexes = g.getVertexArray();
        for(Vertex v:vertexes) {
            store.get(v).state = 0;
        }

        Vertex[] vertexArray = g.getVertexArray();
        if (vertexArray.length > 0) {
            List<Vertex> reachabiList = new LinkedList<>();

            if(!dfs(vertexArray[0], reachabiList, g.outEdges(vertexArray[0]), false))
                return false;

            Set<Vertex> reachableHashSet;
            if (g.isDirected()) {
                List<Vertex> reachabiListReverse = new LinkedList<>();
                makeAllZero(g);
                if(!dfs(vertexArray[0], reachabiListReverse, g.inEdges(vertexArray[0]), true))
                    return false;
                /*System.out.println("Reachability of " + vertexArray[0].getName());
                for (Vertex i: reachabiList) {
                    System.out.print(i.getName() + " ");
                }
                System.out.println();
                System.out.println("Reachability of " + vertexArray[0].getName() + " in reverse");
                for (Vertex i: reachabiListReverse) {
                    System.out.print(i.getName() + " ");
                }*/
                System.out.println();
                reachableHashSet = convertToSet(reachabiList);
                Set<Vertex> reachableReverseHashSet = convertToSet(reachabiListReverse);
                reachableHashSet.retainAll(reachableReverseHashSet);
                /*System.out.println("Common components of " + vertexArray[0].getName());*//*System.out.println("Common components of " + vertexArray[0].getName());*/
                // putInComp(reachableHashSet);
                // if (reachableHashSet.size() == 0) {
            } else {
                reachableHashSet = convertToSet(reachabiList);
            }
            reachableHashSet.add(vertexArray[0]);

            if (reachableHashSet.size() == vertexArray.length)
                return true;
            return false;
        }
        return false;
    }

    public HashSet<Vertex> convertToSet(List<Vertex> list) {
        return new HashSet<Vertex>(list);
    }

    public void makeAllZero(Graph g) {
        for(Vertex v:g.getVertexArray()) {
            store.get(v).state = 0;
        }
    }

    public boolean isStronglyConnected() {
        if(dfsAll(g)) {
            return true;
        } else {
            return false;
        }
    }

    /* To do: test if the graph is Eulerian.
     * If the graph is not Eulerian, it prints the message:
     * "Graph is not Eulerian" and one reason why, such as
     * "inDegree = 5, outDegree = 3 at Vertex 37" or
     * "Graph is not strongly connected"
     */
    public boolean isEulerian() {
        if (g.isDirected()) {
            System.out.println("I am directed");
            if (isStronglyConnected()) {
                return true;
            }
            return false;
        } else {
        }
        return false;
    }


    public List<Vertex> findEulerTour() {
        if(!isEulerian()) {
            System.out.println("NOT Capable of having euler tour");
            return new LinkedList<Vertex>();
        } else {
            System.out.println("Capable of having euler tour");
            eulerAll();
        }
        // Graph is Eulerian...find the tour and return tour
        return tour;
    }

    public void eulerAll() {
        Vertex[] vertexArray = g.getVertexArray();
        System.out.println("Start: " + this.start.getName());
        eachEulerTour(this.start);

    }

    public void eachEulerTour(Vertex src) {

        Stack<Vertex> stack = new Stack<>();
        stack.push(src);


        while (stack.empty() == false) {
            Iterator<Edge> temp = null;
            Vertex v = stack.peek();

            if (store.get(v).edgeIterator == null) {
                store.get(v).edgeIterator = g.outEdges(v).iterator();
            }
            temp = store.get(v).edgeIterator;

            if (temp.hasNext()) {
                Vertex ver = temp.next().otherEnd(v);
                stack.push(ver);
            }
            else{
                tour.add(0, stack.pop());
                counter+=1;
            }
        }

    }



    public void printEulerTour() {
        /*System.out.println("here");*/
        for (Vertex v: tour) {
            System.out.print(v + " ");
        }
        System.out.println();
        tour = new LinkedList<>();
    }

    public void checkGraphIterator() {
        System.out.println("Checking");
        Edge[] e = g.getEdgeArray();
        for (Edge each : e) {
            System.out.println(each.toString());
        }

    }
    public static void main(String[] args) throws Exception {
        Scanner in;
        /*if (args.length == 1) {
            System.out.println("I am here");
            in = new Scanner(new File(args[0]));
        } else if (args.length > 1) {
            // in = new Scanner(System.in);
            in = new Scanner(new File(args[0]));
        } else {
            // String input = "9 13  1 2 1  2 3 1  3 1 1  3 4 1  4 5 1  5 6 1  6 3 1  4 7 1  7 8 1  8 4 1  5 7 1  7 9 1  9 5 1";
            String input = "8 12  1 2 1  2 3 1  3 1 1  2 6 1  6 5 1  6 4 1  4 2 1  4 5 1  5 7 1  7 6 1  5 8 1  8 4 1";
            in = new Scanner(input);
        }*/
        in = new Scanner(new File("C:\\Users\\Ishan\\IdeaProjects\\Implementation of DS and Algo\\LP2Git\\src\\ixs180019\\test4-cycles.txt")) ;
        int start = 1;
        if(args.length > 1) {
            start = Integer.parseInt(args[1]);
        }
        // output can be suppressed by passing 0 as third argument
        if(args.length > 2) {
            VERBOSE = Integer.parseInt(args[2]);
        }
        Graph g = Graph.readDirectedGraph(in);
        Vertex startVertex = g.getVertex(start);
        Timer timer = new Timer();
        g.printGraph(false);

        Euler euler = new Euler(g, startVertex);
        euler.checkGraphIterator();
        List<Vertex> tour = euler.findEulerTour();

        System.out.println();
        timer.end();
        if(VERBOSE > 0) {
            System.out.println("Output:");
            euler.printEulerTour();
            System.out.println();
            System.out.println("Vertices:"+ euler.counter);
        }
        System.out.println(timer);


    }

    public void setVerbose(int ver) {
        VERBOSE = ver;
    }
}