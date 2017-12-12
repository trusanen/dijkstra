package com.trusanen.dijkstra;

import com.trusanen.dijkstra.heap.MinBinaryHeap;
import com.trusanen.dijkstra.models.Edge;
import com.trusanen.dijkstra.models.Graph;
import com.trusanen.dijkstra.models.Node;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

/*
  An implementation of the Dijkstra's shortest path
  algorithm with a binary heap.
  Worst-case performance is O(|E| + |V| * log |V|)
 */
public class Dijkstra {
    Node root;
    Node[] nodes;
    public Dijkstra(Graph g, Node root) {
        this.root = root;
        nodes = g.nodes; // TODO: duplicate nodes

        // Add the edges to the nodes for the nodes to know their
        // neighbourhood
        for (Edge e : g.edges) { // O(|E|)
            e.src.edges.add(e);
            e.dst.edges.add(e);
        }

        // Initialize heap
        MinBinaryHeap q = new MinBinaryHeap(nodes.length);
        for (int i = 0; i < nodes.length ; i++) { // O(|V| * log |V|)
            Node n = nodes[i];
            if (n.equals(root)) n.dist = 0;
            else n.dist = Double.MAX_VALUE;
            q.insert(n); // O(log |V|)
        }

        HashSet<Node> nodesVisited = new HashSet<Node>();
        /*
        *  Loop is bound by O(|E| * T_dp + |V| * T_gm),
        *  where T_dp and T_gm are the time-complexities
        *  of the decreasePriority- and getMin-operations
        *
        *  Thus we have worst-case performance of
        *  O((|E| + |V|) * log |V|)
        */
        while(!q.isEmpty()) {
            Node curr = q.getMin(); // O(log |V|)
            nodesVisited.add(curr); // O(1)

            for (Edge e : curr.edges) {
                Node other = e.getOther(curr);
                if (!nodesVisited.contains(other)) { // O(1)
                    double newDist = curr.dist + e.weight;
                    if (newDist < other.dist) {
                        other.predecessor = curr;
                        q.decreasePriority(other, newDist); // O(log |V|)
                    }
                }
            }
        }
    }

    public List<Node> getPath(int i) {
        List<Node> path = new ArrayList<Node>();
        Node curr = nodes[i];
        do {
            path.add(curr);
            curr = curr.predecessor;
        } while(curr != null);
        Collections.reverse(path);
        return path;
    }
}