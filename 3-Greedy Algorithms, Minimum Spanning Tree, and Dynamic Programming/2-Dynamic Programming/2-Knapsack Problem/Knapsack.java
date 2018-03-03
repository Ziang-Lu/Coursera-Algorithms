/**
 * Given n items, each item with a non-negative value v and a non-negative
 * integral size (weight) w, and a non-negative integral capacity W, select a
 * subset of the items, that maximizes sum(v), subject to sum(w) <= W.
 *
 * Algorithm: (Dynamic programming)
 * Denote S to be the optimal solution and item-n to be the last item.
 * Consider whether item-n is in S:
 * 1. item-n is NOT in S:
 *    => S must be optimal among only the first (n - 1) items and capacity W.
 *    => S = the optimal solution among the first (n - 1) items and capacity W
 * 2. item-n is in S:
 *    => {S - item-n} must be optimal among only the first (n - 1) items and the
 *       residual capacity (W - w_n) (i.e., the space is "reserved" for item-n).
 *    => S = the optimal solution among the first (n - 1) items and the residual
 *           capacity (W - w_n) + item-n
 *
 * i.e.,
 * Let S(i, x) be the optimal solution for the subproblem among the first i
 * items and capacity x, then
 * S(i, x) = max(S(i - 1, x), S(i - 1, x - w_i) + v_i)
 */

import java.util.HashSet;

public class Knapsack {

    /**
     * Default negative value for subproblem solutions.
     */
    private static final int DEFAULT_SUBPROBLEM_SOL = -1;

    /**
     * Subproblem solutions.
     * Since there are only O(nW) distinct subproblems, the first time we solve
     * a subproblem, we can cache its solution in a global take for O(1) lookup
     * time later on.
     */
    private int[][] subproblems;

    /**
     * Solves the knapsack problem of the items with the given values and
     * weights, and the given capacity, in a straightforward way.
     * @param vals values of the items
     * @param weights weights of the items
     * @param cap capacity of the knapsack
     * @return included items
     */
    public HashSet<Integer> knapsackStraightforward(int[] vals, int[] weights, int cap) {
        // Check whether the input arrays are null or empty
        if ((vals == null) || (vals.length == 0) || (weights == null) || (weights.length == 0)) {
            throw new IllegalArgumentException("The input values and weights should not be null or empty.");
        }
        // Check whether the input capacity is non-negative
        if (cap < 0) {
            throw new IllegalArgumentException("The input capacity should be non-negative.");
        }

        int n = vals.length;
        initializeSubproblemSols(n, cap);
        knapsackHelper(vals, weights, n - 1, cap);
        return reconstruct(vals, weights, cap);
        // With memoization, the overall running time complexity is O(nW), where W is the knapsack capacity.
    }

    /**
     * Private helper method to initialize the subproblem solutions.
     * @param n number of items
     * @param cap capacity of the knapsack
     */
    private void initializeSubproblemSols(int n, int cap) {
        subproblems = new int[n][cap + 1];
        for (int i = 0; i < n; ++i) {
            for (int x = 0; x <= cap; ++x) {
                subproblems[i][x] = DEFAULT_SUBPROBLEM_SOL;
            }
        }
        // Running time complexity: O(nW), where W is the knapsack capacity
    }

    /**
     * Private helper method to solve the knapsack subproblem with the given
     * first items and the given capacity recursively.
     * @param vals values of the items
     * @param weights weights of the items
     * @param lastItem last item of the subproblem
     * @param currCap capacity of the subproblem
     */
    private void knapsackHelper(int[] vals, int[] weights, int lastItem, int currCap) {
        if (subproblems[lastItem][currCap] != DEFAULT_SUBPROBLEM_SOL) {
            return;
        }

        // Base case
        if (lastItem == 0) {
            if (weights[0] <= currCap) {
                subproblems[0][currCap] = vals[0];
            }
            return;
        }
        // Recursive case
        if (weights[lastItem] > currCap) {
            knapsackHelper(vals, weights, lastItem - 1, currCap);
            subproblems[lastItem][currCap] = subproblems[lastItem - 1][currCap];
        } else {
            knapsackHelper(vals, weights, lastItem - 1, currCap);
            int resultWithoutLast = subproblems[lastItem - 1][currCap];
            knapsackHelper(vals, weights, lastItem - 1, currCap - weights[lastItem]);
            int resultWithLast = subproblems[lastItem  - 1][currCap - weights[lastItem]] + vals[lastItem];
            subproblems[lastItem][currCap] = Math.max(resultWithoutLast, resultWithLast);
        }
    }

    /**
     * Private helper method to reconstruct the included items according to the
     * optimal solution using backtracking.
     * @param vals values of the items
     * @param weights weights of the items
     * @param cap capacity of the knapsack
     * @return included items
     */
    private HashSet<Integer> reconstruct(int[] vals, int[] weights, int cap) {
        HashSet<Integer> includedItems = new HashSet<Integer>();
        int currItem = vals.length - 1, currCap = cap;
        while (currItem >= 1) {
            if ((weights[currItem] <= currCap)
                    && (subproblems[currItem - 1][currCap] < (subproblems[currItem - 1][currCap - weights[currItem]]
                            + vals[currItem]))) {
                // Case 2: The current item is included.
                includedItems.add(currItem);
                currCap -= weights[currItem];
            }
            --currItem;
        }
        if (weights[0] <= currCap) {
            includedItems.add(0);
        }
        return includedItems;
        // Running time complexity: O(n)
    }

    /**
     * Solves the knapsack problem of the items with the given values and
     * weights, and the given capacity, in an improved bottom-up way.
     * @param vals values of the items
     * @param weights weights of the items
     * @param cap capacity of the knapsack
     * @return included items
     */
    public HashSet<Integer> knapsack(int[] vals, int[] weights, int cap) {
        // Check whether the input arrays are null or empty
        if ((vals == null) || (vals.length == 0) || (weights == null) || (weights.length == 0)) {
            throw new IllegalArgumentException("The input values and weights should not be null or empty.");
        }
        // Check whether the input capacity is non-negative
        if (cap < 0) {
            throw new IllegalArgumentException("The input capacity should be non-negative.");
        }

        int n = vals.length;
        // Initialization
        subproblems = new int[n][cap + 1];
        for (int x = 0; x <= cap; ++x) {
            if (weights[0] <= x) {
                subproblems[0][x] = vals[0];
            }
        }
        // Bottom-up calculation
        for (int item = 1; item < n; ++item) {
            for (int x = 0; x <= cap; ++x) {
                if (weights[item] > x) {
                    subproblems[item][x] = subproblems[item - 1][x];
                } else {
                    subproblems[item][x] = Math.max(subproblems[item - 1][x],
                            subproblems[item - 1][x - weights[item]] + vals[item]);
                }
            }
        }
        return reconstruct(vals, weights, cap);
        // Overall running time complexity: O(nW), where W is the knapsack capacity
    }

}
