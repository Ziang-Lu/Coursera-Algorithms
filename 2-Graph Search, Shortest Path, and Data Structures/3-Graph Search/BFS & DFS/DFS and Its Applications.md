### Depth-First Search (DFS)

"Aggressive exploration of the graph" (go backwards only when necessary)

##### Algorithm: (Given graph $G$, source vertex $s$)

**Iterative** implementation:

```
1. Initialize G as s explored and other vertices unexplord
2. Let S be the stack of the vertices (vertices explored whose edges haven't been checked yet) initialized with s
3. While S is not empty
       1. Take out the first vertex v
       2. For every edge (v, w) (an edge with one vertex explored and the other not),
              If w is unexplored
                  Mark w as explored
                  Push it to S (to the top of S)
```

**Recursive** implementation:

```
1. Initialize G as s explored and other vertices unexplord
2. For every edge (v, w),
       If w is unexplored   (This itself serves as a base case: all the w's of s are explored.)
           Mark w as explored
           Do DFS on (G, w)   (Recursion)
```




##### Applications:

1. Naive DFS

   ​	Given a graph $G$ and a source vertex $s$, find all the findable vertices starting from $s$.

2. Undirected connectivity

   ​	Given a undirected graph $G$, find the number of connected components ("pieces") of $G$.

   **Algorithm: (Given graph $G$)**

   ```
   1. Initialize G as unexplored, count = 0
   2. For every vertex v,
          If v is unexplored (i.e., not explored in some previous BFS)
              count++
              Do DFS towards v   (Discovers precisely v's connected component)
   ```

3. Directed connectivity

   ​	**Strong** Connected Component (SCC):

   ​	A graph is **strongly** connected iff you can **get from any vertex to any other vertex through directed edges**.

   ​	=> An SCC of a directed graph is a part of the graph that is strongly connected.

   ​	

   **Kosaraju's two-pass algorithm: (Given graph $G$)**

   ```
   1. Let Grev be G with all directed edges reversed
   2. Run DFS-loop on Grev:   [First pass]
          For every vertex v,
              If v is unexplored
                  Do DFS towards v
                  Insides DFS, do the DFS along reversed edges, and each time a vertex is finished, set its finishing time (1,2,...,n) (即是第几个完成check全部incident edges的)
                  [本质上, 这个finishing time就是topological ordering]
   3. Clear explored and get ready for the second DFS-loop
   4. Run DFS-loop on G:    [Second pass]
          count = 0
          For every vertex v from finishing time n to 1,   [即反过来的topological ordering]
              If v is unexplored (i.e., not explored from some previous DFS)
                  count++
                  Do DFS towards v (Discovers precisely v's SCC)
   ```

   **Revised algorithm: (Given graph $G$)**

   ```
   1. Run DFS-loop on G:   [First pass]
      For every vertex v,
          If v is unexplored
              Mark v as explored
              Do DFS towards v
              Inside DFS, each time a vertex is finished, set its finishing time (1,2,...,n) (即是第几个完成check全部emissive edges的)
              [本质上, 这个finishing time就是反过来的topological ordering]
   2. Clear explored and get ready for the second pass
   3. Run DFS-loop on G:    [Second pass]
          count = 0
          For every vertex v from finishing time 1 to n,   [即反过来的topological ordering]
              If v is unexplored (i.e., not explored from some previous DFS)
                  count++
                  Do DFS towards v (Discovers precisely v's SCC)
   ```

4. Topological sort   **[Specific to directed graph and DFS]**


   Topological ordering of a directed graph:

   An ordering of the vertices so that all of the **edges only go forward in the ordering**.

   i.e., f($v$ (a vertex)) = $i$ (an order in the topological ordering (1, 2, …, $n$))

   => If ($v$, $w$) is a directed edge from $v$ to $w$, then f($v$) < f($w$). (即在topological ordering中, $v$要在$w$前面)

   => Directed edges have higher f-values as you traverse them.



​	**Algorithm using DFS: (Given graph $G$)**

        ```
For every vertex v,
    If v is unexplored
        Do DFS towards v
        Inside DFS, each time a vertex is finished, set its topological order (n,n-1,...,1)
        [本质上, topological ordering就是反过来的finishing time顺序]
        ```

​	**Straightforward algorithm: (Given graph $G$)**

```
1. Let v be a sink vertex (only incident edges, no emissive edges)
2. Set f(v) = n
3. Recurse on the graph without v
```

