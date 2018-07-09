package undirected_graph;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

import graph.GraphInterface;

/**
 * Adjacency list representation of a undirected graph.
 *
 * Note that parallel edges and self-loops are not allowed.
 * @author Ziang Lu
 */
public class UndirectedGraph implements GraphInterface {

    /**
     * Vertex list.
     */
    private final List<Vertex> vtxList;
    /**
     * Edge list.
     */
    private final List<UndirectedEdge> edgeList;

    /**
     * Default constructor.
     */
    public UndirectedGraph() {
        vtxList = new ArrayList<>();
        edgeList = new ArrayList<>();
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
            throw new IllegalArgumentException("The input vertex doesn't exist.");
        }

        removeVtx(vtxToRemove);
    }

    /**
     * Private helper method to remove the given vertex from this graph.
     * @param vtxToRemove vertex to remove
     */
    private void removeVtx(Vertex vtxToRemove) {
        // Remove all the edges associated with the vertex to remove
        List<UndirectedEdge> edgesToRemove = vtxToRemove.edges();
        while (edgesToRemove.size() > 0) {
            removeEdge(edgesToRemove.get(0));
        }
        // Remove the vertex
        vtxList.remove(vtxToRemove);
    }

    @Override
    public void addEdge(int end1ID, int end2ID) {
        // Check whether the input endpoints both exist
        Vertex end1 = findVtx(end1ID), end2 = findVtx(end2ID);
        if ((end1 == null) || (end2 == null)) {
            throw new IllegalArgumentException("The endpoints don't both exist.");
        }
        // Check whether the input endpoints are the same (self-loop)
        if (end1ID == end2ID) {
            throw new IllegalArgumentException("The endpoints are the same (self-loop).");
        }

        UndirectedEdge newEdge = new UndirectedEdge(end1, end2);
        addEdge(newEdge);
    }

    /**
     * Private helper method to add the given edge to this graph.
     * @param newEdge new edge
     */
    private void addEdge(UndirectedEdge newEdge) {
        Vertex end1 = newEdge.end1(), end2 = newEdge.end2();
        end1.addEdge(newEdge);
        end2.addEdge(newEdge);
        edgeList.add(newEdge);
    }

    @Override
    public void removeEdge(int end1ID, int end2ID) {
        // Check whether the input vertices both exist
        Vertex end1 = findVtx(end1ID), end2 = findVtx(end2ID);
        if ((end1 == null) || (end2 == null)) {
            throw new IllegalArgumentException("The input vertices don't both exist.");
        }
        // Check whether the edge to remove exists
        UndirectedEdge edgeToRemove = end1.getEdgeWithNeighbor(end2);
        if (edgeToRemove == null) {
            throw new IllegalArgumentException("The edge to remove doesn't exist.");
        }

        removeEdge(edgeToRemove);
    }

    /**
     * Private helper method to remove the given edge from this graph.
     * @param edgeToRemove edge to remove
     */
    private void removeEdge(UndirectedEdge edgeToRemove) {
        Vertex end1 = edgeToRemove.end1(), end2 = edgeToRemove.end2();
        end1.removeEdge(edgeToRemove);
        end2.removeEdge(edgeToRemove);
        edgeList.remove(edgeToRemove);
    }

    /**
     * Removes all the edges between a vertex pair from this graph.
     * @param end1ID endpoint1 ID
     * @param end2ID endpoint2 ID
     */
    public void removeEdgesBetweenPair(int end1ID, int end2ID) {
        try {
            while (true) {
                removeEdge(end1ID, end2ID);
            }
        } catch (IllegalArgumentException ex) {}
    }

    @Override
    public void showGraph() {
        System.out.println("The vertices are:");
        for (Vertex vtx : vtxList) {
            System.out.println(vtx);
        }
        System.out.println("The edges are:");
        for (UndirectedEdge edge : edgeList) {
            System.out.println(edge);
        }
    }

    @Override
    public List<Integer> bfs(int srcVtxID) {
        // Check whether the input source vertex exists
        Vertex srcVtx = findVtx(srcVtxID);
        if (srcVtx == null) {
            throw new IllegalArgumentException("The source vertex doesn't exist.");
        }

        // 1. Initialize G as s explored and other vertices unexplored
        srcVtx.setAsExplored();
        // 2. Let Q be the queue of vertices initialized with s
        Queue<Vertex> queue = new ArrayDeque<>();
        queue.offer(srcVtx);

        List<Integer> findableVtxIDs = new ArrayList<>();
        findableVtxIDs.add(srcVtxID);

        // 3. While Q is not empty
        while (!queue.isEmpty()) {
            // (1) Take out the first vertex v
            Vertex vtx = queue.poll();
            // (2) For every edge (v, w)
            for (UndirectedEdge edge : vtx.edges()) {
                // Find the neighbor
                Vertex neighbor = null;
                if (edge.end1() == vtx) { // endpoint2 is the neighbor.
                    neighbor = edge.end2();
                } else { // endpoint1 is the neighbor.
                    neighbor = edge.end1();
                }
                // If w is unexplored
                if (!neighbor.explored()) {
                    // Mark w as explored
                    neighbor.setAsExplored();

                    findableVtxIDs.add(neighbor.id());

                    // Push w to Q
                    queue.offer(neighbor);
                }
            }
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
        Queue<Vertex> queue = new ArrayDeque<>();
        queue.offer(srcVtx);
        // 3. While Q is not empty
        while (!queue.isEmpty()) {
            // (1) Take out the first vertex v
            Vertex vtx = queue.poll();
            // (2) For every edge (v, w)
            for (UndirectedEdge edge : vtx.edges()) {
                // Find the neighbor
                Vertex neighbor = null;
                if (edge.end1() == vtx) { // endpoint2 is the neighbor.
                    neighbor = edge.end2();
                } else { // endpoint1 is the neighbor.
                    neighbor = edge.end1();
                }
                // If w is unexplored
                if (!neighbor.explored()) {
                    // Mark w as explored
                    neighbor.setAsExplored();

                    neighbor.setLayer(vtx.layer() + 1);
                    if (neighbor == destVtx) { // Found it
                        return destVtx.layer();
                    }

                    // Push w to Q
                    queue.offer(neighbor);
                }
            }
        }
        // The destination vertex is not findable starting from the given source vertex.
        return Integer.MAX_VALUE;
    }

    @Override
    public int numOfConnectedComponentsWithBFS() {
        // Undirected connectivity
        List<List<Integer>> components = new ArrayList<>();
        for (Vertex vtx : vtxList) {
            // If v is unexplored (i.e., not explored from some previous BFS)
            if (!vtx.explored()) {
                // Do BFS towards v   (Discovers precisely v's connected component)
                List<Integer> component = bfs(vtx.id());
                components.add(component);
            }
        }
        return components.size();
    }

    /*
     * Iterative implementation of DFS ignored (simply replacing the queue with
     * a stack in BFS).
     */

    @Override
    public List<Integer> dfs(int srcVtxID) {
        // Check whether the input source vertex exists
        Vertex srcVtx = findVtx(srcVtxID);
        if (srcVtx == null) {
            throw new IllegalArgumentException("The source vertex doesn't exist.");
        }

        // Initialize G as s explored and other vertices unexplored
        srcVtx.setAsExplored();

        List<Integer> findableVtxIDs = new ArrayList<>();
        findableVtxIDs.add(srcVtxID);

        dfsHelper(srcVtx, findableVtxIDs);

        return findableVtxIDs;
    }

    /**
     * Private helper method to do DFS and find all the findable vertices from
     * the given vertex recursively.
     * @param vtx
     * @param findableVtxIDs all the findable vertices
     */
    private void dfsHelper(Vertex vtx, List<Integer> findableVtxIDs) {
        // For every edge (v, w)
        for (UndirectedEdge edge : vtx.edges()) {
            // Find the neighbor
            Vertex neighbor = null;
            if (edge.end1() == vtx) { // endpoint2 is the neighbor.
                neighbor = edge.end2();
            } else { // endpoint1 is the neighbor.
                neighbor = edge.end1();
            }
            // If w is unexplored   (This itself serves as a base case: all the w's of v are explored.)
            if (!neighbor.explored()) {
                // Mark w as explored
                neighbor.setAsExplored();

                findableVtxIDs.add(neighbor.id());

                // Do DFS on (G, w)   (Recursion)
                dfsHelper(neighbor, findableVtxIDs);
            }
        }
    }

    @Override
    public int numOfConnectedComponentsWithDFS() {
        // Undirected connectivity
        List<List<Integer>> components = new ArrayList<>();
        // For every vertex v
        for (Vertex vtx : vtxList) {
            // If v is unexplored (i.e., not explored from some previous DFS)
            if (!vtx.explored()) {
                // Do DFS towards v   (Discovers precisely v's connected component)
                List<Integer> component = dfs(vtx.id());
                components.add(component);
            }
        }
        return components.size();
    }

}
