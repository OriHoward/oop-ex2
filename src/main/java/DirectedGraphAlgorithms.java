import api.DirectedWeightedGraph;
import api.DirectedWeightedGraphAlgorithms;
import api.EdgeData;
import api.NodeData;

import java.util.Iterator;
import java.util.List;

public class DirectedGraphAlgorithms implements DirectedWeightedGraphAlgorithms {

    DirectedGraph currGraph;


    @Override
    public void init(DirectedWeightedGraph g) {
        Iterator<NodeData> nodeIter = this.currGraph.nodeIter();
        while (nodeIter.hasNext()) {
            NodeData currNode = nodeIter.next();
            currNode.setTag(NodeTagEnum.WHITE.getValue());
        }
    }

    @Override
    public DirectedWeightedGraph getGraph() {
        return null;
    }

    @Override
    public DirectedWeightedGraph copy() {
        return null;
    }

    boolean visitAll(GraphNode currNode, Iterator<EdgeData> edgeDataIterator, int counter) {
        int numOfNodes = currGraph.nodeMap.size();
        currNode.setTag(NodeTagEnum.GRAY.getValue());
        EdgeData currEdge;
        GraphNode nextNode;
        while (edgeDataIterator.hasNext()) {
            currEdge = edgeDataIterator.next();
            nextNode = (GraphNode) currGraph.nodeMap.get(currEdge.getDest());
            if (nextNode.getTag() == NodeTagEnum.WHITE.getValue()) {
                visitAll(nextNode, edgeDataIterator, counter);
                nextNode.setTag(NodeTagEnum.BLACK.getValue());
                counter++;
            }
        }
        return counter == numOfNodes;
    }

    @Override
    public boolean isConnected() {
        int numOfNodes = currGraph.nodeMap.size();
        int counter = 0;
        for (int i = 0; i < numOfNodes; i++) {
            Iterator<EdgeData> edgeDataIterator = currGraph.edgeIter(i);
            GraphNode currNode = (GraphNode) currGraph.nodeMap.get(i);
            if (!visitAll(currNode, edgeDataIterator, counter)) {
                return false;
            }
            init(currGraph);
            counter = 0;
        }
        return true;
    }

    @Override
    public double shortestPathDist(int src, int dest) {
        return 0;
    }

    @Override
    public List<NodeData> shortestPath(int src, int dest) {
        return null;
    }

    @Override
    public NodeData center() {
        if (!isConnected()) {
            return null;
        }

        return null;
    }

    @Override
    public List<NodeData> tsp(List<NodeData> cities) {
        return null;
    }

    @Override
    public boolean save(String file) {
        return false;
    }

    @Override
    public boolean load(String file) {
        DirectedGraph g = new DirectedGraph();
        boolean isLoaded = g.loadGraph(file);
        if (isLoaded) {
            currGraph = g;
        }
        return isLoaded;
    }
}
