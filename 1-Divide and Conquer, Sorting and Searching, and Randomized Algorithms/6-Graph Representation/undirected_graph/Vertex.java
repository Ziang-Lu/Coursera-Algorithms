package undirected_graph;

import java.util.ArrayList;

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
     * Constructor with parameter.
     * @param vtxID vertex ID
     */
    Vertex(int vtxID) {
        this.vtxID = vtxID;
        myEdges = new ArrayList<Edge>();
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
        if ((newEdge.end1 != this) || (newEdge.end2 != this)) {
            System.out.println("The input edge should involve this vertex.");
            return;
        }

        myEdges.add(newEdge);
    }

    /**
     * Removes the given edge from this vertex.
     * @param edgeToRemove edge to remove
     */
    void removeEdge(Edge edgeToRemove) {
        // Check whether the input edge is null
        if (edgeToRemove == null) {
            System.out.println("The input edge should not be null.");
            return;
        }
        // Check whether the input edge involves this vertex
        if ((edgeToRemove.end1 != this) || (edgeToRemove.end2 != this)) {
            System.out.println("The input edge should involve this vertex.");
            return;
        }

        myEdges.remove(edgeToRemove);
    }

    @Override
    public String toString() {
        return String.format("Vertex #%d", vtxID);
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
