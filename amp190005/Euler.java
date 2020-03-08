/** Starter code for LP2
 *  @author rbk ver 1.0
 *  @author SA ver 1.1
 */

// change to your netid
package amp190005;

import amp190005.Graph.Timer;
import amp190005.Graph.*;
import java.io.File;
import java.util.*;
import java.io.File;
import java.util.*;


/**
 * Euler (LP2)
 * This program finds a Euler path and its different
 * operations like dfs(), dfsReverse(), isEdgesSame(), dfsAll(), convertToSet(), makeALlZero(),
 * isStronglyConnected(), isEulerian(), findEulerTour(), eulerAll(), eachEulerTour(), printEulerTour(), checkGraphIterator()
 * @author Anish Patel      amp190005
 * @author Henil Doshi     hxd180025
 * @author Ishan Shah     ixs180019
 * @author Neel Gotecha     nxg180023
 * @version 1.0
 * @since 2020-03-08
 */
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

    /**
     * Constructor for initializing graph with starting vertex and
     * initialize other data structures store results
     * @param g Graph to be initialized, start is the starting vertex
     */
    public Euler(Graph g, Vertex start) {
        super(g, new EulerVertex(null));
        this.start = start;
        this.strongComponents = new ArrayList<Set<Vertex>>();
        Euler.globalGraph = g;
        tour = new LinkedList<>();
        counter = 0;
    }

    /**
     * This method is used to dfs the graph from source vertex in normal order
     * @param src is the source vertex,
     *        reachabilityList stores the vertices that can be reached from current vertex,
     *        edgesToUse stores the iterable object of the edges of current vertex,
     *        isReverse tells the method if the graph is to be traversed in reverse order.
     * @return boolean returns if the graph can be traversed or not
     */
    public boolean dfs(Vertex src, List<Vertex> reachabilityList, Iterable<Edge> edgesToUse, boolean isReverse) {

        if(isReverse){
            return dfsReverse(src,reachabilityList);
        }

        Stack<Vertex> stack = new Stack<>();
        // Push the current source node
        stack.push(src);

        while (stack.empty() == false){
            Vertex v = stack.pop();

            if(store.get(v).state ==  0){
                store.get(v).state = 1;
                if (!isEdgesSame(v)) {
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

    /**
     * This method is used to dfs the graph from source vertex in reverse order
     * @param src is the source vertex,
     *        reachabilityList stores the vertices that can be reached from current vertex,
     * @return boolean returns if the graph can be traversed or not
     */
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
                if (!isEdgesSame(v)) {
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

    /**
     * This method checks if the number of outgoing and incoming edges from a vertex are same or not
     * @param src is the source vertex,
     * @return boolean returns if number of outgoing and incoming edges are same or not
     */
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

    /**
     * This method does dfs traversal on entire graph
     * @param g graph to be traversed,
     * @return boolean returns if the graph got traversed or not
     */
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

    /**
     * This method converts list to set
     * @param src is the source vertex,
     * @return boolean returns if number of outgoing and incoming edges are same or not
     */
    public HashSet<Vertex> convertToSet(List<Vertex> list) {
        return new HashSet<Vertex>(list);
    }

    /**
     * This method changes state of all the vertices to 0
     * @param g graph whose vertices state should be changed,
     * @return nothing
     */
    public void makeAllZero(Graph g) {
        for(Vertex v:g.getVertexArray()) {
            store.get(v).state = 0;
        }
    }

    /**
     * This method checks if the graph is strongly connected or not
     * @param nothing
     * @return boolean returns if the graph is strongly connected or not
     */
    public boolean isStronglyConnected() {
        if(dfsAll(g)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * This method checks if the graph is Eulerian or not
     * @param nothing
     * @return boolean returns if the graph is Eulerian or not
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


    /**
     * This method finds Euler tour of the graph
     * @param nothing
     * @return List returns list of euler tour
     */
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


    /**
     * This method checks if the graph is Eulerian or not
     * @param nothing
     * @return nothing
     */
    public void eulerAll() {
        Vertex[] vertexArray = g.getVertexArray();
        System.out.println("Start: " + this.start.getName());
        eachEulerTour(this.start);

    }


    /**
     * This method is the implmentation of the Euler tour
     * @param src, source vertex from where tour should start
     * @return nothing
     */
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

    /**
     * This method prints the Euler tour
     * @param nothing
     * @return nothing
     */
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

    /**
     * This is the main method that tests the Euler graph
     * @param args Takes in graph as input. If not provided, then by default graph is given statically, else this argument.
     * @return nothing
     * @throws java.lang.Exception General exception
     */
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
            System.out.println("Edges visited:"+ euler.counter-1);
        }
        System.out.println(timer);


    }

    public void setVerbose(int ver) {
        VERBOSE = ver;
    }
}