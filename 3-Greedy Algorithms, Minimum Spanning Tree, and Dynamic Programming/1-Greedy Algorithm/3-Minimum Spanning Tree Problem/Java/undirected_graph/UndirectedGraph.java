package undirected_graph;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collections;
import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.Random;

import graph.GraphInterface;
import union_find.UnionFind;

/**
 * Adjacency list representation of a undirected graph.
 *
 * Note that parallel edges and self-loops are not allowed.
 * @author Ziang Lu
 */
public class UndirectedGraph implements GraphInterface {

    /**
     * Vertex list.
     */
    private final ArrayList<Vertex> vtxList;
    /**
     * Edge list.
     */
    private final ArrayList<UndirectedEdge> edgeList;

    /**
     * Default constructor.
     */
    public UndirectedGraph() {
        vtxList = new ArrayList<Vertex>();
        edgeList = new ArrayList<UndirectedEdge>();
    }

    @Override
    public void addVtx(int newVtxID) {
        // Check whether the input vertex is repeated
        if (findVtx(newVtxID) != null) {
            throw new IllegalArgumentException("The input vertex is repeated.");
        }

        Vertex newVtx = new Vertex(newVtxID);
        vtxList.add(newVtx);
    }

    /**
     * Private helper method to find the given vertex in this adjacency list.
     * @param vtxID vertex ID to look for
     * @return vertex if found, null if not found
     */
    private Vertex findVtx(int vtxID) {
        for (Vertex vtx : vtxList) {
            if (vtx.id() == vtxID) {
                return vtx;
            }
        }
        // Not found
        return null;
    }

    @Override
    public void removeVtx(int vtxID) {
        // Check whether the input vertex exists
        Vertex vtxToRemove = findVtx(vtxID);
        if (vtxToRemove == null) {
            throw new IllegalArgumentException("The input vertex doesn't exist.");
        }

        removeVtx(vtxToRemove);
    }

    /**
     * Private helper method to remove the given vertex from this graph.
     * @param vtxToRemove vertex to remove
     */
    private void removeVtx(Vertex vtxToRemove) {
        // Remove all the edges associated with the vertex to remove
        ArrayList<UndirectedEdge> edgesToRemove = vtxToRemove.edges();
        while (edgesToRemove.size() > 0) {
            UndirectedEdge edgeToRemove = edgesToRemove.get(0);
            removeEdge(edgeToRemove);
        }
        // Remove the vertex
        vtxList.remove(vtxToRemove);
    }

    @Override
    public void addEdge(int end1ID, int end2ID, double length) {
        // Check whether the input endpoints both exist
        Vertex end1 = findVtx(end1ID), end2 = findVtx(end2ID);
        if ((end1 == null) || (end2 == null)) {
            throw new IllegalArgumentException("The endpoints don't both exist.");
        }
        // Check whether the input endpoints are the same (self-loop)
        if (end1ID == end2ID) {
            throw new IllegalArgumentException("The endpoints are the same (self-loop).");
        }

        UndirectedEdge newEdge = new UndirectedEdge(end1, end2, length);
        addEdge(newEdge);
    }

    /**
     * Private helper method to add the given edge to this graph.
     * @param newEdge new edge
     */
    private void addEdge(UndirectedEdge newEdge) {
        Vertex end1 = newEdge.end1(), end2 = newEdge.end2();
        end1.addEdge(newEdge);
        end2.addEdge(newEdge);
        edgeList.add(newEdge);
    }

    @Override
    public void removeEdge(int end1ID, int end2ID) {
        // Check whether the input vertices both exist
        Vertex end1 = findVtx(end1ID), end2 = findVtx(end2ID);
        if ((end1 == null) || (end2 == null)) {
            throw new IllegalArgumentException("The input vertices don't both exist.");
        }
        // Check whether the edge to remove exists
        UndirectedEdge edgeToRemove = end1.getEdgeWithNeighbor(end2);
        if (edgeToRemove == null) {
            throw new IllegalArgumentException("The edge to remove doesn't exist.");
        }

        removeEdge(edgeToRemove);
    }

    /**
     * Private helper method to remove the given edge from this graph.
     * @param edgeToRemove edge to remove
     */
    private void removeEdge(UndirectedEdge edgeToRemove) {
        Vertex end1 = edgeToRemove.end1(), end2 = edgeToRemove.end2();
        end1.removeEdge(edgeToRemove);
        end2.removeEdge(edgeToRemove);
        edgeList.remove(edgeToRemove);
    }

    /**
     * Removes all the edges between a vertex pair from this graph.
     * @param end1ID endpoint1 ID
     * @param end2ID endpoint2 ID
     */
    public void removeEdgesBetweenPair(int end1ID, int end2ID) {
        try {
            while (true) {
                removeEdge(end1ID, end2ID);
            }
        } catch (IllegalArgumentException ex) {}
    }

    @Override
    public void showGraph() {
        System.out.println("The vertices are:");
        for (Vertex vtx : vtxList) {
            System.out.println(vtx);
        }
        System.out.println("The edges are:");
        for (UndirectedEdge edge : edgeList) {
            System.out.println(edge);
        }
    }

    /**
     * Finds the minimum spanning tree (MST) in this graph using straightforward
     * Prim's MST Algorithm.
     * @return cost of the MST
     */
    public double primMSTStraightforward() {
        // 1. Arbitrarily choose a source vertex s
        Random randomGenerator = new Random();
        Vertex srcVtx = vtxList.get(randomGenerator.nextInt(vtxList.size()));

        // 2. Initialize X = {s}, which contains the vertices we've spanned so far, and T = {empty}, which is the
        //    current spanning tree
        BitSet spanned = new BitSet();
        spanned.set(srcVtx.id());
        ArrayList<UndirectedEdge> currSpanningTree = new ArrayList<UndirectedEdge>();

        // 3. Create a heap containing all the edges with one endpoint in X (the set) and the other in (V-X)
        PriorityQueue<UndirectedEdge> crossingEdges = new PriorityQueue<UndirectedEdge>(srcVtx.edges());

        // 4. While X != V
        while (spanned.cardinality() < vtxList.size()) {
            // Among all crossing edges e = (v, w) with v in X (the set) and w in (V-X), pick up the cheapest crossing
            // edge
            UndirectedEdge cheapestCrossingEdge = crossingEdges.poll();
            // Add e to T
            currSpanningTree.add(cheapestCrossingEdge);
            // Add w to X (the set)
            Vertex w = null;
            if (spanned.get(cheapestCrossingEdge.end1().id())) { // endpoint2 is the w.
                w = cheapestCrossingEdge.end2();
            } else { // endpoint1 is the w.
                w = cheapestCrossingEdge.end1();
            }
            spanned.set(w.id());

            // Update the crossing edges with w's edges if necessary
            for (UndirectedEdge wEdge : w.edges()) {
                // Find the neighbor
                Vertex neighbor = null;
                if (wEdge.end1().id() == w.id()) { // endpoint2 is the neighbor.
                    neighbor = wEdge.end2();
                } else { // endpoint1 is the neighbor.
                    neighbor = wEdge.end1();
                }
                // Check whether the neighbor of w has been spanned
                if (!spanned.get(neighbor.id())) {
                    crossingEdges.offer(wEdge);
                }
            }
        }

        return currSpanningTree.stream().mapToDouble(edge -> edge.cost()).sum();
        // Overall running time complexity: O((m + n)log m)
        // Since usually m >= n, it could be simplified to O(mlog m).
    }

    /**
     * Finds the minimum spanning tree (MST) in this graph using improved Prim's
     * MST Algorithm.
     * The improvement comes from the idea of Dijkstra's Shortest-Path
     * Algorithm, especially the invariants of the algorithm and the proper
     * usage of the heap.
     * @return cost of the MST
     */
    public double primMSTImproved() {
        // 1. Arbitrarily choose a source vertex s
        Random randomGenerator = new Random();
        Vertex srcVtx = vtxList.get(randomGenerator.nextInt(vtxList.size()));

        // 2. Initialize X = {s}, which contains the vertices we've spanned so far, and T = {empty}, which is the
        //    current spanning tree
        BitSet spanned = new BitSet();
        spanned.set(srcVtx.id());
        for (UndirectedEdge edge : srcVtx.edges()) {
            // Find the neighbor
            Vertex neighbor = null;
            if (edge.end1().id() == srcVtx.id()) { // endpoint2 is the neighbor.
                neighbor = edge.end2();
            } else { // endpoint1 is the neighbor.
                neighbor = edge.end1();
            }
            neighbor.setMinCostIncidentEdge(edge);
            neighbor.setMinIncidentCost(edge.cost());
        }
        ArrayList<UndirectedEdge> currSpanningTree = new ArrayList<UndirectedEdge>();

        // 3. Create a heap containing all the vertices not in X (V-X)
        PriorityQueue<Vertex> vtxsToProcess = new PriorityQueue<Vertex>(vtxList);

        // 4. While X != V
        while (spanned.cardinality() < vtxList.size()) {
            // Among all crossing edges e = (v, w) with v in X (the set) and w in (V-X) (the heap), pick up the cheapest
            // crossing edge
            Vertex w = vtxsToProcess.poll();
            // Add e to T
            currSpanningTree.add(w.minCostIncidentEdge());
            // Add w to X (the set)
            spanned.set(w.id());

            // Extracting one vertex from the heap (V-X) may influence some minimum cost of the incident edges from X
            // (the set) of the vertices that are still in the heap (V-X).
            // Vertices that are not connected to w and are still in the heap (V-X) won't be influenced.
            // => The minimum cost of the incident edges from X (the set) of vertices that are connected to w and are
            //    still in the heap (V-X) may drop down.

            // Update the minimum cost of the incident edges from X (the set) for the vertices if necessary
            for (UndirectedEdge wEdge : w.edges()) {
                // Find the neighbor
                Vertex neighbor = null;
                if (wEdge.end1().id() == w.id()) { // endpoint2 is the neighbor.
                    neighbor = wEdge.end2();
                } else { // endpoint1 is the neighbor.
                    neighbor = wEdge.end1();
                }
                // Check whether the neighbor of w has been spanned
                if (!spanned.get(neighbor.id())) {
                    // Check whether the minimum cost of the incident edges from X (the set) needs to be updated
                    double newCost = wEdge.cost();
                    if (newCost < neighbor.minIncidentCost()) {
                        // Remove this neighbor from the heap (V-X)
                        vtxsToProcess.remove(neighbor);
                        // Update its minimum cost of the incident edges from X (the set)
                        neighbor.setMinCostIncidentEdge(wEdge);
                        neighbor.setMinIncidentCost(newCost);
                        // Put this neighbor back to the heap (V-X)
                        vtxsToProcess.offer(neighbor);
                    }
                }
            }
        }

        return currSpanningTree.stream().mapToDouble(edge -> edge.cost()).sum();
        // Overall running time complexity: O((m + n)log n)
        // Since usually m >= n, it could be simplified to O(mlog n).
    }

    /**
     * Finds the minimum spanning tree (MST) in this graph using straightforward
     * Kruskal's MST Algorithm.
     * @return cost of the MST
     */
    public double kruskalMSTStraightforward() {
        // 1. Sort the edges in order of increasing cost   [O(mlog m)]
        ArrayList<UndirectedEdge> edges = new ArrayList<UndirectedEdge>(edgeList);
        Collections.sort(edges);

        // 2. Initialize T = {empty}, which is the current spanning tree
        ArrayList<UndirectedEdge> currSpanningTree = new ArrayList<UndirectedEdge>();

        // 3. For each edge e = (v, w) in the sorted edge list   [O(mn)]
        for (UndirectedEdge edge : edges) {
            // Check whether adding e to T causes cycles in T
            // This is equivalent to checking whether there exists a v-w path in T before adding e.
            if (dfsAndCheckPath(currSpanningTree, edge.end1(), edge.end2()) == false) {
                currSpanningTree.add(edge);
            }
        }

        return currSpanningTree.stream().mapToDouble(edge -> edge.cost()).sum();
        // Overall running time complexity: O(mn)
    }

    /**
     * Private helper method to check whether there exists a v-w path in the
     * given spanning tree.
     * @param spanningTree given spanning tree
     * @param v vertex v
     * @param w vertex w
     * @return whether v-w path exists in the given spanning tree
     */
    private boolean dfsAndCheckPath(ArrayList<UndirectedEdge> spanningTree, Vertex v, Vertex w) {
        // Create a map between vertices and its neighbors
        HashMap<Integer, ArrayList<Vertex>> connections = constructConnections(spanningTree);
        if (!connections.containsKey(v.id()) || !connections.containsKey(w.id())) {
            return false;
        }
        return dfsAndCheckPathHelper(connections, v, w);
        // Running time complexity: O(n)
    }

    /**
     * Helper method to construct the connection map from the given edges.
     * @param edges given edges
     * @return constructed connected map
     */
    private HashMap<Integer, ArrayList<Vertex>> constructConnections(ArrayList<UndirectedEdge> edges) {
        HashMap<Integer, ArrayList<Vertex>> connections = new HashMap<Integer, ArrayList<Vertex>>();
        for (UndirectedEdge edge : edges) {
            addNeighbor(connections, edge.end1(), edge.end2());
            addNeighbor(connections, edge.end2(), edge.end1());
        }
        return connections;
        // Running time complexity: O(n)
    }

    /**
     * Helper method to add the given neighbor of the given vertex to the given
     * connection map.
     * @param connections given connection map
     * @param v given vertex
     * @param neighbor given neighbor
     */
    private void addNeighbor(HashMap<Integer, ArrayList<Vertex>> connections, Vertex v, Vertex neighbor) {
        ArrayList<Vertex> neighbors = connections.getOrDefault(v.id(), new ArrayList<Vertex>());
        neighbors.add(neighbor);
        connections.put(v.id(), neighbors);
        // Running time complexity: O(1)
    }

    /**
     * Helper method to check whether there exists a curr-target path in the
     * given connection map recursively.
     * @param spanningTree given spanning tree
     * @param curr current vertex
     * @param target target vertex
     * @return whether curr-target path exists in the given spanning tree
     */
    private boolean dfsAndCheckPathHelper(HashMap<Integer, ArrayList<Vertex>> connections, Vertex curr, Vertex target) {
        curr.setAsExplored();
        for (Vertex neighbor : connections.get(curr.id())) {
            if (neighbor.id() == target.id()) {
                return true;
            }
            if (!neighbor.explored()) {
                if (dfsAndCheckPathHelper(connections, neighbor, target) == true) {
                    return true;
                }
            }
        }
        return false;
        // Running time complexity: O(n)
    }

    /**
     * Finds the minimum spanning tree (MST) in this graph using improved
     * Kruskal's MST Algorithm.
     * @return cost of the MST
     */
    public double kruskalMSTImproved() {
        // 1. Sort the edges in order of increasing cost   [O(mlog m)]
        ArrayList<UndirectedEdge> edges = new ArrayList<UndirectedEdge>(edgeList);
        Collections.sort(edges);

        // 2. Initialize T = {empty}, which is the current spanning tree
        ArrayList<UndirectedEdge> currSpanningTree = new ArrayList<UndirectedEdge>();

        // 3. Create a UnionFind of vertices
        // object -> vertex
        // group -> connected component w.r.t. the edges in T
        // Each of the vertex is on its own isolated connected component.
        UnionFind<Vertex> unionFind = new UnionFind<Vertex>(vtxList);

        // 4. For each edge e = (v, w) in the sorted edge list   [O(nlog n)]
        for (UndirectedEdge edge : edges) {
            // Check whether adding e to T causes cycles in T
            // This is equivalent to checking whether there exists a v-w path in T before adding e.
            // This is equivalent to checking whether the leaders of v and w in the UnionFind are the same.
            if (edge.end1().leader() != edge.end2().leader()) {
                currSpanningTree.add(edge);
                // Fuse the two connected components to a single one
                String groupNameV = edge.end1().leader().objName(), groupNameW = edge.end2().leader().objName();
                unionFind.union(groupNameV, groupNameW);
            }
        }
        /*
         * Originally we would think it involves O(mn) leader updates; however,
         * we can change to a "vertex-centric" view:
         * Consider the number of leader updates for a single vertex:
         * Every time the leader of this vertex gets updated, the size of its
         * connected components at least doubles, so suppose it experiences x
         * leader updates in total, we have
         *     2^x <= n
         *     x <= log2 n
         * Thus, each vertex experiences O(log n) leader updates, leading to a
         * O(nlog n) leader updates in total.
         */

        return currSpanningTree.stream().mapToDouble(edge -> edge.cost()).sum();
        // Overall running time complexity: O(mlog m)
    }

    /**
     * Clusters the graph into the given number of clusters using maximum
     * spacing as the objective function, which is to maximize the minimum
     * distance between a pair of separated points, using Single-link Algorithm,
     * which is exactly the same as Kruskal's MST algorithm.
     * @param k number of clusters
     * @return maximum spacing of the clustering
     */
    public double clusteringWithMaxSpacing(int k) {
        // Check whether the input k is greater than 1
        if (k <= 1) {
            throw new IllegalArgumentException("The number of clusters must be at least 2.");
        }

        ArrayList<UndirectedEdge> edges = new ArrayList<UndirectedEdge>(edgeList);
        Collections.sort(edges);

        // Initially, each point is in a separate cluster.
        UnionFind<Vertex> unionFind = new UnionFind<Vertex>(vtxList);

        boolean stopped = false;
        for (UndirectedEdge edge : edges) {
            if (edge.end1().leader() != edge.end2().leader()) {
                if (stopped) {
                    return edge.cost();
                }
                // Let p, q = closest pair of separated points, which determines current the spacing
                // Merge the clusters containing p and q into a single cluster
                String groupNameV = edge.end1().leader().objName(), groupNameW = edge.end2().leader().objName();
                unionFind.union(groupNameV, groupNameW);
                if (unionFind.numOfGroups() == k) { // Repeat until only k clusters
                    // The maximum spacing is simply the cost of the next cheapest crossing edge among different
                    // connected components.
                    stopped = true;
                }
            }
        }
        // Codes should never reach here.
        return 0.0;
        // Overall running time complexity: O(mlog m)
    }

}
