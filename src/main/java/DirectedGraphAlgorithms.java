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
        return currGraph;
    }

    @Override
    public DirectedWeightedGraph copy() {
        return null;
    }

    @Override
    public boolean isConnected() {
        int numOfNodes = currGraph.nodeMap.size();
        for (int nodeKey = 0; nodeKey < numOfNodes; nodeKey++) {
            dijkstra(nodeKey);
            for (int j = 0; j < dist.length; j++) {
                if (dist[j] == Integer.MAX_VALUE) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public double shortestPathDist(int src, int dest) {
        dijkstra(src);
        return dist[dest];
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
                    // add the list of the currNode
                    if (prev[currNode.getKey()] != null) {
                        prev[neighbor.getKey()] = new ArrayList(prev[currNode.getKey()]);
                    }
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
        ArrayList<Double> centerNodePotential = new ArrayList<>();
        for (int nodeKey = 0; nodeKey < this.currGraph.nodeMap.size(); nodeKey++) {
            dijkstra(nodeKey);
            centerNodePotential.add(findMax(dist));
        }
        return findCenterNode(centerNodePotential);

    }

    private NodeData findCenterNode(ArrayList<Double> centerNodePotential) {
        double currMin = centerNodePotential.get(0);
        int chosenNodeKey = 0;
        for (int i = 1; i < centerNodePotential.size(); i++) {
            if (centerNodePotential.get(i) < currMin) {
                currMin = centerNodePotential.get(i);
                chosenNodeKey = i;
            }
        }
        return this.currGraph.getNode(chosenNodeKey);
    }

    private double findMax(Double[] dist) {
        double currMax = dist[0];
        double max = currMax;
        for (int i = 1; i < dist.length; i++) {
            if (dist[i] > currMax) {
                currMax = dist[i];
                max = currMax;
            }
        }
        return max;
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
