package directed_graph;

import java.util.ArrayList;

/**
 * Adjacency list representation of a directed graph.
 *
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
        if (findVtx(newVtxID) == null) {
            System.out.println("The input vertex is repeated.");
            return;
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
            if (vtx.vtxID == vtxID) {
                return vtx;
            }
        }
        // Not found
        return null;
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
            System.out.println("The input vertices don't both exist.");
            return;
        }
        // Check whether the edge to add already exists
        if (findEdge(tailID, headID) != null) {
            System.out.println("The edge to add already exists.");
            return;
        }

        Edge newEdge = new Edge(tail, head);
        tail.addEdge(newEdge);
        head.addEdge(newEdge);
        edgeList.add(newEdge);
    }

    /**
     * Private helper method to find the edge from the given tail to the given
     * head.
     * @param tailID tail ID
     * @param headID head ID
     * @return edge if found, null if not found
     */
    private Edge findEdge(int tailID, int headID) {
        for (Edge edge : edgeList) {
            if ((edge.tail.vtxID == tailID) && (edge.head.vtxID == headID)) {
                return edge;
            }
        }
        // Not found
        return null;
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
            System.out.println("The input vertices don't both exist.");
            return;
        }
        // Check whether the edge to remove exists
        Edge edgeToRemove = findEdge(tailID, headID);
        if (edgeToRemove == null) {
            System.out.println("The edge to remove doesn't exist.");
            return;
        }

        tail.removeEdge(edgeToRemove);
        head.removeEdge(edgeToRemove);
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
