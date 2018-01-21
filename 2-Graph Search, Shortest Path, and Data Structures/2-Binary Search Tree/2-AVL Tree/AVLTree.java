/**
 * A very simple self-balanced AVL Tree implementation.
 *
 * Invariant of an AVL Tree:
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
            left = null;
            right = null;
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
     * Private helper method to insert the given key to the given sub-tree
     * recursively.
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
        return rebalance(curr);
        // T(n) = T(n/2) + O(n)
        // a = 1, b = 2, d = 1
        // According to Master Method, the overall running time complexity is O(n).

        // For insertion, there is at most one rebalancing operation when backtracking and rebalancing, since after
        // rebalancing the first encountered unbalanced node when backtracking, all of its upper nodes remain balanced.
    }

    /**
     * Helper method to rebalance the given node if it is unbalanced.
     * @param curr given node
     * @return root of the sub-tree after rebalancing
     */
    private Node rebalance(Node curr) {
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
        // Running time complexity: O(n)
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
        // Running time complexity: O(n)
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
        // T(n) = 2T(n/2) + O(1)
        // a = 2, b = 2, d = 0
        // According to Master Method, the running time complexity is O(n).
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
        // Running time complexity: O(1)
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
        // Running time complexity: O(1)
    }

    @Override
    public void delete(int key) {
        if (root == null) {
            return;
        }

        root = deleteHelper(key, root);
    }

    /**
     * Private helper method to delete the given key from the given sub-tree
     * recursively.
     * @param key key to delete
     * @param curr current node
     * @return root of the sub-tree after deletion
     */
    private Node deleteHelper(int key, Node curr) {
        // Case 1: Not found
        if (curr == null) {
            return null;
        }

        if (curr.key == key) { // Found it
            if ((curr.left == null) && (curr.right == null)) {
                // Case 2: The node to delete is a leaf.
                return null;
            } else if (curr.right == null) {
                // Case 3: The node only have left child.
                return curr.left;
            } else if (curr.left == null) {
                // Case 3: The node only have right child.
                return curr.right;
            } else {
                // Case 4: The node has both left and right children.
                Node successor = getSuccessor(curr);
                successor.left = curr.left;

                // A reconnection in the right sub-tree may break the balance of the current (successor) node.
                return rebalance(successor);
            }
        }

        // Recursive case
        if (curr.key > key) {
            curr.left = deleteHelper(key, curr.left);
        } else {
            curr.right = deleteHelper(key, curr.right);
        }

        // A deletion in the left or right sub-tree may break the balance of the current node.
        return rebalance(curr);
        // T(n) = T(n/2) + O(n)
        // a = 1, b = 2, d = 1
        // According to Master Method, the overall running time complexity is O(n).

        // For deletion, there could be multiple rebalancing operations when backtracking and rebalancing, since after
        // rebalancing the first encountered unbalanced node when backtracking, its upper nodes may also be unbalanced.
    }

    /**
     * Helper method to get the successor of the given node to delete.
     * Note that the given node to delete has both left and right children
     * @param nodeToDelete node to delete
     * @return successor
     */
    private Node getSuccessor(Node nodeToDelete) {
        return getSuccessorHelper(nodeToDelete, nodeToDelete, nodeToDelete.right);
        // Running time complexity: O(n)
    }

    /**
     * Helper method to get the successor of the given node to delete in the
     * given sub-tree recursively.
     * @param nodeToDelete node to delete
     * @param parent parent node
     * @param curr current node
     * @return successor
     */
    private Node getSuccessorHelper(Node nodeToDelete, Node parent, Node curr) {
        // Base case
        if (curr.left == null) {
            if (curr != nodeToDelete.right) {
                parent.left = curr.right;
                curr.right = nodeToDelete.right;
            }
            return curr;
        }
        // Recursive case
        Node successor = getSuccessorHelper(nodeToDelete, curr, curr.left);
        // A reconnection in the left sub-tree may break the balance of the current node.
        if (curr == nodeToDelete.right) {
            parent.right = rebalance(curr);
        } else {
            parent.left = rebalance(curr);
        }
        return successor;
        // T(n) = T(n/2) + O(n)
        // a = 1, b = 2, d = 1
        // According to Master Method, the running time complexity is O(n).
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

    /**
     * Main driver.
     * @param args arguments from command line
     */
    public static void main(String[] args) {
        AVLTree tree = new AVLTree();

        // Test insertion
        tree.insert(7);
        tree.insert(2);
        tree.insert(1); tree.insert(1);
        tree.insert(5);
        tree.insert(3);
        tree.insert(6);
        tree.insert(4);
        tree.insert(9);
        tree.insert(8);
        tree.insert(11); tree.insert(11);
        tree.insert(10);
        tree.insert(12);
        System.out.println("Testing insertion **********************************");
        System.out.println("At");
        tree.traverseInOrder(); // [1] [2] [3] [4] [5] [6] [7] [8] [9] [10] [11] [12]
        System.out.println("root: " + tree.root); // [5]
        System.out.println("root.left: " + tree.root.left); // [2]
        System.out.println("root.right: " + tree.root.right); // [9]
        System.out.println("****************************************************");
        System.out.println();

        // Test search
        System.out.println("Testing searching *******");
        System.out.println("Searching for 2: " + tree.search(2)); // true
        System.out.println("Searching for 100: " + tree.search(100)); // false
        System.out.println("*************************");
        System.out.println();

        // Test deletion
        System.out.println("Testing deletion ******************************");
        tree.delete(1);
        System.out.println("After deleting 1:");
        tree.traverseInOrder(); // [2] [3] [4] [5] [6] [7] [8] [9] [10] [11] [12]

        tree.delete(9);
        System.out.println("After deleting 9:");
        tree.traverseInOrder(); // [2] [3] [4] [5] [6] [7] [8] [10] [11] [12]

        tree.delete(11);
        System.out.println("After deleting 11:");
        tree.traverseInOrder(); // [2] [3] [4] [5] [6] [7] [8] [10] [12]
        System.out.println("***********************************************");
        System.out.println();
    }

}
