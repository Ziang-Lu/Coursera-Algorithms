/**
 * Given n items, each item with a non-negative value v and a non-negative
 * integral size (weight) w, and a non-negative and integral capacity W, select
 * a subset of the items, that maximize sum(v), subject to sum(w) <= W.
 *
 *
 * Algorithm: (Dynamic programming)
 * Denote S to be the optimal solution, i.e., and item-n to be the last item.
 * Consider whether item-n is in S:
 * 1. item-n is NOT in S:
 *    => S must be optimal among only the first (n - 1) items and capacity W.
 *       (Proof:
 *       Assume S* is the optimal solution among the first (n - 1) items and
 *       capacity W (S* > S), then among n items and capacity W, we still have
 *       S* > S, so S is not the optimal solution among n items and capacity W.
 *       [CONTRADICTION])
 *    => S = the optimal solution among the first (n - 1) items and capacity W
 * 2. item-n is in S:
 *    => {S - item-n} must be optimal among only the first (n - 1) items and
 *       the residual capacity (W - w_n) (i.e., the space is "reserved" for
 *       item-n).
 *       (Proof:
 *       Assume S* is the optimal solution among the first (n - 1) items and
 *       the residual capacity (W - w_n) (S* > S - item-n), then among n items
 *       and capacity W, we have S* + item-n > S, so S is not the optimal
 *       solution among n items and capacity W. [CONTRADICTION])
 *    => S = the optimal solution among the first (n - 1) items and the residual
 *           capacity (W - w_n) + item-n
 *
 * i.e.,
 * Let S(i, x) be the optimal solution for the subproblem among the first i
 * items and capacity x, then
 * S(i, x) = max(S(i - 1, x), S(i - 1, x - w_i) + item-i)
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
    private int[][] subproblemSols; 

    /**
     * Solves the knapsack problem of the items with the given values and
     * weights, and the given capacity.
     * @param vals values of the items
     * @param weights weights of the items
     * @param capacity capacity of the knapsack
     * @return included items
     */
    public HashSet<Integer> knapsackStraightforward(int[] vals, int[] weights, int capacity) {
        // Check whether the input arrays are null or empty
        if ((vals == null) || (vals.length == 0) || (weights == null) || (weights.length == 0)) {
            throw new IllegalArgumentException("The input values and weights should not be null or empty.");
        }
        // Check whether the input capacity is non-negative
        if (capacity < 0) {
            throw new IllegalArgumentException("The input capacity should be non-negative.");
        }

        int n = vals.length;
        initializeSubproblemSols(n, capacity);
        knapsackHelper(vals, weights, n - 1, capacity);
        return reconstruct(vals, weights, capacity);
        // Overall running time complexity: O(nW)
    }

    /**
     * Private helper method to initialize the subproblem solutions.
     * @param n number of items
     * @param capacity capacity of the knapsack
     */
    private void initializeSubproblemSols(int n, int capacity) {
        subproblemSols = new int[n][capacity + 1];
        for (int i = 0; i < n; ++i) {
            for (int j = 0; j <= capacity; ++j) {
                subproblemSols[i][j] = DEFAULT_SUBPROBLEM_SOL;
            }
        }
        // Running time complexity: O(nW)
    }

    /**
     * Private helper method to solve the knapsack subproblem with the given
     * first vertices and the given capacity recursively.
     * @param vals values of the items
     * @param weights weights of the items
     * @param lastItem last item of the subproblem
     * @param currCapacity capacity of the subproblem
     */
    private void knapsackHelper(int[] vals, int[] weights, int lastItem, int currCapacity) {
        if (subproblemSols[lastItem][currCapacity] != DEFAULT_SUBPROBLEM_SOL) {
            return;
        }

        // Base case 1: Only the first item
        if (lastItem == 0) {
            if (weights[0] > currCapacity) {
                subproblemSols[0][currCapacity] = 0;
            } else {
                subproblemSols[0][currCapacity] = vals[0];
            }
            return;
        }
        // Base case 2: No capacity available
        if (currCapacity == 0) {
            subproblemSols[lastItem][0] = 0;
            return;
        }

        // Recursive case
        if (weights[lastItem] > currCapacity) {
            knapsackHelper(vals, weights, lastItem - 1, currCapacity);
            subproblemSols[lastItem][currCapacity] = subproblemSols[lastItem - 1][currCapacity];
        } else {
            knapsackHelper(vals, weights, lastItem - 1, currCapacity);
            int resultWithoutLast = subproblemSols[lastItem - 1][currCapacity];
            knapsackHelper(vals, weights, lastItem - 1, currCapacity - weights[lastItem]);
            int resultWithLast = subproblemSols[lastItem  - 1][currCapacity - weights[lastItem]] + vals[lastItem];
            subproblemSols[lastItem][currCapacity] = Math.max(resultWithoutLast, resultWithLast);
        }
    }

    /**
     * Private helper method to reconstruct the included items according to the
     * optimal solution using backtracking.
     * @param vals values of the items
     * @param weights weights of the items
     * @param capacity capacity of the knapsack
     * @return included items
     */
    private HashSet<Integer> reconstruct(int[] vals, int[] weights, int capacity) {
        HashSet<Integer> includedItems = new HashSet<Integer>();
        int currItem = vals.length - 1, currCapacity = capacity;
        while (currItem >= 1) {
            if ((weights[currItem] <= currCapacity) &&
                    (subproblemSols[currItem - 1][currCapacity] <
                            (subproblemSols[currItem - 1][currCapacity - weights[currItem]] + vals[currItem]))) {
                // Case 2: The current item is included.
                includedItems.add(currItem);
                currCapacity -= weights[currItem];
            }
            --currItem;
        }
        if (weights[0] <= currCapacity) {
            includedItems.add(0);
        }
        return includedItems;
        // Running time complexity: O(n)
    }

    public static void main(String[] args) {
        Knapsack knapsackSolver = new Knapsack();
        System.out
                .println(knapsackSolver.knapsackStraightforward(new int[] { 3, 2, 4, 4 }, new int[] { 4, 3, 2, 3 }, 6));
    }

}
