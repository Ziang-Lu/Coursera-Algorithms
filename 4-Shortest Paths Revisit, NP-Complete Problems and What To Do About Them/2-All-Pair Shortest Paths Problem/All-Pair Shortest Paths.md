### All-Pair Shortest Paths (APSP) Problem

Extension to Single-Source Shortest Paths problem, but to compute the shortest paths for **all** pairs of vertices.

<br>

**If the input graph is guaranteed to have NO negative edges:** -> Run **Dijkstra's algorithm** (O($m$log $n$)) $n$ times for each vertex as the source vertex

=> For **sparse** graph (m~$\Theta(n)$), the algorithm runs in **O($n^2$log n)**.   **[GOOD ENOUGH]**

​	**Floyd-Warshall algorithm: O($n^3$)**   **[WORSE]**

​	**Johson's algorithm: O($n^2$log $n$)**   **[(Same as Dijkstra) GOOD ENOUGH]**

=> For **dense** graph (m~$\Theta(n^2)$), the algorithm runs in **O($n^3$log $n$)**.

​	**Floyd-Warshall algorithm: O($n^3$)**   *[Comparable to Dijkstra's algorithm]*

​	**Johson's algorithm: O($n^3$log $n$)**   *[(Same as Dijkstra)]*

<br>

**If the input graph may possibly have negative edges:** -> Run **Bellman-Ford algorithm** (O($m$$n$)) $n$ times for each vertex as the source vertex

=> For **sparse** graph (m~$\Theta(n)$), the algorithm runs in **O($n^3$)**.

​	**Floyd-Warshall algorithm: O($n^3$)**   *[Comparable to Bellman-Ford algorithm]*

​	**Johson's algorithm: O($n^2$log $n$)**   **[BETTER]**

=> For **dense** graph (m~$\Theta(n^2)$), the algorithm runs in **O($n^4$)**.   **[BAD]**

​	**Floyd-Warshall algorithm: O($n^3$)**   **[BETTER]**

​	**Johson's algorithm: O($n^3$log $n$)**   *[(Same as Dijkstra)]*

<br>

#### Floyd-Warshall APSP Algorithm (Dynamic programming)

*Though we may assume that the input graph doesn't have negative directed cycles, Floyd-Warshall APSP Algorithm is still able to discover this anyway, i.e., finds an excuse that why the shortest paths cannot be computed.*

=>

Given a directed edge $G=(V,E)$ with each edge length $c_e$ could possibly be negative, and a source vertex $s$, Floyd-Warshall APSP Algorithm will either computes the shortest paths for all pairs of the vertices, or discovers negative cycles.

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

<br>

#### Johnson's APSP Algorithm: (PARTIALLY dynamic programming, from the pre-processing step using Bellman-Ford algorithm)

**Key insight: Re-weighting technique**

*If we give **each vertex $v$ a weight $w_v$**, and then **re-weight the edges** $e = (v, w) \in E$ as*
$$
c_e' \ = \ c_e + w_v - w_w
$$
*Then consider an arbitrary path $P$ of length $L$ from an arbitrary source $s$ to an arbitrary destination $d$: after re-weighting, the new path length $L'$ is*
$$
L' \ = \ L + w_s - w_{v_1} + w_{v_1} - w_{v_2} + ... +w_{v_{n-1}} - w-{v_n} + w_{v_n} - w_d \\
= L + w_s - w_d \\
= L + const
$$
*Thus this guarantees that after re-weighting, between each pair of vertices, the ranking of the path lengths remains unchanged, so the **shortest path preserves**.*

*

**As long as we can find a group of $w_v$, making all edge lengths $c_e$ (which could be possibly negative, and thus requires Bellman-Ford algorithm) to be non-negative $c_e'$ (which enables Dijkstra's algorithm), we can solve the APSP problem as fast as running Dijkstra's algorithm $n$ times on graphs with non-negative edges.**

**=> HOW DO WE FIND THESE $w_v$?**

<br>

**Algorithm:**

1. Form $G'$ by adding a void source vertex $s$ and new edges ($s$, $v$) with length 0 for each $v \in V$

2. Find the shortest path lengths from $s$, namely run Bellman-Ford algorithm with $s$ as the source vertex

   *Note that the deliberate setting guarantees that all shortest path lengths from $s$ is finite, and have to be $\le 0$.*

   *Also note that Bellman-Ford algorithm is also responsible for detecting negative cycles in $G'$ ($G$) if there are some.*

3. **For each $v \in V$, define $w_v$ as the shortest path lengths from $s$ to that $v$**

   *Proof of $c_{(v, w)}'$ being non-negative:*

   *Since $w_v$ is the shortest path length from $s$ to $v$, the path from $s$ to $v$ and then to $w$ has length $w_v+c_{(v, w)}$, and this path length has to be no greater than the shortest path length from $s$ to $w$, namely $w_w$, so we have*
   $$
   w_v + c_{(v, w)} \ \ge \ w_w \\
   c_{v, w} + w_v - w_w \ \ge \ 0
   $$
   *i.e.,*
   $$
   c_{(v, w)}' \ \ge \ 0
   $$

4. …… (as previousely)

5. Post-process: for each $s, v \in V$, the shortest path length before re-weighting is $L(s, v) = L'(s, v) - w_s + w_v$.

The algorithm is a combination of 1 invocation of Bellman-Ford algorithm (O($m$$n$)) and $n$ invocations of Dijkstra's algorithm (O($m$log $n$)) (= O($m$$n$log $n$)), and thus is **govened by the $n$ invocations of Dijkstra's algorithm** and runs in **O($m$$n$log $n$)**.

<br>

### Summary

**Single-Source Shortest Paths (SSSP) problem:**

* Dijkstra's algorithm: O($m$log $n$)

  *Restriction: No negative edges*

* Bellman-Ford algorithm: O($m$$n$)

|                                 | Graph W/O Negative Edge   | Graph W Negative Edge  |
| ------------------------------- | ------------------------- | ---------------------- |
| Sparse graph ($m = \Theta(n)$)  | Dijkstra: O($n$log $n$)   | Bellman-Ford: O($n^2$) |
| Dense graph ($m = \Theta(n^2)$) | Dijkstra: O($n^2$log $n$) | Bellman-Ford: O($n^3$) |

**APSP problem:**

|                                 | Graph W/O Negative Edge                                      | Graph W Negative Edge                                        |
| ------------------------------- | ------------------------------------------------------------ | ------------------------------------------------------------ |
| Sparse graph ($m = \Theta(n)$)  | **$n$ x Dijkstra = O($n^2$log $n$)**<br />Floyd-Warshall: O($n^3$)<br />Johnson: O($n^2$log $n$) | $n$ x Bellman-Ford = O($n^3$)<br />Floyd-Warshall: O($n^3$)<br />**Johnson: O($n^2$log $n$)** |
| Dense graph ($m = \Theta(n^2)$) | $n$ x Dijkstra = O($n^3$log $n$)<br />**Floyd-Warshall: O($n^3$)**<br />Johnson: O($n^3$log $n$) | $n$  xBellman-Ford = O($n^4$)<br />**Floyd-Warshall: O($n^3$)**<br />Johnson: O($n^3$log $n$) |

