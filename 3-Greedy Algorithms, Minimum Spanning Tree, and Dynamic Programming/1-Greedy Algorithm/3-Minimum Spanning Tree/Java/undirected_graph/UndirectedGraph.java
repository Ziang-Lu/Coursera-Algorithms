package undirected_graph;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.PriorityQueue;
import java.util.Random;

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

    /**
     * Finds the minimum spanning tree (MST) in this graph using straightforward
     * Prim's MST Algorithm.
     * @return cost of the MST
     */
    public double primMSTStraightforward() {
        // 1. Arbitrarily choose a source vertex s
        Random randomGenerator = new Random();
        Vertex srcVtx = vtxList.get(randomGenerator.nextInt(vtxList.size()));
        // 2. Initialize X = {s}, which contains the vertices we've spanned so far, and T = {empty}, which is the
        //    current spanning tree
        BitSet spanned = new BitSet();
        spanned.set(srcVtx.id());
        ArrayList<UndirectedEdge> currSpanningTree = new ArrayList<UndirectedEdge>();
        // 3. Create a heap containing all the edges with one endpoint in X and the other in (V-X)
        PriorityQueue<UndirectedEdge> crossingEdges = new PriorityQueue<UndirectedEdge>(srcVtx.edges());
        // 3. While X != V
        while (spanned.cardinality() < vtxList.size()) {
            // Extract the cheapest crossing edge e = (v, w) with v in X and w in (V-X)
            UndirectedEdge cheapestCrossingEdge = crossingEdges.poll();
            // Add e to T
            currSpanningTree.add(cheapestCrossingEdge);
            // Add w to X
            Vertex w = null;
            if (spanned.get(cheapestCrossingEdge.end1().id())) { // endpoint2 is the w.
                w = cheapestCrossingEdge.end2();
            } else { // endpoint1 is the w.
                w = cheapestCrossingEdge.end1();
            }
            spanned.set(w.id());

            // Update the crossing edges with w's edges if necessary
            for (UndirectedEdge wEdge : w.edges()) {
                // Find the neighbor
                Vertex neighbor = null;
                if (wEdge.end1().id() == w.id()) { // endpoint2 is the neighbor.
                    neighbor = wEdge.end2();
                } else { // endpoint1 is the neighbor.
                    neighbor = wEdge.end1();
                }
                // Check whether the neighbor of w has been spanned
                if (!spanned.get(neighbor.id())) {
                    crossingEdges.offer(wEdge);
                }
            }
        }

        return currSpanningTree.stream().mapToDouble(edge -> edge.length()).sum();
    }

    /**
     * Finds the minimum spanning tree (MST) in this graph using improved Prim's
     * MST Algorithm.
     * @return cost of the MST
     */
    public double primMSTImproved() {
        // TODO implementation
        return 0.0;
    }

}
