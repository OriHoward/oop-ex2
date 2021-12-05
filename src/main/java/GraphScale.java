import api.NodeData;

import java.awt.geom.Point2D;
import java.util.Iterator;

public class GraphScale {
    DirectedGraph graph;
    Point2D minPoint;
    Point2D maxPoint;
    int width;
    int height;


    public GraphScale(DirectedGraph graph, int width, int height) {
        this.graph = graph;
        this.width = width;
        this.height = height;
        this.minPoint = new Point2D.Double();
        this.maxPoint = new Point2D.Double();
    }


    public void setRange() {
        double minX = Integer.MIN_VALUE;
        double minY = Integer.MIN_VALUE;
        double maxX = Integer.MIN_VALUE;
        double maxY = Integer.MIN_VALUE;
        Iterator<NodeData> nodeIter = this.graph.nodeIter();
        while (nodeIter.hasNext()) {
            NodeData currNode = nodeIter.next();
            minX = Math.min(currNode.getLocation().x(),minX);
            minY = Math.min(currNode.getLocation().y(),minY);
            maxX = Math.max(currNode.getLocation().x(),maxX);
            maxY = Math.max(currNode.getLocation().y(),maxY);
        }
        this.minPoint = new Point2D.Double(minX,minY);
        this.maxPoint = new Point2D.Double(maxX,maxY);
    }

    public Point2D convert(Point2D point) {
        setRange();
        double rangeOfX = Math.abs(this.minPoint.getX() - this.maxPoint.getX());
        double rangeOfY = Math.abs(this.minPoint.getY() - this.maxPoint.getY());
        double currPercentageOfX = (point.getX()/rangeOfX);
        double currPercentageOfY = (point.getY()/rangeOfY);
        double newLocationX = this.width*currPercentageOfX;
        double newLocationY = this.width*currPercentageOfY;
        point.setLocation(newLocationX,newLocationY);
        return point;
    }



}
