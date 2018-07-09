#!usr/bin/env python3
# -*- coding: utf-8 -*-

__author__ = 'Ziang Lu'

from abc import ABC, abstractmethod


class IllegalArgumentError(ValueError):
    pass


class AbstractVertex(object):
    __slots__ = ['_vtx_id']

    def __init__(self, vtx_id: int):
        """
        Constructor with parameter.
        :param vtx_id: int
        """
        self._vtx_id = vtx_id

    @property
    def vtx_id(self) -> int:
        """
        Accessor of vtx_id.
        :return: int
        """
        return self._vtx_id


class AbstractGraph(ABC):
    __slots__ = ['_vtx_list', '_edge_list']

    def __init__(self):
        """
        Default constructor.
        """
        self._vtx_list = []
        self._edge_list = []

    @abstractmethod
    def add_vtx(self, new_vtx_id: int) -> None:
        """
        Adds a new vertex to this graph.
        :param new_vtx_id: int
        :return: None
        """
        pass

    def _find_vtx(self, vtx_id: int) -> AbstractVertex:
        """
        Private helper function to find the given vertex in this adjacency list.
        :param vtx_id: int
        :return: AbstractVertex
        """
        for vtx in self._vtx_list:
            if vtx.vtx_id == vtx_id:
                return vtx
        # Not found
        return None

    def remove_vtx(self, vtx_id: int) -> None:
        """
        Removes a vertex from this graph.
        :param vtx_id: int
        :return: None
        """
        # Check whether the input vertex exists
        vtx_to_remove = self._find_vtx(vtx_id)
        if not vtx_to_remove:
            raise IllegalArgumentError("The input vertex doesn't exist.")

        self._remove_vtx(vtx_to_remove=vtx_to_remove)

    @abstractmethod
    def _remove_vtx(self, vtx_to_remove: AbstractVertex) -> None:
        """
        Private helper function to remove the given vertex from this graph.
        :param vtx_to_remove: AbstractVertex
        :return: None
        """
        pass

    @abstractmethod
    def add_edge(self, end1_id: int, end2_id: int, length: int) -> None:
        """
        Adds a new edge to this graph.
        :param end1_id: int
        :param end2_id: int
        :param length: int
        :return: None
        """
        pass

    @abstractmethod
    def _add_edge(self, new_edge) -> None:
        """
        Private helper function to add the given edge to this graph.
        :param new_edge: Edge
        :return: None
        """
        pass

    @abstractmethod
    def remove_edge(self, end1_id: int, end2_id: int) -> None:
        """
        Removes an edge from this graph.
        :param end1_id: int
        :param end2_id: int
        :return: None
        """
        pass

    @abstractmethod
    def _remove_edge(self, edge_to_remove) -> None:
        """
        Private helper function to remove the given edge from this graph.
        :param edge_to_remove: Edge
        :return: None
        """
        pass

    def show_graph(self) -> None:
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

    @abstractmethod
    def compute_maximum_cut(self) -> int:
        """
        Computes a cut with maximum number of crossing edges using local search
        heuristic.
        :return: int
        """
        pass
