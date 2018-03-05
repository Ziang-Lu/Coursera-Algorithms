### Shortest Paths Revisit

Drawbacks of Dijkstra's Shortest-Path Algorithm:

1. Not always correct with negative edge lengths

   e.g., if edges <—> financial transactions

2. Every vertex needs to know the entire graph.

   => Thus non-feasible for massive graphs.

   e.g., if the problem involves Internet routing

<br>

From drawback #2, we yield another problem:

### "Distributed" Shortest Paths Problem

Each vertex uses only **local** computation, i.e., communicates with only the **vertices it is connected to**.

#### Bellman-Ford Shortest-Path Algorithm: (Dynamic programming)

*Though we may assume that the input graph doesn't have negative directed cycles (directed cycles with negative overall length) reachable from the source vertex, the Bellman-Ford Shortest-Path Algorithm is still able to discover this anyway, i.e., finds an excuse that why the shorest paths cannot be computed.*

=>

GIven a directed graph $G=(V,E)$ with each edge lengths $c_e$ could possibly be negative, and a source vertex $s$, the Bellman-Ford Shortest-Path Algorithm will either computes the shortest paths from $s$ to any other vertices, or discovers negative cycles reachable from $s$.

<br>

*Fix a destination $v$, we introduce an extra parameter $i$ to describe the maximum allowed number of edges from $s$ to $v$ (like a "budget"), and this "budget" will be used to represent the subproblem size.*

**Optimal substructure lemma:**

Let $P(s, v, i)$ be the optimal solution (shortest path with minimum total length) from $s$ to some destination $v$, using at most $i$ edges

*注意: 由于有了budget $i$的限制, 我们可以允许$P(s, v, i)$中有negative cycle, 而不用担心使用无限次该negative cycle导致产生负无穷的shortest path*

* Case 1: If $P(s, v, i)$ has $\le (i-1)$ edges, (i.e., $P$ doesn't use up its budget $i$.)

  => $P(s, v, i) \ = \ P(s, v, i-1)$

* Case 2: If $P(s, v, i)$ has exactly $i$ edges, (i.e., $P$ uses up all of its budget $i$.), with final hop ($w$, $v$)

  => By plucking off the final hop ($w$, $v$) from $P$, we form $P'(s, w, i-1)$.

  => $P(s, v, i) \ = \ min_{(w, v)} {P'(s, w, i-1)}$ + $c_{(w, v)}$   (in-degree($v$) candidates)

=> $P(s, v, i)$ is the minimum among the above (1 + in-degree(v)) candidates.

*(Assume the input graph doesn't have negative cycles reachable from $s$, then for each path, removing cycles only makes the total length go down, so each shortest path must not contain cycles; therefore, each shortest path has at most ($n$ - 1) edges.)*

*=> $i$ can be restricted to be $\le (n-1)$, i.e., $i$ = {1, 2, …, $n$ - 1}.*

**Pseudo code: (Bottom-up calculation)**

* Let $L[v, i]$ = 2D array indexed by $v$ and $i$
* Initialization: ($i$ = 0)
  * For $v \in V$:
    * If $v$ == $s$:
      * $L[v, 0] = 0$
    * Else:
      * $L[v, 0] = +\infty$
* For $i$ = 1, 2, …, $n$ - 1
  * For $v \in V$:
    * $L[v, i] \ = \ min\{L[v, i-1], min_{(w, v)}L(w, i-1)\}$
* The final solution lies in exactly $L[v, n-1]$ for $v \in V$.

**Optimization 1: Early-stopping**

The algorithm may stop early when in the current iteration, no update is made for any vertex.

**Optimization 2: Space optimization**

In basic implementation, we need O($n^2$) space. However, since we only need $L[v, i-1]$ to compute $L[v, i]$, we only need to keep track of the subproblem solutions in the previous outer iteration, so the space needed is reduced to $O(n)$.

However, in this way, we lose the ability to reconstruct the shortest paths. Thus, we may at the same time keep the penultimate vertex $B(s, v, i)$ for the shortest path from $s$ to $v$ using at most $i$ edges.

***

*Extension to detect negative cycles reachable from $s$:*

*If the input graph has negative cycles reachable from $s$, then we just run the outer loop for one extra iteration, and check if there is still an improvement on some vertex. If so, then the input graph must have negative cycles reachable from $s$.*

***

<br>

"Distributed":

In each round, each vertex only "communicate" with the vertices it is connected to.

<br>

#### Application: Internet rounting

Modifications to Bellman-Ford Shortest-Path Algorithm towards a routing protocol:

1. Internet protocols are **destination-driven** rather than source-driven.

   => *Reverse all directions in the Bellman-Ford algorithm, to compute the shortest paths from any vertex to a given destination vertex*

   => *At the same time, just like previously we need to keep track of penultimate vertices, now for each vertex we need to keep track of the next hop.*

   "Distance vector protocol" (距离矢量协议)

2. Since different computers on the Internet run in different speeds, we can't assume all $P(s, v, i - 1)$ get computed before $P(s, v, i)$, so we need a **asynchronous** Bellman-Ford algorithm implementation.

   => *Change from pull-base to push-based, i.e., as soon as $P(s, v, i) \lt P(s, v, i - 1)$, $v$ notifies all of its neighbors.*

3. Routers and links may **fail**.

   => *Each vertex maintains the entire shortest path to the destination vertex, not just the next hop.*

   "Path vector protocol" (路径矢量协议)


