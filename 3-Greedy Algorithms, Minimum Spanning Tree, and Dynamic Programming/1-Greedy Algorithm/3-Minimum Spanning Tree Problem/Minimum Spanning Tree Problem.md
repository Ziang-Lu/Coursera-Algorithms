### Minimum Spanning Tree (MST) Problem

#### Problem Definition

**Input:**

A **undirected** graph $G(V, E)$, and a cost $c_e$ for each edge $e \in E$, which could be negative.

*For directed graphs, this is called "Optimal Branching Problem".*

---

Spanning tree (that spans all the vertices):

$T \subseteq E$, s.t.

* No cycle/loop
* The spanning tree is connected over all the vertices, i.e., there is a path between each pair of the graph.

$Cost_T$ = $\Sigma c_e$ for $e \in T$

---

*Assumption: G is connected. Or it's impossible to find a spanning tree that spans all the vertices. -> Can be easily checked by BFS/DFS in linear time*

**Output**

A spanning tree $T \subseteq E$ that spans the all the vertices with minimum cost

<br>

#### Greedy Algorithms

**O(mlog n)**

1. Prim's MST Algorithm

   *Grow a tree one edge at a time greedily.*

   * Arbitrarily choose a source vertex $s \in V$

   * Initialize $X = \{s\}$, which contains the vertices we've spanned so far, and $T = \emptyset$, which is the current spanning tree

     *Invariant: The current spanning tree $T$ spans all the vertices in $X$.*

   * While $X \neq V$:

     * Let $e = (v, w)$ be the **cheapest (greedy)** edge of $G$ with $v \in X$ and $w \notin X$.
     * Add $e$ to $T$
     * Add $w$ to $X$

2. Kruskal's MST Algorithm

   *Contrast to Prim's algorithm, Kruskal's algorithm throws out the desire to have a connected subgraph at each step of the iteration, and it will totally content to grow a tree in parallel with lots of simultaneous little pieces, only having them coalesce at the very end of the algorithm.*

   *=> Pick the cheapest remaining edge greedily as long as it does not create cycles.*

   * Sort the edges in order of increasing cost

     *Notation: Rename the edges to 1, 2, …, $m$ s.t. $c_1 \lt c_2 \lt … \lt c_m$*

   * Initialize $T = \emptyset$, which is the current spanning tree

   * For **$i$ = 1 to $m$ (greedy)**

     * If adding $i$ to $T$ doesn't cause cycles in $T$   *[This can be done by BFS/DFS towards T.]*
       * Add $i$ to $T$

<br>

#### Application: Clustering

**Problem Statement:**

Given $N$ points, a distance measure $d$ (symmetrical, i.e., $d(p, q) = d(q, p)$), and the number of clustering $k$.

*Two points $p$ and $q$ are called **separated** if they are assigned to **different clusterings**.*

*=> Objective function (variable): the **spacing of a clustering** is the **distance between the closest pair of separated points**.*

Compute the k-clustering with maximum spacing, i.e, $O(c^*) = min_{separated \ p, q}d(p, q)$.

<br>

**Algorithm: Single-link Cluster (单链聚类)**

Since as a greedy algorithm, we want to maximize $min_{separated \ p, q} d(p, q)$ (spacing) as much as possible.

=> We just need to make $p$ and $q$ **not separated** by **fusing the two clusters** containing $p$ and $q$, respectively.

* Inittially, each point is in a separate cluster.
* Repeat until only $k$ clusters:
  * Let $p$, $q$ = closest pair of separated points, which determines the current spacing
  * Merge the clusters containing $p$ and$q$ into a single cluster

**=> Exactly the same as Kruskal's MST Algorithm !!!** (except aborting early, i.e., when the number of connected components drops down to $k$)

