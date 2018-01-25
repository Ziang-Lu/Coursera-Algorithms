#!usr/bin/env python3
# -*- coding: utf-8 -*-

"""
Adjacency list representation of directed graph.

Note that parallel edges are allowed, but not self-loops.
"""

__author__ = 'Ziang Lu'

from queue import Queue
from graph_basics import AbstractVertex, AbstractGraph


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


class DirectedEdge(object):
    def __init__(self, tail, head):
        """
        Constructor with parameter.
        :param tail: Vertex
        :param head: Vertex
        """
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

    def add_edge(self, tail_id, head_id):
        # Check whether the input endpoints both exist
        tail, head = self._find_vtx(tail_id), self._find_vtx(head_id)
        if tail is None or head is None:
            raise IllegalArgumentError("The endpoints don't both exist.")
        # Check whether the input vertices are the same
        if tail_id == head_id:
            raise IllegalArgumentError("The endpoints are the same "
                                       "(self-loop).")

        new_edge = DirectedEdge(tail, head)
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

    def bfs(self, src_vtx_id):
        # Check whether the input source vertex exists
        src_vtx = self._find_vtx(src_vtx_id)
        if src_vtx is None:
            raise IllegalArgumentError("The input source vertex doesn't exist.")

        # 1. Initialize G as s explored and other vertices unexplored
        src_vtx.set_as_explored()
        # 2. Let Q be the queue of vertices initialized with s
        queue = Queue()
        queue.put(src_vtx)

        findable_vtx_ids = [src_vtx_id]

        # 3. While Q is not empty
        while not queue.empty():
            # (1) Take out the first vertex v
            vtx = queue.get()
            # (2) For every directed edge (v, w)
            for edge in vtx.emissive_edges:
                w = edge.head
                # If w is unexplored
                if not w.explored:
                    # Mark w as explored
                    w.set_as_explored()

                    findable_vtx_ids.append(w.vtx_id)

                    # Push w to Q
                    queue.put(w)
        return findable_vtx_ids

    def shortest_path(self, src_vtx_id, dest_vtx_id):
        # Check whether the input source and destination vertices both exist
        src_vtx, dest_vtx = self._find_vtx(src_vtx_id), \
            self._find_vtx(dest_vtx_id)
        if src_vtx is None:
            raise IllegalArgumentError("The input source and destination "
                                       "vertices don't both exist.")

        # 1. Initialize G as s explored and other vertices unexplored
        src_vtx.set_as_explored()
        # 2. Let Q be the queue of vertices initialized with s
        queue = Queue()
        queue.put(src_vtx)
        # 3. While Q is not empty
        while not queue.empty():
            # (1) Take out the first vertex v
            vtx = queue.get()
            # (2) For every directed edge (v, w)
            for edge in vtx.emissive_edges:
                w = edge.head
                # If w is unexplored
                if not w.explored:
                    # Mark w as explored
                    w.set_as_explored()

                    w.layer = vtx.layer + 1
                    if w is dest_vtx:
                        return dest_vtx.layer

                    # Push w to Q
                    queue.put(w)
        # The destination vertex is not findable starting from the given source
        # vertex along directed edges.
        return -1

    def num_of_connected_components_with_bfs(self):
        # Directed connectivity
        # Not implemented
        return 0

    def _dfs_helper(self, vtx, findable_vtx_ids):
        # For every directed edge (v, w)
        for edge in vtx.emissive_edges:
            w = edge.head
            # If w is unexplored
            # (This itself serves as a base case: all the w's of s are
            # explored.)
            if not w.explored:
                # Mark w as explored
                w.set_as_explored()

                findable_vtx_ids.append(w.vtx_id)

                # Do DFS on (G, w)   (Recursion)
                self._dfs_helper(vtx=w, findable_vtx_ids=findable_vtx_ids)

    def num_of_connected_components_with_dfs(self):
        # Directed connectivity
        # Strong Connected Components (SCC):
        # A graph is strongly connected iff you can get from any vertex to any
        # other vertex through directed edges.
        # => An SCC of a directed graph is a part of the graph that is strongly
        #    connected.

        # 1. Run DFS-loop on G   [First pass]
        vtxs_sorted_by_finish_time = self._get_vtxs_sorted_by_finish_time()
        # Essentially, the vertices sorted by this finishing time is exactly
        # the reversed topological ordering.

        # 2. Clear explored and get ready for the second pass
        self.clear_explored()

        # 3. Run DFS-loop on G   [Second pass]
        count = 0
        # For every vertex v from finishing time 1 to n
        # (i.e., reverse topological ordering)
        for vtx in vtxs_sorted_by_finish_time:
            # If v is unexplored (i.e, not explored from some previous DFS)
            if not vtx.explored:
                # Mark v as explored
                vtx.set_as_explored()
                count += 1
                # Do DFS towards v (Discovers precisely v's SCC)
                self.dfs(src_vtx_id=vtx.vtx_id)
        return count

    def _get_vtxs_sorted_by_finish_time(self):
        """
        Private helper function to get the vertices sorted by finishing time
        when using DFS
        :return: list[Vertex]
        """
        vtxs_sorted_by_finish_time = []
        # For every vertex v
        for vtx in self._vtx_list:
            # If v is unexplored
            if not vtx.explored:
                # Mark v as explored
                vtx.set_as_explored()
                # Do DFS towards v
                self._dfs_helper_with_finish_time(vtx=vtx,
                                                  vtxs_sorted_by_finish_time=
                                                  vtxs_sorted_by_finish_time)
        return vtxs_sorted_by_finish_time

    def _dfs_helper_with_finish_time(self, vtx, vtxs_sorted_by_finish_time):
        """
        Helper function to do DFS and set the finishing time of the given
        vertex.
        :param vtx: Vertex
        :param vtxs_sorted_by_finish_time: list[Vertex]
        :return: None
        """
        # For every edge (v, w)
        for edge in vtx.emissive_edges:
            w = edge.head
            # If w is unexplored
            if not w.explored:
                # Mark w as explored
                w.set_as_explored()
                # Do DFS towards w
                self._dfs_helper_with_finish_time(vtx=w,
                                                  vtxs_sorted_by_finish_time=
                                                  vtxs_sorted_by_finish_time)
        # Set v's finishing time
        vtxs_sorted_by_finish_time.append(vtx)

    def topological_sort(self):
        """
        Returns the topological ordering of the vertices of this directed graph
        using DFS.
        :return: list[int]
        """
        vtxs_sorted_by_finish_time = self._get_vtxs_sorted_by_finish_time()

        return list(reversed(list(map(lambda vtx: vtx.vtx_id,
                                      vtxs_sorted_by_finish_time))))

    def topological_sort_straightforward(self):
        """
        Returns the topological ordering of the vertices of this directed graph
        using straightforward algorithm.
        :return: list[int]
        """
        topological_ordering = [0] * len(self._vtx_list)
        self._topological_sort_straightforward_helper(topological_ordering,
                                                      curr_order=
                                                      len(self._vtx_list))
        return topological_ordering

    def _topological_sort_straightforward_helper(self, topological_ordering,
                                                 curr_order):
        """
        Private helper function to fill in the given order of the topological
        ordering using straightforward algorithm recursively.
        :param topological_ordering: list[int]
        :param curr_order: int
        :return: None
        """
        # Base case 1: Finished ordering
        if curr_order == 0:
            return
        sink_vtx = self._get_sink_vertex()
        # Base case 2: No more sink vertices
        if sink_vtx is None:
            return

        # Recursive case
        topological_ordering[curr_order - 1] = sink_vtx.vtx_id
        self._remove_vtx(sink_vtx)
        self._topological_sort_straightforward_helper(topological_ordering,
                                                      curr_order=curr_order - 1)

    def _get_sink_vertex(self):
        """
        Helper function to get a sink vertex.
        :return: Vertex
        """
        for vtx in self._vtx_list:
            if not vtx.emissive_edges:
                return vtx
        return None
