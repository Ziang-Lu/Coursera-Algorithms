### Minimum Spanning Tree (MST)

#### Informal Goal

Connect a bunch of points together as cheaply as possible.

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

<br>

#### Application

1. Clustering
2. Networking