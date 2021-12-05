package algos;

import api.DirectedWeightedGraph;
import api.DirectedWeightedGraphAlgorithms;
import api.EdgeData;
import api.NodeData;
import org.apache.commons.lang.SerializationUtils;

import java.util.*;

public class DirectedGraphAlgorithms implements DirectedWeightedGraphAlgorithms {

    DirectedGraph currGraph;
    Double[] dist;
    List<NodeData>[] prev;

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

        DirectedGraph graphCopy = (DirectedGraph) SerializationUtils.clone(this.currGraph);

        return graphCopy;
    }

    @Override
    public boolean isConnected() {
        int numOfNodes = currGraph.nodeMap.size();
        HashSet<Integer> foundConnected = new HashSet<>();
        for (int nodeKey = 0; nodeKey < numOfNodes; nodeKey++) {
            if (!foundConnected.contains(nodeKey)) {
                dijkstra(nodeKey);
                int currMaxIdx = findMax();
                if (dist[currMaxIdx] == Integer.MAX_VALUE) {
                    return false;
                }
                prev[currMaxIdx].forEach((node) -> foundConnected.add(node.getKey()));
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
        List<NodeData> fullPath = prev[dest];
        fullPath.add(this.currGraph.nodeMap.get(dest));
        return fullPath;
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
//        if (!isConnected()) {
//            return null;
//        }
        // min of the longest distance
        Iterator<NodeData> nodeIter = this.getGraph().nodeIter();
        NodeData currNode;
        double currMinMax = Integer.MAX_VALUE;
        int chosenNode = 0;
        HashSet<Integer> passed = new HashSet<>();
        while (nodeIter.hasNext()) {
            currNode = nodeIter.next();
            if (!passed.contains(currNode.getKey())) {

                dijkstra(currNode.getKey());
                int minmaxIdx = findMax();
                if (dist[minmaxIdx] < currMinMax) {
                    currMinMax = dist[minmaxIdx];
                    chosenNode = currNode.getKey();
                }
                addMinimalSources(minmaxIdx, passed);
                System.out.println(passed.size());
            }
        }

        return this.currGraph.getNode(chosenNode);
    }

    private void addMinimalSources(int minmaxIdx, HashSet<Integer> passed) {
        for (NodeData scannedNode : prev[minmaxIdx]) {
            GraphNode currNode = (GraphNode) scannedNode;
            Iterator<EdgeData> nodeNeighbors = currNode.getDestMap().values().iterator();
            passed.add(scannedNode.getKey());
            while (nodeNeighbors.hasNext()) {
                passed.add(nodeNeighbors.next().getSrc());
            }
        }
    }

    private int findMax() {
        double max = dist[0];
        int maxIdx = 0;
        for (int i = 1; i < dist.length; i++) {
            if (dist[i] > max) {
                max = dist[i];
                maxIdx = i;
            }
        }
        return maxIdx;
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