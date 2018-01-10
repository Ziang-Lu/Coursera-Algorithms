package undirected_graph;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Vertex class.
 *
 * Note that parallel edges are allowed, but not self-loops.
 * @author Ziang Lu
 */
class Vertex {

    /**
     * Vertex ID.
     */
    final int vtxID;
    /**
     * Edges of this vertex.
     */
    ArrayList<Edge> myEdges;
    /**
     * Frequency of neighbors.
     */
    HashMap<Integer, Integer> freqOfNeighbors;

    /**
     * Constructor with parameter.
     * @param vtxID vertex ID
     */
    Vertex(int vtxID) {
        this.vtxID = vtxID;
        myEdges = new ArrayList<Edge>();
        freqOfNeighbors = new HashMap<Integer, Integer>();
    }

    /**
     * Adds the given edge to this vertex.
     * @param newEdge edge to add
     */
    void addEdge(Edge newEdge) {
        // Check whether the input edge is null
        if (newEdge == null) {
            System.out.println("The input edge should not be null.");
            return;
        }
        // Check whether the input edge involves this vertex
        if ((newEdge.end1 != this) && (newEdge.end2 != this)) {
            System.out.println("The input edge should involve this vertex.");
            return;
        }

        myEdges.add(newEdge);

        // Find the neighbor associated with the input edge
        Vertex neighbor = null;
        if (newEdge.end1 == this) { // endpoint2 is the neighbor.
            neighbor = newEdge.end2;
        } else { // endpoint1 is the neighbor.
            neighbor = newEdge.end1;
        }
        // Update the frequency of the neighbor
        Integer freq = freqOfNeighbors.getOrDefault(neighbor.vtxID, 0);
        ++freq;
        freqOfNeighbors.put(neighbor.vtxID, freq);
    }

    /**
     * Removes the given edge from this vertex.
     * @param edgeToRemove edge to remove
     */
    void removeEdge(Edge edgeToRemove) {
        // Check whether the input edge is null
        if (edgeToRemove == null) {
            System.out.println("The input edge should not mbe null.");
            return;
        }
        // Check whether the input edge involves this vertex
        if ((edgeToRemove.end1 != this) || (edgeToRemove.end2 != this)) {
            System.out.println("The input edge should involve this vertex.");
            return;
        }

        myEdges.remove(edgeToRemove);

        // Find the neighbor associated with the input edge
        Vertex neighbor = null;
        if (edgeToRemove.end1 == this) { // endpoint2 is the neighbor.
            neighbor = edgeToRemove.end2;
        } else { // endpoint1 is the neighbor.
            neighbor = edgeToRemove.end1;
        }
        // Update the frequency of the neighbor
        Integer freq = freqOfNeighbors.get(neighbor.vtxID);
        if (freq == 1) {
            freqOfNeighbors.remove(neighbor.vtxID);
        } else {
            --freq;
            freqOfNeighbors.put(neighbor.vtxID, freq);
        }
    }

    @Override
    public String toString() {
        return String.format("Vertex #%d, Its neighbors and frequencies: %s", vtxID, freqOfNeighbors);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Vertex)) {
            return false;
        }
        Vertex another = (Vertex) o;
        return vtxID == another.vtxID;
    }

}
