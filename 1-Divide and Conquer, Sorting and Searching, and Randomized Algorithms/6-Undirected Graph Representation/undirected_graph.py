#!usr/bin/env python3
# -*- coding: utf-8 -*-

"""
Adjacency list representation of undirected graph.

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
            print('The input edge should not be None.')
            return
        # Check whether the input edge involves this vertex
        if new_edge.end1 != self and new_edge.end2 != self:
            print('The input edge should involve this vertex.')
            return

        self._my_edges.append(new_edge)

    def remove_edge(self, edge_to_remove):
        """
        Removes the given edge from this vertex.
        :param edge_to_remove: Edge
        :return: None
        """
        # Check whether the input edge is None
        if edge_to_remove is None:
            print('The input edge should not be None.')
            return
        # Check whether the input edge involves this vertex
        if edge_to_remove.end1 != self and edge_to_remove.end2 != self:
            print('The input edge should involve this vertex.')
            return

        self._my_edges.remove(edge_to_remove)

    def __repr__(self):
        """
        String representation of this vertex.
        :return:
        """
        return "Vertex #%d" % self._vtx_id

    def __eq__(self, other):
        """
        Equality test between this and the given vertex.
        :param other: Vertex
        :return: bool
        """
        return isinstance(other, Vertex) and self._vtx_id == other.vtx_id


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

    def add_edge(self, end1_id, end2_id):
        """
        Adds a new edge to this graph.
        :param end1_id: int
        :param end2_id: int
        :return: None
        """
        # Check whether the input vertices both exist
        end1, end2 = self._find_vtx(vtx_id=end1_id), \
            self._find_vtx(vtx_id=end2_id)
        if end1 is None or end2 is None:
            print("The input vertices don't both exist.")
            return
        # Check whether the edge to add already exists.
        if self._find_edge(end1_id=end1_id, end2_id=end2_id):
            print("The edge to add already exists.")
            return

        new_edge = Edge(end1, end2)
        end1.add_edge(new_edge=new_edge)
        end2.add_edge(new_edge=new_edge)
        self._edge_list.append(new_edge)

    def _find_edge(self, end1_id, end2_id):
        """
        Private helper function to find the edge connecting the given two
        vertices in this adjacency list.
        :param end1_id: int
        :param end2_id: int
        :return: Edge
        """
        for edge in self._edge_list:
            curr_end1_id, curr_end2_id = edge.end1.vtx_id, edge.end2.vtx_id
            if (curr_end1_id == end1_id and curr_end2_id == end2_id) or \
                    (curr_end1_id == end2_id and curr_end2_id == end1_id):
                return edge
        # Not found
        return None

    def remove_edge(self, end1_id, end2_id):
        """
        Removes an edge from this graph.
        :param end1_id: int
        :param end2_id: int
        :return: None
        """
        # Check whether the input vertices both exist
        end1, end2 = self._find_vtx(vtx_id=end1_id), \
            self._find_vtx(vtx_id=end2_id)
        if end1 is None or end2 is None:
            print("The input vertices don't both exist.")
            return
        # Check whether the edge to remove exists
        edge_to_remove = self._find_edge(end1_id=end1_id, end2_id=end2_id)
        if edge_to_remove is None:
            print("The edge to remove doesn't exist.")
            return

        end1.remove_edge(edge_to_remove)
        end2.remove_edge(edge_to_remove)
        self._edge_list.remove(edge_to_remove)

    def show_graph(self):
        """
        Shows this graph.
        :return:
        """
        print('The vertices are:')
        for vtx in self._vtx_list:
            print(vtx)
        print('The edges are:')
        for edge in self._edge_list:
            print(edge)
