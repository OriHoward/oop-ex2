package algos;

import api.DirectedWeightedGraph;
import api.EdgeData;
import api.NodeData;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.io.Reader;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;


public class DirectedGraph implements DirectedWeightedGraph, Serializable {
    private int MCount;
    private HashMap<Integer, NodeData> nodeMap;
    private List<EdgeData> parsedEdges;

    public DirectedGraph() {
        this.MCount = 0;
        this.nodeMap = new HashMap<>();
        parsedEdges = new ArrayList<>();
    }

    public DirectedGraph(String jsonfile) {
        this.MCount = 0;
        this.nodeMap = new HashMap<>();
        parsedEdges = new ArrayList<>();
        this.loadGraph(jsonfile);
    }

    @Override
    public NodeData getNode(int key) {
        return nodeMap.get(key);

    }

    @Override
    public EdgeData getEdge(int src, int dest) {
        GraphNode node = (GraphNode) nodeMap.get(src);
        if (node == null) {
            return null;
        }
        if (node.getDestMap().get(dest) == null) {
            return null;
        }
        return node.getDestMap().get(dest);
    }

    @Override
    public void addNode(NodeData n) {
        if (n != null) {
            this.nodeMap.put(n.getKey(), n);
            MCount++;
        }
    }

    public List<EdgeData> getParsedEdges() {
        return parsedEdges;
    }

    @Override
    public void connect(int src, int dest, double w) {
        GraphNode srcNode = (GraphNode) nodeMap.get(src);
        GraphNode destNode = (GraphNode) nodeMap.get(dest);
        GraphEdge edge = new GraphEdge(src, dest, w);
        GraphEdge oldEdge = (GraphEdge) srcNode.getDestMap().get(dest);
        //we need to check if that is a new node, or it overrides an existing one
        if (oldEdge == null) {
            parsedEdges.add(edge);
        } else {
            parsedEdges.remove(oldEdge);
            parsedEdges.add(edge);
        }
        srcNode.addDest(edge);
        destNode.addSrc(edge);
        MCount++;
    }

    @Override
    public Iterator<NodeData> nodeIter() {
        return new DecoratedGraphIterator<>(this.nodeMap.values().iterator(), this);
    }

    @Override
    public Iterator<EdgeData> edgeIter() {
        return new DecoratedGraphIterator<>(this.parsedEdges.iterator(), this);
    }

    @Override
    public Iterator<EdgeData> edgeIter(int node_id) {
        GraphNode currNode = (GraphNode) this.nodeMap.get(node_id);
        return new DecoratedGraphIterator<>(currNode.getDestMap().values().iterator(), this);
    }


    @Override
    public NodeData removeNode(int key) {
        GraphNode currNode = (GraphNode) this.nodeMap.get(key);
        if (currNode==null){
            return null;
        }
        //remove this node from the dest mapping of each father
        currNode.getSourceMap().keySet().forEach(nodeKey -> {
            GraphNode currFather = (GraphNode) this.nodeMap.get(nodeKey);
            EdgeData removedEdge = currFather.removeDest(currNode.getKey());
            MCount++;
            parsedEdges.remove(removedEdge);
        });
        //remove this node from each source map of its children
        currNode.getDestMap().keySet().forEach(nodeKey -> {
            GraphNode currChild = (GraphNode) this.nodeMap.get(nodeKey);
            EdgeData removedEdge = currChild.removeSrc(currNode.getKey());
            MCount++;
            parsedEdges.remove(removedEdge);
        });
        MCount++;
        return this.nodeMap.remove(key);
    }


    @Override
    public EdgeData removeEdge(int src, int dest) {
        GraphNode srcNode = (GraphNode) nodeMap.get(src);
        GraphNode destNode = (GraphNode) nodeMap.get(dest);
        if (srcNode == null || destNode == null) {
            return null;
        }
        EdgeData removedEdge = srcNode.removeDest(dest);
        if (removedEdge == null) {
            return null;
        }
        MCount++;
        parsedEdges.remove(removedEdge);
        return destNode.removeSrc(src);
    }

    @Override
    public int nodeSize() {
        return this.nodeMap.size();
    }

    @Override
    public int edgeSize() {
        return parsedEdges.size();
    }

    @Override
    public int getMC() {
        return MCount;
    }

    public void setParsedEdges(List<EdgeData> parsedEdges) {
        this.parsedEdges = parsedEdges;
    }

    public HashMap<Integer, NodeData> getNodeMap() {
        return nodeMap;
    }

    public void hasChanged(int givenMCount) {
        if (givenMCount != this.MCount) {
            throw new RuntimeException("Changes where performed during iteration");
        }
    }

    public boolean loadGraph(String filename) {
        try {
            Gson gson = new Gson();
            Reader reader = Files.newBufferedReader(Paths.get(filename));
            JsonObject jsonObject = gson.fromJson(reader, JsonObject.class);
            for (JsonElement currEdgeElem : jsonObject.get("Edges").getAsJsonArray()) {
                JsonObject edgeObject = currEdgeElem.getAsJsonObject();
                parsedEdges.add(new GraphEdge(
                        edgeObject.get("src").getAsInt(),
                        edgeObject.get("dest").getAsInt(),
                        edgeObject.get("w").getAsDouble()
                ));
            }
            for (JsonElement currNodeElem : jsonObject.get("Nodes").getAsJsonArray()) {
                JsonObject nodeObject = currNodeElem.getAsJsonObject();
                String[] posParts = nodeObject.get("pos").getAsString().split(",");
                GraphNode currNode = new GraphNode(
                        new NodeLocation(
                                Double.parseDouble(posParts[0]),
                                posParts.length > 1 ? Double.parseDouble(posParts[1]) : 0,
                                posParts.length > 2 ? Double.parseDouble(posParts[2]) : 0),
                        nodeObject.get("id").getAsInt()
                );
                this.nodeMap.put(currNode.getKey(), currNode);
            }
            reader.close();
            initiateEdgeMaps();
            return true;

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void initiateEdgeMaps() {
        for (EdgeData edge : parsedEdges) {
            GraphNode srcNode = (GraphNode) this.nodeMap.get(edge.getSrc());
            GraphNode dstNode = (GraphNode) this.nodeMap.get(edge.getDest());
            srcNode.addDest(edge);
            dstNode.addSrc(edge);
        }
    }
}
