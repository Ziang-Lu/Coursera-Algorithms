#!usr/bin/env python3
# -*- coding: utf-8 -*-

"""
Adjacency list representation of directed graph.

Note that parallel edges are allowed, but not self-loops.
"""

__author__ = 'Ziang Lu'


class Vertex(object):
    def __init__(self, vtx_id):
        """
        Constructor with parameter.
        :param vtx_id: int
        """
        self._vtx_id = vtx_id
        self._emissive_edges = []
        self._freq_of_emissive_neighbors = {}
        self._incident_edges = []
        self._freq_of_incident_neighbors = {}

    @property
    def vtx_id(self):
        """
        Accessor of vtx_id.
        :return: int
        """
        return self._vtx_id

    def add_emissive_edge(self, new_emissive_edge):
        """
        Adds the given emissive edge to this vertex.
        :param new_emissive_edge: Edge
        :return: None
        """
        # Check whether the input emissive edge is None
        if new_emissive_edge is None:
            print('The emissive edge to add should not be None.')
            return
        # Check whether the input emissive edge involves this vertex as the tail
        if new_emissive_edge.tail != self:
            print('The emissive edge to add should involve this vertex as the '
                  'tail.')
            return

        self._emissive_edges.append(new_emissive_edge)

        emissive_neighbor = new_emissive_edge.head
        freq = self._freq_of_emissive_neighbors.get(emissive_neighbor.vtx_id, 0)
        freq += 1
        self._freq_of_emissive_neighbors[emissive_neighbor.vtx_id] = freq

    def add_incident_edge(self, new_incident_edge):
        """
        Adds the given incident edge to this vertex
        :param new_incident_edge: Edge
        :return: None
        """
        # Check whether the input incident edge is None
        if new_incident_edge is None:
            print('The incident edge to add should not be None.')
            return
        # Check whether the input incident edge involves this vertex as the head
        if new_incident_edge.head != self:
            print('The incident edge to add should involve this vertex as the '
                  'head.')
            return

        self._incident_edges.append(new_incident_edge)

        incident_neighbor = new_incident_edge.tail
        freq = self._freq_of_incident_neighbors.get(incident_neighbor.vtx_id, 0)
        freq += 1
        self._freq_of_incident_neighbors[incident_neighbor.vtx_id] = freq

    def remove_emissive_edge(self, emissive_edge_to_remove):
        """
        Removes the given emissive edge from this vertex.
        :param emissive_edge_to_remove: Edge
        :return: None
        """
        # Check whether the input emissive edge is None
        if emissive_edge_to_remove is None:
            print('The emissive edge to remove should not be None.')
            return
        # Check whether the input emissive edge involves this vertex as the tail
        if emissive_edge_to_remove.tail != self:
            print('The emissive edge to remove should involve this vertex as '
                  'the tail.')
            return

        self._emissive_edges.remove(emissive_edge_to_remove)

        emissive_neighbor = emissive_edge_to_remove.head
        freq = self._freq_of_emissive_neighbors.get(emissive_neighbor.vtx_id)
        if freq == 1:
            self._freq_of_emissive_neighbors.pop(emissive_neighbor.vtx_id)
        else:
            freq -= 1
            self._freq_of_emissive_neighbors[emissive_neighbor.vtx_id] = freq

    def remove_incident_edge(self, incident_edge_to_remove):
        """
        Removes the given incident edge from this vertex.
        :param incident_edge_to_remove: Edge
        :return: None
        """
        # Check whether the input incident edge is None
        if incident_edge_to_remove is None:
            print('The incident edge to remove should not be None.')
            return
        # Check whether the input incident edge involves this vertex as the head
        if incident_edge_to_remove.head != self:
            print('The incident edge to remove should involve this vertex as '
                  'the head.')
            return

        self._incident_edges.remove(incident_edge_to_remove)

        incident_neighbor = incident_edge_to_remove.tail
        freq = self._freq_of_incident_neighbors.get(incident_neighbor.vtx_id)
        if freq == 1:
            self._freq_of_incident_neighbors.pop(incident_neighbor.vtx_id)
        else:
            freq -= 1
            self._freq_of_incident_neighbors[incident_neighbor.vtx_id] = freq

    def __repr__(self):
        """
        String representation of this vertex.
        :return: str
        """
        s = 'Vertex #%d\n' % self._vtx_id
        s += 'Its emissive neighbors and frequencies: %s\n' % \
             self._freq_of_emissive_neighbors
        s += 'Its incident neighbors and frequencies: %s\n' % \
             self._freq_of_incident_neighbors
        return s

    def __eq__(self, other):
        """
        Equality test between this and the given vertex
        :param other: Vertex
        :return: bool
        """
        return isinstance(other, Vertex) and self._vtx_id == other.vtx_id


class Edge(object):
    def __init__(self, tail, head):
        """
        Constructor with parameter.
        :param tail: Vertex
        :param head: Vertex
        """
        # Check whether the input endpoints are None
        if tail is None or head is None:
            print('The endpoints should not be None.')
            return
        # Check whether the input endpoints are the same (self-loop)
        if tail.vtx_id == head.vtx_id:
            print('The endpoints are the same (self-loop).')
            return

        self._tail = tail
        self._head = head

    @property
    def tail(self):
        """
        Accessor of tail.
        :return: Vertex
        """
        return self._tail

    @property
    def head(self):
        """
        Accessor of head.
        :return: Vertex
        """
        return self._head

    def __repr__(self):
        """
        String representation of this edge.
        :return: str
        """
        return 'Edge from Vertex #%d to Vertex #%d' % \
            (self._tail.vtx_id, self._head.vtx_id)


class AdjacencyList(object):
    def __init__(self):
        """
        Default constructor.
        """
        self.vtx_list = []
        self.edge_list = []

    def add_vtx(self, new_vtx_id):
        """
        Adds a new vertex to this graph.
        :param new_vtx_id: int
        :return: None
        """
        # Check whether the input vertex is repeated
        if self._find_vtx(vtx_id=new_vtx_id):
            print('The input vertex is repeated.')
            return

        new_vtx = Vertex(new_vtx_id)
        self.vtx_list.append(new_vtx)

    def _find_vtx(self, vtx_id):
        """
        Private helper function to find the given vertex in this adjacency list.
        :param vtx_id: int
        :return: Vertex
        """
        for vtx in self.vtx_list:
            if vtx.vtx_id == vtx_id:
                return vtx
        # Not found
        return None

    def add_edge(self, tail_id, head_id):
        """
        Adds a new egde to this graph.
        :param tail_id: int
        :param head_id: int
        :return: None
        """
        # Check whether the input vertices both exist
        tail, head = self._find_vtx(vtx_id=tail_id), \
            self._find_vtx(vtx_id=head_id)
        if tail is None or head is None:
            print('The input vertices don\'t both exist.')
            return

        new_edge = Edge(tail, head)
        tail.add_emissive_edge(new_edge)
        head.add_incident_edge(new_edge)
        self.edge_list.append(new_edge)

    def remove_edge(self, tail_id, head_id):
        """
        Removes an edge from this graph.
        :param tail_id: int
        :param head_id: int
        :return: None
        """
        # Check whether the input vertices both exist
        tail, head = self._find_vtx(vtx_id=tail_id), \
            self._find_vtx(vtx_id=head_id)
        if tail is None or head is None:
            print('The input vertices don\'t both exist.')
            return
        # Check whether the edge to remove exists
        edge_to_remove = self._find_edge(tail_id=tail_id, head_id=head_id)
        if edge_to_remove is None:
            print('The edge to remove doesn\'t exist.')
            return

        tail.remove_emissive_edge(edge_to_remove)
        head.remove_incident_edge(edge_to_remove)
        self.edge_list.remove(edge_to_remove)

    def _find_edge(self, tail_id, head_id):
        """
        Private helper function to find the first edge from the given tail to
        the given head in this adjacency list.
        :param tail_id: int
        :param head_id: int
        :return: Edge
        """
        for edge in self.edge_list:
            if edge.tail.vtx_id == tail_id and edge.head.vtx_id == head_id:
                return edge
        # Not found
        return None

    def show_graph(self):
        """
        Shows this graph.
        :return: None
        """
        print('The vertices are:')
        for vtx in self.vtx_list:
            print(vtx)
        print('The edges are:')
        for edge in self.edge_list:
            print(edge)
