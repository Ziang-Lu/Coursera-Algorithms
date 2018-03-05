package graph;

import java.util.ArrayList;
import java.util.LinkedList;

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
     * Returns the mapping between the vertices and the shortest paths from the
     * given vertex using Bellman-Ford Shortest-Path Algorithm in an improved
     * bottom-up way.
     * Note that in this application, the vertex IDs are exactly from 0 to
     * (n - 1).
     * @param srcVtxID source vertex ID
     * @return shortest paths
     */
    ArrayList<LinkedList<Integer>> bellmanFordShortestPaths(int srcVtxID);

    /**
     * Returns the mapping between the vertices and the shortest paths from the
     * given vertex using Bellman-Ford Shortest-Path Algorithm in an improved
     * bottom-up way with early-stopping and space optimization.
     * Note that in this application, the vertex IDs are exactly from 0 to
     * (n - 1).
     * @param srcVtxID source vertex ID
     * @return shortest paths
     */
    ArrayList<LinkedList<Integer>> bellmanFordShortestPathsOptimized(int srcVtxID);

    /**
     * Returns the mapping between the vertices and the shortest paths to the
     * given vertex using Bellman-Ford Shortest-Path Algorithm in an improved
     * bottom-up way.
     * Note that in this application, the vertex IDs are exactly from 0 to
     * (n - 1).
     * @param destVtxID destination vertex ID
     * @return destination-driven shortest paths
     */
    ArrayList<LinkedList<Integer>> bellmanFordShortestPathsDestDriven(int destVtxID);

    /**
     * Returns the mapping between the vertices and the shortest paths to the
     * given vertex using Bellman-Ford Shortest-Path Algorithm in an improved
     * bottom-up way with early-stopping and space optimization.
     * Note that in this application, the vertex IDs are exactly from 0 to
     * (n - 1).
     * @param destVtxID destination vertex ID
     * @return destination-driven shortest paths
     */
    ArrayList<LinkedList<Integer>> bellmanFordShortestPathsDestDrivenOptimized(int destVtxID);

    /**
     * Returns the mapping between the vertices and the shortest paths to the
     * given vertex in a push-based way.
     * @param destVtxID destination vertex ID
     * @return destination-driven shortest paths
     */
    ArrayList<LinkedList<Integer>> shortestPathsDestDrivenPushBased(int destVtxID);

}
