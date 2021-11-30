import api.DirectedWeightedGraph;
import api.EdgeData;
import api.NodeData;
import com.google.gson.Gson;
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
            ArrayList<GraphEdge> edgez = new ArrayList<>(Arrays.asList(gson.fromJson(jsonObject.get("Edges"), GraphEdge[].class)));
            ArrayList<GraphNode> nodez = new ArrayList<>(Arrays.asList(gson.fromJson(jsonObject.get("Nodes"), GraphNode[].class)));

            reader.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
