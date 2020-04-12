/**
 * Invariants of a Red-Black Tree:
 * 1. Each node is either red or black.
 * 2. The root is always black.
 * 3. Never allow two red nodes in a row
 * 4. Every root-null path passes exactly the same number of black nodes.
 *    i.e., The number of black nodes that a root-null passes cannot depend on
 *          the path.
 *
 * Implementation according to:
 * https://zhuanlan.zhihu.com/p/37470948
 *
 * @author Ziang Lu
 */
public class RedBlackTree implements BSTInterface {

    /**
     * Static nested Node class.
     */
    private static class Node {
        /**
         * Key of this node.
         */
        private final int key;
        /**
         * Reference to the parent.
         */
        private Node parent;
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
         * @param parent parent of the node
         * @param color color of the node
         */
        Node(int key, Node parent, boolean color) {
            this.key = key;
            this.parent = parent;
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

    @Override
    public boolean search(int key) {
        return searchHelper(key, root);
    }

    /**
     * Private helper method to search for the given key in the given subtree
     * recursively.
     * @param key key to search for
     * @param curr current node
     * @return whether the key is in the subtree
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
        if (curr.key > key) {
            return searchHelper(key, curr.left);
        } else {
            return searchHelper(key, curr.right);
        }
        // T(n) = T(n/2) + O(1)
        // a = 1, b = 2, d = 0
        // According to Master Method, time: O(log n)
    }

    @Override
    public void insert(int key) {
        if (root == null) {
            root = new Node(key, null, BLACK);
            return;
        }
        insertHelper(key, null, root, true);
    }

    /**
     * Private helper method to insert the given key to the given subtree
     * recursively.
     *
     * Insertion:
     * 1. Insert a new element as a normal BST (Might break invariants)
     *    Note that the newly inserted node is always red, since the probability
     *    of breaking the invariants for a red node is smaller than that of a
     *    black node
     *    i.e., If we insert a red node, there is a half-half probability of
     *          breaking invariant #3; if we insert a black node, we must have
     *          broken invariant #4, which is also more difficult to restore.
     * 2. Recolor and perform rotations until invariants are restored
     * @param key key to insert
     * @param curr current node
     * @param isLC whether the current node is the left child
     * @return root of the subtree after insertion
     */
    private void insertHelper(int key, Node parent, Node curr, boolean isLC) {
        // Base case 1: Found the spot to insert
        if (curr == null) {
            Node newNode = new Node(key, parent, RED);
            if (isLC) {
                parent.left = newNode;
            } else {
                parent.right = newNode;
            }
            // Restore the invariants
            restoreInvariants(newNode); // O(log n)
            return;
        }
        // Base case 2: Found it
        if (curr.key == key) {
            // No duplicates allowed
            return;
        }

        // Recursive case
        if (curr.key > key) {
            insertHelper(key, curr, curr.left, true);
        } else {
            insertHelper(key, curr, curr.right, false);
        }
        // T(n) = T(n/2) + O(1)
        // a = 1, b = 2, d = 0
        // According to Master Method, time: O(log n)
    }

    /**
     * Helper method to restore the invariants.
     * @param toRestore node to restore (must be red)
     */
    private void restoreInvariants(Node toRestore) {
        Node parent = toRestore.parent;
        // Case 1: The parent is black.
        if (parent.color == BLACK) { // This won't violate invariant #3.
            return;
        }

        // Case 2: The parent is red.
        if (parent.color == RED) { // This violates invariant #3.
            // Then toRestore must have a grandparent, and it must be black according to invariant #3.
            Node grandparent = toRestore.parent.parent;
            if (parent == grandparent.left) {
                Node uncle = grandparent.right;
                if ((uncle != null) && (uncle.color == RED)) { // Case 2.1: The uncle is red.
                    // To restore invariant #3 and #4, simply recolor the grandparent to red and the parent and the
                    // uncle to black.
                    restoreParentAndUncleAreRed(grandparent, parent, uncle);
                    // In this way, the same case applies to the newly recolored grandparent.
                    // Then we simply need to recurse towards grandparent.
                    restoreInvariants(grandparent);
                } else if ((uncle != null) && (uncle.color == BLACK)) { // Case 3.2: The uncle is black.
                    if (toRestore == parent.right) { // Case 2.2.1: toRestore is the right child.
                        /*
                                 Black
                                /   \
                               Red   Black
                                 \
                               Red (Curr)
                         */
                        leftRotate(parent);
                        // In this way, the same case applies to the newly left rotated parent.
                        // Then we simply need to recurse towards parent.
                        restoreInvariants(parent);
                    } else { // Case 2.2.2: toRestore is the left child.
                        /*
                                 Black
                                 /   \
                               Red   Black
                              /
                            Red (Curr)
                         */
                        // Recolor the parent to black, and the grandparent to red
                        parent.color = BLACK;
                        grandparent.color = RED;
                        rightRotate(grandparent);
                    }
                }
            } else {
                Node uncle = grandparent.left;
                if ((uncle != null) && (uncle.color == RED)) { // Case 2.1: The uncle is red.
                    restoreParentAndUncleAreRed(grandparent, parent, uncle);
                    restoreInvariants(grandparent);
                } else if ((uncle != null) && (uncle.color == BLACK)) { // Case 3.2: The uncle is black.
                    if (toRestore == parent.left) { // Case 2.2.1: toRestore is the left child.
                        /*
                                 Black
                                 /   \
                              Black   Red
                                     /
                                Red (Curr)
                         */
                        rightRotate(parent);
                        // In this way, the same case applies to the newly left rotated parent.
                        // Then we simply need to recurse towards parent.
                        restoreInvariants(parent);
                    } else { // Case 2.2.2: toRestore is the right child.
                        /*
                                 Black
                                /   \
                             Black   Red
                                       \
                                    Red (Curr)
                         */
                        // Recolor the parent to black, and the grandparent to red
                        parent.color = BLACK;
                        grandparent.color = RED;
                        leftRotate(grandparent);
                    }
                }
            }
        }

        // Recolor the root to black to ensure invariant #2
        root.color = BLACK;
        // Time: O(log n)
    }

    /**
     * Helper method to restore the invariants when the parent and the uncle are
     * both red.
     * @param grandparent grandparent
     * @param parent parent node
     * @param uncle uncle node
     */
    private void restoreParentAndUncleAreRed(Node grandparent, Node parent, Node uncle) {
        grandparent.color = RED;
        parent.color = BLACK;
        uncle.color = BLACK;
        // Time: O(1)
    }

    /**
     * Helper method to do a left rotation on the given node.
     * @param toRotate node to rotate on
     */
    private void leftRotate(Node toRotate) {
        // Temporarily store the parent and the right child
        Node parent = toRotate.parent;
        Node tmp = toRotate.right;

        // Reconnect the references to achieve left rotation
        toRotate.right = tmp.left;
        if (tmp.left != null) {
            tmp.left.parent = toRotate;
        }

        tmp.left = toRotate;
        toRotate.parent = tmp;

        if (parent != null) {
            if (toRotate == parent.left) {
                parent.left = tmp;
            } else {
                parent.right = tmp;
            }
        }
        tmp.parent = parent;

        // Take care of the root
        if (toRotate == root) {
            root = tmp;
        }
        // Time: O(1)
    }

    /**
     * Helper method to do a right rotation on the given node.
     * @param toRotate node to rotate on
     */
    private void rightRotate(Node toRotate) {
        // Temporarily store the parent and the left child
        Node parent = toRotate.parent;
        Node tmp = toRotate.left;

        // Reconnect the references to achieve right rotation
        toRotate.left = tmp.right;
        if (tmp.right != null) {
            tmp.right.parent = toRotate;
        }

        tmp.right = toRotate;
        toRotate.parent = tmp;

        if (parent != null) {
            if (toRotate == parent.left) {
                parent.left = tmp;
            } else {
                parent.right = tmp;
            }
        }
        tmp.parent = parent;

        // Take care of the root
        if (toRotate == root) {
            root = tmp;
        }
        // Time: O(1)
    }

}
