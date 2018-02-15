#!usr/bin/env python3
# -*- coding: utf-8 -*-

"""
Scheduling Problem:
-One shared resource
-Many jobs to do, each job has an importance/priority w_j and an elapse time l_j
 (Jobs with higher weights deserve to be processed earlier than those with lower
 weights.)
-In what order should we sequence these jobs?

Criterion:
Minimize the weighted sum of the completion times, where the completion time of
a job is the overall timestamp at which the job is finished

Greedy algorithm:
Give each job a "score" of w_j/l_j, and sort the jobs by this "score"
这个score本质意义上为单位时间内可以完成多少importance/priority
=> Make sense

Proof:
-Fix an arbitrary input (N jobs)

Let s be the job sequence output by the greedy algorithm.
Rename the jobs, s.t., w_1/l_1 >= w_2/l_2 >= ... >= w_N/l_N

Assume that there exists an s* that is the optimal job sequence and strictly
better than s.
In this way, s = 1, 2, ..., N, and in s* there must exist consecutive jobs
(i, j) s.t. i > j.

"Exchange argument":
By exchanging i and j, the completion time of i will go up, and that of j will
go down, and all other stuff remains unchanged.
=> Cost: the criterion will increase by w_i * l_j
   Benefit: the criterion will decrease by w_j * l_i
Since i > j, then w_i/l_i <= w_j/l_j => w_i*l_j (cost) <= w_j*l_i (benefit)
=> Exchanging i and j where i > j has non-negative net benefit.
=> We can keep doing this kind of exchanges with non-negative net benefits, and
   finally yield s.
   i.e., s is optimal.
"""

__author__ = 'Ziang Lu'

from functools import total_ordering


@total_ordering
class Job(object):
    def __init__(self, weight, length):
        self._weight = weight
        self._length = length

    @property
    def weight(self):
        """
        Accessor of weight.
        :return: float
        """
        return self._weight

    @property
    def length(self):
        """
        Accessor of length.
        :return: float
        """
        return self._length

    def __lt__(self, other):
        score, other_score = self._weight / self._length, other.weight / other.length
        return other_score < score

    def __repr__(self):
        return 'Job with weight %lf and length %lf' %\
               (self._weight, self._length)


def schedule(jobs):
    jobs.sort()