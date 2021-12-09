import algos.DirectedGraph;
import algos.DirectedGraphAlgorithms;
import api.DirectedWeightedGraph;
import api.EdgeData;
import api.NodeData;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DirectedGraphAlgorithmsTest {

    static DirectedGraphAlgorithms algos;
    static final double EPSILON = 0.0000000001;

    @BeforeAll
    static void init() {
        algos = new DirectedGraphAlgorithms();
    }


    @Test
    void isConnected() {
        algos.load("dataTests/notConnected.json");
        assertFalse(algos.isConnected());
        algos.load("dataTests/isConnected.json");
        assertTrue(algos.isConnected());

    }

    @Test
    void shortestPathDist() {
        algos.load("dataTests/shortestPathTest.json");
        assertEquals(1.0, algos.shortestPathDist(1, 5));
    }


    @Test
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
        Double[] dist = algos.getDist();
        assertEquals(1.9, dist[0], EPSILON);
        assertEquals(0.0, dist[1], EPSILON);
        assertEquals(0.4, dist[2], EPSILON);
        assertEquals(1.2, dist[3], EPSILON);
        assertEquals(Integer.MAX_VALUE, dist[4], EPSILON);
        assertEquals(2.4, dist[5], EPSILON);
    }

    @Test
    void center() {
        algos.load("dataTests/centerTest.json");
        NodeData expectedNode = algos.getGraph().getNode(2);
        assertEquals(expectedNode, algos.center());
        algos.load("data/1000Nodes.json");
        NodeData expectedNode2 = algos.getGraph().getNode(362);
        assertEquals(expectedNode2, algos.center());
    }

    @Test
    void tsp() {
        algos.load("data/G1.json");
        List<NodeData> randomList = new ArrayList<>();

        randomList.add(algos.getGraph().getNode(5));
        randomList.add(algos.getGraph().getNode(2));
        randomList.add(algos.getGraph().getNode(9));

        List<NodeData> actualPath = new ArrayList<>();
        actualPath.add(algos.getGraph().getNode(2));
        actualPath.add(algos.getGraph().getNode(6));
        actualPath.add(algos.getGraph().getNode(5));
        actualPath.add(algos.getGraph().getNode(6));
        actualPath.add(algos.getGraph().getNode(7));
        actualPath.add(algos.getGraph().getNode(8));
        actualPath.add(algos.getGraph().getNode(9));

        List<NodeData> shortestPath = algos.tsp(randomList);

        assertEquals(actualPath, shortestPath);
        assertNull(algos.tsp(null));
        assertNull(algos.tsp(new ArrayList<>()));
    }

    @Test
    void copy() {
        algos.load("dataTests/isConnected.json");
        DirectedWeightedGraph copyGraph = algos.copy();
        copyGraph.connect(0, 1, 1.7);
        ArrayList<EdgeData> originalEdges = new ArrayList<>();
        ArrayList<EdgeData> copiedEdges = new ArrayList<>();

        algos.getGraph().edgeIter().forEachRemaining(originalEdges::add);
        copyGraph.edgeIter().forEachRemaining(copiedEdges::add);
        assertNotEquals(originalEdges, copiedEdges);
    }

    @Test
    void initTest() {
        DirectedGraph graph = new DirectedGraph();
        graph.loadGraph("dataTests/notConnected.json");
        DirectedGraphAlgorithms algos = new DirectedGraphAlgorithms();
        algos.load("dataTests/isConnected.json");
        assertTrue(algos.isConnected());
        algos.init(graph);
        assertFalse(algos.isConnected());
    }
}