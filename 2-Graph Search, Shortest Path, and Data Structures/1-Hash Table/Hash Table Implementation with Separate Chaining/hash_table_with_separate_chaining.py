#!usr/bin/env python3
# -*- coding: utf-8 -*-

"""
A very simple hash table implementation with separate chaining, mapping
lowercase words to their frequencies.
"""

__author__ = 'Ziang Lu'

import hashlib
import math
import re
from typing import List


class IllegalArgumentError(ValueError):
    pass


class DataItem(object):

    def __init__(self, data: str):
        """
        Constructor with parameter.
        :param data: str
        """
        self._data = data
        self._freq = 1

    @property
    def data(self) -> str:
        """
        Accessor of data.
        :return: str
        """
        return self._data

    @property
    def freq(self) -> int:
        """
        Accessor of freq.
        :return: int
        """
        return self._freq

    def add_freq(self) -> None:
        """
        Adds 1 to the frequency.
        :return: None
        """
        self._freq += 1

    def __repr__(self):
        return '[{data}, {freq}]'.format(data=self._data, freq=self._freq)


class HTWithSC(object):
    _LOAD_FACTOR = 5.0

    @staticmethod
    def _initialize_data(capacity: int) -> List[list]:
        """
        Initializes data list with the given capacity.
        :param capacity: int
        :return: list[list[DataItem]]
        """
        return [[] for _ in range(capacity)]

    @staticmethod
    def _is_lower_word(text: str) -> bool:
        """
        Determines whether the given text is a lowercase word.
        :param text: str
        :return: bool
        """
        lower_word_regex = re.compile('^[a-z]+$')
        return text is not None and lower_word_regex.match(text)

    @staticmethod
    def _is_prime(n: int) -> bool:
        """
        Determines whether the given number is prime.
        :param n: int
        :return: bool
        """
        if n <= 1:
            return False
        if n == 2:
            return True
        if n % 2 == 0:
            return False
        for i in range(3, int(math.sqrt(n)), 2):
            if n % i == 0:
                return False
        return True

    def __init__(self, initial_capacity=10):
        """
        Constructor with parameter.
        :param initial_capacity: int
        """
        # Check whether the input initial capacity is positive
        if initial_capacity <= 0:
            raise IllegalArgumentError(
                'The initial capacity should be positive.'
            )

        self._md5 = hashlib.md5()
        self._data = self._initialize_data(initial_capacity)
        self._num_of_items = 0

    def hash_value(self, text: str) -> int:
        """
        Calculates the hash value of the given text.
        :param text: str
        :return: int
        """
        # Check whether the input string is a lowercase word
        if not self._is_lower_word(text):
            return -1

        self._md5.update(text.encode('utf-8'))
        return int(self._md5.hexdigest(), base=16) % len(self._data)

    def size(self) -> int:
        """
        Returns the number of items in the hash table.
        :return: int
        """
        return self._num_of_items

    def contains(self, text: str) -> bool:
        """
        Checks whether the given text is in the hash table.
        :param text: str
        :return: bool
        """
        # Check whether the input string is a lowercase word
        if not self._is_lower_word(text):
            return False

        hash_val = self.hash_value(text)
        chaining = self._data[hash_val]
        for item in chaining:
            if item.data == text:
                return True
        return False

    def insert(self, text: str) -> None:
        """
        Inserts the given text to the hash table.
        No duplicate allowed
        :param text: str
        :return: None
        """
        # Check whether the input string is a lowercase word
        if not self._is_lower_word(text):
            return

        hash_val = self.hash_value(text)
        chaining = self._data[hash_val]
        for item in chaining:
            if item.data == text:
                # No duplicate allowed
                item.add_freq()
                return
        chaining.append(DataItem(text))
        self._num_of_items += 1
        if self._num_of_items / len(self._data) > self._LOAD_FACTOR:
            self._rehash()

    def _rehash(self) -> None:
        """
        Private helper function to rehash when the load factor is reached.
        :return: None
        """
        # Rather than insert the words again, we simply need to reconnect the
        # reference between two lists
        # Temporarily store the original list
        tmp = self._data
        # Create the new list
        new_size = self._find_next_prime(start=len(self._data) * 2 + 1)
        self._data = self._initialize_data(capacity=new_size)
        for chaining in tmp:
            for item in chaining:
                hash_val = self.hash_value(item.data)
                self._data[hash_val].append(item)

    def _find_next_prime(self, start: int) -> int:
        """
        Helper function to find the next prime starting from the given number.
        :param start: int
        :return: int
        """
        n = start
        while not self._is_prime(n):
            n += 1
        return n

    def remove(self, text: str) -> str:
        """
        Removes the given text from the hash table.
        :param text: str
        :return: str
        """
        # Check whether the input string is a lowercase word
        if not self._is_lower_word(text):
            return None

        hash_val = self.hash_value(text)
        chaining = self._data[hash_val]
        idx_to_remove = -1
        for i, node in enumerate(chaining):
            if node.data == text:
                idx_to_remove = i
                break
        if idx_to_remove == -1:  # Not found
            return None
        removed_data = chaining[idx_to_remove].data
        self._num_of_items -= 1
        return removed_data

    def display(self) -> None:
        """
        Displays the hash table.
        :return: None
        """
        for chaining in self._data:
            print(chaining)
