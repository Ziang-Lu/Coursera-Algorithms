package directed_graph;

import java.util.ArrayList;

/**
 * Adjacency list representation of a directed graph.
 *
 * Note that parallel edges are allowed, but not self-loops.
 * @author Ziang Lu
 */
public class AdjacencyList {

    /**
     * Vertex list.
     */
    private ArrayList<Vertex> vtxList;
    /**
     * Edge list.
     */
    private ArrayList<Edge> edgeList;

    /**
     * Default constructor.
     */
    public AdjacencyList() {
        vtxList = new ArrayList<Vertex>();
        edgeList = new ArrayList<Edge>();
    }

    /**
     * Adds a new vertex to this graph.
     * @param newVtxID new vertex ID
     */
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
            if (vtx.getID() == vtxID) {
                return vtx;
            }
        }
        // Not found
        return null;
    }

    /**
     * Removes a vertex from this graph.
     * @param vtxID vertex ID
     */
    public void removeVtx(int vtxID) {
        // Check whether the input vertex exists
        Vertex vtxToRemove = findVtx(vtxID);
        if (vtxToRemove == null) {
            throw new IllegalArgumentException("The inpue vertex doesn't exist.");
        }

        removeVtx(vtxToRemove);
    }

    /**
     * Private helper method to remove the given vertex from this graph.
     * @param vtxToRemove vertex to remove
     */
    private void removeVtx(Vertex vtxToRemove) {
        // Remove all the edges associated with the vertex to remove
        ArrayList<Edge> edgesToRemove = new ArrayList<Edge>();
        edgesToRemove.addAll(vtxToRemove.getEmissiveEdges());
        edgesToRemove.addAll(vtxToRemove.getIncidentEdges());
        while (edgesToRemove.size() > 0) {
            Edge edgeToRemove = edgesToRemove.get(0);
            removeEdge(edgeToRemove);
        }
        // Remove the vertex
        vtxList.remove(vtxToRemove);
    }

    /**
     * Adds a new edge to this graph.
     * @param tailID tail ID
     * @param headID head ID
     */
    public void addEdge(int tailID, int headID) {
        // Check whether the input vertices both exist
        Vertex tail = findVtx(tailID), head = findVtx(headID);
        if ((tail == null) || (head == null)) {
            throw new IllegalArgumentException("The input vertices don't both exist.");
        }
        // Check whether the input vertices both exist
        if (tailID == headID) {
            throw new IllegalArgumentException("The input vertices are the same (self-loop).");
        }

        Edge newEdge = new Edge(tail, head);
        addEdge(newEdge);
    }

    /**
     * Private helper method to add the given edge to this graph.
     * @param newEdge new edge
     */
    private void addEdge(Edge newEdge) {
        Vertex tail = newEdge.tail, head = newEdge.head;
        tail.addEmissiveEdge(newEdge);
        head.addIncidentEdge(newEdge);
        edgeList.add(newEdge);
    }

    /**
     * Removes an edge from this graph.
     * @param tailID tail ID
     * @param headID head ID
     */
    public void removeEdge(int tailID, int headID) {
        // Check whether the input vertices both exist
        Vertex tail = findVtx(tailID), head = findVtx(headID);
        if ((tail == null) || (head == null)) {
            throw new IllegalArgumentException("The input vertices don't both exist.");
        }
        // Check whether the edge to remove exists
        Edge edgeToRemove = tail.getEmissiveEdgeWithHead(head);
        if (edgeToRemove == null) {
            throw new IllegalArgumentException("The edge to remove doesn't exist.");
        }

        removeEdge(edgeToRemove);
    }

    /**
     * Private helper method to remove the given edge from this graph.
     * @param edgeToRemove edge to remove
     */
    private void removeEdge(Edge edgeToRemove) {
        Vertex tail = edgeToRemove.tail, head = edgeToRemove.head;
        tail.removeEmissiveEdge(edgeToRemove);
        head.removeIncidentEdge(edgeToRemove);
        edgeList.remove(edgeToRemove);
    }

    /**
     * Shows the graph.
     */
    public void showGraph() {
        System.out.println("The vertices are:");
        for (Vertex vtx : vtxList) {
            System.out.println(vtx);
        }
        System.out.println("The edges are:");
        for (Edge edge : edgeList) {
            System.out.println(edge);
        }
    }

}
