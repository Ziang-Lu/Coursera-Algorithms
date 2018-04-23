/**
 * Scheduling Problem:
 * -One shared resource
 * -Many jobs to do, each job has an importance/priority w_j and an elapse time
 *  l_j
 *  (Jobs with higher weights deserve to be processed earlier than those with
 *  lower weights.)
 * -In what order should we sequence these jobs?
 *
 * Criterion:
 * Minimize the weighted sum of completion times, where the completion time of
 * a job is the overall timestamp at which the job is finished
 *
 * Greedy algorithm:
 * Given each job a "score" of w_j/l_j, and sort the jobs by this "score"
 * 这个score本质上意义为单位时间内可完成多少importance/priority
 * => Make sense
 *
 * Proof:
 * -Fix arbitrary input (N jobs)
 *
 * Let s be the job sequence output by the greedy algorithm.
 * Rename the jobs, s.t. w_1/l_1 >= w_2/l_2 >= ... >= w_N/l_N
 *
 * Assume that there exists an s* that is the optimal job sequence and strictly
 * better than s.
 * In this way, s = 1, 2, ..., N, and in s* there must exist consecutive jobs
 * (i, j) s.t. i > j.
 *
 * "Exchange argument":
 * By exchanging i and j, the completion time of i will go up, and that of j will
 * go down, and all other stuff remains unchanged.
 * => Cost: the criterion will increase by w_i * l_j
 *    Benefit: the criterion will decrease by w_j * l_i
 * Since i > j, then w_i/l_i <= w_j/l_j => w_i*l_j (cost) <= w_j*l_i (benefit)
 * => Exchanging i and j where i > j has non-negative net benefit.
 * => We can keep doing this kind of exchanges with non-negative net benefits,
 *    and finally yield s.
 *    i.e., s is optimal.
 */

import java.util.Arrays;
import java.util.Comparator;

class Job {
    /**
     * Importance/priority of the job.
     */
    private final double weight;
    /**
     * Elapse time of this job.
     */
    private final double length;

    /**
     * Constructor with parameter.
     * @param weight weight of the job
     * @param length elapse time of the job
     */
    Job(double weight, double length) {
        this.weight = weight;
        this.length = length;
    }

    /**
     * Accessor of weight.
     * @return weight
     */
    double getWeight() {
        return weight;
    }

    /**
     * Accessor of length.
     * @return length
     */
    double getLength() {
        return length;
    }
}

public class Scheduler {

    /**
     * Schedules the given jobs to minimize the criterion above.
     * @param jobs jobs to schedule
     */
    public static void scheduleJobs(Job[] jobs) {
        Arrays.sort(jobs, new Comparator<Job>() {
            @Override
            public int compare(Job o1, Job o2) {
                double score1 = o1.getWeight() / o1.getLength(), score2 = o2.getWeight() / o2.getLength();
                return Double.compare(score2, score1);
            }
        });
    }

}
