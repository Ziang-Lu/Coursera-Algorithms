package union_find;

/**
 * A simple interface for objects to be stored in UnionFind data structure.
 *
 * @author Ziang Lu
 */
public interface UnionFindObj {

    /**
     * Accessor of name.
     * @return name
     */
    public String name();

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

}
