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

### Advanced Union-Find -> "Lazy Union"

**Goal: Only update a single pointer in each ``union()`` operation**

=> When two groups merge, make one group's leader (i.e., root of the tree) point to the other leader (to be a child of the root of the other tree).

Pros:

``union()`` operation reduces to two ``find()`` operations and one link operation:

```
rx, ry = find(x), find(y)
Link rx and ry in O(1) time
```

Cons:

``find()`` operation needs to follow a path of parent pointers until the root, which is pointing to itself.

<br>

**Problem: Which group to change the leader?**

<br>

#### Optimization 1: Union-by-Rank (from the idea of the previous practical optimization)

Look at which of the two trees is already deeper, and we want to keep the root of that deeper tree, so we install the root of the shallower tree as the child of the deeper one.

=> For each object, maintain a ``rank`` field, which is the maximum number of hops from some leaf of this object's tree to this object.

=> The rank of an object is 1 + max rank of its children.