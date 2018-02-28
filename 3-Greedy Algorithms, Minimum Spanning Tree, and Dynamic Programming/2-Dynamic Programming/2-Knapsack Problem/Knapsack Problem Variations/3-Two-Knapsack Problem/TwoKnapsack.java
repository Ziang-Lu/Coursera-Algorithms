/**
 * Similar to the knapsack problem, but with two capacities W1 and W2, and to
 * maximize sum(v in 1) + sum(v in 2), subject to sum(w in 1) <= W1 and
 * sum(w in 2) <= W2.
 *
 * Algorithm: (Dynamic programming)
 * Denote S (S1 + S2) to be the optimal solution and item-n to be the last item.
 * Consider whether item-n is in S:
 * 1. item-n is NOT in S:
 *    => S must be optimal among only the first (n - 1) items and capacities W1
 *       and W2.
 *    => S = the optimal solution among only the first (n - 1) items and
 *           capacities W1 and W2
 * 2. item-n is in S1:
 *    => {S1 - item-n, S2} must be optimal among only the first (n - 1) items
 *       and capacities (W1 - w_n) and W2 (i.e., the space is "reserved" for
 *       item-n in W1).
 *    => S = the optimal solution among only the first (n - 1) items and
 *           capacities (W1 - w_n) and W2 + v_n in W1
 * 3. item-n is in S2:
 *    => {S1, S2 - item-n} must be optimal among only the first (n - 1) items
 *       and capacities W1 and (W2 - w_n) (i.e., the space is "reserved" for
 *       item-n in W2).
 *    => S = the optimal solution among only the first (n - 1) items and
 *           capacities W1 and (W2 - w_n) + v_n in W2
 *
 * i.e.,
 * Let S(i, x1, x2) be the optimal solution for the subproblem among the first i
 * items and capacities x1 and x2, then
 * S(i, x1, x2) = max(S(i - 1, x1, x2),
 *                    S(i - 1, x1 - w_i, x2) + v_i,
 *                    S(i - 1, x1, x2 - w_i) + v_i)
 */

import java.util.ArrayList;
import java.util.HashSet;

public class TwoKnapsack {

    /**
     * Default negative value for the subproblem solutions.
     */
    private static final int DEFAULT_SUBPROBLEM_SOL = -1;

    /**
     * Subproblem solutions.
     * Since there are only O(n*W1*W2) distinct subproblems, the first time we
     * solve a subproblem, we can cache its solution in a global take for O(1)
     * lookup time later on.
     */
    private int[][][] subproblemSols;

    /**
     * Solves the two-knapsack problem of the items with the given values and
     * weights, and the given capacities, in a straightforward way.
     * @param vals values of the items
     * @param weights weights of the items
     * @param capacity1 capacity of knapsack-1
     * @param capacity2 capacity of knapsack-2
     * @return included items in knapsack-1 and knapsack-2
     */
    public ArrayList<HashSet<Integer>> twoKnapsackStraightforward(int[] vals, int[] weights, int capacity1,
            int capacity2) {
        // Check whether the input arrays are null or empty
        if ((vals == null) || (vals.length == 0) || (weights == null) || (weights.length == 0)) {
            throw new IllegalArgumentException("The input values and weights should not be null or empty.");
        }
        // Check whether the input capacities are non-negative
        if ((capacity1 < 0) || (capacity2 < 0)) {
            throw new IllegalArgumentException("The input capacities should be non-negative.");
        }

        int n = vals.length;
        initializeSubproblemSols(n, capacity1, capacity2);
        twoKnapsackHelper(vals, weights, n - 1, capacity1, capacity2);
        return reconstruct(vals, weights, capacity1, capacity2);
        // With memoization, the overall running time complexity is O(n*W1*W2).
    }

    /**
     * Private helper method to initialize the subproblem solutions.
     * @param n number of items
     * @param capacity1 capacity of knapsack-1
     * @param capacity2 capacity of knapsack-2
     */
    private void initializeSubproblemSols(int n, int capacity1, int capacity2) {
        subproblemSols = new int[n][capacity1 + 1][capacity2 + 1];
        for (int i = 0; i < n; ++i) {
            for (int x1 = 0; x1 <= capacity1; ++x1) {
                for (int x2 = 0; x2 <= capacity2; ++x2) {
                    subproblemSols[i][x1][x2] = DEFAULT_SUBPROBLEM_SOL;
                }
            }
        }
        // Running time complexity: O(n*W1*W2)
    }

    /**
     * Private helper method to solve the two-knapsack subproblem with the given
     * first items and the given capacities recursively.
     * @param vals values of the items
     * @param weights weights of the items
     * @param lastItem last item of the subproblem
     * @param currCapacity1 capacity 1 of the subproblem
     * @param currCapacity2 capacity 2 of the subproblem
     */
    private void twoKnapsackHelper(int[] vals, int[] weights, int lastItem, int currCapacity1, int currCapacity2) {
        if (subproblemSols[lastItem][currCapacity1][currCapacity2] != DEFAULT_SUBPROBLEM_SOL) {
            return;
        }

        // Base case
        if (lastItem == 0) {
            if ((weights[0] > currCapacity1) && (weights[0] > currCapacity2)) {
                subproblemSols[0][currCapacity1][currCapacity2] = 0;
            } else {
                subproblemSols[0][currCapacity1][currCapacity2] = vals[0];
            }
            return;
        }
        // Recursive case
        twoKnapsacksHelper(vals, weights, lastItem - 1, currCapacity1, currCapacity2);
        int resultWithoutLast = subproblemSols[lastItem - 1][currCapacity1][currCapacity2];
        if (weights[lastItem] > currCapacity1) {
            twoKnapsacksHelper(vals, weights, lastItem - 1, currCapacity1, currCapacity2 - weights[lastItem]);
            int resultWithLastIn2 = subproblemSols[lastItem - 1][currCapacity1][currCapacity2 - weights[lastItem]]
                    + vals[lastItem];
            subproblemSols[lastItem][currCapacity1][currCapacity2] = Math.max(resultWithoutLast, resultWithLastIn2);
        } else if (weights[lastItem] > currCapacity2) {
            twoKnapsacksHelper(vals, weights, lastItem - 1, currCapacity1 - weights[lastItem], currCapacity2);
            int resultWithLastIn1 = subproblemSols[lastItem - 1][currCapacity1 - weights[lastItem]][currCapacity2]
                    + vals[lastItem];
            subproblemSols[lastItem][currCapacity1][currCapacity2] = Math.max(resultWithoutLast, resultWithLastIn1);
        } else {
            twoKnapsacksHelper(vals, weights, lastItem - 1, currCapacity1 - weights[lastItem], currCapacity2);
            int resultWithLastIn1 = subproblemSols[lastItem - 1][currCapacity1 - weights[lastItem]][currCapacity2]
                    + vals[lastItem];
            twoKnapsacksHelper(vals, weights, lastItem - 1, currCapacity1, currCapacity2 - weights[lastItem]);
            int resultWithLastIn2 = subproblemSols[lastItem - 1][currCapacity1][currCapacity2 - weights[lastItem]]
                    + vals[lastItem];
            subproblemSols[lastItem][currCapacity1][currCapacity2] = Math
                    .max(Math.max(resultWithoutLast, resultWithLastIn1), resultWithLastIn2);
        }
    }

    /**
     * Private helper method to reconstruct the included items in knapsack-1 and
     * knapsack-2 according to the optimal solution using backtracking.
     * @param vals values of the items
     * @param weights weights of the items
     * @param capacity1 capacity of knapsack-1
     * @param capacity2 capacity of knapsack-2
     * @return included items in knapsack-1 and knapsack-2
     */
    private ArrayList<HashSet<Integer>> reconstruct(int[] vals, int[] weights, int capacity1, int capacity2) {
        HashSet<Integer> includedItems1 = new HashSet<Integer>(), includedItems2 = new HashSet<Integer>();
        int currItem = vals.length - 1, currCapacity1 = capacity1, currCapacity2 = capacity2;
        while (currItem >= 1) {
            int resultWithoutCurr = subproblemSols[currItem - 1][currCapacity1][currCapacity2];
            if (weights[currItem] > currCapacity1) {
                int resultWithCurrIn2 = subproblemSols[currItem - 1][currCapacity1][currCapacity2 - weights[currItem]]
                        + vals[currItem];
                if (resultWithoutCurr < resultWithCurrIn2) {
                    // Case 3: The current item is included in S2.
                    includedItems2.add(currItem);
                }
            } else if (weights[currItem] > currCapacity2) {
                int resultWithCurrIn1 = subproblemSols[currItem - 1][currCapacity1 - weights[currItem]][currCapacity2]
                        + vals[currItem];
                if (resultWithoutCurr < resultWithCurrIn1) {
                    // Case 2: The current item is included in S1.
                    includedItems1.add(currItem);
                }
            } else {
                int resultWithCurrIn1 = subproblemSols[currItem - 1][currCapacity1 - weights[currItem]][currCapacity2]
                        + vals[currItem];
                int resultWithCurrIn2 = subproblemSols[currItem - 1][currCapacity1][currCapacity2 - weights[currItem]]
                        + vals[currItem];
                int result = Math.max(Math.max(resultWithoutCurr, resultWithCurrIn1), resultWithCurrIn2);
                if (result == resultWithoutCurr) {
                    // Case 1: The current item is not included.
                } else if (result == resultWithCurrIn1) {
                    // Case 2: The current item is included in S1.
                    includedItems1.add(currItem);
                    currCapacity1 -= weights[currItem];
                } else if (result == resultWithCurrIn2) {
                    // Case 3: The current item is included in S2.
                    includedItems2.add(currItem);
                    currCapacity2 -= weights[currItem];
                }
            }
            --currItem;
        }
        if (weights[0] <= currCapacity1) {
            includedItems1.add(0);
        } else if (weights[0] <= currCapacity2) {
            includedItems2.add(0);
        }
        ArrayList<HashSet<Integer>> knapsacks = new ArrayList<HashSet<Integer>>();
        knapsacks.add(includedItems1);
        knapsacks.add(includedItems2);
        return knapsacks;
        // Running time complexity: O(n)
    }

    /**
     * Solves the two-knapsack problem of the items with the given values and
     * weights, and the given capacities, in an improved bottom-up way.
     * @param vals values of the items
     * @param weights weights of the items
     * @param capacity1 capacity of knapsack-1
     * @param capacity2 capacity of knapsack-2
     * @return included items in knapsack-1 and knapsack-2
     */
    public ArrayList<HashSet<Integer>> twoKnapsack(int[] vals, int[] weights, int capacity1, int capacity2) {
        // Check whether the input arrays are null or empty
        if ((vals == null) || (vals.length == 0) || (weights == null) || (weights.length == 0)) {
            throw new IllegalArgumentException("The input values and weights should not be null or empty.");
        }
        // Check whether the input capacities are non-negative
        if ((capacity1 < 0) || (capacity2 < 0)) {
            throw new IllegalArgumentException("The input capacities should be non-negative.");
        }

        int n = vals.length;
        // Initialization
        subproblemSols = new int[n][capacity1 + 1][capacity2 + 1];
        for (int x1 = 0; x1 <= capacity1; ++x1) {
            for (int x2 = 0; x2 <= capacity2; ++x2) {
                if ((weights[0] > x1) && (weights[0] > x2)) {
                    subproblemSols[0][x1][x2] = 0;
                } else {
                    subproblemSols[0][x1][x2] = vals[0];
                }
            }
        }
        // Bottom-up calculation
        for (int item = 1; item < n; ++item) {
            for (int x1 = 0; x1 <= capacity1; ++x1) {
                for (int x2 = 0; x2 <= capacity2; ++x2) {
                    int resultWithoutCurr = subproblemSols[item - 1][x1][x2];
                    if ((weights[item] <= x1) && (weights[item] <= x2)) {
                        int resultWithCurrIn1 = subproblemSols[item - 1][x1 - weights[item]][x2] + vals[item];
                        int resultWithCurrIn2 = subproblemSols[item - 1][x1][x2 - weights[item]] + vals[item];
                        subproblemSols[item][x1][x2] = Math.max(Math.max(resultWithoutCurr, resultWithCurrIn1),
                                resultWithCurrIn2);
                    } else if (weights[item] <= x1) {
                        int resultWithCurrIn1 = subproblemSols[item - 1][x1 - weights[item]][x2] + vals[item];
                        subproblemSols[item][x1][x2] = Math.max(resultWithoutCurr, resultWithCurrIn1);
                    } else if (weights[item] <= x2) {
                        int resultWithCurrIn2 = subproblemSols[item - 1][x1][x2 - weights[item]] + vals[item];
                        subproblemSols[item][x1][x2] = Math.max(resultWithoutCurr, resultWithCurrIn2);
                    }
                }
            }
        }
        return reconstruct(vals, weights, capacity1, capacity2);
    }

}
