package com.trusanen.dijkstra.heap;

import com.trusanen.dijkstra.models.Node;

import java.util.Arrays;
import java.util.LinkedList;

/**
 * A fast implementation of a minimum binary heap.
 *
 * MinBinaryHeap is a binary heap implementation
 * to be used with the Dijkstra-class.
 *
 * The implementation is optimized for fast insertion and
 * getMin- and decreasePriority-operations. The nodes
 * are designed to be loaded at once to the heap and then
 * extracted one at a time or their priority decreased.
 *
 * Inserting nodes to the heap after the first extraction
 * is not allowed. This is because the heap does not keep
 * track on what nodes have been removed. A queue could be
 * used to track all integers that are available in the 'nodes'-
 * array to have this restriction lifted, but this would impose
 * some performance degradation.
 */
public class MinBinaryHeap {
    int n;
    Node[] nodes;
    int[] heap; // heap[i] <=> index j of node[j] in heap
    boolean locked = false;

    public MinBinaryHeap(int maxN) {
        n = 0; // amount of nodes in heap
        nodes = new Node[maxN];
        heap = new int[maxN];
        Arrays.fill(heap, -1);
    }

    public int size() {
        return n;
    }
    public boolean isEmpty() {
        return n == 0;
    }

    public Node getMin() {
        locked = true;
        exchange(0, n-1);
        Node node = nodes[heap[n-1]];
        nodes[heap[n-1]] = null;
        heap[n-1] = -1; // removed
        node.heapIndex = -1;
        n--;
        sink(0);
        return node;
    }

    boolean contains(int i) {
        return nodes[i] == null ? false : true;
    }

    public void insert(Node node) {
        if (locked) throw new IllegalStateException("Would overwrite a node, not allowed");
        nodes[n] = node;
        heap[n] = n;
        node.heapIndex = n;
        n++;
        bubble(n-1);
    }

    void exchange(int i, int j) {
        int swap = heap[i];
        heap[i] = heap[j];
        heap[j] = swap;
        // Set heap indices
        nodes[heap[i]].heapIndex = i;
        nodes[heap[j]].heapIndex = j;
    }

    Node item(int i) {
        return nodes[i];
    }

    Node heap(int i) {
        return nodes[heap[i]];
    }

    Node left(int i) {
        return nodes[heap[2*i+1]];
    }

    Node right(int i) {
        return nodes[heap[2*i+2]];
    }

    Node parent(int i) {
        return nodes[heap[(i-1)/2]];
    }

    boolean smaller(Node a, Node b) {
        return a.compareTo(b) <= 0;
    }

    void bubble(int i) {
        int curr = i;
        while(curr > 0) {
            if (smaller(heap(curr), parent(curr))) {
                exchange(curr, (curr-1)/2);
                curr = (curr-1)/2;
            } else {
                return;
            }
        }
    }

    void sink(int i) {
        int curr = i;
        // Handles all cases with nodes with 2 children
        while(2*curr+2 < n) {
            Node left = left(curr);
            Node right = right(curr);
            if (smaller(left, right)) {
                if (smaller(left, heap(curr))) {
                    exchange(curr, 2*curr+1);
                    curr = 2*curr+1;
                } else {
                    return;
                }
            } else {
                if (smaller(right, heap(curr))) {
                    exchange(curr, 2*curr+2);
                    curr = 2*curr+2;
                } else {
                    return;
                }
            }
        }
        /*
           Handles the possible case of the last node having only 1 child (left)
        */
        if (2*curr+1 < n && smaller(left(curr), heap(curr))) {
            exchange(curr, 2*curr+1);
        }
    }

    void decreasePriority(int i, double priority) {
        if (heap(i).dist <= priority)
            throw new IllegalArgumentException("New priority not smaller than original!");
        heap(i).dist = priority;
        bubble(i);
    }

    public void decreasePriority(Node node, double priority) {
        decreasePriority(node.heapIndex, priority);
    }

    boolean validate() {
        // validates the heap, O(n), for testing purposes only
        if (isEmpty()) return true;
        LinkedList<Integer> q = new LinkedList<Integer>();
        q.push(0);
        while(!q.isEmpty()) {
            int curr = q.pop();
            if (heap(curr).heapIndex != curr)
                throw new IllegalStateException("Node had wrong heap index at " + curr);
            if (2*curr+1 < n) {
                if (smaller(heap(curr), left(curr)))
                    q.push(2*curr+1);
                else throw new IllegalStateException("Heap relation was not satisfied with " +
                        curr + " and it's left child");
            }
            if (2*curr+2 < n) {
                if (smaller(heap(curr), right(curr)))
                    q.push(2*curr+2);
                else throw new IllegalStateException("Heap relation was not satisfied with " +
                        curr + " and it's right child");
            }
        }
        return true;
    }
}
