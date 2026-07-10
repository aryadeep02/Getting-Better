# Binary Search - Complete Theory (0 → Pro)

> Binary Search is one of the most important algorithms in computer science.
>
> Beginners think Binary Search is only used to search an element in a sorted array.
> Professionals know Binary Search is actually a technique for reducing the search space by half until the answer is found.

---

# Table of Contents

1. Introduction
2. Why Binary Search?
3. Prerequisites
4. Working Principle
5. Time Complexity
6. Search Space
7. Binary Search Algorithm
8. Mid Calculation
9. Loop Invariant
10. Why Binary Search Works
11. Binary Search Templates
12. Types of Binary Search
13. Boundary Searching
14. Binary Search on Answer
15. Common Mistakes
16. Dry Run
17. Applications
18. Interview Tips

---

# 1. Introduction

Imagine you have a dictionary containing one million words.

If you start checking every word from the beginning,

```
Apple
Ball
Cat
Dog
...
Zebra
```

this is **Linear Search**.

Worst Case

```
1,000,000 comparisons
```

Instead,

You open the dictionary roughly in the middle.

If your target word comes before the current page,

discard the second half.

Otherwise,

discard the first half.

Repeat the process.

This idea is called **Binary Search**.

Binary means **two**.

Every comparison divides the search space into **two parts**.

---

# 2. Why Binary Search?

Suppose

```
n = 1,000,000
```

Linear Search

```
Worst Case = 1,000,000 comparisons
```

Binary Search

```
log₂(1,000,000)

≈ 20 comparisons
```

Just twenty decisions instead of one million.

That is why Binary Search is one of the fastest searching algorithms.

---

# 3. Prerequisites

Binary Search requires one very important property.

## Sorted Data

```
1 3 5 7 9 11 15
```

Because the data is sorted,

after comparing with the middle element,

we immediately know which half can never contain the answer.

Without ordering,

Binary Search cannot safely discard half the elements.

---

# 4. Working Principle

Suppose

```
1 3 5 7 9 11 13
```

Target

```
11
```

Step 1

```
Middle = 7
```

Target is larger.

Discard left half.

Remaining

```
9 11 13
```

Middle

```
11
```

Found.

Notice

Only two comparisons were required.

---

# 5. Time Complexity

Every iteration removes half the elements.

```
n

↓

n/2

↓

n/4

↓

n/8

↓

...
```

After k divisions,

```
n / 2^k = 1
```

Therefore,

```
k = log₂ n
```

Time Complexity

```
O(log n)
```

Space Complexity

```
O(1)
```

---

# 6. Search Space

The most important concept in Binary Search is the **search space**.

Search space simply means

> "Where can the answer still exist?"

Initially

```
Entire Array
```

After each comparison,

half of the search space is eliminated.

Eventually,

only one possible answer remains.

Professional programmers think in terms of shrinking the search space—not searching the array.

---

# 7. Binary Search Algorithm

Maintain two pointers.

```
Left

Right
```

Initially

```
Left = first index

Right = last index
```

Repeat until

```
Left > Right
```

At every iteration

1. Find middle
2. Compare middle with target
3. Eliminate one half
4. Continue on the remaining half

---

# 8. Why Not (left + right) / 2 ?

Suppose

```
left = 2,000,000,000

right = 2,100,000,000
```

Adding them exceeds the maximum value of an integer.

This causes integer overflow.

Therefore,

always use

```
left + (right - left) / 2
```

which produces the same middle without overflow.

---

# 9. Loop Invariant

A loop invariant is a condition that remains true during every iteration.

For Binary Search,

the invariant is

> "If the target exists, it is always inside the current search space."

Initially,

the search space is the whole array.

After every comparison,

only impossible positions are removed.

Therefore,

the target is never accidentally discarded.

This is why Binary Search is correct.

---

# 10. Why Binary Search Works

Suppose

```
Array

2 5 7 10 15 20
```

Middle

```
7
```

Target

```
15
```

Since

```
15 > 7
```

every number before 7 is also smaller than 15.

Those numbers can never contain the answer.

Hence,

discard the entire left half.

This logic is only valid because the array is sorted.

---

# 11. Binary Search Templates

There is no single Binary Search.

Different problems require different templates.

The common ones are

- Exact Search
- First Occurrence
- Last Occurrence
- Lower Bound
- Upper Bound
- Binary Search on Answer
- Rotated Array Search

Mastering these templates solves almost every Binary Search interview problem.

---

# 12. Types of Binary Search

## Exact Search

Goal

Find one occurrence of the target.

---

## First Occurrence

If duplicates exist,

find the leftmost occurrence.

---

## Last Occurrence

If duplicates exist,

find the rightmost occurrence.

---

## Lower Bound

Find the first value

```
>= target
```

---

## Upper Bound

Find the first value

```
> target
```

---

## Binary Search on Answer

Instead of searching an array,

search the answer itself.

Examples

- Minimum speed
- Minimum capacity
- Maximum distance
- Smallest divisor

The answer space is monotonic,

making Binary Search possible.

---

# 13. Boundary Searching

Many interview questions are actually boundary problems.

Instead of asking

```
Where is x?
```

they ask

```
Where does x begin?

Where does x end?
```

These require slight modifications to standard Binary Search.

---

# 14. Binary Search on Answer

This is one of the most powerful applications.

Suppose the answer lies between

```
1 and 1,000,000
```

Instead of checking every value,

Binary Search tests a candidate answer.

If it satisfies the condition,

search for an even better answer.

Otherwise,

search the opposite half.

This converts optimization problems into Binary Search problems.

---

# 15. Common Mistakes

### Mistake 1

Using

```
(left + right) / 2
```

Use the overflow-safe formula instead.

---

### Mistake 2

Infinite loop.

Always ensure

```
Left

or

Right

actually moves.
```

---

### Mistake 3

Wrong stopping condition.

Understand when to use

```
Left <= Right
```

and

```
Left < Right
```

depending on the template.

---

### Mistake 4

Returning immediately when duplicates exist.

Boundary problems require continuing the search.

---

### Mistake 5

Trying Binary Search on unsorted data.

Sorting changes indices,

which may invalidate the question.

---

# 16. Dry Run

Array

```
2 4 6 8 10 12 14
```

Target

```
10
```

Iteration 1

```
Middle = 8

Target larger

Discard left
```

Iteration 2

```
Middle = 12

Target smaller

Discard right
```

Iteration 3

```
Middle = 10

Found
```

Only three comparisons.

---

# 17. Applications

Binary Search appears in

- Searching
- Databases
- Version Control
- Competitive Programming
- Operating Systems
- Networking
- AI Optimization
- Scheduling
- Load Balancing
- Game Development
- Machine Learning Hyperparameter Search

It is one of the most widely used algorithms in software engineering.

---

# 18. Interview Tips

When reading a problem, ask yourself:

✅ Is the data sorted?

✅ Can I eliminate half the search space?

✅ Is the answer monotonic?

✅ Am I searching an index or an answer?

✅ Is this a boundary problem?

If the answer to any of these is "yes",

Binary Search is likely the correct approach.

---

# Summary

Binary Search is not simply an algorithm for searching arrays.

It is a general problem-solving technique based on repeatedly halving a valid search space.

The most valuable skill is not memorizing code.

The most valuable skill is recognizing when a problem's search space is monotonic and can be reduced by half after every comparison.

Once this intuition develops, Binary Search becomes applicable far beyond searching in sorted arrays.