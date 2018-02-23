/**
 * Given a path graph, with each vertex having a non-negative weight, find the
 * maximum-weight independent set (MWIS), where an independent set is a subset
 * of the vertices, so that no two vertices are adjacent.
 *
 * Algorithm: (Dynamic programming)
 * Denote S to be the optimal solution, i.e., the MWIS in the given path graph,
 * and v_n to be the last vertex of the given path graph.
 * Consider whether v_n is in S:
 * 1. v_n is NOT in S:
 *    Let G' = G - v_n
 *    => S is the MWIS in G'.
 *       (Proof:
 *       Assume S* is the MWIS in G' (S* > S), then in G, we still have S* > S,
 *       so S is not the optimal solution in G. [CONTRADICTION])
 *    => S = the MWIS in G'
 * 2. v_n is in S:
 *    => v_n-1 is NOT in S.
 *    Let G'' = G - v_n - v_n-1
 *    => {S - v_n} is the MWIS in G''.
 *       (Proof:
 *       Assume S* is the MWIS in G'' (S* > {S - v_n}), then in G, we have
 *       S* + v_n > S, so S is not the optimal solution in G. [CONTRADICTION])
 *    => S = the MWIS in G'' + v_n
 *
 * i.e.,
 * Let S(i) be the optimal solution for the subproblem with the first i vertices
 * in the given path graph, then
 * S(i) = max(S(i - 1), S(i - 2) + v_i)
 */

import java.util.Arrays;
import java.util.HashSet;

public class MaxWeightIndependentSet {

    /**
     * Default negative value for mwisSubResults.
     */
    private static final int DEFAULT_MWIS_SUB_RESULT = -1;
    /**
     * Solutions for the subproblems.
     * Since there are only O(n) distinct subproblems, the first time we solve a
     * subproblem, we can cache its solution in a global take for O(1) lookup
     * time later on.
     */
    private static int[] subproblemSols;

    /**
     * Finds the maximum-weight independent set (MWIS) in a path graph with the
     * given weights in a straightforward way.
     * @param weights weights of the path graph
     * @return MWIS in the given path graph
     */
    public static HashSet<Integer> findMWISStraightforward(int[] weights) {
        // Check whether the input array is null or empty
        if ((weights == null) || (weights.length == 0)) {
            throw new IllegalArgumentException("The input weights should not be null or empty.");
        }

        if (weights.length == 1) {
            HashSet<Integer> mwis = new HashSet<Integer>();
            mwis.add(1);
            return mwis;
        }

        subproblemSols = new int[weights.length];
        Arrays.fill(subproblemSols, DEFAULT_MWIS_SUB_RESULT);
        findMWISHelper(weights, weights.length - 1);
        return reconstructMWIS(weights);
        // With memoization, the overall running time complexity is O(n), since there are only n distinct subproblems.
    }

    /**
     * Private helper method to find the MWIS in the given sub path graph with
     * the given weights recursively.
     * @param weights weights of the path graph
     * @param lastIdx last vertex index of the sub path graph
     */
    private static void findMWISHelper(int[] weights, int lastIdx) {
        if (subproblemSols[lastIdx] != DEFAULT_MWIS_SUB_RESULT) {
            return;
        }

        // Base case 1: Only the left-most vertex
        if (lastIdx == 0) {
            subproblemSols[0] = weights[0];
            return;
        }
        // Base case 2: Only the left-most two vertices
        if (lastIdx == 1) {
            subproblemSols[1] = Math.max(weights[0], weights[1]);
            return;
        }

        // Recursive case
        findMWISHelper(weights, lastIdx - 1);
        int resultWithoutLast = subproblemSols[lastIdx - 1];
        findMWISHelper(weights, lastIdx - 2);
        int resultWithLast = subproblemSols[lastIdx - 1] + weights[lastIdx];
        subproblemSols[lastIdx] = Math.max(resultWithoutLast, resultWithLast);
    }

    /**
     * Private helper method to reconstruct MWIS from the solutions of the
     * subproblems.
     * @param weights weights of the path graph
     * @return MWIS of the given path graph
     */
    private static HashSet<Integer> reconstructMWIS(int[] weights) {
        HashSet<Integer> mwis = new HashSet<Integer>();
        int i = subproblemSols.length - 1;
        while (i >= 2) {
            if (subproblemSols[i - 1] >= (subproblemSols[i - 2] + weights[i])) {
                i--;
            } else {
                mwis.add(i + 1);
                i -= 2;
            }
        }
        if (i == 1) {
            if (weights[0] >= weights[1]) {
                mwis.add(1);
            } else {
                mwis.add(2);
            }
        } else if (i == 0) {
            mwis.add(1);
        }
        return mwis;
        // Running time complexity: O(n)
    }

    /**
     * Finds the maximum-weight independent set (MWIS) in a path graph with the
     * given weights in an improved bottom-up way.
     * @param weights weights of the path graph
     * @return MWIS in the given path graph
     */
    public static HashSet<Integer> findMWIS(int[] weights) {
        // Check whether the input array is null or empty
        if ((weights == null) || (weights.length == 0)) {
            throw new IllegalArgumentException("The input weights should not be null or empty.");
        }

        if (weights.length == 1) {
            HashSet<Integer> mwis = new HashSet<Integer>();
            mwis.add(1);
            return mwis;
        }

        subproblemSols = new int[weights.length];
        subproblemSols[0] = weights[0];
        subproblemSols[1] = Math.max(weights[0], weights[1]);
        for (int i = 2; i < weights.length; ++i) {
            subproblemSols[i] = Math.max(subproblemSols[i - 1], subproblemSols[i - 2] + weights[i]);
        }
        return reconstructMWIS(weights);
        // Overall running time complexity: O(n)
    }

}
