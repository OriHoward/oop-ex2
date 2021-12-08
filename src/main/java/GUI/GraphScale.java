package GUI;

import algos.DirectedGraphAlgorithms;
import api.NodeData;

import java.awt.geom.Point2D;
import java.util.Iterator;

public class GraphScale {
    DirectedGraphAlgorithms algos;
    Point2D minPoint;
    Point2D maxPoint;
    int width;
    int height;


    public GraphScale(DirectedGraphAlgorithms graph, int width, int height) {
        this.algos = graph;
        this.width = width;
        this.height = height;
        this.minPoint = new Point2D.Double();
        this.maxPoint = new Point2D.Double();
        setRange();
    }


    public void setRange() {
        double minX = Integer.MAX_VALUE;
        double minY = Integer.MAX_VALUE;
        double maxX = Integer.MIN_VALUE;
        double maxY = Integer.MIN_VALUE;
        Iterator<NodeData> nodeIter = this.algos.getGraph().nodeIter();
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
        double rangeOfX = Math.abs(this.minPoint.getX() - this.maxPoint.getX());
        double rangeOfY = Math.abs(this.minPoint.getY() - this.maxPoint.getY());
        double distX = Math.abs(this.maxPoint.getX() - point.getX());
        double distY = Math.abs(this.maxPoint.getY() - point.getY());
        double currPercentageOfX = (distX/rangeOfX);
        double currPercentageOfY = (distY/rangeOfY);
        double newLocationX = this.width*currPercentageOfX;
        double newLocationY = this.height*currPercentageOfY;
        point.setLocation(newLocationX,newLocationY);
        return point;
    }



}
