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
    private final int vtxID;
    /**
     * Frequency of neighbors.
     */
    private HashMap<Integer, Integer> freqOfNeighbors;
    /**
     * Edges of this vertex.
     */
    private ArrayList<Edge> edges;

    /**
     * Constructor with parameter.
     * @param vtxID vertex ID
     */
    Vertex(int vtxID) {
        this.vtxID = vtxID;
        freqOfNeighbors = new HashMap<Integer, Integer>();
        edges = new ArrayList<Edge>();
    }

    /**
     * Accessor of vtxID.
     * @return vtxID
     */
    int getID() {
        return vtxID;
    }

    /**
     * Returns the first edge with the given neighbor.
     * @param neighbor given neighbor
     * @return edge if found, null if not found
     */
    Edge getEdgeWithNeighbor(Vertex neighbor) {
        // Check whether the input neighbor is null
        if (neighbor == null) {
            throw new IllegalArgumentException("The input neighbor should not be null.");
        }

        for (Edge edge : edges) {
            if (((edge.getEnd1() == this) && (edge.getEnd2() == neighbor))
                    || ((edge.getEnd1() == neighbor) && (edge.getEnd2() == this))) {
                return edge;
            }
        }
        // Not found
        return null;
    }

    /**
     * Accessor of edges.
     * @return edges
     */
    ArrayList<Edge> getEdges() {
        return edges;
    }

    /**
     * Adds the given edge to this vertex.
     * @param newEdge edge to add
     */
    void addEdge(Edge newEdge) {
        // Check whether the input edge is null
        if (newEdge == null) {
            throw new IllegalArgumentException("The edge to add should not be null.");
        }
        // Check whether the input edge involves this vertex
        if ((newEdge.getEnd1() != this) && (newEdge.getEnd2() != this)) {
            throw new IllegalArgumentException("The edge to add should involve this vertex.");
        }

        edges.add(newEdge);

        // Find the neighbor associated with the input edge
        Vertex neighbor = null;
        if (newEdge.getEnd1() == this) { // endpoint2 is the neighbor.
            neighbor = newEdge.getEnd2();
        } else { // endpoint1 is the neighbor.
            neighbor = newEdge.getEnd1();
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
            throw new IllegalArgumentException("The edge to remove should not be null.");
        }
        // Check whether the input edge involves this vertex
        if ((edgeToRemove.getEnd1()!= this) && (edgeToRemove.getEnd2() != this)) {
            throw new IllegalArgumentException("The edge to remove should involve this vertex.");
        }

        edges.remove(edgeToRemove);

        // Find the neighbor associated with the input edge
        Vertex neighbor = null;
        if (edgeToRemove.getEnd1() == this) { // endpoint2 is the neighbor.
            neighbor = edgeToRemove.getEnd2();
        } else { // endpoint1 is the neighbor.
            neighbor = edgeToRemove.getEnd1();
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

}
