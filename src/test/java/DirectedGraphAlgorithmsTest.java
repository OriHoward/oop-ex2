import api.DirectedWeightedGraphAlgorithms;
import api.NodeData;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class DirectedGraphAlgorithmsTest {

    static DirectedGraphAlgorithms algos;
    static final double EPSILON = 0.0000000001;

    @BeforeAll
    static void init() {
        algos = new DirectedGraphAlgorithms();
    }


    @org.junit.jupiter.api.Test
    void isConnected() {
        algos.load("dataTests/notConnected.json");
        assertFalse(algos.isConnected());
        algos.load("dataTests/isConnected.json");
        assertTrue(algos.isConnected());

    }

    @org.junit.jupiter.api.Test
    void shortestPathDist() {
        algos.load("dataTests/shortestPathTest.json");
        assertEquals(1.0, algos.shortestPathDist(1, 5));
    }


    @org.junit.jupiter.api.Test
    void shortestPath() {
        algos.load("dataTests/shortestPathTest.json");
        ArrayList<NodeData> trail = new ArrayList<>();
        NodeData node1 = algos.getGraph().getNode(1);
        NodeData node2 = algos.getGraph().getNode(2);
        NodeData node4 = algos.getGraph().getNode(4);
        NodeData node5 = algos.getGraph().getNode(5);
        trail.add(node1);
        trail.add(node2);
        trail.add(node4);
        trail.add(node5);
        assertEquals(trail, algos.shortestPath(1, 5));
    }

    @Test
    void dijkstra() {
        algos.load("dataTests/dijkstraTest.json");
        algos.dijkstra(1);
        Double[] dist = algos.dist;
        assertEquals(1.9, dist[0], EPSILON);
        assertEquals(0.0, dist[1], EPSILON);
        assertEquals(0.4, dist[2], EPSILON);
        assertEquals(1.2, dist[3], EPSILON);
        assertEquals(Integer.MAX_VALUE, dist[4], EPSILON);
        assertEquals(2.4, dist[5], EPSILON);
    }

    @org.junit.jupiter.api.Test
    void center() {
        algos.load("dataTests/centerTest.json");
        NodeData expectedNode = algos.getGraph().getNode(2);
        assertEquals(expectedNode, algos.center());
    }

    @org.junit.jupiter.api.Test
    void tsp() {
    }
}