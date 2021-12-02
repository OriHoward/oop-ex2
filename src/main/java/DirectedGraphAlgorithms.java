import api.DirectedWeightedGraph;
import api.DirectedWeightedGraphAlgorithms;
import api.EdgeData;
import api.NodeData;

import java.util.*;

public class DirectedGraphAlgorithms implements DirectedWeightedGraphAlgorithms {

    DirectedGraph currGraph;
    Double[] dist;
    List[] prev;

    @Override
    public void init(DirectedWeightedGraph g) {
        dist = new Double[this.currGraph.nodeSize()];
        prev = new ArrayList[this.currGraph.nodeSize()];

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
        dijkstra(src);
        return prev[dest];
    }

    public void dijkstra(int src) {

        dist[src] = 0.0;
        Comparator<NodeData> byWeight = Comparator.comparing((NodeData n) -> dist[n.getKey()]);
        Queue<NodeData> toScan = new PriorityQueue<>(byWeight.reversed());
        Iterator<NodeData> nodesIter = this.currGraph.nodeIter();

        while (nodesIter.hasNext()) {
            NodeData currNode = nodesIter.next();
            if (currNode.getKey() != src) {
                dist[currNode.getKey()] = (double) Integer.MAX_VALUE;
                prev[currNode.getKey()] = new ArrayList<>();
            }
            toScan.add(currNode);
        }

        while (!toScan.isEmpty()) {
            NodeData currNode = toScan.remove();
            Iterator<EdgeData> edgeIter = this.currGraph.edgeIter(currNode.getKey());
            EdgeData currEdge;
            while (edgeIter.hasNext()) {
                currEdge = edgeIter.next();
                NodeData neighbor = this.currGraph.getNode(currEdge.getDest());
                double alt = dist[currNode.getKey()] + currEdge.getWeight();
                if (alt < dist[neighbor.getKey()]) {
                    dist[neighbor.getKey()] = alt;
                    prev[neighbor.getKey()].add(currNode);
                    toScan.add(neighbor);
                }
            }
        }
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
        init(g);
        return isLoaded;
    }
}
