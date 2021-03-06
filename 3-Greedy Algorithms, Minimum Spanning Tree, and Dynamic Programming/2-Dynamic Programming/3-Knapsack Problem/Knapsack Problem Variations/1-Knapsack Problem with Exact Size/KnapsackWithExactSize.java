/**
 * Similar to the knapsack problem, but need to subject to sum(w) = W.
 *
 * Algorithm: Same as the ordinary knapsack problem.
 */

import java.util.HashSet;
import java.util.Set;

public class KnapsackWithExactSize {

    /**
     * Subproblem solutions.
     * Since there are only O(nW) distinct subproblems, the first time we solve
     * a subproblem, we can cache its solution in a global take for O(1) lookup
     * time later on.
     */
    private double[][] subproblems;

    /**
     * Solves the knapsack problem (with exact size) of the items with the given
     * values and weights, and the given capacity, in an improved bottom-up way.
     * @param vals values of the items
     * @param weights weights of the items
     * @param cap capacity of the knapsack
     * @return included items
     */
    public Set<Integer> knapsackWithExactSize(double[] vals, int[] weights, int cap) {
        // Check whether the input arrays are null or empty
        if ((vals == null) || (vals.length == 0)) {
            return new HashSet<>();
        }
        // Check whether the input capacity is non-negative
        if (cap < 0) {
            return new HashSet<>();
        }

        int n = vals.length;
        // Initialization
        subproblems = new double[n][cap + 1];
        for (int x = 0; x <= cap; ++x) {
            if (weights[0] == x) {
                subproblems[0][x] = vals[0];
            }
        }
        // Bottom-up calculation
        for (int item = 1; item < n; ++item) {
            for (int x = 0; x <= cap; ++x) {
                if (weights[item] > x) {
                    subproblems[item][x] = subproblems[item - 1][x];
                } else {
                    // Note that if subproblem has no solution, the current item should not be included either.
                    double resultWithCurr = 0;
                    if (subproblems[item - 1][x - weights[item]] != 0) {
                        resultWithCurr = subproblems[item - 1][x - weights[item]] + vals[item];
                    }
                    subproblems[item][x] = Math.max(subproblems[item - 1][x], resultWithCurr);
                }
            }
        }
        return reconstruct(vals, weights, cap);
        // Overall running time complexity: O(nW), where W is the knapsack capacity
    }

    /**
     * Private helper method to reconstruct the included items according to the
     * optimal solution using backtracking.
     * @param vals values of the items
     * @param weights weights of the items
     * @param cap capacity of the knapsack
     * @return included items
     */
    private Set<Integer> reconstruct(double[] vals, int[] weights, int cap) {
        Set<Integer> included = new HashSet<>();
        int item = vals.length - 1, currCap = cap;
        while (item >= 1) {
            while (item >= 1) {
                if ((weights[item] <= currCap) && (subproblems[item - 1][currCap - weights[item]] != 0)
                        && (subproblems[item - 1][currCap] <
                        (subproblems[item - 1][currCap - weights[item]] + vals[item]))) {
                    // Case 2: The current item is included.
                    included.add(item);
                    currCap -= weights[item];
                }
                --item;
            }
        }
        if (weights[0] == currCap) {
            included.add(0);
        }
        return included;
        // Running time complexity: O(n)
    }

}
