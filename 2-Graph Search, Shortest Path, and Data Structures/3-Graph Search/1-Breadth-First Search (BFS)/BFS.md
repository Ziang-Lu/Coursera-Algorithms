### Breadth-First Search (BF)

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

