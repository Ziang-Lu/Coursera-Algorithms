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

public class OptimalBSTFinderOptimized {

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
    private int[][] subproblems;

    /**
     * Finds the optimal BST for items with the given weight distribution, and
     * calculates its weighted search time (WST), in an improved bottom-up way.
     * @param weights weight distribution of the items
     * @return optimal WST
     */
    public int findOptimalBST(int[] weights) {
        // Check whether the input array is null or empty
        if ((weights == null) || (weights.length == 0)) {
            return 0;
        }

        int n = weights.length;
        // Initialization
        roots = new int[n][n];
        subproblems = new int[n][n];
        for (int i = 0; i < n; ++i) {
            roots[i][i] = i;
            subproblems[i][i] = weights[i];
        }
        // Bottom-up calculation
        for (int nItem = 2; nItem <= n; ++nItem) {
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
