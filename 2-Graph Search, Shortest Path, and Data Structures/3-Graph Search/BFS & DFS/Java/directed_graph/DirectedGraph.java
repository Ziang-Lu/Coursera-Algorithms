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
        findableVtxIDs.add(srcVtxID);

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

                    findableVtxIDs.add(w.id());

                    // Push w to Q
                    queue.offer(w);
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
        ArrayDeque<Vertex> queue = new ArrayDeque<Vertex>();
        queue.offer(srcVtx);
        // 3. While Q is not empty
        while (!queue.isEmpty()) {
            // (1) Take out the first vertex v
            Vertex vtx = queue.poll();
            // (2) For every edge (v, w)
            for (DirectedEdge edge : vtx.emissiveEdges()) {
                Vertex w = edge.head();
                // If w is unexplored
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
        // Directed connectivity
        // TODO Auto-generated method stub
        return 0;
    }

    /*
     * Iterative implementation of DFS ignored (simply replacing the queue with
     * a stack in BFS).
     */

    @Override
    public ArrayList<Integer> dfs(int srcVtxID) {
        // Check whether the input source vertex exists
        Vertex srcVtx = findVtx(srcVtxID);
        if (srcVtx == null) {
            throw new IllegalArgumentException("The source vertex doesn't exist.");
        }

        // Initialize G as s explored and other vertices unexplored
        srcVtx.setAsExplored();

        ArrayList<Integer> findableVtxIDs = new ArrayList<Integer>();
        findableVtxIDs.add(srcVtxID);

        dfsHelper(srcVtx, findableVtxIDs);

        return findableVtxIDs;
    }

    /**
     * Private helper method to do DFS and find all the findable vertices from
     * the given vertex along directed edges recursively.
     * @param vtx given vertex
     * @param findableVtxIDs all the findable vertices
     */
    private void dfsHelper(Vertex vtx, ArrayList<Integer> findableVtxIDs) {
        // For every directed edge (v, w)
        for (DirectedEdge edge : vtx.emissiveEdges()) {
            Vertex w = edge.head();
            // If w is unexplored   (This itself serves as a base case: all the w's of v are explored.)
            if (!w.explored()) {
                // Mark w as explored
                w.setAsExplored();

                findableVtxIDs.add(w.id());

                // Do DFS on (G, w) (Recursion)
                dfsHelper(w, findableVtxIDs);
            }
        }
    }

    @Override
    public int numOfConnectedComponentsWithDFS() {
        // Directed connectivity
        /*
         * Strong Connected Component (SCC):
         * A graph is strongly connected iff you can get from any vertex to any
         * other vertex through directed edges.
         * => An SCC of a directed graph is a part of the graph that is strongly
         *    connected.
         */
        // 1. Run DFS-loop on G   [First pass]
        ArrayList<Vertex> vtxsSortedByFinishTime = getVtxsSortedByFinishTime();
        // Essentially, the vertices sorted by this finishing time is exactly the reversed topological ordering.

        // 2. Clear explored and get ready for the second pass
        clearExplored();

        // 3. Run DFS-loop on G   [Second pass]
        int count = 0;
        // For every vertex v from finishing time n to 1   (i.e., reversed topological ordering)
        for (Vertex vtx : vtxsSortedByFinishTime) {
            // If v is unexplored (i.e., not explored from some previous DFS)
            if (!vtx.explored()) {
                // Mark v as explored
                vtx.setAsExplored();
                ++count;
                // Do DFS towards v   (Discovers precisely v's SCC)
                dfs(vtx.id());
            }
        }
        return count;
    }

    /**
     * Private helper method to get the vertices sorted by finishing time when
     * using DFS.
     * @return vertices sorted by finishing time
     */
    private ArrayList<Vertex> getVtxsSortedByFinishTime() {
        ArrayList<Vertex> vtxsSortedByFinishTime = new ArrayList<Vertex>();
        // For every vertex v
        for (Vertex vtx : vtxList) {
            // If v is unexplored
            if (!vtx.explored()) {
                // Mark v as explored
                vtx.setAsExplored();
                // Do DFS towards v
                dfsHelperWithFinishTime(vtx, vtxsSortedByFinishTime);
            }
        }
        return vtxsSortedByFinishTime;
    }

    /**
     * Helper method to do DFS and set the finishing time of the given
     * vertex recursively.
     * @param vtx given vertex
     * @param vtxsSortedByFinishTime vertices sorted by finishing time
     */
    private void dfsHelperWithFinishTime(Vertex vtx, ArrayList<Vertex> vtxsSortedByFinishTime) {
        // For every edge (v, w)
        for (DirectedEdge edge : vtx.emissiveEdges()) {
            Vertex w = edge.head();
            // If w is unexplored
            if (!w.explored()) {
                // Mark w as explored
                w.setAsExplored();
                // Do DFS towards w
                dfsHelperWithFinishTime(w, vtxsSortedByFinishTime);
            }
        }
        // Set v's finishing time
        vtxsSortedByFinishTime.add(vtx);
    }

    /**
     * Returns the topological ordering of the vertices of this directed graph
     * using DFS.
     * @return topological ordering of the vertices
     */
    public int[] topologicalSort() {
        ArrayList<Vertex> vtxsSortdByFinishTime = getVtxsSortedByFinishTime();
        // Essentially, the vertices sorted by this finishing time is exactly the reversed topological ordering.

        int n = vtxList.size();
        int[] topologicalOrdering = new int[n];
        for (int i = 0; i < n; i++) {
            topologicalOrdering[i] = vtxsSortdByFinishTime.get(n - 1 - i).id();
        }
        return topologicalOrdering;
    }

    /**
     * Returns the topological ordering of the vertices of this directed graph
     * using straightforward algorithm.
     * @return topological ordering of the vertices
     */
    public int[] topologicalSortStraightforward() {
        int[] topologicalOrdering = new int[vtxList.size()];
        topologicalSortStraightforwardHelper(topologicalOrdering, vtxList.size());
        return topologicalOrdering;
    }

    /**
     * Private helper method to fill in the given order of the topological
     * ordering using straightforward algorithm recursively.
     * @param topologicalOrdering topological ordering of the vertices
     * @param currOrder current order
     */
    private void topologicalSortStraightforwardHelper(int[] topologicalOrdering, int currOrder) {
        // Base case 1: Finished ordering
        if (currOrder == 0) {
            return;
        }
        Vertex sinkVtx = getSinkVertex();
        // Base case 2: No more sink vertices
        if (sinkVtx == null) {
            return;
        }

        // Recursive case
        topologicalOrdering[currOrder - 1] = sinkVtx.id();
        removeVtx(sinkVtx);
        topologicalSortStraightforwardHelper(topologicalOrdering, currOrder - 1);
    }

    /**
     * Helper method to get a sink vertex.
     * @return sink vertex
     */
    private Vertex getSinkVertex() {
        for (Vertex vtx : vtxList) {
            if (vtx.emissiveEdges().isEmpty()) {
                return vtx;
            }
        }
        // No more sink vertices
        return null;
    }

}
