package graph;

import java.util.Map;

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
    void addEdge(int end1ID, int end2ID, double length);

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
     * Returns the mapping between the vertices and the shortest distances from
     * the given vertex using Dijkstra's Shortest-Path Algorithm.
     * Assumption: This graph has no n edge with negative length.
     * @param srcVtxID source vertex ID
     * @return mapping between vertices and shortest distances
     */
    Map<Integer, Double> dijkstraShortestPaths(int srcVtxID);

}
