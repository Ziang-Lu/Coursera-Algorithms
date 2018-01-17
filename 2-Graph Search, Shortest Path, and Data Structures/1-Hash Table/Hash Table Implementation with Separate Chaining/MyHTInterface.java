/**
 * A very simple hash table interface, mapping lowercase words to their
 * frequencies.
 *
 * @author Ziang Lu
 */
public interface MyHTInterface {

    /**
     * Calculates the hash value of the given text.
     * @param text text to calculate hash value
     * @return hash value of the word
     */
    int hashValue(String text);

    /**
     * Returns the number of items in the hash table.
     * @return number of items in the hash table
     */
    int size();

    /**
     * Checks whether the given text is in the hash table.
     * @param text text to check
     * @return whether the word is in the hash table
     */
    boolean contains(String text);

    /**
     * Inserts the given text to the hash table.
     * No duplicate allowed
     * @param text text to insert
     */
    void insert(String text);

    /**
     * Removes the given text from the hash table.
     * @param text text to remove
     * @return word if found, null if not found
     */
    String remove(String text);

    /**
     * Displays the hash table.
     */
    void display();

}
