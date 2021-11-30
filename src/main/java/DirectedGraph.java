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
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;


public class DirectedGraph implements DirectedWeightedGraph {
    private int MCount;
    private int numOfEdges;
    HashMap<Integer, GraphNode> nodeMap;

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
                GraphNode srcNode = this.nodeMap.get(edge.getSrc());
                GraphNode dstNode = this.nodeMap.get(edge.getDest());
                srcNode.addDest(edge);
                dstNode.addSrc(edge);
            }
            System.out.println("a;sldkfjas;ldkfja;sdlkfj");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
