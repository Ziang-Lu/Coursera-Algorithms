Application of "*Use heuristics (efficient algorithms that are not always correct)*"   => Approximate algorithms

Frequently-used heuristic design paradigm: Dynamic programming

### Knapsack Problem with Dynamic Programming-Based Heuristic

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

Since there are O($n \cdot \Sigma v_i$) $\le$ O($n \cdot n v_{max}$) = $O(n^2 v_{max})$ subproblems, and for each problem, we only do constant amout of work, the overall running time complexity is **O($n^2 v_{max}$)**.

Note that the running time still runs exponentially in the input size (the number of encoding bits of the maximum value $v_{max}$) as explained previously, but it much faster than the O($2^n$) exponential-time brute-force search.

***

<br>

With heuristics, we shoot for **arbitrarily good approximation**.

For a **user-specified parameter $\epsilon \in (0,1)$**, guarantee a **$(1 - \epsilon)$ approximation**, i.e., $\ge (1 - \epsilon)$ * optimal solution.

This naturally forms a trade-off between running time and correctness:

* $\epsilon$ is small   =>   correctness is good; running time is slow
* $\epsilon$ is large   =>   correctness is poor; runninig time is fast

<br>

#### Dynamic Programming-Based Heuristic

According to the above dynamic programming algorithm, if the **input values are all not too big, i.e., in O($n^k$)**, then the algorithm runs in **O($n^{k + 2}$)**, which is **polynomial-time** and thus efficient.

Thus, we can propose a heuristic that **rounding down the input values**, in order to get these "not too big" values, as follows.

1. Perform the transformation

   **Choose a parameter $m$, and round the input values to the nearest multiple of $m$: $\hat{v_i} = [\frac{v_i}{m}]$**

   *Intuitive choice of $m$:*

   *$m$ is small   =>   throw less information   =>   correctness is good*

   *$m$ is large   =>   throw more information   =>   correctness is poor*

2. Feed the rounded values, the original weights and the knapsack capacity into the above algorithm, and get the included items in the optimal solution

   *Intuitive choice of $m$:*

   *$m$ is small   =>   $v_{max}$ is large   =>   running time is slow*

   *$m$ is large   =>   $v_{max} is small$   =>   running time is fast*

*Therefore, $m$ should be a function of $\epsilon$, i.e., $m(\epsilon)$:*

* *$\epsilon$ is small   =>   $m$ is small*
* *$\epsilon$ is large   =>   $m$ is large*

Since $\hat{v_i} = [\frac{v_i}{m}]$, we have
$$
v_i - m \le m\hat{v_i} \le v_i
$$
Note that the algorithm always correctly the included items in the optimal solution, but not necessarily the optimal solution value itself. Denote the optimal solution as $S$, and out heuristic solution as $S^*$. Thus, we have
$$
\Sigma_{i \in S*} \ \hat{v_i} \le \Sigma_{i \in S} \ \hat{v_i}
$$
Multiplying both side by $m$, and applying the previous inequality, we get
$$
\Sigma_{i \in S^*} \ v_i - km \ \le \ m\Sigma_{i \in S^*} \ \hat{v_i} \ \le \ m\Sigma_{i \in S} \ \hat{v_i} \ \le \ \Sigma_{i \in S} \ v_i
$$
so
$$
\Sigma_{i \in S^*} \ v_i - km \ \le \ \Sigma_{i \in S} \ v_i
$$
i.e.,
$$
\hat{OPT} - km \ \le \ OPT \\
\hat{OPT} - OPT \ \le \ km \ \le nm \ \le \ \epsilon OPT
$$
Assuming that all items fit in the knapsack, we have the lower bound of $OPT$ as $v_{max}$, we just need to let
$$
nm \ \le \ \epsilon v_{max}
$$
i.e.,
$$
m \ \le \ \frac{\epsilon v_{max}}{n}
$$
In order words, guaranteeing ($1 - \epsilon$) approximation, we can set $m$ to be as large as $\frac{\epsilon v_{max}}{n}$, in order to get better running time, which is reduced to
$$
O(n^2 \hat{v_{max}}) \ \le \ O(n^2\frac{v_{max}}{m}) \ = \ O(\frac{n^3}{\epsilon})
$$
