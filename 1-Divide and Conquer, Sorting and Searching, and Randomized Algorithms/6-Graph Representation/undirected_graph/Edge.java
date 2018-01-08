package undirected_graph;

/**
 * Edge class.
 *
 * @author Ziang Lu
 */
class Edge {

    /**
     * Endpoint 1.
     */
    final Vertex end1;
    /**
     * Endpoint 2.
     */
    final Vertex end2;

    /**
     * Constructor with parameter.
     * @param end1 endpoint1
     * @param end2 endpoint2
     */
    Edge(Vertex end1, Vertex end2) {
        this.end1 = end1;
        this.end2 = end2;
    }

    @Override
    public String toString() {
        return String.format("Edge connecting Vertex #%d and Vertex #%d", end1.vtxID, end2.vtxID);
    }

}
