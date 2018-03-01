/**
 * Given items 1, 2, ..., n (assume in sorted order), with positive frequencies
 * p_1, p_2, ..., p_n, compute a valid BST that minimizes the weighted search
 * time (WST):
 * C(T) = sum(p_i * [search time for item-i])
 * where the search time for item-i is defined to be the number of nodes you
 * have to visit until you find it (inclusive).
 *
 * Optimal substructure lemma:
 * Suppose an optimal BST for items 1, 2, ..., n T HAS ROOT r, left subtree T_L
 * and right subtree T_R, then T_L is optimal for items 1, 2, ..., r-1 and T_R
 * is optimal for items r+1, r+2, ..., n
 * -Proof:
 * Suppose T_L is not optimal for items 1, 2, ..., r-1 with C(T_L*) < C(T_L).
 * (The other case is similar.)
 * Thus, we can obtain a T* from T by replacing the left subtree by T*.
 * =>
 * C(T) = sum(p_i * [search time for item-i in T])
 *      = C(T_L) + sum(p_i * 1) for 1<=i<=r-1 + p_r * 1 + C(T_R) + sum(p_i * 1)
 *        for r+1<=i<=n
 *      = C(T_L) + C(T_R) + sum(p_i) for 1<=i<=n
 *      < C(T_L*) + C(T_R) + const = C(T*)
 * Thus by proof by contradiction, T_L must be optimal for items 1, 2, ..., r-1.
 *
 * Algorithm: (Dynamic programming)
 * Suppose 1<=i<=j<=n,
 * C(T_{i,j}) = min_r (i<=r<=j) (C(T_L) + C(T_R) + sum(p_k) for i<=k<=j)
 */

public class OptimalBSTFinder {

    /**
     * Default value for subproblem solutions.
     */
    private static final int DEFAULT_SUBPROBLEM_SOL = 0;

    /**
     * Subproblem solutions.
     * Since there are only O(n^2) subproblems, the first time we solve a
     * subproblem, we can cache its solution in a global take for O(1) lookup
     * time later on.
     */
    private int[][] subproblems;

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
        // Base case
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
        subproblems = new int[n][n];
        int nItem = 1;
        for (int i = 0; i < n; ++i) {
            subproblems[i][i] = weights[i];
        }
        // Bottom-up calculation
        for (nItem = 2; nItem <= n; ++nItem) {
            for (int start = 0; start < (n - nItem + 1); ++start) {
                int end = Math.min(start + nItem - 1, n - 1);
                int minWST = Integer.MAX_VALUE;
                int weightSum = 0;
                for (int k = start; k <= end; ++k) {
                    weightSum += weights[k];
                }
                for (int r = start; r <= end; ++r) {
                    int leftSubtreeWST = 0, rightSubtreeWST = 0;
                    if (start <= (r - 1)) {
                        leftSubtreeWST = subproblems[start][r - 1];
                    }
                    if ((r + 1) <= end) {
                        rightSubtreeWST = subproblems[r + 1][end];
                    }
                    int currWST = leftSubtreeWST + rightSubtreeWST + weightSum;
                    if (currWST < minWST) {
                        minWST = currWST;
                    }
                }
                subproblems[start][end] = minWST;
            }
        }
        return subproblems[0][n - 1];
        // Overall running time complexity: O(n^3)
    }

}
