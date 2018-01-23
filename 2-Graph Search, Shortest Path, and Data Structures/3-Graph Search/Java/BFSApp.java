import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import directed_graph.DirectedGraph;
import graph.GraphInterface;
import undirected_graph.UndirectedGraph;

/**
 * BFS Applications.
 * 1. Naive BFS
 * 2. Shortest path (specific to BFS)
 * 3. Undirected connectivity
 *
 * @author Ziang Lu
 */
public class BFSApp {

    /**
     * Main driver.
     * @param args arguments from command line
     */
    public static void main(String[] args) {
        System.out.println("Testing undirected graph...");
        testUndirectedGraph("undirected_graph_info.txt");
        System.out.println();

        System.out.println("Testing directed graph...");
        testDirectedGraph("directed_graph_info.txt");
    }

    /**
     * Test driver for BFS with undirected graph.
     * @param filename undirected graph filename
     */
    private static void testUndirectedGraph(String filename) {
        // Construct the graph
        GraphInterface graph = constructGraph(filename, true);

        // Find all the findable vertices starting from vertex #1 using BFS
        System.out.println("Findable vertices from vertex #1 using BFS: " + graph.bfs(1)); // [1, 2, 3, 4, 5, 6]
        graph.clearExplored();

        // Find the length of the shortest path from vertex #1 to vertex #6
        System.out.println("Length of the shortest path from vertex #1 to vertex #6: " + graph.shortestPath(1, 6)); // 3
        graph.clearExplored();

        // Find the number of connected components of the undirected graph using BFS
        System.out.println(
                "Number of connected components of the graph using BFS: " + graph.numOfConnectedComponentsWithBFS()); // 1
    }

    /**
     * Test driver for BFS with directed graph.
     * @param filename directed graph filename
     */
    private static void testDirectedGraph(String filename) {
        // Construct the graph
        GraphInterface graph = constructGraph(filename, false);

        // Find all the findable vertices starting from vertex #1 using BFS
        System.out.println("Findable vertices from vertex #1 using BFS: " + graph.bfs(1)); // [1, 2, 3]
        graph.clearExplored();

        // Find the length of the shortest path from vertex #1 to vertex #3
        System.out.println("Length of the shortest path from vertex #1 to vertex #3: " + graph.shortestPath(1, 3)); // 1
        graph.clearExplored();
    }

    /**s
     * Private helper method to construct a undirected graph from the given file.
     * @param filename given filename
     * @param undirected whether the graph to construct is undirected
     * @return constructed undirected graph
     */
    private static GraphInterface constructGraph(String filename, boolean undirected) {
        GraphInterface graph = null;

        Scanner scanner = null;
        try {
            scanner = new Scanner(new File(filename), "latin1");

            if (undirected) {
                graph = new UndirectedGraph();
            } else {
                graph = new DirectedGraph();
            }
            // Add the vertices
            int nVtx = Integer.parseInt(scanner.nextLine());
            for (int vtxID = 1; vtxID <= nVtx; ++vtxID) {
                graph.addVtx(vtxID);
            }
            // Add the edges
            while (scanner.hasNextLine()) {
                String[] ends = scanner.nextLine().split(" ");
                graph.addEdge(Integer.parseInt(ends[0]), Integer.parseInt(ends[1]));
            }
        } catch (FileNotFoundException ex) {
            System.out.println("Cannot found the file.");
        } finally {
            if (scanner != null) {
                scanner.close();
            }
        }

        return graph;
    }

}