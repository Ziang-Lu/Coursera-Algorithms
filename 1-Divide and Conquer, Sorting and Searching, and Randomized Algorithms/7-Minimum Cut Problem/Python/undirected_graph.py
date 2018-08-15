#!usr/bin/env python3
# -*- coding: utf-8 -*-

"""
Adjacency list representation of undirected graph.

Note that parallel edges are allowed, but not self-loops.
"""

__author__ = 'Ziang Lu'

import random

from graph_basics import AbstractGraph, AbstractVertex


class IllegalArgumentError(ValueError):
    pass


class Vertex(AbstractVertex):
    __slots__ = ['_freq_of_neighbors', '_edges']

    def __init__(self, vtx_id: int):
        """
        Constructor with parameter.
        :param vtx_id: int
        """
        super().__init__(vtx_id)
        self._freq_of_neighbors = {}
        self._edges = []

    def get_edge_with_neighbor(self, neighbor: AbstractVertex):
        """
        Returns the first edge with the given neighbor.
        :param neighbor: AbstractVertex
        :return: UndirectedEdge
        """
        # Check whether the input neighbor is None
        if not neighbor:
            raise IllegalArgumentError('The input neighbor should not be None.')

        for edge in self._edges:
            if (edge.end1 is self and edge.end2 is neighbor) or \
                    (edge.end1 is neighbor and edge.end2 is self):
                return edge
        # Not found
        return None

    @property
    def edges(self) -> list:
        """
        Accessor of edges.
        :return: list[UndirectedEdge]
        """
        return self._edges

    def add_edge(self, new_edge) -> None:
        """
        Adds the given edge to this vertex.
        :param new_edge: UndirectedEdge
        :return: None
        """
        # Check whether the input edge is None
        if not new_edge:
            raise IllegalArgumentError('The edge to add should not be None.')
        # Check whether the input edge involves this vertex
        if new_edge.end1 is not self and new_edge.end2 is not self:
            raise IllegalArgumentError(
                'The edge to add should involve this vertex.'
            )

        self._edges.append(new_edge)

        # Find the neighbor associated with the input edge
        if new_edge.end1 is self:  # endpoint2 is the neighbor.
            neighbor = new_edge.end2
        else:  # endpoint1 is the neighbor.
            neighbor = new_edge.end1
        # Update the frequency of the neighbor
        freq = self._freq_of_neighbors.get(neighbor.vtx_id, 0)
        freq += 1
        self._freq_of_neighbors[neighbor.vtx_id] = freq

    def remove_edge(self, edge_to_remove) -> None:
        """
        Removes the given edge from this vertex.
        :param edge_to_remove: UndirectedEdge
        :return: None
        """
        # Check whether the input edge is None
        if not edge_to_remove:
            raise IllegalArgumentError('The edge to remove should not be None.')
        # Check whether the input edge involves this vertex
        if edge_to_remove.end1 is not self and edge_to_remove.end2 is not self:
            raise IllegalArgumentError(
                'The edge to remove should involve this vertex.'
            )

        self._edges.remove(edge_to_remove)

        # Find the neighbor associated with the input edge
        if edge_to_remove.end1 is self:  # endpoint2 is the neighbor.
            neighbor = edge_to_remove.end2
        else:  # endpoint1 is the neighbor.
            neighbor = edge_to_remove.end1
        # Update the frequency of the neighbor
        freq = self._freq_of_neighbors.get(neighbor.vtx_id)
        if freq == 1:
            self._freq_of_neighbors.pop(neighbor.vtx_id)
        else:
            freq -= 1
            self._freq_of_neighbors[neighbor.vtx_id] = freq

    def __repr__(self):
        return f'Vertex #{self._vtx_id}, Its neighbors and frequencies: {self._freq_of_neighbors}'


class UndirectedEdge(object):
    __slots__ = ['_end1', '_end2']

    def __init__(self, end1: Vertex, end2: Vertex):
        """
        Constructor with parameter.
        :param end1: Vertex
        :param end2: Vertex
        """
        self._end1 = end1
        self._end2 = end2

    @property
    def end1(self) -> Vertex:
        """
        Accessor of end1.
        :return: Vertex
        """
        return self._end1

    @property
    def end2(self) -> Vertex:
        """
        Accessor of end2.
        :return: Vertex
        """
        return self._end2

    @end1.setter
    def end1(self, end1: Vertex) -> None:
        """
        Mutator of end1.
        :param end1: Vertex
        :return: None
        """
        self._end1 = end1

    @end2.setter
    def end2(self, end2: Vertex) -> None:
        """
        Mutator of end2.
        :param end2: Vertex
        :return: None
        """
        self._end2 = end2

    def __repr__(self):
        return f'Edge between Vertex #{self._end1.vtx_id} and Vertex #{self._end2.vtx_id}'


class UndirectedGraph(AbstractGraph):
    __slots__ = []

    def __init__(self):
        """
        Default constructor.
        """
        super().__init__()

    def add_vtx(self, new_vtx_id):
        # Check whether the input vertex is repeated
        if self._find_vtx(new_vtx_id):
            raise IllegalArgumentError('The input vertex is repeated.')

        new_vtx = Vertex(new_vtx_id)
        self._vtx_list.append(new_vtx)

    def _remove_vtx(self, vtx_to_remove):
        # Remove all the edges associated with the vertex to remove
        edges_to_remove = vtx_to_remove.edges
        while len(edges_to_remove):
            self._remove_edge(edge_to_remove=edges_to_remove[0])
        # Remove the vertex
        self._vtx_list.remove(vtx_to_remove)

    def add_edge(self, end1_id, end2_id):
        # Check whether the input endpoints both exist
        end1, end2 = self._find_vtx(end1_id), self._find_vtx(end2_id)
        if not end1 or not end2:
            raise IllegalArgumentError(
                "The endpoints don't both exist."
            )
        # Check whether the input endpoints are the same (self-loop)
        if end1_id == end2_id:
            raise IllegalArgumentError(
                'The endpoints are the same (self-loop).'
            )

        new_edge = UndirectedEdge(end1, end2)
        self._add_edge(new_edge=new_edge)

    def _add_edge(self, new_edge):
        end1, end2 = new_edge.end1, new_edge.end2
        end1.add_edge(new_edge)
        end2.add_edge(new_edge)
        self._edge_list.append(new_edge)

    def remove_edge(self, end1_id, end2_id):
        # Check whether the input endpoints both exist
        end1, end2 = self._find_vtx(end1_id), self._find_vtx(vtx_id=end2_id)
        if not end1 or not end2:
            raise IllegalArgumentError("The endpoints don't both exist.")

        # Check whether the edge to remove exists
        edge_to_remove = end1.get_edge_with_neighbor(end2)
        if not edge_to_remove:
            raise IllegalArgumentError("The edge to remove doesn't exist.")

        self._remove_edge(edge_to_remove=edge_to_remove)

    def _remove_edge(self, edge_to_remove):
        end1, end2 = edge_to_remove.end1, edge_to_remove.end2
        end1.remove_edge(edge_to_remove)
        end2.remove_edge(edge_to_remove)
        self._edge_list.remove(edge_to_remove)

    def remove_edges_between_pair(self, end1_id: int, end2_id: int) -> None:
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

    def compute_minimum_cut(self) -> int:
        """
        Computes a cut with the fewest number of crossing edges.
        :return: int
        """
        if len(self._vtx_list) <= 1:
            return 0

        # While there are more than 2 vertices
        while len(self._vtx_list) > 2:
            # 1. Pick up an edge randomly
            random_idx = random.randint(0, len(self._edge_list) - 1)
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
            merged_vtx = self._find_vtx(merged_vtx_id)
            self._reconnect_edges(end=end1, merged_vtx=merged_vtx)
            self._reconnect_edges(end=end2, merged_vtx=merged_vtx)
        return len(self._edge_list)

    def _get_next_vtx_id(self) -> int:
        """
        Private helper function to get the next available vertex ID, which is 1
        greater than the current largest vertex ID.
        :return: int
        """
        return max(vtx.id for vtx in self._vtx_list) + 1

    def _reconnect_edges(self, end: Vertex, merged_vtx: Vertex) -> None:
        """
        Private helper function to reconstruct the edges associated with the
        given endpoint to the given merged vertex, and remove the endpoint.
        :param end: Vertex
        :param merged_vtx: Vertex
        :return: None
        """
        for edge_from_end in end.edges:
            # Find the neighbor
            if edge_from_end.end1 is end:  # endpoint2 is the neighbor.
                neighbor = edge_from_end.end2
                # Remove the dge from the neighbor
                neighbor.remove_edge(edge_to_remove=edge_from_end)
                # Reform the edge to connect the neighbor and the merged vertex
                edge_from_end.end1 = merged_vtx
            else:  # endpoint1 is the neighbor.
                neighbor = edge_from_end.end1
                neighbor.remove_edge(edge_to_remove=edge_from_end)
                edge_from_end.end2 = merged_vtx
            # add the new edge to both the neighbor and the merged vertex
            neighbor.add_edge(edge_from_end)
            merged_vtx.add_edge(edge_from_end)
        # Remove the endpoint
        self._vtx_list.remove(end)
