#!usr/bin/env python3
# -*- coding: utf-8 -*-

"""
Adjacency list representation of directed graph.

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
        self._freq_of_emissive_neighbors = {}
        self._emissive_edges = []
        self._freq_of_incident_neighbors = {}
        self._incident_edges = []

    def get_emissive_edge_with_head(self, head):
        """
        Returns the first emissive edge with the given head.
        :param head: Vertex
        :return: Edge
        """
        # Check whether the input head is None
        if head is None:
            raise IllegalArgumentError('The input head should not be None.')

        for emissive_edge in self._emissive_edges:
            if emissive_edge.head is head:
                return emissive_edge
        # Not found
        return None

    @property
    def emissive_edges(self):
        """
        Accessor of emissive_edges.
        :return: list[Edge]
        """
        return self._emissive_edges

    def get_incident_edge_with_tail(self, tail):
        """
        Returns the first incident edge with the given tail.
        :param tail: Vertex
        :return: Edge
        """
        # Check whether the input tail is None
        if tail is None:
            raise IllegalArgumentError('The input tail should not be None.')

        for incident_edge in self._incident_edges:
            if incident_edge.tail is tail:
                return incident_edge
        # Not found
        return None

    @property
    def incident_edges(self):
        """
        Accessor of incident_edges.
        :return: list[Edge]
        """
        return self._incident_edges

    def add_emissive_edge(self, new_emissive_edge):
        """
        Adds the given emissive edge to this vertex.
        :param new_emissive_edge: Edge
        :return: None
        """
        # Check whether the input emissive edge is None
        if new_emissive_edge is None:
            raise IllegalArgumentError('The emissive edge to add should not be '
                                       'None.')
        # Check whether the input emissive edge involves this vertex as the tail
        if new_emissive_edge.tail is not self:
            raise IllegalArgumentError('The emissive edge to add should involve'
                                       ' this vertex as the tail.')

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
            raise IllegalArgumentError('The incident edge to add should not be '
                                       'None.')
        # Check whether the input incident edge involves this vertex as the head
        if new_incident_edge.head is not self:
            raise IllegalArgumentError('The incident edge to add should involve'
                                       ' this vertex as the head.')

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
            raise IllegalArgumentError('The emissive edge to remove should not '
                                       'be None.')
        # Check whether the input emissive edge involves this vertex as the tail
        if emissive_edge_to_remove.tail is not self:
            raise IllegalArgumentError('The emissive edge to remove should '
                                       'involve this vertex as the tail.')

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
            raise IllegalArgumentError('The incident edge to remove should not '
                                       'be None.')
        # Check whether the input incident edge involves this vertex as the head
        if incident_edge_to_remove.head is not self:
            raise IllegalArgumentError('The incident edge to remove should '
                                       'involve this vertex as the head.')

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


class DirectedEdge(AbstractEdge):
    def __init__(self, tail, head, length):
        """
        Constructor with parameter.
        :param tail: Vertex
        :param head: Vertex
        """
        super().__init__(length)
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

    @tail.setter
    def tail(self, tail):
        """
        Mutator of tail.
        :param tail: Vertex
        :return: None
        """
        self._tail = tail

    @head.setter
    def head(self, head):
        """
        Mutator of head.
        :param head: Vertex
        :return: None
        """
        self._head = head

    def __repr__(self):
        """
        String representation of this edge.
        :return head: str
        """
        return 'Edge from Vertex #%d to Vertex #%d' % \
            (self._tail.vtx_id, self._head.vtx_id)


class DirectedGraph(AbstractGraph):
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
        edges_to_remove = []
        edges_to_remove.extend(vtx_to_remove.emissive_edges)
        edges_to_remove.extend(vtx_to_remove.incident_edges)
        while len(edges_to_remove) > 0:
            edge_to_remove = edges_to_remove[0]
            self._remove_edge(edge_to_remove=edge_to_remove)
        # Remove the vertex
        self._vtx_list.remove(vtx_to_remove)

    def add_edge(self, tail_id, head_id, length):
        # Check whether the input endpoints both exist
        tail, head = self._find_vtx(tail_id), self._find_vtx(head_id)
        if tail is None or head is None:
            raise IllegalArgumentError("The endpoints don't both exist.")
        # Check whether the input vertices are the same
        if tail_id == head_id:
            raise IllegalArgumentError("The endpoints are the same "
                                       "(self-loop).")

        new_edge = DirectedEdge(tail, head, length)
        self._add_edge(new_edge=new_edge)

    def _add_edge(self, new_edge):
        tail, head = new_edge.tail, new_edge.head
        tail.add_emissive_edge(new_edge)
        head.add_incident_edge(new_edge)
        self._edge_list.append(new_edge)

    def remove_edge(self, tail_id, head_id):
        # Check whether the input endpoints both exist
        tail, head = self._find_vtx(tail_id), self._find_vtx(head_id)
        if tail is None or head is None:
            raise IllegalArgumentError("The endpoints don't both exist.")
        # Check whether the edge to remove exists
        edge_to_remove = tail.get_emissive_edge_with_head(head)
        if edge_to_remove is None:
            raise IllegalArgumentError("The edge to remove doesn't exist.")

        self._remove_edge(edge_to_remove=edge_to_remove)

    def _remove_edge(self, edge_to_remove):
        tail, head = edge_to_remove.tail, edge_to_remove.head
        tail.remove_emissive_edge(edge_to_remove)
        head.remove_incident_edge(edge_to_remove)
        self._edge_list.remove(edge_to_remove)

    def remove_directed_edges_between_pair(self, tail_id, head_id):
        """
        Removes all the directed edges between a vertex pair from this graph.
        :param tail_id: int
        :param head_id: int
        :return: None
        """
        try:
            while True:
                self.remove_edge(tail_id=tail_id, head_id=head_id)
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
                # Case 2: P(s, v, i) has i edges (i.e., P uses up all of its
                # budget i.), with final hop (w, v)
                for incident_edge in vtx.incident_edges:
                    w = incident_edge.tail
                    # By plucking off the final hop (w, v), we form
                    # P'(s, w, i - 1).
                    path_length = subproblems[w.vtx_id][budget - 1] + \
                        incident_edge.length
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
            for incident_edge in vtx.incident_edges:
                w = incident_edge.tail
                path_length = subproblems[w.vtx_id][n - 1] + \
                    incident_edge.length
                if path_length < min_path_length:
                    min_path_length = path_length
                    made_update_in_extra_iter = True
        if made_update_in_extra_iter:
            raise IllegalArgumentError('The graph has negative cycles reachable'
                                       ' from the source vertex.')
        # The final solution lies in exactly subproblems[v][n - 1].
        return self._reconstruct_shortest_paths(subproblems=subproblems)
        # Outer for-loop: n iterations
        # Inner for-loop: sum(in-degree(v)) = m
        # => Overall running time complexity: O(mn)
        # Overall space complexity: O(n^2)

    def _reconstruct_shortest_paths(self, subproblems):
        shortest_paths = []
        for vtx in self._vtx_list:
            shortest_path = [vtx.vtx_id]
            curr_vtx, budget = vtx, len(self._vtx_list) - 1
            while budget >= 1:
                # Find the previous vertex to backtrack
                prev_vtx = curr_vtx
                min_path_length = subproblems[curr_vtx.vtx_id][budget - 1]
                for incident_edge in curr_vtx.incident_edges:
                    w = incident_edge.tail
                    path_length = subproblems[w.vtx_id][budget - 1] + \
                        incident_edge.length
                    if path_length < min_path_length:
                        prev_vtx = w
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
        for vtx in self._vtx_list:
            if vtx is not src_vtx:
                prev_iter_subproblems[vtx.vtx_id] = super().INFINITY
        # In order to recover the ability to reconstruct the shortest paths, we
        # also keep track of the penultimate vertices in the previous outer
        # iteration.
        prev_iter_penultimate_vtxs, curr_iter_penultimate_vtxs = \
            [None] * n, [None] * n
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
                for incident_edge in vtx.incident_edges:
                    w = incident_edge.tail
                    path_length = prev_iter_subproblems[w.vtx_id] + \
                        incident_edge.length
                    if path_length < min_path_length:
                        min_path_length = path_length
                        made_update_in_iter = True
                        penultimate_vtx = w
                curr_iter_subproblems[vtx.vtx_id] = min_path_length
                curr_iter_penultimate_vtxs[vtx.vtx_id] = penultimate_vtx
            budget += 1
            prev_iter_subproblems = curr_iter_subproblems.copy()
            prev_iter_penultimate_vtxs = curr_iter_penultimate_vtxs.copy()
        made_update_in_iter = False
        for vtx in self._vtx_list:
            min_path_length = prev_iter_subproblems[vtx.vtx_id]
            for incident_edge in vtx.incident_edges:
                w = incident_edge.tail
                path_length = prev_iter_subproblems[w.vtx_id] + \
                    incident_edge.length
                if path_length < min_path_length:
                    min_path_length = path_length
                    made_update_in_iter = True
        if made_update_in_iter:
            raise IllegalArgumentError('The graph has negative cycles reachable'
                                       ' from the source vertex..')
        # The final solution lies in exactly prev_iter_subproblems.

        # We can reconstruct the shortest paths from these penultimate vertices.
        return self._reconstruct_shortest_paths_optimized(
            penultimate_vtxs=prev_iter_penultimate_vtxs)
        # Overall running time complexity: O(mn)
        # Overall space complexity: O(n)

    def bellman_ford_shortest_paths_dest_driven(self, dest_vtx_id):
        # Check whether the input destination vertex exists
        dest_vtx = self._find_vtx(dest_vtx_id)
        if dest_vtx is None:
            raise IllegalArgumentError("The destination vertex doesn't exist.")

        n = len(self._vtx_list)
        # Initialization
        subproblems = [[0] * n for i in range(n)]
        for vtx in self._vtx_list:
            if vtx is not dest_vtx:
                subproblems[vtx.vtx_id][0] = super().INFINITY
        # Bottom-up calculation
        for budget in range(1, n):
            for vtx in self._vtx_list:
                # Case 1: P(v, d, i) has <= (i - 1) edges (i.e., P doesn't use
                # all of its budget i.)
                min_path_length = subproblems[vtx.vtx_id][budget - 1]
                # Case 2: P(v, d, i) has exactly i edges (i.e., P uses up all of
                # its budget i.), with first hop (v, w)
                for emissive_edge in vtx.emissive_edges:
                    w = emissive_edge.head
                    # By plucking off the first hop (v, w), we form
                    # P'(w, d, i - 1).
                    path_length = subproblems[w.vtx_id][budget - 1] + \
                        emissive_edge.length
                    if path_length < min_path_length:
                        min_path_length = path_length
                # P(v, d, i) is the minimum among the above (1 + out-degree(v))
                # candidates.
                subproblems[vtx.vtx_id][budget] = min_path_length
        # Extension to detect negative cycles reachable from d:
        # If the input graph has negative cycles reachable from d, then we just
        # run the outer loop for one extra iteration, and check if there is
        # still an improvement on some vertex. If so, then the input graph has
        # negative cycles reachable from d.
        made_update_in_extra_iter = False
        for vtx in self._vtx_list:
            min_path_length = subproblems[vtx.vtx_id][n - 1]
            for emissive_edge in vtx.emissive_edges:
                w = emissive_edge.head
                path_length = subproblems[w.vtx_id][n - 1] + \
                    emissive_edge.length
                if path_length < min_path_length:
                    min_path_length = path_length
                    made_update_in_extra_iter = True
        if made_update_in_extra_iter:
            raise IllegalArgumentError('The graph has negative cycles reachable'
                                       ' from the destination vertex.')
        # The final solution lies in exactly subproblems[v][n - 1].
        return self._reconstruct_shortest_paths_dest_driven(
            subproblems=subproblems)
        # Outer for-loop: n iterations
        # Inner for-loop: sum(out-degree(v)) = m
        # Overall running time complexity: O(mn)
        # Overall space complexity: O(n^2)

    def _reconstruct_shortest_paths_dest_driven(self, subproblems):
        shortest_paths = []
        for vtx in self._vtx_list:
            shortest_path = [vtx.vtx_id]
            curr_vtx, budget = vtx, len(self._vtx_list) - 1
            while budget >= 1:
                # Find the next vertex
                next_vtx = curr_vtx
                min_path_length = subproblems[curr_vtx.vtx_id][budget - 1]
                for emissive_edge in curr_vtx.emissive_edges:
                    w = emissive_edge.head
                    path_length = subproblems[w.vtx_id][budget - 1] + \
                        emissive_edge.length
                    if path_length < min_path_length:
                        next_vtx = w
                        min_path_length = path_length
                if next_vtx is not curr_vtx:
                    shortest_path.append(next_vtx.vtx_id)
                curr_vtx = next_vtx
                budget -= 1
            shortest_paths.append(shortest_path)
        return shortest_paths
        # Running time complexity: O(mn)

    def bellman_ford_shortest_paths_dest_driven_optimized(self, dest_vtx_id):
        # Check whether the input destination vertex exists
        dest_vtx = self._find_vtx(dest_vtx_id)
        if dest_vtx is None:
            raise IllegalArgumentError("The destination vertex doesn't exist.")

        n = len(self._vtx_list)
        # Initialization
        # Space optimization: We only keep track of the subproblem solutions in
        # the previous outer iteration.
        prev_iter_subproblems, curr_iter_subproblems = [0] * n, [0] * n
        for vtx in self._vtx_list:
            if vtx is not dest_vtx:
                prev_iter_subproblems[vtx.vtx_id] = super().INFINITY
        # In order to recover the ability to reconstruct the shortest paths, we
        # also keep track of the next vertices in the previous outer iteration.
        prev_iter_next_vtxs, curr_iter_next_vtxs = [None] * n, [None] * n
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
                next_vtx = prev_iter_next_vtxs[vtx.vtx_id]
                for emissive_edge in vtx.emissive_edges:
                    w = emissive_edge.head
                    path_length = prev_iter_subproblems[w.vtx_id] + \
                        emissive_edge.length
                    if path_length < min_path_length:
                        min_path_length = path_length
                        made_update_in_iter = True
                        next_vtx = w
                curr_iter_subproblems[vtx.vtx_id] = min_path_length
                curr_iter_next_vtxs[vtx.vtx_id] = next_vtx
            budget += 1
            prev_iter_subproblems = curr_iter_subproblems.copy()
            prev_iter_next_vtxs = curr_iter_next_vtxs.copy()
        made_update_in_iter = False
        for vtx in self._vtx_list:
            min_path_length = prev_iter_subproblems[vtx.vtx_id]
            for emissive_edge in vtx.emissive_edges:
                w = emissive_edge.head
                path_length = prev_iter_subproblems[w.vtx_id] + \
                    emissive_edge.length
                if path_length < min_path_length:
                    path_length = min_path_length
                    made_update_in_iter = True
            curr_iter_subproblems[vtx.vtx_id] = min_path_length
        if made_update_in_iter:
            raise IllegalArgumentError('The graph has negative cycles reachable'
                                       ' from the destination vertex.')
        # The final solution lies in exactly in prev_iter_subproblems.
        return self._reconstruct_shortest_paths_dest_driven_optimized(
            next_vtxs=prev_iter_next_vtxs)
        # Overall running time complexity: O(mn)
        # Overall space complexity: O(n)

    def shortest_paths_dest_driven_push_based(self, dest_vtx_id):
        # Check whether the input destination vertex exists
        dest_vtx = self._find_vtx(dest_vtx_id)
        if dest_vtx is None:
            raise IllegalArgumentError("The destination vertex doesn't exist.")

        n = len(self._vtx_list)
        # Initialization
        min_path_lengths, next_vtxs = [0] * n, [None] * n
        for vtx in self._vtx_list:
            if vtx is not dest_vtx:
                min_path_lengths[vtx.vtx_id] = super().INFINITY
        # Start the notifications from the destination vertex
        for incident_edge in dest_vtx.incident_edges:
            self._notify_tail(incident_edge, min_path_lengths=min_path_lengths,
                              next_vtxs=next_vtxs)
        return self._reconstruct_shortest_paths_dest_driven_optimized(
            next_vtxs=next_vtxs)
        # Overall running time complexity: O(2^n)

    def _notify_tail(self, edge, min_path_lengths, next_vtxs):
        """
        Private helper function to notify the tail of the given edge with an
        updated minimum path length of the head of the given edge recursively.
        :param edge: DirectedEdge
        :param min_path_lengths: list[int]
        :param next_vtxs: list[Vertex]
        :return: None
        """
        next_vtx, curr_vtx = edge.head, edge.tail
        updated_path_length = min_path_lengths[next_vtx.vtx_id]
        new_path_length = updated_path_length + edge.length
        if new_path_length < min_path_lengths[curr_vtx.vtx_id]:
            min_path_lengths[curr_vtx.vtx_id] = new_path_length
            next_vtxs[curr_vtx.vtx_id] = next_vtx
            # Notify all incident neighbors
            for incident_edge in curr_vtx.incident_edges:
                self._notify_tail(incident_edge,
                                  min_path_lengths=min_path_lengths,
                                  next_vtxs=next_vtxs)
