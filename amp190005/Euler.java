/**
 * Euler (LP2)
 * This program finds a Euler path and its different
 * operations like dfs(), dfsReverse(), isEdgesSame(), dfsAll(), convertToSet(), makeALlZero(),
 * isStronglyConnected(), isEulerian(), findEulerTour(), eachEulerTour(), printEulerTour(), checkGraphIterator()
 * @author Anish Patel      amp190005
 * @author Henil Doshi     hxd180025
 * @author Ishan Shah     ixs180019
 * @author Neel Gotecha     nxg180023
 * @version 1.0
 * @since 2020-03-08
 */

package amp190005;

import amp190005.Graph.Timer;
import amp190005.Graph.*;

import java.util.*;

public class Euler extends GraphAlgorithm<Euler.EulerVertex> {
    static int VERBOSE = 1;
    int counter;
    Vertex start;
    List<Vertex> tour;
    static Graph globalGraph;
    List<Set<Vertex>> strongComponents;
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

                System.out.println();
                reachableHashSet = convertToSet(reachabiList);
                Set<Vertex> reachableReverseHashSet = convertToSet(reachabiListReverse);
                reachableHashSet.retainAll(reachableReverseHashSet);

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
     * @param list is the list to be converted to set,
     * @return HashSet converts list to set
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
     * @return boolean returns if the graph is Eulerian or not
     */
    public boolean isEulerian() {
        if (g.isDirected()) {
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
     * @return List returns list of euler tour
     */
    public List<Vertex> findEulerTour() {
        if(!isEulerian()) {
            System.out.println("NOT Capable of having euler tour");
            return new LinkedList<Vertex>();
        } else {
            System.out.println("Capable of having euler tour");
            eachEulerTour(this.start);
        }
        return tour;
    }

    /**
     * This method is the implementation of the Euler tour
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
     * @return nothing
     */
    public void printEulerTour() {
        for (Vertex v: tour) {
            System.out.print(v + " ");
        }
        System.out.println();
        tour = new LinkedList<>();
    }


    /**
     * This is the main method that tests the Euler graph
     * @param args Takes in graph as input. If not provided, then by default graph is given statically, else this argument.
     * @return nothing
     * @throws java.lang.Exception General exception
     */
    public static void main(String[] args) throws Exception {
        Scanner in;
        if (args.length > 1) {
            in = new Scanner(System.in);
        } else {
            String input = "9 12 1 2 1 2 3 1 3 1 1 3 4 1 4 5 1 5 6 1 6 3 1 4 7 1 7 8 1 8 4 1 5 7 1 7 9 1";
            in = new Scanner(input);

        }
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
        Euler euler = new Euler(g, startVertex);

        timer.end();
        if(VERBOSE > 0) {
            System.out.println("Output:");
            List<Vertex> tour = euler.findEulerTour();
            euler.printEulerTour();
            System.out.println();
            //Uncomment line below to see number of edges visited
            /*System.out.println("Edges visited:"+ (euler.counter-1));*/
        }
        System.out.println(timer);
    }

    public void setVerbose(int ver) {
        VERBOSE = ver;
    }
}
