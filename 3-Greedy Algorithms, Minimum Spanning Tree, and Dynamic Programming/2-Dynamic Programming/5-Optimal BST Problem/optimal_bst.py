#!usr/bin/env python3
# -*- coding: utf-8

"""
Given items 1, 2, ..., n (assume in sorted order), with positive frequencies
p_1, p_2, ..., p_n, compute a valid BST that minimizes the weighted search time
(WST):
C(T) = sum(p_i * [search time for item-i])
where the search time for item-i is defined to be the number of nodes you have
to visit until you find it (inclusive).

Optimal substructure lemma:
Suppose an optimal BST for items 1, 2, ..., n T HAS ROOT r, left subtree T_L and
right subtree T_R, then T_L is optimal for items 1, 2, ..., r-1 and T_R is
optimal for items r+1, r+2, ..., n
-Proof:
Suppose T_L is not optimal for items 1, 2, ..., r-1 with C(T_L*) < C(T_L).
(The other case is similar.)
Thus, we can obtain a T* from T by replacing the left subtree by T*.
=>
C(T) = sum(p_i * [search time for item-i in T]
     = C(T_L) + sum(p_i * 1) for 1<=i<=r-1 + p_r * 1 + C(T_R) + sum(p_i * 1) for
       r+1<=i<=n)
     = C(T_L) + C(T_R) + sum(p_i) for 1<=i<=n
     < C(T_L*) + C(T_R) + const = C(T*)
Thus by proof by contradiction, T_L must be optimal for items 1, 2, ..., r-1.

Algorithm: (Dynamic programming)
Suppose 1<=i<=j<=n,
C(T_{i,j}) = min_(i<=r<=j) {C(T_L) + C(T_R) + sum(p_k) for i<=k<=j}
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
    subproblems = [[0] * n for i in range(n)]
    n_item = 1
    for i in range(n):
        subproblems[i][i] = weights[i]
    # Bottom-up calculation
    for n_item in range(2, n + 1):
        for start in range(0, n - n_item + 1):
            end = min(start + n_item - 1, n - 1)
            min_wst = sys.maxsize
            weight_sum = 0
            for k in range(start, end + 1):
                weight_sum += weights[k]
            for r in range(start, end + 1):
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
                    min_wst = curr_wst
            subproblems[start][end] = min_wst
    return subproblems[start][end]
    # Overall running time complexity: O(n^3)
