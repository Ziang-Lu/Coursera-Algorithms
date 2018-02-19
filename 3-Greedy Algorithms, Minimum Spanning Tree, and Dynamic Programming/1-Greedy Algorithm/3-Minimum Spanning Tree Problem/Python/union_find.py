#!usr/bin/env python3
# -*- coding: utf-8 -*-

"""
A simple implementation of UnionFind data structure.
Maintain a partition of a set of objects
"""

__author__ = 'Ziang Lu'


class IllegalArgumentError(ValueError):
    pass


class UnionFindObj(object):
    def __init__(self):
        self._leader = self

    @property
    def name(self):
        """
        Accessor of name.
        :return: str
        """
        pass

    @property
    def leader(self):
        """
        Accessor of leader.
        :return: UnionFindObj
        """
        return self._leader

    @leader.setter
    def leader(self, leader):
        """
        Mutator of leader.
        :param leader: UnionFindObj
        :return: None
        """
        self._leader = leader


class UnionFind(object):
    def __init__(self, objs):
        """
        Constructor with parameter.
        :param objs: list[UnionFindObj]
        """
        self._groups = {}
        for obj in objs:
            self._groups[obj.name] = [obj]

    def find(self, obj):
        """
        Returns the name of the group that the given object belongs to.
        :param obj: UnionFindObj
        :return: str
        """
        return obj.leader.name
        # Running time complexity: O(1)

    def union(self, group_name_a, group_name_b):
        # Check whether the input strings are null or empty
        if group_name_a is None or len(group_name_a) == 0 or \
                group_name_b is None or len(group_name_b) == 0:
            raise IllegalArgumentError('The input strings should not be None or'
                                       ' empty.')
        # Check whether the input group names exist
        if group_name_a not in self._groups or group_name_b not in self._groups:
            raise IllegalArgumentError("The input group names don't both "
                                       "exist.")

        group_a, group_b = \
            self._groups[group_name_a], self._groups[group_name_b]
        # In order to reduce the number of leader updates, let the smaller group
        # inherit the leader of the larger one.
        if len(group_a) >= len(group_b):
            larger, smaller = group_a, group_b
        else:
            larger, smaller = group_b, group_a
        larger_leader = larger[0].leader
        smaller_name = smaller[0].leader.name
        self._update_leader(group=smaller, new_leader=larger_leader)
        larger.extend(smaller)
        self._groups.pop(smaller_name)

    def _update_leader(self, group, new_leader):
        """
        Private helper function to update the leader of the given group to the
        given new leader.
        :param group: list[UnionFindObj]
        :param new_leader: UnionFindObj
        :return: None
        """
        for obj in group:
            obj.leader = new_leader
        # Running time complexity: O(1)
