package graph;

/**
 * AbstractEdge class.
 *
 * @author Ziang Lu
 */
public class AbstractEdge {

    /**
     * Length of this edge.
     */
    private final int length;

    /**
     * Constructor with parameter.
     * @param length length of this edge
     */
    public AbstractEdge(int length) {
        this.length = length;
    }

    /**
     * Accessor of length.
     * @return length
     */
    public int length() {
        return length;
    }

}
