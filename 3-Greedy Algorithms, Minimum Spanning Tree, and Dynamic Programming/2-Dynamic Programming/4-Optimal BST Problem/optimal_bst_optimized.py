#!usr/bin/env python3
# -*- coding: utf-8

"""
Same as the optimal BST problem.

Algorithm: Similar to the ordinary optimal BST problem, but with Knuth's
optimization.
The key point is the monotony of the roots:
root(i, j-1) <= root(i, j) <= root(i+1, j)
[Proof ?????]
Thus, by keeping track of the root, we can significantly reduce the number of
brute-force searches when searching for the root of a subproblem.
"""

__author__ = 'Ziang Lu'

import sys


class IllegalArgumentError(ValueError):
    pass


def find_optimal_bst(weights):
    """
    Finds the optimal BST for items with the given weight distribution, and
    calculates its weighted search time (WST), in an improved bottom-up way.
    :param weights: list[int]
    :return: int
    """
    # Check whether the input array is None or empty
    if weights is None or len(weights) == 0:
        raise IllegalArgumentError('The input weight distribution is should be '
                                   'None or empty.')

    n = len(weights)
    # Initialization
    roots = [[0] * n for i in range(n)]
    subproblems = [[0] * n for i in range(n)]
    n_item = 1
    for i in range(n):
        roots[i][i] = i
        subproblems[i][i] = weights[i]
    # Bottom-up calculation
    for n_item in range(2, n + 1):
        for start in range(0, n - n_item + 1):
            end = min(start + n_item - 1, n - 1)
            root_with_min_wst, min_wst = -1, sys.maxsize
            weight_sum = 0
            for k in range(start, end + 1):
                weight_sum += weights[k]
            # Use Knuth's optimization:
            # root(i, j-1) <= root(i, j) <= root(i+1, j)
            for r in range(roots[start][end - 1], roots[start + 1][end] + 1):
                if start > r - 1:
                    left_subtree_wst = 0
                else:
                    left_subtree_wst = subproblems[start][r - 1]
                if r + 1 > end:
                    right_subtree_wst = 0
                else:
                    right_subtree_wst = subproblems[r + 1][end]
                curr_wst = left_subtree_wst + right_subtree_wst + weight_sum
                if curr_wst < min_wst:
                    root_with_min_wst = r
                    min_wst = curr_wst
            roots[start][end] = root_with_min_wst
            subproblems[start][end] = min_wst
    return subproblems[start][end]
    # Overall running time complexity: O(n^2) [Analysis ?????]
