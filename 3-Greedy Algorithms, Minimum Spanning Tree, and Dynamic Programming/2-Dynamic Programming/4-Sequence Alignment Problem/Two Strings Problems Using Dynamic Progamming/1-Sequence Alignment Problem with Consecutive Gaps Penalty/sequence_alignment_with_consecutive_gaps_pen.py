#!usr/bin/env python3
# -*- coding: utf-8 -*-

"""
Similar to the sequence alignment problem, but the penalty for inserting k gaps
in a row becomes (a*k + b), with a and b being non-negative.

Algorithm: Similar to the ordinary sequence alignment problem, but need to know
whether the optimal solution to the subproblem ends with a gap, in order to
determine whether need to add a (if the optimal solution to the subproblem ends
with a gap) or (a + b) (if the optimal solution to the subproblem doesn't end
with a gap) to the total penalty.
=> We need to keep track of whether each subproblem solution ends with a gap.
   => We can only solve this problem in a bottom-up way.
"""

__author__ = 'Ziang Lu'

from typing import Dict, List


def sequence_alignment_with_consecutive_gaps_pen(
    x: str, y: str, a: int, b: int, pen_map: Dict[str, Dict[str, int]]
) -> List[str]:
    """
    Solves the sequence alignment (with consecutive gaps penalty) of the given
    two strings with the given penalties in a bottom-up way.
    :param x: str
    :param y: str
    :param a: int
    :param b: int
    :param pen_map: dict{str: dict{str: int}}
    :return: list[str]
    """
    # Check whether the input strings are None or empty
    if not x or not y:
        return []
    # Check whether the input a and b are non-negative
    if a < 0 or b < 0:
        return []
    # Check whether the input map is None
    if not pen_map:
        return []

    m, n = len(x), len(y)
    # Initialization
    subproblems = [[0] * (n + 1) for _ in range(m + 1)]
    sx_ends_with_gap = [[False] * (n + 1) for _ in range(m + 1)]
    sy_ends_with_gap = [[False] * (n + 1) for _ in range(m + 1)]
    for i in range(1, m + 1):
        subproblems[i][0] = a * i + b
        sy_ends_with_gap[i][0] = True
    for j in range(1, n + 1):
        subproblems[0][j] = a * j + b
        sx_ends_with_gap[0][j] = True
    # Bottom-up calculation
    for i in range(m + 1):
        for j in range(n + 1):
            x_curr, y_curr = x[i - 1], y[j - 1]
            result1 = subproblems[i - 1][j - 1] + pen_map[x_curr][y_curr]
            result2 = subproblems[i - 1][j] + a
            if not sy_ends_with_gap[i - 1][j]:
                result2 += b
            result3 = subproblems[i][j - 1] + a
            if not sx_ends_with_gap[i][j - 1]:
                result3 += b
            result = min(result1, result2, result3)
            subproblems[i][j] = result
            if result == result1:
                pass
            elif result == result2:
                sy_ends_with_gap[i][j] = True
            else:
                sx_ends_with_gap[i][j] = True
    return _reconstruct_optimal_alignment(x, y, a, b, pen_map, subproblems,
                                          sx_ends_with_gap=sx_ends_with_gap,
                                          sy_ends_with_gap=sy_ends_with_gap)
    # Overall running time complexity: O(mn)


def _reconstruct_optimal_alignment(
    x: str, y: str, a: int, b: int, pen_gap: Dict[str, Dict[str, int]],
    dp: List[List[int]], sx_ends_with_gap: List[List[bool]],
    sy_ends_with_gap: List[List[bool]]
) -> List[str]:
    """
    Private helper function to reconstruct the optimal alignment according to
    the optimal solution using backtracking.
    :param x: str
    :param y: str
    :param a: int
    :param b: int
    :param pen_gap: dict{str: dict{str: int}}
    :param dp: list[list[int]]
    :param sx_ends_with_gap: list[list[bool]]
    :param sy_ends_with_gap: list[list[bool]]
    :return: list[str]
    """
    sx, sy = '', ''
    i, j = len(x), len(y)
    while i >= 1 and j >= 1:
        x_curr, y_curr = x[i - 1], y[j - 1]
        result1 = dp[i - 1][j - 1] + pen_gap[x_curr][y_curr]
        result2 = dp[i - 1][j] + a
        if not sx_ends_with_gap[i - 1][j]:
            result2 += b
        result3 = dp[i][j - 1] + a
        if not sy_ends_with_gap[i][j - 1]:
            result3 += b
        result = min(result1, result2, result3)
        if result == result1:
            # Case 1: The final positions are x_i and y_j.
            sx = x_curr + sx
            sy = y_curr + sy
            i -= 1
            j -= 1
        elif result == result2:
            # Case 2: The final positions are x_i and a gap.
            sx = x_curr + sx
            sy = ' ' + sy
            i -= 1
        else:
            # Case 3: The final positions are a gap and y_j.
            sx = ' ' + sx
            sy = y_curr + sy
            j -= 1
    if i:
        sy = ' ' * i + sy
    else:
        sx = ' ' * j + sx
    return [sx, sy]
    # Running time complexity: O(m + n)
