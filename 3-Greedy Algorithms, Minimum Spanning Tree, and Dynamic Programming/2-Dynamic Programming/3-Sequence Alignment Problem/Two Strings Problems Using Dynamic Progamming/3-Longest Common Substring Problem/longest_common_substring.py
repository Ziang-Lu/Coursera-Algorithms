#!usr/bin/env python3
# -*- coding: utf-8 -*-

"""
Given two strings X and Y, find the longest common substring.

Algorithm: (Dynamic programming)
Find the longest common suffix for all pairs of prefixes of the strings.
=> The maximum of these longest common suffixes of possible prefixes must be the
   longest common substring.

Let LCS(X_i, Y_j) be the longest common suffix for the subproblem with the
prefix of i characters of X and j characters of Y, respectively, then consider
the final characters x_i and y_j:
1. x_i and y_j are different:
   => LCS(X_i, Y_j) = 0
2. x_i and y_j are the same:
   Let X_i' = {X_i - x_i} and Y_j' = {Y_j - y_j}
   => LCS(X_i, Y_j) = LCS(X_i', Y_j') + x_i
"""

__author__ = 'Ziang Lu'


class IllegalArgumentError(ValueError):
    pass


def find_longest_common_substring(x, y):
    # Check whether the input strings are None or empty
    if x is None or len(x) == 0 or y is None or len(y) == 0:
        raise IllegalArgumentError('The input strings should noe bt None or '
                                   'empty.')

    m, n = len(x), len(y)
    # Initialization
    subproblems = [[0] * (n + 1) for i in range(m + 1)]
    # Bottom-up calculation
    for i in range(1, m + 1):
        for j in range(1, n + 1):
            x_curr, y_curr = x[i - 1], y[j - 1]
            if x_curr == y_curr:
                subproblems[i][j] = subproblems[i - 1][j - 1] + 1

    # Find the maximum of the longest common suffix of possible prefixes, which
    # is exactly the longest common substring
    i_max, max_length = 0, subproblems[0][0]
    for i in range(m + 1):
        for j in range(n + 1):
            if subproblems[i][j] > max_length:
                i_max = i
                max_length = subproblems[i][j]
    return x[i_max - max_length:i_max]
    # Overall running time complexity: O(mn)
