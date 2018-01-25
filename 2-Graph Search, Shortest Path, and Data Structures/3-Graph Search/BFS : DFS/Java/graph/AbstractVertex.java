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
     * Whether this vertex is explored by BFS/DFS.
     */
    protected boolean explored;
    /**
     * Layer from a source vertex.
     */
    protected int layer;

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

    /**
     * Accessor of explored.
     * @return explored
     */
    public boolean explored() {
        return explored;
    }

    /**
     * Accessor of layer.
     * @return layer
     */
    public int layer() {
        return layer;
    }

    /**
     * Sets this vertex to explored.
     */
    public void setAsExplored() {
        explored = true;
    }

    /**
     * Sets this vertex to unexplored.
     */
    public void setAsUnexplored() {
        explored = false;
    }

    /**
     * Mutator of layer.
     * @param layer layer
     */
    public void setLayer(int layer) {
        this.layer = layer;
    }

}
