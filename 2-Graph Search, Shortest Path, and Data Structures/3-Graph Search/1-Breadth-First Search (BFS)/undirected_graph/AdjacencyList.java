package undirected_graph;

import java.util.ArrayDeque;
import java.util.ArrayList;

/**
 * Adjacency list representation of a undirected graph.
 *
 * Note that parallel edges are allowed, but not self-loops.
 * @author Ziang Lu
 */
public class AdjacencyList {

    /**
     * Vertex list.
     */
    private ArrayList<Vertex> vtxList;
    /**
     * Edge list.
     */
    private ArrayList<Edge> edgeList;

    /**
     * Default constructor.
     */
    public AdjacencyList() {
        vtxList = new ArrayList<Vertex>();
        edgeList = new ArrayList<Edge>();
    }

    /**
     * Adds a new vertex to this graph.
     * @param newVtxID new vertex ID
     */
    public void addVtx(int newVtxID) {
        // Check whether the input vertex is repeated
        if (findVtx(newVtxID) != null) {
            throw new IllegalArgumentException("The input vertex is repeated.");
        }

        Vertex newVtx = new Vertex(newVtxID);
        vtxList.add(newVtx);
    }

    /**
     * Private helper method to find the given vertex in this adjacency list.
     * @param vtxID vertex ID to look for
     * @return vertex if found, null if not found
     */
    private Vertex findVtx(int vtxID) {
        for (Vertex vtx : vtxList) {
            if (vtx.id() == vtxID) {
                return vtx;
            }
        }
        // Not found
        return null;
    }

    /**
     * Removes a vertex from this graph.
     * @param vtxID vertex ID
     */
    public void removeVtx(int vtxID) {
        // Check whether the input vertex exists
        Vertex vtxToRemove = findVtx(vtxID);
        if (vtxToRemove == null) {
            throw new IllegalArgumentException("The input vertex doesn't exist.");
        }

        removeVtx(vtxToRemove);
    }

    /**
     * Private helper method to remove the given vertex from this graph.
     * @param vtxToRemove vertex to remove
     */
    private void removeVtx(Vertex vtxToRemove) {
        // Remove all the edges associated with the vertex to remove
        ArrayList<Edge> edgesToRemove = vtxToRemove.edges();
        while (edgesToRemove.size() > 0) {
            Edge edgeToRemove = edgesToRemove.get(0);
            removeEdge(edgeToRemove);
        }
        // Remove the vertex
        vtxList.remove(vtxToRemove);
    }

    /**
     * Adds a new edge to this graph.
     * @param end1ID endpoint1 ID
     * @param end2ID endpoint2 ID
     */
    public void addEdge(int end1ID, int end2ID) {
        // Check whether the input endpoints both exist
        Vertex end1 = findVtx(end1ID), end2 = findVtx(end2ID);
        if ((end1 == null) || (end2 == null)) {
            throw new IllegalArgumentException("The endpoints don't both exist.");
        }
        // Check whether the input endpoints are the same (self-loop)
        if (end1ID == end2ID) {
            throw new IllegalArgumentException("The endpoints are the same (self-loop).");
        }

        Edge newEdge = new Edge(end1, end2);
        addEdge(newEdge);
    }

    /**
     * Private helper method to add the given edge to this graph.
     * @param newEdge new edge
     */
    private void addEdge(Edge newEdge) {
        Vertex end1 = newEdge.end1(), end2 = newEdge.end2();
        end1.addEdge(newEdge);
        end2.addEdge(newEdge);
        edgeList.add(newEdge);
    }

    /**
     * Removes an edge from this graph.
     * @param end1ID endpoint1 ID
     * @param end2ID endpoint2 ID
     */
    public void removeEdge(int end1ID, int end2ID) {
        // Check whether the input vertices both exist
        Vertex end1 = findVtx(end1ID), end2 = findVtx(end2ID);
        if ((end1 == null) || (end2 == null)) {
            throw new IllegalArgumentException("The input vertices don't both exist.");
        }
        // Check whether the edge to remove exists
        Edge edgeToRemove = end1.getEdgeWithNeighbor(end2);
        if (edgeToRemove == null) {
            throw new IllegalArgumentException("The edge to remove doesn't exist.");
        }

        removeEdge(edgeToRemove);
    }

    /**
     * Private helper method to remove the given edge from this graph.
     * @param edgeToRemove edge to remove
     */
    private void removeEdge(Edge edgeToRemove) {
        Vertex end1 = edgeToRemove.end1(), end2 = edgeToRemove.end2();
        end1.removeEdge(edgeToRemove);
        end2.removeEdge(edgeToRemove);
        edgeList.remove(edgeToRemove);
    }

    /**
     * Removes all the edges between a vertex pair from this graph.
     * @param end1ID endpoint1 ID
     * @param end2ID endpoint2 ID
     */
    public void removeEdgesBetweenPair(int end1ID, int end2ID) {
        try {
            while (true) {
                removeEdge(end1ID, end2ID);
            }
        } catch (IllegalArgumentException ex) {}
    }

    /**
     * Shows the graph.
     */
    public void showGraph() {
        System.out.println("The vertices are:");
        for (Vertex vtx : vtxList) {
            System.out.println(vtx);
        }
        System.out.println("The edges are:");
        for (Edge edge : edgeList) {
            System.out.println(edge);
        }
    }

    /**
     * Finds all the findable vertices from the given source vertex using BFS.
     * @param srcVtxID source vertex ID
     * @return all the findable vertices
     */
    public ArrayList<Integer> bfs(int srcVtxID) {
        // Check whether the input source vertex exists
        Vertex srcVtx = findVtx(srcVtxID);
        if (srcVtx == null) {
            throw new IllegalArgumentException("The source vertex doesn't exist.");
        }

        // 1. Initialize G as s explored and other vertices unexplored
        srcVtx.setAsExplored();
        // 2. Let Q be the queue of vertices initialized with s
        ArrayDeque<Vertex> queue = new ArrayDeque<Vertex>();
        queue.offer(srcVtx);

        ArrayList<Integer> findableVtxIDs = new ArrayList<Integer>();

        // 3. While Q is not empty
        while (!queue.isEmpty()) {
            // (1) Take out the first vertex v
            Vertex vtx = queue.poll();
            // (2) For every edge (v, w)
            for (Edge edge : vtx.edges()) {
                // Find the neighbor
                Vertex neighbor = null;
                if (edge.end1() == vtx) { // endpoint2 is the neighbor.
                    neighbor = edge.end2();
                } else { // endpoint1 is the neighbor.
                    neighbor = edge.end1();
                }
                // If w is not explored
                if (!neighbor.explored()) {
                    // Mark w as explored
                    neighbor.setAsExplored();
                    // Push w to Q
                    queue.offer(neighbor);
                }
            }

            findableVtxIDs.add(vtx.id());
        }

        return findableVtxIDs;
    }

    /**
     * Finds the length of the shortest path from the given source vertex to the
     * given destination vertex using BFS.
     * @param srcVtxID source vertex ID
     * @param destVtxID destination vertex ID
     * @return length of the shorted path
     */
    public int shortestPath(int srcVtxID, int destVtxID) {
        // Check whether the input source and destination vertices exist
        Vertex srcVtx = findVtx(srcVtxID), destVtx = findVtx(destVtxID);
        if ((srcVtx == null) || (destVtx == null)) {
            throw new IllegalArgumentException("The source and destination vertices don't both exist.");
        }

        // 1. Initialize G as s explored and other vertices unexplored
        srcVtx.setAsExplored();
        // 2. Let Q be the queue of vertices initialized with s
        ArrayDeque<Vertex> queue = new ArrayDeque<Vertex>();
        queue.offer(srcVtx);
        // 3. While Q is not empty
        while (!queue.isEmpty()) {
            // (1) Take out the first vertex v
            Vertex vtx = queue.poll();

            int currLayer = vtx.layer();

            // (2) For every edge (v, w)
            for (Edge edge : vtx.edges()) {
                // Find the neighbor
                Vertex neighbor = null;
                if (edge.end1() == vtx) { // endpoint2 is the neighbor.
                    neighbor = edge.end2();
                } else { // endpoint1 is the neighbor.
                    neighbor = edge.end1();
                }
                // If w is not explored
                if (!neighbor.explored()) {
                    // Mark w as explored
                    neighbor.setAsExplored();

                    neighbor.setLayer(currLayer + 1);
                    if (neighbor == destVtx) { // Found it
                        return destVtx.layer();
                    }

                    // Push w to Q
                    queue.offer(neighbor);
                }
            }
        }
        // The destination vertex is not findable starting from the given source vertex.
        return Integer.MAX_VALUE;
    }

}
