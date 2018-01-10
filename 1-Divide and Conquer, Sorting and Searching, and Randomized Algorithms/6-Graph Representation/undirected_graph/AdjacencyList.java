package undirected_graph;

import java.util.ArrayList;

/**
 * Adjacency list representation of a undirected graph.
 *
 * Note that parallel edges and self-loops are not allowed.
 * @author Ziang Lu
 */
public class AdjacencyList {

    /**
     * Vertex list.
     */
    public ArrayList<Vertex> vtxList;
    /**
     * Edge list.
     */
    public ArrayList<Edge> edgeList;

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
     * @param end1ID endpoint1 ID
     * @param end2ID endpoint2 ID
     */
    public void addEdge(int end1ID, int end2ID) {
        // Check whether the input vertices both exist
        Vertex end1 = findVtx(end1ID), end2 = findVtx(end2ID);
        if ((end1 == null) || (end2 == null)) {
            System.out.println("The input vertices don't both exist.");
            return;
        }
        // Check whether the edge to add already exists
        if (findEdge(end1ID, end2ID) != null) {
            System.out.println("The edge to add already exists.");
            return;
        }

        Edge newEdge = new Edge(end1, end2);
        end1.addEdge(newEdge);
        end2.addEdge(newEdge);
        edgeList.add(newEdge);
    }

    /**
     * Private helper method to find the edge connecting the given vertices in
     * this adjacency list.
     * @param end1ID endpoint1 ID
     * @param end2ID endpoint2 ID
     * @return edge if found, null if not found
     */
    private Edge findEdge(int end1ID, int end2ID) {
        for (Edge edge : edgeList) {
            int currEnd1ID = edge.end1.vtxID, currEnd2ID = edge.end2.vtxID;
            if (((currEnd1ID == end1ID) && (currEnd2ID == end2ID))
                    || ((currEnd1ID == end2ID) && (currEnd2ID == end1ID))) {
                return edge;
            }
        }
        // Not found
        return null;
    }

    /**
     * Removes an edge from this graph.
     * @param end1ID endpoint1 ID
     * @param end2ID endpoint2 ID
     */
    public void removeEdge(int end1ID, int end2ID) {
        // Check whether the input vertices both exist
        Vertex end1 = findVtx(end1ID), end2 = findVtx(end2ID);
        if ((end1 == null) || (end2 == null)) {
            System.out.println("The input vertices don't both exist.");
            return;
        }
        // Check whether the edge to remove exists
        Edge edgeToRemove = findEdge(end1ID, end2ID);
        if (edgeToRemove == null) {
            System.out.println("The edge to remove doesn't exist.");
            return;
        }

        end1.removeEdge(edgeToRemove);
        end2.removeEdge(edgeToRemove);
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

