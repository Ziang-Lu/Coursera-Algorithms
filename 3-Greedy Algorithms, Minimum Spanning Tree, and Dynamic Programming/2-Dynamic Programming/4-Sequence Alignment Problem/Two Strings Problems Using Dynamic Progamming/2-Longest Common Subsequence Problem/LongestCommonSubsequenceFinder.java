/**
 * Given two strings X and Y of length m and n, respectively, find the longest
 * common subsequence (LCS).
 *
 * Algorithm: (Dynamic programming)
 * Denote LCS(X, Y) to be the LCS of X and Y.
 * Consider the final characters x_m and y_n:
 * 1. x_m and y_n are the same:
 *    Let X' = {X - x_m} and Y' = {Y - y_n}
 *    => {LCS(X, Y) - x_m} is the LCS of X' and Y'.
 *    => LCS(X, Y) = LCS(X', Y') + x_m
 * 2. x_m and y_n are different:
 *    => {LCS(X, Y) - x_m} is the LCS of X' and Y, and {LCS(X, Y) - y_n} is the
 *       LCS of X and Y'.
 *    => LCS(X, Y) = the longer one between LCS(X', Y) and LCS(X, Y')
 *
 * i.e.,
 * Let LCS(X_i, Y_j) be the LCS for subproblems with the prefix of i characters
 * of X and j characters of Y, respectively, then
 * If x_i == y_j:
 *     LCS(X_i, Y_j) = LCS(X_i - x_i, Y_j - y_j) + 1
 * Else:
 *     LCS(X_i, Y_j) = max{LCS(X_i - x_i, Y_j), LCS(X_i, Y_j - y_j)}
 */

import java.util.LinkedList;

public class LongestCommonSubsequenceFinder {

    /**
     * Subproblem solutions.
     * Since there are only O(mn) subproblems, the first time we solve a
     * subproblem, we can cache its solution in a global take for O(1) lookup
     * time later on.
     */
    private int[][] subproblems;

    /**
     * Finds the longest common subsequence of the given two strings in an
     * improved bottom-up way.
     * @param x first string
     * @param y second string
     * @return longest common subsequence
     */
    public LinkedList<Character> longestCommonSubsequence(String x, String y) {
        // Check whether the input strings are null or empty
        if ((x == null) || (x.length() == 0) || (y == null) || (y.length() == 0)) {
            throw new IllegalArgumentException("The input strings should not be null or empty.");
        }

        int m = x.length(), n = y.length();
        // Initialization
        subproblems = new int[m + 1][n + 1];
        // Bottom-up calculation
        for (int i = 1; i <= m; ++i) {
            for (int j = 1; j <= n; ++j) {
                char xCurr = x.charAt(i - 1), yCurr = y.charAt(j - 1);
                if (xCurr == yCurr) {
                    subproblems[i][j] = subproblems[i - 1][j - 1] + 1;
                } else {
                    subproblems[i][j] = Math.max(subproblems[i - 1][j], subproblems[i][j - 1]);
                }
            }
        }
        return reconstructLongestCommonSubsequence(x, y);
        // Overall running time complexity: O(mn)
    }

    /**
     * Private helper method to reconstruct the longest common subsequence
     * according to the optimal solution using backtracking.
     * @param x first string
     * @param y first string
     * @return longest common subsequence
     */
    private LinkedList<Character> reconstructLongestCommonSubsequence(String x, String y) {
        LinkedList<Character> lcs = new LinkedList<>();
        int i = x.length(), j = y.length();
        while ((i >= 1) && (j >= 1)) {
            char xCurr = x.charAt(i - 1), yCurr = y.charAt(j - 1);
            if (xCurr == yCurr) {
                lcs.addFirst(xCurr);
                --i;
                --j;
            } else {
                if (subproblems[i][j] == subproblems[i - 1][j]) {
                    --i;
                } else {
                    --j;
                }
            }
        }
        return lcs;
        // Running time complexity: O(m + n)
    }

}
