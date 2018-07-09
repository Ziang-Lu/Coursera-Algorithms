#!usr/bin/env python3
# -*- coding: utf-8 -*-

"""
Similar to the knapsack problem, but with two capacities W1 and W2, and to
maximize sum(v in 1) and sum(v in 2), subject to sum(w in 1) <= W1 and
sum(w in 2) <= W2.

Algorithm: (Dynamic programming)
Denote S (S1 + S2) to be the optimal solution and item-n to be the last item.
Consider whether item-n is in S:
1. item-n is NOT in S:
   => S must be optimal among only the first (n - 1) items and capacities W1 and
      W2.
   => S = the optimal solution among only the first (n - 1) items and capacities
          W1 and W2
2. item-n is in S1:
   => {S1 - item-n, S2} must be optimal among only the first (n - 1) items and
      capacities (W1 - w_n) and W2 (i.e., the space is "reserved" for item-n in
      W1).
   => S = the optimal solution among only the first (n - 1) items and capacities
          (W1 - w_n) and W2 + v_n in W1
3. item-n is in S2:
   => {S1, S2 - item-n} must be optimal among only the first (n - 1) items and
      capacities W1 and (W2 - w_n) (i.e., the space is "reserved" for item-n in
      W2).
   => S = the optimal solution among only the first (n - 1) items and capacities
          W1 and (W2 - w_n) + v_n in W2

i.e.,
Let S(i, x1, x2) be the optimal solution for the subproblem among the first i
items and capacities x1 and x2, then
S(i, x1, x2) = max{S(i - 1, x1, x2),
                   S(i - 1, x1 - w_i, x2) + v_i,
                   S(i - 1, x1, x2 - w_i) + v_i}
"""

__author__ = 'Ziang Lu'

from typing import List, Set


def two_knapsack(vals: List[float], weights: List[int], cap1: int,
                 cap2: int) -> List[Set[int]]:
    """
    Solves the two-knapsack problem of the items with the given values and
    weights, and the given capacities, in an improved bottom-up way.
    :param vals: list[float]
    :param weights: list[int]
    :param cap1: int
    :param cap2: int
    :return: list[set{int}]
    """
    # Check whether the input arrays are None or empty
    if not vals:
        return []
    # Check whether the input capacities are non-negative
    if cap1 < 0 or cap2 < 0:
        return []

    n = len(vals)
    # Initialization
    subproblems = [
        [[0.0] * (cap2 + 1) for _ in range(cap1 + 1)]
        for _ in range(n)
    ]
    for x1 in range(cap1 + 1):
        for x2 in range(cap2 + 1):
            if weights[0] <= x1 or weights[0] <= x2:
                subproblems[0][x1][x2] = vals[0]
    # Bottom-up calculation
    for item in range(1, n):
        for x1 in range(cap1 + 1):
            for x2 in range(cap2 + 1):
                result_without_curr = subproblems[item - 1][x1][x2]
                if weights[item] <= x1 and weights[item] <= x2:
                    result_with_curr_in_1 = \
                        subproblems[item - 1][x1 - weights[item]][x2]
                    result_with_curr_in_2 = \
                        subproblems[item - 1][x1][x2 - weights[item]]
                    subproblems[item][x1][x2] = max(result_without_curr,
                                                    result_with_curr_in_1,
                                                    result_with_curr_in_2)
                elif weights[item] <= x1:
                    result_with_curr_in_1 = \
                        subproblems[item - 1][x1 - weights[item]][x2]
                    subproblems[item][x1][x2] = max(result_without_curr,
                                                    result_with_curr_in_1)
                elif weights[item] <= x2:
                    result_with_curr_in_2 = \
                        subproblems[item - 1][x1][x2 - weights[item]]
                    subproblems[item][x1][x2] = max(result_without_curr,
                                                    result_with_curr_in_2)
    return _reconstruct(vals, weights, cap1, cap2, subproblems)
    # Overall running time complexity: O(n*W1*W2), where W1 and W2 are the
    # knapsack capacities


def _reconstruct(vals: List[float], weights: List[int], cap1: int, cap2: int,
                 dp: List[List[List[float]]]) -> List[Set[int]]:
    """
    Private helper function to reconstruct the included items in knapsack-1 and
    knapsack-2 according to the optimal solution using backtracking.
    :param vals: list[int]
    :param weights: list[int]
    :param cap1: int
    :param cap2: int
    :param dp: list[list[list[float]]]
    :return: list[set{int}]
    """
    included1, included2 = set(), set()
    item, curr_cap1, curr_cap2 = len(vals) - 1, cap1, cap2
    while item >= 1:
        result_without_curr = dp[item - 1][curr_cap1][curr_cap2]
        if weights[item] > curr_cap1:
            result_with_curr_in_2 = \
                dp[item - 1][curr_cap1][curr_cap2 - weights[item]] + vals[item]
            if result_without_curr < result_with_curr_in_2:
                # Case 3: The current item is included in S2.
                included2.add(item)
        elif weights[item] > curr_cap2:
            result_with_curr_in_1 = \
                dp[item - 1][curr_cap1 - weights[item]][curr_cap2] + vals[item]
            if result_without_curr < result_with_curr_in_1:
                # Case 2: The current item is included in S1.
                included1.add(item)
        else:
            result_with_curr_in_1 = \
                dp[item - 1][curr_cap1 - weights[item]][curr_cap2] + vals[item]
            result_with_curr_in_2 = \
                dp[item - 1][curr_cap1][curr_cap2 - weights[item]] + vals[item]
            result = max(result_without_curr, result_with_curr_in_1,
                         result_with_curr_in_2)
            if result == result_without_curr:
                # Case 1: The current item is not included.
                pass
            elif result == result_with_curr_in_1:
                # Case 2: The current item is included in S1.
                included1.add(item)
                curr_cap1 -= weights[item]
            else:
                # Case 3: The current item is included in S2.
                included2.add(item)
                curr_cap2 -= weights[item]
        item -= 1
    if weights[0] <= curr_cap1:
        included1.add(0)
    elif weights[0] <= curr_cap2:
        included2.add(0)
    return [included1, included2]
    # Running time complexity: O(n)
