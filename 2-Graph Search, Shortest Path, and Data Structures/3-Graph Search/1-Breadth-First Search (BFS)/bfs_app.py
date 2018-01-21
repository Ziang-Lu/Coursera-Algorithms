#!usr/bin/env python3
# -*- coding: utf-8 -*-

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

        # Find all the findable vertices starting from vertex #1 using BFS
        print(graph.bfs(src_vtx_id=1))

        # Find the length of the shorted path from vertex #1 to vertex #6 using
        # BFS
        print(graph.shortest_path(src_vtx_id=1, dest_vtx_id=6))


if __name__ == '__main__':
    main()
