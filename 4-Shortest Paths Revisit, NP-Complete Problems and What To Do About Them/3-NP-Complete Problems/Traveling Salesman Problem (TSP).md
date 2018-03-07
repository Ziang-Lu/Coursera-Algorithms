Application of *"Solve in exponential time, but faster than brute-force way"* to deal with NP-complete problems

### Traveling Salesman Problem (TSP)

#### Problem Definition

Classical problem; problem definition omitted

<br>

This problem can be solved in a **brute-force** way, namely **$n!$ permutations of the destinations**.

However, it can be solved smarter and faster.

<br>

#### Algorithm: (Dynamic programming)

**Optimal Substructure Lemma**

Let $P(v, S)$ to be the shortest-path from $s$ to $v$ that visits the vertices in $S \subset V$, containing $s$ and $v$, exactly once for each.

Then by plucking off the final hop ($w$, $v$), we form $P'(w, S - v)$ that is the shortest-path from $s$ to $w$ that vists the vertices in $(S - v) \subset V$, containing $s$ and $w$, exactly once for each. Therefore, the recurrence is

$P(v, S) \ = \ min_{w \in S, w \ne v} \{P'(w, S - v) \ + c_{(w, v)}\}$

*(w即为shortest path中的倒数第二个vertex)*

**Pseudo Code: (Bottom-up calculation)**

*Note that the subproblem size is charactered by the cardinity of $S$, $m = |S|$.*

* Let $A[v][S]$ be a 2D array indexed by $v$ and $S$
* Initialization: $m = 1$
* For $m$ = {2, 3, …, n}:
  * For each set $S \subset V$ of size $m$ that contains $s$:
    * For each $v \in V$
    * ​

<br>

By dynamic programming, this problem can be solved in O($n^2 \cdot 2^n$) exponential-time.

