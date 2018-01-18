/**
 * A very simple self-balanced AVL Tree implementation.
 *
 * Invariance of an AVL Tree:
 * The heights of the left and right sub-trees of any node in an AVL Tree won't
 * differ more than 1.
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
         * Constructor with parameter.
         * @param key key to store
         */
        Node(int key) {
            this.key = key;
        }

        @Override
        public String toString() {
            StringBuilder s = new StringBuilder();
            s.append("[").append(key).append("]");
            return s.toString();
        }
    }

    /**
     * Root of this AVL Tree.
     */
    private Node root;

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
    public void insert(int key) {
        if (root == null) {
            root = new Node(key);
            return;
        }

        root = insertHelper(key, null, root, true);
    }

    /**
     * Private helper method to insert the given key to the given sub-tree.
     * @param key key to insert
     * @param parent parent node
     * @param curr current node
     * @param isLC whether the current node is the left child
     * @return root of the sub-tree after insertion
     */
    private Node insertHelper(int key, Node parent, Node curr, boolean isLC) {
        // Base case 1: Found the spot to insert
        if (curr == null) {
            Node newNode = new Node(key);
            if (isLC) {
                parent.left = newNode;
            } else {
                parent.right = newNode;
            }
            return newNode;
        }
        // Base case 2: Found it
        if (curr.key == key) {
            // No duplicate allowed
            return curr;
        }

        // Recursive case
        if (curr.key > key) {
            curr.left = insertHelper(key, curr, curr.left, true);
        } else {
            curr.right = insertHelper(key, curr, curr.right, false);
        }

        // An insertion in the left or right sub-tree may break the balance of the current node.
        int balance = getBalance(curr);
        // For detailed explanation, please refer to the tutorial.
        if ((balance > 1) && (getBalance(curr.left) > 0)) {
            // Left-left imbalance
            // For the unbalanced node, the height of the left sub-tree is 2 higher than the right sub-tree;
            // for the left child, the height of the left sub-tree is 1 higher than the right sub-tree.
            return rightRotate(curr);
        } else if ((balance < -1) && (getBalance(curr.right) < 0)) {
            // Right-right imbalance
            // For the unbalanced node, the height of the right sub-tree is 2 higher than the left sub-tree;
            // for the right child, the height of the right sub-tree is 1 higher than the left sub-tree.
            return leftRotate(curr);
        } else if ((balance > 1) && (getBalance(curr.left) < 0)) {
            // Left-right imbalance
            // For the unbalanced node, the height of the left sub-tree is 2 higher than the right sub-tree;
            // for the left child, the height of the right sub-tree is 1 higher than the left sub-tree.

            // First do a left rotation towards the left child, making the case a left-left imbalance
            curr.left = leftRotate(curr.left);

            return rightRotate(curr);
        } else if ((balance < -1) && (getBalance(curr.right) > 0)) {
            // Right-left imbalance
            // For the unbalanced node, the height of the right sub-tree is 2 higher than the left sub-tree;
            // for the right child, the height of the left sub-tree is 1 higher than the right sub-tree.

            // First do a right rotation towards the right child, making the case a right-right imbalance
            curr.right = rightRotate(curr.right);

            return leftRotate(curr);
        }

        // The insertion doesn't break the balance of the current node.
        return curr;
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

        int leftHeight = getHeight(node.left), rightHeight = getHeight(node.right);
        return leftHeight - rightHeight;
    }

    /**
     * Helper method to calculate the height of the given node recursively.
     * @param node given node
     * @return height of the node
     */
    private int getHeight(Node node) {
        // Base case
        if (node == null) {
            return 0;
        }
        // Recursive case
        int leftHeight = getHeight(node.left), rightHeight = getHeight(node.right);
        return 1 + Math.max(leftHeight, rightHeight);
    }

    /**
     * Helper method to do a right rotation towards the given unbalanced node.
     * @param unbalanced given unbalanced node
     * @return root of the sub-tree after right rotation
     */
    private Node rightRotate(Node unbalanced) {
        // Temporarily store the left child
        Node tmp = unbalanced.left;

        // Reconnect the references to realize right rotation
        unbalanced.left = unbalanced.left.right;
        tmp.right = unbalanced;

        return tmp;
    }

    /**
     * Helper method to do a left rotation towards the given unbalanced node.
     * @param unbalanced given unbalanced node
     * @return root of the sub-tree after left rotation
     */
    private Node leftRotate(Node unbalanced) {
        // Temporarily store the right child
        Node tmp = unbalanced.right;

        // Reconnect the references to realize left rotation
        unbalanced.right = unbalanced.right.left;
        tmp.left = unbalanced;

        return tmp;
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

}
