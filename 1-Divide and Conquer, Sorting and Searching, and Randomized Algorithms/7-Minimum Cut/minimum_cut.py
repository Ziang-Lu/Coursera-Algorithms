#!usr/bin/env python3
# -*- coding: utf-8 -*-

"""
Given a graph, compute a cut with the fewest number of crossing edges.

Format of graph file:
[number of edges]   (The vertex ID starts from 1.)
[end1_id, end2_id]
[end1_id, end2_id]
"""

__author__ = 'Ziang Lu'

from undirected_graph import AdjacencyList


def main():
    with open('graph_info.txt', 'rt') as f:
        # Construct the graph
        graph = AdjacencyList()

        # Add the vertices
        n_vtx = int(f.readline())
        for vtx_id in range(1, n_vtx + 1):
            graph.add_vtx(new_vtx_id=vtx_id)
        # Add the edges
        for line in f.readlines():
            ends = line.split(' ')
            graph.add_edge(end1_id=int(ends[0]), end2_id=int(ends[1]))

        # Compute a minimum cut
        print('Minimum cut: %d' % graph.compute_minimum_cut())


if __name__ == '__main__':
    main()
