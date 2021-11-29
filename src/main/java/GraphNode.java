import api.GeoLocation;
import api.NodeData;

import java.util.HashMap;

public class GraphNode implements NodeData {
    private GeoLocation location;
    private int id;
    private double weight;


    public GraphNode(GeoLocation location, int id, double weight) {
        this.location = new NodeLocation(location.x(), location.y(), location.z());
        this.id = id;
        this.weight = weight;
    }

    @Override
    public int getKey() {
        return this.id;
    }

    @Override
    public GeoLocation getLocation() {
        return this.location;
    }

    @Override
    public void setLocation(GeoLocation p) {
        this.location = new NodeLocation(p.x(), p.y(), p.z());
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
        return "position: " + location.x() + ", " + location.y() + ", " + location.z() + "" +
                "weight: " + this.getWeight() + "id: " + this.id;
    }

    @Override
    public void setInfo(String s) {

    }

    @Override
    public int getTag() {
        return 0;
    }

    @Override
    public void setTag(int t) {

    }
}
