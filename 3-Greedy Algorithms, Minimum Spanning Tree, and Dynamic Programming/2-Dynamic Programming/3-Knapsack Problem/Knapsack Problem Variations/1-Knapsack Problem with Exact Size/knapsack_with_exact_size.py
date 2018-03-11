#!usr/bin/env python3
# -*- coding: utf-8 -*-

"""
Similar to the knapsack problem, but need to subject to sum(w) = W.

Algorithm: Same as the ordinary knapsack problem.
"""

__author__ = 'Ziang Lu'


class IllegalArgumentError(ValueError):
    pass


def knapsack_with_exact_size(vals, weights, cap):
    """
    Solves the knapsack problem (with exact size) of the items with the given
    values and weights, and the given capacity, in an improved bottom-up way.
    :param vals: list[float]
    :param weights: list[int]
    :param cap: int
    :return: set{int}
    """
    # Check whether the input arrays are None or empty
    if vals is None or len(weights) == 0 or weights is None or len(weights) == 0:
        raise IllegalArgumentError('The input values and weights should not be '
                                   'null or empty.')
    # Check whether the input capacity is non-negative
    if cap < 0:
        raise IllegalArgumentError('The input capacity should be non-negative.')

    n = len(vals)
    # Initialization
    subproblems = [[0] * (cap + 1) for i in range(n)]
    for x in range(cap + 1):
        if weights[0] == x:
            subproblems[0][x] = vals[0]
    # Bottom-up calculation
    for item in range(1, n):
        for x in range(cap + 1):
            if weights[item] > x:
                subproblems[item][x] = subproblems[item - 1][x]
            else:
                # Note that if subproblem has no solution, then the current item
                # should not be included either.
                if subproblems[item - 1][x - weights[item]]:
                    result_with_curr = \
                        subproblems[item - 1][x - weights[item]] + vals[item]
                else:
                    result_with_curr = 0
                subproblems[item][x] = max(subproblems[item - 1][x],
                                           result_with_curr)
    return _reconstruct(vals, weights, cap, subproblems=subproblems)
    # Overall running time complexity: O(nW), where W is the knapsack capacity


def _reconstruct(vals, weights, cap, subproblems):
    """
    Private helper function to reconstruct the included items according to
    the optimal solution using backtracking.
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
                and subproblems[curr_item - 1][curr_cap - weights[curr_item]] \
                and result_without_curr \
                < (subproblems[curr_item - 1][curr_cap - weights[curr_item]] +
                   vals[curr_item]):
            # Case 2: The current item is included.
            included_items.add(curr_item)
            curr_cap -= weights[curr_item]
        curr_item -= 1
    if weights[0] == curr_cap:
        included_items.add(0)
    return included_items
    # Running time complexity: O(n)
