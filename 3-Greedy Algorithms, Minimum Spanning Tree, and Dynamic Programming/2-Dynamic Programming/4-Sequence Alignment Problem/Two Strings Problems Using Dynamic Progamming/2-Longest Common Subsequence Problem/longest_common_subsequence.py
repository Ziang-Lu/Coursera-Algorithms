#!usr/bin/env python3
# -*- coding: utf-8 -*-

"""
Given two strings X and Y of length m and n, respectively, find the longest
common subsequence (LCS).

Algorithm: (Dynamic programming)
Denote LCS(X, Y) to be the LCS of X and Y.
Consider the final characters x_m and y_n:
1. x_m and y_n are the same:
   Let X' = {X - x_m} and Y' = {Y - y_n}
   => {LCS(X, Y) - x_m} is the LCS of X' and Y'
   => LCS(X, Y) = LCS(X', Y') + x_m
2. x_m and y_n are different:
   => {LCS(X, Y) - x_m} is the LCS of X' and Y, and {LCS(X, Y) - y_n} is the LCS
      of X and Y'.
   => LCS(X, Y) = the longer one between LCS(X', Y) and LCS(X, Y')

i.e.,
Let LCS(X_i, Y_j) be the LCS for subproblems with the prefix of i characters of
X and j characters of Y, respectively, then
If x_i == y_j:
   LCS(X_i, Y_j) = LCS(X_i - x_i, Y_j - y_j) + 1
Else:
   LCS(X_i, Y_j) = max{LCS(X_i - x_i, Y_j), LCS(X_i, Y_j - y_j)}
"""

__author__ = 'Ziang Lu'


class IllegalArgumenError(ValueError):
    pass


def longest_common_subsequence(x, y):
    """
    Finds the longest common subsequence of the given strings in an improved
    bottom-up way.
    :param x: str
    :param y: str
    :return: list[char]
    """
    # Check whether the the input strings are None or empty
    if x is None or len(x) == 0 or y is None or len(y) == 0:
        raise IllegalArgumenError('The input strings should not be None or '
                                  'empty.')

    m, n = len(x), len(y)
    # Initialization
    subproblems = [[0 * (n + 1)] for i in range(m + 1)]
    # Bottom-up calculation
    for i in range(1, m + 1):
        for j in range(1, n + 1):
            x_curr, y_curr = x[i - 1], y[j - 1]
            if x_curr == y_curr:
                subproblems[i][j] = subproblems[i - 1][j - 1] + 1
            else:
                subproblems[i][j] = max(subproblems[i - 1][j],
                                        subproblems[i][j - 1])
    return _reconstruct_longest_common_subsequence(x, y,
                                                   subproblems=subproblems)
    # Overall running time complexity: O(mn)


def _reconstruct_longest_common_subsequence(x, y, subproblems):
    """
    Private helper function to reconstruct the longest common subsequence
    according to the optimal solution using backtracking.
    :param x: str
    :param y: str
    :return: list[char]
    """
    lcs = []
    i , j = len(x), len(y)
    while i >= 1 and j >= 1:
        x_curr, y_curr = x[i - 1], y[j - 1]
        if x_curr == y_curr:
            lcs.insert(0, x_curr)
            i -= 1
            j -= 1
        else:
            if subproblems[i][j] == subproblems[i - 1][j]:
                i -= 1
            else:
                j -= 1
    return lcs
    # Running time complexity: O(m + n)
