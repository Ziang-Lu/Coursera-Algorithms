/**
 * Given an array A, calculate the # of inversions in the given array, where an
 * inversion is defined as a pair of numbers with indices i and j, s.t. i < j
 * and A[i] > A[j].
 *
 * Naive implementation: O(n^2)
 *
 * Algorithm:
 * Accompanying with Merge Sort, we can count the # of inversions at the same
 * time in the merging step.
 */

public class InversionCounter {

    /**
     * Counts the # of inversions in the given array.
     * @param nums array to count inversions
     * @return # of inversions
     */
    public int countInversions(int[] nums) {
        // Check whether the input array is null or empty
        if ((nums == null) || (nums.length == 0)) {
            return 0;
        }

        // Since we can't modify the input array, we need to make a copy.   [O(n)]
        int[] numsCopy = new int[nums.length];
        System.arraycopy(nums, 0, numsCopy, 0, nums.length);
        return mergeSortHelper(numsCopy, 0, numsCopy.length - 1, new int[nums.length]);
        // Overall running time complexity: O(nlog n), better than O(n^2)
    }

    /**
     * Private helper method to sort the given sub-array recursively using Merge
     * Sort.
     * @param nums array to sort
     * @param left left bound
     * @param right right bound
     * @param aux auxiliary array for merging
     * @return # of inversions in the given sub-array
     */
    private int mergeSortHelper(int[] nums, int left, int right, int[] aux) {
        // Base case
        if (left == right) {
            return 0;
        }
        // Recursive case
        // [Divide]
        int mid = left + (right - left) / 2;
        // [Conquer]
        int leftInversionCount = mergeSortHelper(nums, left, mid, aux);
        int rightInversionCount = mergeSortHelper(nums, mid + 1, right, aux);
        // Combine the results
        return leftInversionCount + rightInversionCount + merge(nums, left, mid, right, aux);
        // T(n) = 2T(n/2) + O(n)
        // a = 2, b = 2, d = 1
        // According to Master Method, the running time complexity is O(nlog n).
    }

    /**
     * Helper method to merge the given sub-array.
     * @param nums array to sort
     * @param left left bound
     * @param mid right bound of the left half
     * @param right right bound
     * @param aux auxiliary array
     * @return # of inversions across the two halves
     */
    private int merge(int[] nums, int left, int mid, int right, int[] aux) {
        int leftPtr = left, rightPtr = mid + 1;
        int mergedPtr = left;
        int inversionCount = 0;
        while ((leftPtr <= mid) && (rightPtr <= right)) {
            if (nums[leftPtr] <= nums[rightPtr]) {
                aux[mergedPtr] = nums[leftPtr];
                ++leftPtr;
                inversionCount += rightPtr - (mid + 1);
            } else {
                aux[mergedPtr] = nums[rightPtr];
                ++rightPtr;
            }
            ++mergedPtr;
        }
        while (leftPtr <= mid) {
            aux[mergedPtr] = nums[leftPtr];
            ++leftPtr;
            ++mergedPtr;
            inversionCount += right + 1 - (mid + 1);
        }
        while (rightPtr <= right) {
            aux[mergedPtr] = nums[rightPtr];
            ++rightPtr;
            ++mergedPtr;
        }
        System.arraycopy(aux, left, nums, left, right - left + 1);
        return inversionCount;
        // Running time complexity: O(n)
    }

}
