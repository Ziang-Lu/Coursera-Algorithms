/**
 * A simple class for objects to be stored in UnionFind data structure.
 *
 * @author Ziang Lu
 */
public class UnionFindObj {

    /**
     * Name of this object.
     * Actually this represents the subset that this object belongs to.
     */
    private String name;
    /**
     * Leader of this object.
     */
    private UnionFindObj leader;

    /**
     * Default constructor.
     * Any subclass must override name field.
     */
    public UnionFindObj() {
        name = null;
        leader = this;
    }

    /**
     * Accessor of name.
     * @return name
     */
    public String name() {
        return name;
    }

    /**
     * Accessor of leader.
     * @return leader
     */
    public UnionFindObj leader() {
        return leader;
    }

    /**
     * Mutator of leader.
     * @param leader leader
     */
    public void setLeader(UnionFindObj leader) {
        this.leader = leader;
    }

}
