package directed_graph;

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
     * Emissive edge of this vertex.
     */
    ArrayList<Edge> emissiveEdges;
    /**
     * Frequency of emissive neighbors.
     */
    HashMap<Integer, Integer> freqOfEmissiveNeighbors;
    /**
     * Incident of this vertex.
     */
    ArrayList<Edge> incidentEdges;
    /**
     * Frequency of incident neighbors.
     */
    HashMap<Integer, Integer> freqOfIncidentNeighbors;

    /**
     * Constructor with parameter.
     * @param vtxID vertex ID
     */
    Vertex(int vtxID) {
        this.vtxID = vtxID;
        emissiveEdges = new ArrayList<Edge>();
        freqOfEmissiveNeighbors = new HashMap<Integer, Integer>();
        incidentEdges = new ArrayList<Edge>();
        freqOfIncidentNeighbors = new HashMap<Integer, Integer>();
    }

    /**
     * Adds the given emissive edge to this vertex.
     * @param newEdge emissive edge to add
     */
    void addEmissiveEdge(Edge newEmissiveEdge) {
        // Check whether the input emissive edge is null
        if (newEmissiveEdge == null) {
            System.out.println("The emissive edge to add should not be null.");
            return;
        }
        // Check whether the input emissive edge involves this vertex as the tail
        if (newEmissiveEdge.tail != this) {
            System.out.println("The emissive edge to add should involve this vertex as the tail.");
            return;
        }

        emissiveEdges.add(newEmissiveEdge);

        // Find the emissive neighbor associated with the input emissive edge
        Vertex emissiveNeighbor = newEmissiveEdge.head;
        // Update the frequency of the emissive neighbor
        Integer freq = freqOfEmissiveNeighbors.getOrDefault(emissiveNeighbor.vtxID, 0);
        ++freq;
        freqOfEmissiveNeighbors.put(emissiveNeighbor.vtxID, freq);
    }

    /**
     * Adds the given incident edge to this vertex.
     * @param newIncidentEdge incident edge to add
     */
    void addIncidentEdge(Edge newIncidentEdge) {
        // Check whether the input incident edge is null
        if (newIncidentEdge == null) {
            System.out.println("The incident edge to add should not be null.");
            return;
        }
        // Check whether the input incident edge involves this vertex as the head
        if (newIncidentEdge.head != this) {
            System.out.println("The incident edge to add should involve this vertex as the head.");
            return;
        }

        incidentEdges.add(newIncidentEdge);

        // Find the incident neighbor associated with the input incident edge
        Vertex incidentNeighbor = newIncidentEdge.tail;
        // Update the frequency of the incident neighbor
        Integer freq = freqOfIncidentNeighbors.getOrDefault(incidentNeighbor.vtxID, 0);
        ++freq;
        freqOfIncidentNeighbors.put(incidentNeighbor.vtxID, freq);
    }

    /**
     * Removes the given emissive edge from this vertex
     * @param emissiveEdgeToRemove edge to remove
     */
    void removeEmissiveEdge(Edge emissiveEdgeToRemove) {
        // Check whether the input emissive edge is null
        if (emissiveEdgeToRemove == null) {
            System.out.println("The emissive edge to remove should not be null.");
            return;
        }
        // Check whether the input emissive edge involves this vertex as the tail
        if (emissiveEdgeToRemove.tail != this) {
            System.out.println("The emissive edge to remove should involve this vertex as the tail.");
            return;
        }

        emissiveEdges.remove(emissiveEdgeToRemove);

        // Find the emissive neighbor associated with the input emissive edge
        Vertex emissiveNeighbor = emissiveEdgeToRemove.head;
        // Update the frequency of the emissive neighbor
        Integer freq = freqOfEmissiveNeighbors.get(emissiveNeighbor.vtxID);
        if (freq == 1) {
            freqOfEmissiveNeighbors.remove(emissiveNeighbor.vtxID);
        } else {
            --freq;
            freqOfEmissiveNeighbors.put(emissiveNeighbor.vtxID, freq);
        }
    }

    /**
     * Removes the given incident edge from this vertex
     * @param incidentEdgeToRemove edge to remove
     */
    void removeIncidentEdge(Edge incidentEdgeToRemove) {
        // Check whether the input incident edge is null
        if (incidentEdgeToRemove == null) {
            System.out.println("The incident edge to remove should not be null.");
            return;
        }
        // Check whether the input incident edge involves this vertex as the head
        if (incidentEdgeToRemove.tail != this) {
            System.out.println("The incident edge to remove should involve this vertex as the head.");
            return;
        }

        incidentEdges.remove(incidentEdgeToRemove);

        // Find the incident neighbor associated with the input incident edge
        Vertex incidentNeighbor = incidentEdgeToRemove.tail;
        // Update the frequency of the emissive neighbor
        Integer freq = freqOfIncidentNeighbors.get(incidentNeighbor.vtxID);
        if (freq == 1) {
            freqOfIncidentNeighbors.remove(incidentNeighbor.vtxID);
        } else {
            --freq;
            freqOfIncidentNeighbors.put(incidentNeighbor.vtxID, freq);
        }
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append("Vertex #").append(vtxID).append('\n');
        s.append("Its emissive neighbors and frequencyes: ").append(freqOfEmissiveNeighbors).append('\n');
        s.append("Its incident neighbors and frequencyes: ").append(freqOfIncidentNeighbors).append('\n');
        return s.toString();
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
