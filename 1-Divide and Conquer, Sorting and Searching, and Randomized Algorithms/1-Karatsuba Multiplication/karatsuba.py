#!usr/bin/env python3
# -*- coding: utf-8 -*-


def karatsuba(x, y):
    """
    Calculates the multiplication of two integers using Karatsuba
    Multiplication.
    :param x: int
    :param y: int
    :return: int
    """
    x_s, y_s = str(x), str(y)

    # Pad leading zeros
    if len(x_s) > len(y_s):
        y_s = pad_zeros(y_s, n_zeros=len(x_s) - len(y_s), at_front=True)
    elif len(x_s) < len(y_s):
        x_s = pad_zeros(x_s, n_zeros=len(y_s) - len(x_s), at_front=True)

    n = len(x_s)
    # Base case
    if n == 1:
        return x * y
    # Recursive case
    a_s, b_s, c_s, d_s = x_s[:n // 2], x_s[n // 2:], y_s[:n // 2], y_s[n // 2:]
    a, b, c, d = int(a_s), int(b_s), int(c_s), int(d_s)
    ac = karatsuba(a, c)
    bd = karatsuba(b, d)
    ad_bc = karatsuba(a + b, c + d) - ac - bd
    # Combine the results
    part_1_s = pad_zeros(str(ac), n_zeros=2 * (n - n // 2), at_front=False)
    part_2_s = pad_zeros(str(ad_bc), n_zeros=n - n // 2, at_front=False)
    return int(part_1_s) + int(part_2_s) + bd


def pad_zeros(s, n_zeros, at_front):
    new_s = ""
    if at_front:
        new_s += '0' * n_zeros
        new_s += s
    else:
        new_s += s
        new_s += '0' * n_zeros
    return new_s


if __name__ == '__main__':
    assert karatsuba(1234, 5678) == 7006652
    print('Passed!')
