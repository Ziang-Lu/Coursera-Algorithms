### Single-Vertex Shortest Paths

Input:

* A directed graph $G$ = ($V$, $E$) with each edge having a **non-negative (assumption for Dijkstra's shortest-path algorithm)** length, $l_e$.

  (即edge是weighted的了)

* A source vertex $s$

Output:

For each vertex $v$, compute the length of the shortest-path from $s$ to $v$, L($v$).

(即不再是单纯的# of edges in the shortest path, 而是看各条path的length, 找到shortest-path)

(有时候"绕路"反而是"捷径")



**Dijkstra's Shortest-Path Algorithm: (Given graph $G$, source vertex $s$)**

-> A slick generalization of BFS (When all edges lengths are 1, Dijkstra's shortest path algorithm => BFS)

1. Initialize $X = \{ s \}$, vertices processsed so far; vertices that we have correctly computed the shortest distance from $s$

   $A = \{ 0 \}$, corresponding shortest distances

   *(本质上是一种mapping)*

2. While $X \ne V$,

   Need to grow $X$ by $1$ => Which vertex to pick?

   **Among all directed crossing edges ($v$, $w$), with $v \in X$, $w \in (V-X)$, pick the edge that minimizes $A[v]+l_{vw}$ (Dijkstra's Greedy Criterion).**

   (Denote this pair ($v^*$, $w^*$))

   ​	Add $w^*$ to $X$

   ​	Set $A[w^*]=A[v]+l_{v^*w^*}$



Note that the algorithm can be generalized to undirected graphs.

