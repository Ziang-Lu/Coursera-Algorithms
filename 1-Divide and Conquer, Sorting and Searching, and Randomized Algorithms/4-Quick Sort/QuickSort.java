import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Quick Sort implementation.
 *
 * @author Ziang Lu
 */
public class QuickSort {

    /**
     * Sorts the given array using Quick Sort.
     * @param nums array to sort
     */
    public void quickSort(int[] nums) {
        // Check whether the input array is null or empty
        if ((nums == null) || (nums.length == 0)) {
            return;
        }

        quickSortHelper(nums, 0, nums.length - 1);
    }

    /**
     * Private helper method to sort the given sub-array recursively using
     * QuickSort.
     * @param nums array to sort
     * @param left left bound
     * @param right right bound
     */
    private void quickSortHelper(int[] nums, int left, int right) {
        // Base case
        if (left >= right) {
            return;
        }
        // Recursive case
        // Choose a pivot from the given sub-array, and move it to the left
        choosePivot(nums, left, right);
        int pivotIdx = partition(nums, left, right);
        quickSortHelper(nums, left, pivotIdx - 1);
        quickSortHelper(nums, pivotIdx + 1, right);
    }

    /**
     * Helper method to choose a pivot from the given sub-array, and move it to
     * the left.
     * @param nums array to sort
     * @param left left bound
     * @param right right bound
     */
    private void choosePivot(int[] nums, int left, int right, boolean randomly) {
        // [Randomized] Randomly choose a pivot from the given sub-array
        Random random = new Random();
        int pivotIdx = left + random.nextInt(right + 1 - left);
        // [Deterministic] Use the median of medians as the pivot
        // Move the pivot to the left
        if (pivotIdx != left) {
            swap(nums, pivotIdx, left);
        }
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

        // Iterate over the sub-array, use a pointer to keep track of the smaller part, and swap the current number with
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
    }

}
