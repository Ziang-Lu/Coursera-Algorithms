#!usr/bin/env python3
# -*- coding: utf-8 -*-

"""
Huffman encoding implementation.
"""

__author__ = 'Ziang Lu'

import heapq
from collections import Counter, deque
from functools import total_ordering
from typing import Deque


@total_ordering
class Node(object):
    __slots__ = ['_c', '_freq', '_left', '_right']

    def __init__(self, c: str, freq: int, left=None, right=None):
        """
        Constructor with parameter.
        :param c: str
        :param freq: int
        :param left: Node
        :param right: Node
        """
        self._c = c
        self._freq = freq
        self._left = left
        self._right = right

    @property
    def c(self) -> str:
        """
        Accessor of c.
        :return: str
        """
        return self._c

    @property
    def freq(self) -> int:
        """
        Accessor of freq.
        :return: int
        """
        return self._freq

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
        :return: None
        """
        return self._right

    def __lt__(self, other):
        return self._freq < other.freq


class HuffmanTree(object):
    __slots__ = ['_root', '_encoding_map']

    @staticmethod
    def _get_min_freq_node(primitives: Deque[Node],
                           merged: Deque[Node]) -> Node:
        """
        Gets a node with minimum frequency from the given two queues.
        :param primitives: deque[Node]
        :param merged: deque[Node]
        :return: Node
        """
        if not primitives:
            return merged.popleft()
        elif not merged:
            return primitives.popleft()

        primitives_front, merged_front = primitives[0], merged[0]
        if primitives_front <= merged_front:
            return primitives.popleft()
        else:
            return merged.popleft()

    def __init__(self, s: str):
        """
        Constructor with parameter.
        :param s: str
        """
        # self.construct_huffman_tree_with_heap(s)
        self.construct_huffman_tree_with_two_queues(s)

    def construct_huffman_tree_with_heap(self, s: str) -> None:
        """
        Constructs the Huffman tree using the given string with a heap.
        :param s: s
        :return: None
        """
        # Check whether the input string is None or empty
        if not s:
            self._root = Node(None, 0)
            return

        # Create a map between characters and their frequencies   [O(n)]
        freq_of_chars = Counter(s)

        # Create a Node heap from the map   [O(nlog n)]
        node_heap = []
        for c, freq in freq_of_chars.items():
            heapq.heappush(node_heap, Node(c, freq, left=None, right=None))

        # Construct the Huffman tree using a heap   [O(nlog n)]
        while len(node_heap) > 1:
            # Take out two nodes with minimum frequency from the heap
            min_node1 = heapq.heappop(node_heap)
            min_node2 = heapq.heappop(node_heap)
            # Combine the two nodes
            combined = Node(None, min_node1.freq + min_node2.freq,
                            left=min_node1, right=min_node2)
            # Put the combined node back to the heap
            heapq.heappush(node_heap, combined)
        # By now there is only one node in the heap, which is exactly the root
        # of the Huffman tree
        self._root = heapq.heappop(node_heap)

        # Create the encoding   [O(n)]
        self._encoding_map = {}
        self._create_encoding(self._root, encoding_so_far='')
        # Overall running time complexity: O(nlog n)

    def _create_encoding(self, curr: Node, encoding_so_far: str) -> None:
        """
        Private helper function to create the encoding recursively.
        :param curr: Node
        :param encoding_so_far: str
        :return: None
        """
        # Base case
        if not curr.left and not curr.right:
            self._encoding_map[curr.c] = encoding_so_far
            return
        # Recursive case
        self._create_encoding(curr.left, encoding_so_far=encoding_so_far + '0')
        self._create_encoding(curr.right, encoding_so_far=encoding_so_far + '1')
        # T(n) = 2T(n/2) + O(1)
        # a = 2, b = 2, d = 0
        # According to Master Method, the running time complexity is O(n).

    def construct_huffman_tree_with_two_queues(self, s: str) -> None:
        """
        Constructs the Huffman tree using the given string with two queues.
        :param s: str
        :return: None
        """
        # Check whether the input string is None or empty
        if not s:
            self._root = Node(None, 0)
            return

        # Create a map between characters and their frequencies   [O(n)]
        freq_of_chars = Counter(s)

        # Create a node array and sort it
        nodes = [
            Node(c, freq, left=None, right=None)
            for c, freq in freq_of_chars.items()
        ]
        nodes.sort()

        # Construct the Huffman tree using two queues   [O(n)]
        # "primitives" contains the nodes with the original character keys,
        # while "merged" contains the merged nodes.
        # According to the implementation, both of the queues are kept sorted.
        primitives, merged = deque(nodes), deque()
        while primitives or len(merged) != 1:
            # Take out two nodes with minimum frequency from the two queues
            min_node_1 = self._get_min_freq_node(primitives, merged)
            min_node_2 = self._get_min_freq_node(primitives, merged)
            # Combines the two nodes
            combined = Node(None, min_node_1.freq + min_node_2.freq,
                            left=min_node_1, right=min_node_2)
            # Put the combined node to the merged queue
            merged.append(combined)
        # By now there is only one node in the merged queue, which is exactly
        # the root of the Huffman tree.
        self._root = merged.popleft()

        # Create the encoding
        self._encoding_map = {}
        self._create_encoding(self._root, encoding_so_far='')

    def encode(self, msg: str) -> str:
        """
        Encodes the given message.
        :param msg: str
        :return: str
        """
        # Check whether the input string is None or empty
        if not msg:
            return ''

        return ''.join(map(lambda c: self._encoding_map[c], msg))

    def decode(self, encoded: str) -> str:
        """
        Decodes the given encoded string.
        :param encoded: str
        :return: str
        """
        # Check whether the input string is None or empty
        if not encoded:
            return ''

        return self._decode_helper(encoded, idx=0, curr=self._root)

    def _decode_helper(self, encoded: str, idx: int, curr: Node) -> str:
        """
        Private helper function to decode the encoded string recursively.
        :param encoded: str
        :param idx: int
        :param curr: Node
        :return: str
        """
        # Base case 1: Decoded all the encoded string
        if idx >= len(encoded):
            return curr.c
        # Base case 2: Reach a leaf
        if not curr.left and not curr.right:
            # Restart from the root
            return curr.c + \
                   self._decode_helper(encoded, idx=idx, curr=self._root)

        # Recursive case
        if encoded[idx] == '0':
            return self._decode_helper(encoded, idx=idx + 1, curr=curr.left)
        else:
            return self._decode_helper(encoded, idx=idx + 1, curr=curr.right)
