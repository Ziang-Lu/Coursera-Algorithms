#!usr/bin/env python3
# -*- coding: utf-8 -*-

"""
Given n items, each item with a non-negative value v and a non-negative integral
size (weight) w, and a non-negative and integral capacity W, select a subset of
the items, that maximizes sum(v), subject to sum(w) <= W.

Algorithm: (Dynamic programming)
Denote S to be the optimal solution, i.e., and item-n to be the last item.
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
S(i, x) = max(S(i - 1, x), S(i - 1, x - w_i) + item-i)
"""

__author__ = 'Ziang Lu'


class IllegalArgumentError(ValueError):
    pass


def knapsack_straightforward(vals, weights, capacity):
    """
    Solves the knapsack problem of the items with the given values and weights,
    and the given capacity, in a straightforward way.
    :param vals: list[int]
    :param weights: list[int]
    :param capacity: int
    :return: set{int}
    """
    # Check whether the input arrays are None or empty
    if vals is None or len(weights) == 0 or weights is None or len(weights) == 0:
        raise IllegalArgumentError('The input values and weights should not be '
                                   'null or empty.')
    # Check whether the input capacity is non-negative
    if capacity < 0:
        raise IllegalArgumentError('The input capacity should be non-negative.')

    n = len(vals)
    subproblem_sols = [[-1 for x in range(capacity + 1)] for i in range(n)]
    _knapsack_helper(vals, weights, last_item=n - 1, curr_capacity=capacity,
                     subproblem_sols=subproblem_sols)
    return _reconstruct(vals, weights, capacity,
                        subproblem_sols=subproblem_sols)
    # With memoization, the overall running time complexity is O(nW), where W is
    # the knapsack capacity


def _knapsack_helper(vals, weights, last_item, curr_capacity, subproblem_sols):
    """
    Private helper function to solve the knapsack subproblem with the given
    first items and the given capacity recursively.
    :param vals: list[int]
    :param weights: list[int]
    :param last_item: int
    :param curr_capacity: int
    :param subproblem_sols: list[list[int]]
    :return: None
    """
    if subproblem_sols[last_item][curr_capacity] != -1:
        return

    # Base case
    if last_item == 0:
        if weights[0] > curr_capacity:
            subproblem_sols[0][curr_capacity] = 0
        else:
            subproblem_sols[0][curr_capacity] = vals[0]
        return
    # Recursive case
    if weights[last_item] > curr_capacity:
        _knapsack_helper(vals, weights, last_item=last_item - 1,
                         curr_capacity=curr_capacity,
                         subproblem_sols=subproblem_sols)
        subproblem_sols[last_item][curr_capacity] = subproblem_sols[last_item - 1][curr_capacity]
    else:
        _knapsack_helper(vals, weights, last_item=last_item - 1,
                         curr_capacity=curr_capacity,
                         subproblem_sols=subproblem_sols)
        result_without_last = subproblem_sols[last_item - 1][curr_capacity]
        _knapsack_helper(vals, weights, last_item=last_item - 1,
                         curr_capacity=curr_capacity - weights[last_item],
                         subproblem_sols=subproblem_sols)
        result_with_last = \
            subproblem_sols[last_item - 1][curr_capacity - weights[last_item]] \
            + vals[last_item]
        subproblem_sols[last_item][curr_capacity] = max(result_without_last,
                                                        result_with_last)


def _reconstruct(vals, weights, capacity, subproblem_sols):
    """
    Private helper function to reconstruct the included items according to the
    optimal solution using backtracking.
    :param vals: list[int]
    :param weights: list[int]
    :param capacity: int
    :param subproblem_sols: list[list[int]]
    :return: set{int}
    """
    included_items = set()
    curr_item, curr_capacity = len(vals) - 1, capacity
    while curr_item >= 1:
        result_without_curr = subproblem_sols[curr_item - 1][curr_capacity]
        result_with_curr = \
            subproblem_sols[curr_item - 1][curr_capacity - weights[curr_item]] \
            + vals[curr_item]
        if weights[curr_item] <= curr_capacity and \
                result_without_curr < result_with_curr:
            # Case 2: The current item is included.
            included_items.add(curr_item)
            curr_capacity -= weights[curr_item]
        curr_item -= 1
    if weights[0] <= curr_capacity:
        included_items.add(0)
    return included_items
    # Running time complexity: O(n)
