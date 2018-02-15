package undirected_graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.PriorityQueue;

import graph.GraphInterface;

/**
 * Adjacency list representation of a undirected graph.
 *
 * Note that parallel edges are allowed, but not self-loops.
 * @author Ziang Lu
 */
public class UndirectedGraph implements GraphInterface {

    /**
     * Vertex list.
     */
    private final ArrayList<Vertex> vtxList;
    /**
     * Edge list.
     */
    private final ArrayList<UndirectedEdge> edgeList;

    /**
     * Default constructor.
     */
    public UndirectedGraph() {
        vtxList = new ArrayList<Vertex>();
        edgeList = new ArrayList<UndirectedEdge>();
    }

    @Override
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

    @Override
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
        ArrayList<UndirectedEdge> edgesToRemove = vtxToRemove.edges();
        while (edgesToRemove.size() > 0) {
            UndirectedEdge edgeToRemove = edgesToRemove.get(0);
            removeEdge(edgeToRemove);
        }
        // Remove the vertex
        vtxList.remove(vtxToRemove);
    }

    @Override
    public void addEdge(int end1ID, int end2ID, double length) {
        // Check whether the input endpoints both exist
        Vertex end1 = findVtx(end1ID), end2 = findVtx(end2ID);
        if ((end1 == null) || (end2 == null)) {
            throw new IllegalArgumentException("The endpoints don't both exist.");
        }
        // Check whether the input endpoints are the same (self-loop)
        if (end1ID == end2ID) {
            throw new IllegalArgumentException("The endpoints are the same (self-loop).");
        }

        UndirectedEdge newEdge = new UndirectedEdge(end1, end2, length);
        addEdge(newEdge);
    }

    /**
     * Private helper method to add the given edge to this graph.
     * @param newEdge new edge
     */
    private void addEdge(UndirectedEdge newEdge) {
        Vertex end1 = newEdge.end1(), end2 = newEdge.end2();
        end1.addEdge(newEdge);
        end2.addEdge(newEdge);
        edgeList.add(newEdge);
    }

    @Override
    public void removeEdge(int end1ID, int end2ID) {
        // Check whether the input vertices both exist
        Vertex end1 = findVtx(end1ID), end2 = findVtx(end2ID);
        if ((end1 == null) || (end2 == null)) {
            throw new IllegalArgumentException("The input vertices don't both exist.");
        }
        // Check whether the edge to remove exists
        UndirectedEdge edgeToRemove = end1.getEdgeWithNeighbor(end2);
        if (edgeToRemove == null) {
            throw new IllegalArgumentException("The edge to remove doesn't exist.");
        }

        removeEdge(edgeToRemove);
    }

    /**
     * Private helper method to remove the given edge from this graph.
     * @param edgeToRemove edge to remove
     */
    private void removeEdge(UndirectedEdge edgeToRemove) {
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

    @Override
    public void showGraph() {
        System.out.println("The vertices are:");
        for (Vertex vtx : vtxList) {
            System.out.println(vtx);
        }
        System.out.println("The edges are:");
        for (UndirectedEdge edge : edgeList) {
            System.out.println(edge);
        }
    }

    @Override
    public HashMap<Integer, Double> dijkstraShortestPaths(int srcVtxID) {
        // Check whether the input source vertex exists
        Vertex srcVtx = findVtx(srcVtxID);
        if (srcVtx == null) {
            throw new IllegalArgumentException("The source vertex doesn't exist.");
        }

        // 1. Create a map between vertices and the shortest distances from the source vertex (X)
        HashMap<Integer, Double> shortestDistances = new HashMap<Integer, Double>();

        // Since we'll be using a lot of extract-min operations, we consider to use a heap to speed up the algorithm.

        // 2. Initialize the local minimum Dijkstra score of the source vertex to 0, and create a heap containing all
        //    the vertices not in X (V-X)
        srcVtx.setLocalMinScore(0);
        PriorityQueue<Vertex> vtxsToProcess = new PriorityQueue<Vertex>(vtxList);

        // 3. While X != V
        while (shortestDistances.size() < vtxList.size()) {
            // Among all directed crossing edges (v, w) with v in X (the map) and w in (V-X) (the heap), pick the edge
            // that minimizes A[v] + l_vw ("Dijkstra's Greedy Criterion")

            // Simply extract the vertex with the minimum Dijkstra score from the heap (V-X), which is exactly w*
            // In other words, in the tournament's second round, each vertex proposes its "local" winner score, and the
            // heap (V-X) chooses the "overall" winner score.
            Vertex wStar = vtxsToProcess.poll();
            double minScore = wStar.localMinScore();

            // Put w* and the corresponding shortest distance to the map (X)
            shortestDistances.put(wStar.id(), minScore);

            // Extracting one vertex from the heap (V-X) may influence some local minimum Dijkstra scores of vertices
            // that are still in the heap (V-X).
            // Vertices that are not connected to w* and are still in the heap (V-X) won't be influenced.
            // => The local minimum Dijkstra scores of vertices that are connected to w* and are still in the heap (V-X)
            //    may drop down.

            // Update the local minimum Dijkstra scores for the vertices if necessary
            for (UndirectedEdge edgeFromWStar : wStar.edges()) {
                // Find the neighbor
                Vertex neighbor = null;
                if (edgeFromWStar.end1() == wStar) { // endpoint2 is the neighbor.
                    neighbor = edgeFromWStar.end2();
                } else { // endpoint1 is the neighbor
                    neighbor = edgeFromWStar.end1();
                }
                // Check whether the neighbor of w* is still in the heap (V-X)
                // This is equivalent to checking whether this neighbor is not in the map (X).
                // The reason we choose to check the latter condition is because searching is faster in hash table
                // (O(1)).
                if (!shortestDistances.containsKey(neighbor.id())) {
                    // Check whether the local minimum Dijkstra score of this neighbor needs to be updated
                    double newScore = minScore + edgeFromWStar.length();
                    if (newScore < neighbor.localMinScore()) {
                        // Remove this neighbor from the heap (V-X)
                        vtxsToProcess.remove(neighbor);
                        // Update its local minimum Dijkstra score
                        neighbor.setLocalMinScore(newScore);
                        // Put this neighbor back to the heap (V-X)
                        vtxsToProcess.offer(neighbor);
                    }
                }
            }
        }
        return shortestDistances;
    }

}
