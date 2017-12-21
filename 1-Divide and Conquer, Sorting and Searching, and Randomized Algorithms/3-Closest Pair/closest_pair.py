#!usr/bin/env python3
# -*- coding: utf-8 -*-

"""
Given an array of points, find the closest pair.

Naive implementation: O(n^2)
"""

__author__ = 'Ziang Lu'

import copy
import math


def find_closest_pair_1d(points):
    """
    Finds the closest pair in the given 1D points.
    :param points: list[float]
    :return: list[float]
    """
    # Check whether the input point list is null or empty
    if points is None or len(points) == 0:
        print('The input list should not be None or empty.')
        return []

    if len(points) == 1:
        print('The length of the input list should be at least 2.')
        return []

    # Sort the points   [O(nlog n)]
    points.sort()
    # The closest pair must be adjacent in the ordering.

    # Iterate over the adjacent pairs, and find the closest pair   [O(n)]
    p1, p2 = points[0], points[1]
    min_distance = points[1] - points[0]
    for i in range(1, len(points) - 1):
        if points[i + 1] - points[i] < min_distance:
            p1, p2 = points[i], points[i + 1]
            min_distance = points[i + 1] - points[i]
    return [p1, p2]


class Point(object):
    def __init__(self, x, y):
        """
        Constructor with parameter.
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

    def distance(self, another):
        """
        Calculates the Euclidean distance between this point and the given
        point.
        :param another: Point
        :return: float
        """
        return math.sqrt((self._x - another.x)**2 + (self._y - another.y)**2)

    def __repr__(self):
        """
        String representation of this point.
        :return: str
        """
        return 'Point(x=%s, y=%s)' % (self._x, self._y)


def find_closest_pair_2d(points):
    """
    Finds the closest pair in the given 2D points.
    :param points: list[Point]
    :return: list[Point]
    """
    # Check whether the input point list is None or empty
    if points is None or len(points) == 0:
        return []

    if len(points) == 1:
        return []

    # Pre-processing
    # Make a copy of the points sorted by x (Px)   [O(nlog n)]
    Px = copy.deepcopy(points)
    Px = sorted(Px, key=lambda p: p.x)
    # Make a copy of the points sorted by y (Py)   [O(nlog n)]
    Py = copy.deepcopy(points)
    Py = sorted(Py, key=lambda p: p.y)

    return _find_closest_pair_2d_helper(Px, Py)


def _find_closest_pair_2d_helper(Px, Py):
    """
    Private helper function to find the closest pair in the given 2D points
    recursively.
    :param Px: list[Point]
    :param Py: list[Point]
    :return: list[Point]
    """
    num_of_points = len(Px)
    # Base case 1: 2 points
    if num_of_points == 2:
        return Px
    # Base case 2: 3 points
    if num_of_points == 3:
        distance1, distance2, distance3 = \
            Px[0].distance(Px[1]), Px[1].distance(Px[2]), Px[2].distance(Px[0])
        min_distance = min(distance1, distance2, distance3)
        if min_distance == distance1:
            return [Px[0], Px[1]]
        elif min_distance == distance2:
            return [Px[1], Px[2]]
        else:
            return [Px[2], Px[0]]

    # Recursive case
    # [Divide]
    # Let Q be the left half of P, and R be the right half of P
    Qx, Qy = Px[:num_of_points // 2], []
    Rx, Ry = Px[num_of_points // 2:], []
    # To create Qy and Ry, iterate over Py: if x is smaller than or equal to the
    # threshold, put the point in Qy; otherwise put it in Ry
    x_threshold = Qx[-1].x
    for p in Py:
        if p.x <= x_threshold:
            Qy.append(p)
        else:
            Ry.append(p)
    # [Conquer]
    l_closest_pair = _find_closest_pair_2d_helper(Qx, Qy)
    r_closet_pair = _find_closest_pair_2d_helper(Rx, Ry)
    # Combine the results
    l_closest_distance, r_closest_distance = \
        l_closest_pair[0].distance(l_closest_pair[1]), \
        r_closet_pair[0].distance(r_closet_pair[1])
    delta = l_closest_distance
    delta_pair = l_closest_pair
    if l_closest_distance > r_closest_distance:
        delta = r_closest_distance
        delta_pair = r_closet_pair
    closer_split_pair = _find_closer_split_pair(Px, Py, x_threshold=x_threshold,
                                                delta=delta)
    if closer_split_pair is not None:
        return closer_split_pair
    else:
        return delta_pair
    # T(n) = 2T(n / 2) + O(n)
    # a = 2, b = 2, d = 1
    # According to Master Method, the running time complexity is O(nlog n)


def _find_closer_split_pair(Px, Py, x_threshold, delta):
    """
    Helper function to find the closest closer split pair in the given 2D
    points.
    :param Px: list[Point]
    :param Py: list[Point]
    :param x_threshold: float
    :param delta: float
    :return: list[Point]
    """
    # Filtering
    lower_bound, upper_bound = x_threshold - delta, x_threshold + delta
    # Let Sy be the points of P with x within the range
    Sy = []
    # To create Sy, iterate over Py: if x is within the range, put the point in
    # Sy
    for p in Py:
        if lower_bound < p.x < upper_bound:
            Sy.append(p)
    # Iterate over Sy, and for each point, look at its at most 7 subsequent
    # points, and find the closest closer split pair
    p1, p2 = None, None
    closer_distance = delta
    for i in range(len(Sy) - 1):
        num_to_look = min(7, len(Sy) - 1 - i)
        for j in range(1, num_to_look + 1):
            if Sy[i].distance(Sy[i + j]) < closer_distance:
                p1, p2 = Sy[i], Sy[i + j]
                closer_distance = Sy[i].distance(Sy[i + j])
    # Return the closest closer split pair accordingly
    if closer_distance == delta:
        return None
    return [p1, p2]
