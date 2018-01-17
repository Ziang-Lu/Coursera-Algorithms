### Problem: Every hash function has its kryptonite.

If we fix a single hash function, then there is a dataset that breaks the hash function.

People can (accidentally / **intentionally**) paralyze a real-world system which uses hash table.

=> Analyze the has function, and **reverse engineer** a **pathological dataset** to paralyze the hash table.



### Question: How to design a "safe" (thus good - spread out the data evenly [good hashing performance]) hash function?



#### Solution 1: Cryptographic hash function

It **does have** its pathological dataset.

=> BUT it's **infeasible to reverse engineer** its pathological dataset.



#### Solution 2: Universal hashing

1. Design **a family of hash functions**
2. At **runtime**, pick one of them **at random** (Randomization)
3. By inspecting the code, you'll **have no idea what was the real-time random choice of a hash function**, i.e., you'll **know nothing** about what was the **actual hash function**.
4. You'll **won't be able to reverse engineer** a pathological dataset.

For each **fixed dataset**, this **random choice of a hash function** will **do well** on that dataset **on average**.



#### Universal hash functions:

##### <u>Definition</u>:

For a fixed universe $U$ and number of buckets in the hash table $n$, a family of hash functions $H$ is universal iff

for all $x, \ y \in U \ (x \neq y)$,
$$
Pr_{h\in H}[x,y \ collide \iff h(x)=h(y)] \le \frac{1}{n}
$$
i.e., collision probability as small as with "gold standard" of perfectly random hashing

##### Constant time guarantee with <u>separate chaining</u>:

##### <u>Theorem</u>:

If the hash function $h$ is chosen **uniformly at random** from the universal family $H$, and the hash table is implemented with **separate chaining**, then all operations **run in O(1) time**, i.e., **expectation** of all operations are O(1) **on average**, **regardless of the universe**.

##### Proof

(We will analyze a worst-case: an unsuccessful look-up; other operations are only faster.)

(We are making no assumption about the dataset.)



Let $S$ = dataset, consider looking up for $x \notin S$.

Running time =

O(1) (Use the has function to find the bucket) +

O(length of the list in that bucket) (Search through the list in that bucket, and end up with an unsuccessful look-up)



Denote: $L$ = length of the list in that bucket

Thus, $L$ is a random variable depending on the random choice of $h$.



=> E[running time] = E[L]



Denote: For $y \in S$ (=> $y \ne x$), define $z_y$ = 1 if $x, y$ collide ($h(x) = h(y)$), and 0 otherwise.



=> $L = \Sigma_{y \in S}z_y$

=> $E[L] = E[\Sigma_{y \in S}z_y]$   (Linearity of Expectatation)   => $\Sigma_{y \in S}E[z_y] = \Sigma_{y \in S}Pr[z_y = 1] = \Sigma_{y \in S}Pr[h(y) = h(x)]$

According to the definition of universal hash functions,
$$
Pr_{h \in H}[h(x) = h(y)] \le \frac{1}{n}
$$
=> **E[running time] $\le \Sigma_{y \in S}\frac{1}{n} = \frac{m}{n}$ (load factor), where $m$ is the number of objects in $S$.**

Since we have $m$ and $n$ of roughly similar size, E[running time] $\le$ O(1).

(As long as you keep the load factor under control, so the number of buckets is commensurate witht he size of the dataset that you're storing, you get a hash table with universal hashing and separate chaining that guarantee O(1) expected running time.)