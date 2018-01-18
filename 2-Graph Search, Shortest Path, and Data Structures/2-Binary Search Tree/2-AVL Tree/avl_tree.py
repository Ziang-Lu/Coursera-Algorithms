#!usr/bin/env python3
# -*- coding: utf-8 -*-

"""
A very simple self-balanced AVL Tree implementation.

Invariance of an AVL Tree:
The heights of the left and right sub-trees of any node in an AVL Tree won't
differ more than 1.
"""

__author__ = 'Ziang Lu'


class Node(object):
    def __init__(self, key):
        """
        Constructor with parameter.
        :param key: int
        """
        self._key = key
        self._left = None
        self._right = None

    @property
    def key(self):
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

    @left.setter
    def left(self, left):
        """
        Mutator of left.
        :param left: Node
        :return: None
        """
        self._left = left

    @right.setter
    def right(self, right):
        """
        Mutator of right.
        :param right: Node
        :return: None
        """
        self._right = right

    def __repr__(self):
        """
        String representation of this node.
        :return: str
        """
        s = '[%d]' % self._key
        return s


class AVLTree(object):
    def __init__(self):
        self._root = None

    def search(self, key):
        """
        Searches for the given key in this BST.
        :param key: int
        :return: bool
        """
        return self._search_helper(key, curr=self._root)

    def _search_helper(self, key, curr):
        """
        Private helper function to search for the given key in the given
        sub-tree recursively.
        :param key: int
        :param curr: Node
        :return: bool
        """
        # Base case 1: Not found
        if curr is None:
            return False
        # Base case 2: Found it
        if curr.key == key:
            return True

        # Recursive case
        return self._search_helper(key, curr=curr.left) or \
            self._search_helper(key, curr=curr.right)

    def insert(self, key):
        """
        Inserts the given key to the BST.
        :param key: int
        :return: None
        """
        if self._root is None:
            self._root = Node(key)
            return

        self._root = self._insert_helper(key, parent=None, curr=self._root,
                                         is_lc=True)

    def _insert_helper(self, key, parent, curr, is_lc):
        """
        Private helper function to insert the given key to the given sub-tree
        recursively.
        :param key: int
        :param parent: Node
        :param curr: Node
        :param is_lc: bool
        :return: Node
        """
        # Base case 1: Found the spot to insert
        if curr is None:
            new_node = Node(key)
            if is_lc:
                parent.left = new_node
            else:
                parent.right = new_node
            return new_node
        # Base case 2: Found it
        if curr.key == key:
            # No duplicate allowed
            return curr

        # Recursive case
        if curr.key > key:
            curr.left = self._insert_helper(key, parent=curr, curr=curr.left,
                                            is_lc=True)
        else:
            curr.right = self._insert_helper(key, parent=curr, curr=curr.right,
                                             is_lc=False)

        # An insertion in the left or right sub-tree may break the balance of
        # the current node.
        balance = self._get_balance(curr)
        # For detailed explanation, please refer to the tutorial.
        if balance > 1 and self._get_balance(curr.left) > 0:
            # Left-left imbalance
            # For the unbalanced node, the height of the left sub-tree is 2
            # higher than the right sub-tree;
            # for the left child, the height of the left sub-tree is 1 higher
            # than the right sub-tree.
            return self._right_rotate(unbalanced=curr)
        elif balance < -1 and self._get_balance(curr.right) < 0:
            # Right-right imbalance
            # For the unbalanced node, the height of the right sub-tree is 2
            # higher than the left sub-tree;
            # for the right child, the height of the right sub-tree is 1 higher
            # than the left sub-tree.
            return self._left_rotate(unbalanced=curr)
        elif balance > 1 and self._get_balance(curr.left) < 0:
            # Left-left imbalance
            # For the unbalanced node, the height of the left sub-tree is 2
            # higher than the right sub-tree;
            # for the left child, the height of the right sub-tree is 1 higher
            # than the left sub-tree.

            # First do a left rotation towards the left child, making the case a
            # left-left imbalance
            curr.left = self._left_rotate(unbalanced=curr.left)

            return self._right_rotate(unbalanced=curr)
        elif balance < -1 and self._get_balance(curr.right) > 0:
            # Right-left imbalance
            # For the unbalanced node, the height of the right sub-tree is 2
            # higher than the left sub-tree;
            # for the right child, the height of the left sub-tree is 1 higher
            # than the right sub-tree.

            # First do a right rotation towards the right child, making the case
            # a right-right imbalance
            curr.right = self._right_rotate(unbalanced=curr.right)

            return self._left_rotate(unbalanced=curr)

        # The insertion doesn't break the balance of the current node.
        return curr

    def _get_balance(self, node):
        """
        Helper function to calculate the balance of the given node.
        :param node: Node
        :return: int
        """
        if node is None:
            return 0

        left_height, right_height = self._get_height(node.left), \
            self._get_height(node.right)
        return left_height - right_height

    def _get_height(self, node):
        """
        Helper function to calculate the height of the given node recursively.
        :param node: Node
        :return: int
        """
        # Base case
        if node is None:
            return 0
        # Recursive case
        left_height, right_height = self._get_height(node.left), \
            self._get_height(node.right)
        return 1 + max(left_height, right_height)

    def _right_rotate(self, unbalanced):
        """
        Helper method to do a right rotation towards the given unbalanced node.
        :param unbalanced: Node
        :return: Node
        """
        # Temporarily store the left child
        tmp = unbalanced.left

        # Reconnect the references to realize right rotation
        unbalanced.left = unbalanced.left.right
        tmp.right = unbalanced

        return tmp

    def _left_rotate(self, unbalanced):
        """
        Helper method to do a left rotation towards the given unbalanced node.
        :param unbalanced: Node
        :return: Node
        """
        # Temporarily store the right child
        tmp = unbalanced.right

        # Reconnect the references to realize left rotation
        unbalanced.right = unbalanced.right.left
        tmp.left = unbalanced

        return tmp

    def traverse_in_order(self):
        """
        Traverses the BST in-order.
        :return: str
        """
        s = self._traverse_in_order_helper(curr=self._root)
        print(s.strip())

    def _traverse_in_order_helper(self, curr):
        """
        Private helper function to traverse the given sub-tree in-order
        recursively.
        :param curr: Node
        :return: None
        """
        # Base case
        if curr is None:
            return ''
        # Recursive case
        s = self._traverse_in_order_helper(curr=curr.left)
        s += str(curr) + ' '
        s += self._traverse_in_order_helper(curr=curr.right)
        return s
