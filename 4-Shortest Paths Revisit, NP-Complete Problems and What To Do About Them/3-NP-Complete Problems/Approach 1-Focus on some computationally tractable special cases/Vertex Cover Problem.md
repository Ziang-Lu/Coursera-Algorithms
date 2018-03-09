### Vertex Cover Problem

#### Problem Definition (somewhat "complement" to maximum-weight independent set problem)

Given a undirected graph $G=(V,E)$, find a minimum-size vertex cover, which is a subset of the vertices such that each edge has at least one endpoint in the subset.

*(Choose a minimum-size subset of the vertices that cover all the edges)*

**In general graphs, this problem is NP-complete.**

<br>

However, we can restrict the graph to be some **special graph (path graph, tree graph, or bipartite graph (两偶图))**.

------

Bipartite graph (两偶图)

A graph that has no odd cycle.

Equivalently, you can divide the vertices into two groups $A$ and $B$, s.t. every single edge has exactly one endpoint in $A$ and the other in $B$.

i.e., There exists a cut that slices every single edge.

------

