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
    print('Testing undirected graph...')
    _test_undirected_graph(filename='undirected_graph_info.txt')
    print()

    print('Testing directed graph...')
    _test_directed_graph(filename='directed_graph_info.txt')


def _test_undirected_graph(filename):
    """
    Test driver for undirected graph with BFS.
    :param filename: str
    :return: None
    """
    # Construct the graph
    graph = _construct_graph(filename=filename, undirected=True)

    # BFS

    # Find all the findable vertices starting from vertex #1 using BFS
    print('Findable vertices from vertex #1 using BFS: %s' %
          graph.bfs(src_vtx_id=1))  # [1, 2, 3, 4, 5, 6]
    graph.clear_explored()

    # Find the length of the shortest path from vertex #1 to vertex #6
    print('Length of the shortest path from vertex #1 to vertex #6: %d' %
          graph.shortest_path(src_vtx_id=1, dest_vtx_id=6))  # 3
    graph.clear_explored()

    # Find the number of connected components of the undirected graph using BFS
    # (Undirected connectivity)
    print('Number of connected components of the graph using BFS (Undirected '
          'connectivity): %d' % graph.num_of_connected_components_with_bfs())  # 1
    graph.clear_explored()

    # DFS

    # Find all the findable vertices starting from vertex #1 using DFS
    print('Findable vertices from vertex #1 using DFS: %s' %
          graph.bfs(src_vtx_id=1))  # [1, 2, 3, 4, 5, 6]
    graph.clear_explored()

    # Find the number of connected components of the undirected graph using DFS
    # (Undirected connectivity)
    print('Number of connected components of the graph using DFS (Undirected '
          'connectivity): %d' % graph.num_of_connected_components_with_dfs())  # 1
    graph.clear_explored()


def _test_directed_graph(filename):
    """
    Test driver for directed graph with BFS.
    :param filename: str
    :return: None
    """
    # Construct the graph
    graph = _construct_graph(filename=filename, undirected=False)

    # BFS

    # Find all the findable vertices from vertex #1 using BFS
    print('Findable vertices from vertex #1 using BFS: %s' %
          graph.bfs(src_vtx_id=1))  # [1, 4, 7]
    graph.clear_explored()

    # Find the length of the shortest path from vertex #1 to vertex #7
    print('Length of the shortest path from vertex #1 to vertex #7: %d' %
          graph.shortest_path(src_vtx_id=1, dest_vtx_id=7))  # 2
    graph.clear_explored()

    # DFS

    # Find all the findable vertices from vertex #1 using DFS
    print('Findable vertices from vertex #1 using DFS: %s' %
          graph.bfs(src_vtx_id=1))  # [1, 4, 7]
    graph.clear_explored()

    # Find the number of SCCs of the directed graph using DFS
    # (Directed connectivity)
    print('Number of SCCs of the graph using DFS (Directed connectivity): %d' %
          graph.num_of_connected_components_with_dfs())  # 3
    graph.clear_explored()

    # Find the topological ordering of the vertices of the directed graph using
    # DFS
    print('Topological ordering of the vertices of the directed graph using '
          'DFS: %s' % graph.topological_sort())  # [2, 8, 5, 6, 9, 3, 1, 4, 7]
    # The result might be wrong when there is no topological ordering of the
    # directed graph.

    # Find the topological ordering of the vertices of the directed graph using
    # straightforward algorithm
    print('Topological ordering of the vertices of the directed graph using '
          'straightforward algorithm: %s' %
          graph.topological_sort_straightforward())  # [0, 0, 0, 0, 0, 0, 0, 0, 0]
    # The result is because that the graph doesn't have a topological ordering.


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
            graph.add_edge(int(ends[0]), int(ends[1]))
        return graph


if __name__ == '__main__':
    main()
