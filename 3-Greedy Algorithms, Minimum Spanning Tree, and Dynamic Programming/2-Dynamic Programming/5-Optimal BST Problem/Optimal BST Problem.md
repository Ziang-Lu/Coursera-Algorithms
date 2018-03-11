### Optimal BST Problem

Originally, a perfectly balanced BST, like a Red-Black Tree will provide the best performance (O(log n)) when frequency of access is uniform (or known, and thus must be infered as uniform).

***

Inspiration from Huffman encoding:

When the frequency distribution of the characters is non-uniform, we can use variable-length encoding to better compress the overall encoding length.

***

=> When frequency of access if non-uniform, the perfectly balanced BST **NEED NOT** be the best BST.

i.e., You might want to have a possibly unbalanced BST, with the more frequently accessed objects closer to the root, to get a better overall search time.



**Input:**

Items 1, 2, …, $n$ (assume in sorted order), with positive frequencies $p_1$, $p_2$, …, $p_n$

**Output:**

Compute a valid BST that minimize the **weighted** search time (WST):
$$
C(T) = \Sigma_i p_i * [search \ time \ for \ item-i]
$$
where the search time for item-i is defined to be the number of nodes you have to visit until you find it (inclusive).

