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

    @property
    def vtx_id(self) -> int:
        """
        Accessor of vtx_id.
        :return: int
        """
        return self._vtx_id


class AbstractEdge(object):

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

    @abstractmethod
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
    def bellman_ford_shortest_paths(self, src_vtx_id: int) -> List[List[int]]:
        """
        Returns the mapping between the vertices and the shortest paths from the
        given vertex using Bellman-Ford Shortest-Path Algorithm in an improved
        bottom-up way.
        Note that in this application, the vertex IDs are exactly from 0 to
        (n - 1).
        :param src_vtx_id: int
        :return: list[list[int]]
        """
        pass

    @abstractmethod
    def _reconstruct_shortest_paths(self, subproblems: List[List[int]]) -> List[
        List[int]]:
        """
        Private helper function to reconstruct the shortest paths according to
        the optimal solution using backtracking.
        :param subproblems: list[list[int]]
        :return: list[list[int]]
        """
        pass

    @abstractmethod
    def bellman_ford_shortest_paths_optimized(self, src_vtx_id: int) -> List[
        List[int]]:
        """
        Returns the mapping between the vertices and the shortest paths from the
        given vertex using Bellman-Ford Shortest-Path Algorithm in an improved
        bottom-up way with early-stopping and space optimization.
        Note that in this application, the vertex IDs are exactly from 0 to
        (n - 1).
        :param src_vtx_id: int
        :return: list[list[int]]
        """
        pass

    def _reconstruct_shortest_paths_optimized(self, penultimate_vtxs: List[
        AbstractVertex]) -> List[List[int]]:
        """
        Private helper function to reconstruct the shortest paths according to
        the penultimate vertices in the shortest paths using backtracking.
        :param penultimate_vtxs: list[AbstractVertex]
        :return: list[list[int]]
        """
        shortest_paths = []
        for vtx in self._vtx_list:
            shortest_path = []
            curr_vtx = vtx
            while curr_vtx is not None:
                shortest_path.insert(0, curr_vtx.vtx_id)
                prev_vtx = penultimate_vtxs[curr_vtx.vtx_id]
                curr_vtx = prev_vtx
            shortest_paths.append(shortest_path)
        return shortest_paths
        # Running time complexity: O(n^2)

    @abstractmethod
    def bellman_ford_shortest_paths_dest_driven(self, dest_vtx_id: int) -> List[
        List[int]]:
        """
        Returns the mapping between the vertices and the shortest paths to the
        given vertex using Bellman-Ford Shortest-Path Algorithm in an improved
        bottom-up way.
        Note that in this application, the vertex IDs are exactly from 0 to
        (n - 1).
        :param dest_vtx_id: int
        :return: list[list[int]]
        """
        pass

    @abstractmethod
    def _reconstruct_shortest_paths_dest_driven(self,
                                                subproblems: List[List[int]]) \
            -> List[List[int]]:
        """
        Private helper function to reconstruct the destination-driven shortest
        paths according to the optimal solution using backtracking.
        :param subproblems: list[list[int]]
        :return: list[list[int]]
        """
        pass

    @abstractmethod
    def bellman_ford_shortest_paths_dest_driven_optimized(self,
                                                          dest_vtx_id: int) -> \
            List[List[int]]:
        """
        Returns the mapping between the vertices and the shortest paths to the
        given vertex using Bellman-Ford Shortest-Path Algorithm in an improved
        bottom-up way with early stopping and space optimization.
        :param dest_vtx_id: int
        :return: list[list[int]]
        """
        pass

    def _reconstruct_shortest_paths_dest_driven_optimized(self, next_vtxs: List[
        AbstractVertex]) -> List[List[int]]:
        """
        Private heloer function to reconstruct the destination-driven shortest
        paths according to the next vertices in the shortest paths using
        backtracking.
        :param next_vtxs: list[AbstractVertex]
        :return: list[list[int]]
        """
        shortest_paths = []
        for vtx in self._vtx_list:
            shortest_path = []
            curr_vtx = vtx
            while curr_vtx is not None:
                shortest_path.append(curr_vtx.vtx_id)
                next_vtx = next_vtxs[curr_vtx.vtx_id]
                curr_vtx = next_vtx
            shortest_paths.append(shortest_path)
        return shortest_paths
        # Running time complexity: O(n^2)

    @abstractmethod
    def shortest_paths_dest_driven_push_based(self, dest_vtx_id: int) -> List[
        List[int]]:
        """
        Returns the mapping between the vertices and the shortest paths to the
        given vertex in a push-based way.
        :param dest_vtx_id: int
        :return: list[list[int]]
        """
        pass
