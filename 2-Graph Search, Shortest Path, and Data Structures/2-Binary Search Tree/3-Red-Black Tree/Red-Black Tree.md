### Red-Black Tree

#### Invariants

1. Each node is either **red** or **black**.

   Store one bit more information at each node, indicating whether it's a red node, or a black node

2. The **root** is always **black**.

3. **Never** allow **two red nodes in a row**

   i.e., If you have a red node in the Red-Black Tree, then its parent and children must all be black.

4. Every **root-null path** passes exact **the same number of black nodes**.

   i.e., The number of black nodes that a root-null path passes cannot depend on the path.



#### Height Guarantee

##### Claim: Every Red-Black Tree with $n$ nodes has height O($log_2 n$).

##### Proof:

Observation:

For every BST, if every root-null path has $\ge k$ nodes, then the top of this tree has to be totally filled in.

i.e., The top of this tree has to include a perfectly balanced tree of depth $(k-1)$. 即至少前k个level是full的

Proof:

Assume the tree misses some nodes in these top $k$ levels, then there exists a way of hitting a null seeing less than $k$ nodes. => Contradiction!
$$
n \ge 2^k-1
$$

$$
k \le log_2(n+1)
$$



For a Red-Black Tree with $n$ nodes (some red, some black), each root-null path having $m_{black}$ black nodes (Invarient #4), we have
$$
m_{black} \le k \le log_2 (n + 1)
$$
for the shorted root-null path. Thus,
$$
m_{black} \le log_2 (n + 1)
$$
According to Invariant #3,
$$
Max \ height \le m_{red} + m_{black} \le 2m_{black} \le 2log_2(n + 1)
$$
where Max Height is exactly the longest root-null path.

(A Red-Black Tree has to look like a perfectly balanced tree with at most a factor of 2 "inflation".)