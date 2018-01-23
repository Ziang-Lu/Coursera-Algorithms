package directed_graph;

import java.util.ArrayDeque;
import java.util.ArrayList;

import graph.GraphInterface;

/**
 * Adjacency list representation of a directed graph.
 *
 * Note that parallel edges are allowed, but not self-loops.
 * @author Ziang Lu
 */
public class DirectedGraph implements GraphInterface {

    /**
     * Vertex list.
     */
    private ArrayList<Vertex> vtxList;
    /**
     * Edge list.
     */
    private ArrayList<DirectedEdge> edgeList;

    /**
     * Default constructor.
     */
    public DirectedGraph() {
        vtxList = new ArrayList<Vertex>();
        edgeList = new ArrayList<DirectedEdge>();
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
        ArrayList<DirectedEdge> edgesToRemove = new ArrayList<DirectedEdge>();
        edgesToRemove.addAll(vtxToRemove.emissiveEdges());
        edgesToRemove.addAll(vtxToRemove.incidentEdges());
        while (edgesToRemove.size() > 0) {
            DirectedEdge edgeToRemove = edgesToRemove.get(0);
            removeEdge(edgeToRemove);
        }
        // Remove the vertex
        vtxList.remove(vtxToRemove);
    }

    @Override
    public void addEdge(int tailID, int headID) {
        // Check whether the input endpoints both exist
        Vertex tail = findVtx(tailID), head = findVtx(headID);
        if ((tail == null) || (head == null)) {
            throw new IllegalArgumentException("The endpoints don't both exist.");
        }
        // Check whether the input endpoints both exist
        if (tailID == headID) {
            throw new IllegalArgumentException("The endpoints are the same (self-loop).");
        }

        DirectedEdge newEdge = new DirectedEdge(tail, head);
        addEdge(newEdge);
    }

    /**
     * Private helper method to add the given edge to this graph.
     * @param newEdge new edge
     */
    private void addEdge(DirectedEdge newEdge) {
        Vertex tail = newEdge.tail(), head = newEdge.head();
        tail.addEmissiveEdge(newEdge);
        head.addIncidentEdge(newEdge);
        edgeList.add(newEdge);
    }

    @Override
    public void removeEdge(int tailID, int headID) {
        // Check whether the input endpoints both exist
        Vertex tail = findVtx(tailID), head = findVtx(headID);
        if ((tail == null) || (head == null)) {
            throw new IllegalArgumentException("The endpoints don't both exist.");
        }
        // Check whether the edge to remove exists
        DirectedEdge edgeToRemove = tail.getEmissiveEdgeWithHead(head);
        if (edgeToRemove == null) {
            throw new IllegalArgumentException("The edge to remove doesn't exist.");
        }

        removeEdge(edgeToRemove);
    }

    /**
     * Private helper method to remove the given edge from this graph.
     * @param edgeToRemove edge to remove
     */
    private void removeEdge(DirectedEdge edgeToRemove) {
        Vertex tail = edgeToRemove.tail(), head = edgeToRemove.head();
        tail.removeEmissiveEdge(edgeToRemove);
        head.removeIncidentEdge(edgeToRemove);
        edgeList.remove(edgeToRemove);
    }

    /**
     * Removes all the directed edges between a vertex pair from this graph.
     * @param tailID tail ID
     * @param headID head ID
     */
    public void removeDirectedEdgesBetweenPair(int tailID, int headID) {
        try {
            removeEdge(tailID, headID);
        } catch (IllegalArgumentException ex) {};
    }

    @Override
    public void showGraph() {
        System.out.println("The vertices are:");
        for (Vertex vtx : vtxList) {
            System.out.println(vtx);
        }
        System.out.println("The edges are:");
        for (DirectedEdge edge : edgeList) {
            System.out.println(edge);
        }
    }

    @Override
    public ArrayList<Integer> bfs(int srcVtxID) {
        // Check whether the input source vertex exists
        Vertex srcVtx = findVtx(srcVtxID);
        if (srcVtx == null) {
            throw new IllegalArgumentException("The source vertex doesn't exist.");
        }

        // 1. Initialize G as s explored and other vertices unexplored
        srcVtx.setAsExplored();
        // 2. Let Q be the queue of vertices initialized with s
        ArrayDeque<Vertex> queue = new ArrayDeque<Vertex>();
        queue.offer(srcVtx);

        ArrayList<Integer> findableVtxIDs = new ArrayList<Integer>();

        // 3. While Q is not empty
        while (!queue.isEmpty()) {
            // (1) Take out the first vertex v
            Vertex vtx = queue.poll();
            // (2) For every edge (v, w)
            for (DirectedEdge edge : vtx.emissiveEdges()) {
                Vertex w = edge.head();
                if (!w.explored()) {
                    // Mark w as explored
                    w.setAsExplored();
                    // Push w to Q
                    queue.offer(w);
                }
            }

            findableVtxIDs.add(vtx.id());
        }

        return findableVtxIDs;
    }

    @Override
    public void clearExplored() {
        for (Vertex vtx : vtxList) {
            vtx.setAsUnexplored();
        }
    }

    @Override
    public int shortestPath(int srcVtxID, int destVtxID) {
        // Check whether the input source and destination vertices both exist
        Vertex srcVtx = findVtx(srcVtxID), destVtx = findVtx(destVtxID);
        if ((srcVtx == null) || (destVtx == null)) {
            throw new IllegalArgumentException("The source and destination vertices don't both exist.");
        }

        // 1. Initialize G as s explored and other vertices unexplored
        srcVtx.setAsExplored();
        // 2. Let Q be the queue of vertices initialized with s
        ArrayDeque<Vertex> queue = new ArrayDeque<Vertex>();
        queue.offer(srcVtx);
        // 3. While Q is not empty
        while (!queue.isEmpty()) {
            // (1) Take out the first vertex v
            Vertex vtx = queue.poll();
            // (2) For every edge (v, w)
            for (DirectedEdge edge : vtx.emissiveEdges()) {
                Vertex w = edge.head();
                // If w is not explored
                if (!w.explored()) {
                    // Mark w as explored
                    w.setAsExplored();

                    w.setLayer(vtx.layer() + 1);
                    if (w == destVtx) { // Found it
                        return destVtx.layer();
                    }

                    // Push w to Q
                    queue.offer(w);
                }
            }
        }
        // The destination vertex is not findable starting from the given source vertex along directed edges.
        return Integer.MAX_VALUE;
    }

    @Override
    public int numOfConnectedComponentsWithBFS() {
        // TODO Auto-generated method stub
        return 0;
    }

}
