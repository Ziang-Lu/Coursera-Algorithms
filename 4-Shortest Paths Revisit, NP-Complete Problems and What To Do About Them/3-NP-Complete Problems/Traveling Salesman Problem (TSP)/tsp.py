#!usr/bin/env python3
# -*- coding: utf-8 -*-

"""
Traveling Salesman Problem (TSP)

This problem can be solves in a brute-force way, namely n! permutations of the
vertices.
However, it can be solved smarter and faster.

Algorithm: (Dynamic programming)
Optimal Substructure Lemma:
Let P(v, S) be the shortest path from s to v that visits the vertices in S,
containing s and v, exactly once for each.
Then by plucking off the final hop (w, v), we form P'(w, S-v) that is the
shortest path from s to w that visits the vertices in {S - v}, containing s and
w, exactly once for each. Therefore, the recurrence is
P(v, S) = min_(w in S, w != v) {P'(w, S-v) + c_(w, v)}
(w即为shortest path中的倒数第二个vertex)
"""

__author__ = 'Ziang Lu'

import gc
import itertools
import math

INFINITY = 1000000


class IllegalArgumentError(ValueError):
    pass


class City(object):
    def __init__(self, x, y):
        """
        Constructur with parameters.
        :param x: float
        :param y: float
        """
        self._x = x
        self._y = y

    @property
    def x(self):
        """
        Accessor of x.
        :return: float
        """
        return self._x

    @property
    def y(self):
        """
        Accessor of y.
        :return: float
        """
        return self._y


def tsp(cities):
    """
    Solves the TSP of the given cities, assuming the first city is the source
    city.
    :param cities: list[City]
    :return: float
    """
    # Check whether the input cities is None or empty
    if cities is None or len(cities) == 0:
        raise IllegalArgumentError('The input cities should not be None or '
                                   'empty.')

    n, s = len(cities), 0

    # Initialization
    A = [{} for v in range(n)]
    # Convert the city subset that contains only the source city to a bit string
    S = _construct_bit_str(length=n, set_idxs=[s])
    for v in range(n):
        if v == s:
            A[s][S] = 0
        else:
            A[v][S] = INFINITY

    # Bottom-up calculation
    for m in range(2, n + 1):
        for subset in itertools.combinations(range(n), m):
            # The source city must be in the city subset.
            if s not in subset:
                continue
            # Convert the city subset to a bit string
            S = _construct_bit_str(length=n, set_idxs=subset)
            for v in range(n):
                # Let P(v, S) be the shortest path from s to v that visits the
                # vertices in S, containing s and v, exactly once for each.

                # Make sure S contains v
                if not _bit_is_set(S, v):
                    continue

                if v == s:
                    A[s][S] = INFINITY
                    continue

                # By plucking off the final hop (w, v), we form P'(w, S-v) that
                # is the shortest path from s to w that visits the vertices in
                # {S - v}, containing s and w, exactly once for each.
                # P(v, S) is the minimum among the possible choices of w in S,
                # w != v.
                S_wo_v = _change_bit(bit_str=S, i=v, setting=False)
                min_path_length = INFINITY
                for w in range(len(S)):
                    if _bit_is_set(S_wo_v, w) and w != v:
                        path_length = A[w][S_wo_v] + \
                            _euclidean_distance(cities[w], cities[v])
                        if path_length < min_path_length:
                            min_path_length = path_length
                A[v][S] = min_path_length

    # By now the algorithm only computes the shortest paths from s to each v
    # that visit all the vertices exactly once for each.
    # However to complete TSP, we still need to go back to s.
    S = _construct_bit_str(length=n, set_idxs=range(n))
    min_tour_length = INFINITY
    for w in range(n):
        if w != s:
            tour_length = A[w][S] + _euclidean_distance(cities[w], cities[s])
            if tour_length < min_tour_length:
                min_tour_length = tour_length
    return min_tour_length
    # Since there are O(n 2^n) subproblems, and each subproblem runs in O(n),
    # the overall running time complexity is O(n^2 2^n).
    # Note that the running time complexity is still exponential, but it's
    # better than brute-force (O(n!)).
    # Overall space complexity: O(n 2^n)


def _construct_bit_str(length, set_idxs):
    """
    Private helper function to construct a bit string of the given length, with
    the given indices set.
    :param length: int
    :param set_idxs: iterable
    :return: str
    """
    bit_str = '0' * length
    for set_idx in set_idxs:
        bit_str = _change_bit(bit_str=bit_str, i=set_idx, setting=True)
    return bit_str
    # Running time complexity: O(n), where n is the length of the set indices


def _change_bit(bit_str, i, setting):
    """
    Helper function to change the i-th bit in the given bit string.
    :param bit_str: str
    :param i: int
    :param setting: bool
    :return: str
    """
    if setting:
        return bit_str[:i] + '1' + bit_str[i + 1:]
    else:
        return bit_str[:i] + '0' + bit_str[i + 1:]
    # Running time complexty: O(1)


def _bit_is_set(bit_str, i):
    """
    Private helper function to check whether the i-th bit in the given bit
    string is set.
    :param bit_str: str
    :param i: int
    :return: bool
    """
    return bit_str[i] == '1'
    # Running time complexity: O(1)


def _euclidean_distance(city1, city2):
    """
    Private helper function to return the Euclidean distance between the given
    two cities.
    :param city1: City
    :param city2: City
    :return: float
    """
    return math.sqrt((city1.x - city2.x)**2 + (city1.y - city2.y)**2)
    # Running time complexity: O(1)


def tsp_optimized(cities):
    """
    Solves the TSP of the given cities, assuming the first city is the source
    city, with space optimization.
    :param cities: list[City]
    :return: float
    """
    # Check whether the input cities is None or empty
    if cities is None or len(cities) == 0:
        raise IllegalArgumentError('The input cities should not be None or '
                                   'empty.')

    n, s = len(cities), 0

    # Initialization
    # Space optimization: We only keep track of the subproblem solutions in the
    # previous out-most iteration.
    prev_m_subproblems = [{} for v in range(n)]
    S = _construct_bit_str(length=n, set_idxs=[s])
    for v in range(n):
        if v == s:
            prev_m_subproblems[s][S] = 0
        else:
            prev_m_subproblems[v][S] = INFINITY

    # Bottom-up calculation
    for m in range(2, n + 1):
        # Initialize the subproblems of size-m subsets
        curr_m_subproblems = [{} for v in range(n)]

        for subset in itertools.combinations(range(n), m):
            if s not in subset:
                continue
            S = _construct_bit_str(length=n, set_idxs=subset)
            for v in range(n):
                if not _bit_is_set(S, v):
                    continue

                if v == s:
                    curr_m_subproblems[s][S] = INFINITY
                    continue

                S_wo_v = _change_bit(bit_str=S, i=v, setting=False)
                min_path_length = INFINITY
                for w in range(len(S)):
                    if _bit_is_set(S_wo_v, w) and w != v:
                        path_length = prev_m_subproblems[w][S_wo_v] + \
                            _euclidean_distance(cities[w], cities[v])
                        if path_length < min_path_length:
                            min_path_length = path_length
                curr_m_subproblems[v][S] = min_path_length
        prev_m_subproblems = curr_m_subproblems
        # Explicitly run garbage collection
        gc.collect()

    # By now the algorithm only computes the shortest paths from s to each v
    # that visit all the vertices exactly once for each.
    # However to complete TSP, we still need to go back to s.
    S = _construct_bit_str(length=n, set_idxs=range(n))
    min_tour_length = INFINITY
    for w in range(n):
        if w != s:
            tour_length = prev_m_subproblems[w][S] + \
                _euclidean_distance(cities[w], cities[s])
            if tour_length < min_tour_length:
                min_tour_length = tour_length
    return min_tour_length
    # Overall running time complexity: (O(n^2 2^n))
    # Overall space complexity: O(n * (n choose k))
