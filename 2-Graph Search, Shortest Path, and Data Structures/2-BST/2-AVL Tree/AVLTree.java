import java.util.ArrayList;
import java.util.List;

/**
 * A very simple self-balanced AVL-Tree implementation.
 *
 * Invariant of an AVL-Tree:
 * The heights of the left and right subtrees of any node won't differ more than
 * 1.
 *
 * @author Ziang Lu
 */
public class AVLTree implements BSTInterface {

    /**
     * Static nested Node class.
     */
    private static class Node {
        /**
         * Key of this node.
         */
        private final int key;
        /**
         * Reference to left and right children.
         */
        private Node left, right;
        /**
         * Height of this node.
         */
        private int height;

        /**
         * Constructor with parameter.
         * @param key key of the node
         */
        Node(int key) {
            this.key = key;
            left = null;
            right = null;
            height = 1;
        }

        /**
         * Updates the height of this node.
         */
        void updateHeight() {
            int leftHeight = 0, rightHeight = 0;
            if (left != null) {
                leftHeight = left.height;
            }
            if (right != null) {
                rightHeight = right.height;
            }
            height = 1 + Math.max(leftHeight, rightHeight);
        }
    }

    /**
     * Makes a right rotation towards the given unbalanced node.
     * @param unbalanced unbalanced node
     * @return root of the subtree after right rotation
     */
    private static Node rightRotate(Node unbalanced) {
        // Temporarily store the left child
        Node tmp = unbalanced.left;

        // Reconnect the references to achieve right rotation
        unbalanced.left = unbalanced.left.right;
        tmp.right = unbalanced;
        // Also, take care of the height
        unbalanced.updateHeight();
        tmp.updateHeight();

        return tmp;
        // Time: O(1)
    }

    /**
     * Makes a left rotation towards the given unbalanced node.
     * @param unbalanced unbalanced node
     * @return root of the subtree after left rotation
     */
    private static Node leftRotate(Node unbalanced) {
        // Temporarily store the right child
        Node tmp = unbalanced.right;

        // Reconnect the references to achieve left rotation
        unbalanced.right = unbalanced.right.left;
        tmp.left = unbalanced;
        // Also, take care of the height
        unbalanced.updateHeight();
        tmp.updateHeight();

        return tmp;
        // Time: O(1)
    }

    /**
     * Root of this AVL-Tree.
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
            root = new Node(key);
            return;
        }
        root = insertHelper(key, new ArrayList<>(), root);
    }

    /**
     * Private helper method to insert the given key to the given subtree
     * recursively.
     * @param key key to insert
     * @param path nodes along the path
     * @param curr current node
     * @return root of the subtree after insertion
     */
    private Node insertHelper(int key, List<Node> path, Node curr) {
        // Base case 1: Found the spot to insert
        if (curr == null) {
            Node parent = path.get(path.size() - 1);
            Node newNode = new Node(key);
            if (curr == parent.left) {
                parent.left = newNode;
            } else {
                parent.right = newNode;
            }
            updatePathHeight(path);
            return newNode;
        }
        // Base case 2: Found it
        if (curr.key == key) {
            // No duplicates allowed
            return curr;
        }

        // Recursive case
        path.add(curr);
        if (curr.key > key) {
            curr.left = insertHelper(key, path, curr.left, true);
        } else {
            curr.right = insertHelper(key, path, curr.right, false);
        }
        // An insertion in the left or right subtree may break the balance of the current node.
        return rebalance(curr, path);
        // For insertion, there is at most one rebalancing operation when backtracking and rebalancing, since after
        // rebalancing the first encountered unbalanced node, all of its upper nodes remain balanced.
        // T(n) = T(n/2) + O(1)
        // a = 1, b = 2, d = 0
        // According to Master Method, time: O(log n)
    }

    /**
     * Helper method to update the heights of the nodes along the path.
     * @param path nodes along the path
     */
    private void updatePathHeight(List<Node> path) {
        for (int i = path.size() - 1; i >= 0; --i) {
            path.get(i).updateHeight();
        }
        // Time: O(log n)
    }

    /**
     * Helper method to rebalance along the given path.
     * @param curr current node
     * @param path path to rebalance
     * @return root of the subtree after rebalancing
     */
    private Node rebalance(Node curr, List<Node> path) {
        int balance = getBalance(curr);
        // For detailed explanation, please refer to the tutorial
        if ((balance > 1) && (getBalance(curr.left) > 0)) { // "Left-left imbalance"
            // For the unbalanced node, the height of the left subtree is 2 higher than the right subtree;
            // for the left child, the height of the left subtree is 1 higher than the right subtree.
            Node newRoot = rightRotate(curr);
            updatePathHeight(path);
            return newRoot;
        } else if ((balance < -1) && (getBalance(curr.right) < 0)) { // "Right-right imbalance"
            // For the unbalanced node, the height of the right subtree is 2 higher than the left subtree;
            // for the right child, the height of the right subtree is 1 higher than the left subtree.
            Node newRoot = leftRotate(curr);
            updatePathHeight(path);
            return newRoot;
        } else if ((balance > 1) && (getBalance(curr.left) < 0)) { // "Left-right imbalance"
            // For the unbalanced node, the height of the left subtree is 2 higher than the right subtree;
            // for the left child, the height of the right subtree is 1 higher than the left subtree.

            // First do a left rotation towards the left child, making the case a left-left imbalance
            curr.left = leftRotate(curr.left);

            Node newRoot = rightRotate(curr);
            updatePathHeight(path);
            return newRoot;
        } else if ((balance < -1) && (getBalance(curr.right) > 0)) { // "Right-left imbalance"
            // For the unbalanced node, the height of the right subtree is 2 higher than the left subtree;
            // for the right child, the height of the left subtree is 1 higher than the right subtree.

            // First do a right rotation towards the right child, making the case a right-right imbalance
            curr.right = rightRotate(curr.right);

            Node newRoot = leftRotate(curr);
            updatePathHeight(path);
            return newRoot;
        }

        // The insertion doesn't break the balance of the current node.
        return curr;
        // Time: O(log n)
    }

    /**
     * Helper method to calculate the balance of the given node.
     * @param node given node
     * @return balance of the node
     */
    private int getBalance(Node node) {
        if (node == null) {
            return 0;
        }
        int leftHeight = 0, rightHeight = 0;
        if (node.left != null) {
            leftHeight = node.left.height;
        }
        if (node.right != null) {
            rightHeight = node.right.height;
        }
        return leftHeight - rightHeight;
        // Time: O(1)
    }

}
