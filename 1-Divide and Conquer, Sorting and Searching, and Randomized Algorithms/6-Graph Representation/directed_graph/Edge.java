package directed_graph;

/**
 * Edge class.
 *
 * @author Ziang Lu
 */
class Edge {

    /**
     * Tail of this edge.
     */
    private Vertex tail;
    /**
     * Head of this edge.
     */
    private Vertex head;

    /**
     * Constructor with parameter.
     * @param tail tail of this edge
     * @param head head of this edge
     */
    Edge(Vertex tail, Vertex head) {
        this.tail = tail;
        this.head = head;
    }

    /**
     * Accessor of tail.
     * @return tail
     */
    Vertex getTail() {
        return tail;
    }

    /**
     * Accessor of head.
     * @return head
     */
    Vertex getHead() {
        return head;
    }

    /**
     * Mutator of tail.
     * @param tail tail
     */
    void setTail(Vertex tail) {
        this.tail = tail;
    }

    /**
     * Mutator of head.
     * @param head head
     */
    void setHead(Vertex head) {
        this.head = head;
    }

    @Override
    public String toString() {
        return String.format("Edge from Vertex #%d to Vertex #%d", tail.getID(), head.getID());
    }

}
