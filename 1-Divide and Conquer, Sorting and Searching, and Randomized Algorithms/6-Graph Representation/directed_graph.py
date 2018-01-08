#!usr/bin/env python3
# -*- coding: utf-8 -*-

"""
Adjacency list representation of directed graph.

Note that parallel edges and self-loops are not allowed.
"""

__author__ = 'Ziang Lu'


class Vertex(object):
    def __init__(self, vtx_id):
        """
        Constructor with parameter.
        :param vtx_id: int
        """
        self._vtx_id = vtx_id
        self._my_edges = []

    @property
    def vtx_id(self):
        """
        Accessor of vtx_id.
        :return: int
        """
        return self._vtx_id

    def add_edge(self, new_edge):
        """
        Adds the given edge to this vertex.
        :param new_edge: Edge
        :return: None
        """
        # Check whether the input edge is None
        if new_edge is None:
            print('The edge to add should not be None.')
            return
        # Check whether the input edge involves this vertex
        if new_edge.tail != self and new_edge.head != self:
            print('The edge to add should involve this vertex.')
            return

        self._my_edges.append(new_edge)

    def remove_edge(self, edge_to_remove):
        # Check whether the input edge is None
        if edge_to_remove is None:
            print('The edge to remove should not be None.')
            return
        # Check whether the input edge involves this vertex
        if edge_to_remove.tail != self and edge_to_remove.head != self:
            print('The edge to remove should involve this vertex')
            return

        self._my_edges.remove(edge_to_remove)

    def __repr__(self):
        """
        String representation of this vertex.
        :return: str
        """
        return 'Vertex #%d' % self._vtx_id

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
        return 'Edge from Vertex #%d to Vertex #d' % \
            (self._tail.vtx_id, self._head.vtx_id)


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
            print('The input vertex is repeated.')
            return

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
        # Check whether the edge to add already exists
        if self._find_edge(tail_id=tail_id, head_id=head_id):
            print('The edge to add already exists.')
            return

        new_edge = Edge(tail, head)
        tail.add_edge(new_edge)
        head.add_edge(new_edge)
        self._edge_list.append(new_edge)

    def _find_edge(self, tail_id, head_id):
        """
        Private helper function to find the edge from the given tail to the
        given head in this adjacency list.
        :param tail_id: int
        :param head_id: int
        :return: Edge
        """
        for edge in self._edge_list:
            if edge.tail.vtx_id == tail_id and edge.head.vtx_id == head_id:
                return edge
        # Not found
        return None

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

        tail.remove_edge(edge_to_remove)
        head.remove_edge(edge_to_remove)
        self._edge_list.remove(edge_to_remove)

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
