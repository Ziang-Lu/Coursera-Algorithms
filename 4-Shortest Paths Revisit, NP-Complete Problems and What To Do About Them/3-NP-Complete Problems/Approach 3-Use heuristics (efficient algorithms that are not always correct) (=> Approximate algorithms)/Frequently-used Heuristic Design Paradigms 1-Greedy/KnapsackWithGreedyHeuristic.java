/**
 * Knapsack problem with greedy-based heuristic.
 *
 * Motivation: Ideal items should have large value and small size.
 * => Similar to the scheduling problem
 *
 * Greedy algorithm:
 * 1. Sort the items by v_i/w_i
 * 2. Pack the items in this order. If one doesn't fit, skip it and continue.
 *    This single greedy heuristic can be arbitrarily bad relative to the
 *    optimal solution.
 *    Why? In the scheduling problem, this greedy algorithm is always correct
 *    because it doesn't have the restriction similar to the knapsack capacity.
 *    However in the knapsack problem., due to this restriction, the greedy
 *    algorithm is not always guaranteed to be correct.
 *    To fix this issue, we add the following second greedy heuristic.
 * 3. Sort the items again simply by v_i
 * 4. Same as 2
 * 5. Return whichever is better between the two greedy packings
 */

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

public class KnapsackWithGreedyHeuristic {

    /**
     * Static nested Item class.
     */
    private static class Item {
        /**
         * Index of this item.
         */
        private final int idx;
        /**
         * Value of this item.
         */
        private final double val;
        /**
         * Weight of this item.
         */
        private final int weight;

        /**
         * Constructor with parameter.
         * @param idx idex of the item
         * @param val value of the item
         * @param weight weight of the item
         */
        Item(int idx, dobule val, int weight) {
            this.idx = idx;
            this.val = val;
            this.weight = weight;
        }

        /**
         * Accessor of idx.
         * @return idx
         */
        int getIdx() {
            return idx;
        }

        /**
         * Accessor of val.
         * @return val
         */
        double getVal() {
            return val;
        }

        /**
         * Accessor of weight.
         * @return
         */
        int getWeight() {
            return weight;
        }
    }

    /**
     * Solves the knapsack problem of the items with the given values and
     * weights, and the given capacity, using a greedy heuristic.
     * @param vals values of the items
     * @param weights weights of the items
     * @param cap capacity of the knapsack
     * @return included items
     */
    public Set<Integer> knapsackGreedy(double[] vals, int[] weights, int cap) {
        // Check whether the input arrays are null or empty
        if ((vals == null) || (vals.length == 0)) {
            return new HashSet<>();
        }
        // Check whether the input capacity is non-negative
        if (cap < 0) {
            return new HashSet<>();
        }

        int n = vals.length;
        Item[] items = new Item[n];
        for (int idx = 0; idx < n; ++idx) {
            items[idx] = new Item(idx, vals[idx], weights[idx]);
        }

        Set<Integer> included1 = greedyPacking(items, cap, new Comparator<Item>() {
            @Override
            public int compare(Item o1, Item o2) {
                double ratio1 = o1.getVal() / o1.getWeight(), ratio2 = o2.getVal() / o2.getWeight();
                return Double.compare(ratio2, ratio1);
            }
        });
        int totalVal1 = 0;
        for (Integer idx : included1) {
            totalVal1 += vals[idx];
        }

        Set<Integer> included2 = greedyPacking(items, cap, new Comparator<Item>() {
            @Override
            public int compare(Item o1, Item o2) {
                return Double.compare(o2.getVal(), o1.getVal());
            }
        });
        int totalVal2 = 0;
        for (Integer idx : included2) {
            totalVal2 += vals[idx];
        }

        if (totalVal1 >= totalVal2) {
            return included1;
        } else {
            return included2;
        }
        // Overall running time complexity: O(nlog n)
    }

    /**
     * Private helper method to pack the items using the given greedy heuristic.
     * @param items items to pack
     * @param cap capacity of the knapsack
     * @param comparator given greedy heuristic
     * @return included items
     */
    private Set<Integer> greedyPacking(Item[] items, int cap, Comparator<Item> comparator) {
        Arrays.sort(items, comparator);
        Set<Integer> included = new HashSet<>();
        int totalWeight = 0;
        for (Item item : items) {
            if ((totalWeight + item.getWeight()) > cap) {
                continue;
            }
            included.add(item.getIdx());
            totalWeight += item.getWeight();
        }
        return included;
        // Running time complexity: O(nlog n)
    }

}
