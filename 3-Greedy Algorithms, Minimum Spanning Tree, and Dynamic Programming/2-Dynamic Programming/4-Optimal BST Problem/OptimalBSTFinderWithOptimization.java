/**
 * Same as the optimal BST problem.
 *
 * Algorithm: Similar to the ordinary optimal BST problem, but with Knuth's
 * optimization.
 * The key point is the monotony of the roots:
 * root(i, j-1) <= root(i, j) <= root(i+1, j)
 * [Proof ?????]
 * Thus, by keeping track of the root, we can significantly reduce the number
 * of brute-force searches when searching for the root of a subproblem.
 */

public class OptimalBSTFinderWithOptimization {

    /**
     * Default value for subproblem solutions.
     */
    private static final int DEFAULT_SUBPROBLEM_SOL = 0;

    /**
     * Roots of the subproblems.
     */
    private int[][] roots;
    /**
     * Subproblem solutions.
     * Since there are only O(n^2) subproblems, the first time we solve a
     * subproblem, we can cache its solution in a global take for O(1) lookup
     * time later on.
     */
    private final int[][] subproblems;

    /**
     * Finds the optimal BST for items with the given weight distribution, and
     * calculates its weighted search time (WST), in a straightforward way.
     * @param weights weight distribution of the items
     * @return optimal WST
     */
    public int findOptimalBSTStraightforward(int[] weights) {
        // Check whether the input array is null or empty
        if ((weights == null) || (weights.length == 0)) {
            throw new IllegalArgumentException("The input weight distribution should not be null or empty.");
        }

        int n = weights.length;
        initializeSubproblemSols(n);
        return findOptimalBSTHelper(weights, 0, n - 1);
        // With memoization, the overall running time complexity is O(n^3).
    }

    /**
     * Private helper method to initialize the subproblem solutions.
     * @param n number of items
     */
    private void initializeSubproblemSols(int n) {
        subproblems = new int[n][n];
        for (int i = 0; i < n; ++i) {
            for (int j = 0; j < n; ++j) {
                subproblems[i][j] = DEFAULT_SUBPROBLEM_SOL;
            }
        }
        // Running time complexity: O(n^2)
    }

    /**
     * Private helper method to find the optimal BST for the given contiguous
     * items with the given weight distribution, and calculates its WST,
     * recursively.
     * @param weights weights of the items
     * @param start starting item of the contiguous items
     * @param end ending item of the contiguous items
     * @return optimal WST of the given continuous items
     */
    private int findOptimalBSTHelper(int[] weights, int start, int end) {
        // Base case 1: start > end
        if (start > end) {
            return 0;
        }
        // Recursive case
        if (subproblems[start][end] != DEFAULT_SUBPROBLEM_SOL) {
            return subproblems[start][end];
        }
        int minWST = Integer.MAX_VALUE;
        int weightSum = 0;
        for (int k = start; k <= end; ++k) {
            weightSum += weights[k];
        }
        // Use Knuth's optimization:
        // root(i, j-1) <= root(i, j) <= root(i+1, j)
        for (int r = start; r <= end; ++r) {
            int currWST = findOptimalBSTHelper(weights, start, r - 1) + findOptimalBSTHelper(weights, r + 1, end)
                    + weightSum;
            if (currWST < minWST) {
                minWST  = currWST;
            }
        }
        subproblems[start][end] = minWST;
        return minWST;
    }

    /**
     * Finds the optimal BST for items with the given weight distribution, and
     * calculates its weighted search time (WST), in an improved bottom-up way.
     * @param weights weight distribution of the items
     * @return optimal WST
     */
    public int findOptimalBST(int[] weights) {
        // Check whether the input array is null or empty
        if ((weights == null) || (weights.length == 0)) {
            throw new IllegalArgumentException("The input weight distribution should not be null or empty.");
        }

        int n = weights.length;
        // Initialization
        roots = new int[n][n];
        subproblems = new int[n][n];
        int nItem = 1;
        for (int i = 0; i < n; ++i) {
            roots[i][i] = i;
            subproblems[i][i] = weights[i];
        }
        // Bottom-up calculation
        for (nItem = 2; nItem <= n; ++nItem) {
            for (int start = 0; start < (n - nItem + 1); ++start) {
                int end = Math.min(start + nItem - 1, n - 1);
                int rootWithMinWST = -1, minWST = Integer.MAX_VALUE;
                int weightSum = 0;
                for (int k = start; k <= end; ++k) {
                    weightSum += weights[k];
                }
                // Use Knuth optimization:
                // root(i, j-1) <= root(i, j) <= root(i+1, j)
                for (int r = roots[start][end - 1]; r <= roots[start + 1][end]; ++r) {
                    int leftSubtreeWST = 0, rightSubtreeWST = 0;
                    if (start <= (r - 1)) {
                        leftSubtreeWST = subproblems[start][r - 1];
                    }
                    if ((r + 1) <= end) {
                        rightSubtreeWST = subproblems[r + 1][end];
                    }
                    int currWST = leftSubtreeWST + rightSubtreeWST + weightSum;
                    if (currWST < minWST) {
                        rootWithMinWST = r;
                        minWST = currWST;
                    }
                }
                roots[start][end] = rootWithMinWST;
                subproblems[start][end] = minWST;
            }
        }
        return subproblems[0][n - 1];
        // Overall running time complexity: O(n^2) [Analysis ?????]
    }

}
