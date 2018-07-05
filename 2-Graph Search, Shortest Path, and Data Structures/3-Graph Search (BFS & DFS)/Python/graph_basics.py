#!usr/bin/env python3
# -*- coding: utf-8 -*-

__author__ = 'Ziang Lu'

from abc import ABC, abstractmethod
from typing import List


class IllegalArgumentError(ValueError):
    pass


class AbstractVertex(object):

    def __init__(self, vtx_id: int):
        """
        Constructor with parameter.
        :param vtx_id: int
        """
        self._vtx_id = vtx_id
        self._explored = False
        self._layer = 0

    @property
    def vtx_id(self) -> int:
        """
        Accessor of vtx_id.
        :return: int
        """
        return self._vtx_id

    @property
    def explored(self) -> bool:
        """
        Accessor of explored.
        :return: bool
        """
        return self._explored

    @property
    def layer(self) -> int:
        """
        Accessor of layer.
        :return: int
        """
        return self._layer

    def set_as_explored(self) -> None:
        """
        Sets this vertex to explored.
        :return: None
        """
        self._explored = True

    def set_as_unexplored(self) -> None:
        """
        Sets this vertex to unexplored.
        :return: None
        """
        self._explored = False

    @layer.setter
    def layer(self, layer: int) -> None:
        """
        Mutator of layer.
        :param layer: int
        :return: None
        """
        self._layer = layer


class AbstractGraph(ABC):

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
        if vtx_to_remove is None:
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
    def add_edge(self, end1_id: int, end2_id: int) -> None:
        """
        Adds a new edge to this graph.
        :param end1_id: int
        :param end2_id: int
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
    def bfs(self, src_vtx_id: int) -> List[int]:
        """
        Finds all the findable vertices starting from the given source vertex
        using BFS.
        :param src_vtx_id: int
        :return: list[int]
        """
        pass

    def clear_explored(self) -> None:
        """
        Sets all the vertices to unexplored.
        :return: None
        """
        for vtx in self._vtx_list:
            vtx.set_as_unexplored()

    @abstractmethod
    def shortest_path(self, src_vtx_id: int, dest_vtx_id: int) -> int:
        """
        Finds the length of the shortest path from the given source vertex to
        the given destination vertex.
        :param src_vtx_id: int
        :param dest_vtx_id: int
        :return: int
        """
        pass

    @abstractmethod
    def num_of_connected_components_with_bfs(self) -> int:
        """
        Returns the number of connected components of this graph using BFS.
        :return: int
        """
        pass

    # Iterative implementation of DFS ignored (simply replacing the queue with a
    # stack in BFS)

    def dfs(self, src_vtx_id: int) -> List[int]:
        """
        Finds all the findable vertices starting from the given source vertex
        using BFS.
        :param src_vtx_id: int
        :return: list[int]
        """
        # Check whether the input source vertex exists
        src_vtx = self._find_vtx(src_vtx_id)
        if src_vtx is None:
            raise IllegalArgumentError("The input source vertex doesn't exist.")

        # Initialize G as s explored and other vertices unexplored
        src_vtx.set_as_explored()

        findable_vtx_ids = [src_vtx_id]

        self._dfs_helper(vtx=src_vtx, findable_vtx_ids=findable_vtx_ids)

        return findable_vtx_ids

    @abstractmethod
    def _dfs_helper(self, vtx, findable_vtx_ids: List[int]) -> None:
        """
        Private helper function to do DFS and find all the findable vertices
        from the given vertex recursively.
        :param vtx: Vertex
        :param findable_vtx_ids: list[int]
        :return: None
        """
        pass

    @abstractmethod
    def num_of_connected_components_with_dfs(self) -> int:
        """
        Returns the number of connected components of this graph using DFS.
        :return: int
        """
        pass
