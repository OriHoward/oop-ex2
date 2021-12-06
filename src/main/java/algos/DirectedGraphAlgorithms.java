package algos;

import api.DirectedWeightedGraph;
import api.DirectedWeightedGraphAlgorithms;
import api.EdgeData;
import api.NodeData;
import org.apache.commons.lang.SerializationUtils;
import org.w3c.dom.Node;

import java.awt.image.AreaAveragingScaleFilter;
import java.lang.reflect.Array;
import java.util.*;

public class DirectedGraphAlgorithms implements DirectedWeightedGraphAlgorithms {

    DirectedGraph currGraph;
    Double[] dist;
    List<NodeData>[] prev;
    Comparator<NodeData> byWeightNew = (NodeData n1, NodeData n2) -> {
        double firstDist = dist[n1.getKey()];
        double secondDist = dist[n2.getKey()];
        if (firstDist == secondDist)
            return 0;
        return firstDist - secondDist > 0 ? 1 : -1;
    };

    public Double[] getDist() {
        return dist;
    }

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
        return dfs();
    }

    public boolean dfs() {
        DirectedGraph graphCopy = (DirectedGraph) this.copy();
        if (graphCopy == null) {
            return false;
        }
        HashSet<Integer> scannedNodes = new HashSet<>();
        NodeData firstNode = graphCopy.nodeMap.get(0);
        dfsTraversal(graphCopy, firstNode, scannedNodes);

        if (scannedNodes.size() != graphCopy.nodeMap.size()) {
            return false;
        }

        scannedNodes.clear();
        Iterator<NodeData> nodeDataIterator = graphCopy.nodeIter();
        while (nodeDataIterator.hasNext()) {
            nodeDataIterator.next().setTag(NodeTagEnum.WHITE.getValue());
        }

        //dfs traversal on the reversed graph
        graphCopy.initiateEdgeMaps();
        dfsTraversal(graphCopy, firstNode, scannedNodes);

        if (scannedNodes.size() != graphCopy.nodeMap.size()) {
            return false;
        }

        return true;

    }

    //this dfs is iterative, so it doesn't have stackoverflow when we are going over large graphs (10k nodes)
    private void dfsTraversal(DirectedGraph graph, NodeData currNode, HashSet<Integer> scannedNodes) {
        Stack<NodeData> dfsStack = new Stack<>();
        dfsStack.add(currNode);
        while (!dfsStack.isEmpty()) {
            currNode = dfsStack.pop();
            scannedNodes.add(currNode.getKey());
            if (currNode.getTag() == NodeTagEnum.WHITE.getValue()) {
                currNode.setTag(NodeTagEnum.GRAY.getValue());
                Iterator<EdgeData> edgeIter = graph.edgeIter(currNode.getKey());
                while (edgeIter.hasNext()) {
                    GraphEdge currNeighEdge = (GraphEdge) edgeIter.next();
                    dfsStack.add(graph.getNode(currNeighEdge.getDest()));
                    int temp = currNeighEdge.getDest();
                    currNeighEdge.setDest(currNeighEdge.getSrc());
                    currNeighEdge.setSource(temp);
                }
            }
            currNode.setTag(NodeTagEnum.BLACK.getValue());
            GraphNode asGraphNode = (GraphNode) currNode;
            asGraphNode.setDestMap(new HashMap<>());
            asGraphNode.setSourceMap(new HashMap<>());
        }
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
        Queue<NodeData> toScan = new PriorityQueue<>(byWeightNew);
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

    public void dijkstraMinimize(int src) {
        dist[src] = 0.0;
        Queue<NodeData> toScan = new PriorityQueue<>(byWeightNew);
        Iterator<NodeData> nodesIter = this.currGraph.nodeIter();

        while (nodesIter.hasNext()) {
            NodeData currNode = nodesIter.next();
            if (currNode.getKey() != src) {
                dist[currNode.getKey()] = (double) Integer.MAX_VALUE;
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
        // min of the longest distance
        Iterator<NodeData> nodeIter = this.getGraph().nodeIter();
        NodeData currNode;
        double currMinMax = Integer.MAX_VALUE;
        int chosenNode = 0;
        while (nodeIter.hasNext()) {
            currNode = nodeIter.next();
            dijkstraMinimize(currNode.getKey());
            int minmaxIdx = findMax();
            if (dist[minmaxIdx] < currMinMax) {
                currMinMax = dist[minmaxIdx];
                chosenNode = currNode.getKey();
            }
        }
        return this.currGraph.getNode(chosenNode);
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

        int counter = 0;
        for (int i = 0; i < cities.size(); i++) {
            dijkstra(i);
            for (int j = 0; j < prev.length; j++) {
                if (prev[j] != null) {
                    for (int k = 0; k < prev[j].size(); k++) {
                        if (cities.contains(prev[j].get(k))) {
                            counter++;
                        }
                    }
                    if (counter == cities.size()) {
                        return prev[j];
                    }
                }
                counter = 0;
            }
        }
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
