package directed_graph;

import java.util.ArrayList;
import java.util.List;

import graph.GraphInterface;

/**
 * Adjacency list representation of a directed graph.
 *
 * Note that parallel edges and self-loops are not allowed.
 * @author Ziang Lu
 */
public class DirectedGraph implements GraphInterface {

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
    private final List<DirectedEdge> edgeList;
    /**
     * Floyd-Warshall subproblem solutions.
     * Since there are only O(n^3) subproblems, the first time we solve a
     * subproblem, we can cache its solution in a global take for O(1) lookup
     * time later on.
     */
    private int[][][] floydWarshallSubproblems;

    /**
     * Default constructor.
     */
    public DirectedGraph() {
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
        List<DirectedEdge> edgesToRemove = new ArrayList<>();
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
    public void addEdge(int tailID, int headID, int length) {
        // Check whether the input endpoints both exist
        Vertex tail = findVtx(tailID), head = findVtx(headID);
        if ((tail == null) || (head == null)) {
            throw new IllegalArgumentException("The endpoints don't both exist.");
        }
        // Check whether the input endpoints both exist
        if (tailID == headID) {
            throw new IllegalArgumentException("The endpoints are the same (self-loop).");
        }

        DirectedEdge newEdge = new DirectedEdge(tail, head, length);
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
        for (DirectedEdge edge : edgeList) {
            floydWarshallSubproblems[0][edge.tail().id()][edge.head().id()] = edge.length();
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
        int[][] prevIterSubproblems = new int[n][n], currIterSubproblems = new int[n][n];
        for (Vertex srcVtx : vtxList) {
            for (Vertex destVtx : vtxList) {
                if (srcVtx != destVtx) {
                    prevIterSubproblems[srcVtx.id()][destVtx.id()] = INFINITY;
                }
            }
        }
        for (DirectedEdge edge : edgeList) {
            prevIterSubproblems[edge.tail().id()][edge.head().id()] = edge.length();
        }
        // Bottom-up calculation
        for (int k = 1; k <= n; ++k) {
            for (Vertex srcVtx : vtxList) {
                for (Vertex destVtx : vtxList) {
                    int pathLengthWithoutKthVtx = prevIterSubproblems[srcVtx.id()][destVtx.id()];
                    int kthVtxID = k - 1;
                    int pathLengthWithKthVtx = prevIterSubproblems[srcVtx.id()][kthVtxID]
                            + prevIterSubproblems[kthVtxID][destVtx.id()];
                    currIterSubproblems[srcVtx.id()][destVtx.id()] = Math.min(pathLengthWithoutKthVtx,
                            pathLengthWithKthVtx);
                }
            }
            System.arraycopy(currIterSubproblems, 0, prevIterSubproblems, 0, n);
        }
        for (Vertex vtx : vtxList) {
            if (prevIterSubproblems[vtx.id()][vtx.id()] < 0) {
                throw new IllegalArgumentException("The graph has negative cycles.");
            }
        }
        // The final solution lies in exactly prevIterSubproblems.
        return prevIterSubproblems;
        // Overall running time complexity: O(n^3)
        // Overall space complexity: O(n^2)
    }

}
