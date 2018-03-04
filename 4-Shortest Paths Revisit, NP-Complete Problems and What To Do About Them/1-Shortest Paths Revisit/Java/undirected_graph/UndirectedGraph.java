package undirected_graph;

import java.util.ArrayList;
import java.util.LinkedList;

import graph.GraphInterface;

/**
 * Adjacency list representation of a undirected graph.
 *
 * Note that parallel edges are allowed, but not self-loops.
 * @author Ziang Lu
 */
public class UndirectedGraph implements GraphInterface {

    /**
     * Infinity value.
     */
    private static final int INFINITY = 1000000;

    /**
     * Vertex list.
     */
    private final ArrayList<Vertex> vtxList;
    /**
     * Edge list.
     */
    private final ArrayList<UndirectedEdge> edgeList;
    /**
     * Bellman-Ford subproblem solutions.
     * Since there are only O(n^2) subproblems, the first time we solve a
     * subproblem, we can cache its solution in a global take for O(1) lookup
     * time later on.
     */
    private Integer[][] bellmanFordSubproblems;

    /**
     * Default constructor.
     */
    public UndirectedGraph() {
        vtxList = new ArrayList<Vertex>();
        edgeList = new ArrayList<UndirectedEdge>();
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
        ArrayList<UndirectedEdge> edgesToRemove = vtxToRemove.edges();
        while (edgesToRemove.size() > 0) {
            UndirectedEdge edgeToRemove = edgesToRemove.get(0);
            removeEdge(edgeToRemove);
        }
        // Remove the vertex
        vtxList.remove(vtxToRemove);
    }

    @Override
    public void addEdge(int end1ID, int end2ID, int length) {
        // Check whether the input endpoints both exist
        Vertex end1 = findVtx(end1ID), end2 = findVtx(end2ID);
        if ((end1 == null) || (end2 == null)) {
            throw new IllegalArgumentException("The endpoints don't both exist.");
        }
        // Check whether the input endpoints are the same (self-loop)
        if (end1ID == end2ID) {
            throw new IllegalArgumentException("The endpoints are the same (self-loop).");
        }

        UndirectedEdge newEdge = new UndirectedEdge(end1, end2, length);
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
    public ArrayList<LinkedList<Integer>> bellmanFordShortestPaths(int srcVtxID) {
        // Check whether the input source vertex exists
        Vertex srcVtx = findVtx(srcVtxID);
        if (srcVtx == null) {
            throw new IllegalArgumentException("The source vertex doesn't exist.");
        }

        int n = vtxList.size();
        // Initialization
        bellmanFordSubproblems = new Integer[n][n];
        for (Vertex vtx : vtxList) {
            if (vtx != srcVtx) {
                bellmanFordSubproblems[vtx.id()][0] = INFINITY;
            }
        }
        // Bottom-up calculation
        for (int budget = 1; budget <= (n - 1); ++budget) {
            for (Vertex vtx : vtxList) {
                // Case 1: P(s, v, i) has <= (i - 1) edges (i.e., P doesn't use up all of its budget i.)
                Integer minPathLength = bellmanFordSubproblems[vtx.id()][budget - 1];
                // Case 2: P(s, v, i) has exactly i edges (i.e., P uses up all of its budget i.), with final hop (w, v)
                for (UndirectedEdge edge : vtx.edges()) {
                    // Find the neighbor
                    Vertex neighbor = null;
                    if (edge.end1() == vtx) { // endpoint2 is the neighbor.
                        neighbor = edge.end2();
                    } else { // endpoint1 is the neighbor.
                        neighbor = edge.end1();
                    }
                    // By plucking off the final hop (w, v), we form P'(s, w, i - 1).
                    int pathLength = bellmanFordSubproblems[neighbor.id()][budget - 1] + edge.length();
                    if (pathLength < minPathLength) {
                        minPathLength = pathLength;
                    }
                    // P(s, v, i) is the minimum among the above (1 + in-degree(v)) candidates.
                    bellmanFordSubproblems[vtx.id()][budget] = minPathLength;
                }
            }
        }
        /*
         * Extension to detect negative cycles reachable from s:
         * If the input graph has negative cycles reachable from s, then we just run the outer loop for one extra
         * iteration, and check if there is still an improvement on some vertex. If so, then the input graph must have
         * negative cycles reachable from s.
         */
        boolean madeUpdateInExtraIter = false;
        for (Vertex vtx : vtxList) {
            Integer minPathLength = bellmanFordSubproblems[vtx.id()][n - 1];
            for (UndirectedEdge edge : vtx.edges()) {
                // Find the neighbor
                Vertex neighbor = null;
                if (edge.end1() == vtx) { // endpoint2 is the neighbor.
                    neighbor = edge.end2();
                } else { // endpoint1 is the neighbor.
                    neighbor = edge.end1();
                }
                int pathLength = bellmanFordSubproblems[neighbor.id()][n - 1] + edge.length();
                if (pathLength < minPathLength) {
                    minPathLength = pathLength;
                    madeUpdateInExtraIter = true;
                }
            }
        }
        if (madeUpdateInExtraIter) {
            throw new IllegalArgumentException("The graph has negative cycles reachable from the source vertex.");
        }
        // The final solution lies in exactly subproblems[v][n - 1].
        return reconstructShortestPaths(srcVtxID, n - 1);
        // Outer for-loop: n iterations
        // Inner for-loop: sum(in-degree(v)) = m
        // => Overall running time complexity: O(mn)
        // Overall space complexity: O(n^2)
    }

    /**
     * Private helper method to reconstruct the shortest paths according to the
     * optimal solution using backtracking.
     * @param srcVtxID source vertex ID
     * @return shortest paths
     */
    private ArrayList<LinkedList<Integer>> reconstructShortestPaths(int srcVtxID) {
        ArrayList<LinkedList<Integer>> shortestPaths = new ArrayList<LinkedList<Integer>>();
        for (Vertex vtx : vtxList) {
            LinkedList<Integer> shortestPath = new LinkedList<Integer>();
            shortestPath.add(vtx.id());
            Vertex currVtx = vtx;
            int budget = vtxList.size() - 1;
            while (budget >= 1) {
                // Find the previous vertex to backtrack
                Vertex prevVtx = currVtx;
                Integer minPathLength = bellmanFordSubproblems[currVtx.id()][budget - 1];
                for (UndirectedEdge edge : currVtx.edges()) {
                    // Find the neighbor
                    Vertex neighbor = null;
                    if (edge.end1() == currVtx) { // endpoint2 is the neighbor.
                        neighbor = edge.end2();
                    } else { // endpoint1 is the neighbor.
                        neighbor = edge.end1();
                    }
                    int pathLength = bellmanFordSubproblems[neighbor.id()][budget - 1] + edge.length();
                    if (pathLength < minPathLength) {
                        prevVtx = neighbor;
                        minPathLength = pathLength;
                    }
                }
                if (prevVtx != currVtx) {
                    shortestPath.addFirst(prevVtx.id());
                }
                currVtx = prevVtx;
                --budget;
            }
            shortestPaths.add(shortestPath);
        }
        return shortestPaths;
        // Running time complexity: O(mn)
    }

    @Override
    public ArrayList<LinkedList<Integer>> bellmanFordShortestPathsOptimized(int srcVtxID) {
        // Check whether the input source vertex exists
        Vertex srcVtx = findVtx(srcVtxID);
        if (srcVtx == null) {
            throw new IllegalArgumentException("The source vertex doesn't exist.");
        }

        int n = vtxList.size();
        // Initialization
        // Space optimization: We only keep track of the subproblem solutions in the previous outer iteration.
        int[] prevIterSubproblems = new int[n], currIterSubproblems = new int[n];
        // In order to recover the ability to reconstruct the shortest paths, we also keep track of the penultimate
        // vertices in the previous outer iteration.
        Vertex[] prevIterPenultimateVtxs = new Vertex[n], currIterPenultimateVtxs = new Vertex[n];
        for (Vertex vtx : vtxList) {
            if (vtx != srcVtx) {
                prevIterSubproblems[vtx.id()] = INFINITY;
            }
        }
        // Bottom-up calculation
        int budget = 1;
        // Optimization: Early-stopping
        // The algorithm may stop early when in the current iteration, no update is made for any vertex.
        boolean madeUpdateInIter = true;
        while ((budget <= (n - 1)) && madeUpdateInIter) {
            madeUpdateInIter = false;
            for (Vertex vtx : vtxList) {
                Integer minPathLength = prevIterSubproblems[vtx.id()];
                Vertex penultimateVtx = prevIterPenultimateVtxs[vtx.id()];
                for (UndirectedEdge edge : vtx.edges()) {
                    // Find the neighbor
                    Vertex neighbor = null;
                    if (edge.end1() == vtx) { // endpoint2 is the neighbor.
                        neighbor = edge.end2();
                    } else { // endpoint1 is the neighbor.
                        neighbor = edge.end1();
                    }
                    int pathLength = prevIterSubproblems[neighbor.id()] + edge.length();
                    if (pathLength < minPathLength) {
                        minPathLength = pathLength;
                        madeUpdateInIter = true;
                        penultimateVtx = neighbor;
                    }
                    currIterSubproblems[vtx.id()] = minPathLength;
                    currIterPenultimateVtxs[vtx.id()] = penultimateVtx;
                }
            }
            ++budget;
            System.arraycopy(currIterSubproblems, 0, prevIterSubproblems, 0, n);
            System.arraycopy(currIterPenultimateVtxs, 0, prevIterPenultimateVtxs, 0, n);
        }
        madeUpdateInIter = false;
        for (Vertex vtx : vtxList) {
            Integer minPathLength = prevIterSubproblems[vtx.id()];
            for (UndirectedEdge edge : vtx.edges()) {
                // Find the neighbor
                Vertex neighbor = null;
                if (edge.end1() == vtx) { // endpoint2 is the neighbor.
                    neighbor = edge.end2();
                } else { // endpoint1 is the neighbor.
                    neighbor = edge.end1();
                }
                int pathLength = prevIterSubproblems[neighbor.id()] + edge.length();
                if (pathLength < minPathLength) {
                    minPathLength = pathLength;
                    madeUpdateInIter = true;
                }
            }
            currIterSubproblems[vtx.id()] = minPathLength;
        }
        if (madeUpdateInIter) {
            throw new IllegalArgumentException("The graph has negative cycles reachable from the source vertex.");
        }
        // The final solution lies in exactly prevIterSubproblems.

        // We can reconstruct the shortest paths from these penultimate vertices.
        return reconstructShortestPathsOptimized(srcVtxID, prevIterPenultimateVtxs);
        // Overall running time complexity: O(mn)
        // Overall space complexity: O(n)
    }

    /**
     * Private helper method to reconstruct the shortest paths according to the
     * penultimate vertices in the shortest paths using backtracking.
     * @param penultimateVtxs penultimate vertices in the shortest paths
     * @return shortest paths
     */
    private ArrayList<LinkedList<Integer>> reconstructShortestPathsOptimized(Vertex[] penultimateVtxs) {
        ArrayList<LinkedList<Integer>> shortestPaths = new ArrayList<LinkedList<Integer>>();
        for (Vertex vtx : vtxList) {
            LinkedList<Integer> shortestPath = new LinkedList<Integer>();
            Vertex currVtx = vtx;
            while (currVtx != null) {
                shortestPath.addFirst(currVtx.id());
                Vertex prevVtx = penultimateVtxs[currVtx.id()];
                currVtx = prevVtx;
            }
            shortestPaths.add(shortestPath);
        }
        return shortestPaths;
        // Running time complexity: O(n^2)
    }

}
