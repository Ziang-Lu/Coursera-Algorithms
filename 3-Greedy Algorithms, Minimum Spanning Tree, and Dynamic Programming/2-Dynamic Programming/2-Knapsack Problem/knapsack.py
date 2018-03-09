#!usr/bin/env python3
# -*- coding: utf-8 -*-

"""
Given n items, each item with a non-negative value v and a non-negative integral
size (weight) w, and a non-negative integral capacity W, select a subset of the
items, that maximizes sum(v), subject to sum(w) <= W.

Algorithm: (Dynamic programming)
Denote S to be the optimal solution and item-n to be the last item.
Consider whether item-n is in S:
1. item-n is NOT in S:
   => S must be optimal among only the first (n - 1) items and capacity W.
   => S = the optimal solution among the first (n - 1) items and capacity W
2. item-n is in S:
   => {S - item-n} must be optimal among only the first (n - 1) items and the
      residual capacity (W - w_n) (i.e., the space is "reserved" for item-n).
   => S = the optimal solution among the first (n - 1) items and the residual
          capacity (W - w_n) + item-n

i.e.,
Let S(i, x) be the optimal solution for the subproblem among the first i items
and capacity x, then
S(i, x) = max{S(i - 1, x), S(i - 1, x - w_i) + v_i}
"""

__author__ = 'Ziang Lu'


class IllegalArgumentError(ValueError):
    pass


def knapsack(vals, weights, cap):
    """
    Solves the knapsack problem of the items with the given values and weights,
    and the given capacity, in an improved bottom-up way.
    :param vals: list[float]
    :param weights: list[int]
    :param cap: int
    :return: set{int}
    """
    # Check whether the input arrays are None or empty
    if vals is None or len(weights) == 0 \
            or weights is None or len(weights) == 0:
        raise IllegalArgumentError('The input values and weights should not be '
                                   'null or empty.')
    # Check whether the input capacity is non-negative
    if cap < 0:
        raise IllegalArgumentError('The input capacity should be non-negative.')

    n = len(vals)
    # Initialization
    subproblems = [[0] * (cap + 1) for i in range(n)]
    for x in range(cap + 1):
        if weights[0] <= x:
            subproblems[0][x] = vals[0]
    # Bottom-up calculation
    for item in range(1, n):
        for x in range(cap + 1):
            if weights[item] > x:
                subproblems[item][x] = subproblems[item - 1][x]
            else:
                result_without_curr = subproblems[item - 1][x]
                result_with_curr = \
                    subproblems[item - 1][x - weights[item]] + vals[item]
                subproblems[item][x] = max(result_without_curr,
                                           result_with_curr)
    return _reconstruct(vals, weights, cap, subproblems=subproblems)


def _reconstruct(vals, weights, cap, subproblems):
    """
    Private helper function to reconstruct the included items according to the
    optimal solution using backtracking.
    :param vals: list[float]
    :param weights: list[int]
    :param cap: int
    :param subproblems: list[list[float]]
    :return: set{int}
    """
    included_items = set()
    curr_item, curr_cap = len(vals) - 1, cap
    while curr_item >= 1:
        result_without_curr = subproblems[curr_item - 1][curr_cap]
        if weights[curr_item] <= curr_cap \
                and result_without_curr \
                < (subproblems[curr_item - 1][curr_cap - weights[curr_item]] +
                   vals[curr_item]):
            # Case 2: The current item is included.
            included_items.add(curr_item)
            curr_cap -= weights[curr_item]
        curr_item -= 1
    if weights[0] <= curr_cap:
        included_items.add(0)
    return included_items
    # Running time complexity: O(n)
