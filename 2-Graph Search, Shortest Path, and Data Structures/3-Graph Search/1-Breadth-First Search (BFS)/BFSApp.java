import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import undirected_graph.AdjacencyList;

public class BFSApp {

    /**
     * Main driver.
     * @param args arguments from command line
     */
    public static void main(String[] args) {
        Scanner scanner = null;
        try {
            scanner = new Scanner(new File("undirected_graph_info.txt"));
            
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

            // Find all the findable vertices starting from vertex #1 using BFS
            System.out.println("Findable vertices from vertex #1: " + graph.bfs(1)); // [1, 2, 3, 4, 5, 6]
            graph.clearExplored();

            // Find the length of the shortest path from vertex #1 to vertex #6 using BFS
            System.out.println("Length of the shortest path from vertex #1 to vertex #6: " + graph.shortestPath(1, 6)); // 3
            graph.clearExplored();

            // Finds the number of connected components of the graph
            System.out.println("Number of connected components of the graph: " + graph.numOfConnectedComponents()); // 1
        } catch (FileNotFoundException ex) {
            System.out.println("Cannot find the file.");
        } finally {
            if (scanner != null) {
                scanner.close();
            }
        }
    }

}