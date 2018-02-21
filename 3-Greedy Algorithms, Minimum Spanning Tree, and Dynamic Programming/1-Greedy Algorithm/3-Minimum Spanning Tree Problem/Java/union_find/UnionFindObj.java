package union_find;

/**
 * A simple interface for objects to be stored in Union-Find data structure.
 *
 * @author Ziang Lu
 */
public interface UnionFindObj {

    /**
     * Accessor of leader.
     * @return leader
     */
    public UnionFindObj leader();

    /**
     * Mutator of leader.
     * @param leader leader
     */
    public void setLeader(UnionFindObj leader);

    /**
     * Returns the name of the object.
     * @return name
     */
    public String objName();

}
