#!usr/bin/env python3
# -*- coding: utf-8 -*-

"""
Adjacency list representation of directed graph.

Note that parallel edges are allowed, but not self-loops.
"""

__author__ = 'Ziang Lu'

from graph_basics import AbstractGraph, AbstractVertex


class IllegalArgumentError(ValueError):
    pass


class Vertex(AbstractVertex):
    __slots__ = [
        '_freq_of_emissive_neighbors',
        '_emissive_edges',
        '_freq_of_incident_neighbors',
        '_incident_edges'
    ]

    def __init__(self, vtx_id: int):
        """
        Constructor with parameter.
        :param vtx_id: int
        """
        super().__init__(vtx_id)
        self._freq_of_emissive_neighbors = {}
        self._emissive_edges = []
        self._freq_of_incident_neighbors = {}
        self._incident_edges = []

    def get_emissive_edge_with_head(self, head: AbstractVertex):
        """
        Returns the first emissive edge with the given head.
        :param head: AbstractVertex
        :return: DirectedEdge
        """
        # Check whether the input head is None
        if not head:
            raise IllegalArgumentError('The input head should not be None.')

        for emissive_edge in self._emissive_edges:
            if emissive_edge.head is head:
                return emissive_edge
        # Not found
        return None

    @property
    def emissive_edges(self) -> list:
        """
        Accessor of emissive_edges.
        :return: list[DirectedEdge]
        """
        return self._emissive_edges

    def get_incident_edge_with_tail(self, tail: AbstractVertex):
        """
        Returns the first incident edge with the given tail.
        :param tail: AbstractVertex
        :return: DirectedEdge
        """
        # Check whether the input tail is None
        if not tail:
            raise IllegalArgumentError('The input tail should not be None.')

        for incident_edge in self._incident_edges:
            if incident_edge.tail is tail:
                return incident_edge
        # Not found
        return None

    @property
    def incident_edges(self) -> list:
        """
        Accessor of incident_edges.
        :return: list[DirectedEdge]
        """
        return self._incident_edges

    def add_emissive_edge(self, new_emissive_edge) -> None:
        """
        Adds the given emissive edge to this vertex.
        :param new_emissive_edge: DirectedEdge
        :return: None
        """
        # Check whether the input emissive edge is None
        if not new_emissive_edge:
            raise IllegalArgumentError('The emissive edge to add should not be '
                                       'None.')
        # Check whether the input emissive edge involves this vertex as the tail
        if new_emissive_edge.tail is not self:
            raise IllegalArgumentError('The emissive edge to add should involve'
                                       ' this vertex as the tail.')

        self._emissive_edges.append(new_emissive_edge)

        emissive_neighbor = new_emissive_edge.head
        freq = self._freq_of_emissive_neighbors.get(emissive_neighbor.vtx_id, 0)
        freq += 1
        self._freq_of_emissive_neighbors[emissive_neighbor.vtx_id] = freq

    def add_incident_edge(self, new_incident_edge) -> None:
        """
        Adds the given incident edge to this vertex
        :param new_incident_edge: DirectedEdge
        :return: None
        """
        # Check whether the input incident edge is None
        if not new_incident_edge:
            raise IllegalArgumentError('The incident edge to add should not be '
                                       'None.')
        # Check whether the input incident edge involves this vertex as the head
        if new_incident_edge.head is not self:
            raise IllegalArgumentError('The incident edge to add should involve'
                                       ' this vertex as the head.')

        self._incident_edges.append(new_incident_edge)

        incident_neighbor = new_incident_edge.tail
        freq = self._freq_of_incident_neighbors.get(incident_neighbor.vtx_id, 0)
        freq += 1
        self._freq_of_incident_neighbors[incident_neighbor.vtx_id] = freq

    def remove_emissive_edge(self, emissive_edge_to_remove) -> None:
        """
        Removes the given emissive edge from this vertex.
        :param emissive_edge_to_remove: DirectedEdge
        :return: None
        """
        # Check whether the input emissive edge is None
        if not emissive_edge_to_remove:
            raise IllegalArgumentError(
                'The emissive edge to remove should not be None.'
            )
        # Check whether the input emissive edge involves this vertex as the tail
        if emissive_edge_to_remove.tail is not self:
            raise IllegalArgumentError(
                'The emissive edge to remove should involve this vertex as the '
                'tail.'
            )

        self._emissive_edges.remove(emissive_edge_to_remove)

        emissive_neighbor = emissive_edge_to_remove.head
        freq = self._freq_of_emissive_neighbors.get(emissive_neighbor.vtx_id)
        if freq == 1:
            self._freq_of_emissive_neighbors.pop(emissive_neighbor.vtx_id)
        else:
            freq -= 1
            self._freq_of_emissive_neighbors[emissive_neighbor.vtx_id] = freq

    def remove_incident_edge(self, incident_edge_to_remove) -> None:
        """
        Removes the given incident edge from this vertex.
        :param incident_edge_to_remove: DirectedEdge
        :return: None
        """
        # Check whether the input incident edge is None
        if not incident_edge_to_remove:
            raise IllegalArgumentError(
                'The incident edge to remove should not be None.'
            )
        # Check whether the input incident edge involves this vertex as the head
        if incident_edge_to_remove.head is not self:
            raise IllegalArgumentError(
                'The incident edge to remove should involve this vertex as the '
                'head.'
            )

        self._incident_edges.remove(incident_edge_to_remove)

        incident_neighbor = incident_edge_to_remove.tail
        freq = self._freq_of_incident_neighbors.get(incident_neighbor.vtx_id)
        if freq == 1:
            self._freq_of_incident_neighbors.pop(incident_neighbor.vtx_id)
        else:
            freq -= 1
            self._freq_of_incident_neighbors[incident_neighbor.vtx_id] = freq

    def __repr__(self):
        s = f'Vertex #{self._vtx_id}\n'
        s += f'Its emissive neighbors and frequencies: {self._freq_of_emissive_neighbors}\n'
        s += f'Its incident neighbors and frequencies: {self._freq_of_incident_neighbors}\n'
        return s

    def __eq__(self, other):
        return isinstance(other, Vertex) and self._vtx_id == other.vtx_id


class DirectedEdge(object):
    __slots__ = ['_tail', '_head']

    def __init__(self, tail: Vertex, head: Vertex):
        """
        Constructor with parameter.
        :param tail: Vertex
        :param head: Vertex
        """
        self._tail = tail
        self._head = head

    @property
    def tail(self) -> Vertex:
        """
        Accessor of tail.
        :return: Vertex
        """
        return self._tail

    @property
    def head(self) -> Vertex:
        """
        Accessor of head.
        :return: Vertex
        """
        return self._head

    @tail.setter
    def tail(self, tail: Vertex) -> None:
        """
        Mutator of tail.
        :param tail: Vertex
        :return: None
        """
        self._tail = tail

    @head.setter
    def head(self, head: Vertex) -> None:
        """
        Mutator of head.
        :param head: Vertex
        :return: None
        """
        self._head = head

    def __repr__(self):
        return f'Edge from Vertex #{self._tail.vtx_id} to Vertex #{self._head.vtx_id}'


class DirectedGraph(AbstractGraph):
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
        edges_to_remove = []
        edges_to_remove.extend(vtx_to_remove.emissive_edges)
        edges_to_remove.extend(vtx_to_remove.incident_edges)
        while len(edges_to_remove):
            self._remove_edge(edge_to_remove=edges_to_remove[0])
        # Remove the vertex
        self._vtx_list.remove(vtx_to_remove)

    def add_edge(self, tail_id, head_id):
        # Check whether the input endpoints both exist
        tail, head = self._find_vtx(tail_id), self._find_vtx(head_id)
        if not tail or not head:
            raise IllegalArgumentError("The endpoints don't both exist.")
        # Check whether the input vertices are the same
        if tail_id == head_id:
            raise IllegalArgumentError(
                'The endpoints are the same (self-loop).'
            )

        new_edge = DirectedEdge(tail, head)
        self._add_edge(new_edge=new_edge)

    def _add_edge(self, new_edge):
        tail, head = new_edge.tail, new_edge.head
        tail.add_emissive_edge(new_edge)
        head.add_incident_edge(new_edge)
        self._edge_list.append(new_edge)

    def remove_edge(self, tail_id, head_id):
        # Check whether the input endpoints both exist
        tail, head = self._find_vtx(tail_id), self._find_vtx(head_id)
        if not tail or not head:
            raise IllegalArgumentError("The endpoints don't both exist.")
        # Check whether the edge to remove exists
        edge_to_remove = tail.get_emissive_edge_with_head(head)
        if not edge_to_remove:
            raise IllegalArgumentError("The edge to remove doesn't exist.")

        self._remove_edge(edge_to_remove=edge_to_remove)

    def _remove_edge(self, edge_to_remove):
        tail, head = edge_to_remove.tail, edge_to_remove.head
        tail.remove_emissive_edge(edge_to_remove)
        head.remove_incident_edge(edge_to_remove)
        self._edge_list.remove(edge_to_remove)
