/**
 * Similar to the sequence alignment problem, but the penalty for inserting k
 * gaps in a row becomes (a*k + b), with a and b being non-negative.
 *
 * Algorithm: Similar to the ordinary sequence alignment problem, but need to
 * know whether the optimal solution to the subproblem ends with a gap, in
 * order to determine whether need to add a (if the optimal solution to the
 * subproblem ends with a gap) or (a + b) (if the optimal solution to the
 * subproblem doesn't end with a gap) to the total penalty.
 * => We need to keep track of whether each subproblem solution ends with a gap.
 *    => We can only solve this problem in a bottom-up way.
 */

import java.util.HashMap;

public class SequenceAlignmentWithConsecutiveGapsPen {

    /**
     * Subproblem solutions.
     * Since there are only O(mn) subproblems, the first time we solve a
     * subproblem, we can cache its solution in a global take for O(1) lookup
     * time later on.
     */
    private final int[][] subproblems;
    /**
     * Whether each subproblem solution Sx ends with a gap.
     */
    private final boolean[][] sxEndsWithGap;
    /**
     * Whether each subproblem solution Sy ends with a gap.
     */
    private final boolean[][] syEndsWithGap;

    /**
     * Solves the sequence alignment (with consecutive gaps penalty) of the
     * given two strings with the given penalties in a bottom-up way.
     * @param x first string
     * @param y second string
     * @param a slope of consecutive gaps penalty
     * @param b intercept of consecutive gaps penalty
     * @param penMap penalty between each character pair
     * @return optimal alignment
     */
    public String[] sequenceAlignmentWithConsecutiveGapsPen(String x, String y, int a, int b,
            HashMap<Character, HashMap<Character, Integer>> penMap) {
        // Check whether the input strings are null or empty
        if ((x == null) || (x.length() == 0) || (y == null) || (y.length() == 0)) {
            throw new IllegalArgumentException("The input sequences should not be null or empty.");
        }
        // Check whether the input a and b are non-negative
        if ((a < 0) || (b < 0)) {
            throw new IllegalArgumentException("The input a and b should be non-negative.");
        }
        // Check whether the input map is null
        if (penMap == null) {
            throw new NullPointerException("The input penalty map should not be null.");
        }

        int m = x.length(), n = y.length();
        // Initialization
        subproblems = new int[m + 1][n + 1];
        sxEndsWithGap = new boolean[m + 1][n + 1]; syEndsWithGap = new boolean[m + 1][n + 1];
        subproblems[0][0] = 0;
        sxEndsWithGap[0][0] = false; syEndsWithGap[0][0] = false;
        for (int i = 1; i <= m; ++i) {
            subproblems[i][0] = a * i + b;
            sxEndsWithGap[i][0] = false; syEndsWithGap[i][0] = true;
        }
        for (int j = 1; j <= n; ++j) {
            subproblems[0][j] = a * j + b;
            sxEndsWithGap[0][j] = true; syEndsWithGap[0][j] = false;
        }
        // Bottom-up calculation
        for (int i = 1; i <= m; ++i) {
            for (int j = 1; j <= n; ++j) {
                char xCurr = x.charAt(i - 1), yCurr = y.charAt(j - 1);
                int result1 = subproblems[i - 1][j - 1] + penMap.get(xCurr).get(yCurr);
                int result2 = subproblems[i - 1][j] + a;
                if (!syEndsWithGap[i - 1][j]) {
                    result2 += b;
                }
                int result3 = subproblems[i][j - 1] + a;
                if (!sxEndsWithGap[i][j - 1]) {
                    result3 += b;
                }
                int result = Math.min(Math.min(result1, result2), result3);
                subproblems[i][j] = result;
                if (result == result1) {
                    sxEndsWithGap[i][j] = false;
                    syEndsWithGap[i][j] = false;
                } else if (result == result2) {
                    sxEndsWithGap[i][j] = false;
                    syEndsWithGap[i][j] = true;
                } else {
                    sxEndsWithGap[i][j] = true;
                    syEndsWithGap[i][j] = false;
                }
            }
        }
        return reconstructOptimalAlignment(x, y, a, b, penMap);
        // Overall running time complexity: O(mn)
    }

    /**
     * Private helper method to reconstruct the optimal alignment according to
     * the optimal solution using backtracking.
     * @param x first string
     * @param y second string
     * @param a slope of consecutive gaps penalty
     * @param b intercept of consecutive gaps penalty
     * @param penMap penalty between each character pair
     * @return optimal alignment
     */
    private String[] reconstructOptimalAlignment(String x, String y, int a, int b,
            HashMap<Character, HashMap<Character, Integer>> penMap) {
        StringBuilder sx = new StringBuilder(), sy = new StringBuilder();
        int i = x.length(), j = y.length();
        while ((i >= 1) && (j >= 1)) {
            char xCurr = x.charAt(i - 1), yCurr = y.charAt(j - 1);
            int result1 = subproblems[i - 1][j - 1] + penMap.get(xCurr).get(yCurr);
            int result2 = subproblems[i - 1][j] + a;
            if (!sxEndsWithGap[i - 1][j]) {
                result2 += b;
            }
            int result3 = subproblems[i][j - 1] + a;
            if (!syEndsWithGap[i][j - 1]) {
                result3 += b;
            }
            int result = Math.min(Math.min(result1, result2), result3);
            if (result == result1) {
                // Case 1: The final positions are x_i and y_j.
                sx.insert(0, xCurr);
                sy.insert(0, yCurr);
                --i;
                --j;
            } else if (result == result2) {
                // Case 2: The final positions are x_i and a gap.
                sx.insert(0, xCurr);
                sy.insert(0, ' ');
                --i;
            } else {
                // Case 3: The final positions are a gap and y_j.
                sx.insert(0, ' ');
                sy.insert(0, yCurr);
                --j;
            }
        }
        if (i != 0) {
            padSpaces(sy, i);
        } else if (j != 0) {
            padSpaces(sx, j);
        }
        return new String[]{sx.toString(), sy.toString()};
        // Running time complexity: O(m + n)
    }

    /**
     * Helper method to pad the given number of spaces to the given
     * StringBuilder.
     * @param s StringBuilder to pad
     * @param n number of spaces
     */
    private void padSpaces(StringBuilder s, int n) {
        for (int k = 0; k < n; ++k) {
            s.insert(0, ' ');
        }
        // Running time complexity: O(n)
    }

}
