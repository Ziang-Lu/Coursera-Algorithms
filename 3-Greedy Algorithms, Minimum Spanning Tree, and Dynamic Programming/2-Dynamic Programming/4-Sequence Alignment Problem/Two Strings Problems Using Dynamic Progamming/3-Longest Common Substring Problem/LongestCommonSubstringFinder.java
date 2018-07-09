/**
 * Given two strings X and Y, find the longest common substring.
 *
 * Algorithm: (Dynamic programming)
 * Find the longest common suffix for all pairs of prefixes of the strings.
 * => The maximum of these longest common suffixes of possible prefixes must be
 *    the longest common substring.
 *
 * Let LCS(X_i, Y_j) be the longest common suffix for the subproblem with the
 * prefix of i characters of X and j characters of Y, respectively, then
 * consider the final characters x_i and y_j:
 * 1. x_i and y_j are different:
 *    => LCS(X_i, Y_j) = 0
 * 2. x_i and y_j are the same:
 *    Let X_i' = {X_i - x_i} and Y_j' = {Y_j - y_j}
 *    => LCS(X_i, Y_j) = LCS(X_i', Y_j') + x_i
 */

public class LongestCommonSubstringFinder {

    /**
     * Subproblem solutions.
     * Since there are only O(mn) subproblems, the first time we solve a
     * subproblem, we can cache its solution in a global take for O(1) lookup
     * time later on.
     */
    private int[][] subproblems;

    /**
     * Finds the longest common substring of the given two strings in a
     * bottom-up way.
     * @param x first string
     * @param y second string
     * @return longest common substring
     */
    public String findLongestCommonSubstring(String x, String y) {
        // Check whether the input strings are null or empty
        if ((x == null) || (x.length() == 0) || (y == null) || (y.length() == 0)) {
            return "";
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
                }
            }
        }

        // Find the maximum of the longest common suffixes of possible prefixes, which is exactly the longest common
        // substring
        int iMax = 0, maxLength = subproblems[0][0];
        for (int i = 0; i <= m; ++i) {
            for (int j = 0; j <= n; ++j) {
                if (subproblems[i][j] > maxLength) {
                    iMax = i;
                    maxLength = subproblems[i][j];
                }
            }
        }
        return x.substring(iMax - maxLength, iMax);
        // Overall running time complexity is O(mn).
    }

}
