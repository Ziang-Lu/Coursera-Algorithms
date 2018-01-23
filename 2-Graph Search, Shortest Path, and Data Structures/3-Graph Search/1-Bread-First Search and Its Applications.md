### Breadth-First Search (BFS)

"Cautious and tentative exploration of the graph"

##### Algorithm: (Given graph $G$, source vertex $s$)

1. Initialize $G$ as $s$ explored and other vertices unexplored

2. Let $Q$ be the queue of vertices (vertices explored whose edges haven't been checked yet) initialized with $s$

3. While $q$ is not empty,

   1. Take out the first vertex $v$ (in the front of $Q$)

   2. For every edge ($v$, $w$) (an edge with one vertex explored and the other not),

      ​	If $w$ is not explored

      ​		Mark $w$ as explored

      ​		Push it to $Q$ (to the back of $Q$)




##### Applications:

1. Naive BFS

   ​	Given a graph $G$ and a source vertex $s$, find all the findable vertices starting from $s$.

2. Shortest path   **[Specific to BFS]**

   ​	Given a graph $G$, a source vertex $s$ and a destination vertex $d$, find the length of the shortest path from $s$ to $d$.

3. Undirected connectivity

   ​	Given a undirected graph $G$, find the number of connected components ("pieces") of $G$.

