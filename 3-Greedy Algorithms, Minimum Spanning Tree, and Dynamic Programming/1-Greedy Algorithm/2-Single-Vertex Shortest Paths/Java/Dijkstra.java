import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import directed_graph.DirectedGraph;
import graph.GraphInterface;
import undirected_graph.UndirectedGraph;

public class Dijkstra {

    /**
     * Main driver.
     * @param args arguments from command line
     */
    public static void main(String[] args) {
        // Test directed graph
        GraphInterface directedGraph = constructGraph("directed_graph_info.txt", false);

        // Find the shortest distances from vertex #1 using Dijkstra's shortest-path algorithm
        System.out.println("Shorest distances from vertex #1: " + directedGraph.dijkstraShortestPaths(1));

        // Test undirected graph
        GraphInterface undirectedGraph = constructGraph("undirected_graph_info.txt", true);

        // Find the shortest distances from vertex #1 using Dijkstra's shortest-path algorithm
        System.out.println("Shorest distances from vertex #1: " + undirectedGraph.dijkstraShortestPaths(1));
    }

    /**
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
                String[] edge_info = scanner.nextLine().split(" ");
                graph.addEdge(Integer.parseInt(edge_info[0]), Integer.parseInt(edge_info[1]),
                        Double.parseDouble(edge_info[2]));
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
