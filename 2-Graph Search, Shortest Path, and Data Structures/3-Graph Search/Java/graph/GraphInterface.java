package graph;

import java.util.ArrayList;

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

    /**
     * Finds all the findable vertices from the given source vertex using BFS.
     * @param srcVtxID source vertex ID
     * @return all the findable vertices
     */
    ArrayList<Integer> bfs(int srcVtxID);

    /**
     * Sets all the vertices to unexplored.
     */
    void clearExplored();

    /**
     * Finds the length of the shortest path from the given source vertex to the
     * given destination vertex using BFS.
     * @param srcVtxID source vertex ID
     * @param destVtxID destination vertex ID
     * @return length of the shorted path
     */
    int shortestPath(int srcVtxID, int destVtxID);

    /**
     * Returns the number of connected components of this graph using BFS.
     * @return number of connected components
     */
    int numOfConnectedComponentsWithBFS();

}
