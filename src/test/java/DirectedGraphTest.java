import api.DirectedWeightedGraph;
import api.DirectedWeightedGraphAlgorithms;
import api.EdgeData;
import api.NodeData;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.assertThrows;


class DirectedGraphTest {

    static DirectedWeightedGraphAlgorithms algos;
    static int counter = 0;


    @BeforeAll
    static void setUp() {
        algos = new DirectedGraphAlgorithms();
        algos.load("dataTests/isConnected.json");
    }

    @BeforeEach
    void resetVars() {
        counter = 0;
    }

    @Test
    void nodeIter() {
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            DirectedWeightedGraph graph = algos.getGraph();
            Iterator<NodeData> currIter = graph.nodeIter();
            while (currIter.hasNext()) {
                NodeData edy = currIter.next();
                if (counter == 1) {
                    graph.connect(0, 4, 1.6);
                }
                counter += 1;
            }
        });
    }

    @Test
    void edgeIter() {
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            DirectedWeightedGraph graph = algos.getGraph();
            Iterator<EdgeData> currIter = graph.edgeIter();
            while (currIter.hasNext()) {
                EdgeData edy = currIter.next();
                if (counter == 1) {
                    graph.connect(0, 4, 1.6);
                }
                counter += 1;
            }
        });
    }

    @Test
    void testEdgeIter() {
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            DirectedWeightedGraph graph = algos.getGraph();
            Iterator<EdgeData> currIter = graph.edgeIter(0);
            while (currIter.hasNext()) {
                EdgeData edy = currIter.next();
                if (counter == 1) {
                    graph.connect(0, 4, 1.6);
                }
                counter += 1;
            }
        });
    }
}