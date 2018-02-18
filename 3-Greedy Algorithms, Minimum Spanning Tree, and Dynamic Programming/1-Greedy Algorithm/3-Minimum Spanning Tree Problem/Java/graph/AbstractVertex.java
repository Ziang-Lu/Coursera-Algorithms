package graph;

/**
 * AbstractVertex class.
 *
 * Note that parallel edges are allowed, but not self-loops.
 * @author Ziang Lu
 */
public abstract class AbstractVertex {

    /**
     * Vertex ID.
     */
    protected final int vtxID;
    /**
     * Whether this vertex is explored by the BFS/DFS.
     */
    protected boolean explored;

    /**
     * Constructor with parameter.
     * @param vtxID vertex ID
     */
    public AbstractVertex(int vtxID) {
        this.vtxID = vtxID;
        explored = false;
    }

    /**
     * Accessor of vtxID.
     * @return vtxID
     */
    public int id() {
        return vtxID;
    }

    /**
     * Accessor of explored.
     * @return explored
     */
    public boolean explored() {
        return explored;
    }

    /**
     * Sets this vertex to explored.
     */
    public void setAsExplored() {
        explored = true;
    }

}
