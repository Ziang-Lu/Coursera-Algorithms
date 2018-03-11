package graph;

/**
 * AbstractVertex class.
 *
 * Note that parallel edges and self-loops are not allowed.
 * @author Ziang Lu
 */
public abstract class AbstractVertex {

    /**
     * Vertex ID.
     */
    protected final int vtxID;

    /**
     * Constructor with parameter.
     * @param vtxID vertex ID
     */
    public AbstractVertex(int vtxID) {
        this.vtxID = vtxID;
    }

    /**
     * Accessor of vtxID.
     * @return vtxID
     */
    public int id() {
        return vtxID;
    }

}
