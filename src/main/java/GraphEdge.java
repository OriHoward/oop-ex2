import api.EdgeData;

import java.util.ArrayList;

public class GraphEdge implements EdgeData {
    private int source;
    private int dest;
    private double weight;

    public GraphEdge(int source, int dest, double weight) {
        this.source = source;
        this.dest = dest;
        this.weight = weight;
    }

    @Override
    public int getSrc() {
        return this.source;
    }

    @Override
    public int getDest() {
        return this.dest;
    }

    @Override
    public double getWeight() {
        return this.weight;
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
