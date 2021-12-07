package algos;

import api.EdgeData;
import api.GeoLocation;
import api.NodeData;
import com.google.gson.annotations.Expose;

import java.io.Serializable;
import java.util.HashMap;

public class GraphNode implements NodeData, Serializable {
    @Expose private GeoLocation pos;
    @Expose private int id;
    private double weight;

    private HashMap<Integer, EdgeData> destMap;
    private HashMap<Integer, EdgeData> sourceMap;
    private NodeTagEnum tag;

    public GraphNode(GeoLocation location, int id) {
        this.pos = new NodeLocation(location.x(), location.y(), location.z());
        this.id = id;
        this.weight = 0;
        this.destMap = new HashMap<>();
        this.sourceMap = new HashMap<>();
        tag = NodeTagEnum.WHITE;

    }

    public HashMap<Integer, EdgeData> getDestMap() {
        return destMap;
    }

    public HashMap<Integer, EdgeData> getSourceMap() {
        return sourceMap;
    }


    public void addDest(EdgeData edge) {
        this.destMap.put(edge.getDest(), edge);
    }

    public void setSourceMap(HashMap<Integer, EdgeData> sourceMap) {
        this.sourceMap = sourceMap;
    }

    public void setDestMap(HashMap<Integer, EdgeData> destMap) {
        this.destMap = destMap;
    }

    public void addSrc(EdgeData edge) {
        this.sourceMap.put(edge.getSrc(), edge);

    }

    public EdgeData removeDest(int dest) {
        return destMap.remove(dest);
    }

    public EdgeData removeSrc(int source) {
        return sourceMap.remove(source);
    }

    @Override
    public int getKey() {
        return this.id;
    }

    @Override
    public GeoLocation getLocation() {
        return this.pos;
    }

    @Override
    public void setLocation(GeoLocation p) {
        this.pos = new NodeLocation(p.x(), p.y(), p.z());
    }

    @Override
    public double getWeight() {
        return this.weight;
    }

    @Override
    public void setWeight(double w) {
        this.weight = w;
    }

    @Override
    public String getInfo() {
        return "position: " + pos.x() + ", " + pos.y() + ", " + pos.z() + "" +
                " id: " + this.id;
    }

    @Override
    public void setInfo(String s) {

    }

    @Override
    public int getTag() {
        return tag.getValue();
    }

    @Override
    public void setTag(int t) {
        this.tag = NodeTagEnum.values()[t];
    }
}
