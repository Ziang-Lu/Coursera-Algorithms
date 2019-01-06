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
         * @param color color of this node
         */
        Node(int key, boolean color) {
            this.key = key;
            parent = null;
            left = null;
            right = null;
            this.color = color;
        }

        @Override
        public String toString() {
            StringBuilder s = new StringBuilder();
            s.append("[").append(key).append(" ");
            if (color == RED) {
                s.append("R");
            } else {
                s.append("B");
            }
            s.append("]");
            return s.toString();
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

    /**
     * Searches for the given key in the Red-Black Tree.
     * @param key key to search for
     * @return whether the key is in the Red-Black Tree
     */
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
    }

    /**
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
     */
    public void insert(int key) {
        insertHelper(key, null, root, true);
    }

    /**
     * Private helper method to insert the given key to the given subtree
     * recursively.
     * @param key key to insert
     * @param curr current node
     * @param isLC whether the current node is the left child
     */
    private void insertHelper(int key, Node parent, Node curr, boolean isLC) {
        // Base case 1: Found the spot to insert
        if (curr == null) {
            Node newNode = new Node(key, RED);
            newNode.parent = parent;
            if (parent == null) {
                root = newNode;
            } else {
                if (isLC) {
                    parent.left = newNode;
                } else {
                    parent.right = newNode;
                }
            }

            // Restore the invariants   [O(log n)]
            restoreInvariants(newNode);
            return;
        }
        // Base case 2: Found it
        if (curr.key == key) {
            // No duplicate allowed
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
        // According to Master Method, the overall running time complexity is O(log n).
    }

    /**
     * Helper method to restore the invariants.
     * @param toRestore node to restore (must be red)
     */
    private void restoreInvariants(Node toRestore) {
        // Case 1: Insert the first node (as the root)
        // Since toRestore is red, this only violates invariant #2.
        // We simply need to recolor toRestore to black.
        if (toRestore == root) {
            toRestore.color = BLACK;
            return;
        }

        // Case 2: The parent is black.
        // This won't violate invariant #3.

        // Case 3: The parent is red.
        // This violates invariant #3.
        Node parent = toRestore.parent;
        if (parent.color == RED) {
            // Then toRestore must have a grandparent, and it must be black according to invariant #3.
            Node grandparent = toRestore.parent.parent;

            if (parent == grandparent.left) {
                Node uncle = grandparent.right;

                if ((uncle != null) && (uncle.color == RED)) { // Case 3.1: The uncle is red.
                    // To restore invariant #3 and #4, simply recolor the grandparent to red and the parent and the uncle to
                    // black.
                    restoreParentAndUncleAreRed(grandparent, parent, uncle);

                    // In this way, the same case applies to the newly recolored grandparent.
                    // Then we simply need to recurse towards grandparent.
                    restoreInvariants(grandparent);
                } else { // Case 3.2: The uncle is black.
                    if (toRestore == parent.right) { // Case 3.2.1: toRestore is the right child.
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
                    } else { // Case 3.2.2: toRestore is the left child.
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

                if ((uncle != null) && (uncle.color == RED)) { // Case 3.1: The uncle is red.
                    restoreParentAndUncleAreRed(grandparent, parent, uncle);
                    restoreInvariants(grandparent);
                } else { // Case 3.2: The uncle is black.
                    if (toRestore == parent.left) { // Case 3.2.1: toRestore is the left child.
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
                    } else { // Case 3.2.2: toRestore is the right child.
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
        // Running time complexity: O(log n)
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
        // Running time complexity: O(1)
    }

    /**
     * Helper method to do a left rotation on the given node.
     * @param toRotate node to rotate on
     */
    private void leftRotate(Node toRotate) {
        // Temporarily store the parent and the right child
        Node parent = toRotate.parent;
        Node tmp = toRotate.right;

        // Reconnect the references to realize left rotation
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

        if (toRotate == root) {
            root = tmp;
        }
        // Running time complexity: O(1)
    }

    /**
     * Helper method to do a right rotation on the given node.
     * @param toRotate node to rotate on
     */
    private void rightRotate(Node toRotate) {
        // Temporarily store the parent and the left child
        Node parent = toRotate.parent;
        Node tmp = toRotate.left;

        // Reconnect the references to realize right rotation
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

        if (toRotate == root) {
            root = tmp;
        }
        // Running time complexity: O(1)
    }

    /**
     * Traverses this Red-Black Tree in-order.
     */
    public void traverseInOrder() {
        StringBuilder s = new StringBuilder();
        traverseInOrderHelper(root, s);
        System.out.println(s.toString().trim());
    }

    /**
     * Private helper method to traverse the given subtree in-order recursively.
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
        s.append(curr.toString()).append(' ');
        traverseInOrderHelper(curr.right, s);
    }

}
