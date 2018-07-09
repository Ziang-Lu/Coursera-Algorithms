#!usr/bin/env python3
# -*- coding: utf-8 -*-

"""
Given a path graph, with each vertex having a non-negative weight, find the
maximum-weight independent set (MWIS), where an independent set is a subset of
the vertices, so that no two vertices are adjacent.

Algorithm: (Dynamic programming)
Denote S to be the optimal solution, i.e., the MWIS in the given path graph, and
v_n to be the last vertex of the given path graph.
Consider whether v_n is in S:
1. v_n is NOT in S:
   Let G' = G - v_n
   => S is the MWIS in G'.
   => S = the MWIS in G'
2. v_n is in S:
   => v_n-1 is NOT in S.
   Let G'' = G - v_n - v_n-1
   => {S - v_n} is the MWIS in G''.
   => S = the MWIS in G'' + v_n

i.e.,
Let S(i) be the optimal solution for the subproblem with the first i vertices in
the given path graph, then
S(i) = max{S(i - 1), S(i - 2) + v_i}
"""

__author__ = 'Ziang Lu'

from typing import List, Set


def find_mwis(weights: List[int]) -> Set[int]:
    """
    Finds the maximum-weight independent set (MWIS) in a path graph with the
    given weights in an improved bottom-up way.
    :param weights: list[int]
    :return: set{int}
    """
    # Check whether the input array is None or empty
    if not weights:
        return set()

    if len(weights) == 1:
        return {0}

    # Initialization
    subproblems = [weights[0], max(weights[0], weights[1])]
    # Bottom-up calculation
    for curr in range(2, len(weights)):
        subproblems.append(
            max(subproblems[curr - 1], subproblems[curr - 2] + weights[curr])
        )
    return _reconstruct_mwis(weights, subproblems)
    # Overall running time complexity: O(n)


def _reconstruct_mwis(weights: List[int], dp: List[int]) -> Set[int]:
    """
    Private helper function to reconstruct MWIS according to the optimal
    solution using backtracking.
    :param weights: list[int]
    :param dp: list[int]
    :return: set{int}
    """
    mwis = set()
    curr = len(dp) - 1
    while curr >= 2:
        if dp[curr - 1] >= dp[curr - 2] + weights[curr]:
            curr -= 1
        else:
            mwis.add(curr)
            curr -= 2
    if curr == 1:
        if weights[0] >= weights[1]:
            mwis.add(0)
        else:
            mwis.add(1)
    elif curr == 0:
        mwis.add(0)
    return mwis
    # Running time complexity: O(n)
