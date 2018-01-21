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
     * Whether this vertex is explored by BFS.
     */
    private boolean explored;
    /**
     * Layer from a source vertex.
     */
    private int layer;

    /**
     * Constructor with parameter.
     * @param vtxID vertex ID
     */
    Vertex(int vtxID) {
        this.vtxID = vtxID;
        freqOfNeighbors = new HashMap<Integer, Integer>();
        edges = new ArrayList<Edge>();
        explored = false;
        layer = 0;
    }

    /**
     * Accessor of vtxID.
     * @return vtxID
     */
    int id() {
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
            if (((edge.end1() == this) && (edge.end2() == neighbor))
                    || ((edge.end1() == neighbor) && (edge.end2() == this))) {
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
    ArrayList<Edge> edges() {
        return edges;
    }

    /**
     * Accessor of explored.
     * @return explored
     */
    boolean explored() {
        return explored;
    }

    /**
     * Accessor of layer.
     * @return layer
     */
    int layer() {
        return layer;
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
        if ((newEdge.end1() != this) && (newEdge.end2() != this)) {
            throw new IllegalArgumentException("The edge to add should involve this vertex.");
        }

        edges.add(newEdge);

        // Find the neighbor associated with the input edge
        Vertex neighbor = null;
        if (newEdge.end1() == this) { // endpoint2 is the neighbor.
            neighbor = newEdge.end2();
        } else { // endpoint1 is the neighbor.
            neighbor = newEdge.end1();
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
        if ((edgeToRemove.end1()!= this) && (edgeToRemove.end2() != this)) {
            throw new IllegalArgumentException("The edge to remove should involve this vertex.");
        }

        edges.remove(edgeToRemove);

        // Find the neighbor associated with the input edge
        Vertex neighbor = null;
        if (edgeToRemove.end1() == this) { // endpoint2 is the neighbor.
            neighbor = edgeToRemove.end2();
        } else { // endpoint1 is the neighbor.
            neighbor = edgeToRemove.end1();
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

    /**
     * Sets this vertex to explored.
     */
    void setAsExplored() {
        explored = true;
    }

    /**
     * Mutator of layer.
     * @param layer layer
     */
    void setLayer(int layer) {
        this.layer = layer;
    }

    @Override
    public String toString() {
        return String.format("Vertex #%d, Its neighbors and frequencies: %s", vtxID, freqOfNeighbors);
    }

}
