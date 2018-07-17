/**
 * Given an array, find the k-th largest element.
 *
 * Naive implementation:
 * 1. Sort the array
 * 2. Take out the k-th largest element
 * O(nlog n)
 */

import java.util.Random;

public class RandomSelection {

    /**
     * Finds the k-th largest element in the given array.
     * @param nums given array
     * @param k target order
     * @return k-th largest element
     */
    public int kthLargest(int[] nums, int k) {
        // Check whether the input array is null or empty
        if ((nums == null) || nums.length == 0) {
            return 0;
        }
        // Check whether the input k is valid
        if ((k < 0) || (k >= nums.length)) {
            return 0;
        }

        return kthLargestHelper(nums, 0, nums.length - 1, k);
        // Overall expected running time complexity: O(n), better than O(nlog n)
    }

    /**
     * Private helper method to find the k-th largest element in the given array
     * recursively.
     * @param nums given array
     * @param left left bound
     * @param right right bound
     * @param k target order
     * @return k-th largest element
     */
    private int kthLargestHelper(int[] nums, int left, int right, int k) {
        // Base case 1: Shrink to only one number
        if (left == right) {
            return nums[left];
        }
        // Choose a pivot from the given subarray, and move it to the left
        choosePivot(nums, left, right);
        int pivotIdx = partition(nums, left, right);
        // Base case 2: Found it
        if (pivotIdx == k) {
            return nums[pivotIdx];
        }

        // Recursive case
        if (pivotIdx > k) {
            return kthLargestHelper(nums, left, pivotIdx - 1, k);
        } else {
            return kthLargestHelper(nums, pivotIdx + 1, right, k);
        }
        // T(n) = T(n/2) + O(n)
        // a = 1, b = 2, d = 1
        // According to Master Method, the expected running time complexity is O(n).
    }

    /**
     * Helper method to choose a pivot from the given subarray, and move it to
     * the left.
     * @param nums array to sort
     * @param left left bound
     * @param right right bound
     */
    private void choosePivot(int[] nums, int left, int right) {
        // [Randomized] Randomly choose a pivot from the given subarray
        Random random = new Random();
        int pivotIdx = left + random.nextInt(right + 1 - left);
        // Move the pivot to the left
        if (pivotIdx != left) {
            swap(nums, pivotIdx, left);
        }
        // Running time complexity: O(1) or O(n), depending on the method
    }

    /**
     * Helper method to swap two elements in the given array.
     * @param array given array
     * @param i index of the first element
     * @param j index of the second element
     */
    private void swap(int[] array, int i, int j) {
        int tmp = array[j];
        array[j] = array[i];
        array[i] = tmp;
        // Running time complexity: O(1)
    }

    /**
     * Helper method to partition the given part of the array.
     * @param nums array to partition
     * @param left left bound
     * @param right right bound
     * @return
     */
    private int partition(int[] nums, int left, int right) {
        // The pivot has already been moved to the left.
        int pivot = nums[left];

        // Iterate over the subarray, use a pointer to keep track of the smaller part, and swap the current number with
        // the pointer as necessary
        int smallerPtr = left + 1;
        int i = left + 1;
        while (true) {
            while ((i <= right) && (nums[i] > pivot)) {
                ++i;
            }
            if (i > right) {
                break;
            }
            if (i != smallerPtr) {
                swap(nums, i, smallerPtr);
            }
            ++smallerPtr;
            ++i;
        }
        if (left != (smallerPtr - 1)) {
            swap(nums, left, smallerPtr - 1);
        }
        return smallerPtr - 1;
        // Running time complexity: O(n)
    }

}
