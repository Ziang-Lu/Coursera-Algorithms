#!usr/bin/env python3
# -*- coding: utf-8 -*-

"""
Adjacency list representation of undirected graph.

Note that parallel edges are allowed, but not self-loops.
"""

__author__ = 'Ziang Lu'

from graph_basics import AbstractVertex, AbstractEdge, AbstractGraph


class IllegalArgumentError(ValueError):
    pass


class Vertex(AbstractVertex):
    def __init__(self, vtx_id):
        """
        Constructor with parameter.
        :param vtx_id: int
        """
        super().__init__(vtx_id)
        self._freq_of_neighbors = {}
        self._edges = []

    def get_edge_with_neighbor(self, neighbor):
        """
        Returns the first edge with the given neighbor.
        :param neighbor: Vertex
        :return: Edge
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
    def edges(self):
        """
        Accessor of edges.
        :return: list[Edge]
        """
        return self._edges

    def add_edge(self, new_edge):
        """
        Adds the given edge to this vertex.
        :param new_edge: Edge
        :return: None
        """
        # Check whether the input edge is None
        if new_edge is None:
            raise IllegalArgumentError('The edge to add should not be None.')
        # Check whether the input edge involves this vertex
        if (new_edge.end1 is not self) and (new_edge.end2 is not self):
            raise IllegalArgumentError('The edge to add should involve this '
                                       'vertex.')

        self._edges.append(new_edge)

        # Find the neighbor associated with the input edge
        if new_edge.end1 is self:  # endpoint2 is the neighbor.
            neighbor = new_edge.end2
        else:  # endpoint1 is the neighbor.
            neighbor = new_edge.end1
        # Update the frequency of the neighbor
        freq = self._freq_of_neighbors.get(neighbor.vtx_id, 0)
        freq += 1
        self._freq_of_neighbors[neighbor.vtx_id] = freq

    def remove_edge(self, edge_to_remove):
        """
        Removes the given edge from this vertex.
        :param edge_to_remove: Edge
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

        self._edges.remove(edge_to_remove)

        # Find the neighbor associated with the input edge
        if edge_to_remove.end1 is self:  # endpoint2 is the neighbor.
            neighbor = edge_to_remove.end2
        else:  # endpoint1 is the neighbor.
            neighbor = edge_to_remove.end1
        # Update the frequency of the neighbor
        freq = self._freq_of_neighbors.get(neighbor.vtx_id)
        if freq == 1:
            self._freq_of_neighbors.pop(neighbor.vtx_id)
        else:
            freq -= 1
            self._freq_of_neighbors[neighbor.vtx_id] = freq

    def __repr__(self):
        """
        String representation of this vertex.
        :return: str
        """
        return 'Vertex #%d, Its neighbors and frequencies: %s' % \
            (self._vtx_id, self._freq_of_neighbors)


class UndirectedEdge(AbstractEdge):
    def __init__(self, end1, end2, length):
        """
        Constructor with parameter.
        :param end1: Vertex
        :param end2: Vertex
        """
        super().__init__(length)
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

    @end1.setter
    def end1(self, end1):
        """
        Mutator of end1.
        :param end1: Vertex
        :return: None
        """
        self._end1 = end1

    @end2.setter
    def end2(self, end2):
        """
        Mutator of end2.
        :param end2: Vertex
        :return: None
        """
        self._end2 = end2

    def __repr__(self):
        """
        String representation of this edge.
        :return: str
        """
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

    def remove_edges_between_pair(self, end1_id, end2_id):
        """
        Removes all the edges between a vertex pair from this graph.
        :param end1_id: int
        :param end2_id: int
        :return: None
        """
        try:
            while True:
                self.remove_edge(end1_id=end1_id, end2_id=end2_id)
        except IllegalArgumentError:
            pass

    def bellman_ford_shortest_paths(self, src_vtx_id):
        # Check whether the input source vertex exists
        src_vtx = self._find_vtx(src_vtx_id)
        if src_vtx is None:
            raise IllegalArgumentError("The source vertex doesn't exist.")

        n = len(self._vtx_list)
        # Initialization
        subproblems = [[0] * n for i in range(n)]
        for vtx in self._vtx_list:
            if vtx is not src_vtx:
                subproblems[vtx.vtx_id][0] = super().INFINITY
        # Bottom-up calculation
        for budget in range(1, n):
            for vtx in self._vtx_list:
                # Case 1: P(s, v, i) has <= (i - 1) edges (i.e., P doesn't use
                # up all of its budget i.)
                min_path_length = subproblems[vtx.vtx_id][budget - 1]
                # Case 2: P(s, v, i) has i edges (i.e., P doesn't use up all of
                # its budget i.),
                for edge in vtx.edges:
                    # Find the neighbor
                    if edge.end1 is vtx:  # endpoint2 is the neighbor.
                        neighbor = edge.end2
                    else:  # endpoint1 is the neighbor.
                        neighbor = edge.end1
                    # By plucking off the final hop (w, v), we form
                    # P(s, w, i - 1).
                    path_length = subproblems[neighbor.vtx_id][budget - 1] + \
                        edge.length
                    if path_length < min_path_length:
                        min_path_length = path_length
                # P(s, v, i) is the minimum among the above (1 + in-degree(v))
                # candidates.
                subproblems[vtx.vtx_id][budget] = min_path_length
        # Extension to detect negative cycles reachable from s:
        # If the input graph has negative cycles reachable from s, then we just
        # run the outer loop for one extra iteration, and check if there is
        # still an improvement on some vertex. If so, then the input graph has
        # negative cycles reachable from s.
        made_update_in_extra_iter = False
        for vtx in self._vtx_list:
            min_path_length = subproblems[vtx.vtx_id][n - 1]
            for edge in vtx.edges:
                # Find the neighbor
                if edge.end1 is vtx:  # endpoint2 is the neighbor.
                    neighbor = edge.end2
                else:  # endpoint1 is the neighbor.
                    neighbor = edge.end1
                path_length = subproblems[neighbor.vtx_id][n - 1] + edge.length
                if path_length < min_path_length:
                    min_path_length = path_length
                    made_update_in_extra_iter = True
        if made_update_in_extra_iter:
            raise IllegalArgumentError('The graph has negative cycles reachable'
                                       ' from the source vertex.')
        # The final solution lies in exactly subproblems[v][n - 1].
        return self._reconstruct_shortest_paths(src_vtx_id,
                                                subproblems=subproblems)
        # Outer for-loop: n iterations
        # Inner for-loop: sum(in-degree(v)) = m
        # => Overall running time complexity: O(mn)
        # Overall space complexity: O(n^2)

    def _reconstruct_shortest_paths(self, src_vtx_id, subproblems):
        shortest_paths = []
        for vtx in self._vtx_list:
            shortest_path = [vtx.vtx_id]
            curr_vtx, budget = vtx, len(self._vtx_list) - 1
            while budget >= 1:
                # Find the previous vertex to backtrack
                prev_vtx = curr_vtx
                min_path_length = subproblems[curr_vtx.vtx_id][budget - 1]
                for edge in curr_vtx.edges:
                    # Find the neighbor
                    if edge.end1 is vtx:  # endpoint2 is the neighbor.
                        neighbor = edge.end2
                    else:  # endpoint1 is the neighbor.
                        neighbor = edge.end1
                    path_length = subproblems[neighbor.vtx_id][budget - 1] + \
                        edge.length
                    if path_length < min_path_length:
                        prev_vtx = neighbor
                        min_path_length = path_length
                if prev_vtx is not curr_vtx:
                    shortest_path.insert(0, prev_vtx.vtx_id)
                curr_vtx = prev_vtx
                budget -= 1
            shortest_paths.append(shortest_path)
        return shortest_paths
        # Running time complexity: O(mn)

    def bellman_ford_shortest_paths_optimized(self, src_vtx_id):
        # Check whether the input source vertex exists
        src_vtx = self._find_vtx(src_vtx_id)
        if src_vtx is None:
            raise IllegalArgumentError("The source vertex doesn't exist.")

        n = len(self._vtx_list)
        # Initialization
        # Space optimization: We only keep track of the subproblem solutions in
        # the previous outer iteration.
        prev_iter_subproblems, curr_iter_subproblems = [0] * n, [0] * n
        # In order to recover the ability to reconstruct the shortest paths, we
        # also keep track of the penultimate vertices in the previous outer
        # iteration.
        prev_iter_penultimate_vtxs, curr_iter_penultimate_vtxs = \
            [None] * n, [None] * n
        for vtx in self._vtx_list:
            if vtx is not src_vtx:
                prev_iter_subproblems[vtx.vtx_id] = super().INFINITY
        # Bottom-up calculation
        budget = 1
        # Optimization: Early-stopping
        # The algorithm may stop early when in the current iteration, no update
        # is made for any vertex.
        made_update_in_iter = True
        while budget <= n - 1 and made_update_in_iter:
            made_update_in_iter = False
            for vtx in self._vtx_list:
                min_path_length = prev_iter_subproblems[vtx.vtx_id]
                penultimate_vtx = prev_iter_penultimate_vtxs[vtx.vtx_id]
                for edge in vtx.edges:
                    # Find the neighbor
                    if edge.end1 is vtx:  # endpoint2 is the neighbor.
                        neighbor = edge.end2
                    else:  # endpoint1 is the neighbor.
                        neighbor = edge.end1
                    path_length = prev_iter_subproblems[neighbor.vtx_id] + \
                        edge.length
                    if path_length < min_path_length:
                        min_path_length = path_length
                        made_update_in_iter = True
                        penultimate_vtx = neighbor
                curr_iter_subproblems[vtx.vtx_id] = min_path_length
                curr_iter_penultimate_vtxs[vtx.vtx_id] = penultimate_vtx
            budget += 1
            prev_iter_subproblems = curr_iter_subproblems.copy()
            prev_iter_penultimate_vtxs = curr_iter_penultimate_vtxs.copy()
        made_update_in_iter = False
        for vtx in self._vtx_list:
            min_path_length = prev_iter_subproblems[vtx.vtx_id]
            for edge in vtx.edges:
                # Find the neighbor
                if edge.end1 is vtx:  # endpoint2 is the neighbor.
                    neighbor = edge.end2
                else:  # endpoint1 is the neighbor.
                    neighbor = edge.end1
                path_length = prev_iter_subproblems[neighbor.vtx_id] + \
                              edge.length
                if path_length < min_path_length:
                    min_path_length = path_length
                    made_update_in_iter = True
        if made_update_in_iter:
            raise IllegalArgumentError('The graph has negative cycles reachable'
                                       ' from the source vertex.')
        # The final solution lies in exactly prev_iter_subproblems.

        # We can reconstruct the shortest paths from these penultimate vertices.
        return self._reconstruct_shortest_paths_optimized(
            penultimate_vtxs=prev_iter_penultimate_vtxs)
        # Overall running time complexity: O(mn)
        # Overall space complexity: O(n)
