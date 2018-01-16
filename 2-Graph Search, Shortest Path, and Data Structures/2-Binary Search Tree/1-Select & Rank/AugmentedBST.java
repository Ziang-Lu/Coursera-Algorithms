/**
 * Select & Rank problem:
 * Given the ranking of a key in the BST, find the key.
 * Given a key in the BST, find its ranking.
 *
 * The two sub-problems are mutually reverse problems.
 */

/**
 * Augmented BST class.
 * A simple BST, but each node has an additional data field: the number of nodes
 * in the sub-tree rooted at that node.
 *
 * @author Ziang Lu
 */
public class AugmentedBST implements BSTInterface {

    /**
     * Static nested Node class.
     */
    private static class Node {
        /**
         * Key of this node.
         */
        private int key;
        /**
         * Reference to left child.
         */
        private Node left;
        /**
         * Reference to right child.
         */
        private Node right;
        /**
         * Number of nodes in the sub-tree rooted at this node.
         */
        private int size;

        /**
         * Constructor with parameter.
         * @param key key to store
         */
        Node(int key) {
            this.key = key;
            left = null;
            right = null;
            size = 1;
        }

        @Override
        public String toString() {
            StringBuilder s = new StringBuilder();
            s.append("[").append(key).append(", size: ").append(size).append("]");
            return s.toString();
        }
    }

    /**
     * Root of this augmented BST.
     */
    private Node root;

    /**
     * Default contructor.
     */
    public AugmentedBST() {
        root = null;
    }

    @Override
    public boolean search(int key) {
        return searchHelper(key, root);
    }

    /**
     * Private helper method to search for the given key in the given sub-tree
     * recursively.
     * @param key key to search for
     * @param curr current node
     * @return whether the key is in the sub-tree
     */
    private boolean searchHelper(int key, Node curr) {
        // Base case 1: Not found
        if (curr == null) {
            return false;
        }
        // Base case 2: Found it
        if (curr.key == key) {
            return true;
        }

        // Recursive case
        return searchHelper(key, curr.left) || searchHelper(key, curr.right);
    }

    @Override
    public boolean insert(int key) {
        if (root == null) {
            root = new Node(key);
            return true;
        }

        return insertHelper(key, null, root, true);
    }

    /**
     * Private helper method to insert the given key to the given sub-tree.
     * @param key key to insert
     * @param parent parent node
     * @param curr current node
     * @param isLC whether the current node is the left child
     * @return whether the insertion is successful
     */
    private boolean insertHelper(int key, Node parent, Node curr, boolean isLC) {
        // Base case 1: Found the spot to insert
        if (curr == null) {
            if (isLC) {
                parent.left = new Node(key);
            } else {
                parent.right = new Node(key);
            }
            return true;
        }
        // Base case 2: Found it
        if (curr.key == key) {
            // No duplicates allowed
            return false;
        }

        ++curr.size;
        // Recursive case
        if (curr.key > key) {
            return insertHelper(key, curr, curr.left, true);
        } else {
            return insertHelper(key, curr, curr.right, false);
        }
    }

    @Override
    public void traverseInOrder() {
        StringBuilder s = new StringBuilder();
        traverseInOrderHelper(root, s);
        System.out.println(s.toString().trim());
    }

    /**
     * Private helper method to traverse the given sub-tree in-order
     * recursively.
     * @param curr current node
     * @param s StringBuilder to append
     */
    private void traverseInOrderHelper(Node curr, StringBuilder s) {
        // Base case
        if (curr == null) {
            return;
        }
        // Recursive case
        traverseInOrderHelper(curr.left, s);
        s.append(curr).append(" ");
        traverseInOrderHelper(curr.right, s);
    }

    /***
     * Returns the key with the given ranking in the BST.
     * @param rank given ranking
     * @return key if found, RuntimeException if not found
     */
    public int select(int rank) {
        // Check whether the BST is empty
        if (root == null) {
            throw new RuntimeException("The BST is empty.");
        }
        // Check whether the input ranking is out of range
        int numOfNodes = root.size;
        if ((rank <= 0) || (rank > numOfNodes)) {
            throw new RuntimeException("The input ranking is out of range.");
        }

        return selectHelper(rank, root);
    }

    /**
     * Private helper method to find the key with the given ranking in the given
     * sub-tree recursively.
     * @param rank given ranking
     * @param curr current node
     * @return key found
     */
    private int selectHelper(int rank, Node curr) {
        int leftSize = 0;
        if (curr.left != null) {
            leftSize = curr.left.size;
        }
        // Base case
        int currRankInSubTree = leftSize + 1;
        if (currRankInSubTree == rank) {
            return curr.key;
        }

        // Recursive case
        if (currRankInSubTree > rank) {
            return selectHelper(rank, curr.left);
        } else {
            // Note that rank in the right sub-tree is (rank - currRankInSubTree)
            return selectHelper(rank - currRankInSubTree, curr.right);
        }
    }

    /**
     * Returns the ranking of the given key in the BST. 
     * @param key given key
     * @return ranking if found, -1 if not found
     */
    public int getRank(int key) {
        return getRankHelper(key, root);
    }

    /**
     * Private helper method to find the ranking of the given key in the given
     * sub-tree recursively.
     * @param key given key
     * @param curr current node
     * @return ranking in the sub-tree if found, -1 if not found
     */
    private int getRankHelper(int key, Node curr) {
        // Base case 1: Not found
        if (curr == null) {
            return -1;
        }
        int leftSize = 0;
        if (curr.left != null) {
            leftSize = curr.left.size;
        }
        int currRankInSubTree = leftSize + 1;
        // Base case 2: Found it
        if (curr.key == key) {
            return currRankInSubTree;
        }

        // Recursive case
        if (curr.key > key) {
            return getRankHelper(key, curr.left);
        } else {
            // Note that the rank in the right sub-tree is (rank - currRankInSubTree)
            return currRankInSubTree + getRankHelper(key, curr.right);
        }
    }

}
