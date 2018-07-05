#!usr/bin/env python3
# -*- coding: utf-8 -*-

"""
Select & Rank problem:
Given the ranking of a key in the BST, find the key.
Given a key in the BST, find its ranking.

The two sub-problems are mutually reverse problems.
"""

__author__ = 'Ziang Lu'


class Node(object):

    def __init__(self, key: int):
        """
        Constructor with parameter.
        :param key: int
        """
        self._key = key
        self._left = None
        self._right = None
        self._size = 1

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
    def size(self) -> int:
        """
        Accessor of size.
        :return: int
        """
        return self._size

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

    @size.setter
    def size(self, size: int) -> None:
        """
        Mutator of size.
        :param size: int
        :return: None
        """
        self._size = size

    def __repr__(self):
        s = '[%d, size: %d]' % (self._key, self._size)
        return s


class AugmentedBST(object):
    """
    Augmented BST class.
    A simple BST class, but each node has an additional data field: the number
    of nodes in the sub-tree rooted at that node.
    """

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
        return self._search_helper(key, curr=self._root)

    def _search_helper(self, key: int, curr: Node) -> bool:
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

    def insert(self, key: int) -> bool:
        """
        Inserts the given key to the BST.
        :param key: int
        :return: bool
        """
        if self._root is None:
            self._root = Node(key)
            return True

        return self._insert_helper(key, parent=None, curr=self._root,
                                   is_lc=True)

    def _insert_helper(self, key: int, parent: Node, curr: Node,
                       is_lc: bool) -> bool:
        """
        Private helper function to insert the given key to the given sub-tree
        recursively.
        :param key: int
        :param parent: Node
        :param curr: Node
        :param is_lc: bool
        :return: bool
        """
        # Base case 1: Found the spot to insert
        if curr is None:
            if is_lc:
                parent.left = Node(key)
            else:
                parent.right = Node(key)
            return True
        # Base case 2: Found it
        if curr.key == key:
            # No duplicate allowed
            return False

        curr.size += 1
        # Recursive case
        if curr.key > key:
            return self._insert_helper(key, parent=curr, curr=curr.left,
                                       is_lc=True)
        else:
            return self._insert_helper(key, parent=curr, curr=curr.right,
                                       is_lc=False)

    def traverse_in_order(self) -> None:
        """
        Traverses the BST in-order.
        :return: str
        """
        s = self._traverse_in_order_helper(curr=self._root)
        print(s.strip())

    def _traverse_in_order_helper(self, curr: Node) -> str:
        """
        Private helper function to traverse the given sub-tree in-order
        recursively.
        :param curr: Node
        :return: str
        """
        # Base case
        if curr is None:
            return ''
        # Recursive case
        s = self._traverse_in_order_helper(curr=curr.left)
        s += str(curr) + ' '
        s += self._traverse_in_order_helper(curr=curr.right)
        return s

    def select(self, rank: int) -> int:
        """
        Returns the key with the given ranking in the BST.
        :param rank: int
        :return: int
        """
        # Check whether the BST is empty
        if self._root is None:
            raise ValueError('The BST is empty.')
        # Check whether the input ranking is out of range
        num_of_nodes = self._root.size
        if rank <= 0 or rank > num_of_nodes:
            raise ValueError('The input ranking is out of range.')

        return self._select_helper(rank, curr=self._root)

    def _select_helper(self, rank: int, curr: Node) -> int:
        """
        Private helper function to find the key with the given ranking in the
        given sub-tree recursively.
        :param rank: int
        :param curr: Node
        :return: int
        """
        left_size = 0
        if curr.left is not None:
            left_size = curr.left.size
        curr_rank_in_subtree = left_size + 1
        # Base case
        if curr_rank_in_subtree == rank:
            return curr.key

        # Recursive case
        if curr_rank_in_subtree > rank:
            return self._select_helper(rank, curr=curr.left)
        else:
            # Note that the rank in the right sub-tree is
            # (rank - curr_rank_in_subtree)
            return self._select_helper(rank - curr_rank_in_subtree,
                                       curr=curr.right)

    def get_rank(self, key: int) -> int:
        """
        Returns the ranking of the given key in the BST.
        :param key: int
        :return: int
        """
        return self._get_rank_helper(key, curr=self._root)

    def _get_rank_helper(self, key: int, curr: Node) -> int:
        """
        Private helper function to find the ranking of the given key in the
        given sub-tree recursively.
        :param key: int
        :param curr: Node
        :return: int
        """
        # Base case 1: Not found
        if curr is None:
            return -1
        left_size = 0
        if curr.left is not None:
            left_size = curr.left.size
        curr_rank_in_subtree = left_size + 1
        # Base case 2: Found it
        if curr.key == key:
            return curr_rank_in_subtree

        # Recursive case
        if curr.key > key:
            return self._get_rank_helper(key, curr=curr.left)
        else:
            # Note that the rank in the right sub-tree is
            # (rank - curr_rank_in_subtree)
            return curr_rank_in_subtree + \
                   self._get_rank_helper(key, curr=curr.right)
