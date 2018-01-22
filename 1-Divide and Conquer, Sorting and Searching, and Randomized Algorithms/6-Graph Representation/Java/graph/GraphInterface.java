package graph;

/**
 * A very simple interface for any graph implementation.
 *
 * @author Ziang Lu
 */
public interface GraphInterface {

    /**
     * Adds a new vertex to this graph.
     * @param newVtxID new vertex ID
     */
    void addVtx(int newVtxID);

    /**
     * Removes a vertex from this graph.
     * @param vtxID vertex ID
     */
    void removeVtx(int vtxID);

    /**
     * Adds a new edge to this graph.
     * @param end1ID endpoint1 ID
     * @param end2ID endpoint2 ID
     */
    void addEdge(int end1ID, int end2ID);

    /**
     * Removes an edge from this graph.
     * @param end1ID endpoint1 ID
     * @param end2ID endpoint2 ID
     */
    void removeEdge(int end1ID, int end2ID);

    /**
     * Shows the graph.
     */
    void showGraph();

}
