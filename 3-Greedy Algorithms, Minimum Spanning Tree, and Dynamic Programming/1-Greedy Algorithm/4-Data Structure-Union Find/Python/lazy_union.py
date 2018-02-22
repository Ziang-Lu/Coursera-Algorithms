#!bin/usr/env python3
# -*- coding: utf-8 -*-

"""
Lazy Union implementation of Union-Find data structure.
Lazy Union: Only update a single pointer in each union() operation
When two groups merge, make one group's leader (i.e., root of the tree) point to
the other leader (to be a child of the root of the other tree).

-Pros:
union() operation reduces to two find() operations and one link operation.
=> O(n)
-Cons:
find() operation needs to follow a path of parent pointers until the root, which
points to itself.

Problem: Which group to change leader?

Optimization 1: Union-by-Rank
Look at which of the two trees is already deeper, and we want to keep the root
of that deeper tree, so we install the root of the shallower tree as the child
of the deeper one.
=> Both find() and union() operations: O(r), where r is the rank of the root of
   the tree that the object belongs to

Claim: With Union-by-Rank optimization, the maximum rank for any object is
O(log n).

Proof:
********************
Claim: The subtree of a rank-r object has size >= 2^r.
Proof: by induction on the number of union() operations
Base case: Initially all ranks are 0, and all subtree sizes are 1 (2^0).
Inductive hypothesis: same as the claim
Inductive step:
(Nothing to prove when the ranks of all objects don't change. (Subtree sizes
only go up.))
When one of the ranks change (i.e., when two two groups has roots of the same
rank r), the new rank is r + 1, and the new subtree size is
2^r + 2^r = 2 * 2^r = 2^(r + 1) = 2^(new rank)
********************
Thus, the maximum possible rank r_max = log2 n.

=> Both find and union operations: O(log n)

Problem: Why bother traversing an object-root path multiple times?

Optimization 2: Path Compression
After a find(x) operation, install shortcuts (i.e., rewire parent pointers) to
x's root all along the x~root path.

=> In effect, path compression makes the tree shallower and more bushy.
(=> 最终回到了Eager Union的效果)

Important: Maintain all ranks EXACTLY as WITHOUT path compression!
=> Now the rank of an object only serves as an upper bound of the maximum number
   of hops on a path from a leaf to that object, rather than the previous
   equality.
"""

__author__ = 'Ziang Lu'


class LazyUnionObj:
    """
    Simple class for object to be stored in Union-Find data structure.
    """
    def __init__(self, name):
        """
        Constructor with parameter.
        :param name: str
        """
        self._name = name
        self._parent = self
        self._rank = 0

    @property
    def obj_name(self):
        """
        Accessor of name.
        :return: str
        """
        return self._name

    @property
    def parent(self):
        """
        Accessor of parent.
        :return: LazyUnionObj
        """
        return self._parent

    @property
    def rank(self):
        """
        Accessor of rank.
        :return: int
        """
        return self._rank

    @parent.setter
    def parent(self, parent):
        """
        Mutator of parent.
        :param parent: LazyUnionObj
        :return: None
        """
        self._parent = parent

    @rank.setter
    def rank(self, rank):
        """
        Mutator of rank.
        :param rank: int
        :return: None
        """
        self._rank = rank


class LazyUnion(object):
    @staticmethod
    def find(obj):
        """
        Returns the name of the group, which is exactly the name of the group
        root, that the given object belongs to.
        :param obj: LazyUnionObj
        :return: str
        """
        root = LazyUnion._find_root(obj)
        LazyUnion._path_compression(obj=obj, root=root)
        return root.obj_name
        # Running time complexity: O(log n)

    @staticmethod
    def _find_root(obj):
        """
        Private helper function to find the root of the given object.
        :param obj: LazyUnionObj
        :return: LazyUnionObj
        """
        curr = obj
        while not LazyUnion._is_root(curr):
            curr = curr.parent
        return curr
        # Running time complexity: O(log n)

    @staticmethod
    def _is_root(obj):
        """
        Helper function to check whether the given object is a root.
        :param obj: LazyUnionObj
        :return: bool
        """
        return obj.parent is obj
        # Running time complexity: O(1)

    @staticmethod
    def _path_compression(obj, root):
        """
        Private helper function to do path compression for the given object.
        :param obj: LazyUnionObj
        :param root: LazyUnionObj
        """
        curr = obj
        while not LazyUnion._is_root(curr):
            parent = curr.parent
            curr.parent = root
            curr = parent
        # Running time complexity: O(log n)

    @staticmethod
    def union(x, y):
        """
        Fuses the two groups that the given two objects belong to, respectively,
        together.
        :param x: LazyUnionObj
        :param y: LazyUnionObj
        :return: None
        """
        # Find the root of the groups   [O(log n)]
        x_root, y_root = LazyUnion._find_root(x), LazyUnion._find_root(y)
        if x_root is y_root:
            return
        # Make one group's root point to the other root   [O(1)]
        if x_root.rank >= y_root.rank:
            larger_rank_root, smaller_rank_root = x_root, y_root
        else:
            larger_rank_root, smaller_rank_root = y_root, x_root
        smaller_rank_root.parent = larger_rank_root
        larger_rank_root.rank = max(larger_rank_root.rank,
                                    1 + smaller_rank_root.rank)
        # Running time complexity: O(log n)
