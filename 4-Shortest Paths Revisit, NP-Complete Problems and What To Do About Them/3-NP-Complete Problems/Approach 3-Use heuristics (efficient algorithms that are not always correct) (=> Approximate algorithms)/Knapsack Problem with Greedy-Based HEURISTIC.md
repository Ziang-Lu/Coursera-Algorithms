Application of "*Use heuristics (efficient algorithms that are not always correct)*"   => Approximate algorithms

### Knapsack Problem with Greedy-Based HEURISTIC

*Motivation: Ideal items should have large value and small size.*

=> Similar to the **scheduling problem**

#### Algorithm: (Greedy)

1. Sort the items by $\frac{v_i}{w_i}$

2. Pack items in this order. If one doesn't fit, skip it and continue.

   *This single greedy heuristic can be arbitrarily bad relative to the optimal solution.*

   *Why? In the scheduling problem, this greedy algorithm is always correct because it doesn't have the restriction similar to a knapsack capacity. However in the knapsack problem, due to this restriction, the greedy algorithm is not always guaranteed to be correct.*

   *To fix this issue, we add the following second greedy heuristic.*

3. Sort the items again simply by $v_i$

4. Same as 2

5. Return whichever is better between the two greedy packings