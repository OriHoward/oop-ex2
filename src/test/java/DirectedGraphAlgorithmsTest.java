import api.DirectedWeightedGraphAlgorithms;
import api.NodeData;
import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.*;

class DirectedGraphAlgorithmsTest {

    static DirectedWeightedGraphAlgorithms algos;
    static ArrayList<NodeLocation> locations;
    static ArrayList<GraphNode> nodes;
    static ArrayList<GraphEdge> edges;
    static double x, y, z;
    static int size;

    @BeforeAll
    static void init() {
        x = 0;
        y = 0;
        z = 0;
        size = 10;
        algos = new DirectedGraphAlgorithms();
        DirectedGraph graph = (DirectedGraph) algos.getGraph();
        locations = new ArrayList<>();
        nodes = new ArrayList<>();
        edges = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            locations.add(new NodeLocation(x++, y++, z++));
        }
        x = y = z = 0;
        for (int i = 0; i < size; i++) {
            nodes.add(i, new GraphNode(locations.get(i), i));
        }
        edges.add(0, new GraphEdge(0, 2, 1.4));
        edges.add(1, new GraphEdge(4, 2, 1.3));
        edges.add(2, new GraphEdge(2, 4, 1.2));
        edges.add(3, new GraphEdge(3, 1, 1.1));
        edges.add(4, new GraphEdge(1, 0, 1.6));
        for (int i = 0; i < size; i++) {
            graph.addNode(nodes.get(i));
            graph.connect(edges.get(i).getSrc(), edges.get(i).getDest(), edges.get(i).getWeight());
        }


    }


    @org.junit.jupiter.api.Test
    void isConnected() {

    }

    @org.junit.jupiter.api.Test
    void shortestPathDist() {
    }

    @org.junit.jupiter.api.Test
    void shortestPath() {
    }

    @org.junit.jupiter.api.Test
    void dijkstra() {
    }

    @org.junit.jupiter.api.Test
    void center() {
    }

    @org.junit.jupiter.api.Test
    void tsp() {
    }
}