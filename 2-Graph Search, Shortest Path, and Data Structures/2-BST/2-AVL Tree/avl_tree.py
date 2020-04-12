#!usr/bin/env python3
# -*- coding: utf-8 -*-

"""
A very simple self-balancing AVL-Tree implementation.

Invariant of an AVL-Tree:
The heights of the left and right subtrees of any node won't differ more than 1.
"""

__author__ = 'Ziang Lu'

from typing import List


class Node(object):
    __slots__ = ['_key', '_left', '_right', '_height']

    def __init__(self, key: int):
        """
        Constructor with parameter.
        :param key: int
        """
        self._key = key
        self._left = None
        self._right = None
        self._height = 1

    @property
    def key(self) -> int:
        """
        Accessor of key.
        :return: int
        """
        return self._key

    @property
    def left(self):
        """
        Accessor of left.
        :return: Node
        """
        return self._left

    @property
    def right(self):
        """
        Accessor of right.
        :return: Node
        """
        return self._right

    @property
    def height(self) -> int:
        """
        Accessor of height.
        :return: int
        """
        return self._height

    @left.setter
    def left(self, left) -> None:
        """
        Mutator of left.
        :param left: Node
        :return: None
        """
        self._left = left

    @right.setter
    def right(self, right) -> None:
        """
        Mutator of right.
        :param right: Node
        :return: None
        """
        self._right = right

    def update_height(self) -> None:
        """
        Updates the height of this node.
        :return: None
        """
        left_height, right_height = 0, 0
        if self._left:
            left_height = self._left.height
        if self._right:
            right_height = self._right.height
        self._height = 1 + max(left_height, right_height)


class AVLTree(object):
    __slots__ = ['_root']

    @staticmethod
    def _right_rotate(unbalanced: Node) -> Node:
        """
        Makes a right rotation towards the given unbalanced node.
        :param unbalanced: Node
        :return: Node
        """
        # Temporarily store the left child
        tmp = unbalanced.left

        # Reconnect the references to realize right rotation
        unbalanced.left = unbalanced.left.right
        tmp.right = unbalanced

        return tmp
        # Running time complexity: O(1)

    @staticmethod
    def _left_rotate(unbalanced: Node) -> Node:
        """
        Makes a left rotation towards the given unbalanced node.
        :param unbalanced: Node
        :return: Node
        """
        # Temporarily store the right child
        tmp = unbalanced.right

        # Reconnect the references to realize left rotation
        unbalanced.right = unbalanced.right.left
        tmp.left = unbalanced

        return tmp
        # Running time complexity: O(1)

    def __init__(self):
        """
        Default constructor.
        """
        self._root = None

    def search(self, key: int) -> bool:
        """
        Searches for the given key in this BST.
        :param key: int
        :return: bool
        """
        return self._search_helper(key, self._root)

    def _search_helper(self, key: int, curr: Node) -> bool:
        """
        Private helper function to search for the given key in the given subtree
        recursively.
        :param key: int
        :param curr: Node
        :return: bool
        """
        # Base case 1: Not found
        if not curr:
            return False
        # Base case 2: Found it
        if curr.key == key:
            return True

        # Recursive case
        if curr.key > key:
            return self._search_helper(key, curr.left)
        else:
            return self._search_helper(key, curr.right)
        # T(n) = T(n/2) + O(1)
        # a = 1, b = 2, d = 0
        # According to Master Method, time: O(log n)

    def insert(self, key: int) -> None:
        """
        Inserts the given key to the BST.
        :param key: int
        :return: None
        """
        if not self._root:
            self._root = Node(key)
            return
        self._root = self._insert_helper(key, [], self._root, is_lc=True)

    def _insert_helper(self, key: int, path: List[Node], curr: Node,
                       is_lc: bool) -> Node:
        """
        Private helper function to insert the given key to the given subtree
        recursively.
        :param key: int
        :param parent: list[Node]
        :param curr: Node
        :param is_lc: bool
        :return: Node
        """
        # Base case 1: Found the spot to insert
        if not curr:
            parent = path[-1]
            new_node = Node(key)
            if is_lc:
                parent.left = new_node
            else:
                parent.right = new_node
            self._update_path_height(path)
            return new_node
        # Base case 2: Found it
        if curr.key == key:
            # No duplicates allowed
            return curr

        # Recursive case
        path.append(curr)
        if curr.key > key:
            curr.left = self._insert_helper(key, path, curr.left, is_lc=True)
        else:
            curr.right = self._insert_helper(key, path, curr.right, is_lc=False)
        # An insertion in the left or right subtree may break the balance of
        # the current node.
        return self._rebalance(curr, path)
        # For insertion, there is at most one rebalancing operation when
        # backtracking and rebalancing, since after rebalancing the first
        # encountered unbalanced node when backtracking, all of its upper nodes
        # remain balanced.
        # T(n) = T(n/2) + O(1)
        # a = 1, b = 2, d = 1
        # According to Master Method, time: O(log n)

    def _update_path_height(self, path: List[Node]) -> None:
        """
        Helper method to update the heights of the nodes along the path.
        :param path: list[Node]
        :return: Node
        """
        for node in reversed(path):
            node.update_height()
        # Time: O(log n)

    def _rebalance(self, curr: Node, path: List[Node]) -> Node:
        """
        Helper function to rebalance the given node if it is unbalanced.
        :param curr: Node
        :param path: list[Node]
        :return: Node
        """
        balance = self._get_balance(curr)
        # For detailed explanation, please refer to the tutorial
        # "Left-left imbalance"
        if balance > 1 and self._get_balance(curr.left) > 0:
            # For the unbalanced node, the height of the left subtree is 2
            # higher than the right subtree;
            # for the left child, the height of the left subtree is 1 higher
            # than the right subtree.
            new_root = self._right_rotate(unbalanced=curr)
            self._update_path_height(path)
            return new_root
        # "Right-right imbalance"
        elif balance < -1 and self._get_balance(curr.right) < 0:
            # For the unbalanced node, the height of the right subtree is 2
            # higher than the left subtree;
            # for the right child, the height of the right subtree is 1 higher
            # than the left subtree.
            new_root = self._left_rotate(unbalanced=curr)
            self._update_path_height(path)
            return new_root
        # "Left-left imbalance"
        elif balance > 1 and self._get_balance(curr.left) < 0:
            # For the unbalanced node, the height of the left subtree is 2
            # higher than the right subtree;
            # for the left child, the height of the right subtree is 1 higher
            # than the left subtree.

            # First do a left rotation towards the left child, making the case a
            # left-left imbalance
            curr.left = self._left_rotate(unbalanced=curr.left)

            new_root = self._right_rotate(unbalanced=curr)
            self._update_path_height(path)
            return new_root
        # "Right-left imbalance"
        elif balance < -1 and self._get_balance(curr.right) > 0:
            # For the unbalanced node, the height of the right subtree is 2
            # higher than the left subtree;
            # for the right child, the height of the left subtree is 1 higher
            # than the right subtree.

            # First do a right rotation towards the right child, making the case
            # a right-right imbalance
            curr.right = self._right_rotate(unbalanced=curr.right)

            new_root = self._left_rotate(unbalanced=curr)
            self._update_path_height(new_root)
            return new_root

        # The insertion doesn't break the balance of the current node.
        return curr
        # Time: O(log n)

    def _get_balance(self, node: Node) -> int:
        """
        Helper function to calculate the balance of the given node.
        :param node: Node
        :return: int
        """
        if not node:
            return 0
        left_height, right_height = 0, 0
        if node.left:
            left_height = node.left.height
        if node.right:
            right_height = node.right.height
        return left_height - right_height
        # Time: O(1)
