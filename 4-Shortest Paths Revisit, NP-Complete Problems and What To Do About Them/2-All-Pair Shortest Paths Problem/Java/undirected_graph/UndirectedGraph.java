package undirected_graph;

import java.util.ArrayList;
import java.util.List;

import graph.GraphInterface;

/**
 * Adjacency list representation of a undirected graph.
 *
 * Note that parallel edges and self-loops are not allowed.
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
    private final List<Vertex> vtxList;
    /**
     * Edge list.
     */
    private final List<UndirectedEdge> edgeList;
    /**
     * Floyd-Warshall subproblem solutions.
     * Since there are only O(n^2) subproblems, the first time we solve a
     * subproblem, we can cache its solution in a global take for O(1) lookup
     * time later on.
     */
    private int[][][] floydWarshallSubproblems;

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
    public int[][] floydWarshallApsp() {
        int n = vtxList.size();
        // Initialization
        floydWarshallSubproblems = new int[n + 1][n][n];
        for (Vertex srcVtx : vtxList) {
            for (Vertex destVtx : vtxList) {
                if (srcVtx != destVtx) {
                    floydWarshallSubproblems[0][srcVtx.id()][destVtx.id()] = INFINITY;
                }
            }
        }
        for (UndirectedEdge edge : edgeList) {
            floydWarshallSubproblems[0][edge.end1().id()][edge.end2().id()] = edge.length();
            floydWarshallSubproblems[0][edge.end2().id()][edge.end1().id()] = edge.length();
        }
        // Bottom-up calculation
        for (int k = 1; k <= n; ++k) {
            for (Vertex srcVtx : vtxList) {
                for (Vertex destVtx : vtxList) {
                    // Case 1: the internal nodes in P(k, s, d) does NOT contain node-k
                    int pathLengthWithoutKthVtx = floydWarshallSubproblems[k - 1][srcVtx.id()][destVtx.id()];
                    // Case 2: the internal nodes in P(k, s, d) contains node-k
                    // By dividing P(k, s, d) into two subpaths by node-k, we have
                    // P(k, s, d) = P1(k - 1, s, k) + P2(k - 1, k, d)
                    int kthVtxID = k - 1;
                    int pathLengthWithKthVtx = floydWarshallSubproblems[k - 1][srcVtx.id()][kthVtxID]
                            + floydWarshallSubproblems[k - 1][kthVtxID][destVtx.id()];
                    // P(k, s, d) is the smaller between the above two candidates.
                    floydWarshallSubproblems[k][srcVtx.id()][destVtx.id()] = Math.min(pathLengthWithoutKthVtx,
                            pathLengthWithKthVtx);
                }
            }
        }
        /*
         * Extension to detect negative cycles:
         * If the input graph has negative cycles, then constructing the recurrence will lead to at least one v in
         * A[n, v, v] (the diagonal of the final solution), s.t. A[n, v, v] < 0. Thus, we just need to check A[n, v, v]
         * (the diagonal of the final solution), and see if there is a negative value.
         */
        for (Vertex vtx : vtxList) {
            if (floydWarshallSubproblems[n][vtx.id()][vtx.id()] < 0) {
                throw new IllegalArgumentException("The graph has negative cycles.");
            }
        }
        // The final solution lies in exactly subproblems[n][s][d].
        return floydWarshallSubproblems[n];
        // Overall running time complexity: O(n^3)
        // Overall space complexity: O(n^3)
    }

    @Override
    public int[][] floydWarshallApspOptimized() {
        int n = vtxList.size();
        // Initialization
        // Space optimization: We only keep track of the subproblem solutions in the previous out-most iteration.
        int[][] prevIterDp = new int[n][n], currIterDp = new int[n][n];
        for (Vertex srcVtx : vtxList) {
            for (Vertex destVtx : vtxList) {
                if (srcVtx != destVtx) {
                    prevIterDp[srcVtx.id()][destVtx.id()] = INFINITY;
                }
            }
        }
        for (UndirectedEdge edge : edgeList) {
            prevIterDp[edge.end1().id()][edge.end2().id()] = edge.length();
            prevIterDp[edge.end2().id()][edge.end1().id()] = edge.length();
        }
        // Bottom-up calculation
        for (int k = 1; k <= n; ++k) {
            for (Vertex srcVtx : vtxList) {
                for (Vertex destVtx : vtxList) {
                    int pathLengthWithoutKthVtx = prevIterDp[srcVtx.id()][destVtx.id()];
                    int kthVtxID = k - 1;
                    int pathLengthWithKthVtx = prevIterDp[srcVtx.id()][kthVtxID] + prevIterDp[kthVtxID][destVtx.id()];
                    currIterDp[srcVtx.id()][destVtx.id()] = Math.min(pathLengthWithoutKthVtx, pathLengthWithKthVtx);
                }
            }
            System.arraycopy(currIterDp, 0, prevIterDp, 0, n);
        }
        for (Vertex vtx : vtxList) {
            if (prevIterDp[vtx.id()][vtx.id()] < 0) {
                throw new IllegalArgumentException("The graph has negative cycles.");
            }
        }
        // The final solution lies in exactly prevIterDp.
        return prevIterDp;
        // Overall running time complexity: O(n^3)
        // Overall space complexity: O(n^2)
    }

}
