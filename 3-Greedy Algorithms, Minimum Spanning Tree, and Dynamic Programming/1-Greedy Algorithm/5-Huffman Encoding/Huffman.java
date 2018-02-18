import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

/**
 * Huffman encoding implementation.
 *
 * @author Ziang Lu
 */
public class Huffman {

    /**
     * Static nested Node class.
     */
    private static class Node implements Comparable<Node> {
        /**
         * Character stored in this Node.
         */
        private Character c;
        /**
         * Frequency of the character.
         */
        private int freq;
        /**
         * Reference to left child.
         */
        Node left;
        /**
         * Reference to right child.
         */
        Node right;

        /**
         * Constructor with parameter.
         * @param c character to store
         * @param freq frequency of the character
         */
        Node(Character c, int freq) {
            this(c, freq, null, null);
        }

        /**
         * Constructor with parameter.
         * @param c character to store
         * @param freq frequency of the character
         * @param left left child
         * @param right right child
         */
        Node(Character c, int freq, Node left, Node right) {
            this.c = c;
            this.freq = freq;
            this.left = left;
            this.right = right;
        }

        @Override
        public int compareTo(Node o) {
            return Integer.compare(freq, o.freq);
        }
    }

    /**
     * Root of the Huffman tree.
     */
    private Node huffmanTreeRoot;
    /**
     * Encoding map.
     */
    private HashMap<Character, String> encodingMap;

    /**
     * Constructor with parameter.
     * @param s string to build the Huffman tree
     */
    public Huffman(String s) {
//        constructHuffmanTreeWithHeap(s);
        constructHuffmanTreeWithTwoQueues(s);
    }

    /**
     * Constructs the Huffman tree using the given string with a heap.
     * @param s string to build the Huffman tree
     */
    public void constructHuffmanTreeWithHeap(String s) {
        // Check whether the input string is null or empty
        if ((s == null) || (s.length() == 0)) {
            huffmanTreeRoot = new Node(null, 0);
            return;
        }

        // Create a map between characters and their frequencies   [O(n)]
        HashMap<Character, Integer> freqOfChars = getFreqOfChars(s);

        // Create a Node heap from the map   [O(nlog n)]
        PriorityQueue<Node> heap = new PriorityQueue<Node>();
        for (Map.Entry<Character, Integer> entry : freqOfChars.entrySet()) {
            heap.offer(new Node(entry.getKey(), entry.getValue()));
        }

        // Construct the Huffman tree using a heap   [O(nlog n)]
        while (heap.size() > 1) {
            // Take out two nodes with minimum frequency from the heap
            Node minNode1 = heap.poll();
            Node minNode2 = heap.poll();
            // Combine the two nodes
            Node combined = new Node(null, minNode1.freq + minNode2.freq, minNode1, minNode2);
            // Put the combined node back to the heap
            heap.offer(combined);
        }
        // By now there is only one node in the heap, which is exactly the root of the Huffman tree.
        huffmanTreeRoot = heap.poll();

        // Create the encoding   [O(n)]
        encodingMap = new HashMap<Character, String>();
        createEncoding(huffmanTreeRoot, new StringBuilder());
        // Overall running time complexity: O(nlog n)
    }

    /**
     * Private helper method to create a map between the characters in the given
     * string and their frequencies.
     * @param s given string
     * @return map created map
     */
    private HashMap<Character, Integer> getFreqOfChars(String s) {
        HashMap<Character, Integer> freqOfChars = new HashMap<Character, Integer>();
        for (char c : s.toCharArray()) {
            Integer freq = freqOfChars.getOrDefault(c, 0);
            ++freq;
            freqOfChars.put(c, freq);
        }
        return freqOfChars;
        // Running time complexity: O(n)
    }

    /**
     * Private helper method to create the encoding recursively.
     * @param curr current node
     * @param encodingSoFar encoding so far
     */
    private void createEncoding(Node curr, StringBuilder encodingSoFar) {
        // Base case
        if ((curr.left == null) && (curr.right == null)) {
            encodingMap.put(curr.c, encodingSoFar.toString());
            return;
        }
        // Recursive case
        createEncoding(curr.left, encodingSoFar.append('0'));
        encodingSoFar.deleteCharAt(encodingSoFar.length() - 1); // Remember to remove the appended character back
        createEncoding(curr.right, encodingSoFar.append('1'));
        encodingSoFar.deleteCharAt(encodingSoFar.length() - 1); // Remember to remove the appended character back
        // T(n) = 2T(n/2) + O(1)
        // a = 2, b = 2, d = 0
        // According to Master Method, the running time complexity is O(n).
    }

    /**
     * Constructs the Huffman tree using the given string with two queues.
     * @param s string to build the Huffman tree
     */
    public void constructHuffmanTreeWithTwoQueues(String s) {
        // Check whether the input string is null or empty
        if ((s == null) || (s.length() == 0)) {
            huffmanTreeRoot = new Node(null, 0);
            return;
        }

        // Create a map between characters and their frequencies
        HashMap<Character, Integer> freqOfChars = getFreqOfChars(s);

        // Create a Node array and sort it   [O(nlog n)]
        ArrayList<Node> nodes = new ArrayList<Node>();
        for (Map.Entry<Character, Integer> entry : freqOfChars.entrySet()) {
            nodes.add(new Node(entry.getKey(), entry.getValue(), null, null));
        }
        Collections.sort(nodes);

        // Construct the Huffman tree using two queues   [O(n)]
        // "primitives" contains the nodes with the original character keys, while "merged" contains the merged nodes.
        // According to the implementation, both of the queues are kept sorted.
        ArrayDeque<Node> primitives = new ArrayDeque<Node>(nodes), merged = new ArrayDeque<Node>();
        while ((primitives.size() != 0) || (merged.size() != 1)) {
            // Take out two nodes with minimum frequency from the two queues
            Node minNode1 = getMinFreqNode(primitives, merged);
            Node minNode2 = getMinFreqNode(primitives, merged);
            // Combine the two nodes
            Node combined = new Node(null, minNode1.freq + minNode2.freq, minNode1, minNode2);
            // Put the combined node to the merged queue
            merged.offer(combined);
        }
        // By now there is only one node in the merged queue, which is exactly the root of the Huffman tree.
        huffmanTreeRoot = merged.pop();

        // Create the encoding [O(n)]
        encodingMap = new HashMap<Character, String>();
        createEncoding(huffmanTreeRoot, new StringBuilder());
        // Overall running time complexity: O(nlog n)
    }

    /**
     * Private helper method to get a node with minimum frequency from the given
     * two queues.
     * @param primitives queue of primitive nodes
     * @param merged queue of merged nodes
     * @return node with minimum frequency
     */
    private Node getMinFreqNode(ArrayDeque<Node> primitives, ArrayDeque<Node> merged) {
        if (primitives.size() == 0) {
            return merged.poll();
        } else if (merged.size() == 0) {
            return primitives.poll();
        }

        Node primitivesFront = primitives.peek();
        Node mergedFront = merged.peek();
        if (primitivesFront.compareTo(mergedFront) <= 0) {
            return primitives.pop();
        } else {
            return merged.pop();
        }
    }

    /**
     * Encodes the given message.
     * @param msg message to encode
     * @return encoded string
     */
    public String encode(String msg) {
        // Check whether the input string is null or empty
        if ((msg == null) || (msg.length() == 0)) {
            return "";
        }

        StringBuilder s = new StringBuilder();
        for (char c : msg.toCharArray()) {
            s.append(encodingMap.get(c));
        }
        return s.toString();
    }

    /**
     * Decodes the given encoded string.
     * @param encoded encoded string to decode
     * @return decoded message
     */
    public String decode(String encoded) {
        // Check whether the input string is null or empty
        if ((encoded == null) || (encoded.length() == 0)) {
            return "";
        }

        StringBuilder msg = new StringBuilder();
        decodeHelper(encoded, 0, huffmanTreeRoot, msg);
        return msg.toString();
    }

    /**
     * Private helper method to decode the encoded string recursively.
     * @param encoded encoded string to decode
     * @param idx index of the character in the encoded string to decode
     * @param curr current node
     * @param s decoded message
     */
    private void decodeHelper(String encoded, int idx, Node curr, StringBuilder s) {
        // Base case 1: Decoded all the encoded string
        if (idx >= encoded.length()) {
            s.append(curr.c);
            return;
        }
        // Base case 2: Reach a leaf
        if ((curr.left == null) && (curr.right == null)) {
            s.append(curr.c);
            // Restart from the root
            decodeHelper(encoded, idx, huffmanTreeRoot, s);
            return;
        }

        // Recursive case
        if (encoded.charAt(idx) == '0') {
            decodeHelper(encoded, idx + 1, curr.left, s);
        } else {
            decodeHelper(encoded, idx + 1, curr.right, s);
        }
    }

}
