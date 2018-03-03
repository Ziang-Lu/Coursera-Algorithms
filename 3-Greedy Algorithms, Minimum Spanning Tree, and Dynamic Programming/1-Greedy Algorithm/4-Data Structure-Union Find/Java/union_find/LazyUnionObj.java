package union_find;

/**
 * Simple class for objects to be stored in Union-Find data structure.
 *
 * @author Ziang Lu
 */
public class LazyUnionObj {

    /**
     * Name of this object.
     */
    private final String name;
    /**
     * Parent of this object.
     */
    private LazyUnionObj parent;
    /**
     * Maximum number of hops from some leaf of this object's tree to this
     * object.
     * This concept is inverse to "depth".
     */
    private int rank;

    /**
     * Constructor with parameter.
     * @param name name of this object
     */
    public LazyUnionObj(String name) {
        this.name = name;
        parent = this;
        rank = 0;
    }

    /**
     * Accessor of name.
     * @return name
     */
    public String objName() {
        return name;
    }

    /**
     * Accessor of parent.
     * @return parent
     */
    public LazyUnionObj parent() {
        return parent;
    }

    /**
     * Accessor of rank.
     * @return rank
     */
    public int rank() {
        return rank;
    }

    /**
     * Mutator of parent.
     * @param parent parent
     */
    public void setParent(LazyUnionObj parent) {
        this.parent = parent;
    }

    /**
     * Mutator of rank.
     * @param rank rank
     */
    public void setRank(int rank) {
        this.rank = rank;
    }

}
