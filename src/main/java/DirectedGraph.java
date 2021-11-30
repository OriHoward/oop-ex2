import api.DirectedWeightedGraph;
import api.EdgeData;
import api.NodeData;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;


public class DirectedGraph implements DirectedWeightedGraph {
    private int MCount;
    private int numOfEdges;
    HashMap<Integer, NodeData> nodeMap;

    public DirectedGraph() {
        this.MCount = 0;
        this.numOfEdges = 0;
        this.nodeMap = new HashMap<>();
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

    @Override
    public void connect(int src, int dest, double w) {
        GraphNode srcNode = (GraphNode) nodeMap.get(src);
        GraphNode destNode = (GraphNode) nodeMap.get(dest);
        GraphEdge edge = new GraphEdge(src, dest, w);
        //we need to check if that is a new node, or it overrides an existing one
        if (srcNode.getDestMap().get(dest) == null) {
            numOfEdges++;
        }
        srcNode.addDest(edge);
        destNode.addSrc(edge);
        MCount++;
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
        GraphNode currNode = (GraphNode) this.nodeMap.get(key);
        //remove this node from the dest mapping of each father
        currNode.getSourceMap().keySet().forEach(nodeKey -> {
            GraphNode currFather = (GraphNode) this.nodeMap.get(nodeKey);
            currFather.removeDest(currFather.getKey());
            MCount++;
            numOfEdges--;
        });
        //remove this node from each source map of its children
        currNode.getDestMap().keySet().forEach(nodeKey -> {
            GraphNode currChild = (GraphNode) this.nodeMap.get(nodeKey);
            currChild.removeSrc(currChild.getKey());
            MCount++;
            numOfEdges--;
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
        numOfEdges--;
        return destNode.removeSrc(src);
    }

    @Override
    public int nodeSize() {
        return this.nodeMap.size();
    }

    @Override
    public int edgeSize() {
        return numOfEdges;
    }

    @Override
    public int getMC() {
        return MCount;
    }

    public void loadGraph(String filename) {
        try {
            Gson gson = new Gson();
            Reader reader = Files.newBufferedReader(Paths.get(filename));
            JsonObject jsonObject = gson.fromJson(reader, JsonObject.class);
            ArrayList<GraphEdge> parsedEdges = new ArrayList<>();
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

            for (GraphEdge edge : parsedEdges) {
                GraphNode srcNode = (GraphNode) this.nodeMap.get(edge.getSrc());
                GraphNode dstNode = (GraphNode) this.nodeMap.get(edge.getDest());
                srcNode.addDest(edge);
                dstNode.addSrc(edge);
                numOfEdges++;
            }
            System.out.println("a;sldkfjas;ldkfja;sdlkfj");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
