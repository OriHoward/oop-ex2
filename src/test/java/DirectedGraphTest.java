import algos.DirectedGraphAlgorithms;
import api.DirectedWeightedGraph;
import api.DirectedWeightedGraphAlgorithms;
import api.EdgeData;
import api.NodeData;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
    void iterForeach() {
        algos.load("dataTests/isConnected.json");
        DirectedWeightedGraph graph = algos.getGraph();
        Iterator<EdgeData> currIter = graph.edgeIter(0);
        List<Integer> actualList = new ArrayList<>();
        List<Integer> listFromIter = new ArrayList<>();
        actualList.add(1);
        actualList.add(2);
        currIter.forEachRemaining(edgeData -> listFromIter.add(edgeData.getDest()));
        assertEquals(listFromIter, actualList);
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