### All-Pair Shortest Paths (APSP) Problem

Extension to Single-Source Shortest Paths problem, but to compute the shortest paths for **all** pairs of vertices.

<br>

**If the input graph is guaranteed to have NO negative edges:**

-> Run **Dijkstra's algorithm** (O($m$log $n$)) $n$ times for each vertex as the source vertex

=> For **sparse** graph (m~$\Theta(n)$), the algorithm runs in **O($n^2$log n)**.   **[GOOD ENOUGH]**

​	**Floyd-Warshall algorithm: O($n^3$)**   **[WORSE]**

=> For **dense** graph (m~$\Theta(n^2)$), the algorithm runs in **O($n^3$log $n$)**.

​	**Floyd-Warshall algorithm: O($n^3$)**   *[Comparable to Dijkstra's algorithm]*

<br>

**If the input graph may possibly have negative edges:**

-> Run **Bellman-Ford algorithm** (O($m$$n$)) $n$ times for each vertex as the source vertex

=> For **sparse** graph (m~$\Theta(n)$), the algorithm runs in **O($n^3$)**.

​	**Floyd-Warshall algorithm: O($n^3$)**   *[Comparable to Bellman-Ford algorithm]*

=> For **dense** graph (m~$\Theta(n^2)$), the algorithm runs in **O($n^4$)**.   **[BAD]**

​	**Floyd-Warshall algorithm: O($n^3$)**   **[BETTER]**

<br>

#### Floyd-Warshall APSP Algorithm (Dynamic programming)

*Though we may assume that the input graph doesn't have negative directed cycles, the Floyd-Warshall APSP Algorithm is still able to discover this anyway, i.e., finds an excuse that why the shortest paths cannot be computed.*

=>

Given a directed edge $G=(V,E)$ with each edge length $c_e$ could possibly be negative, and a source vertex $s$, the Floyd-Warshall APSP Algorithm will either computes the shortest paths for all pairs of the vertices, or discovers negative cycles.

<br>

*Order the vertices arbitrarily as {1, 2, …, $n$}, and denote $V^{(k)}$ = {1, 2, …, $k$}, i.e., a $k$-vertices prefix of the $n$ vertices.*

*Fix a source $s$ and a destination $d$, we introduce an extra parameter $k$ to describe the maxmimum labeled node in the internal nodes in the shortest path from $s$ to $d$.*

*(即shortest path中的internal nodes最多只包含前k个nodes.)*

**Optimal Substructure Lemma**

Let $P(s, d, k)$ be the cycle-free optimal solution (cycle-free shortest path with internal nodes labeled at most $k$).

* Case 1: If the internal nodes in $P(s, d, k)$ does **NOT** contain node-$k$,

  => $P(s, d, k) \ = \ P(s, d, k-1)$

* Case 2: If the internal nodes in $P(s, d, k)$ contains node-$k$,

  => By dividing $P(s, d, k)$ into two subpaths by node-$k$, we have

  $P(s, d, k) \ = \ P_1(s, k, k-1) + P_2(k, d, k-1)$

=> $P(s, d, k)$ is the smaller between the above two candidates.

**Pseudo code: (Bottom-up calculation)**

* Let $A[k, s, d]$ = 3D array index by $k$, $s$ and $d$
* Initialization ($k = 0$): For $s, d \in V$:
  * If $s$ == $d$:
    * $A[0, s, d] = 0$
  * Else if $edge_{(s, d)} \in E$:
    * $A[0, s, d] = c_{(s, d)}$
  * Else:
    * $A[0, s, d] = +\infty$
* For $k$ = 1, 2, …, $n$
  * For $s \in V$:
    * For $d \in V:$
      * $A[k, s, d] \ = \ min\{A[k-1, s, d], \ A[k-1, s, k] + A[k-1, k-1, d]\}$
* The final solution lies in exactly $A[n, s, d]$ for $s, d \in V$.

The algorithm runs in **O($n^3$)** time and memory.

***

*Extension to detect negative cycles:*

*If the input graph has negative cycles, then constructing the recurrence will lead to at least one $v$ in $A[n, v, v]$ (the diagonal of the final solution), s.t. $A[n, v, v] \lt 0$. Thus, we just need to check $A[n, v, v]$ (the diagonal of the final solution), and see if there is a negative value.*

***

*The reconstruction process is easy to come up with but difficult to implement, and thus is omitted.*

<br>

*Note that in order to detect negative edges, we must fill in the entire 3D array $A$, so early-stopping optimization doesn't apply.*

**Optimization: Space optimization**

In basic implementation, we need O($n^3$) space. However, since we only need $A[k-1, s, d]$ to compute $A[k, s, d]$, we only need to keep track of the subproblem solutions in the previous out-most iteration, so the space needed is reduced to O($n^2$).

However, in this way, we lose the ability to reconstruct the shortest paths. Thus, we may at the same time keep track of the maximum labeled node in the internal nodes in the shortest path from $s$ to $d$.

