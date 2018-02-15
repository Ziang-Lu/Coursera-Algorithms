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
        self._explored = False
        self._layer = 0

    @property
    def vtx_id(self):
        """
        Accessor of vtx_id.
        :return: int
        """
        return self._vtx_id

    @property
    def explored(self):
        """
        Accessor of explored.
        :return: bool
        """
        return self._explored

    @property
    def layer(self):
        """
        Accessor of layer.
        :return: int
        """
        return self._layer

    def set_as_explored(self):
        """
        Sets this vertex to explored.
        :return: None
        """
        self._explored = True

    def set_as_unexplored(self):
        """
        Sets this vertex to unexplored.
        :return: None
        """
        self._explored = False

    @layer.setter
    def layer(self, layer):
        """
        Mutator of layer.
        :param layer: int
        :return: None
        """
        self._layer = layer


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

    def bfs(self, src_vtx_id):
        """
        Finds all the findable vertices starting from the given source vertex
        using BFS.
        :param src_vtx_id: int
        :return: list[int]
        """
        pass

    def clear_explored(self):
        """
        Sets all the vertices to unexplored.
        :return: None
        """
        for vtx in self._vtx_list:
            vtx.set_as_unexplored()

    def shortest_path(self, src_vtx_id, dest_vtx_id):
        """
        Finds the length of the shortest path from the given source vertex to
        the given destination vertex.
        :param src_vtx_id: int
        :param dest_vtx_id: int
        :return: int
        """
        pass

    def num_of_connected_components_with_bfs(self):
        """
        Returns the number of connected components of this graph using BFS.
        :return: int
        """
        pass

    # Iterative implementation of DFS ignored (simply replacing the queue with a
    # stack in BFS)

    def dfs(self, src_vtx_id):
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

    def _dfs_helper(self, vtx, findable_vtx_ids):
        """
        Private helper function to do DFS and find all the findable vertices
        from the given vertex recursively.
        :param vtx: Vertex
        :param findable_vtx_ids: list[int]
        :return: None
        """
        pass

    def num_of_connected_components_with_dfs(self):
        """
        Returns the number of connected components of this graph using DFS.
        :return: int
        """
        pass
