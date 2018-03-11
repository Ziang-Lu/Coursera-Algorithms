Application of "*Use heuristics (efficient algorithms that are not always correct)*"   => Approximate algorithms

Frequently-used heuristic design paradigm: Local search

### Maximum Cut Problem

Similar to minimum cut problem.

**For general graphs, this problem is NP-complete.**

<br>

#### Focus on computationally tractable special case   => Exact algorithms

e.g., For a **bipartite graph**, this problem can be solved by **BFS, and labeling the even layers in one group and the odd layers in the other group**. Thus, it runs in **O($m$)**, and the problem is reduced to be $\in P$.

<br>

#### Use heuristics (efficient algorithms that are not always correct)   => Approximate algorithms

**Local search heuristic:   (有点类似greedy)**

<u>Always maintain a candidate cut</u>, and just iteratively make it better and better via <u>small (local) modifications</u>.

**Algorithm: (Local search heuristic)   (有点类似greedy)**

1. Let (A, B)  be an arbitrary cut of $G$

   *For a vertex $v$:*

   *Denote $D_v(A, B)$ to be the number of internal edges of $v$, and $C_v(A, B)$ to be the number of crossing edges of $v$.*

   *Thus if we move $v$ to the other side, forming a cut (A', B'), then the previous internal edges of $v$ are not crossing the cut, and the previous crossing edges of $v$ are not internal.*

   *i.e., $D_v(A, B) = C_v(A', B')$ and $C_v(A, B) = D_v(A', B')$*

2. While there is a vertex $v$ with $D_v(A, B) > C_v(A, B)$

   * Move $v​$ to the other side

     => $C_v(A', B') = D_v(A, B) > C_v(A, B)$, and all the other edges stay unchanged.

     i.e., The total number of crossing edges increases.

3. Return the final resulting cut

**Running time complexity:**

For each while-loop, the total number of crossing edges only goes up by at least 1, so there are at most $m$ while-loops. Within each while-loop, we only need to do linear work (O($n$)), so the running time complexity is **O($m$$n$)**.