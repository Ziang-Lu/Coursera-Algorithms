### Depth-First Search (BFS)

"Aggressive exploration of the graph" (go backwards only when necessary)

##### Algorithm: (Given graph $G$, source vertex $s$)

**Iterative** implementation:

1. Initialize $G$ as $s$ explored and other vertices unexplored

2. Let $S$ be the **stack** of vertices (vertices explored whose edges haven't been checked yet) initialized with $s$

3. While $S$ is not empty,

   1. Take out the first vertex $v$ (in the front of $Q$)

   2. For every edge ($v$, $w$) (an edge with one vertex explored and the other not),

      ​	If $w$ is not explored

      ​		Mark $w$ as explored

      ​		Push it to $Q$ (to the back of $Q$)




**Recursive** implementation

1. Initialize $G$ as $s$ explored and other vertices unexplored

2. For every edge ($v$, $w$),

   ​	If $w$ is not explored   (This itself serves as a base case: all the $w$'s of $s$ are explored.)

   ​		Mark w as explored

   ​		Do DFS on ($G$, $w$)   (Recursion)



##### Applications:

1. Naive DFS

   ​	Given a graph $G$ and a source vertex $s$, find all the findable vertices starting from $s$.

2. Undirected connectivity

   ​	Given a undirected graph $G$, find the number of connected components ("pieces") of $G$.

3. Directed connectivity

