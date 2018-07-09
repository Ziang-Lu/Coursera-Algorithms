package undirected_graph;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;
import java.util.Random;

import graph.GraphInterface;

/**
 * Adjacency list representation of a undirected graph.
 *
 * Note that parallel edges and self-loops are not allowed.
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

    @Override
    public int computeMaximumCut() {
        // Always maintain a candidate cut, and iteratively make it better and better via small (local) modifications

        // 1. Get an arbitrary cut (A, B)
        BitSet groupA = new BitSet();
        BitSet groupB = new BitSet();
        Random randomGenerator = new Random();
        for (Vertex vtx : vtxList) {
            switch (randomGenerator.nextInt(2)) {
            case 0:
                groupA.set(vtx.id());
                break;
            case 1:
                groupB.set(vtx.id());
                break;
            }
        }
        // Calculate the number of crossing edges
        int nCrossingEdge = 0;
        for (UndirectedEdge edge : edgeList) {
            Vertex end1 = edge.end1(), end2 = edge.end2();
            if (((groupA.get(end1.id())) && (!groupB.get(end2.id())))
                    || ((!groupA.get(end1.id())) && (groupB.get(end2.id())))) {
                ++nCrossingEdge;
            }
        }

        for (Vertex vtx : vtxList) {
            // 2. While there is a vertex v with more internal edges than crossing edges
            int dv = 0, cv = 0;
            for (UndirectedEdge edge : vtx.edges()) {
                // Find the neighbor
                Vertex neighbor = null;
                if (edge.end1() == vtx) { // endpoint2 is the neighbor.
                    neighbor = edge.end2();
                } else { // endpoint1 is the neighbor.
                    neighbor = edge.end1();
                }
                if (((groupA.get(vtx.id())) && (groupB.get(neighbor.id())))
                        || ((!groupA.get(vtx.id())) && (!groupB.get(neighbor.id())))) {
                    ++dv;
                } else {
                    ++cv;
                }
            }
            if (dv > cv) {
                // Move v to the other side
                if (groupA.get(vtx.id())) {
                    groupA.clear(vtx.id());
                    groupB.set(vtx.id());
                } else {
                    groupB.clear(vtx.id());
                    groupA.set(vtx.id());
                }
                nCrossingEdge += dv - cv;
            }
        }

        // 3. Return the final resulting cut
        return nCrossingEdge;
        // Overall running time complexity: O(mn)
    }

}
