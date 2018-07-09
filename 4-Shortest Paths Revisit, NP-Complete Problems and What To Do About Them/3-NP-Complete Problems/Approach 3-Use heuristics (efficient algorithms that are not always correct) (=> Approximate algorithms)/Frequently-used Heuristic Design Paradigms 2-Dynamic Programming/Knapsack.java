/**
 * Knapsack problem.
 *
 * Assume that the values are all integer.
 *
 * Another dynamic programming algorithm:
 * Let S(i, x) be the optimal solution for the subproblem among the first i
 * items and target value we are striving for x, i.e., the minimum total weight
 * subject to getting a total value >= x.
 * Consider whether item-i is in S:
 * 1. item-i is NOT in S:
 *    => S must be optimal among only the first (i - 1) items and target value
 *       x.
 *    => S = the optimal solution among the first (i - 1) items and target value
 *           x
 * 2. item-i is in S:
 *    => {S - item-i} must be optimal among only the first (i - 1) items and
 *       target value (x - v_i).
 *    => S = the optimal solution among the first (i - 1) items and target value
 *           (x - v_i) + item-i
 * i.e.,
 * S(i, x) = max{S(i - 1, x), S(i - 1, x - v_i) + w_i}
 */

import java.util.HashSet;
import java.util.Set;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;

public class Knapsack {

    /**
     * Infinity value.
     */
    private static final double INFINITY = 1000000.0;

    /**
     * Subproblem solutions.
     * Since there are only O(n sum(v)) distinct subproblems, the first time we
     * solve a subproblem, we can cache its solution in a global take for O(1)
     * lookup time later on.
     */
    private double[][] subproblems;

    /**
     * Solves the knapsack problem of the items with the given values and
     * weights, and the given capacity, in an improved bottom-up way.
     * @param vals values of the items
     * @param weights weights of the items
     * @param cap capacity of the knapsack
     * @return included items
     */
    public Set<Integer> knapsack(int[] vals, double[] weights, double cap) {
        // Check whether the input arrays are null or empty
        if ((vals == null) || (vals.length == 0)) {
            return new HashSet<>();
        }
        // Check whether the input capacity is non-negative
        if (cap < 0.0) {
            return new HashSet<>();
        }

        int n = vals.length;
        int valSum = IntStream.of(vals).sum();
        // Initialization
        subproblems = new double[n][valSum + 1];
        for (int x = 0; x <= valSum; ++x) {
            if (vals[0] >= x) {
                subproblems[0][x] = weights[0];
            } else {
                subproblems[0][x] = INFINITY;
            }
        }
        // Bottom-up calculation
        for (int item = 1; item < n; ++item) {
            for (int x = 0; x <= valSum; ++x) {
                double resultWithoutCurr = subproblems[item - 1][x];
                double resultWithCurr = weights[item];
                if (vals[item] < x) {
                    resultWithCurr += subproblems[item - 1][x - vals[item]];
                }
                subproblems[item][x] = Math.min(resultWithoutCurr, resultWithCurr);
            }
        }
        return reconstruct(vals, weights, cap);
        // Overall running time complexity: O(n sum(v)) <= O(n n v_max) = O(n^2 v_max)
    }

    /**
     * Private helper method to find the optimal solution itself and reconstruct
     * the included items according to the optimal solution using backtracking.
     * @param vals values of the items
     * @param weights weights of the items
     * @param cap capacity of the knapsack
     * @return included items
     */
    private Set<Integer> reconstruct(int[] vals, double[] weights, double cap) {
        // Find the optimal solution itself
        int valSum = IntStream.of(vals).sum();
        int maxTotalVal = 0, lastItem = -1;
        for (int x = valSum; x >= 0; --x) {
            boolean found = false;
            for (int item = 0; item < vals.length; ++item) {
                if (subproblems[item][x] <= cap) {
                    maxTotalVal = x;
                    lastItem = item;
                    found = true;
                    break;
                }
            }
            if (found) {
                break;
            }
        }

        // Reconstruct the included items from the optimal solution
        Set<Integer> included = new HashSet<>();
        int item = lastItem, currTargetVal = maxTotalVal;
        double currCap = cap;
        while (item >= 1) {
            double resultWithoutCurr = subproblems[item - 1][currTargetVal];
            double resultWithCurr = weights[item];
            if (vals[item] < currTargetVal) {
                resultWithCurr += subproblems[item - 1][currTargetVal - vals[item]];
            }
            if ((resultWithoutCurr > resultWithCurr) && (weights[item] <= currCap)) {
                // Case 2: The current item is included.
                included.add(item);
                currTargetVal -= vals[item];
                currCap -= weights[item];
            }
            --item;
        }
        if ((vals[0] >= currTargetVal) && (weights[0] <= currCap)) {
            included.add(0);
        }
        return included;
        // Running time complexity: O(n)
    }

    /**
     * Solves the knapsack problem of the items with the given values and
     * weights, and the given capacity, using a dynamic programming-based
     * heuristic with the given tolerant error rate.
     * @param vals values of the items
     * @param weights weights of the items
     * @param cap capacity of the knapsack
     * @param epsilon tolerant error rate
     * @return included items
     */
    public Set<Integer> knapsackWithDynamicProgrammingHeuristic(double[] vals, double[] weights, double cap,
            double epsilon) {
        // Check whether the input arrays are null or empty
        if ((vals == null) || (vals.length == 0)) {
            return new HashSet<>();
        }
        // Check whether the input capacity is non-negative
        if (cap < 0.0) {
            return new HashSet<>();
        }
        // Check whether the input tolerant error rate is in (0, 1)
        if ((epsilon <= 0.0) || (epsilon >= 1.0)) {
            return new HashSet<>();
        }

        // Refer to the documentation for the choice of m
        double maxVal = DoubleStream.of(vals).max().getAsDouble();
        int n = vals.length;
        double m = epsilon * maxVal / n;
        // Perform transformation
        int[] transformedVals = new int[n];
        for (int i = 0; i < n; ++i) {
            transformedVals[i] = (int) (vals[i] / m);
        }

        // Feed the rounded values, the original weights and the knapsack capacity into the above algorithm, and get the
        // included items in the optimal solution
        return knapsack(transformedVals, weights, cap);
        // Overall running time complexity: O(n^3 / epsilon)
    }

}
