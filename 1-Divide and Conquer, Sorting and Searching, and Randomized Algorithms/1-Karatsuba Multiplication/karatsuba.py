#!usr/bin/env python3
# -*- coding: utf-8 -*-

__author__ = 'Ziang Lu'


def karatsuba(x: int, y: int) -> int:
    """
    Calculates the multiplication of two integers using Karatsuba
    Multiplication.
    Naive calculation: O(n^2)
    :param x: int
    :param y: int
    :return: int
    """
    # We assume that the input x and y are both non-negative.

    x_s, y_s = str(x), str(y)

    # Pad leading zeros   [O(n)]
    if len(x_s) > len(y_s):
        y_s = _pad_zeros(y_s, len(x_s) - len(y_s), at_front=True)
    elif len(x_s) < len(y_s):
        x_s = _pad_zeros(x_s, len(y_s) - len(x_s), at_front=True)

    n = len(x_s)
    # Base case
    if n == 1:
        return x * y
    # Recursive case
    # [Divide]   [O(n)]
    a_s, b_s, c_s, d_s = x_s[:n // 2], x_s[n // 2:], y_s[:n // 2], y_s[n // 2:]
    a, b, c, d = int(a_s), int(b_s), int(c_s), int(d_s)
    # [Conquer]
    ac = karatsuba(a, c)
    bd = karatsuba(b, d)
    ad_bc = karatsuba(a + b, c + d) - ac - bd
    # Combine the results   [O(n)]
    part_1_s = _pad_zeros(str(ac), 2 * (n - n // 2), at_front=False)
    part_2_s = _pad_zeros(str(ad_bc), n - n // 2, at_front=False)
    return int(part_1_s) + int(part_2_s) + bd
    # T(n) = 3T(n/2) + O(n)
    # a = 2, b = 2, d = 1
    # According to Master Method, the overall running time complexity is
    # O(n^1.585), better than O(n^2).


def _pad_zeros(s: str, n_zeros: int, at_front: bool) -> str:
    """
    Private helper function to pad the given number of zeros to the given
    string.
    :param s: str
    :param n_zeros: int
    :param at_front: bool
    :return: str
    """
    new_s = ""
    if at_front:
        new_s += '0' * n_zeros
        new_s += s
    else:
        new_s += s
        new_s += '0' * n_zeros
    return new_s
    # Running time complexity: O(n)
