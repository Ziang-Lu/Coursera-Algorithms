#!usr/bin/env python3
# -*- coding: utf-8 -*-

"""
Minimum Cut Problem:
Given a graph, compute a cut with the fewest number of crossing edges.

Algorithm:
What is the probability of success (outputs a specific minimum cut (A, B))?

If the graph has multiple minimum cut, we only focus on this specific minimum
cut (A, B).
Thus, we only define the algorithm to be successful if it outputs this specific
minimum cut (A, B); if it outputs some other minimum cut, we don't count it.

##############################
Notation:
Let k = # of crossing edges of the minimum cut, and call all of these k crossing
edges F.
##############################

Thus, Pr[output is (A, B)] = Pr[never contract an edge from F]

##############################
Notation:
Let Si denote the event that an edge from F is contracted in iteration i
("Screw up in iteration i").
##############################

Thus, Pr[never contract an edge from F] = Pr[~S1 and ~S2 and ... and ~Sn-2]

Pr[S1] = k/m

Key observation:
Each vertex has at least k edges involving it.
(Think about a single cut, which cuts this single vertex.
Thus, # of crossing edges = # of edges involving this single vertex.
Since this is at least a minimum cut, this single vertex must have at least k
edges involving it.)
Thus after i contractions,
k(n-i) <= 2(# of remaining edges) => (# of remaining edges) >= k(n-i)/2

Pr[S1] = k/m <= k/(kn/2) = 2/n
Pr[~S1] = 1 - 2/n = (n-2)/n
Pr[~S1 and ~S2] = Pr[~S1] * Pr[~S2|~S1] >= (n-2)/n * Pr[~S2|~S1]
>= (n-2) * {1 - k/[k(n-1)/2]} = (n-2)/n * (n-3)/(n-1)
...
Pr[~S1 and ~S2 and ... and ~Sn-2] = (n-2)/n * (n-3)/(n-1) * ... * 1/3
= 2/[n(n-1)]


=>
Run the algorithm for a large # of trials (N) (each one independently), and
remember the minimum cut found.

How many trials do we need?

Pr[success] = 1 - Pr[all N trials fail]

##############################
Notation:
Let Ti be that the i-th trial succeeds, i.e., in the i-th trial, the basic
algorithm outputs (A, B).
##############################

Pr[all N trials fail] = Pr[~T1 and ~T2 and ... and ~TN]
= 1 - Pr[~T1] *  Pr[~T2] * ... * Pr[~TN]
>= {1- 2/[n(n-1)]}^N = {[(n+1)(n-2)]/[n(n-1)]}^N

If we use Pr[output is (A, B)] >= 2/[n(n-1)] > 1/n^2 for a single trial, then
Pr[success] >= 1 - (1-1/n^2)^N

Calculus fact: 1 + x <= e^x

Thus,
Pr[success] >= 1 - e^(-N/n^2)

Choose N = n^2ln n, then
Pr[success] >= 1 - 1/n
"""

__author__ = 'Ziang Lu'

import math

from undirected_graph import UndirectedGraph


def _construct_undirected_graph(filename: str) -> UndirectedGraph:
    """
    Private helper function to construct a undirected graph from the given
    undirected graph file.
    :param filename: str
    :return: UndirectedGraph
    """
    with open(filename, 'rt') as f:
        graph = UndirectedGraph()
        # Add the vertices
        n_vtx = int(f.readline())
        for vtx_id in range(1, n_vtx + 1):
            graph.add_vtx(new_vtx_id=vtx_id)
        # Add the edges
        for line in f.readlines():
            ends = line.split(' ')
            graph.add_edge(end1_id=int(ends[0]), end2_id=int(ends[1]))
        return graph


def main():
    # Calculate the number of trials (n^2ln n)
    with open('undirected_graph_info.txt', 'rt') as f:
        n_vtx = int(f.readline())
        n_trial = int(math.ceil(n_vtx ** 2 * math.log(n_vtx)))
        curr_minimum_cut = len(f.readlines())

    for i in range(n_trial):
        # Construct the graph
        graph = _construct_undirected_graph('undirected_graph_info.txt')

        # Compute a minimum cut
        minimum_cut = graph.compute_minimum_cut()
        if minimum_cut < curr_minimum_cut:
            curr_minimum_cut = minimum_cut
    print(f'Minimum cut: {curr_minimum_cut}')  # 2


if __name__ == '__main__':
    main()
