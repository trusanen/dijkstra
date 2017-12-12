package com.trusanen.dijkstra.heap;

import com.trusanen.dijkstra.models.Node;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.*;

/**
 * Created by Topias on 24.10.2017.
 */
public class MinBinaryHeapTest {

    double delta = 1e-12;

    private Node insertNode(double priority, MinBinaryHeap q) {
        Node node = new Node(priority,0);
        node.dist = priority;
        q.insert(node);
        return node;
    }

    private double intervalInMs(double tic, double toc) {
        return (toc - tic) * 1e-6;
    }

    @Test
    public void testConstructor() {
        MinBinaryHeap q = new MinBinaryHeap(1);
        assertFalse(q.contains(0));
    }

    @Test
    public void testInsert() {
        // Distances in order, shouldn't do any bubbles
        MinBinaryHeap q = new MinBinaryHeap(3);
        Node n1 = insertNode(1, q);
        assertEquals(1, q.size());
        assertTrue(q.contains(0));

        Node n2 = insertNode(2, q);
        assertEquals(2, q.size());
        assertTrue(q.contains(1));

        Node n3 = insertNode(3, q);
        assertEquals(3, q.size());
        assertTrue(q.contains(2));

        assertEquals(n1, q.item(0));
        assertEquals(n2, q.item(1));
        assertEquals(n3, q.item(2));
    }

    @Test
    public void testBubble() {
        // Distances in reversed order, should do 2 exchanges
        MinBinaryHeap q = new MinBinaryHeap(3);
        Node n1 = insertNode(3, q);
        assertEquals(1, q.size());
        assertTrue(q.contains(0));

        Node n2 = insertNode(2, q);
        assertEquals(2, q.size());

        Node n3 = insertNode(1, q);
        assertEquals(3, q.size());

        assertEquals(n3, q.heap(0));
        assertEquals(n2, q.heap(2));
        assertEquals(n1, q.heap(1));
    }

    @Test
    public void testSink() {
        // Distances in reversed order, should do 2 exchanges
        MinBinaryHeap q = new MinBinaryHeap(3);
        Node n1 = insertNode(3, q);
        Node n2 = insertNode(2, q);
        Node n3 = insertNode(1, q);
        assertEquals(3, q.size());

        assertEquals(n3, q.getMin());
        assertEquals(2, q.size());
        assertEquals(n2, q.heap(0));
        assertEquals(n1, q.heap(1));

        assertEquals(n2, q.getMin());
        assertEquals(1, q.size());
        assertEquals(n1, q.heap(0));

        assertEquals(n1, q.getMin());
        assertEquals(0, q.size());
    }

    @Test
    public void testSinkWithNodeWithOneChild() {
        int n = 2*2*2 + 1;
        MinBinaryHeap q = new MinBinaryHeap(n);
        ArrayList<Node> nodes = new ArrayList<Node>();
        for (int i = 0 ; i < n ; i++) {
            nodes.add(insertNode(i, q));
            assertEquals(i+1, q.size());
            assertTrue(q.contains(i));
        }

        for (int i = 0 ; i < n ; i++) {
            assertEquals(nodes.get(i), q.heap(i));
        }

        assertEquals(nodes.get(0), q.getMin());
        assertFalse(q.contains(0));

        assertEquals(nodes.get(1), q.heap(0));
        assertEquals(nodes.get(3), q.heap(1));
        assertEquals(nodes.get(2), q.heap(2));
        assertEquals(nodes.get(7), q.heap(3));
        assertEquals(nodes.get(4), q.heap(4));
        assertEquals(nodes.get(5), q.heap(5));
        assertEquals(nodes.get(6), q.heap(6));
        assertEquals(nodes.get(8), q.heap(7));
    }

    @Test
    public void testWithLargeNumberOfNodes() {
        long tic;
        long toc;

        int n = 1000000;
        MinBinaryHeap q = new MinBinaryHeap(n);
        double[] priorities = new double[n];
        System.out.println("Creating " + n + " random numbers...");
        for (int i = 0 ; i < n ; i++) {
            double dist = Math.random()*1000;
            priorities[i] = dist;
        }

        System.out.println("Inserting nodes to heap...");
        tic = System.nanoTime();
        for (int i = 0 ; i < n ; i++) {
            insertNode(priorities[i], q);
        }
        toc = System.nanoTime();
        System.out.println("Took " + intervalInMs(tic, toc) + " ms");
        assertTrue(q.validate());

        System.out.println("Sorting random numbers for checking...");
        Arrays.sort(priorities);

        System.out.println("Extracting all nodes one at a time...");
        tic = System.nanoTime();
        for (int i = 0 ; i < n ; i++) {
            assertEquals(priorities[i], q.getMin().dist, delta);
        }
        toc = System.nanoTime();
        System.out.println("Took " + intervalInMs(tic, toc) + " ms");
    }

    @Test
    public void testDecreasePriority() {
        int n = 7;
        MinBinaryHeap q = new MinBinaryHeap(n);
        ArrayList<Node> nodes = new ArrayList<Node>();
        for (int i = 0 ; i < n ; i++) {
            nodes.add(insertNode(i+2, q));
            assertEquals(i+1, q.size());
            assertTrue(q.contains(i));
        }

        for (int i = 0 ; i < n ; i++) {
            assertEquals(nodes.get(i), q.heap(i));
        }

        q.decreasePriority(6, 1);

        // Left side of the heap should stay unmodified
        assertEquals(nodes.get(6), q.heap(0));
        assertEquals(nodes.get(1), q.heap(1));
        assertEquals(nodes.get(0), q.heap(2));
        assertEquals(nodes.get(3), q.heap(3));
        assertEquals(nodes.get(4), q.heap(4));
        assertEquals(nodes.get(5), q.heap(5));
        assertEquals(nodes.get(2), q.heap(6));
    }

    @Test(expected = IllegalStateException.class)
    public void testInsertAfterExtraction() {
        int n = 1000;
        MinBinaryHeap q = new MinBinaryHeap(n);
        ArrayList<Node> nodes = new ArrayList<Node>();
        double maxPriority = 1000;
        for (int i = 0 ; i < n ; i++) {
            nodes.add(insertNode(Math.random()*maxPriority, q));
            assertTrue(q.validate());
        }

        q.getMin();
        assertTrue(q.validate());
        // Would cause an invalid state of the heap,
        // should throw an exception
        insertNode(0, q);
        assertTrue(q.validate());
    }

    private MinBinaryHeap createRandomHeap() {
        int n = (int)(1000 + Math.random()*9000);
        System.out.println("Creating heap with size " + n);
        MinBinaryHeap q = new MinBinaryHeap(n);
        double maxPriority = 1000;
        for (int i = 0 ; i < (int)(0.5*n + 0.3*Math.random()*n) ; i++) {
            insertNode(Math.random()*maxPriority, q);
        }
        assertTrue(q.validate());
        // Run random operations to heap
        int operations = (int)Math.floor(Math.random()*5000);
        int delMins = 0;
        double delMinSum = 0;
        int decrKeys = 0;
        double decrKeysSum = 0;
        int illegalDecreases = 0;
        long tic; long toc;
        System.out.println("Running random operations on heap...");
        for (int i = 0 ; i < operations ; i++) {
            switch((int)(Math.random()*3)) {
                case 0:
                    if (!q.isEmpty()) {
                        delMins++;
                        tic = System.nanoTime();
                        q.getMin();
                        toc = System.nanoTime();
                        delMinSum += intervalInMs(tic, toc);
                        assertTrue(q.validate());
                    }
                case 1:
                    if (!q.isEmpty()) {
                        try {
                            int index = (int)(Math.random()*(q.size()-1));
                            double priority = Math.random()*maxPriority;
                            tic = System.nanoTime();
                            q.decreasePriority(index, priority);
                            toc = System.nanoTime();
                            decrKeysSum += intervalInMs(tic, toc);
                            decrKeys++;
                        } catch(IllegalArgumentException e) {
                            illegalDecreases++;
                        }
                        assertTrue(q.validate());
                    }
            }
        }
        System.out.println("Did " + delMins + " extractions");
        System.out.println("Extraction took on average: " + (delMinSum)/delMins + " ms");
        System.out.println("Did " + decrKeys + " decreases and " + illegalDecreases + " invalid operations");
        System.out.println("Decrease key took on average: " + (decrKeysSum)/decrKeys + " ms");
        return q;
    }

    @Test
    public void testRandomHeaps() {
        int times = 10;
        MinBinaryHeap q;
        for (int i = 0 ; i < times ; i++) {
            q = createRandomHeap();
            assertTrue(q.validate());
        }
    }

    @Test
    public void testInsertWithRandomPriorities() {
        int n = 1001;
        MinBinaryHeap q = new MinBinaryHeap(n);
        double maxPriority = 1000;
        for (int i = 0 ; i < n ; i++) {
            insertNode(Math.random()*maxPriority, q);
            assertTrue(q.validate());
        }
    }

    @Test
    public void testNodeDecreasePriority() {
        int n = 1001;
        MinBinaryHeap q = new MinBinaryHeap(n);
        ArrayList<Node> nodes = new ArrayList<Node>();
        double maxPriority = 1000;
        for (int i = 0 ; i < n ; i++) {
            nodes.add(insertNode(Math.random()*maxPriority, q));
            assertTrue(q.validate());
        }

        for (int i = 0 ; i < n ; i++) {
            assertEquals(i, q.heap(i).heapIndex);
        }

        for (int i = 0 ; i < n ; i++) {
            Node node = q.heap(q.size()-1);
            q.decreasePriority(node, 0);
            assertTrue(q.validate());
            assertEquals(node, q.getMin());
            assertTrue(q.validate());
        }
    }
}
