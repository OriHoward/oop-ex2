import api.DirectedWeightedGraph;
import api.EdgeData;
import api.NodeData;

import java.util.HashMap;
import java.util.Iterator;

public class DirectedGraph implements DirectedWeightedGraph {
    private int MCount;
    private int numOfEdges;
    HashMap<Integer, GraphNode> nodeMap;

    public DirectedGraph(int MCount, int numOfEdges, HashMap<Integer, GraphNode> nodeMap) {
        this.MCount = 0;
        this.numOfEdges = numOfEdges;
        this.nodeMap = new HashMap<>();
    }

    @Override
    public NodeData getNode(int key) {
        return nodeMap.get(key);

    }
    @Override
    public EdgeData getEdge(int src, int dest) {
        return null;
    }

    @Override
    public void addNode(NodeData n) {

    }

    @Override
    public void connect(int src, int dest, double w) {

    }

    @Override
    public Iterator<NodeData> nodeIter() {
        return null;
    }

    @Override
    public Iterator<EdgeData> edgeIter() {
        return null;
    }

    @Override
    public Iterator<EdgeData> edgeIter(int node_id) {
        return null;
    }

    @Override
    public NodeData removeNode(int key) {
        return null;
    }

    @Override
    public EdgeData removeEdge(int src, int dest) {
        return null;
    }

    @Override
    public int nodeSize() {
        return 0;
    }

    @Override
    public int edgeSize() {
        return 0;
    }

    @Override
    public int getMC() {
        return 0;
    }
}
