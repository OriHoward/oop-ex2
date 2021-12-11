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
    private HashMap<Integer, List<NodeData>> prev = new HashMap();
    private Comparator<GraphNode> byWeightNew = (GraphNode n1, GraphNode n2) -> {
        double firstDist = n1.getDist();
        double secondDist = n2.getDist();
        if (firstDist == secondDist)
            return 0;
        return firstDist - secondDist > 0 ? 1 : -1;
    };


    @Override
    /**
     * copies a given graph to the current graph and overrides it
     */
    public void init(DirectedWeightedGraph g) {
        prev.clear();

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

    /**
     * resets the variables that are populated from the algorithms in a given graph
     *
     * @param g
     */
    private void resetGraphVars(DirectedWeightedGraph g) {
        prev.clear();
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
    /**
     * uses the apache commons jar to create a deep copy
     */
    public DirectedWeightedGraph copy() {

        DirectedGraph graphCopy = (DirectedGraph) SerializationUtils.clone(this.currGraph);

        return graphCopy;
    }

    @Override
    public boolean isConnected() {
        return isConnectedDFS();
    }

    /**
     * This function creates a copy of the original graph for the dfs traversals
     * the first traversal is on the graph as it is and it inverses it, while the 2nd traversal is on the inverse graph
     * this is the fastest way as it way taught in algo class
     *
     * @return
     */
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


    /**
     * this dfs is iterative, so it doesn't have stackoverflow when we are going over large graphs (10k nodes)
     * this dfs also inverses the graph it receives this is the reason we use a copy of the original graph
     *
     * @param graph        the graph to traverse
     * @param currNode     a node to start the traversal rom
     * @param scannedNodes the hashset of the scanned nodes
     */
    private void dfsTraversal(DirectedGraph graph, NodeData currNode, HashSet<Integer> scannedNodes) {
        Stack<NodeData> dfsStack = new Stack<>();
        dfsStack.add(currNode);
        while (!dfsStack.isEmpty()) {
            currNode = dfsStack.pop();
            if (currNode == null) {
                continue;
            }
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
    /**
     * returns the length of the shortest path from src to dest
     */
    public double shortestPathDist(int src, int dest) {
        if (currGraph.getNodeMap().get(src) == null || currGraph.getNodeMap().get(dest) == null) {
            return Integer.MAX_VALUE;
        }
        dijkstra(src);
        GraphNode chosenNode = (GraphNode) currGraph.getNodeMap().get(dest);
        return chosenNode.getDist();
    }

    @Override
    /**
     * calculates the shortest path between src and dest and returns the path which includes all the nodes from src-->dest (included)
     */
    public List<NodeData> shortestPath(int src, int dest) {
        if (src == dest || currGraph.getNodeMap().get(src) == null || currGraph.getNodeMap().get(dest) == null) {
            return null;
        }
        dijkstra(src);
        List<NodeData> fullPath = prev.get(dest);
        fullPath.add(this.currGraph.getNodeMap().get(dest));
        return fullPath;
    }

    /**
     * this versino of the dijkstra algorithm is recreated the shortest path and used in the tsp and shortestPath functions
     */
    public void dijkstra(int src) {
        resetGraphVars(currGraph);
        GraphNode currNode = (GraphNode) this.getGraph().getNode(src);
        currNode.setDist(0.0);
        Queue<GraphNode> toScan = new PriorityQueue<>(byWeightNew);
        Iterator<NodeData> nodesIter = this.currGraph.nodeIter();

        while (nodesIter.hasNext()) {
            currNode = (GraphNode) nodesIter.next();
            if (currNode.getKey() != src) {
                currNode.setDist(Integer.MAX_VALUE);
                prev.put(currNode.getKey(), new ArrayList<>());
            }
            toScan.add(currNode);
        }

        while (!toScan.isEmpty()) {
            currNode = toScan.remove();
            Iterator<EdgeData> edgeIter = this.currGraph.edgeIter(currNode.getKey());
            EdgeData currEdge;
            while (edgeIter.hasNext()) {
                currEdge = edgeIter.next();
                GraphNode neighbor = (GraphNode) this.currGraph.getNode(currEdge.getDest());
                double alt = currNode.getDist() + currEdge.getWeight();
                if (alt < neighbor.getDist()) {
                    neighbor.setDist(alt);
                    // add the list of the currNode
                    if (prev.get(currNode.getKey()) != null) {

                        prev.put(neighbor.getKey(), new ArrayList(prev.get(currNode.getKey())));
                    }
                    prev.get(neighbor.getKey()).add(currNode);
                    toScan.add(neighbor);
                }
            }
        }
    }

    /**
     * this is a minimized version of the dijkstra algorithm that is used in the center function,
     * this doesn't recreate the shortest path, it just calculates the distance from src to every other node
     *
     * @param src
     */
    public void dijkstraMinimize(int src) {
        GraphNode currNode = (GraphNode) this.getGraph().getNode(src);
        currNode.setDist(0.0);
        Queue<GraphNode> toScan = new PriorityQueue<>(byWeightNew);
        Iterator<NodeData> nodesIter = this.currGraph.nodeIter();

        while (nodesIter.hasNext()) {
            currNode = (GraphNode) nodesIter.next();
            if (currNode.getKey() != src) {
                currNode.setDist(Integer.MAX_VALUE);
            }
            toScan.add(currNode);
        }

        while (!toScan.isEmpty()) {
            currNode = toScan.remove();
            Iterator<EdgeData> edgeIter = this.currGraph.edgeIter(currNode.getKey());
            EdgeData currEdge;
            while (edgeIter.hasNext()) {
                currEdge = edgeIter.next();
                GraphNode neighbor = (GraphNode) this.currGraph.getNode(currEdge.getDest());
                double alt = currNode.getDist() + currEdge.getWeight();
                if (alt < neighbor.getDist()) {
                    neighbor.setDist(alt);
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
            GraphNode node = (GraphNode) this.currGraph.getNode(minmaxIdx);
            if (node.getDist() < currMinMax) {
                currMinMax = node.getDist();
                chosenNode = currNode.getKey();
            }
        }
        return this.currGraph.getNode(chosenNode);
    }


    private int findMax() {
        double max = Integer.MIN_VALUE;
        int maxIdx = 0;

        Iterator<NodeData> nodeDataIterator = this.getGraph().nodeIter();
        while (nodeDataIterator.hasNext()) {
            GraphNode curGraphNode = (GraphNode) nodeDataIterator.next();
            if (curGraphNode.getDist() > max) {
                max = curGraphNode.getDist();
                maxIdx = curGraphNode.getKey();
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
        if (cities == null || cities.size() == 0) {
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
            GraphNode node = (GraphNode) this.currGraph.getNode(destNodeId);
            pathMap.put(firstDirection, node.getDist());
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
                    GraphNode node = (GraphNode) this.currGraph.getNode(secondNodeId);
                    pathCost += node.getDist();
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
