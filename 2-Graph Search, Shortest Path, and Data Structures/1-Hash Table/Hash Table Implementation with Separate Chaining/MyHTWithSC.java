import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * A very simple hash table implementation with separate chaining, mapping
 * lowercase words to their frequencies.
 *
 * @author Ziang Lu
 */
public class MyHTWithSC implements MyHTInterface {

    /**
     * Static nested DataItem class.
     */
    private static class DataItem {
        /**
         * Data stored in this DataItem.
         */
        private String data;
        /**
         * Frequency of the data.
         */
        private int freq;

        /**
         * Constructor with parameter.
         * @param data data to store
         */
        DataItem(String data) {
            this.data = data;
            freq = 1;
        }

        /**
         * Adds 1 to the frequency.
         */
        void addFreq() {
            ++freq;
        }

        @Override
        public String toString() {
            StringBuilder s = new StringBuilder();
            s.append("[").append(data).append(", ").append(freq).append("]");
            return s.toString();
        }
    }

    /**
     * Default capacity.
     */
    private static final int DEFAULT_CAPACITY = 10;
    /**
     * Load factor.
     */
    private static final double LOAD_FACTOR = 5.0;

    /**
     * Underlying data items.
     */
    private List<LinkedList<DataItem>> data;
    /**
     * Number of data items.
     */
    private int nItem;

    /**
     * Default capacity.
     */
    public MyHTWithSC() {
        this(DEFAULT_CAPACITY);
    }

    /**
     * Constructor with parameter.
     * @param initialCapacity
     */
    public MyHTWithSC(int initialCapacity) {
        // Check whether the input initial capacity is positive
        if (initialCapacity <= 0) {
            throw new IllegalArgumentException("The initial capacity should be positive.");
        }

        data = initializeData(initialCapacity);
        nItem = 0;
    }

    /**
     * Private helper method to initialize data list with the given capacity.
     * @param capacity capacity
     * @return created data list
     */
    private List<LinkedList<DataItem>> initializeData(int capacity) {
        List<LinkedList<DataItem>> data = new ArrayList<>();
        for (int i = 0; i < capacity; ++i) {
            data.add(new LinkedList<DataItem>());
        }
        return data;
    }

    @Override
    public int hashValue(String text) {
        // Check whether the input string is a lowercase word
        if (!isLowerWord(text)) {
            return -1;
        }

        return text.hashCode() % data.size();
    }

    /**
     * Private helper method to determine whether the given text is a lowercase
     * word.
     * @param text text to check
     * @return whether the given text is a lowercase word
     */
    private boolean isLowerWord(String text) {
        return (text != null) && (text.matches("^[a-z]+$"));
    }

    @Override
    public int size() {
        return nItem;
    }

    @Override
    public boolean contains(String text) {
        // Check whether the input string is a lowercase word
        if (!isLowerWord(text)) {
            return false;
        }

        int hashVal = hashValue(text);
        LinkedList<DataItem> chaining = data.get(hashVal);
        for (DataItem item : chaining) {
            if (item.data.equals(text)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void insert(String text) {
        // Check whether the input string is a lowercase word
        if (!isLowerWord(text)) {
            return;
        }

        int hashVal = hashValue(text);
        LinkedList<DataItem> chaining = data.get(hashVal);
        for (DataItem item : chaining) {
            if (item.data.equals(text)) {
                // No duplicate allowed
                item.addFreq();
                return;
            }
        }
        chaining.add(new DataItem(text));
        ++nItem;
        if ((1.0 * nItem / data.size()) > LOAD_FACTOR) {
            rehash();
        }
    }

    /**
     * Private helper method to rehash when the load factor is reached.
     */
    private void rehash() {
        // Rather than insert the words again, we simply need to reconnect the references between two lists
        // Temporarily store the original list
        List<LinkedList<DataItem>> tmp = data;
        // Create the new list
        int newSize = findNextPrime(data.size() * 2 + 1);
        data = initializeData(newSize);
        for (LinkedList<DataItem> chaining : tmp) {
            for (DataItem item : chaining) {
                int hashVal = hashValue(item.data);
                data.get(hashVal).add(item);
            }
        }
    }

    /**
     * Helper method to find the next prime starting from the given number.
     * @param start starting number
     * @return next prime
     */
    private int findNextPrime(int start) {
        int n = start;
        while (!isPrime(n)) {
            ++n;
        }
        return n;
    }

    /**
     * Helper method to determine whether the given number is prime.
     * @param n number to check
     * @return whether the given number is prime
     */
    private boolean isPrime(int n) {
        if (n <= 1) {
            return false;
        }
        if (n == 2) {
            return true;
        }
        if ((n % 2) == 0) {
            return false;
        }
        for (int i = 3; i < Math.sqrt(n); i += 2) {
            if ((n % i) == 0) {
                return false;
            }
        }
        return true;
    }


    @Override
    public String remove(String text) {
        // Check whether the input string is a lowercase word
        if (!isLowerWord(text)) {
            return null;
        }

        int hashVal = hashValue(text);
        LinkedList<DataItem> chaining = data.get(hashVal);
        int idxToRemove = -1;
        for (int i = 0; i < chaining.size(); ++i) {
            if (chaining.get(i).data.equals(text)) {
                idxToRemove = i;
                break;
            }
        }
        if (idxToRemove == -1) { // Not found
            return null;
        }
        String removedData = chaining.remove(idxToRemove).data;
        --nItem;
        return removedData;
    }

    @Override
    public void display() {
        StringBuilder s = new StringBuilder();
        for (LinkedList<DataItem> chaining : data) {
            s.append(chaining).append('\n');
        }
        System.out.println(s.toString().trim());
    }

}
