package com.trusanen.dijkstra;

import com.trusanen.dijkstra.models.Edge;
import com.trusanen.dijkstra.models.Graph;
import com.trusanen.dijkstra.models.Node;
import org.junit.Test;

import java.util.List;

/**
 * Created by Topias on 24.10.2017.
 */
public class DijkstraTest {
    @Test
    public void getPath() throws Exception {

        Node a = new Node(0,0);
        Node b = new Node(1,0);
        Node c = new Node(2,0);
        Node d = new Node(3,1);
        Node e = new Node(4,2);
        Node f = new Node(3,-1);

        Node[] nodes = {a, b, c, d, e, f};

        Edge aToB = new Edge(0, a, b);
        Edge bToC = new Edge(1, b, c);
        Edge cToD = new Edge(2, c, d);
        Edge dToE = new Edge(3, d, e);
        Edge cToF = new Edge(4, c, f);

        Edge[] edges = {aToB, bToC, cToD, dToE, cToF};

        Graph g = new Graph(nodes, edges);

        Dijkstra alg = new Dijkstra(g, a);

        List<Node> pathAtoA = alg.getPath(0);
        assert(pathAtoA.size() == 1);
        assert(pathAtoA.get(0).equals(a));

        List<Node> pathAToE = alg.getPath(4);

        assert(pathAToE.size() == 5);
        assert(pathAToE.get(0) == nodes[0]);
        assert(pathAToE.get(1) == nodes[1]);
        assert(pathAToE.get(2) == nodes[2]);
        assert(pathAToE.get(3) == nodes[3]);
        assert(pathAToE.get(4) == nodes[4]);

        List<Node> pathAToF = alg.getPath(5);

        assert(pathAToF.size() == 4);
        assert(pathAToF.get(0) == nodes[0]);
        assert(pathAToF.get(1) == nodes[1]);
        assert(pathAToF.get(2) == nodes[2]);
        assert(pathAToF.get(3) == nodes[5]);
    }

    private Graph getRandomGraph(int n, int e) {
        Node[] nodes = new Node[n];
        for (int i = 0 ; i < n ; i++) {
            nodes[i] = new Node(Math.random()*2, Math.random()*2);
        }

        Edge[] edges = new Edge[e];
        for (int i = 0 ; i < e ; i++) {
            Node src = nodes[(int)(Math.random()*n-1)];
            Node dst = nodes[(int)(Math.random()*n-1)];
            edges[i] = new Edge(i, src, dst);
        }

        return new Graph(nodes, edges);
    }

    // TODO: more tests to verify the returned paths are correct

    @Test
    public void testRandomPaths() {
        int times = 10;
        long tic; long toc; double ts = 0;
        int nodes = 200000;
        int edges = 400000;
        for (int i = 0 ; i < times ; i++) {
            Graph g = getRandomGraph(nodes, edges);
            Node target = g.nodes[(int)(Math.random()*g.nodes.length-1)];
            tic = System.nanoTime();
            Dijkstra alg = new Dijkstra(g, target);
            toc = System.nanoTime();
            ts += (toc - tic) * 1e-6;
        }
        System.out.println("Did " + times + " runs of Dijkstra with a graph of "
                + nodes + " nodes and " + edges + " edges");
        System.out.println("Running Dijkstra took on average: " + ts/times + " ms");
    }
}