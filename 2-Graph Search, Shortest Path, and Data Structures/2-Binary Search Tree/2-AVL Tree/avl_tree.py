#!usr/bin/env python3
# -*- coding: utf-8 -*-

"""
A very simple self-balancing AVL Tree implementation.

Invariant of an AVL Tree:
The heights of the left and right subtrees of any node in an AVL Tree won't
differ more than 1.
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

    def __repr__(self):
        s = '[%d]' % self._key
        return s


class AVLTree(object):

    def __init__(self):
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
        Private helper function to search for the given key in the given subtree
        recursively.
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
        if curr.key > key:
            return self._search_helper(key, curr=curr.left)
        else:
            return self._search_helper(key, curr=curr.right)

    def insert(self, key: int) -> None:
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

    def _insert_helper(self, key: int, parent: Node, curr: Node,
                       is_lc: bool) -> Node:
        """
        Private helper function to insert the given key to the given subtree
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

        # An insertion in the left or right subtree may break the balance of
        # the current node.
        return self._rebalance(curr)
        # T(n) = T(n/2) + O(n)
        # a = 1, b = 2, d = 1
        # According to Master Method, the overall running time complexity is
        # O(n).

        # For insertion, there is at most one rebalancing operation when
        # backtracking and rebalancing, since after rebalancing the first
        # encountered unbalanced node when backtracking, all of its upper nodes
        # remain balanced.

    def _rebalance(self, curr: Node) -> Node:
        """
        Helper function to rebalance the given node if it is unbalanced.
        :param curr: Node
        :return: Node
        """
        balance = self._get_balance(curr)
        # For detailed explanation, please refer to the tutorial.
        if balance > 1 and self._get_balance(curr.left) > 0:
            # Left-left imbalance
            # For the unbalanced node, the height of the left subtree is 2
            # higher than the right subtree;
            # for the left child, the height of the left subtree is 1 higher
            # than the right subtree.
            return self._right_rotate(unbalanced=curr)
        elif balance < -1 and self._get_balance(curr.right) < 0:
            # Right-right imbalance
            # For the unbalanced node, the height of the right subtree is 2
            # higher than the left subtree;
            # for the right child, the height of the right subtree is 1 higher
            # than the left subtree.
            return self._left_rotate(unbalanced=curr)
        elif balance > 1 and self._get_balance(curr.left) < 0:
            # Left-left imbalance
            # For the unbalanced node, the height of the left subtree is 2
            # higher than the right subtree;
            # for the left child, the height of the right subtree is 1 higher
            # than the left subtree.

            # First do a left rotation towards the left child, making the case a
            # left-left imbalance
            curr.left = self._left_rotate(unbalanced=curr.left)

            return self._right_rotate(unbalanced=curr)
        elif balance < -1 and self._get_balance(curr.right) > 0:
            # Right-left imbalance
            # For the unbalanced node, the height of the right subtree is 2
            # higher than the left subtree;
            # for the right child, the height of the left subtree is 1 higher
            # than the right subtree.

            # First do a right rotation towards the right child, making the case
            # a right-right imbalance
            curr.right = self._right_rotate(unbalanced=curr.right)

            return self._left_rotate(unbalanced=curr)

        # The insertion/deletion/reconnection doesn't break the balance of the
        # current node.
        return curr
        # Running time complexity: O(n)

    def _get_balance(self, node: Node) -> int:
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
        # Running time complexity: O(n)

    def _get_height(self, node: Node) -> int:
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
        # T(n) = 2T(n/2) + O(1)
        # a = 2, b = 2, d = 0
        # According to Master Method, the running time complexity is O(n).

    def _right_rotate(self, unbalanced: Node) -> Node:
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
        # Running time complexity: O(1)

    def _left_rotate(self, unbalanced: Node) -> Node:
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
        # Running time complexity: O(1)

    def delete(self, key: int) -> None:
        """
        Deletes the given key from the BST.
        :param key: int
        :return: None
        """
        if self._root is None:
            return

        self._root = self._delete_helper(key, curr=self._root)

    def _delete_helper(self, key: int, curr: Node) -> Node:
        """
        Private helper function to delete the given key from the given subtree
        recursively.
        :param key: int
        :param curr: Node
        :return: Node
        """
        # Case 1: Not found
        if curr is None:
            return None

        if curr.key == key:  # Found it
            if curr.left is None and curr.right is None:
                # Case 2: The node to delete is a leaf.
                return None
            elif curr.right is None:
                # Case 3: The node to delete only have left child.
                return curr.left
            elif curr.left is None:
                # Case 3: The node to delete only have right child.
                return curr.right
            else:
                # Case 4: The node to delete has both left and right children.
                successor = self._get_successor(node_to_delete=curr)
                successor.left = curr.left

                # A reconnection in the right subtree may break the balance of
                # the current (successor) node.
                return self._rebalance(curr=successor)

        # Recursive case
        if curr.key > key:
            curr.left = self._delete_helper(key, curr=curr.left)
        else:
            curr.right = self._delete_helper(key, curr=curr.right)

        # A deletion in the left or right subtree may break the balance of the
        # current node.
        return self._rebalance(curr=curr)
        # T(n) = T(n/2) + O(n)
        # a = 1, b = 2, d = 1
        # According to Master Method, the overall running time complexity is
        # O(n).

        # For deletion, there could be multiple rebalancing operations when
        # backtracking and rebalancing, since after rebalancing the first
        # encountered unbalanced node when backtracking, its upper nodes may
        # also be unbalanced.

    def _get_successor(self, node_to_delete: Node) -> Node:
        """
        Helper function to get the successor of the given node to delete.
        :param node_to_delete: Node
        :return: Node
        """
        return self._get_successor_helper(node_to_delete=node_to_delete,
                                          parent=node_to_delete,
                                          curr=node_to_delete.right)
        # Running time complexity: O(n)

    def _get_successor_helper(self, node_to_delete: Node, parent: Node,
                              curr: Node) -> Node:
        """
        Helper function to get the successor of the given node to delete in the
        given subtree recursively.
        :param node_to_delete: Node
        :param parent: Node
        :param curr: Node
        :return: Node
        """
        # Base case
        if curr.left is None:
            if curr is not node_to_delete.right:
                parent.left = curr.right
                curr.right = node_to_delete.right
            return curr
        # Recursive case
        successor = self._get_successor_helper(node_to_delete=node_to_delete,
                                               parent=curr, curr=curr.left)
        # A reconnection in the right subtree may break the balance of
        # the current node.
        if curr is node_to_delete.right:
            parent.right = self._rebalance(curr=curr)
        else:
            parent.left = self._rebalance(curr=curr)
        return successor
        # T(n) = T(n/2) + O(n)
        # a = 1, b = 2, d = 1
        # According to Master Method, the running time complexity is O(n).

    def traverse_in_order(self) -> str:
        """
        Traverses the BST in-order.
        :return: str
        """
        s = self._traverse_in_order_helper(curr=self._root)
        print(s.strip())

    def _traverse_in_order_helper(self, curr: Node) -> str:
        """
        Private helper function to traverse the given subtree in-order
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
