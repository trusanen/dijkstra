package com.trusanen.dijkstra.models;
public class Edge {

    private final int id;

    public final Node src;
    public final Node dst;
    public final float weight;

    public Edge(int id, Node begin, Node end) {
        this.id = id;

        src = begin;
        dst = end;
        weight = (float)Math.sqrt(Math.pow(begin.x - end.x,2) + Math.pow(begin.y - end.y,2));
    }

    public Node getOther(Node n) {
        if (src.equals(n)) return dst;
        else if (dst.equals(n)) return src;
        return null;
    }
}