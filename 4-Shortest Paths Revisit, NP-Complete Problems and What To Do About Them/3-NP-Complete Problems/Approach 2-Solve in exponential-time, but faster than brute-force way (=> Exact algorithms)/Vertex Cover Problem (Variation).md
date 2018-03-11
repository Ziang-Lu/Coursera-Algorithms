Application of "*Solve in exponential-time, but faster than brute-force way*"   => Exact algorithms

### Vertex Cover Problem (Variation)

#### Problem Definition (somewhat "complement" to maximum-weight independent set problem)

Given a undirected graph $G=(V,E)$, find a minimum-size vertex cover, which is a subset of the vertices such that each edge has at least one endpoint in the subset.

*(Choose a minimum-size subset of the vertices that cover all the edges)*

**In general graphs, this problem is NP-complete.**

<br>

#### Focus on computationally tractable special cases   => Exact algorithms

However, we can restrict the graph to be some **special graph (path graph, tree graph, or bipartite graph (两偶图))**.

------

Bipartite graph (两偶图)

A graph that has no odd cycle.

Equivalently, you can divide the vertices into two groups $A$ and $B$, s.t. every single edge has exactly one endpoint in $A$ and the other in $B$.

i.e., There exists a cut that slices every single edge.

------

<br>

#### Solve in exponential-time, but faster than brute-force way   => Exact algorithms

**Problem Variation**

Consider except for the graph, we are also given as an input of a positive integer $k$, and the goal is to check whether there exists a vertex cover that uses at most $k$ vertices.

*($k$ is like a "budget".)*

<br>

This problem can be solved in a **brute-force** way, namely **$C_n^k$ combination of the possible $k$ vertices ($n^k$)**.

*(Note that since $k$ is not necessarily constant (O(1)), this problem is $\notin P$.)*

However, it can be solved smarter and faster.

<br>

**Substructure Lemma**

Consider an abitrary edge ($u$, $v$). Let $G_u$ be $G$ with $u$ and its edges removed, and $G_v$ be $G$ with $v$ and its edges removed. Then

$G$ has a vertex cover of at most $k$ vertices.   <=>   $G_u$ or $G_v$ (or both) has a vertex cover of at msot ($k$ - 1) vertices.

<br>

**Algorithm: (Backtracking)**

1. Pick an edge $(u, v) \in E$

2. Recursively search for a vertex cover in $G_u$ of at most ($k$ - 1) vertices

   If found ($S_u$), then $S = S_u + u$

3. Recursively search for a vertex cover in $G_v$ of at most ($k$ - 1) vertices

   If found ($S_v$), then $S = S_u + v$

4. If neither is found, then $S$ does NOT exist.

**Running time complexity analysis:**

* Consider the number of recursive calls, i.e., the number of nodes in the recursion tree: since the depth of the recursion tree is at most $k$, the algorithm has at most $2^k$ recursive calls.
* Per recursive call, we need to construct $G_u$ and $G_v$ from $G$, which removes $u$ ($v$) and its edges, and thus runs in O($m$).

Thus, the overall running time complexity is **O($m \cdot 2^k$)**.

Note that the running time complexity is still exponential in $k$, but it's better than brute-force way (O($n^k$)): even if $k$ is in O(log $V$), the overall running time complexity is **O($m \cdot 2^{log \ n} \ = \ mn$)**, which is polynomial-time, and thus the  problem becomes $\in P$.

