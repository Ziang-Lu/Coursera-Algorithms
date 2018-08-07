#!usr/bin/env python3
# -*- coding: utf-8 -*-

"""
Quick Sort implementation.
"""

__author__ = 'Ziang Lu'

import random
from typing import List


def quick_sort(nums: List[int]) -> None:
    """
    Sorts the given list using Quick Sort.
    :param nums: list[int]
    :return: None
    """
    # Check whether the input list is null or empty
    if not nums:
        return

    _quick_sort_helper(nums, left=0, right=len(nums) - 1)


def _quick_sort_helper(nums: List[int], left: int, right: int) -> None:
    """
    Private helper function to sort the given sub-list recursively using Quick
    Sort.
    :param nums: list[int]
    :param left: int
    :param right: int
    :return: None
    """
    # Base case
    if left >= right:
        return
    # Recursive case
    # Choose a pivot from the given sub-list, and move it to the left.
    _choose_pivot(nums, left, right)
    pivot_idx = _partition(nums, left=left, right=right)
    _quick_sort_helper(nums, left=left, right=pivot_idx - 1)
    _quick_sort_helper(nums, left=pivot_idx + 1, right=right)


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
    pivot_idx = random.randint(left, right)
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
