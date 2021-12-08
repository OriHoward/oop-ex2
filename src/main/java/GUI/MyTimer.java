package GUI;

import algos.DirectedGraphAlgorithms;
import api.EdgeData;
import api.NodeData;
import javafx.animation.AnimationTimer;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Iterator;

public class MyTimer extends AnimationTimer {

    private double radius;
    private DirectedGraphAlgorithms algos;
    private Pane root;
    ArrayList<Button> nodeList;
    private int width;
    private int height;

    public MyTimer(DirectedGraphAlgorithms algos, Pane root,double radius,ArrayList<Button> nodeList,int width,int height) {
        this.algos = algos;
        this.root = root;
        this.radius = radius;
        this.nodeList = nodeList;
        this.width = width;
        this.height = height;
    }

    @Override
    public void handle(long l) {
        drawNodes();
        connectNodes();
    }

    private void connectNodes() {
        Iterator<EdgeData> edgeIter = this.algos.getGraph().edgeIter();
        while (edgeIter.hasNext()) {
            EdgeData currEdge = edgeIter.next();
            int srcNode = this.algos.getGraph().getNode(currEdge.getSrc()).getKey();
            int destNode = this.algos.getGraph().getNode(currEdge.getDest()).getKey();
            double startX = this.nodeList.get(srcNode).getLayoutX() + this.radius;
            double startY = this.nodeList.get(srcNode).getLayoutY()+ this.radius;
            double endX = this.nodeList.get(destNode).getLayoutX()+ this.radius;
            double endY = this.nodeList.get(destNode).getLayoutY()+ this.radius;
            double startAngle = startAngleInBetween(startX,startY,endX,endY);
            Point2D endPoint = correctPoint(endX,endY,startAngle);
            double endAngle = endAngleInBetween(startX,startY,endX,endY);
            Point2D startPoint = correctPoint(startX,startY,endAngle);
            double correctEndX = endPoint.getX();
            double correctEndY = endPoint.getY();
            double correctStartX = startPoint.getX();
            double correctStartY = startPoint.getY();
            Line line = new Line();
            line.setStyle("-fx-stroke: blue;");
            line.setStartX(correctStartX);
            line.setStartY(correctStartY);
            line.setEndX(correctEndX);
            line.setEndY(correctEndY);
            drawArrow(correctStartX,correctStartY, correctEndX,correctEndY);
            this.root.getChildren().add(line);
        }
    }

    private double endAngleInBetween(double p1X, double p1Y, double p2X, double p2Y) {
        double distX = p2X-p1X;
        double distY = p2Y-p1Y;
        double angle = -Math.atan2(distX, distY);
        angle = Math.toRadians(Math.toDegrees(angle) + 180);
        return angle;
    }


    private double startAngleInBetween(double p1X, double p1Y, double p2X, double p2Y) {
        double distX = p1X - p2X;
        double distY = p1Y - p2Y;
        double angle = -Math.atan2(distX, distY);
        angle = Math.toRadians(Math.toDegrees(angle) + 180);
        return angle;
    }

    private Point2D correctPoint(double x, double y, double angle) {
        angle = angle - Math.toRadians(90.0);
        double newPosX = Math.round((float) (x + Math.cos(angle) * this.radius));
        double newPosY = Math.round((float) (y + Math.sin(angle) * this.radius));
        return new Point2D.Double(newPosX, newPosY);
    }



    private void drawArrow(double strPx, double strPy, double endPx, double endPy) {
        double dist = calculateDist(strPx, strPy, endPx, endPy);
        double leftX = endPx + ((15 / dist) * (((strPx - endPx) * Math.cos(50)) + ((strPy - endPy) * (Math.sin(50)))));
        double leftY = endPy + ((15 / dist) * (((strPy - endPy) * Math.cos(50)) - ((strPx - endPx) * (Math.sin(50)))));
        double rightX = endPx + ((15 / dist) * (((strPx - endPx) * Math.cos(50)) - ((strPy - endPy) * (Math.sin(50)))));
        double rightY = endPy + ((15 / dist) * (((strPy - endPy) * Math.cos(50)) + ((strPx - endPx) * (Math.sin(50)))));
        Line leftLine = new Line();
        Line rightLine = new Line();
        rightLine.setStyle("-fx-stroke: blue;");
        leftLine.setStartX(endPx);
        leftLine.setStartY(endPy);
        rightLine.setStartX(endPx);
        rightLine.setStartY(endPy);

        leftLine.setEndX(leftX);
        leftLine.setEndY(leftY);
        rightLine.setEndX(rightX);
        rightLine.setEndY(rightY);
        this.root.getChildren().addAll(leftLine, rightLine);
    }

    private double calculateDist(double strPx, double strPy, double endPx, double endPy) {
        double distX = Math.pow(Math.abs(strPx - endPx), 2);
        double distY = Math.pow(Math.abs(strPy - endPy), 2);
        return Math.sqrt(distX + distY);
    }

    private void drawNodes() {
        GraphScale scale = new GraphScale(this.algos, this.width, this.height);
        Iterator<NodeData> nodeIter = this.algos.getGraph().nodeIter();
        while (nodeIter.hasNext()) {
            NodeData currNode = nodeIter.next();
            Point2D currPoint = new Point2D.Double(currNode.getLocation().x(), currNode.getLocation().y());
            currPoint = scale.convert(currPoint);
            Button button = new Button();
            button.setStyle(
                    "-fx-background-radius: 8em; " +
                            "-fx-min-width: 20px; " +
                            "-fx-min-height: 20px; " +
                            "-fx-max-width: 20px; " +
                            "-fx-max-height: 20px;"
            );
            button.setText(String.valueOf(currNode.getKey()));
            button.setLayoutX(currPoint.getX() - this.radius);
            button.setLayoutY(currPoint.getY() - this.radius);
            checkBounds(button);
            button.setOnAction(actionEvent -> {
                Stage stage = new Stage();
                stage.setTitle("Info about the Node");
                Text text = new Text(50, 50, currNode.getInfo());
                text.setFont(new Font(40));
                text.setVisible(true);
                Scene scene = new Scene(new Group(text));
                stage.setScene(scene);
                stage.show();
            });
            start();
            this.root.getChildren().add(button);
            this.nodeList.add(button);
        }
        stop();
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            System.out.println("ERROR");
        }
    }

    private void checkBounds(Button button) {
        double centerX = button.getLayoutX();
        double centerY = button.getLayoutY();
        if (centerY > this.height - this.radius * 2) {
            button.setLayoutY(centerY - this.radius * 2);
        }
        if (centerY < this.radius * 2) {
            button.setLayoutY(centerY + this.radius * 2);
        }
        if (centerX > this.width - this.radius * 2) {
            button.setLayoutX(centerX - this.radius * 2);
        }
        if (centerX < this.radius * 2) {
            button.setLayoutX(centerX + this.radius * 2);
        }
    }
}