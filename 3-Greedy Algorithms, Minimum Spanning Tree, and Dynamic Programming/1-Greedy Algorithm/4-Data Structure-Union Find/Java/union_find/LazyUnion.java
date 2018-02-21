package union_find;

/**
 * Lazy Union implementation of Union-Find data structure.
 * Lazy Union: Only update a single pointer in each union() operation
 * When two groups merge, make one group's leader (i.e., root of the tree) point
 * to the other leader (to be a child of the root of the other tree).
 *
 * -Pros:
 * union() operation reduces to two find() operations and one link operation.
 * => O(n)
 * -Cons:
 * find() operation needs to follow a path of parent pointers until the root,
 * which points to itself.
 * => O(n)
 *
 * Problem: Which group to change leader?
 *
 * Optimization 1: Union-By-Rank
 * Look at which of the two trees is already deeper, and we want to keep the
 * root of that deeper tree, so we install the root of the shallower tree as the
 * child of the deeper one.
 * => Both find() and union() operations: O(r), where r is the rank of the root
 *    of the tree that the object belongs to
 *
 * Claim: With Union-by-Rank optimization, the maximum rank for any object is
 * O(log n).
 *
 * Proof:
 * ********************
 * Claim: The subtree of a rank-r object has size >= 2^r.
 * Proof: by induction on the number of union() operations
 * Base case: Initially all ranks are 0, and all subtree sizes are 1 (2^0).
 * Inductive hypothesis: same as the claim
 * Inductive step:
 * (Nothing to prove when the ranks of all objects don't change. (Subtree sizes
 * only go up.))
 * When one of the ranks change (i.e., when the two groups has roots of the same
 * rank r), the new rank is r + 1, and the new subtree size is
 * 2^r + 2^r = 2 * 2^r = 2^(r + 1) = 2^(new rank).
 * ********************
 * Thus, the maximum possible rank r_max = log2 n.
 *
 * => Both find() and union() operations: O(log n)
 *
 * @param T object type that implements UnionFindObj interface
 * @author Ziang Lu
 */
public class LazyUnion <T extends LazyUnionObj> {

    /**
     * Returns the name of the group, which is exactly the name of the group
     * root, that the given object belongs to.
     * @param obj given object
     * @return name of the group, which is exactly the name of the group root
     */
    public String find(T obj) {
        return findRoot(obj).objName();
        // Running time complexity: O(log n)
    }

    /**
     * Private helper method to find the root of the given object.
     * @param obj given object
     * @return root of the given object
     */
    private LazyUnionObj findRoot(T obj) {
        LazyUnionObj curr = obj;
        while (!isRoot(curr)) {
            curr = curr.parent();
        }
        return curr;
        // Running time complexity: O(log n)
    }

    /**
     * Helper method to check whether the given object is a root.
     * @param obj given object
     * @return whether the given object is a root
     */
    private boolean isRoot(LazyUnionObj obj) {
        return obj.parent() == obj;
        // Running time complexity: O(1)
    }

    /**
     * Fuses the two groups that the given two objects belong to, respectively,
     * together.
     * @param x first object
     * @param y second object
     */
    public void union(T x, T y) {
        // Find the root of the groups   [O(log n)]
        LazyUnionObj xRoot = findRoot(x), yRoot = findRoot(y);
        if (xRoot == yRoot) {
            return;
        }
        // Make one group's root point to the other root   [O(1)]
        LazyUnionObj largerRankRoot = null, smallerRankRoot = null;
        if (xRoot.rank() >= yRoot.rank()) {
            largerRankRoot = xRoot; smallerRankRoot = yRoot;
        } else {
            largerRankRoot = yRoot; smallerRankRoot = xRoot;
        }
        smallerRankRoot.setParent(largerRankRoot);
        largerRankRoot.setRank(Math.max(largerRankRoot.rank(), 1 + smallerRankRoot.rank()));
        // Running time complexity: O(log n)
    }

}
