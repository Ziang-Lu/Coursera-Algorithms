package undirected_graph;

/**
 * Edge class.
 *
 * @author Ziang Lu
 */
class UndirectedEdge {

    /**
     * Endpoint 1.
     */
    private Vertex end1;
    /**
     * Endpoint 2.
     */
    private Vertex end2;

    /**
     * Constructor with parameter.
     * @param end1 endpoint1
     * @param end2 endpoint2
     */
    UndirectedEdge(Vertex end1, Vertex end2) {
        this.end1 = end1;
        this.end2 = end2;
    }

    /**
     * Accessor of end1.
     * @return end1
     */
    Vertex end1() {
        return end1;
    }

    /**
     * Accessor of end2.
     * @return end2
     */
    Vertex end2() {
        return end2;
    }

    /**
     * Mutator of end1.
     * @param end1 end1
     */
    void setEnd1(Vertex end1) {
        this.end1 = end1;
    }

    /**
     * Mutator of end2.
     * @param end2 end2
     */
    void setEnd2(Vertex end2) {
        this.end2 = end2;
    }

    @Override
    public String toString() {
        return String.format("Edge connecting Vertex #%d and Vertex #%d", end1.id(), end2.id());
    }

}
