#!usr/bin/env python3
# -*- coding: utf-8 -*-

"""
Adjacency list representation of undirected graph.

Note that parallel edges and self-loops are not allowed.
"""

__author__ = 'Ziang Lu'

import random

from graph_basics import AbstractGraph, AbstractVertex


class IllegalArgumentError(ValueError):
    pass


class Vertex(AbstractVertex):
    __slots__ = ['_edges', '_neighbors']

    def __init__(self, vtx_id: int):
        """
        Constructor with parameter.
        :param vtx_id: int
        """
        super().__init__(vtx_id)
        self._edges = []
        self._neighbors = set()

    def get_edge_with_neighbor(self, neighbor: AbstractVertex):
        """
        Returns the first edge with the given neighbor.
        :param neighbor: AbstractVertex
        :return: Edge
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
        :return: list[Edge]
        """
        return self._edges

    def add_edge(self, new_edge) -> None:
        """
        Adds the given edge to this vertex.
        :param new_edge: Edge
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

        # Find the neighbor associated with the input edge
        if new_edge.end1 is self:  # endpoint2 is the neighbor.
            neighbor = new_edge.end2
        else:  # endpoint1 is the neighbor.
            neighbor = new_edge.end1
        # Check whether the input edge already exists
        if neighbor.vtx_id in self._neighbors:
            raise IllegalArgumentError('The edge to add already exists.')

        self._edges.append(new_edge)
        self._neighbors.add(neighbor.vtx_id)

    def remove_edge(self, edge_to_remove) -> None:
        """
        Removes the given edge from this vertex.
        :param edge_to_remove: Edge
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

        # Find the neighbor associated with the input edge
        if edge_to_remove.end1 is self:  # endpoint2 is the neighbor.
            neighbor = edge_to_remove.end2
        else:  # endpoint1 is the neighbor.
            neighbor = edge_to_remove.end1
        # Check whether the input edge exists
        if neighbor.vtx_id not in self._neighbors:
            raise IllegalArgumentError("The edge to remove doesn't exist.")

        self._edges.remove(edge_to_remove)
        self._neighbors.remove(neighbor.vtx_id)

    def __repr__(self):
        return f'Vertex #{self._vtx_id}, Its neighbors: {self._neighbors}'


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
        return 'Edge between Vertex #{end1_id} and Vertex #{end2_id}'.format(
            end1_id=self._end1.vtx_id, end2_id=self._end2.vtx_id
        )


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

    def add_edge(self, end1_id, end2_id, length):
        # Check whether the input endpoints both exist
        end1, end2 = self._find_vtx(end1_id), self._find_vtx(end2_id)
        if not end1 or not end2:
            raise IllegalArgumentError("The endpoints don't both exist.")
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

    def compute_maximum_cut(self):
        # Always maintain a candidate cut, and iteratively make it better and
        # better via small (local) modifications

        # 1. Get an arbitrary cut (A, B)
        group1, group2 = set(), set()
        for vtx in self._vtx_list:
            r = random.randint(0, 1)
            if r == 1:
                group1.add(vtx.vtx_id)
            else:
                group2.add(vtx.vtx_id)
        # Calculate the number of crossing edges
        n_crossing_edge = 0
        for edge in self._edge_list:
            end1, end2 = edge.end1, edge.end2
            if (end1.vtx_id in group1 and end2.vtx_id not in group2) \
                    or (end1.vtx_id not in group1 and end2.vtx_id in group2):
                n_crossing_edge += 1

        for vtx in self._vtx_list:
            # 2. While there is a vertex v with more internal edges than
            # crossing edges
            dv, cv = 0, 0
            for edge in vtx.edges:
                # Find the neighbor
                if edge.end1 is vtx:  # endpoint2 is the neighbor.
                    neighbor = edge.end2
                else:  # endpoint1 is the neighbor.
                    neighbor = edge.end1
                if vtx.vtx_id in group1 and neighbor.vtx_id in group2 \
                        or vtx.vtx_id not in group1 \
                        and neighbor.vtx_id not in group2:
                    dv += 1
                else:
                    cv += 1
            if dv > cv:
                # Move v to the other side
                if vtx.vtx_id in group1:
                    group1.remove(vtx.vtx_id)
                    group2.add(vtx.vtx_id)
                else:
                    group2.remove(vtx.vtx_id)
                    group1.add(vtx.vtx_id)
                n_crossing_edge += dv - cv

        # 3. Return the final resulting cut
        return n_crossing_edge
        # Overall running time complexity: O(mn)
