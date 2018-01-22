#!usr/bin/env python3
# -*- coding: utf-8 -*-

__author__ = 'Ziang Lu'


class IllegalArgumentError(ValueError):
    pass


class AbstractVertex(object):
    def __init__(self, vtx_id):
        """
        Constructor with parameter.
        :param vtx_id: int
        """
        self._vtx_id = vtx_id

    @property
    def vtx_id(self):
        """
        Accessor of vtx_id.
        :return: int
        """
        return self._vtx_id


class AbstractGraph(object):
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
        pass

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
        vtx_to_remove = self._find_vtx(vtx_id)
        if vtx_to_remove is None:
            raise IllegalArgumentError("The input vertex doesn't exist.")

        self._remove_vtx(vtx_to_remove=vtx_to_remove)

    def _remove_vtx(self, vtx_to_remove):
        """
        Private helper function to remove the given vertex from this graph.
        :param vtx_to_remove: Vertex
        :return: None
        """
        pass

    def add_edge(self, end1_id, end2_id):
        """
        Adds a new edge to this graph.
        :param end1_id: int
        :param end2_id: int
        :return: None
        """
        pass

    def _add_edge(self, new_edge):
        """
        Private helper function to add the given edge to this graph.
        :param new_edge: Edge
        :return: None
        """
        pass

    def remove_edge(self, end1_id, end2_id):
        """
        Removes an edge from this graph.
        :param end1_id: int
        :param end2_id: int
        :return: None
        """
        pass

    def _remove_edge(self, edge_to_remove):
        """
        Private helper function to remove the given edge from this graph.
        :param edge_to_remove: Edge
        :return: None
        """
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

