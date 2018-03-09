Application of "*Use heuristics (efficient algorithms that are not always correct)*"   => Approximate algorithms

### Knapsack Problem with Dynamic Programming-Based HEURISTIC

***

*Assume all the values are integer.*

**Another Dynamic Programming Algorithm**

Let $S(i, x)$ be the optimal solution for the subproblem among the first $i$ items and target value we are striving for $x$, i.e., the minimum total weight subject to getting a total value $\ge x$.

Consider whether item-$i$ is in $S$:

1. item-$i$ is NOT in $S$:

   => $S$ must be optimal among only the first ($i - 1$) items and target value $x$.

   => $S$ = the optimal solution among the first $(i - 1)$ items and target value $x$

2. item-$i$ is in $S$:

   => {$S$ - item-$i$} must be optimal among only the first ($i - 1$) items and target value ($x - v_i$).

   => $S$ = the optimal solution among the first $(i - 1)$ items and target value ($x - v_i$) + item-$i$

i.e.,

$S(i, x) \ = \ max\{S(i - 1, x), \ S(i - 1, x - v_i)\}$

**Running time complexity:**

Since there are O($n \cdot \Sigma v_i$) $\le$ O($n \cdot n v_{max}$) = $O(n^2 v_{max})$ subproblems, and for each problem, we only do constant amout of work, the overall running time complexity is O($n^2 v_{max}$).

***

With heuristics, we shoot for **arbitrarily good approximation**.

For a **user-specified parameter $\epsilon \in (0,1)$**, guarantee a **$(1 - \epsilon)$ approximation**, i.e., $(1 - \epsilon)$ * optimal solution.

This naturally forms a trade-off between running time and correctness:

* $\epsilon$ is small   =>   correctness is good; running time is slow
* $\epsilon$ is large   =>   correctness is poor; runninig time is fast

<br>



