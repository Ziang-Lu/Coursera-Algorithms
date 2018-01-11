/**
 * Given a graph, compute a cut with the fewest number of crossing edges.
 *
 * Format of the graph file:
 * [number of vertices]   (The vertex ID starts from 1.)
 * [end1ID end2ID]
 * [end1ID end2ID]
 * ...
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import undirected_graph.AdjacencyList;

public class MinimumCut {

    /**
     * Main driver.
     * @param args arguments from command line
     */
    public static void main(String[] args) {
        Scanner scanner = null;
        try {
            scanner = new Scanner(new File("graph_info.txt"), "latin1");

            // Construct the graph
            AdjacencyList graph = new AdjacencyList();
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

            // Compute a minimum cut
            System.out.println("Minimum cut: " + graph.computeMinimumCut());
        } catch (FileNotFoundException ex) {
            System.out.println("Cannot find the file.");
        } finally {
            if (scanner != null) {
                scanner.close();
            }
        }
    }

}
