#!usr/bin/env python3
# -*- coding: utf-8 -*-

"""
Given an array A, calculate the # of inversions in the given array, where an
inversion is defined as a pair of number with indices i and j, s.t. i < j and
A[i] > A[j].

Naive implementation: O(n^2)

Algorithm:
Accompanying with Merge Sort, we can count the # of inversions at the same time
in the merging step.
"""

__author__ = 'Ziang Lu'


def count_inversions(nums):
    """
    Counts the # of inversions in the given array.
    :param nums: list[int]
    :return: int
    """
    # Check whether the input array is None or empty
    if nums is None or len(nums) == 0:
        return 0

    # Since we can't modify the input array, we need to make a copy.
    nums_copy = nums.copy()
    return _merge_sort_helper(nums_copy, left=0, right=len(nums_copy) - 1,
                              aux=[0] * len(nums_copy))
    # Overall running time complexity: O(nlog n), better than O(n^2)


def _merge_sort_helper(nums, left, right, aux):
    """
    Private helper function to sort the given sub-array recursively using Merge
    Sort.
    :param nums: list[int]
    :param left: int
    :param right: int
    :param aux: list[int]
    :return: int
    """
    # Base case
    if left == right:
        return 0
    # Recursive case
    # [Divide]
    mid = left + (right - left) // 2
    # [Conquer]
    left_inversion_count = _merge_sort_helper(nums, left=left, right=mid,
                                              aux=aux)
    right_inversion_count = _merge_sort_helper(nums, left=mid + 1, right=right,
                                               aux=aux)
    # Combine the results
    return left_inversion_count + right_inversion_count + \
        _merge(nums, left=left, mid=mid, right=right, aux=aux)
    # T(n) = 2T(n/2) + O(n)
    # a = 2, b = 2, d = 1
    # According to Master Method, the running time complexity is O(nlog n).


def _merge(nums, left, mid, right, aux):
    """
    Helper function to merge the given sub-array.
    :param nums: list[int]
    :param left: int
    :param mid: int
    :param right: int
    :param aux: list[int]
    :return: int
    """
    left_ptr, right_ptr = left, mid + 1
    merged_ptr = left
    inversion_count = 0
    while left_ptr <= mid and right_ptr <= right:
        if nums[left_ptr] <= nums[right_ptr]:
            aux[merged_ptr] = nums[left_ptr]
            left_ptr += 1
            inversion_count += right_ptr - (mid + 1)
        else:
            aux[merged_ptr] = nums[right_ptr]
            right_ptr += 1
        merged_ptr += 1
    while left_ptr <= mid:
        aux[merged_ptr] = nums[left_ptr]
        left_ptr += 1
        merged_ptr += 1
        inversion_count += right_ptr + 1 - (mid + 1)
    while right_ptr <= right:
        aux[merged_ptr] = nums[right_ptr]
        right_ptr += 1
        merged_ptr += 1
    nums[left:right + 1] = aux[left:right + 1]
    return inversion_count
    # Running time complexity: O(n)
