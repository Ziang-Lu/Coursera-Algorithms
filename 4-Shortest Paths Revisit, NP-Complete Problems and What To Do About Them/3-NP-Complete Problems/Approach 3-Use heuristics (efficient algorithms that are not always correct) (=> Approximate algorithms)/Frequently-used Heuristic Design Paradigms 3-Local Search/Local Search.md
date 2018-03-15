Application of "*Use heuristics (efficient algorithms that are not always correct)*"   => Approximate algorithms

Frequently-used heuristic design paradigms: Local search

### Local Search (局部搜索)

***

**Neighborhood**

Let $X$ be a set of candidate solutions to a problem.

For each $x \in X$, we need to specify which $y \in X$ are its "neighbors".

e.g.,

In the <u>Maximum Cut Problem</u>, $X$ = cuts of the graph; $x$ and $y$ are neighboring cuts <=> $x$ and $y$ differ by moving one vertex $v$.

In the <u>Constraint Satisfaction Problem</u>, $X$ = possible assignments of the variables; $x$ and $y$ are neighboring assignments <=> $x$ and $y$ differ by only one variable.

In the <u>Traveling Salesman Problem</u>, $X$ = TSP tours; $x$ and $y$ are neighboring TSP tours <=> $x$ and $y$ differ by only two edges (by swapping a pair of consecutive cities).

***

Local search just **iteratively improves the current solution** by **moving to a neighboring solution**:

1. Let $x$ be some initial solution

   *How to choose the initial solution?*

   * *Use some heuristic (greedy, dynamic programming, etc.)*

     Use local search algorithm as a post-processing step to make the solution even approximately better.*

   * *Pick a random solution*

     **=> **Note that since local search algorithm returns a locally optimal solution, rather than the globally optimal solution that we want, we can run many independent trials to get the globally optimal solution**.*

2. While the current solution $x$ has a better neighboring solution $y$
   * Set $x = y$

     *How to choose the better neighboring solution $y$?*

     * *Choose the $y$ with the largset improvement*

     * *Use some heuristic (greedy, dynamic programming, etc.)*

     * *Pick a random better neighboring solution*

       *=> **Note that since local search algorithm returns a locally optimal solution, rather than the globally optimal solution that we want, we can run many independent trials to get the globally optimal solution**.*

3. Return the final (locally optimal) solution


