#!usr/bin/env python3
# -*- coding: utf-8 -*-

"""
Knapsack problem with greedy-based heuristic.

Motivation: Ideal items should have large value and small size.
=> Similar to the scheduling problem

Greedy algorithm:
1. Sort the items by v_i/w_i
2. Pack the items in this order. If one doesn't fit, skip it and continue.
   This single greedy heuristic can be arbitrarily bad relative to the optimal
   solution.
   Why? In the scheduling problem, this greedy algorithm is always correct
   because it doesn't have the restriction similar to the knapsack capacity.
   However in the knapsack problem., due to this restriction, the greedy
   algorithm is not always guaranteed to be correct.
   To fix this issue, we add the following second greedy heuristic.
3. Sort the items again simply by v_i
4. Same as 2
5. Return whichever is better between the two greedy packings
"""

__author__ = 'Ziang Lu'


class IllegalArgumentError(ValueError):
    pass


class Item(object):
    def __init__(self, idx, val, weight):
        """
        Constructor with parameter.
        :param idx: int
        :param val: int
        :param weight: int
        """
        self._idx = idx
        self._val = val
        self._weight = weight

    @property
    def idx(self):
        """
        Accessor of idx.
        :return: int
        """
        return self._idx

    @property
    def val(self):
        """
        Accessor of val.
        :return: int
        """
        return self._val

    @property
    def weight(self):
        """
        Accessor of weight.
        :return: int
        """
        return self._weight


def knapsack_greedy(vals, weights, cap):
    """
    Solves the knapsack problem of the items of the given values and weights,
    and the given capacity, using a greedy heuristic.
    :param vals: list[int]
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
    items = []
    for idx in range(n):
        items.append(idx, vals[idx], weights[idx])

    included_items1, total_val1 = _greedy_packing(
        items, cap, key_func=lambda item: item.val / item.weight)

    included_items2, total_val2 = _greedy_packing(
        items, cap, key_func=lambda item: item.val)

    if total_val1 >= total_val2:
        return included_items1
    else:
        return included_items2
    # Overall running time complexity: O(nlog n)


def _greedy_packing(items, cap, key_func):
    """
    Private helper function to pack the items using the given greedy heuristic.
    :param items: list[Item]
    :param cap: int
    :param key_func: func
    :return: set{int}
    """
    items.sort(key=key_func)
    included_items = set()
    total_val, total_weight = 0, 0
    for i in range(len(items)):
        if total_weight + items[i].weight > cap:
            continue
        included_items.add(items[i].idx)
        total_val += items[i].val
        total_weight += items[i].weight
    return included_items, total_val
    # Running time complexity: O(nlog n)
