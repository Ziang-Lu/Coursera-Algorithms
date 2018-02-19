import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import undirected_graph.UndirectedGraph;

public class MST {

    /**
     * Main driver.
     * @param args arguments from command line
     */
    public static void main(String[] args) {
        UndirectedGraph graph = constructUndirectedGraph("undirected_graph_info.txt");
        // Find the minimum spanning tree (MST) using Prim's and Kruskal's algorithms with some variations
        System.out.println("Cost of minimum spanning tree (MST) using straightforward Prim's algorithm: "
                + graph.primMSTStraightforward());
        System.out.println(
                "Cost of minimum spanning tree (MST) using improved Prim's algorithm: " + graph.primMSTImproved());
        System.out.println("Cost of minimum spanning tree (MST) using straightforward Kruskal's algorithm: "
                + graph.kruskalMSTStraightforward());
//        System.out.println("Cost of minimum spanning tree (MST) using improved Kruskal's algorithm: "
//                + graph.kruskalMSTImproved());
        // Cluster the graph into 2 clusters using maximum spacing as the objective function, using Single-link
        // Algorithm
        System.out.println("Maximum spacing of 2-clustering: " + graph.clusteringWithMaxSpacing(2));
    }

    /**
     * Private helper method to construct a undirected graph from the given file.
     * @param filename given filename
     * @return constructed graph
     */
    private static UndirectedGraph constructUndirectedGraph(String filename) {
        UndirectedGraph graph = new UndirectedGraph();

        Scanner scanner = null;
        try {
            scanner = new Scanner(new File(filename), "latin1");
            // Add the vertices
            int nVtx = Integer.parseInt(scanner.nextLine());
            for (int vtxID = 1; vtxID <= nVtx; ++vtxID) {
                graph.addVtx(vtxID);
            }
            // Add the edges
            while (scanner.hasNextLine()) {
                String[] edgeInfo = scanner.nextLine().split(" ");
                graph.addEdge(Integer.parseInt(edgeInfo[0]), Integer.parseInt(edgeInfo[1]),
                        Double.parseDouble(edgeInfo[2]));
            }
        } catch (FileNotFoundException ex) {
            System.out.println("Cannot find the file.");
        } finally {
            if (scanner != null) {
                scanner.close();
            }
        }

        return graph;
    }

}
