#!usr/bin/env python3
# -*- coding: utf-8 -*-

"""
Invariants of a Red-Black Tree:
1. Each node is either red or black.
2. The root is always black.
3. Never allow two red nodes in a row
4. Every root-null path passes exact the same number of black nodes.
   i.e., The number of black nodes that a root-null path passes cannot depend on
         the path.
"""

__author__ = 'Ziang Lu'


class Node(object):
    def __init__(self, key, color):
        """
        Constructor with parameter.
        :param key: int
        :param color: bool
        """
        self._key = key
        self._left = None
        self._right = None
        self._color = color


class RedBlackTree(object):
    RED = True
    BLACK = False

    def __init__(self):
        """
        Default constructor.
        """
        self._root = None

    # Insertion:
    # 1. Insert the new element as a normal BST (Might break invariants)
    # 2. Recolor and perform rotations until invariants are restored.
