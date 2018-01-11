#!usr/bin/env python3
# -*- coding: utf-8 -*-

"""
Adjacency list representation of undirected graph.

Note that parallel edges are allowed, but not self-loops.
"""

__author__ = 'Ziang Lu'

import random


class IllegalArgumentError(ValueError):
    pass


class Vertex(object):
    def __init__(self, vtx_id):
        """
        Constructor with parameter.
        :param vtx_id: int
        """
        self._vtx_id = vtx_id
        self._freq_of_neighbors = {}
        self._edges = []

    @property
    def vtx_id(self):
        """
        Accessor of vtx_id.
        :return: int
        """
        return self._vtx_id

    @property
    def freq_of_neighbors(self):
        """
        Accessor of freq_of_neighbors.
        :return: dict{int: int}
        """
        return self._freq_of_neighbors

    def get_edge_with_neighbor(self, neighbor):
        """
        Returns the first edge with the given neighbor.
        :param neighbor: Vertex
        :return: Edge
        """
        # Check whether the input neighbor is None
        if neighbor is None:
            raise IllegalArgumentError('The input neighbor should not be None.')

        for edge in self._edges:
            if (edge.end1 is self and edge.end2 is neighbor) or \
                    (edge.end1 is neighbor and edge.end2 is self):
                return edge
        # Not found
        return None

    @property
    def edges(self):
        """
        Accessor of edges.
        :return: list[Edge]
        """
        return self._edges

    def add_edge(self, new_edge):
        """
        Adds the given edge to this vertex.
        :param new_edge: Edge
        :return: None
        """
        # Check whether the input edge is None
        if new_edge is None:
            raise IllegalArgumentError('The edge to add should not be None.')
        # Check whether the input edge involves this vertex
        if (new_edge.end1 is not self) and (new_edge.end2 is not self):
            raise IllegalArgumentError('The edge to add should involve this '
                                       'vertex.')

        self._edges.append(new_edge)

        # Find the neighbor associated with the input edge
        if new_edge.end1 is self:  # endpoint2 is the neighbor.
            neighbor = new_edge.end2
        else:  # endpoint1 is the neighbor
            neighbor = new_edge.end1
        # Update the frequency of the neighbor
        freq = self._freq_of_neighbors.get(neighbor.vtx_id, 0)
        freq += 1
        self._freq_of_neighbors[neighbor.vtx_id] = freq

    def remove_edge(self, edge_to_remove):
        """
        Removes the given edge from this vertex.
        :param edge_to_remove: Edge
        :return: None
        """
        # Check whether the input edge is None
        if edge_to_remove is None:
            raise IllegalArgumentError('The edge to remove should not be None.')
        # Check whether the input edge involves this vertex
        if (edge_to_remove.end1 is not self) and \
                (edge_to_remove.end2 is not self):
            raise IllegalArgumentError('The edge to remove should involve this '
                                       'vertex.')

        self._edges.remove(edge_to_remove)

        # Find the neighbor associated with the input edge
        if edge_to_remove.end1 is self:  # endpoint2 is the neighbor.
            neighbor = edge_to_remove.end2
        else:  # endpoint1 is the neighbor
            neighbor = edge_to_remove.end1
        # Update the frequency of the neighbor
        freq = self._freq_of_neighbors.get(neighbor.vtx_id)
        if freq == 1:
            self._freq_of_neighbors.pop(neighbor.vtx_id)
        else:
            freq -= 1
            self._freq_of_neighbors[neighbor.vtx_id] = freq

    def __repr__(self):
        """
        String representation of this vertex.
        :return: str
        """
        return 'Vertex #%d, Its neighbors and frequencies: %s' % \
            (self._vtx_id, self._freq_of_neighbors)


class Edge(object):
    def __init__(self, end1, end2):
        """
        Constructor with parameter.
        :param end1: Vertex
        :param end2: Vertex
        """
        # Check whether the input endpoints are None
        if end1 is None or end2 is None:
            print('The endpoints should not be None.')
            return
        # Check whether the input endpoints are the same (self-loop)
        if end1.vtx_id == end2.vtx_id:
            print('The endpoints are the same (self-loop).')
            return

        self._end1 = end1
        self._end2 = end2

    @property
    def end1(self):
        """
        Accessor of end1.
        :return: Vertex
        """
        return self._end1

    @property
    def end2(self):
        """
        Accessor of end2.
        :return: Vertex
        """
        return self._end2

    def __repr__(self):
        """
        String representation of this edge.
        :return: str
        """
        return 'Edge between Vertex #%d and Vertex #%d' % \
            (self._end1.vtx_id, self._end2.vtx_id)


class AdjacencyList(object):
    def __init__(self):
        """
        Default constructor.
        """
        self._vtx_list = []
        self._edge_list = []

    def add_vtx(self, new_vtx_id):
        """
        Adds a new vertex to this graph.
        :param new_vtx_id: int
        :return: None
        """
        # Check whether the input vertex is repeated
        if self._find_vtx(vtx_id=new_vtx_id):
            raise IllegalArgumentError('The input vertex is repeated.')

        new_vtx = Vertex(new_vtx_id)
        self._vtx_list.append(new_vtx)

    def _find_vtx(self, vtx_id):
        """
        Private helper function to find the given vertex in this adjacency list.
        :param vtx_id: int
        :return: Vertex
        """
        for vtx in self._vtx_list:
            if vtx.vtx_id == vtx_id:
                return vtx
        # Not found
        return None

    def remove_vtx(self, vtx_id):
        """
        Removes a vertex from this graph.
        :param vtx_id: int
        :return: None
        """
        # Check whether the input vertex exists
        vtx_to_remove = self._find_vtx(vtx_id=vtx_id)
        if vtx_to_remove is None:
            raise IllegalArgumentError("The input vertex doesn't exist.")

        self._remove_vtx(vtx_to_remove=vtx_to_remove)

    def _remove_vtx(self, vtx_to_remove):
        """
        Private helper function to remove the given vertex from this graph.
        :param vtx_to_remove: Vertex
        :return: None
        """
        # Remove all the edges associated with the vertex to remove
        edges_to_remove = vtx_to_remove.edges
        while len(edges_to_remove) > 0:
            edge_to_remove = edges_to_remove[0]
            self._remove_edge(edge_to_remove=edge_to_remove)
        # Remove the vertex
        self._vtx_list.remove(vtx_to_remove)

    def add_edge(self, end1_id, end2_id):
        """
        Adds a new edge to this graph.
        :param end1_id: int
        :param end2_id: int
        :return: None
        """
        # Check whether the input endpoints both exist
        end1, end2 = self._find_vtx(vtx_id=end1_id), \
            self._find_vtx(vtx_id=end2_id)
        if end1 is None or end2 is None:
            raise IllegalArgumentError("The endpoints don't both exist.")
        # Check whether the input endpoints are the same (self-loop)
        if end1_id == end2_id:
            raise IllegalArgumentError("The endpoints are the same "
                                       "(self-loop).")

        new_edge = Edge(end1, end2)
        self._add_edge(new_edge=new_edge)

    def _add_edge(self, new_edge):
        """
        Private helper function to add the given edge to this graph.
        :param new_edge: Edge
        :return: None
        """
        end1, end2 = new_edge.end1, new_edge.end2
        end1.add_edge(new_edge)
        end2.add_edge(new_edge)
        self._edge_list.append(new_edge)

    def remove_edge(self, end1_id, end2_id):
        """
        Removes an edge from this graph.
        :param end1_id: int
        :param end2_id: int
        :return: None
        """
        # Check whether the input endpoints both exist
        end1, end2 = self._find_vtx(vtx_id=end1_id), \
            self._find_vtx(vtx_id=end2_id)
        if end1 is None or end2 is None:
            raise IllegalArgumentError("The endpoints don't both exist.")

        # Check whether the edge to remove exists
        edge_to_remove = end1.get_edge_with_neighbor(neighbor=end2)
        if edge_to_remove is None:
            raise IllegalArgumentError("The edge to remove doesn't exist.")

        self._remove_edge(edge_to_remove=edge_to_remove)

    def _remove_edge(self, edge_to_remove):
        """
        Private helper function to remove the given edge from this graph.
        :param edge_to_remove: Edge
        :return: None
        """
        end1, end2 = edge_to_remove.end1, edge_to_remove.end2
        end1.remove_edge(edge_to_remove)
        end2.remove_edge(edge_to_remove)
        self._edge_list.remove(edge_to_remove)

    def remove_edges_between_pair(self, end1_id, end2_id):
        """
        Removes all the edges between a vertex pair from this graph.
        :param end1_id: int
        :param end2_id: int
        :return: None
        """
        try:
            while True:
                self.remove_edge(end1_id=end1_id, end2_id=end2_id)
        except IllegalArgumentError:
            pass

    def show_graph(self):
        """
        Shows this graph.
        :return: None
        """
        print('The vertices are:')
        for vtx in self._vtx_list:
            print(vtx)
        print('The edges are:')
        for edge in self._edge_list:
            print(edge)

    def compute_minimum_cut(self):
        """
        Computes a cut with the fewest number of crossing edges.
        :return: int
        """
        if len(self._vtx_list) <= 1:
            return 0

        # While there are more than 2 vertices
        while len(self._vtx_list) > 2:
            # 1. Pick up an edge randomly
            random_idx = random.randrange(len(self._edge_list))
            edge_to_contract = self._edge_list[random_idx]
            end1, end2 = edge_to_contract.end1, edge_to_contract.end2

            # 2. Contract the two endpoints into a single vertex

            # (1) Remove all the edges between the pair
            self.remove_edges_between_pair(end1_id=end1.vtx_id,
                                           end2_id=end2.vtx_id)
            # (2) Create a merged vertex
            merged_vtx_id = self._get_next_vtx_id()
            self.add_vtx(new_vtx_id=merged_vtx_id)
            # (3) Reconstruct the edges associated with the two endpoints to the
            # merged vertex, and remove the two endpoints
            self._reconstruct_edges(end=end1, merged_vtx_id=merged_vtx_id)
            self._remove_vtx(vtx_to_remove=end1)
            self._reconstruct_edges(end=end2, merged_vtx_id=merged_vtx_id)
            self._remove_vtx(vtx_to_remove=end2)
        return len(self._edge_list)

    def _get_next_vtx_id(self):
        """
        Private helper function to get the next available vertex ID, which is 1
        greater than the current largest vertex ID.
        :return: int
        """
        vtx_ids = []
        for vtx in self._vtx_list:
            vtx_ids.append(vtx.vtx_id)
        return max(vtx_ids) + 1

    def _reconstruct_edges(self, end, merged_vtx_id):
        """
        Private helper function to reconstruct the edges associated with the
        given endpoint to the given merged vertex.
        :param end: Vertex
        :param merged_vtx_id: int
        :return: None
        """
        freq_of_neighbors = end.freq_of_neighbors
        for neighbor_id, freq in freq_of_neighbors.items():
            for i in range(freq):
                self.add_edge(end1_id=merged_vtx_id, end2_id=neighbor_id)
