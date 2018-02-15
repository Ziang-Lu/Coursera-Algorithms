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
        // Construct the undirected graph
        UndirectedGraph graph = constructUndirectedGraph("undirected_graph_info.txt");
        // Find the cost of the Minimum Spanning Tree (MST) of the graph using straightforward Prim's MST Algorithm
        System.out.println("Cost of Minimum Spanning Tree (MST): " + graph.primMSTStraightforward());
        // Find the cost of the Minimum Spanning Tree (MST) of the graph using improved Prim's MST Algorithm
        System.out.println("Cost of Minimum Spanning Tree (MST): " + graph.primMSTImproved());
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
        }

        return graph;
    }

}
