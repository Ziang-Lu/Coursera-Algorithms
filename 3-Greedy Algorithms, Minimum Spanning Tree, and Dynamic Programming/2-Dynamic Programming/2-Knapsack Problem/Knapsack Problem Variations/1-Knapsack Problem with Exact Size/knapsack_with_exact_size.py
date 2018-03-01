#!usr/bin/env python3
# -*- coding: utf-8 -*-

"""
Similar to the knapsack problem, but need to subject to sum(w) = W.

Algorithm: Same as the ordinary knapsack problem.
"""

__author__ = 'Ziang Lu'


class IllegalArgumentError(ValueError):
    pass


def knapsack_with_exact_size_straightforward(vals, weights, cap):
    """
    Solves the knapsack problem (with exact size) of the items with the given
    values and weights, and the given capacity, in a straightforward way.
    :param vals: list[int]
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
    subproblems = [[-1 for x in range(cap + 1)] for i in range(n)]
    _knapsack_with_exact_size_helper(vals, weights, last_item=n - 1,
                                     curr_cap=cap, subproblems=subproblems)
    return _reconstruct(vals, weights, cap, subproblems=subproblems)
    # With memoization, the overall running time complexity is O(nW), where W is
    # the knapsack capacity.


def _knapsack_with_exact_size_helper(vals, weights, last_item, curr_cap,
                                     subproblems):
    """
    Private helper function to solve the knapsack problem (with exact size) with
    the given first items and the given capacity recursively.
    :param vals: list[int]
    :param weights: list[int]
    :param last_item: int
    :param curr_cap: int
    :return: None
    """
    if subproblems[last_item][curr_cap] != -1:
        return

    # Base case
    if last_item == 0:
        if weights[0] == curr_cap:
            subproblems[0][curr_cap] = vals[0]
        else:
            subproblems[0][curr_cap] = 0
        return
    # Recursive case
    if weights[last_item] > curr_cap:
        _knapsack_with_exact_size_helper(vals, weights, last_item=last_item - 1,
                                         curr_cap=curr_cap,
                                         subproblems=subproblems)
        subproblems[last_item][curr_cap] = subproblems[last_item - 1][curr_cap]
    else:
        _knapsack_with_exact_size_helper(vals, weights, last_item=last_item - 1,
                                         curr_cap=curr_cap,
                                         subproblems=subproblems)
        result_without_last = subproblems[last_item - 1][curr_cap]
        _knapsack_with_exact_size_helper(vals, weights, last_item=last_item - 1,
                                         curr_cap=curr_cap - weights[last_item],
                                         subproblems=subproblems)
        # Note that if subproblem has no solution, the current last item should
        # not be included either.
        if subproblems[last_item - 1][curr_cap - weights[last_item]]:
            result_with_last = \
                subproblems[last_item - 1][curr_cap - weights[last_item]] + vals[last_item]
        else:
            result_with_last = 0
        subproblems[last_item][curr_cap] = max(result_without_last,
                                               result_with_last)


def _reconstruct(vals, weights, cap, subproblems):
    """
    Private helper function to reconstruct the included items according to
    the optimal solution using backtracking.
    :param vals: list[int]
    :param weights: list[int]
    :param cap: int
    :param subproblems: list[list[int]]
    :return: set{int}
    """
    included_items = set()
    curr_item, curr_cap = len(vals) - 1, cap
    while curr_item >= 1:
        result_without_curr = subproblems[curr_item - 1][curr_cap]
        result_with_curr = \
            subproblems[curr_item - 1][curr_cap - weights[curr_item]] + \
            vals[curr_item]
        if weights[curr_item] <= curr_cap \
                and subproblems[curr_item - 1][curr_cap - weights[curr_item]] \
                and result_without_curr < result_with_curr:
            # Case 2: The current item is included.
            included_items.add(curr_item)
            curr_cap -= weights[curr_item]
        curr_item -= 1
    if weights[0] == curr_cap:
        included_items.add(0)
    return included_items
    # Running time complexity: O(n)


def knapsack_with_exact_size(vals, weights, cap):
    """
    Solves the knapsack problem (with exact size) of the items with the given
    values and weights, and the given capacity, in an improved bottom-up way.
    :param vals: list[int]
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
    subproblems = [[0 for x in range(cap + 1)] for i in range(n)]
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
