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
 * S(i, x1, x2) = max{S(i - 1, x1, x2),
 *                    S(i - 1, x1 - w_i, x2) + v_i,
 *                    S(i - 1, x1, x2 - w_i) + v_i}
 */

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TwoKnapsack {

    /**
     * Subproblem solutions.
     * Since there are only O(n*W1*W2) distinct subproblems, the first time we
     * solve a subproblem, we can cache its solution in a global take for O(1)
     * lookup time later on.
     */
    private double[][][] subproblems;

    /**
     * Solves the two-knapsack problem of the items with the given values and
     * weights, and the given capacities, in an improved bottom-up way.
     * @param vals values of the items
     * @param weights weights of the items
     * @param cap1 capacity of knapsack-1
     * @param cap2 capacity of knapsack-2
     * @return included items in knapsack-1 and knapsack-2
     */
    public List<Set<Integer>> twoKnapsack(double[] vals, int[] weights, int cap1, int cap2) {
        // Check whether the input arrays are null or empty
        if ((vals == null) || (vals.length == 0)) {
            return new ArrayList<>();
        }
        // Check whether the input capacities are non-negative
        if ((cap1 < 0) || (cap2 < 0)) {
            return new ArrayList<>();
        }

        int n = vals.length;
        // Initialization
        subproblems = new double[n][cap1 + 1][cap2 + 1];
        for (int x1 = 0; x1 <= cap1; ++x1) {
            for (int x2 = 0; x2 <= cap2; ++x2) {
                if ((weights[0] <= x1) || (weights[0] <= x2)) {
                    subproblems[0][x1][x2] = vals[0];
                }
            }
        }
        // Bottom-up calculation
        for (int item = 1; item < n; ++item) {
            for (int x1 = 0; x1 <= cap1; ++x1) {
                for (int x2 = 0; x2 <= cap2; ++x2) {
                    double resultWithoutCurr = subproblems[item - 1][x1][x2];
                    if ((weights[item] <= x1) && (weights[item] <= x2)) {
                        double resultWithCurrIn1 = subproblems[item - 1][x1 - weights[item]][x2] + vals[item];
                        double resultWithCurrIn2 = subproblems[item - 1][x1][x2 - weights[item]] + vals[item];
                        subproblems[item][x1][x2] = Math.max(Math.max(resultWithoutCurr, resultWithCurrIn1),
                                resultWithCurrIn2);
                    } else if (weights[item] <= x1) {
                        double resultWithCurrIn1 = subproblems[item - 1][x1 - weights[item]][x2] + vals[item];
                        subproblems[item][x1][x2] = Math.max(resultWithoutCurr, resultWithCurrIn1);
                    } else if (weights[item] <= x2) {
                        double resultWithCurrIn2 = subproblems[item - 1][x1][x2 - weights[item]] + vals[item];
                        subproblems[item][x1][x2] = Math.max(resultWithoutCurr, resultWithCurrIn2);
                    }
                }
            }
        }
        return reconstruct(vals, weights, cap1, cap2);
        // Overall running time complexity: O(n*W1*W2), where W1 and W2 are the knapsack capacities
    }

    /**
     * Private helper method to reconstruct the included items in knapsack-1 and
     * knapsack-2 according to the optimal solution using backtracking.
     * @param vals values of the items
     * @param weights weights of the items
     * @param cap1 capacity of knapsack-1
     * @param cap2 capacity of knapsack-2
     * @return included items in knapsack-1 and knapsack-2
     */
    private List<Set<Integer>> reconstruct(double[] vals, int[] weights, int cap1, int cap2) {
        Set<Integer> included1 = new HashSet<>(), included2 = new HashSet<>();
        int item = vals.length - 1, currCap1 = cap1, currCap2 = cap2;
        while (item >= 1) {
            double resultWithoutCurr = subproblems[item - 1][currCap1][currCap2];
            if (weights[item] > currCap1) {
                double resultWithCurrIn2 = subproblems[item - 1][currCap1][currCap2 - weights[item]] + vals[item];
                if (resultWithoutCurr < resultWithCurrIn2) {
                    // Case 3: The current item is included in S2.
                    included2.add(item);
                }
            } else if (weights[item] > currCap2) {
                double resultWithCurrIn1 = subproblems[item - 1][currCap1 - weights[item]][currCap2] + vals[item];
                if (resultWithoutCurr < resultWithCurrIn1) {
                    // Case 2: The current item is included in S1.
                    included1.add(item);
                }
            } else {
                double resultWithCurrIn1 = subproblems[item - 1][currCap1 - weights[item]][currCap2] + vals[item];
                double resultWithCurrIn2 = subproblems[item - 1][currCap1][currCap2 - weights[item]] + vals[item];
                double result = Math.max(Math.max(resultWithoutCurr, resultWithCurrIn1), resultWithCurrIn2);
                if (result == resultWithoutCurr) {
                    // Case 1: The current item is not included.
                } else if (result == resultWithCurrIn1) {
                    // Case 2: The current item is included in S1.
                    included1.add(item);
                    currCap1 -= weights[item];
                } else if (result == resultWithCurrIn2) {
                    // Case 3: The current item is included in S2.
                    included2.add(item);
                    currCap2 -= weights[item];
                }
            }
            --item;
        }
        if (weights[0] <= currCap1) {
            included1.add(0);
        } else if (weights[0] <= currCap2) {
            included2.add(0);
        }
        List<Set<Integer>> knapsacks = new ArrayList<>();
        knapsacks.add(included1);
        knapsacks.add(included2);
        return knapsacks;
        // Running time complexity: O(n)
    }

}
