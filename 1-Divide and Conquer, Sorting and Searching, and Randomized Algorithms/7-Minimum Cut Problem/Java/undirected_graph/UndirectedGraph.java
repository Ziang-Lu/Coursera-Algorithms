package undirected_graph;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
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
    private final List<Vertex> vtxList;
    /**
     * Edge list.
     */
    private final List<UndirectedEdge> edgeList;

    /**
     * Default constructor.
     */
    public UndirectedGraph() {
        vtxList = new ArrayList<>();
        edgeList = new ArrayList<>();
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
        List<UndirectedEdge> edgesToRemove = vtxToRemove.edges();
        while (edgesToRemove.size() > 0) {
            removeEdge(edgesToRemove.get(0));
        }
        // Remove the vertex
        vtxList.remove(vtxToRemove);
    }

    @Override
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

        UndirectedEdge newEdge = new UndirectedEdge(end1, end2);
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
     * Computes a cut with the fewest number of crossing edges.
     * @return fewest number of crossing edges
     */
    public int computeMinimumCut() {
        if (vtxList.size() <= 1) {
            return 0;
        }

        Random random = new Random();

        // While there are more than 2 vertices
        while (vtxList.size() > 2) {
            // 1. Pick up an edge randomly
            int randomIdx = random.nextInt(edgeList.size());
            UndirectedEdge edgeToContract = edgeList.get(randomIdx);
            Vertex end1 = edgeToContract.end1(), end2 = edgeToContract.end2();

            // 2. Contract the two endpoints into a single vertex

            // (1) Remove all the edges between the pair
            removeEdgesBetweenPair(end1.id(), end2.id());
            // (2) Create a merged vertex
            int mergedVtxID = getNextVtxID();
            addVtx(mergedVtxID);
            // (3) Reconstruct the edges associated with the two endpoints to the merged vertex, and remove the two
            // endpoints
            Vertex mergedVtx = findVtx(mergedVtxID);
            reconstructEdges(end1, mergedVtx);
            reconstructEdges(end2, mergedVtx);
        }
        return edgeList.size();
    }

    /**
     * Private helper method to get the next available vertex ID, which is 1
     * greater than the current largest vertex ID.
     * @return next available vertex ID
     */
    private int getNextVtxID() {
        ArrayList<Integer> vtxIDs = new ArrayList<>();
        for (Vertex vtx : vtxList) {
            vtxIDs.add(vtx.id());
        }
        return Collections.max(vtxIDs) + 1;
    }

    /**
     * Private helper method to reconnect the edges associated with the given
     * endpoint to the given merged vertex, and remove the endpoint.
     * @param end endpoint
     * @param mergedVtx merged vertex
     */
    private void reconstructEdges(Vertex end, Vertex mergedVtx) {
        for (UndirectedEdge edgeFromEnd : end.edges()) {
            // Find the neighbor
            Vertex neighbor = null;
            if (edgeFromEnd.end1() == end) { // endpoint2 is the neighbor.
                neighbor = edgeFromEnd.end2();
                // Remove the edge from the neighbor
                neighbor.removeEdge(edgeFromEnd);
                // Reform the edge to connect the neighbor and the merged vertex
                edgeFromEnd.setEnd1(mergedVtx);
            } else { // endpoint1 is the neighbor.
                neighbor = edgeFromEnd.end1();
                neighbor.removeEdge(edgeFromEnd);
                edgeFromEnd.setEnd2(mergedVtx);
            }
            // Add the new edge to both the neighbor and the merged vertex
            neighbor.addEdge(edgeFromEnd);
            mergedVtx.addEdge(edgeFromEnd);
        }
        // Remove the endpoint
        vtxList.remove(end);
    }

}
