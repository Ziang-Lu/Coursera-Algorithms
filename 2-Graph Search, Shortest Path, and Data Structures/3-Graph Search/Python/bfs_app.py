#!usr/bin/env python3
# -*- coding: utf-8 -*-

__author__ = 'Ziang Lu'

from directed_graph import DirectedGraph
from undirected_graph import UndirectedGraph


def main():
    """
    Main driver.
    :return: None
    """
    _test_undirected_graph(filename='undirected_graph_info.txt')


def _test_undirected_graph(filename):
    """
    Test driver for undirected graph with BFS.
    :param filename: str
    :return: None
    """
    # Construct the graph
    graph = _construct_graph(filename=filename, undirected=True)

    # Find all the findable vertices starting from vertex #1 using BFS
    print('Findable vertices from vertex #1 using BFS: %s' %
          graph.bfs(src_vtx_id=1))  # [1, 2, 3, 4, 5, 6]
    graph.clear_explored()

    # Find the length of the shortest path from vertex #1 to vertex #6
    print('Length of the shortest path from vertex #1 to vertex #6: %d' %
          graph.shortest_path(src_vtx_id=1, dest_vtx_id=6))  # 3
    graph.clear_explored()

    # Find the number of connected components of the undirected graph using BFS
    print('Number of connected components using BFS: %d' %
          graph.num_of_connected_components_with_bfs())  # 1


def _test_directed_graph(filename):
    """
    Test driver for directed graph with BFS.
    :param filename: str
    :return: None
    """
    # Construct the graph
    graph = _construct_graph(filename=filename, undirected=False)

    # Find all the findable vertices from vertex #1 using BFS
    print('Findable vertices from vertex #1 using BFS: %s' %
          graph.bfs(src_vtx_id=1))  # [1, 2, 3]
    graph.clear_explored()

    # Find the length of the shortest path from vertex #1 to vertex #6
    print('Length of the shortest path from vertex #1 to vertex #3: %d' %
          graph.shortest_path(src_vtx_id=1, dest_vtx_id=3))  # 1


def _construct_graph(filename, undirected):
    """
    Private helper method to construct a graph from the given graph file.
    :param filename: str
    :param undirected: bool
    :return: UndirectedGraph or DirectedGraph
    """
    with open(filename, 'rt') as f:
        # Construct the graph
        if undirected:
            graph = UndirectedGraph()
        else:
            graph = DirectedGraph()
        # Add the vertices
        n_vtx = int(f.readline())
        for vtx_id in range(1, n_vtx + 1):
            graph.add_vtx(new_vtx_id=vtx_id)
        # Add the edges
        for line in f.readlines():
            ends = line.split(' ')
            graph.add_edge(end1_id=int(ends[0]), end2_id=int(ends[1]))
        return graph


if __name__ == '__main__':
    main()
