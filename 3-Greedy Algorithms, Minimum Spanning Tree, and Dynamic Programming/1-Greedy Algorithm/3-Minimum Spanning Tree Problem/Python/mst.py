#!usr/bin/env python3
# -*- coding: utf-8 -*-

__author__ = 'Ziang Lu'

from undirected_graph import UndirectedGraph


def main():
    graph = _construct_undirected_graph('undirected_graph_info.txt')
    print("Cost of the Minimum Spanning Tree (MST): %f" %
          graph.prim_mst_straightforward())


def _construct_undirected_graph(filename):
    """
    Private helper function to construct a undirected graph from the given file.
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
            edge_info = line.split(' ')
            graph.add_edge(end1_id=int(edge_info[0]), end2_id=int(edge_info[1]),
                           cost=float(edge_info[2]))
        return graph


if __name__ == '__main__':
    main()
