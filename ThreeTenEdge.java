//TODO: Nothing, all done.

import org.apache.commons.collections15.Factory;

import java.awt.Color;

/**
 *  An edge representation for the graph simulation.
 *
 *  @author Katherine (Raven) Russell
 */
class ThreeTenEdge {
    /**
     *  The unique id of this edge.
     */
    private int id;

    /**
     *  The weight of the edge.
     */
    private int weight = 0;

    /**
     *  The color of the edge (for the GUI).
     */
    private Color color = Color.BLACK;

    /**
     *  Constructs an edge with a given id.
     *
     *  @param id the unique id of the edge
     */
    public ThreeTenEdge(int id) { this.id = id; this.weight = (int)(Math.random()*20)+1; }

    /**
     *  Constructs an edge with a given id and weight.
     *
     *  @param id the unique id of the edge
     *  @param weight the weight of the edge
     */
    public ThreeTenEdge(int id, int weight) { this.id = id; this.weight = weight; }

    /**
     *  Fetches the unique id of the edge.
     *
     *  @return the unique id of the edge
     */
    public int getId() { return id; }

    /**
     *  Fetches the weight of the edge.
     *
     *  @return the weight of the edge
     */
    public int getWeight() { return weight; }

    /**
     *  Fetches the color of the edge.
     *
     *  @return the color of the edge
     */
    public Color getColor() { return color; }

    /**
     *  Sets the color of the edge.
     *
     *  @param color the color of the edge
     */
    public void setColor(Color color) { this.color = color; }

    /**
     *  {@inheritDoc}
     */
    @Override
    public String toString() {
        return ""+weight;
    }

    /**
     *  {@inheritDoc}
     */
    @Override
    public boolean equals(Object o) {
        if(o instanceof ThreeTenEdge) {
            return this.id == ((ThreeTenEdge)o).id;
        }
        return false;
    }

    /**
     *  {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return (id*7)+(weight*31);
    }

    /**
     *  The number of edges created so far (for gerating unique ids
     *  from the factory method).
     */
    public static int edgeCount = 0;

    /**
     *  Generates a new edge with a random weight and a (probably)
     *  unique id.
     *
     *  @return a new edge with a random weight
     */
    public static Factory<ThreeTenEdge> getFactory() {
        return new Factory<ThreeTenEdge> () {
            public ThreeTenEdge create() {
                return new ThreeTenEdge(edgeCount++);
            }
        };
    }
}

