/**
 * Minimum Cut Problem:
 * Given a graph, compute a cut with the fewest number of crossing edges.
 *
 * Algorithm:
 * What is the probability of success (outputs a specific minimum cut (A, B))?
 *
 * If the graph has multiple minimum cut, we only focus on this specific minimum
 * cut (A, B).
 * Thus, we only define the algorithm to be successful if it outputs this specific
 * minimum cut (A, B); if it outputs some other minimum cut, we don't count it.
 *
 * ##############################
 * Notation:
 * Let k = # of crossing edges of the minimum cut, and call all of these k crossing
 * edges F.
 * ##############################
 *
 * Thus, Pr[output is (A, B)] = Pr[never contract an edge from F]
 *
 * ##############################
 * Notation:
 * Let Si denote the event that an edge from F is contracted in iteration i
 * ("Screw up in iteration i").
 * ##############################
 *
 * Thus, Pr[never contract an edge from F] = Pr[~S1 and ~S2 and ... and ~Sn-2]
 *
 * Pr[S1] = k/m
 *
 * Key observation:
 * Each vertex has at least k edges involving it.
 * (Think about a single cut, which cuts this single vertex.
 * Thus, # of crossing edges = # of edges involving this single vertex.
 * Since this is at least a minimum cut, this single vertex must have at least k
 * edges involving it.)
 * Thus after i contractions,
 * k(n-i) <= 2(# of remaining edges) => (# of remaining edges) >= k(n-i)/2
 *
 * Pr[S1] = k/m <= k/(kn/2) = 2/n
 * Pr[~S1] = 1 - 2/n = (n-2)/n
 * Pr[~S1 and ~S2] = Pr[~S1] * Pr[~S2|~S1] >= (n-2)/n * Pr[~S2|~S1]
 * >= (n-2) * {1 - k/[k(n-1)/2]} = (n-2)/n * (n-3)/(n-1)
 * ...
 * Pr[~S1 and ~S2 and ... and ~Sn-2] = (n-2)/n * (n-3)/(n-1) * ... * 1/3
 * = 2/[n(n-1)]
 *
 *
 * =>
 * Run the algorithm for a large # of trials (N) (each one independently), and remember the minimum cut found.
 *
 * How many trials do we need?
 *
 * Pr[success] = 1 - Pr[all N trials fail]
 *
 * ##############################
 * Notation:
 * Let Ti be that the i-th trial succeeds, i.e., in the i-th trial, the basic
 * algorithm outputs (A, B).
 * ##############################
 *
 * Pr[all N trials fail] = Pr[~T1 and ~T2 and ... and ~TN]
 * = 1 - Pr[~T1] *  Pr[~T2] * ... * Pr[~TN]
 * >= {1- 2/[n(n-1)]}^N = {[(n+1)(n-2)]/[n(n-1)]}^N
 *
 * If we use Pr[output is (A, B)] >= 2/[n(n-1)] > 1/n^2 for a single trial, then
 * Pr[success] >= 1 - (1-1/n^2)^N
 *
 * Calculus fact: 1 + x <= e^x
 *
 * Thus,
 * Pr[success] >= 1 - e^(-N/n^2)
 *
 * Choose N = n^2ln n, then
 * Pr[success] >= 1 - 1/n
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import undirected_graph.UndirectedGraph;

public class MinimumCut {

    /**
     * Main driver.
     * @param args arguments from command line
     */
    public static void main(String[] args) {
        // Calculate the number of trials
        Scanner scanner = null;
        int nTrial = 0, minimumCut = 0;
        try {
            scanner = new Scanner(new File("graph_info.txt"), "latin1");
            int nVtx = Integer.parseInt(scanner.nextLine());
            nTrial = (int) Math.ceil(nVtx * nVtx * Math.log(nVtx));
            int nEdge = 0;
            while (scanner.hasNext()) {
                scanner.nextLine();
                ++nEdge;
            }
            minimumCut = nEdge;
        } catch (FileNotFoundException ex) {
            System.out.println("Cannot find the file.");
        } finally {
            if (scanner != null) {
                scanner.close();
            }
        }

        for (int i = 0; i < nTrial; ++i) {
            // Construct the graph
            UndirectedGraph graph = constructUndirectedGraph("undirected_graph_info.txt");

            // Compute a minimum cut
            int currMinimumCut = graph.computeMinimumCut();
            if (currMinimumCut < minimumCut) {
                minimumCut = currMinimumCut;
            }
        }
        System.out.println("Minimum cut: " + minimumCut); // 2
    }

    /**
     * Private helper method to construct a undirected graph from the given file.
     * @param filename given filename
     * @return constructed undirected graph
     */
    private static UndirectedGraph constructUndirectedGraph(String filename) {
        UndirectedGraph graph = null;

        Scanner scanner = null;
        try {
            scanner = new Scanner(new File(filename), "latin1");

            graph = new UndirectedGraph();
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
