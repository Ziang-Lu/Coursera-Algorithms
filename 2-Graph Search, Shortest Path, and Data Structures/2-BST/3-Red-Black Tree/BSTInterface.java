/**
 * Binary Search Tree simple interface.
 *
 * @author Ziang Lu
 */
public interface BSTInterface {
    /**
     * Searches for the given key in the BST.
     * @param key key to search for
     * @return whether the key is in the BST
     */
    boolean search(int key);

    /**
     * Inserts the given key to the BST.
     * No duplicates allowed
     * @param key key to insert
     */
    void insert(int key);
}
