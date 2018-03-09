**Ubiquitous intractability:**

Many important problems seem **impossible to solve efficiently**.

<br>

### Computational Tractability (i.e., Polynomial-Time Solvability):

Definition:

A computational problem is polynomial-time solvable if there is an algorithm that correctly solves it in O($n^k$) time for some constant $k$.

*For concrete algorithm, $k$ should be relatively small, like 1, 2 or 3.*

=> **Class P = set of all polynomial-time solvable problems**

=> Generally speaking, computational problems in P can be solved.

=> Computational problems that are not in P generally require significant computational and human resources, and domain expertise to solve.

<br>

**如何证明一个problem是polynomial-time UNSOLVABLE ($\notin P$)的?**

<u>Really good idea:</u>

Amass evident of intractability via <u>relative difficulty</u> ("as hard as" some other problems)

*(即证明某个问题至少和某些其他问题一样难)*

***

**Reduction (between two problems):**

Problem $\Pi_1$ reduces to problem $\Pi_2$ if given a polynomial-time solvable subroutine for $\Pi_2$, can use it to solve $\Pi_1$ in polynomial-time.

*All you need to solve $\Pi_1$ is a polynomial-time algorithm tha solves $\Pi_2$.*   *($\Pi_2$比$\Pi_1$更难)*

<u>Contrapositive collary:</u>

Suppose $\Pi_1$ reduces to $\Pi_2$, then $\Pi_1 \notin P$ => $\Pi_2 \notin P$.

<br>

**Completeness:**

Let $C$ be a set of problems. The problem $\Pi$ is said to be **$C$-complete** if:

* $\Pi \in C$
* Everything in $C$ reduces to $\Pi$, i.e., $\Pi$ is at least "as hard as any other problem in $C$".

=> 可以想象: $\Pi$ is the hardest-problem in $C$.

***

结合之前:

若要证明一个problem $\Pi$是polynomial-time unsolvable ($\notin P$)的, 则需要选取合适的$C$使得$\Pi$是$C$-complete的, 然后证明$C$中其他的问题都是$\notin P$的, 则根据reduction的contrapositive collary, $\Pi$也是$\notin P$的.

<br>

**Class NP ("Non-deterministic Polynomial")**

A problem is in NP if:

* solutions always have length polynomial in the input size;

  *This actually restrict the number of possible solutions in exponential number.*

* **purported solution can be verified in polynomial time**.

=> Every problem in NP can be solved by brute-force search in exponential number.

=> NP <=> All brute-force solvable problems.

<br>

### NP-Complete Problems

If a problem $\Pi$ is NP-complete, then by definition, all problems in NP reduce to $\Pi$ (即$\Pi$是NP中最难的).

**=> If there is a polynomial-time algorithm that solves a single NP-complete problem $\Pi$, then all problems in NP can be solved in polynomial-time. => NP = P**

**But, does it exist? At least NOT for now… (i.e., P ?= NP still need to be explored)**

<br>

**Proving a problem to be NP-complete is a strong sign that it cannot be solved efficiently (in polynomial-time) and thus a strong evidence of its computational intractablity. (Check for NP-completeness <=> Check for computational intractability)**

But, how to check whether a problem $\Pi$ is NP-complete?

1. Find a known NP-complete problem $\Pi'$

2. Prove that $\Pi'$ reduces to $\Pi$, i.e., $\Pi$ is at least as hard as $\Pi'$

   即$\Pi$比$\Pi'$更难

*The knapsack problem is NP-complete, because it runs in O($n$$W$), where $W$ is the knapsack capacity: since $W$ is encoded by $log \ W$ bits, increasing the number of encoding bits of $W$ by $1$ will lead to add exponentially subproblems.*

<br>

#### Algorithmic Approaches to NP-Complete Problems

*You should not explect some general-purposed and efficient (polynomial-time) algorithm. You may even need to make some compromises.*

1. **Focus on some computationally tractable special cases**   => Exact algorithm

   * e.g., <u>maximum-weight independent set problem</u>

     (In general graph, this is NP-complete; however if we restrict the graph to be a path graph, then this problem can be solved in O($n$) time using dynamic programming.)

   * e.g., *Refer to "**2-SAT problem**" and "**vertex cover problem**"*

2. **Solve in exponential-time, but faster than brute-force way**   => Exact algorithm

   * e.g., <u>dynamic programming-based solution to the knapsack problem</u>

     (Though the algorithm still runs exponentially in the input size (number of encoding bits of the knapsack capacity $W$) as explained above, but is much faster than the O($2^n$) exponential-time brute-force search.)

   * e.g., *Refer to "**Traveling Salesman Problem (TSP)**" and "**vertex cover problem (variation)**"*

3. **Use heuristics (efficient algorithms that are not always correct)**   => Approximate algorithm

   *=> Sacrifice some correctness; gain efficiency.*

   * e.g., *Refer to "**greedy-based solution to the knapsack problem**"*


