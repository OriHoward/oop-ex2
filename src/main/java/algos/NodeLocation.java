package algos;

import api.GeoLocation;

import java.io.Serializable;

public class NodeLocation implements GeoLocation, Serializable {
    private double x;
    private double y;
    private double z;

    public NodeLocation(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public double x() {
        return this.x;
    }

    @Override
    public double y() {
        return this.y;
    }

    @Override
    public double z() {
        return this.z;
    }

    @Override
    public double distance(GeoLocation g) {
        double distX = Math.abs(this.x - g.x());
        double distY = Math.abs(this.y - g.y());
        double distZ = Math.abs(this.z - g.z());
        return Math.sqrt(Math.pow(distX,2) + Math.pow(distY,2) + Math.pow(distZ,2));
    }
}
