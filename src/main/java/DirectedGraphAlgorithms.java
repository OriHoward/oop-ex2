import api.DirectedWeightedGraph;
import api.DirectedWeightedGraphAlgorithms;
import api.EdgeData;
import api.NodeData;

import java.util.*;

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

    @Override
    public boolean isConnected() {
        return false;
    }

    @Override
    public double shortestPathDist(int src, int dest) {
        return 0;
    }

    @Override
    public List<NodeData> shortestPath(int src, int dest) {
        Double[] dist = new Double[this.currGraph.nodeSize()];
        dist[src] = 0.0;
        Comparator<NodeData> byWeight = Comparator.comparing((NodeData n) -> dist[n.getKey()]);

        Queue<NodeData> toScan = new PriorityQueue<>(byWeight.reversed());
        Iterator<NodeData> nodesIter = this.currGraph.nodeIter();

        ArrayList<NodeData>[] prev = new ArrayList[this.currGraph.nodeSize()];
        while (nodesIter.hasNext()) {
            NodeData currNode = nodesIter.next();
            if (currNode.getKey() != src) {
                dist[currNode.getKey()] = Double.valueOf(Integer.MAX_VALUE);
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

        //at this point in the code I know what is the distance between the source node and every other node in the graph
        //I need to sort the nodes by the distance so that I can start iterating over them
        return null;
    }

    @Override
    public NodeData center() {
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
