#!usr/bin/env python3
# -*- coding: utf-8 -*-

__author__ = 'Ziang Lu'

from undirected_graph import AdjacencyList


def main():
    with open('undirected_graph_info.txt', 'rt') as f:
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

        # Find all the findable vertices starting from vertex #1 using BFS
        print('Findable vertices from vertex #1: %s' % graph.bfs(src_vtx_id=1))  # [1, 2, 3, 4, 5, 6]
        graph.clear_explored()

        # Find the length of the shorted path from vertex #1 to vertex #6 using
        # BFS
        print('Length of the shortest path from vertex #1 to vertex #6: %d' %
              graph.shortest_path(src_vtx_id=1, dest_vtx_id=6))  # 3
        graph.clear_explored()

        print('Number of connected components: %d' %
              graph.num_of_connected_components())


if __name__ == '__main__':
    main()
