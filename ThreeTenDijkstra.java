//TODO: Nothing, all done.

import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.util.EdgeType;

import java.awt.Color;
import java.util.Collection;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.NoSuchElementException;

/**
 *  Simulation of Dijkstra's shortest path algorithm.
 *
 *  @author Katherine (Raven) Russell
 */
class ThreeTenDijkstra implements ThreeTenAlg {
    /**
     *  Wrapper class for nodes to add additional properties
     *  associated with this algorithm.
     */
    private class NodeForAlg implements Comparable<NodeForAlg> {
        /**
         *  The actual node from the graph.
         */
        ThreeTenNode graphNode = null;

        /**
         *  The parent of the node (indicates the shortest path trail).
         */
        ThreeTenNode parent = null;

        /**
         *  Whether or not the node is considered "done".
         */
        boolean done = false;

        /**
         *  The distance to the node.
         */
        int distance = Integer.MAX_VALUE;

        /**
         *  Constructs a new algorithm node from a graph node.
         *
         *  @param node the graph node to wrap		 */
        public NodeForAlg(ThreeTenNode node) { this.graphNode = node; }

        /**
         *  {@inheritDoc}
         */
        @Override
        public int compareTo(NodeForAlg o) {
            if(this.distance == o.distance)
                return this.graphNode.compareTo(o.graphNode);
            return this.distance - o.distance;
        }
    }

    /**
     *  The graph the algorithm will run on.
     */
    Graph<ThreeTenNode, ThreeTenEdge> graph;

    /**
     *  Whether or not the algorithm has been started.
     */
    boolean started = false;

    /**
     *  The priority queue of nodes for the algorithm.
     */
    WeissPriorityQueue<NodeForAlg> queue;

    /**
     *  A mapping of graph nodes to algorithm nodes.
     */
    HashMap<ThreeTenNode,NodeForAlg> graphNodesToAlgNodes;

    /**
     *  The last minimum node chosen by the algorithm.
     */
    NodeForAlg lastMin = null;

    /**
     *  The color when a node is "done".
     */
    public static final Color COLOR_DONE_NODE = Color.GREEN;

    /**
     *  The color when an edge is done AND in use for a shortest path.
     */
    public static final Color COLOR_DONE_EDGE_1 = Color.GREEN.darker();

    /**
     *  The color when an edge is done AND NOT in use for a shortest path.
     */
    public static final Color COLOR_DONE_EDGE_2 = Color.LIGHT_GRAY;

    /**
     *  The color when a node has been selected as the minimum.
     */
    public static final Color COLOR_ACTIVE_NODE_1 = Color.RED;

    /**
     *  The color when a node has been updated by the minimum.
     */
    public static final Color COLOR_ACTIVE_NODE_2 = Color.YELLOW;

    /**
     *  The color when an edge has been used for an update.
     */
    public static final Color COLOR_ACTIVE_EDGE = Color.ORANGE;

    /**
     *  The color when a node has "no color".
     */
    public static final Color COLOR_NONE_NODE = Color.WHITE;

    /**
     *  The color when an edge has "no color".
     */
    public static final Color COLOR_NONE_EDGE = Color.BLACK;

    /**
     *  The infinity sign.
     */
    public static final String INFINITY_SIGN = "\u221e";

    /**
     *  {@inheritDoc}
     */
    public EdgeType graphEdgeType() {
        return EdgeType.DIRECTED;
    }

    /**
     *  {@inheritDoc}
     */
    public void reset(Graph<ThreeTenNode, ThreeTenEdge> graph) {
        this.graph = graph;
        started = false;
    }

    /**
     *  Gets the next minimum id node from the available nodes. This
     *  is chosen as the starting spot for the shortest path algorithm.
     *
     *  @return the node with the minimum id
     */
    public ThreeTenNode getMinimumIdNode() {
        ThreeTenNode minIdNode = null;

        for(ThreeTenNode n : graph.getVertices()) {
            if(minIdNode == null || n.compareTo(minIdNode) < 0) {
                minIdNode = n;
            }
        }

        return minIdNode;
    }

    /**
     *  {@inheritDoc}
     */
    public void start() {
        //sets all nodes to infinity sign except one with
        //the minimum id, which is set to 0

        //Add/change anything you want here.

        this.started = true;
        this.lastMin = null;

        for(ThreeTenNode v : graph.getVertices()) {
            v.setText(INFINITY_SIGN);
        }

        ThreeTenNode minIdNode = getMinimumIdNode();
        minIdNode.setText("0");

        queue = new WeissPriorityQueue<>();
        graphNodesToAlgNodes = new HashMap<>();

        for(ThreeTenNode v : graph.getVertices()) {

            NodeForAlg n = new NodeForAlg(v);
            if(v.equals(minIdNode)) {
                n.distance = 0;
            }
            graphNodesToAlgNodes.put(v,n);
            queue.add(n);
        }
    }

    /**
     *  Returns all the unused edges in the graph (for recoloring
     *  at the end).
     *
     *  @return a collection of edges not used for any path
     */
    public Collection<ThreeTenEdge> getUnusedEdges() {
        ArrayList<ThreeTenEdge> l = new ArrayList<>();

        for(ThreeTenEdge e : graph.getEdges()) {
            if(e.getColor() == COLOR_NONE_EDGE) {
                l.add(e);
            }
        }

        return l;
    }

    /**
     *  {@inheritDoc}
     */
    public void finish() {
        //sets all unused edges to grey color
        for(ThreeTenEdge e : getUnusedEdges()) {
            e.setColor(COLOR_DONE_EDGE_2);
        }
    }

    /**
     *  Sets the node to a given distance (or infinity if appropriate).
     *
     *  @param n the node to set
     *  @param distance the distance to set
     */
    public void setNodeText(ThreeTenNode n, int distance) {
        String text = (distance == Integer.MAX_VALUE ? INFINITY_SIGN : ""+distance);
        n.setText(text);
    }

    /**
     *  Sets the node next to a given distance (or infinity if appropriate)
     *  while also displaying the change from a previous value.
     *
     *  @param n the node to set
     *  @param oldCost the previous cost
     *  @param newCost the new cost
     */
    public void setNodeText(ThreeTenNode n, int oldCost, int newCost) {
        String text = (oldCost == Integer.MAX_VALUE ? INFINITY_SIGN : ""+oldCost);
        n.setText("" + text + "->" + newCost);
    }

    /**
     *  Recolors things after the last step and updates the text.
     */
    public void cleanUpLastStep() {
        if(lastMin != null) {
            //mark the node as "done"
            lastMin.done = true;

            //reset last selected node to green (done)
            lastMin.graphNode.setColor(COLOR_DONE_NODE);

            //change arrows from orange highlight back to black (not done)
            for(ThreeTenEdge e : graph.getOutEdges(lastMin.graphNode)) {
                if(e.getColor() == COLOR_ACTIVE_EDGE) {
                    e.setColor(COLOR_NONE_EDGE);
                }
            }

            //reset all non-finished nodes back to white (not done) and appropriate text
            for(NodeForAlg n : queue) {
                n.graphNode.setColor(COLOR_NONE_NODE);
                setNodeText(n.graphNode, n.distance);
                String text = (n.distance == Integer.MAX_VALUE ? INFINITY_SIGN : ""+n.distance);
                n.graphNode.setText(text);
            }
        }
    }

    /**
     *  Gets the next minimum and sets up the next step of the algorithm.
     *
     *  @return whether or not a new minimum was found
     */
    public boolean setupNextMin() {
        if(queue.isEmpty()) return false;

        NodeForAlg currMin = queue.remove();

        //set color of next node and path to it (if not first node)
        currMin.graphNode.setColor(COLOR_ACTIVE_NODE_1);

        //only unreachable nodes left
        if(currMin.distance == Integer.MAX_VALUE) {
            lastMin = currMin;
            return true;
        }

        //attach to parent
        if(currMin.parent != null) {
            graph.findEdge(currMin.parent, currMin.graphNode).setColor(COLOR_DONE_EDGE_1);
        }

        lastMin = currMin;
        return true;
    }

    /**
     *  Updates the neighbors of the current minimum cost node.
     */
    public void doUpdates() {
        //if the next min does not have an distance of infinity...
        if(lastMin.distance == Integer.MAX_VALUE) {
            return;
        }

        //highlight outgoing edges and neighbors in orange (to update)
        Collection<ThreeTenEdge> outEdges = graph.getOutEdges(lastMin.graphNode);
        for(ThreeTenEdge e : outEdges) {
            ThreeTenNode n = graph.getOpposite(lastMin.graphNode, e);
            NodeForAlg algNode = graphNodesToAlgNodes.get(n);

            //calc new cost
            int newCost = lastMin.distance + e.getWeight();

            if(!algNode.done && newCost < algNode.distance) {

                //set colors and text
                e.setColor(COLOR_ACTIVE_EDGE);
                n.setColor(COLOR_ACTIVE_NODE_2);

                String text = (algNode.distance == Integer.MAX_VALUE ? "\u221e" : ""+algNode.distance);
                setNodeText(n, algNode.distance, newCost);

                //update in queue
                algNode.distance = newCost;
                algNode.parent = lastMin.graphNode;
                queue.update(algNode);
            }
        }
    }

    /**
     *  {@inheritDoc}
     */
    public boolean step() {
        if(!started) {
            start();
            return true;
        }

        cleanUpLastStep();
        if(!setupNextMin()) {
            finish();
            return false;
        }
        doUpdates();

        return true;
    }
}
