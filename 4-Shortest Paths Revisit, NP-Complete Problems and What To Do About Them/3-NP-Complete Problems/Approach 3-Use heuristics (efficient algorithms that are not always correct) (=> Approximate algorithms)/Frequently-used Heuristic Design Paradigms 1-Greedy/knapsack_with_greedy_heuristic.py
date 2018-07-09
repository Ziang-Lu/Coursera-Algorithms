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

from typing import Callable, List, Set, Tuple


class Item(object):
    __slots__ = ['_idx', '_val', '_weight']

    def __init__(self, idx: int, val: float, weight: int):
        """
        Constructor with parameter.
        :param idx: int
        :param val: float
        :param weight: int
        """
        self._idx = idx
        self._val = val
        self._weight = weight

    @property
    def idx(self) -> int:
        """
        Accessor of idx.
        :return: int
        """
        return self._idx

    @property
    def val(self) -> float:
        """
        Accessor of val.
        :return: int
        """
        return self._val

    @property
    def weight(self) -> int:
        """
        Accessor of weight.
        :return: int
        """
        return self._weight


def knapsack_greedy(vals: List[float], weights: List[int],
                    cap: int) -> Set[int]:
    """
    Solves the knapsack problem of the items of the given values and weights,
    and the given capacity, using a greedy heuristic.
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

    items = [Item(i, info[0], info[1]) for i, info in enumerate(zip(vals, weights))]

    included1, total_val1 = _greedy_packing(
        items, cap, func=lambda x: x.val / x.weight
    )

    included2, total_val2 = _greedy_packing(items, cap, func=lambda x: x.val)

    if total_val1 >= total_val2:
        return included1
    else:
        return included2
    # Overall running time complexity: O(nlog n)


def _greedy_packing(items: List[Item], cap: int,
                    func: Callable) -> Tuple[Set[int], int]:
    """
    Private helper function to pack the items using the given greedy heuristic.
    :param items: list[Item]
    :param cap: int
    :param func: func
    :return: set{int}, int
    """
    items.sort(key=func)
    included = set()
    total_val, total_weight = 0, 0
    for item in items:
        if total_weight + item.weight > cap:
            continue
        included.add(item.idx)
        total_val += item.val
        total_weight += item.weight
    return included, total_val
    # Running time complexity: O(nlog n)
