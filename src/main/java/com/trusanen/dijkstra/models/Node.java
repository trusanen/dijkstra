package com.trusanen.dijkstra.models;

import java.util.ArrayList;
import java.util.List;

public class Node implements Comparable<Node> {
    final double x;
    final double y;

    public Node(double x, double y) {
        this.edges = new ArrayList<>();
        this.dist = -1;
        this.heapIndex = -1;
        this.x = x;
        this.y = y;
    }

    public double dist;
    public Node predecessor;
    public List<Edge> edges;
    public int heapIndex;

    @Override
    public int compareTo(Node o) {
        return Double.compare(this.dist, o.dist);
    }
}
