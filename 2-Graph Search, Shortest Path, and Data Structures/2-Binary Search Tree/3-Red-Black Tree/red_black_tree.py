#!usr/bin/env python3
# -*- coding: utf-8 -*-

"""
Invariants of a Red-Black Tree:
1. Each node is either red or black.
2. The root is always black.
3. Never allow two red nodes in a row
4. Every root-null path passes exactly the same number of black nodes.
   i.e., The number of black nodes that a root-null passes cannot depend on
         the path.

Implementation according to:
https://zhuanlan.zhihu.com/p/37470948
"""

__author__ = 'Ziang Lu'


class Node(object):
    __slots__ = ['_key', '_parent', '_left', '_right', '_color']

    def __init__(self, key: int, color: bool):
        """
        Constructor with parameter.
        :param key: int
        :param color: boolean
        """
        self._key = key
        self._parent = None
        self._left = None
        self._right = None
        self._color = color

    @property
    def key(self) -> int:
        """
        Accessor of key.
        :return: int
        """
        return self._key

    @property
    def parent(self):
        """
        Accessor of parent.
        :return: Node
        """
        return self._parent

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
    def color(self) -> bool:
        """
        Accessor of color.
        :return: None
        """
        return self._color

    @parent.setter
    def parent(self, parent) -> None:
        """
        Mutator of parent.
        :param parent: Node
        :return: None
        """
        self._parent = parent

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

    @color.setter
    def color(self, color: bool) -> None:
        """
        Mutator of color.
        :param color: boolean
        :return: None
        """
        self._color = color

    def __repr__(self):
        s = f'[{self._key} '
        if self._color == RedBlackTree.RED:
            s += 'R'
        else:
            s += 'B'
        s += ']'
        return s


class RedBlackTree(object):
    __slots__ = ['_root']

    RED = True
    BLACK = False

    def __init__(self):
        """
        Default constructor.
        """
        self._root = None

    def search(self, key: int) -> bool:
        """
        Searches for the given key in the Red-Black Tree.
        :param key: int
        :return: boolean
        """
        return self._search_helper(key, self._root)

    def _search_helper(self, key: int, curr: Node) -> bool:
        """
        Private helper method to search for the given key in the given subtree
        recursively.
        :param key: int
        :param curr: Node
        :return: boolean
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

    def insert(self, key: int) -> None:
        """
        Insertion:
        1. Insert a new element as a normal BST (Might break invariants)
           Note that the newly inserted node is always red, since the
           probability of breaking the invariants for a red node is smaller than
           that of a black node
           i.e., If we insert a red node, there is a half-half probability of
                 breaking invariant #3; if we insert a black node, we must have
                 broken invariant #4, which is also more difficult to restore.
        2. Recolor and perform rotations until invariants are restored
        :param key: int
        :return: None
        """
        self._insert_helper(key, parent=None, curr=self._root, is_lc=True)

    def _insert_helper(self, key: int, parent: Node, curr: Node,
                       is_lc: bool) -> None:
        """
        Private helper method to insert the given key to the given subtree
        recursively.
        :param key: int
        :param parent: Node
        :param curr: Node
        :param is_lc: boolean
        :return: None
        """
        # Base case 1: Found the spot to insert
        if not curr:
            new_node = Node(key, self.RED)
            new_node.parent = parent
            if not parent:
                self._root = new_node
            else:
                if is_lc:
                    parent.left = new_node
                else:
                    parent.right = new_node

            # Restore the invariants   [O(log n)]
            self._restore_invariants(to_restore=new_node)
            return
        # Base case 2: Found it
        if curr.key == key:
            # No duplicate allowed
            return

        # Recursive case
        if curr.key > key:
            self._insert_helper(key, parent=curr, curr=curr.left, is_lc=True)
        else:
            self._insert_helper(key, parent=curr, curr=curr.right, is_lc=False)
        # T(n) = T(n/2) + O(1)
        # a = 1, b = 2, d = 0
        # According to Master Method, the overall running time complexity is
        # O(log n).

    def _restore_invariants(self, to_restore: Node) -> None:
        """
        Helper method to restore the invariants.
        :param to_restore: Node
        :return: None
        """
        # Case 1: Insert the first node (as the root)
        # Since to_store is red, this only violates invariant #2.
        # We simply need to recolor toRestore to black.
        if to_restore is self._root:
            to_restore.color = self.BLACK
            return

        # Case 2: The parent is black.
        # This won't violate invariant #3.

        # Case 3: The parent is red.
        # This violates invariant #3.
        parent = to_restore.parent
        if parent.color == self.RED:
            # Then to_restore must have a grandparent, and it must be black
            # according to invariant #3.
            grandparent = to_restore.parent.parent

            if parent is grandparent.left:
                uncle = grandparent.right

                if uncle and uncle.color == self.RED:
                    # Case 3.1: The uncle is red.
                    #         Black
                    #        /    \
                    #     Red     Red
                    #     /
                    # Red (Curr)
                    # To restore invariant #3 and #4, simply recolor the
                    # grandparent to red and the parent and the uncle to black.
                    self._restore_parent_and_uncle_are_red(grandparent, parent,
                                                           uncle)
                    #          Red
                    #        /    \
                    #     Black  Black
                    #     /
                    # Red (Curr)

                    # In this way, the same case applies to the newly recolored
                    # grandparent.
                    # Then we simply need to recurse towards grandparent.
                    self._restore_invariants(to_restore=grandparent)
                else:
                    # Case 3.2: The uncle is black.
                    if to_restore is parent.right:
                        # Case 3.2.1: to_restore is the right child.
                        #         Black
                        #        /    \
                        #     Red    Black
                        #       \
                        #    Red (Curr)
                        self._left_rotate(to_rotate=parent)

                        # In this way, the same case applies to the newly left
                        # rotated parent.
                        # Then we simply need to recurse towards parent.
                        self._restore_invariants(to_restore=parent)
                    else:
                        # Case 3.2.2: to_restore is the left child.
                        #         Black
                        #        /    \
                        #     Red    Black
                        #     /
                        # Red (Curr)
                        # Recolor the parent to black, and the grandparent to
                        # red
                        parent.color = self.BLACK
                        grandparent.color = self.RED
                        self._right_rotate(to_rotate=grandparent)
            else:
                uncle = grandparent.left

                if uncle and uncle.color == self.RED:
                    # Case 3.1: The uncle is red.
                    self._restore_parent_and_uncle_are_red(grandparent, parent,
                                                           uncle)
                    self._restore_invariants(to_restore=grandparent)
                else:
                    # Case 3.2: The uncle is black.
                    if to_restore is parent.left:
                        # Case 3.2.1: to_restore is the left child.
                        #     Black
                        #    /    \
                        # Black   Red
                        #         /
                        #    Red (Curr)
                        self._right_rotate(to_rotate=parent)

                        # In this way, the same case applies to the newly  left
                        # rotated parent.
                        # Then we simply need to recurse towards parent.
                        self._restore_invariants(to_restore=parent)
                    else:
                        # Case 3.2.2: to_restore is the right child
                        #     Black
                        #    /    \
                        # Black   Red
                        #           \
                        #        Red (Curr)

                        # Recolor the parent to black, and the grandparent to
                        # red
                        parent.color = self.BLACK
                        grandparent.color = self.RED
                        self._left_rotate(to_rotate=grandparent)

        # Recolor the root to black to ensure invariant #2
        self._root.color = self.BLACK
        # Running time complexity: O(log n)

    def _restore_parent_and_uncle_are_red(self, grandparent: Node, parent: Node,
                                          uncle: Node) -> None:
        """
        Helper method to restore the invariants when the parent and the uncle
        are both red.
        :param grandparent: Node
        :param parent: Node
        :param uncle: Node
        :return: None
        """
        grandparent.color = self.RED
        parent.color = self.BLACK
        uncle.color = self.BLACK
        # Running time complexity: O(1)

    def _left_rotate(self, to_rotate: Node) -> None:
        """
        Helper method to do a left rotation on the given node.
        :param to_rotate: Node
        :return: None
        """
        # Temporarily store the parent and the right child
        parent = to_rotate.parent
        tmp = to_rotate.right

        # Reconnect the references to realize left rotation
        to_rotate.right = tmp.left
        if tmp.left:
            tmp.left.parent = to_rotate

        tmp.left = to_rotate
        to_rotate.parent = tmp

        if parent:
            if to_rotate is parent.left:
                parent.left = tmp
            else:
                parent.right = tmp
        tmp.parent = parent

        if to_rotate is self._root:
            self._root = to_rotate
        # Running time complexity: O(1)

    def _right_rotate(self, to_rotate: Node) -> None:
        """
        Helper method to do a right rotation on the given node.
        :param to_rotate: Node
        :return: None
        """
        # Temporarily store the parent and the left child
        parent = to_rotate.parent
        tmp = to_rotate.left

        # Reconnect the references to realize right rotation
        to_rotate.left = tmp.right
        if tmp.right:
            tmp.right.parent = to_rotate

        tmp.right = to_rotate
        to_rotate.parent = tmp

        if parent:
            if to_rotate is parent.left:
                parent.left = tmp
            else:
                parent.right = tmp
        tmp.parent = parent

        if to_rotate is self._root:
            self._root = to_rotate
        # Running time complexity: O(1)

    def traverse_in_order(self) -> None:
        """
        Traverses the Red-Black Tree in-order.
        :return: None
        """
        s = self._traverse_in_order_helper(self._root)
        print(s.strip())

    def _traverse_in_order_helper(self, curr: Node) -> str:
        """
        Private helper function to traverse the given subtree in-order
        recursively.
        :param curr: Node
        :return: str
        """
        # Base case
        if not curr:
            return ''
        # Recursive case
        s = self._traverse_in_order_helper(curr.left)
        s += str(curr) + ' '
        s += self._traverse_in_order_helper(curr.right)
        return s
