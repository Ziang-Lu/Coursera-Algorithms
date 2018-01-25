package graph;

/**
 * Abstract edge class.
 *
 * @author Ziang Lu
 */
public class AbstractEdge {

    /**
     * Length of the edge.
     */
    protected double length;

    /**
     * Constructor with parameter.
     * @param length
     */
    public AbstractEdge(double length) {
        this.length = length;
    }

    /**
     * Accessor of length.
     * @return length
     */
    public double length() {
        return length;
    }

}
