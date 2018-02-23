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
      (Proof:
      Assume S* is the MWIS in G' (S* > S), then in G, we still have S* > S, so
      S is not the optimal solution in G. [CONTRADICTION])
   => S = the MWIS in G'
2. v_n is in S:
   => v_n-1 is NOT in S.
   Let G'' = G - v_n - v_n-1
   => {S - v_n} is the MWIS in G''.
      (Proof:
      Assume S* is the MWIS in G'' (S* > {S - v_n}), then in G, we have
      S* + v_n > S, so S is not the optimal solution in G. [CONTRADICTION])
   => S = the MWIS in G'' + v_n

i.e.,
Let S(i) be the optimal solution for the subproblem with the first i vertices in
the given path graph, then
S(i) = max(S(i - 1), S(i - 2) + v_i)
"""

__author__ = 'Ziang Lu'


class IllegalArgumentError(ValueError):
    pass


def find_mwis_straightforward(weights):
    """
    Finds the maximum-weight independent set (MWIS) in a path graph with the
    given weights in a straightforward way.
    :param weights: list[int]
    :return: set{int}
    """
    # Check whether the input array is None or empty
    if weights is None or len(weights) == 0:
        raise IllegalArgumentError('The input weights should not be None or '
                                   'empty.')

    if len(weights) == 1:
        return set([1])

    subproblem_sols = [-1 for i in range(len(weights))]
    _find_mwis_helper(weights, last_idx=len(weights) - 1,
                      subproblem_sols=subproblem_sols)
    return _reconstruct_mwis(weights=weights, subproblem_sols=subproblem_sols)
    # With memoization, the overall running time complexity is O(n).


def _find_mwis_helper(weights, last_idx, subproblem_sols):
    """
    Private helper function to find the MWIS in the given sub path graph with
    the given weights recursively.
    :param weights: list[int]
    :param last_idx: int
    :param subproblem_sols: list[int]
    :return: None
    """
    if weights[last_idx] != -1:
        return

    # Base case 1: Only the left-most vertex
    if last_idx == 0:
        subproblem_sols[0] = weights[0]
        return
    # Base case 2: Only the left-most two vertices
    if last_idx == 1:
        subproblem_sols[1] = max(weights[0], weights[1])
        return

    # Recursive case
    _find_mwis_helper(weights, last_idx=last_idx - 1,
                      subproblem_sols=subproblem_sols)
    result_without_last = subproblem_sols[last_idx - 1]
    _find_mwis_helper(weights, last_idx=last_idx - 2,
                      subproblem_sols=subproblem_sols)
    result_with_last = subproblem_sols[last_idx - 2] + weights[last_idx]
    subproblem_sols[last_idx] = max(result_without_last, result_with_last)


def _reconstruct_mwis(weights, subproblem_sols):
    """
    Private helper function to reconstruct MWIS from the solutions of the
    subproblems.
    :param weights: list[int]
    :param subproblem_sols: list[int]
    :return: set{int}
    """
    mwis = set()
    i = len(subproblem_sols) - 1
    while i >= 2:
        if subproblem_sols[i - 1] >= subproblem_sols[i - 2] + weights[i]:
            i -= 1
        else:
            mwis.add(i + 1)
            i -= 2
    if i == 1:
        if weights[0] >= weights[1]:
            mwis.add(1)
        else:
            mwis.add(2)
    elif i == 0:
        mwis.add(1)
    return mwis
    # Running time complexity: O(n)


def find_mwis(weights):
    """
    Finds the maximum-weight independent set (MWIS) in a path graph with the
    given weights in an improved bottom-up way.
    :param weights: list[int]
    :return: set{int}
    """
    # Check whether the input array is None or empty
    if weights is None or len(weights) == 0:
        raise IllegalArgumentError('The input weights should not be None or '
                                   'empty.')

    if len(weights) == 1:
        return set([1])

    subproblem_sols = [weights[0], max(weights[0], weights[1])]
    for i in range(2, len(weights)):
        subproblem_sols[i] = max(subproblem_sols[i - 1],
                                 subproblem_sols[i - 2] + weights[i])
    return _reconstruct_mwis(weights=weights, subproblem_sols=subproblem_sols)
    # Overall running time complexity: O(n)
