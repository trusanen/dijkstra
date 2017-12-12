package com.trusanen.dijkstra.models;

/**
 * Created by Topias on 24.10.2017.
 */
public class Graph {
    public Node[] nodes;
    public Edge[] edges;

    public Graph(Node[] nodes, Edge[] edges) {
        this.nodes = nodes;
        this.edges = edges;
    }
}