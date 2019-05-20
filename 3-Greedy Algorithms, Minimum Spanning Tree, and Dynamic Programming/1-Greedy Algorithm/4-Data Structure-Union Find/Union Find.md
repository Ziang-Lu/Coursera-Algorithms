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

  **O(1)**

##### Keypoint #2 (Practical optimization)

In order to reduce the number of leader updates when two subsets "merge", let the smaller subset inherit the leader of the larger one.

Maintain a ``size`` field for each subset.

* ``union(Ca, Cb)``

  Update the leader of the objects in $C_b$ ($C_a$) to the leader of $C_a$ ($C_b$).

  **O(n)**

If we merge the sole subsets one by one to the single entire set, like in Kruskal's MST Algorithm, orignally we would think it involves **O(mn)** leader updates; however, we can change to a **"vertex-centric view"**:

Consider the number of leader updates for a single vertex:

Every time the leader of this vertex gets updated, the size of its connected component at least doubles, so suppose it experiences $x$ leader updates in total, we have
$$
2^x \le n \\
x \le log_2n
$$
Thus, each vertex experiences O(log n) leader updates, leading to a **O(nlog n)** leader updates in total.

<br>

### Advanced Union-Find -> "Lazy Union"

**Goal: Only update a single pointer in each ``union()`` operation**

=> When two groups merge, make one group's leader (i.e., root of the tree) point to the other leader (to be a child of the root of the other tree).

**Pros:**

``union()`` operation reduces to two ``find()`` operations and one link operation: (**O(n)**)

```
rx, ry = find(x), find(y)
Link rx and ry in O(1) time
```

**Cons:**

``find()`` operation needs to follow a path of parent pointers until the root, which points to itself. (**O(n)**)

<br>

#### Optimization 1: Union-by-Rank (from the idea of the previous practical optimization)

Look at which of the two trees is already deeper, and we want to keep the root of that deeper tree, so we install the root of the shallower tree as the child of the deeper one.

=> For each object, maintain a ``rank`` field, which is the maximum number of hops from some leaf of this object's tree to this object.

=> The rank of an object is 1 + maximum rank of its children.

=> Both ``find()`` and ``union()`` operations: O(r), where r is the rank of the root that the object belongs to

<br>

**Claim: With Union-by-Rank optimization, the maximum rank for any object is O(log n).**

Proof:

***

Claim: The subtree of a rank-$r$ object has size $\ge 2^r$.

Proof: by induction on the number of ``union()`` operations

<u>Base case:</u> Initially all ranks are 0, and all subtree sizes are 1 ($2^0$).

<u>Inductive hypothesis:</u> same as the claim.

<u>Inductive step:</u>

Nothing to prove when the ranks of all obejcts don't change. (Subtree sizes only go up.)

When one of the ranks changes (i.e., when the two groups has roots of the same rank $r$), the new rank is $r + 1$, and the new subtree size is $\ge 2^r + 2^r = 2 * 2^r = 2^{r+1} = 2^{new \ rank}$.

***

Thus, the maximum possible rank $r_{max} = log_2 n$.

<br>

=> **Both ``find()`` and ``union()`` operations: O(log n), where r is the rank of the root of the tree that the object belongs to**

<br>

**Problem: Why bother traversing a leaf-root path multiple times?**

<br>

#### Optimization 2: Path Compression

After a ``find(x)`` operation, install shortcuts (i.e., rewire parent pointers) to $x$'s root all along the $x$~root path.

=> In effect, path compression makes the tree shallower and more bushy.   *(=> 最终回到了Eager Union的效果)*

=> **Speed up subsequent ``find(x)`` operations towards $x$**

<br>

<u>Important: Maintain all ranks EXACTLY as WITHOUT path compression!</u>

=> Now the rank of an object only serves as an upper bound of the maximum number of hops on a path from a leaf to that object, rather than the previous equality.
