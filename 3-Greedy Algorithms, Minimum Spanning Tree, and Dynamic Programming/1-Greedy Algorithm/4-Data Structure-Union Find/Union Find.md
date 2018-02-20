### Data Structure - Union-Find

Maintain a **partition** of a set of objects

e.g., $C_1$, $C_2$, $C_3$ and $C_4$ denote disjoint subsets that together form the union of the entire set.

#### Supported operations

* ``find(x)``

  Returns the name of the group that the given object belongs to.

  e.g., Suppose ``x`` is an object in $C_3$, then ``find(x)`` should return ``"C3"``.


* ``union(Ca, Cb)``

  Fuses the given two groups together, i.e., objects in the first group and objects in the second group should all coalesce, and be now in one single group.

<br>

#### Implementation

##### Keypoint #1

*Maintain a linked structure per subset, and each subset has an arbitrary leader (representative of the subset) object.*

Invariant:

Each object points to the leader of its subset, i.e., each object "remembers" which subset it belongs to.

* ``find(x)``

  Simply return the leader of $x$.

  O(1)

##### Keypoint #2 (Practical optimization)

In order to reduce the number of leader updates when two subsets "merge", let the smaller subset inherit the leader of the larger one.

Maintain a ``size`` field for each subset.

* ``union(Ca, Cb)``

  Update the leader of the objects in $C_b$ ($C_a$) to the leader of $C_a$ ($C_b$).

  **O(n)**

If we merge the sole subsets one by one to the single entire set, like in Kruskal's MST Algorithm, orignally we would think it involves **O(mn)** leader updates; however, we can change to a **"vertex-centric view"**:

Consider the number of leader updates for a single vertex:

Every time the leader of this vertex gets updated, the size of its connected components at least doubles, so suppose it experiences $x$ leader updates in total, we have
$$
2^x \le n \\
x \le log_2n
$$
Thus, each vertex experiences O(log n) leader updates, leading to a **O(nlog n)** leader updates in total.

<br>

### Advanced Union-Find

#### Lazy Union