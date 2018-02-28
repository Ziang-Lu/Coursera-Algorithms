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
S(i, x1, x2) = max(S(i - 1, x1, x2),
                   S(i - 1, x1 - w_i, x2) + v_i,
                   S(i - 1, x1, x2 - w_i) + v_i)
"""

__author__ = 'Ziang Lu'


class IllegalArgumentError(ValueError):
    pass


def two_knapsack_straightforward(vals, weights, capacity1, capacity2):
    """
    Solves the two-knapsack problem of the items with the given values and
    weights, and the given capacities, in a straightforward way.
    :param vals: list[int]
    :param weights: list[int]
    :param capacity1: int
    :param capacity2: int
    :return: list[set{int}]
    """
    # Check whether the input arrays are None or empty
    if vals is None or len(vals) == 0 or weights is None or len(weights) == 0:
        raise IllegalArgumentError('The input values and weights should not be '
                                   'None or empty.')
    # Check whether the input capacities are non-negative
    if capacity1 < 0 or capacity2 < 0:
        raise IllegalArgumentError('The input capacities should be '
                                   'non-negative.')

    n = len(vals)
    subproblem_sols = [[[-1 for x2 in range(capacity2 + 1)]
                        for x1 in range(capacity1 + 1)] for i in range(n)]
    _two_knapsack_helper(vals, weights, last_item=n - 1,
                         curr_capacity1=capacity1, curr_capacity2=capacity2,
                         subproblem_sols=subproblem_sols)
    return _reconstruct(vals, weights, capacity1, capacity2,
                        subproblem_sols=subproblem_sols)
    # With memoization, the overall running time complexity is O(n*W1*W2).


def _two_knapsack_helper(vals, weights, last_item, curr_capacity1,
                         curr_capacity2, subproblem_sols):
    """
    Private helper function to solve the two-knapsack subproblem with the given
    first items and the given capacities recursively.
    :param vals: list[int]
    :param weights: list[int]
    :param last_item: int
    :param curr_capacity1: int
    :param curr_capacity2: int
    :param subproblem_sols: list[list[list[int]]]
    :return: None
    """
    if subproblem_sols[last_item][curr_capacity1][curr_capacity2] != -1:
        return

    # Base case
    if last_item == 0:
        if weights[0] > curr_capacity1 and weights[0] > curr_capacity2:
            subproblem_sols[0][curr_capacity1][curr_capacity2] = 0
        else:
            subproblem_sols[0][curr_capacity1][curr_capacity2] = vals[0]
        return
    # Recursive case
    _two_knapsack_helper(vals, weights, last_item=last_item - 1,
                         curr_capacity1=curr_capacity1,
                         curr_capacity2=curr_capacity2,
                         subproblem_sols=subproblem_sols)
    result_without_last = \
        subproblem_sols[last_item - 1][curr_capacity1][curr_capacity2]
    if weights[last_item] > curr_capacity1:
        _two_knapsack_helper(vals, weights, last_item=last_item - 1,
                             curr_capacity1=curr_capacity1,
                             curr_capacity2=curr_capacity2 - weights[last_item],
                             subproblem_sols=subproblem_sols)
        result_with_last_in_2 = \
            subproblem_sols[last_item - 1][curr_capacity1][curr_capacity2 - weights[last_item]] + vals[last_item]
        subproblem_sols[last_item][curr_capacity1][curr_capacity2] = \
            max(result_without_last, result_with_last_in_2)
    elif weights[last_item] > curr_capacity2:
        _two_knapsack_helper(vals, weights, last_item=last_item - 1,
                             curr_capacity1=curr_capacity1 - weights[last_item],
                             curr_capacity2=curr_capacity2,
                             subproblem_sols=subproblem_sols)
        result_with_last_in_1 = \
            subproblem_sols[last_item - 1][curr_capacity1 - weights[last_item]][curr_capacity2] + vals[last_item]
        subproblem_sols[last_item][curr_capacity1][curr_capacity2] = \
            max(result_without_last, result_with_last_in_1)
    else:
        _two_knapsack_helper(vals, weights, last_item=last_item - 1,
                             curr_capacity1=curr_capacity1 - weights[last_item],
                             curr_capacity2=curr_capacity2,
                             subproblem_sols=subproblem_sols)
        result_with_last_in_1 = \
            subproblem_sols[last_item - 1][curr_capacity1 - weights[last_item]][curr_capacity2] + vals[last_item]
        _two_knapsack_helper(vals, weights, last_item=last_item - 1,
                             curr_capacity1=curr_capacity1,
                             curr_capacity2=curr_capacity2 - weights[last_item],
                             subproblem_sols=subproblem_sols)
        result_with_last_in_2 = \
            subproblem_sols[last_item - 1][curr_capacity1][curr_capacity2 - weights[last_item]] + vals[last_item]
        subproblem_sols[last_item][curr_capacity1][curr_capacity2] = \
            max(result_without_last, result_with_last_in_1,
                result_with_last_in_2)


def _reconstruct(vals, weights, capacity1, capacity2, subproblem_sols):
    """
    Private helper function to reconstruct the included items in knapsack-1 and
    knapsack-2 according to the optimal solution using backtracking.
    :param vals: list[int]
    :param weights: list[int]
    :param capacity1: int
    :param capacity2: int
    :param subproblem_sols: list[list[list[int]]]
    :return: list[set{int}]
    """
    included_items1, included_items2 = set(), set()
    curr_item, curr_capacity1, curr_capacity2 = \
        len(vals) - 1, capacity1, capacity2
    while curr_item >= 1:
        result_without_curr = \
            subproblem_sols[curr_item - 1][curr_capacity1][curr_capacity2]
        if weights[curr_item] > curr_capacity1:
            result_with_curr_in_2 = subproblem_sols[curr_item - 1][curr_capacity1][curr_capacity2 - weights[curr_item]] + vals[curr_item]
            if result_without_curr < result_with_curr_in_2:
                # Case 3: The current item is included in S2.
                included_items2.add(curr_item)
        elif weights[curr_item] > curr_capacity2:
            result_with_curr_in_1 = subproblem_sols[curr_item - 1][curr_capacity1 - weights[curr_item]][curr_capacity2] + vals[curr_item]
            if result_without_curr < result_with_curr_in_1:
                # Case 2: The current item is included in S1.
                included_items1.add(curr_item)
        else:
            result_with_curr_in_1 = subproblem_sols[curr_item - 1][curr_capacity1 - weights[curr_item]][curr_capacity2] + vals[curr_item]
            result_with_curr_in_2 = subproblem_sols[curr_item - 1][curr_capacity1][curr_capacity2 - weights[curr_item]] + vals[curr_item]
            result = max(result_without_curr, result_with_curr_in_1,
                         result_with_curr_in_2)
            if result == result_without_curr:
                # Case 1: The current item is not included.
                pass
            elif result == result_with_curr_in_1:
                # Case 2: The current item is included in S1.
                included_items1.add(curr_item)
                curr_capacity1 -= weights[curr_item]
            else:
                # Case 3: The current item is included in S2.
                included_items2.add(curr_item)
                curr_capacity2 -= weights[curr_item]
        curr_item -= 1
    if weights[0] <= curr_capacity1:
        included_items1.add(0)
    elif weights[0] <= curr_capacity2:
        included_items2.add(0)
    return [included_items1, included_items2]
    # Running time complexity: O(n)


def two_knapsack(vals, weights, capacity1, capacity2):
    """
    Solves the two-knapsack problem of the items with the given values and
    weights, and the given capacities, in an improved bottom-up way.
    :param vals: list[int]
    :param weights: list[int]
    :param capacity1: int
    :param capacity2: int
    :return: list[set{int}]
    """
    # Check whether the input arrays are None or empty
    if vals is None or len(vals) == 0 or weights is None or len(weights) == 0:
        raise IllegalArgumentError('The input values and weights should not be '
                                   'None or empty.')
    # Check whether the input capacities are non-negative
    if capacity1 < 0 or capacity2 < 0:
        raise IllegalArgumentError('The input capacities should be '
                                   'non-negative.')

    n = len(vals)
    # Initialization
    subproblem_sols = [[[0 for x2 in range(capacity2 + 1)]
                        for x1 in range(capacity1 + 1)] for i in range(n)]
    for x1 in range(capacity1 + 1):
        for x2 in range(capacity2 + 1):
            if weights[0] > x1 and weights[0] > x2:
                subproblem_sols[0][x1][x2] = 0
            else:
                subproblem_sols[0][x1][x2] = vals[0]
    # Bottom-up calculation
    for item in range(1, n):
        for x1 in range(capacity1 + 1):
            for x2 in range(capacity2 + 1):
                result_without_curr = subproblem_sols[item - 1][x1][x2]
                if weights[item] <= x1 and weights[item] <= x2:
                    result_with_curr_in_1 = \
                        subproblem_sols[item - 1][x1 - weights[item]][x2]
                    result_with_curr_in_2 = \
                        subproblem_sols[item - 1][x1][x2 - weights[item]]
                    subproblem_sols[item][x1][x2] = max(result_without_curr,
                                                        result_with_curr_in_1,
                                                        result_with_curr_in_2)
                elif weights[item] <= x1:
                    result_with_curr_in_1 = \
                        subproblem_sols[item - 1][x1 - weights[item]][x2]
                    subproblem_sols[item][x1][x2] = max(result_without_curr,
                                                        result_with_curr_in_1)
                elif weights[item] <= x2:
                    result_with_curr_in_2 = \
                        subproblem_sols[item - 1][x1][x2 - weights[item]]
                    subproblem_sols[item][x1][x2] = max(result_without_curr,
                                                        result_with_curr_in_2)
    return _reconstruct(vals, weights, capacity1, capacity2,
                        subproblem_sols=subproblem_sols)
