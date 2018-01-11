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
    final Vertex tail;
    /**
     * Head of this edge.
     */
    final Vertex head;

    /**
     * Constructor with parameter.
     * @param tail tail of this edge
     * @param head head of this edge
     */
    Edge(Vertex tail, Vertex head) {
        this.tail = tail;
        this.head = head;
    }

    @Override
    public String toString() {
        return String.format("Edge from Vertex #%d to Vertex #%d", tail.getID(), head.getID());
    }

}
