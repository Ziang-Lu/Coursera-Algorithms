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
     * @param length length of the new edge
     */
    void addEdge(int end1ID, int end2ID, int length);

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

    /**
     * Returns the all-pair shortest paths (APSP) using Floyd-Warshall APSP
     * Algorithm in a bottom-up way.
     * Note that in this application, the vertex IDs are exactly from 0 to
     * (n - 1).
     * The reconstruction process is easy to come up with but difficult to
     * implement, and thus is omitted.
     * @return APSP lengths
     */
    int[][] floydWarshallApsp();

    /**
     * Returns the all-pair shortest paths (APSP) using Floyd-Warshall APSP
     * Algorithm in a bottom-up way with space optimization.
     * Note that in this application, the vertex IDs are exactly from 0 to
     * (n - 1).
     * The reconstruction process is easy to come up with but difficult to
     * implement, and thus is omitted.
     * @return APSP lengths
     */
    int[][] floydWarshallApspOptimized();

}
