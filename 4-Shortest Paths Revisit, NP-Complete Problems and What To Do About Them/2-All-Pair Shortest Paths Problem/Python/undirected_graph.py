#!usr/bin/env python3
# -*- coding: utf-8 -*-

"""
Adjacency list representation of undirected graph.

Note that parallel edges and self-loops are not allowed.
"""

__author__ = 'Ziang Lu'

from typing import List

from graph_basics import AbstractEdge, AbstractGraph, AbstractVertex


class IllegalArgumentError(ValueError):
    pass


class Vertex(AbstractVertex):

    def __init__(self, vtx_id: int):
        """
        Constructor with parameter.
        :param vtx_id: int
        """
        super().__init__(vtx_id)
        self._edges = []
        self._neighbors = set()

    def get_edge_with_neighbor(self, neighbor: AbstractVertex) -> AbstractEdge:
        """
        Returns the first edge with the given neighbor.
        :param neighbor: AbstractVertex
        :return: AbstractEdge
        """
        # Check whether the input neighbor is None
        if neighbor is None:
            raise IllegalArgumentError('The input neighbor should not be None.')

        for edge in self._edges:
            if (edge.end1 is self and edge.end2 is neighbor) or \
                    (edge.end1 is neighbor and edge.end2 is self):
                return edge
        # Not found
        return None

    @property
    def edges(self) -> List[AbstractEdge]:
        """
        Accessor of edges.
        :return: list[AbstractEdge]
        """
        return self._edges

    def add_edge(self, new_edge: AbstractEdge) -> None:
        """
        Adds the given edge to this vertex.
        :param new_edge: AbstractEdge
        :return: None
        """
        # Check whether the input edge is None
        if new_edge is None:
            raise IllegalArgumentError('The edge to add should not be None.')
        # Check whether the input edge involves this vertex
        if (new_edge.end1 is not self) and (new_edge.end2 is not self):
            raise IllegalArgumentError('The edge to add should involve this '
                                       'vertex.')
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

    def remove_edge(self, edge_to_remove: AbstractEdge) -> None:
        """
        Removes the given edge from this vertex.
        :param edge_to_remove: AbstractEdge
        :return: None
        """
        # Check whether the input edge is None
        if edge_to_remove is None:
            raise IllegalArgumentError('The edge to remove should not be None.')
        # Check whether the input edge involves this vertex
        if (edge_to_remove.end1 is not self) and \
                (edge_to_remove.end2 is not self):
            raise IllegalArgumentError('The edge to remove should involve this '
                                       'vertex.')
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
        return 'Vertex #%d, Its neighbors: %s' % (self._vtx_id, self._neighbors)


class UndirectedEdge(AbstractEdge):

    def __init__(self, end1: Vertex, end2: Vertex, length: int):
        """
        Constructor with parameter.
        :param end1: Vertex
        :param end2: Vertex
        :param length: int
        """
        super().__init__(length)
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
        return 'Edge between Vertex #%d and Vertex #%d' % \
               (self._end1.vtx_id, self._end2.vtx_id)


class UndirectedGraph(AbstractGraph):

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
        while len(edges_to_remove) > 0:
            edge_to_remove = edges_to_remove[0]
            self._remove_edge(edge_to_remove=edge_to_remove)
        # Remove the vertex
        self._vtx_list.remove(vtx_to_remove)

    def add_edge(self, end1_id, end2_id, length):
        # Check whether the input endpoints both exist
        end1, end2 = self._find_vtx(end1_id), self._find_vtx(end2_id)
        if end1 is None or end2 is None:
            raise IllegalArgumentError("The endpoints don't both exist.")
        # Check whether the input endpoints are the same (self-loop)
        if end1_id == end2_id:
            raise IllegalArgumentError("The endpoints are the same "
                                       "(self-loop).")

        new_edge = UndirectedEdge(end1, end2, length)
        self._add_edge(new_edge=new_edge)

    def _add_edge(self, new_edge):
        end1, end2 = new_edge.end1, new_edge.end2
        end1.add_edge(new_edge)
        end2.add_edge(new_edge)
        self._edge_list.append(new_edge)

    def remove_edge(self, end1_id, end2_id):
        # Check whether the input endpoints both exist
        end1, end2 = self._find_vtx(end1_id), self._find_vtx(vtx_id=end2_id)
        if end1 is None or end2 is None:
            raise IllegalArgumentError("The endpoints don't both exist.")

        # Check whether the edge to remove exists
        edge_to_remove = end1.get_edge_with_neighbor(end2)
        if edge_to_remove is None:
            raise IllegalArgumentError("The edge to remove doesn't exist.")

        self._remove_edge(edge_to_remove=edge_to_remove)

    def _remove_edge(self, edge_to_remove):
        end1, end2 = edge_to_remove.end1, edge_to_remove.end2
        end1.remove_edge(edge_to_remove)
        end2.remove_edge(edge_to_remove)
        self._edge_list.remove(edge_to_remove)

    def floyd_warshall_apsp(self):
        n = len(self._vtx_list)
        # Initialization
        subproblems = [[[0] * n for i in range(n)] for k in range(n + 1)]
        for src_vtx in self._vtx_list:
            for dest_vtx in self._vtx_list:
                if src_vtx is not dest_vtx:
                    subproblems[0][src_vtx.vtx_id][dest_vtx.vtx_id] = \
                        super()._INFINITY
        for edge in self._edge_list:
            subproblems[0][edge.end1.vtx_id][edge.end2.vtx_id] = edge.length
            subproblems[0][edge.end2.vtx_id][edge.end1.vtx_id] = edge.length
        # Bottom-up calculation
        for k in range(1, n + 1):
            for src_vtx in self._vtx_list:
                for dest_vtx in self._vtx_list:
                    # Case 1: the internal nodes in P(k, s, d) does NOT contain
                    # node-k
                    path_length_without_kth_vtx = \
                        subproblems[k - 1][src_vtx.vtx_id][dest_vtx.vtx_id]
                    # Case 2: the internal nodes in P(k, s, d) contains node-k
                    # By dividing P(k, s, d) into two subpaths by node-k, we
                    # have
                    # P(k, s, d) = P1(k - 1, s, k) + P2(k - 1, k, d)
                    kth_vtx_id = k - 1
                    path_length_with_kth_vtx = \
                        subproblems[k - 1][src_vtx.vtx_id][kth_vtx_id] + \
                        subproblems[k - 1][kth_vtx_id][dest_vtx.vtx_id]
                    # P(k, s, d) is the smaller between the above two
                    # candidates.
                    subproblems[k][src_vtx.vtx_id][dest_vtx.vtx_id] = \
                        min(path_length_without_kth_vtx,
                            path_length_with_kth_vtx)
        # Extension to detect negative cycles:
        # If the input graph has negative cycles, then constructing the
        # recurrence will lead to at least one v in A[n, v, v] (the diagonal of
        # the final solution), s.t. A[n, v, v] < 0. Thus, we just need to check
        # A[n, v, v] (the diagonal of the final solution), and see if there is
        # a negative value.
        for vtx in self._vtx_list:
            if subproblems[n][vtx.vtx_id][vtx.vtx_id] < 0:
                raise IllegalArgumentError('The graph has negative cycles.')
        # The final solution lies in exactly subproblems[n, s, d].
        return subproblems[n]
        # Overall running time complexity: O(n^3)
        # Overall space complexity: O(n^3)

    def floyd_warshall_apsp_optimized(self):
        n = len(self._vtx_list)
        # Initialization
        prev_iter_subproblems, curr_iter_subproblems = \
            [[0] * n for i in range(n)], [[0] * n for i in range(n)]
        for src_vtx in self._vtx_list:
            for dest_vtx in self._vtx_list:
                if src_vtx is not dest_vtx:
                    prev_iter_subproblems[src_vtx.vtx_id][dest_vtx.vtx_id] = \
                        super()._INFINITY
        for edge in self._edge_list:
            prev_iter_subproblems[edge.end1.vtx_id][edge.end2.vtx_id] = \
                edge.length
            prev_iter_subproblems[edge.end2.vtx_id][edge.end1.vtx_id] = \
                edge.length
        # Bottom-up calculation
        for k in range(1, n + 1):
            for src_vtx in self._vtx_list:
                for dest_vtx in self._vtx_list:
                    path_length_without_kth_vtx = \
                        prev_iter_subproblems[src_vtx.vtx_id][dest_vtx.vtx_id]
                    kth_vtx_id = k - 1
                    path_length_with_kth_vtx = \
                        prev_iter_subproblems[src_vtx.vtx_id][kth_vtx_id] + \
                        prev_iter_subproblems[kth_vtx_id][dest_vtx.vtx_id]
                    curr_iter_subproblems[src_vtx.vtx_id][dest_vtx.vtx_id] = \
                        min(path_length_without_kth_vtx,
                            path_length_with_kth_vtx)
            prev_iter_subproblems = curr_iter_subproblems.copy()
        for vtx in self._vtx_list:
            if prev_iter_subproblems[vtx.vtx_id][vtx.vtx_id] < 0:
                raise IllegalArgumentError('The graph has negative cycles.')
        # The final solution lies in exactly prev_iter_subproblems.
        return prev_iter_subproblems
        # Overall running time complexity: O(n^3)
        # Overall space complexity: O(n^2)
