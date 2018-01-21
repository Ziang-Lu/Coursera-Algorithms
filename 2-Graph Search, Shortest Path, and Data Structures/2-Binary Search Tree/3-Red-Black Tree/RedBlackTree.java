/**
 * Invariants of a Red-Black Tree:
 * 1. Each node is either red or black.
 * 2. The root is always black.
 * 3. Never allow two red nodes in a row
 * 4. Every root-null path passes exact the same number of black nodes.
 *    i.e., The number of black nodes that a root-null path passes cannot depend
 *          on the path.
 *
 * @author Ziang Lu
 */
public class RedBlackTree {

    /**
     * Static nested Node class.
     */
    private static class Node {
        /**
         * Key of this node.
         */
        private int key;
        /**
         * Reference to the left child.
         */
        private Node left;
        /**
         * Reference to the right child.
         */
        private Node right;
        /**
         * Color of this node.
         */
        private boolean color;

        /**
         * Constructor with parameter.
         * @param key key to store
         * @param color color of this node
         */
        Node(int key, boolean color) {
            this.key = key;
            left = null;
            right = null;
            this.color = color;
        }
    }

    /**
     * Color red.
     */
    private static final boolean RED = true;
    /**
     * Color black.
     */
    private static final boolean BLACK = false;

    /**
     * Root of this Red-Black Tree.
     */
    private Node root;

    /**
     * Default constructor.
     */
    public RedBlackTree() {
        root = null;
    }

    /*
     * Insertion:
     * 1. Insert the new element as a normal BST (Might break invariants)
     * 2. Recolor and perform rotations until invariants are restored.
     */

}
