/**
 * Weighted Interval Scheduling Problem:
 * -Many jobs to do, each job has an importance/weight w_j, a starting time s_i
 *  and an ending time e_i.
 *
 * Find a maximum-weight subset of the jobs with no overlapping.
 *
 * Algorithm: (Dynamic programming)
 * Denote S to be the optimal solution, i.e., the maximum-weight subset of the
 * jobs with no overlapping.
 *
 * In order to form a sequence of the jobs, we sort the jobs in finishing time.
 * Denote job-n to be the job with the latest finishing time.
 *
 * Consider whether job-n is in S:
 * 1. job-n is NOT in S:
 *    => S is optimal among the first (n - 1) jobs.
 *    => S = the optimal solution among the first (n - 1) jobs
 * 2. job-n is in S:
 *    => {S - job-n} is optimal among the first k jobs removing the last jobs
 *       overlapping with job-n.
 *    => S = the optimal solution among the first k jobs removing the last jobs
 *           overlapping with job-n.
 *
 * i.e.,
 * Denote L(i) to be the last job not overlapping with job-i.
 * Let S(i) be the optimal solution for the subproblem with the first i jobs,
 * then
 * S(i) = max{S(i - 1), S(L(i)) + w_i}
 */

import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedList;

class Job {
    /**
     * Importance of this job.
     */
    private final double weight;
    /**
     * Starting time of this job.
     */
    private final double startTime;
    /**
     * Ending time of this job.
     */
    private final double endTime;

    /**
     * Constructor with parameter.
     * @param weight weight of the job
     * @param startTime starting time of the job
     * @param endTime ending time of the job
     */
    Job(double weight, double startTime, double endTime) {
        this.weight = weight;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    /**
     * Accessor of weight.
     * @return weight
     */
    double getWeight() {
        return weight;
    }

    /**
     * Accessor of startTime.
     * @return startTime
     */
    double getStartTime() {
        return startTime;
    }

    /**
     * Accessor of endTime.
     * @return endTime
     */
    double getEndTime() {
        return endTime;
    }
}

public class WeightedIntervalScheduling {

    /**
     * Subproblem solutions.
     * Since there are only O(n) distinct subproblems, the first time we solve a
     * subproblem, we can cache its solution in a global take for O(1) lookup
     * time later on.
     */
    private double[] subproblems;

    /**
     * Finds the maximum-weight interval scheduling in an improved bottom-up
     * way.
     * @param jobs jobs to schedule
     * @return maximum-weight interval scheduling
     */
    public LinkedList<Integer> findMaxWeightIntervalScheduling(Job[] jobs) {
        // Check whether the input array is null or empty
        if ((jobs == null) || (jobs.length == 0)) {
            throw new IllegalArgumentException("The input jobs should not be null or empty.");
        }

        // Sort the jobs by finishing time
        Arrays.sort(jobs, new Comparator<Job>() {
            @Override
            public int compare(Job o1, Job o2) {
                return Double.compare(o1.getEndTime(), o2.getEndTime());
            }
        });

        int n = jobs.length;
        // Initialization
        subproblems = new double[n];
        subproblems[0] = jobs[0].getWeight();
        // Bottom-up calculation
        for (int curr = 1; curr < n; ++curr) {
            double resultWithoutCurr = subproblems[curr - 1];
            double resultWithCurr = jobs[curr].getWeight();
            int lastNoOverlap = findLastNoOverlap(jobs, curr);
            if (lastNoOverlap != -1) {
                resultWithCurr += subproblems[lastNoOverlap];
            }
            subproblems[curr] = Math.max(resultWithoutCurr, resultWithCurr);
        }
        return reconstructMaxWeightIntervalScheduling(jobs);
        // Overall running time complexity: O(nlog n)
    }

    /**
     * Private helper method to find the last job not overlapping with the given
     * job using binary search.
     * @param jobs jobs to schedule
     * @param i given job index
     * @return last job index not overlapping with the given job
     */
    private int findLastNoOverlap(Job[] jobs, int i) {
        // Binary search
        int start = 0, end = i - 1;
        while (start <= end) {
            int mid = start + (end - start) / 2;
            if (jobsOverlap(jobs[mid], jobs[i])) {
                end = mid - 1;
            } else {
                start = mid + 1;
            }
        }
        return end;
        // Running time complexity: O(log n)
    }

    /**
     * Helper method to check whether the given two jobs overlap.
     * @param jobA job with earlier finishing time
     * @param jobB job with later finishing time
     * @return whether the given two jobs overlap
     */
    private boolean jobsOverlap(Job jobA, Job jobB) {
        return jobA.getEndTime() <= jobB.getStartTime();
        // Running time complexity: O(1)
    }

    /**
     * Private helper method to reconstruct the maximum-weight interval
     * scheduling according to the optimal solution using backtracking.
     * @param jobs jobs to schedule
     * @return maximum-weight interval scheduling
     */
    private LinkedList<Integer> reconstructMaxWeightIntervalScheduling(Job[] jobs) {
        LinkedList<Integer> scheduledJobs = new LinkedList<>();
        int curr = jobs.length - 1;
        while (curr >= 1) {
            double resultWithoutCurr = subproblems[curr - 1];
            double resultWithCurr = jobs[curr].getWeight();
            int lastNoOverlap = findLastNoOverlap(jobs, curr);
            if (lastNoOverlap != -1) {
                resultWithCurr += subproblems[lastNoOverlap];
            }
            if (resultWithoutCurr >= resultWithCurr) {
                // Case 1: The current job is not scheduled.
                --curr;
            } else {
                // Case 2: The current job is scheduled.
                scheduledJobs.addFirst(curr);
                curr = lastNoOverlap;
            }
        }
        if (curr == 0) {
            scheduledJobs.addFirst(0);
        }
        return scheduledJobs;
        // Running time complexity: O(nlog n)
    }

}
