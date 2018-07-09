#!usr/bin/env python3
# -*- coding: utf-8 -*-

"""
Similar to the knapsack problem, but need to subject to sum(w) = W.

Algorithm: Same as the ordinary knapsack problem.
"""

__author__ = 'Ziang Lu'

from typing import List, Set


def knapsack_with_exact_size(vals: List[float], weights: List[int], cap: int) -> \
        Set[int]:
    """
    Solves the knapsack problem (with exact size) of the items with the given
    values and weights, and the given capacity, in an improved bottom-up way.
    :param vals: list[float]
    :param weights: list[int]
    :param cap: int
    :return: set{int}
    """
    # Check whether the input arrays are None or empty
    if not vals:
        return set()
    # Check whether the input capacity is non-negative
    if cap < 0:
        return set()

    n = len(vals)
    # Initialization
    subproblems = [[0.0] * (cap + 1) for _ in range(n)]
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
                    result_with_curr = subproblems[item - 1][x - weights[item]]\
                                       + vals[item]
                else:
                    result_with_curr = 0
                subproblems[item][x] = max(subproblems[item - 1][x],
                                           result_with_curr)
    return _reconstruct(vals, weights, cap, subproblems)
    # Overall running time complexity: O(nW), where W is the knapsack capacity


def _reconstruct(vals: List[float], weights: List[int], cap: int,
                 dp: List[List[float]]) -> Set[int]:
    """
    Private helper function to reconstruct the included items according to
    the optimal solution using backtracking.
    :param vals: list[float]
    :param weights: list[int]
    :param cap: int
    :param dp: list[list[float]]
    :return: set{int}
    """
    included = set()
    item, curr_cap = len(vals) - 1, cap
    while item >= 1:
        result_without_curr = dp[item - 1][curr_cap]
        if weights[item] <= curr_cap and dp[item - 1][curr_cap - weights[item]]\
                and result_without_curr \
                < dp[item - 1][curr_cap - weights[item]] + vals[item]:
            # Case 2: The current item is included.
            included.add(item)
            curr_cap -= weights[item]
        item -= 1
    if weights[0] == curr_cap:
        included.add(0)
    return included
    # Running time complexity: O(n)
