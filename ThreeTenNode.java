//TODO: Nothing, all done.

import org.apache.commons.collections15.Factory;

import java.awt.Color;

/**
 *  A node representation for the graph simulation.
 *
 *  @author Katherine (Raven) Russell
 */
class ThreeTenNode implements Comparable<ThreeTenNode> {
    /**
     *  The unique id of this node.
     */
    private String id;

    /**
     *  The text on this node.
     */
    private String text;

    /**
     *  The color of the node (for the GUI).
     */
    private Color color = Color.WHITE;

    /**
     *  Constructs a node with a given id.
     *
     *  @param id the unique id of the node
     */
    public ThreeTenNode(String id) { this.id = id; this.text = ""; }

    /**
     *  Constructs a node with a given id and text.
     *
     *  @param id the unique id of the node
     *  @param text the text of the node
     */
    public ThreeTenNode(String id, String text) { this.id = id; this.text = text; }

    /**
     *  Fetches the unique id of the node.
     *
     *  @return the unique id of the node
     */
    public String getId() { return id; }

    /**
     *  Fetches the text of the node.
     *
     *  @return the text of the node
     */
    public String getText() { return text; }

    /**
     *  Sets the text of the node.
     *
     *  @param text the text of the node
     */
    public void setText(String text) { this.text = text; }

    /**
     *  Fetches the color of the node.
     *
     *  @return the color of the node
     */
    public Color getColor() { return color; }

    /**
     *  Sets the color of the node.
     *
     *  @param color the color of the node
     */
    public void setColor(Color color) { this.color = color; }

    /**
     *  {@inheritDoc}
     */
    @Override
    public String toString() {
        return ""+text;
    }

    /**
     *  {@inheritDoc}
     */
    @Override
    public boolean equals(Object o) {
        if(o instanceof ThreeTenNode) {
            return this.id.equals(((ThreeTenNode)o).id);
        }
        return false;
    }

    /**
     *  {@inheritDoc}
     */
    @Override
    public int compareTo(ThreeTenNode o) {
        return this.id.compareTo(((ThreeTenNode)o).id);
    }

    /**
     *  {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return id.hashCode();
    }

    /**
     *  The number of nodes created so far (for gerating unique ids
     *  from the factory method).
     */
    public static int nodeCount = 0;

    /**
     *  Generates a new node with a (probably) unique id.
     *
     *  @return a new node
     */
    public static Factory<ThreeTenNode> getFactory() {
        return new Factory<ThreeTenNode> () {
            public ThreeTenNode create() {
                return new ThreeTenNode("" + nodeCount++);
            }
        };
    }
}

