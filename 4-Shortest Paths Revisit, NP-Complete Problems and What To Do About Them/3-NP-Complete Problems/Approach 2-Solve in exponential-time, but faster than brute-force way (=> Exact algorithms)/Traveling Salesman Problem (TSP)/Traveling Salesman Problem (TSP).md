Application of "*Solve in exponential-time, but faster than brute-force way*"   => Exact algorithms

### Traveling Salesman Problem (TSP)

#### Problem Definition

Classical problem; problem definition omitted

<br>

This problem can be solved in a **brute-force** way, namely **$n!$ permutations of the vertices**.

However, it can be solved smarter and faster.

<br>

#### Algorithm: (Dynamic programming)

**Optimal Substructure Lemma**

Let $P(v, S)$ be the shortest path from $s$ to $v$ that visits the vertices in $S \subset V$, containing $s$ and $v$, exactly once for each.

Then by plucking off the final hop ($w$, $v$), we form $P'(w, S - v)$ that is the shortest path from $s$ to $w$ that vists the vertices in $(S - v) \subset V$, containing $s$ and $w$, exactly once for each. Therefore, the recurrence is

$P(v, S) \ = \ min_{w \in S, w \ne v} \{P'(w, S - v) \ + c_{(w, v)}\}$

*(w即为shortest path中的倒数第二个vertex)*

<br>

**Pseudo Code: (Bottom-up calculation)**

*Note that the subproblem size is charactered by the cardinity of $S$, $m = |S|$.*

* Let $A[v][S]$ be a 2D array indexed by $v$ and $S$ containing $s$

* Initialization: $m = 1$, i.e., $S = \{s\}$

* For each $v \in V$:

  * If $v = s$:
    * $A[s][\{s\}] = 0$
  * Else:
    * $A[v][\{s\}] = +\infty$

* For $m$ = {2, 3, …, n}:

  * For each set $S \subset V$ of size $m$ that contains $s$:

    * For each $v \in V$:

      * (Make sure $S$ contains $v$)

      * If $v = s$:

        * $A[s][S] = +\infty$
        * ``continue``

      * $A[v][S] \ = \ min_{w \in S, w \ne v} \ \{A[w][S - v] \ + \ c_{(w, v)}\}$

        *($w$即为shortest path中的倒数第二个vertex)*

* (By now the algorithm only computes the shortest paths from $s$ to each $v \in V$ that visit all the vertices exactly once for each. However to complete TSP, we still need to go back to $s$.)

  TSP solution = $min_{w \in V, w \ne s} \{A[w][V] + c_{w, s}\}$

  *($w$即为TSP min-cost tour中的最后一站)*

**Running time complexity analysis:**

Since there are $O(n \cdot 2^n)$ subproblems, and each subproblem runs in $O(n)$ time, the overall running time complexity is **O($n^2 \cdot 2^n$)**.

Note that the running time complexity is still exponential, but it's better than brute-force (O($n!$)).

**Optimization: Space optimization**

In basic implementation, we need O($n \cdot 2^n$) space. However, since we only need solutions to subsets of size ($m - 1$) to compute the solutions to subsets of size $m$, we only need to keep track of the subproblem solutions in the previous out-most iteration, so the space needed is reduced to O($n \cdot C_n^k$) $\approx$ O($n^{k+1}$).

