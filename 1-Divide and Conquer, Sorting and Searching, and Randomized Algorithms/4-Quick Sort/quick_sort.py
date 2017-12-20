#!usr/bin/env python3
# -*- coding: utf-8 -*-

__author__ = 'Ziang Lu'

import random


def quick_sort(nums):
    """
    Sorts the given array using Quick Sort.
    :param nums: list[int]
    :return: None
    """
    # Check whether the input array is null or empty
    if nums is None or len(nums) == 0:
        return

    _quick_sort_helper(nums, left=0, right=len(nums) - 1)


def _quick_sort_helper(nums, left, right):
    """
    Private helper function to sort the given sub-array recursively using Quick
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
    # Choose a pivot from the given sub-array, and move it to the left.
    _choose_pivot(nums, left=left, right=right)
    pivot_idx = _partition(nums, left=left, right=right)
    _quick_sort_helper(nums, left=left, right=pivot_idx - 1)
    _quick_sort_helper(nums, left=pivot_idx + 1, right=right)


def _choose_pivot(nums, left, right, randomly=True):
    """
    Helper function to choose a pivot from the given sub-array, and move it to
    the left.
    :param nums: list[int]
    :param left: int
    :param right: int
    :param randomly: bool
    :return: None
    """
    if randomly:
        # [Randomized] Randomly choose a pivot from the given sub-array
        pivot_idx = random.randrange(left, right + 1)
        # Move the pivot to the left
    else:
        # [Deterministic] Use the median of medians as the pivot

        # Create sorted parts
        sorted_parts = []
        i = left
        while i <= right:
            num_of_elems = min(5, right + 1 - left)
            part = nums[i:i + num_of_elems]
            part.sort()
            sorted_parts.append(part)
            i += num_of_elems
        # Take out the medians of the sorted parts
        medians = []
        for sorted_part in sorted_parts:
            medians.append(sorted_part[len(sorted_part) // 2])
        # Use the median of the medians as the pivot
        pivot_idx = len(medians) // 2
    # Move the pivot to the left
    if pivot_idx != left:
        nums[left], nums[pivot_idx] = nums[pivot_idx], nums[left]


def _partition(nums, left, right):
    """
    Helper function to partition the given sub-array.
    :param nums: list[int]
    :param left: int
    :param right: int
    :return: int
    """
    # The pivot has already been moved to the left.
    pivot = nums[left]

    # Iterate over the sub-array, use a pointer to keep track of the smaller
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
