package graph;

/**
 * AbstractVertex class.
 *
 * Note that parallel edges are allowed, but not self-loops.
 * @author Ziang Lu
 */
public abstract class AbstractVertex implements Comparable<AbstractVertex> {

    /**
     * Default local smallest score (infinity).
     */
    protected static final int DEFAULT_LOCAL_MIN_SCORE = Integer.MAX_VALUE;

    /**
     * Vertex ID.
     */
    protected final int vtxID;
    /**
     * Minimum Dijkstra score of any edge from v in X to this vertex w in
     * (V-X). If such an edge doesn't exist, set to infinity.
     * In other words, this is a "local" winner score of the tournament's
     * first-round, so that the heap (V-X) only stores the "local" winner scores
     * of the tournament's first round.
     */
    protected double myLocalMinScore;

    /**
     * Constructor with parameter.
     * @param vtxID vertex ID
     */
    public AbstractVertex(int vtxID) {
        this.vtxID = vtxID;
        myLocalMinScore = DEFAULT_LOCAL_MIN_SCORE;
    }

    /**
     * Accessor of vtxID.
     * @return vtxID
     */
    public int id() {
        return vtxID;
    }

    /**
     * Accessor of myLocalMinScore.
     * @return myLocalMinScore
     */
    public double localMinScore() {
        return myLocalMinScore;
    }

    /**
     * Mutator of myLocalMinScore.
     * @param localMinScore myLocalMinScore
     */
    public void setLocalMinScore(double localMinScore) {
        myLocalMinScore = localMinScore;
    }

    @Override
    public int compareTo(AbstractVertex o) {
        return Double.compare(myLocalMinScore, o.myLocalMinScore);
    }

}
