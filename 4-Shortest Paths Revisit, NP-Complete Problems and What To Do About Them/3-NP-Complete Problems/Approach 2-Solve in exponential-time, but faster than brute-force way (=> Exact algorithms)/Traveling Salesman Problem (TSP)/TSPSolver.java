/**
 * Traveling Salesman Problem (TSP):
 *
 * This problem can be solved in a brute-force way, namely n! permutations of
 * the vertices.
 * However, it can be solved smarter and faster.
 *
 * Algorithm: (Dynamic programming)
 * Let P(v, S) be the shortest path from s to v that visits the vertices in S,
 * containing s and v, exactly once for each.
 * Then by plucking off the final hop (w, v), we form P'(w, S-v) that is the
 * shortest path from s to w that visits the vertices in {S - v}, containing s
 * and w, exactly once for each. Therefore, the recurrence is
 * P(v, S) = min_(w in S, w != v) {P'(w, S-v) + c_(w, v)}
 * (w即为shortest path中的倒数第二个vertex)
 */

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class TSPSolver {

    /**
     * Infinity distance.
     */
    private static final int INFINITY = 1000000;

    /**
     * Solves the TSP of the given cities, assuming the first city is the source
     * city.
     * @param cities cities to solve TSP
     * @return minimum tour length
     */
    public float tsp(Point2D.Float[] cities) {
        // Check whether the input array is null or empty
        if ((cities == null) || (cities.length == 0)) {
            throw new IllegalArgumentException("The input cities should not be null or empty.");
        }

        int n = cities.length, s = 0;

        // Initialization
        List<Map<String, Float>> A = new ArrayList<>();
        // Convert the city subset that contains only the source city to a BitSet
        StringBuilder srcS = new StringBuilder();
        for (int v = 0; v < n; ++v) {
            srcS.append('0');
        }
        srcS.setCharAt(s, '1');
        for (int v = 0; v < n; ++v) {
            Map<String, Float> shortestPathLenths = new HashMap<>();
            if (v == s) {
                shortestPathLenths.put(srcS.toString(), (float) 0.0);
            } else {
                shortestPathLenths.put(srcS.toString(), (float) INFINITY);
            }
            A.add(shortestPathLenths);
        }

        // Bottom-up calculation
        for (int m = 2; m <= n; ++m) {
            for (Set<Integer> subset : combinations(n, m)) {
                // The source city must be in the city subset.
                if (!subset.contains(s)) {
                    continue;
                }
                // Convert the city subset to a bit string
                StringBuilder S = constructBitStr(n, subset);
                for (int v = 0; v < n; ++v) {
                    // Let P(v, S) be the shortest path from s to v that visits the vertices in S, containing s and v,
                    // exactly once for each.

                    // Make sure S contains v
                    if (!bitIsSet(S, v)) {
                        continue;
                    }

                    if (v == s) {
                        Map<String, Float> shortestPathLengths = A.get(s);
                        shortestPathLengths.put(S.toString(), (float) INFINITY);
                        A.set(s, shortestPathLengths);
                        continue;
                    }

                    // By plucking off the final hop (w, v), we form P'(w, S-v) that is the shortest path from s to w
                    // that visits the vertices in {S - v}, containing s and w, exactly once for each.
                    // P(v, S) is the minimum among the possible choices of w in S, w != v.
                    changeBit(S, v, false);
                    float minPathLength = (float) INFINITY;
                    for (int w = 0; w < n; ++w) {
                        if (bitIsSet(S, w) && (w != v)) {
                            float pathLength = A.get(w).get(S.toString()) + (float) cities[w].distance(cities[v]);
                            minPathLength = Math.min(minPathLength, pathLength);
                        }
                    }
                    changeBit(S, v, true);
                    Map<String, Float> shortestPathLengths = A.get(v);
                    shortestPathLengths.put(S.toString(), minPathLength);
                    A.set(v, shortestPathLengths);
                }
            }
        }

        // By now the algorithm only computes the shortest paths from s to each v that visit all the vertices exactly
        // once for each.
        // However to complete TSP, we still need to go back to s.
        StringBuilder S = new StringBuilder();
        for (int v = 0; v < n; ++v) {
            S.append('1');
        }
        float minTourLength = (float) INFINITY;
        for (int w = 0; w < n; ++w) {
            if (w != s) {
                float tourLength = A.get(w).get(S.toString()) + (float) cities[w].distance(cities[s]);
                minTourLength = Math.min(minTourLength, tourLength);
            }
        }
        return minTourLength;
        // Since there are O(n 2^n) subproblems, and each subproblem runs in O(n), the overall running time complexity
        // is O(n^2 2^n).
        // Note that the running time complexity is still exponential, but it's better than brute-force (O(n!)).
        // Overall space complexity: O(n 2^n)
    }

    /**
     * Private helper method to implement combinations "n choose k" with the
     * given n and k. Specifically, returns all the combinations from 0 to (n -
     * 1) with k numbers.
     * @param n given n
     * @param k given k
     * @return combinations from 0 to (n - 1) with k numbers
     */
    private List<Set<Integer>> combinations(int n, int k) {
        List<Set<Integer>> combs = new ArrayList<>();
        combinationsHelper(combs, new Integer[k], 0, 0, n - 1);
        return combs;
        // Running time complexity: O(n^k)
    }

    /**
     * Private helper method to set the given spot to some number in the current
     * combination.
     * @param combs all combinations
     * @param currComb current combination
     * @param spot given spot
     * @param start smallest candidate for the given spot
     * @param n largest candidate for the given spot
     */
    private void combinationsHelper(List<Set<Integer>> combs, Integer[] currComb, int spot, int start, int n) {
        // We can keep the invariant that all the combinations are sorted. Thus [start, n] serves as the candidates for
        // the current spot.

        // Base case
        if (spot >= currComb.length) {
            combs.add(new HashSet<>(Arrays.asList(currComb)));
            return;
        }
        // Recursive case
        for (int candidate = start; candidate < n; ++candidate) {
            currComb[spot] = candidate;
            // Since all of the final combinations should be sorted and all the candidates can only be used once, the
            // candidates for the remaining spots should be [candidate + 1, n].
            combinationsHelper(combs, currComb, spot + 1, start + 1, n);
        }
    }

    /**
     * Private helper method to construct a bit string of the given length, with the given indices set.
     * @param length length of the bit string
     * @param setIdxs set indices
     * @return constructed bit string
     */
    private StringBuilder constructBitStr(int length, Set<Integer> setIdxs) {
        StringBuilder bitStr = new StringBuilder();
        for (int i = 0; i < length; ++i) {
            bitStr.append('0');
        }
        for (Integer setIdx : setIdxs) {
            changeBit(bitStr, setIdx, true);
        }
        return bitStr;
        // Running time complexity: O(n), where n is the length of the set indices
    }

    /**
     * Helper method to change the i-th bit in the given bit string.
     * @param bitStr bit string to change
     * @param i i-th bit to change
     * @param set whether to set or clear the bit
     */
    private void changeBit(StringBuilder bitStr, int i, boolean set) {
        if (set) {
            bitStr.setCharAt(i, '1');
        } else {
            bitStr.setCharAt(i, '0');
        }
        // Running time complexity: O(1)
    }

    /**
     * Private helper method to check whether the i-th bit is set in the given
     * bit string.
     * @param bitStr bit string to check
     * @param i i-th bit to check
     * @return whether the i-th bit is set
     */
    private boolean bitIsSet(StringBuilder bitStr, int i) {
        return bitStr.charAt(i) == '1';
        // Running time complexity: O(1)
    }

    /**
     * Solves the TSP of the given cities, assuming the first city is the source
     * city, with optimization.
     * @param cities cities to solve TSP
     * @return minimum tour length
     */
    public float tspOptimized(Point2D.Float[] cities) {
        // Check whether the input array is null or empty
        if ((cities == null) || (cities.length == 0)) {
            throw new IllegalArgumentException("The input cities should not be null or empty.");
        }

        int n = cities.length, s = 0;

        // Initialization
        // Space optimization: We only keep track of the subproblem solutions in the previous out-most iteration.
        List<Map<String, Float>> prevMSubproblems = new ArrayList<>();
        StringBuilder srcS = new StringBuilder();
        for (int v = 0; v < n; ++v) {
            srcS.append('0');
        }
        srcS.setCharAt(s, '1');
        for (int v = 0; v < n; ++v) {
            Map<String, Float> shortestPathLenths = new HashMap<>();
            if (v == s) {
                shortestPathLenths.put(srcS.toString(), (float) 0.0);
            } else {
                shortestPathLenths.put(srcS.toString(), (float) INFINITY);
            }
            prevMSubproblems.add(shortestPathLenths);
        }

        // Bottom-up calculation
        for (int m = 2; m <= n; ++m) {
            // Initialize the subproblems of size-m subsets
            List<Map<String, Float>> currMSubproblems = new ArrayList<>();
            for (int v = 0; v < n; ++v) {
                currMSubproblems.add(new HashMap<>());
            }

            for (Set<Integer> subset : combinations(n, m)) {
                if (!subset.contains(s)) {
                    continue;
                }
                StringBuilder S = constructBitStr(n, subset);
                for (int v = 0; v < n; ++v) {
                    if (!bitIsSet(S, v)) {
                        continue;
                    }

                    if (v == s) {
                        Map<String, Float> shortestPathLengths = prevMSubproblems.get(s);
                        shortestPathLengths.put(S.toString(), (float) INFINITY);
                        currMSubproblems.set(s, shortestPathLengths);
                        continue;
                    }

                    changeBit(S, v, false);
                    float minPathLength = (float) INFINITY;
                    for (int w = 0; w < n; ++w) {
                        if (bitIsSet(S, w) && (w != v)) {
                            float pathLength = prevMSubproblems.get(w).get(S.toString())
                                    + (float) cities[w].distance(cities[v]);
                            minPathLength = Math.min(minPathLength, pathLength);
                        }
                    }
                    changeBit(S, v, true);
                    Map<String, Float> shortestPathLengths = prevMSubproblems.get(v);
                    shortestPathLengths.put(S.toString(), minPathLength);
                    currMSubproblems.set(v, shortestPathLengths);
                }
            }
            prevMSubproblems = currMSubproblems;
            // Explicitly run garbage collection
            System.gc();
        }

        // By now the algorithm only computes the shortest paths from s to each v that visit all the vertices exactly
        // once for each.
        // However to complete TSP, we still need to go back to s.
        StringBuilder S = new StringBuilder();
        for (int v = 0; v < n; ++v) {
            S.append('1');
        }
        float minTourLength = (float) INFINITY;
        for (int w = 0; w < n; ++w) {
            if (w != s) {
                float tourLength = prevMSubproblems.get(w).get(S.toString()) + (float) cities[w].distance(cities[s]);
                minTourLength = Math.min(minTourLength, tourLength);
            }
        }
        return minTourLength;
        // Overall running time complexity: O(n^2 2^n)
        // Overall space complexity: O(n * (n choose k)) = O(n^(k+1))
    }

}
