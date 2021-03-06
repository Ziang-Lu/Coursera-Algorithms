#!usr/bin/env python3
# -*- coding: utf-8 -*-

__author__ = 'Ziang Lu'

from abc import ABC, abstractmethod
from typing import List


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


class AbstractEdge(object):
    __slots__ = ['_length']

    def __init__(self, length: int):
        """
        Constructor with parameter.
        :param length: int
        """
        self._length = length

    @property
    def length(self) -> int:
        """
        Accessor of length.
        :return: int
        """
        return self._length


class AbstractGraph(ABC):
    __slots__ = ['_vtx_list', '_edge_list']

    _INFINITY = 1000000

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
    def _add_edge(self, new_edge: AbstractEdge) -> None:
        """
        Private helper function to add the given edge to this graph.
        :param new_edge: AbstractEdge
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

    def _remove_edge(self, edge_to_remove: AbstractEdge) -> None:
        """
        Private helper function to remove the given edge from this graph.
        :param edge_to_remove: AbstractEdge
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
    def floyd_warshall_apsp(self) -> List[List[int]]:
        """
        Returns the all-pair shortest paths (APSP) using Floyd-Warshall APSP
        Algorithm in a bottom-up way.
        Note that in this application, the vertex IDs are exactly from 0 to
        (n - 1).
        The reconstruction process is easy to come up with but difficult to
        implement, and thus is omitted.
        :return: list[list[int]]
        """
        pass

    @abstractmethod
    def floyd_warshall_apsp_optimized(self) -> List[List[int]]:
        """
        Returns the all-pair shortest paths (APSP) using Floyd-Warshall APSP
        Algorithm in a bottom-up way with space optimization.
        Note that in this application, the vertex IDs are exactly from 0 to
        (n - 1).
        The reconstruction process is easy to come up with but difficult to
        implement, and thus is omitted.
        :return: list[list[int]]
        """
        pass
