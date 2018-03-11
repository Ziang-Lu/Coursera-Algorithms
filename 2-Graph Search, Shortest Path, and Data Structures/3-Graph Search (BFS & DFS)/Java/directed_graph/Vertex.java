package directed_graph;

import java.util.ArrayList;
import java.util.BitSet;

import graph.AbstractVertex;

/**
 * Vertex class.
 *
 * Note that parallel edges and self-loops are not allowed.
 * @author Ziang Lu
 */
class Vertex extends AbstractVertex {

    /**
     * Emissive edge of this vertex.
     */
    private final ArrayList<DirectedEdge> emissiveEdges;
    /**
     * Emissive edges of this vertex.
     */
    private final BitSet emissiveNeighbors;
    /**
     * Incident of this vertex.
     */
    private final ArrayList<DirectedEdge> incidentEdges;
    /**
     * Incident neighbors of this vertex.
     */
    private final BitSet incidentNeighbors;

    /**
     * Constructor with parameter.
     * @param vtxID vertex ID
     */
    Vertex(int vtxID) {
        super(vtxID);
        emissiveEdges = new ArrayList<DirectedEdge>();
        emissiveNeighbors = new BitSet();
        incidentEdges = new ArrayList<DirectedEdge>();
        incidentNeighbors = new BitSet();
    }

    /**
     * Returns the first emissive edge with the given head.
     * @param head given head
     * @return emissive edge if found, null if not found
     */
    DirectedEdge getEmissiveEdgeWithHead(Vertex head) {
        // Check whether the input head is null
        if (head == null) {
            throw new IllegalArgumentException("The input head should not be null.");
        }

        for (DirectedEdge emissiveEdge : emissiveEdges) {
            if (emissiveEdge.head() == head) {
                return emissiveEdge;
            }
        }
        // Not found
        return null;
    }

    /**
     * Accessor of emissiveEdges.
     * @return emissiveEdges
     */
    ArrayList<DirectedEdge> emissiveEdges() {
        return emissiveEdges;
    }

    /**
     * Returns the first incident edge with the given tail.
     * @param tail given tail
     * @return incident edge if found, null if not found
     */
    DirectedEdge getIncidentEdgeWithTail(Vertex tail) {
        // Check whether the input tail is null
        if (tail == null) {
            throw new IllegalArgumentException("The input tail should not be null.");
        }

        for (DirectedEdge incidentEdge : incidentEdges) {
            if (incidentEdge.tail() == tail) {
                return incidentEdge;
            }
        }
        // Not found
        return null;
    }

    /**
     * Accessor of incidentEdges.
     * @return incidentEdges
     */
    ArrayList<DirectedEdge> incidentEdges() {
        return incidentEdges;
    }

    /**
     * Adds the given emissive edge to this vertex.
     * @param newEdge emissive edge to add
     */
    void addEmissiveEdge(DirectedEdge newEmissiveEdge) {
        // Check whether the input emissive edge is null
        if (newEmissiveEdge == null) {
            throw new IllegalArgumentException("The emissive edge to add should not be null.");
        }
        // Check whether the input emissive edge involves this vertex as the tail
        if (newEmissiveEdge.tail() != this) {
            throw new IllegalArgumentException("The emissive edge to add should involve this vertex as the tail.");
        }
        // Check whether the input emissive edge already exists
        if (emissiveNeighbors.get(newEmissiveEdge.head().id())) {
            throw new IllegalArgumentException("The emissive edge to add already exists.");
        }

        emissiveEdges.add(newEmissiveEdge);
        emissiveNeighbors.set(newEmissiveEdge.head().id());
    }

    /**
     * Adds the given incident edge to this vertex.
     * @param newIncidentEdge incident edge to add
     */
    void addIncidentEdge(DirectedEdge newIncidentEdge) {
        // Check whether the input incident edge is null
        if (newIncidentEdge == null) {
            throw new IllegalArgumentException("The incident edge to add should not be null.");
        }
        // Check whether the input incident edge involves this vertex as the head
        if (newIncidentEdge.head() != this) {
            throw new IllegalArgumentException("The incident edge to add should involve this vertex as the head.");
        }
        // Check whether the input incident edge already exists
        if (incidentNeighbors.get(newIncidentEdge.tail().id())) {
            throw new IllegalArgumentException("The incident edge to add already exists.");
        }

        incidentEdges.add(newIncidentEdge);
        incidentNeighbors.set(newIncidentEdge.tail().id());
    }

    /**
     * Removes the given emissive edge from this vertex
     * @param emissiveEdgeToRemove edge to remove
     */
    void removeEmissiveEdge(DirectedEdge emissiveEdgeToRemove) {
        // Check whether the input emissive edge is null
        if (emissiveEdgeToRemove == null) {
            throw new IllegalArgumentException("The emissive edge to remove should not be null.");
        }
        // Check whether the input emissive edge involves this vertex as the tail
        if (emissiveEdgeToRemove.tail() != this) {
            throw new IllegalArgumentException("The emissive edge to remove should involve this vertex as the tail.");
        }
        // Check whether the input emissive edge exists
        if (!emissiveNeighbors.get(emissiveEdgeToRemove.head().id())) {
            throw new IllegalArgumentException("The emissive edge to remove doesn't exist.");
        }

        emissiveEdges.remove(emissiveEdgeToRemove);
        emissiveNeighbors.clear(emissiveEdgeToRemove.head().id());
    }

    /**
     * Removes the given incident edge from this vertex
     * @param incidentEdgeToRemove edge to remove
     */
    void removeIncidentEdge(DirectedEdge incidentEdgeToRemove) {
        // Check whether the input incident edge is null
        if (incidentEdgeToRemove == null) {
            throw new IllegalArgumentException("The incident edge to remove should not be null.");
        }
        // Check whether the input incident edge involves this vertex as the head
        if (incidentEdgeToRemove.head() != this) {
            throw new IllegalArgumentException("The incident edge to remove should involve this vertex as the head.");
        }
        // Check whether the input incident edge exists
        if (!incidentNeighbors.get(incidentEdgeToRemove.tail().id())) {
            throw new IllegalArgumentException("The incident edge to remove doesn't exist.");
        }

        incidentEdges.remove(incidentEdgeToRemove);
        incidentNeighbors.clear(incidentEdgeToRemove.tail().id());
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append("Vertex #").append(vtxID).append('\n');
        s.append("Its emissive neighbors: ").append(emissiveNeighbors);
        s.append("Its incident neighbors: ").append(incidentNeighbors);
        return s.toString();
    }

}
