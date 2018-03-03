/**
 * Similar to the knapsack problem, but subject to total included items <= k <=
 * n.
 *
 * Algorithm: (Dynamic programming)
 * Denote S to be the optimal solution, and item-n to be the last item.
 * Consider whether item-n is in S:
 * 1. item-n is NOT in S:
 *    => S must be optimal among only the first (n - 1) items with budget k
 *       and capacity W.
 *    => S = the optimal solution among only the first (n - 1) items with
 *           budget k and capacity W
 * 2. item-n is in S:
 *    => S = the optimal solution among only the first (n - 1) items with
 *           budget (k - 1) and capacity (W - w_n) (i.e., the space is
 *           "reserved" for item-n).
 *    => S = the optimal solution among only the first (n - 1) items with
 *           budget (k - 1) and capacity (W - w_n) + v_n
 *
 * i.e.,
 * Let S(i, b, x) be the optimal solution for the subproblem among the first i
 * items with budget b and capacity x, then
 * S(i, b, x) = max(S(i - 1, b, x), S(i - 1, b - 1, x - w_i) + v_i)
 */

import java.util.HashSet;

public class KnapsackWithBudget {

    /**
     * Default negative value for the subproblem solutions.
     */
    private static final int DEFAULT_SUBPROBLEM_SOL = -1;

    /**
     * Subproblem solutions.
     * Since there are only O(n*k*W2) distinct subproblems, the first time we
     * solve a subproblem, we can cache its solution in a global take for O(1)
     * lookup time later on.
     */
    private int[][][] subproblems;

    /**
     * Solves the knapsack problem (with budget) of the items with the given
     * values and weights, with the given budget and capacity, in a
     * straightforward way.
     * @param vals values of the items
     * @param weights weights of the items
     * @param budget budget
     * @param cap capacity of the knapsack
     * @return included items
     */
    public HashSet<Integer> knapsackWithBudgetStraightforward(int[] vals, int[] weights, int budget, int cap) {
        // Check whether the input arrays are null or empty
        if ((vals == null) || (vals.length == 0) || (weights == null) || (weights.length == 0)) {
            throw new IllegalArgumentException("The input values and weights should not be null or empty.");
        }
        // Check whether the input budget is non-negative
        if (budget < 0) {
            throw new IllegalArgumentException("The input budget should be non-negative.");
        }
        // Check whether the input capacity is non-negative
        if (cap < 0) {
            throw new IllegalArgumentException("The input capacity should be non-negative.");
        }

        int n = vals.length;
        initializeSubproblemSols(n, budget, cap);
        knapsackWithBudgetHelper(vals, weights, n - 1, budget, cap);
        return reconstruct(vals, weights, budget, cap);
        // With memoization, the overall running time complexity is O(n*k*W), where k is the budget and W is the
        // knapsack capacity.
    }

    /**
     * Private helper method to initialize the subproblem solutions.
     * @param n number of items
     * @param budget budget
     * @param cap capacity of the knapsack
     */
    private void initializeSubproblemSols(int n, int budget, int cap) {
        subproblems = new int[n][budget + 1][cap + 1];
        for (int i = 0; i < n; ++i) {
            for (int b = 0; b <= budget; ++b) {
                for (int x = 0; x <= cap; ++x) {
                    subproblems[i][b][x] = DEFAULT_SUBPROBLEM_SOL;
                }
            }
        }
        // Running time complexity: O(n*k*W), where k is the budget and W is the knapsack capacity
    }

    /**
     * Private helper method to solve the knapsack problem (with budget) with
     * the given first items with the given budget and capacity recursively.
     * @param vals values of the items
     * @param weights weights of the items
     * @param lastItem last item of the subproblem
     * @param currBudget budget of the problem
     * @param currCap capacity of the problem
     */
    private void knapsackWithBudgetHelper(int[] vals, int[] weights, int lastItem, int currBudget, int currCap) {
        if (subproblems[lastItem][currBudget][currCap] != DEFAULT_SUBPROBLEM_SOL) {
            return;
        }

        // Base case
        if (lastItem == 0) {
            if ((currBudget >= 1) && (weights[0] <= currCap)) {
                subproblems[0][currBudget][currCap] = vals[0];
            }
            return;
        }
        // Recursive case
        if ((currBudget <= 0) || (weights[lastItem] > currCap)) {
            knapsackWithBudgetHelper(vals, weights, lastItem - 1, currBudget, currCap);
            subproblems[lastItem][currBudget][currCap] = subproblems[lastItem - 1][currBudget][currCap];
        } else {
            knapsackWithBudgetHelper(vals, weights, lastItem - 1, currBudget, currCap);
            int resultWithoutLast = subproblems[lastItem - 1][currBudget][currCap];
            knapsackWithBudgetHelper(vals, weights, lastItem - 1, currBudget - 1, currCap - weights[lastItem]);
            int resultWithLast = subproblems[lastItem - 1][currBudget - 1][currCap - weights[lastItem]]
                    + vals[lastItem];
            subproblems[lastItem][currBudget][currCap] = Math.max(resultWithoutLast, resultWithLast);
        }
    }

    /**
     * Private helper method to reconstruct the included items according to the
     * optimal solution using backtracking.
     * @param vals values of the items
     * @param weights weights of the items
     * @param budget budget
     * @param cap capacity of the knapsack
     * @return included items
     */
    private HashSet<Integer> reconstruct(int[] vals, int[] weights, int budget, int cap) {
        HashSet<Integer> includedItems = new HashSet<Integer>();
        int currItem = vals.length - 1, currBudget = budget, currCap = cap;
        while (currItem >= 1) {
            if ((currBudget >= 1) && (weights[currItem] <= currCap) && (subproblems[currItem
                    - 1][currBudget][currCap] < (subproblems[currItem - 1][currBudget - 1][currCap - weights[currItem]]
                            + vals[currItem]))) {
                // Case 2: The current item is included.
                includedItems.add(currItem);
                --currBudget;
                currCap -= weights[currItem];
            }
            --currItem;
        }
        if ((currBudget >= 1) && (weights[0] <= currCap)) {
            includedItems.add(0);
        }
        return includedItems;
        // Running time complexity: O(n)
    }

    /**
     * Solves the knapsack problem (with budget) of the items with the given
     * values and weights, with the given budget and capacity, in an improved
     * bottom-up way.
     * @param vals values of the items
     * @param weights weights of the items
     * @param budget budget
     * @param cap capacity of the knapsack
     * @return included items
     */
    public HashSet<Integer> knapsackWithBudget(int[] vals, int[] weights, int budget, int cap) {
        // Check whether the input arrays are null or empty
        if ((vals == null) || (vals.length == 0) || (weights == null) || (weights.length == 0)) {
            throw new IllegalArgumentException("The input values and weights should not be null or empty.");
        }
        // Check whether the input budget is non-negative
        if (budget < 0) {
            throw new IllegalArgumentException("The input budget should be non-negative.");
        }
        // Check whether the input capacity is non-negative
        if (cap < 0) {
            throw new IllegalArgumentException("The input capacity should be non-negative.");
        }

        int n = vals.length;
        // Initialization
        subproblems = new int[n][budget + 1][cap + 1];
        for (int b = 0; b <= budget; ++b) {
            for (int x = 0; x <= cap; ++x) {
                if ((b >= 1) && (weights[0] <= x)) {
                    subproblems[0][b][x] = vals[0];
                }
            }
        }
        // Bottom-up calculation
        for (int item = 1; item < n; ++item) {
            for (int b = 0; b <= budget; ++b) {
                for (int x = 0; x <= cap; ++x) {
                    if ((b <= 0) || (weights[item] > x)) {
                        subproblems[item][b][x] = subproblems[item - 1][b][x];
                    } else {
                        subproblems[item][b][x] = Math.max(subproblems[item - 1][b][x],
                                subproblems[item - 1][b - 1][x - weights[item]] + vals[item]);
                    }
                }
            }
        }
        return reconstruct(vals, weights, budget, cap);
        // Overall running time complexity: O(n*k*W), where k is the budget and W is the knapsack capacity
    }

}
