### All-Pair Shortest Paths (APSP) Problem

Extension to Single-Source Shortest Paths problem, but to compute the shortest paths for **all** pairs of vertices.

<br>

**If the input graph is guaranteed to have NO negative edges:**

-> Run **Dijkstra's algorithm** (O($m$log $n$)) $n$ times for each vertex as the source vertex

=> For **sparse** graph (m~$\Theta(n)$), the algorithm runs in **O($n^2$log n)**.   **[GOOD ENOUGH]**

=> For **dense** graph (m~$\Theta(n^2)$), the algorithm runs in **O($n^3$log $n$)**.   *[Still to be explored whether we can do better than O($n^3$)]*

<br>

**If the input graph may possibly have negative edges:**

-> Run **Bellman-Ford algorithm** (O($m$$n$)) $n$ times for each vertex as the source vertex

=> For **sparse** graph (m~$\Theta(n)$), the algorithm runs in **O($n^3$)**.   **[GOOD ENOUGH]**

=> For **dense** graph (m~$\Theta(n^2)$), the algorithm runs in **O($n^4$)**.   **[=> Floyd-Warshall algorithm]**

<br>

#### Floyd-Warshall APSP Algorithm (Dynamic programming)