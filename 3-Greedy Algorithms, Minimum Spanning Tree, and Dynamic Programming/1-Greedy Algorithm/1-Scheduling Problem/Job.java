/**
 * Job class.
 *
 * @author Ziang Lu
 */
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

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append("Job with weight ").append(weight).append(" and length ").append(length);
        return s.toString();
    }

}
