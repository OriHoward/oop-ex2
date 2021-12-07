package algos;

import api.EdgeData;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class GraphEdge implements EdgeData, Serializable {
    @SerializedName(value = "src")
    @Expose private int source;
    @SerializedName(value = "w")
    @Expose private double weight;
    @Expose private int dest;

    public void setSource(int source) {
        this.source = source;
    }

    public void setDest(int dest) {
        this.dest = dest;
    }

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
        return "source: " + getSrc() + " dest: " + getDest() + " weight: " + getWeight();
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
