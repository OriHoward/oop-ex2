import api.GeoLocation;
import api.NodeData;

public class GraphNode implements NodeData {
    private int pos;
    private int id;

    public GraphNode (int pos, int id) {

    }
    @Override
    public int getKey() {
        return 0;
    }

    @Override
    public GeoLocation getLocation() {
        return null;
    }

    @Override
    public void setLocation(GeoLocation p) {

    }

    @Override
    public double getWeight() {
        return 0;
    }

    @Override
    public void setWeight(double w) {

    }

    @Override
    public String getInfo() {
        return null;
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
