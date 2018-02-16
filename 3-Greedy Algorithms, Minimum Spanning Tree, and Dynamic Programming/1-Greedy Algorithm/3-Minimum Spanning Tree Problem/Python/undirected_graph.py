#!usr/bin/env python3
# -*- coding: utf-8 -*-

"""
Adjacency list representation of undirected graph.

Note that parallel edges and self-loops are not allowed.
"""

__author__ = 'Ziang Lu'

import heapq
import random
import sys
from functools import total_ordering
from graph_basics import AbstractVertex, AbstractGraph


class IllegalArgumentError(ValueError):
    pass


@total_ordering
class Vertex(AbstractVertex):
    DEFAULT_MIN_INCIDENT_COST = sys.maxsize

    def __init__(self, vtx_id):
        """
        Constructor with parameter.
        :param vtx_id: int
        """
        super().__init__(vtx_id)
        self._freq_of_neighbors = {}
        self._edges = []
        self._min_cost_incident_edge = None
        self._min_incident_cost = Vertex.DEFAULT_MIN_INCIDENT_COST

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

    @property
    def min_cost_incident_edge(self):
        """
        Accessor of min_cost_incident_edge.
        :return: UndirectedEdge
        """
        return self._min_cost_incident_edge

    @property
    def min_incident_cost(self):
        """
        Accessor of min_incident_edge.
        :return: float
        """
        return self._min_incident_cost

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

    @min_cost_incident_edge.setter
    def min_cost_incident_edge(self, min_cost_incident_edge):
        """
        Mutator of min_cost_incident_edge
        :param min_cost_incident_edge: UndirectedEdge
        :return: None
        """
        self._min_cost_incident_edge = min_cost_incident_edge

    @min_incident_cost.setter
    def min_incident_cost(self, min_incident_cost):
        """
        Mutator of min_incident_cost.
        :param min_incident_cost: float
        :return: None
        """
        self._min_incident_cost = min_incident_cost

    def __lt__(self, other):
        return self._min_incident_cost < other.min_incident_cost

    def __repr__(self):
        """
        String representation of this vertex.
        :return: str
        """
        return 'Vertex #%d, Its neighbors and frequencies: %s' % \
            (self._vtx_id, self._freq_of_neighbors)


@total_ordering
class UndirectedEdge(object):
    def __init__(self, end1, end2, cost):
        """
        Constructor with parameter.
        :param end1: Vertex
        :param end2: Vertex
        :param cost: float
        """
        self._end1 = end1
        self._end2 = end2
        self._cost = cost

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

    @property
    def cost(self):
        """
        Accessor of cost.
        :return: float
        """
        return self._cost

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

    def __lt__(self, other):
        return self._cost < other.cost

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

    def add_edge(self, end1_id, end2_id, cost):
        # Check whether the input endpoints both exist
        end1, end2 = self._find_vtx(end1_id), self._find_vtx(end2_id)
        if end1 is None or end2 is None:
            raise IllegalArgumentError("The endpoints don't both exist.")
        # Check whether the input endpoints are the same (self-loop)
        if end1_id == end2_id:
            raise IllegalArgumentError("The endpoints are the same "
                                       "(self-loop).")

        new_edge = UndirectedEdge(end1, end2, cost)
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

    def prim_mst_straightforward(self):
        """
        Finds the minimum spanning tree (MST) in this graph using
        straightforward Prim's MST Algorithm.
        :return: float
        """
        # 1. Arbitrarily choose a source vertex s
        src_vtx = self._vtx_list[random.randint(0, len(self._vtx_list) - 1)]

        # 2. Initialize X = {s}, which contains the vertices we've spanned so
        #    far, and T = {empty}, which current spanning tree
        spanned = set()
        spanned.add(src_vtx.vtx_id)
        curr_spanning_tree = []

        # 3. Create a heap containing all the edge with one endpoint in X (the
        #    set) and the other in (V-X)
        crossing_edges = []
        for edge in src_vtx.edges:
            heapq.heappush(crossing_edges, edge)

        # 4. While X != V
        while len(spanned) < len(self._vtx_list):
            # Among all crossing edges e = (v, w) with v in X (the set) and w in
            # (V-X), pick up the cheapest crossing edge
            cheapest_crossing_edge = heapq.heappop(crossing_edges)
            # Add e to T
            curr_spanning_tree.append(cheapest_crossing_edge)
            # Add w to X (the set)
            if cheapest_crossing_edge.end1.vtx_id in spanned:  # endpoint2 is the w.
                w = cheapest_crossing_edge.end2
            else:  # endpoint1 is the w.
                w = cheapest_crossing_edge.end1
            spanned.add(w.vtx_id)

            # Update the crossing edges with w's edges if necessary
            for w_edge in w.edges:
                # Find the neighbor
                if w_edge.end1.vtx_id == w.vtx_id:  # endpoint2 is the neighbor.
                    neighbor = w_edge.end2
                else:  # endpoint1 is the neighbor.
                    neighbor = w_edge.end1
                # Check whether the neighbor of w has been spanned
                if neighbor.vtx_id not in spanned:
                    heapq.heappush(crossing_edges, w_edge)

        return sum(map(lambda edge: edge.cost, curr_spanning_tree))
        # Overall running time complexity: O((m + n)log m)
        # Since usually m >= n, it could be simplified to O(mlog m).
