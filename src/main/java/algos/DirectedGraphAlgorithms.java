package algos;

import api.DirectedWeightedGraph;
import api.DirectedWeightedGraphAlgorithms;
import api.EdgeData;
import api.NodeData;
import com.google.gson.*;
import org.apache.commons.lang.SerializationUtils;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class DirectedGraphAlgorithms implements DirectedWeightedGraphAlgorithms {

    private DirectedGraph currGraph;
    private Double[] dist;
    private List<NodeData>[] prev;
    private Comparator<NodeData> byWeightNew = (NodeData n1, NodeData n2) -> {
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
        dist = new Double[g.nodeSize()];
        prev = new ArrayList[g.nodeSize()];

        DirectedGraph newGraph = (DirectedGraph) g;
        HashMap<Integer, NodeData> newGraphNodeMap = newGraph.getNodeMap();

        Iterator<NodeData> nodeIter = g.nodeIter();
        while (nodeIter.hasNext()) {
            NodeData currNode = nodeIter.next();
            currNode.setTag(NodeTagEnum.WHITE.getValue());
            newGraphNodeMap.put(currNode.getKey(), currNode);
        }
        Iterator<EdgeData> edgeDataIterator = g.edgeIter();
        List<EdgeData> parsedEdges = new ArrayList<>();
        edgeDataIterator.forEachRemaining(parsedEdges::add);
        newGraph.setParsedEdges(parsedEdges);
        newGraph.initiateEdgeMaps();
        this.currGraph = newGraph;

    }

    private void resetGraphVars(DirectedWeightedGraph g) {
        dist = new Double[g.nodeSize()];
        prev = new ArrayList[g.nodeSize()];
        Iterator<NodeData> nodeIter = g.nodeIter();
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
        return isConnectedDFS();
    }

    public boolean isConnectedDFS() {
        DirectedGraph graphCopy = (DirectedGraph) this.copy();
        if (graphCopy == null) {
            return false;
        }
        HashSet<Integer> scannedNodes = new HashSet<>();
        NodeData firstNode = graphCopy.getNodeMap().get(0);
        dfsTraversal(graphCopy, firstNode, scannedNodes);

        if (scannedNodes.size() != graphCopy.getNodeMap().size()) {
            return false;
        }

        scannedNodes.clear();
        resetGraphVars(graphCopy);

        //dfs traversal on the reversed graph
        graphCopy.initiateEdgeMaps();
        dfsTraversal(graphCopy, firstNode, scannedNodes);

        if (scannedNodes.size() != graphCopy.getNodeMap().size()) {
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
        if (src == dest) {
            return null;
        }
        dijkstra(src);
        List<NodeData> fullPath = prev[dest];
        fullPath.add(this.currGraph.getNodeMap().get(dest));
        return fullPath;
    }

    public void dijkstra(int src) {
        resetGraphVars(currGraph);
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
    /**
     * for each remaining node after we found the optimal path containing the maximum amount of nodes we try to add it the path with the lowest cost
     * if the node was added filter the remaining cities because it might have added other cities during that trip, continue until no cities left
     */
    public List<NodeData> tsp(List<NodeData> cities) {
        if (cities == null || cities.size() ==0) {
            return null;
        }

        if (cities.size() == 1) {
            return cities;
        }

        HashSet<NodeData> citiesWithoutDups = new HashSet<>(cities);
        cities = new ArrayList<>(citiesWithoutDups);

        List<NodeData> bestPath = getOptimalPathFromList(cities);
        clearVisitedCities(bestPath, cities);
        while (!cities.isEmpty()) {
            List<NodeData> optimalPathFromLast = getOptimalPathFromLast(bestPath.get(bestPath.size() - 1), cities);
            if (optimalPathFromLast == null) {
                break;
            } else {
                bestPath.addAll(optimalPathFromLast);
            }
            clearVisitedCities(bestPath, cities);
        }

        return bestPath;
    }

    private void clearVisitedCities(List<NodeData> bestCircle, List<NodeData> cities) {
        for (NodeData node : bestCircle) {
            cities.remove(node);
        }
    }

    private List<NodeData> getOptimalPathFromLast(NodeData lastNode, List<NodeData> cities) {
        HashMap<List<NodeData>, Double> pathMap = new HashMap<>();
        Iterator<NodeData> cityIter = cities.iterator();
        int firstNodeId = lastNode.getKey();

        while (cityIter.hasNext()) {
            int destNodeId = cityIter.next().getKey();
            List<NodeData> firstDirection = shortestPath(firstNodeId, destNodeId);
            pathMap.put(firstDirection, dist[destNodeId]);
        }

        List<NodeData> optimalPath = getOptimalPathFromMap(cities, pathMap);
        if (optimalPath == null) {
            return null;
        } else {
            optimalPath.remove(0);
            return optimalPath;
        }

    }


    private List<NodeData> getOptimalPathFromList(List<NodeData> cities) {
        HashMap<List<NodeData>, Double> pathMap = new HashMap<>();

        for (int i = 0; i < cities.size(); i++) {
            for (int j = 0; j < cities.size(); j++) {
                int firstNodeId = cities.get(i).getKey();
                int secondNodeId = cities.get(j).getKey();
                if (firstNodeId != secondNodeId) {
                    double pathCost = 0;
                    List<NodeData> shortestPath = shortestPath(firstNodeId, secondNodeId);
                    pathCost += dist[secondNodeId];
                    pathMap.put(shortestPath, pathCost);
                }
            }
        }

        return getOptimalPathFromMap(cities, pathMap);
    }

    private List<NodeData> getOptimalPathFromMap(List<NodeData> cities, HashMap<List<NodeData>, Double> pathMap) {
        int maxParticipants = 0;
        List<NodeData> bestMatchPath = null;
        for (List<NodeData> path : pathMap.keySet()) {
            HashSet<Integer> currParticipants = new HashSet<>();
            for (NodeData city : cities) {
                if (path.contains(city)) {
                    currParticipants.add(city.getKey());
                }
            }
            if (currParticipants.size() > maxParticipants) {
                bestMatchPath = path;
                maxParticipants = currParticipants.size();
            } else if (currParticipants.size() == maxParticipants && bestMatchPath != null && pathMap.get(path) < pathMap.get(bestMatchPath)) {
                bestMatchPath = path;
            }
        }
        return bestMatchPath;
    }

    @Override
    public boolean save(String file) {
        try {
            FileWriter fileWriter = new FileWriter(file);
            JsonSerializer<NodeData> posSerializer = new PosSerializer();
            Gson gsonBuilder = new GsonBuilder()
                    .excludeFieldsWithoutExposeAnnotation()
                    .registerTypeAdapter(GraphNode.class, posSerializer)
                    .setPrettyPrinting()
                    .create();
            JsonArray nodeArray = gsonBuilder.toJsonTree(currGraph.getNodeMap().values()).getAsJsonArray();
            JsonArray edgeArray = gsonBuilder.toJsonTree(currGraph.getParsedEdges()).getAsJsonArray();
            JsonObject jsonObj = new JsonObject();
            jsonObj.add("Edges", edgeArray);
            jsonObj.add("Nodes", nodeArray);
            gsonBuilder.toJson(jsonObj, fileWriter);
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public boolean load(String file) {
        DirectedGraph g = new DirectedGraph();
        boolean isLoaded = g.loadGraph(file);
        if (isLoaded) {
            currGraph = g;
        }
        resetGraphVars(g);
        return isLoaded;
    }
}
