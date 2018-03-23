package union_find;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Eager Union implementation of Union-Find data structure.
 * Maintain a partition of a set of objects
 *
 * @param T object type that implements UnionFindObj interface
 * @author Ziang Lu
 */
public class UnionFind <T extends UnionFindObj> {

    /**
     * Mapping between group names and the corresponding group.
     * Maintain a linked structure, and each subset has an arbitrary leader
     * (representative of the group) object, and the group name is exactly the
     * name of the leader.
     */
    private HashMap<String, ArrayList<T>> groups;

    /**
     * Constructor with parameter.
     * @param objs list of sole objects
     */
    public UnionFind(ArrayList<T> objs) {
        groups = new HashMap<>();
        for (T obj : objs) {
            ArrayList<T> group = new ArrayList<>();
            group.add(obj);
            groups.put(obj.objName(), group);
        }
    }

    /**
     * Returns the name of the group, which is exactly the name of the group
     * leader, that the given object belongs to.
     * @param obj given object
     * @return name of the group, which is exactly the name of the group leader
     */
    public String find(T obj) {
        return obj.leader().objName();
        // Running time complexity: O(1)
    }

    /**
     * Fuses the given two groups together.
     * Objects in the first group and objects in the second group should all
     * coalesce, and be now in one single group.
     * @param groupNameA name of the first group
     * @param groupNameB name of the second group
     */
    public void union(String groupNameA, String groupNameB) {
        // Check whether the input strings are null or empty
        if ((groupNameA == null) || (groupNameA.length() == 0) || (groupNameB == null) || (groupNameB.length() == 0)) {
            throw new IllegalArgumentException("The input group names should not be null or empty.");
        }
        // Check whether the input group names exist
        if (!(groups.containsKey(groupNameA)) || !(groups.containsKey(groupNameB))) {
            throw new IllegalArgumentException("The input group names don't both exist.");
        }

        ArrayList<T> groupA = groups.get(groupNameA), groupB = groups.get(groupNameB);
        // In order to reduce the number of leader updates, let the smaller group inherit the leader of the larger one.
        ArrayList<T> larger = null, smaller = null;
        if (groupA.size() >= groupB.size()) {
            larger = groupA; smaller = groupB;
        } else {
            larger = groupB; smaller = groupA;
        }
        UnionFindObj largerLeader = larger.get(0).leader();
        String smallerName = smaller.get(0).objName();
        updateLeader(smaller, largerLeader);

        larger.addAll(smaller);
        groups.remove(smallerName);
        // Running time complexity: O(n)
    }

    /**
     * Private helper method to update leader of the given group to the given new leader.
     * @param group given group
     * @param newLeader given new leader
     */
    private void updateLeader(ArrayList<T> group, UnionFindObj newLeader) {
        for (T obj : group) {
            obj.setLeader(newLeader);
        }
        // Running time complexity: O(n)
    }

}
