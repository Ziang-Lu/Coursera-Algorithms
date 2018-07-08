#!usr/bin/env python3
# -*- coding: utf-8 -*-

"""
Given an array, find the k-th largest element.

Naive implementation:
1. Sort the array
2. Take out the k-th largest element
O(nlog n)
"""

__author__ = 'Ziang Lu'

import random
from typing import List


def kth_largest(nums: List[int], k: int) -> int:
    """
    Finds the k-th largest element in the given list.
    :param nums: list[int]
    :param k: int
    :return: int
    """
    # Check whether the input list is null or empty
    if not nums:
        return 0
    # Check whether the input k is valid
    if k < 0 or k >= len(nums):
        return 0

    return _kth_largest_helper(nums, k, left=0, right=len(nums) - 1)
    # Overall running time complexity: O(n), better than O(nlog n)


def _kth_largest_helper(nums: List[int], k: int, left: int, right: int) -> int:
    """
    Private helper function to find the k-th largest element in the given list
    recursively.
    :param nums: list[int]
    :param k: int
    :param left: int
    :param right: int
    :return: int
    """
    # Base case 1: Shrink to only one number
    if left == right:
        return nums[left]
    # Choose a pivot from the given sub-list, and move it to the left
    _choose_pivot(nums, left=left, right=right)
    pivot_idx = _partition(nums, left=left, right=right)
    # Base case 2: Found it
    if pivot_idx == k:
        return nums[pivot_idx]

    # Recursive case
    if pivot_idx > k:
        return _kth_largest_helper(nums, k, left=left, right=pivot_idx)
    else:
        return _kth_largest_helper(nums, k, left=pivot_idx + 1, right=right)


def _choose_pivot(nums: List[int], left: int, right: int) -> None:
    """
    Helper function to choose a pivot from the given sub-list, and move it to
    the left.
    :param nums: list[int]
    :param left: int
    :param right: int
    :return: None
    """
    # [Randomized] Randomly choose a pivot from the given sub-list
    pivot_idx = random.randrange(left, right + 1)
    # Move the pivot to the left
    if pivot_idx != left:
        nums[left], nums[pivot_idx] = nums[pivot_idx], nums[left]


def _partition(nums: List[int], left: int, right: int) -> int:
    """
    Helper function to partition the given sub-list.
    :param nums: list[int]
    :param left: int
    :param right: int
    :return: int
    """
    # The pivot has already been moved to the left.
    pivot = nums[left]

    # Iterate over the sub-list, use a pointer to keep track of the smaller
    # part, and swap the current number with the pointer as necessary
    smaller_ptr = left + 1
    i = left + 1
    while True:
        while i <= right and nums[i] > pivot:
            i += 1
        if i > right:
            break
        if i != smaller_ptr:
            nums[smaller_ptr], nums[i] = nums[i], nums[smaller_ptr]
        smaller_ptr += 1
        i += 1
    if left != smaller_ptr - 1:
        nums[left], nums[smaller_ptr - 1] = nums[smaller_ptr - 1], nums[left]
    return smaller_ptr - 1
