### Bloom Filter

A variant of hash table

#### Pros & Cons

Pro:

More space-efficient

Cons:

1. Can only store whether we have seen an object or not, but not the objects themselves or pointers to them

2. No deletion

3. Small false-positive probability

   即实际是false, 但Bloom filter给出的是true — "false-positive"

   e.g., 没有add过某个object (false), 但look-up时, Bloom filter给出的是已经add过了该object (true — "false-positive")



#### Implementation

=> Array of bits ($n$ bits)

(Thus more space-efficient; but can only store whether we have seen an object or not, but not the object themselves or pointers to them)

$k$ hash functions ($h_1$, $h_2$, …, $h_k$)

##### Insertion:

$h_1(x) = index_1$

$h_2(x) = index_2$

...

$h_k(x) = index_k$

=> Set $index_1$, $index_2$, …, $index_k$ to 1

##### Look-up:

$h_1(x) = index_1$

$h_2(x) = index_2$

...

$h_k(x) = index_k$

=> Check if $index_1$, $index_2$, …, $index_k$ are all 1

(Thus small false-positive probability)

