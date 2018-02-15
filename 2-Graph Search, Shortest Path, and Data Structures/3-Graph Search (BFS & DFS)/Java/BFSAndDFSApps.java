import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Scanner;

import directed_graph.DirectedGraph;
import graph.GraphInterface;
import undirected_graph.UndirectedGraph;

public class BFSAndDFSApps {

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
        GraphInterface graph = constructGraph(filename, true);

        // BFS

        // Find all the findable vertices starting from vertex #1 using BFS
        System.out.println("Findable vertices from vertex #1 using BFS: " + graph.bfs(1)); // [1, 2, 3, 4, 5, 6]
        graph.clearExplored();

        // Find the length of the shortest path from vertex #1 to vertex #6
        System.out.println("Length of the shortest path from vertex #1 to vertex #6: " + graph.shortestPath(1, 6)); // 3
        graph.clearExplored();

        // Find the number of connected components of the undirected graph using BFS (Undirected connectivity)
        System.out.println("Number of connected components of the graph using BFS (Undirected connectivity): "
                + graph.numOfConnectedComponentsWithBFS()); // 1
        graph.clearExplored();

        // DFS

        // Find all the findable vertices starting from vertex #1 using DFS
        System.out.println("Findable vertices from vertex #1 using DFS: " + graph.dfs(1)); // [1, 2, 3, 4, 5, 6]
        graph.clearExplored();

        // Find the number of connected components of the undirected graph using DFS (Undirected connectivity)
        System.out.println("Number of connected components of the graph using DFS (Undirected connectivity): "
                + graph.numOfConnectedComponentsWithDFS()); // 1
        graph.clearExplored();
    }

    /**
     * Test driver for BFS with directed graph.
     * @param filename directed graph filename
     */
    private static void testDirectedGraph(String filename) {
        DirectedGraph graph = (DirectedGraph) constructGraph(filename, false);

        // Find all the findable vertices starting from vertex #1 using BFS
        System.out.println("Findable vertices from vertex #1 using BFS: " + graph.bfs(1)); // [1, 4, 7]
        graph.clearExplored();

        // Find the length of the shortest path from vertex #1 to vertex #7
        System.out.println("Length of the shortest path from vertex #1 to vertex #7: " + graph.shortestPath(1, 7)); // 2
        graph.clearExplored();

        // DFS

        // Find all the findable vertices starting from vertex #1 using DFS
        System.out.println("Findable vertices from vertex #1 using DFS: " + graph.dfs(1)); // [1, 4, 7]
        graph.clearExplored();

        // Find the number of SCCs of the directed graph using DFS (Directed connectivity)
        System.out.println("Number of SCCs of the graph using DFS (Directed connectivity): "
                + graph.numOfConnectedComponentsWithDFS()); // 3
        graph.clearExplored();

        // Find the topological ordering of the vertices of the directed graph using DFS
        System.out.println("Topological ordering of the vertices of the directed graph using DFS: "
                + Arrays.toString(graph.topologicalSort())); // [2, 8, 5, 6, 9, 3, 1, 4, 7]
        graph.clearExplored();
        // The result might be wrong when there is no topological ordering of the directed graph.

        // Find the topological ordering of the vertices of the directed graph using straightforward algorithm
        System.out
                .println("Topological ordering of the vertices of the directed graph using straightforward algorithm: "
                        + Arrays.toString(graph.topologicalSortStraightforward())); // [0, 0, 0, 0, 0, 0, 0, 0, 0]
        graph.clearExplored();
        // The result is because that the graph doesn't have a topological ordering.
    }

    /**
     * Private helper method to construct a graph from the given file.
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
