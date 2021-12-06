package GUI;

import algos.DirectedGraphAlgorithms;
import api.EdgeData;
import api.NodeData;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.stage.Stage;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Iterator;

public class GraphGUI extends Application {
    final int WIDTH = 1080;
    final int HEIGHT = 800;
    DirectedGraphAlgorithms algos;
    ArrayList<Circle> nodeList;
    Pane root;
    double radius = 10;


    public GraphGUI() {
        algos = new DirectedGraphAlgorithms();
        algos.load("data/G2.json");
        nodeList = new ArrayList<>();
    }


    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Graph");
        root = new Pane();
        Button exitBtn = new Button("Exit");
        exitBtn.setOnAction(e -> Platform.exit());
        root.getChildren().add(exitBtn);
        //        Group root = new Group();
//        MenuBar menuBar = new MenuBar();
//        VBox layout = new VBox(menuBar);
//        Menu runMenu = new Menu("Run");
//        Menu editMenu = new Menu("Edit");
//        menuBar.getMenus().add(runMenu);
//        menuBar.getMenus().add(editMenu);

        AnimationTimer tm = new MyTimer();
        tm.start();


        Scene scene = new Scene(root, WIDTH, HEIGHT);
        primaryStage.setScene(scene);
        primaryStage.show();

    }

    private class MyTimer extends AnimationTimer {

        @Override
        public void handle(long l) {
            drawNodes();
            connectNodes();
        }

        private void connectNodes() {
            Iterator<EdgeData> edgeIter = algos.getGraph().edgeIter();
            while (edgeIter.hasNext()) {
                EdgeData currEdge = edgeIter.next();
                Line line = new Line();
                int srcNode = algos.getGraph().getNode(currEdge.getSrc()).getKey();
                int destNode = algos.getGraph().getNode(currEdge.getDest()).getKey();
                line.setStartX(nodeList.get(srcNode).getCenterX());
                line.setStartY(nodeList.get(srcNode).getCenterY());
                line.setEndX(nodeList.get(destNode).getCenterX());
                line.setEndY(nodeList.get(destNode).getCenterY());
                drawArrow(nodeList.get(srcNode), nodeList.get(destNode));
                root.getChildren().add(line);
            }
        }

        private void drawArrow(Circle startPoint, Circle endPoint) {

            double strPx = startPoint.getCenterX();
            double strPy = startPoint.getCenterY();
            double endPx = endPoint.getCenterX();
            double endPy = endPoint.getCenterY();
            double slope = (strPy - endPy)/(strPx - endPx);



            double dist = calculateDist(startPoint,endPoint);
            double leftX = endPx + ((15/dist)* (((strPx - endPx)*Math.cos(50)) + ((strPy-endPy)*(Math.sin(50)))));
            double leftY = endPy + ((15/dist)* (((strPy - endPy)*Math.cos(50)) - ((strPx-endPx)*(Math.sin(50)))));
            double rightX = endPx + ((15/dist)* (((strPx - endPx)*Math.cos(50)) - ((strPy-endPy)*(Math.sin(50)))));
            double rightY = endPy + ((15/dist)* (((strPy - endPy)*Math.cos(50)) + ((strPx-endPx)*(Math.sin(50)))));
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
            root.getChildren().addAll(leftLine,rightLine);
        }


        private int checkQuarter(Circle startPoint, Circle endPoint) {
            if (endPoint.getCenterX() > startPoint.getCenterX() && endPoint.getCenterY() > startPoint.getCenterY()) {
                return 1;
            }
            if (endPoint.getCenterY() > startPoint.getCenterY() && endPoint.getCenterX() < startPoint.getCenterX()) {
                return 2;
            }
            if (endPoint.getCenterY() < startPoint.getCenterY() && endPoint.getCenterX() < startPoint.getCenterX()) {
                return 3;
            }
            return 4;
        }

        private double calculateDist(Circle startPoint, Circle endPoint) {
            double distX = Math.pow(Math.abs(startPoint.getCenterX() - endPoint.getCenterX()), 2);
            double distY = Math.pow(Math.abs(startPoint.getCenterY() - endPoint.getCenterY()), 2);
            return Math.sqrt(distX + distY);
        }

        private void drawNodes() {
            GraphScale scale = new GraphScale(algos, WIDTH, HEIGHT);
            Iterator<NodeData> nodeIter = algos.getGraph().nodeIter();
            while (nodeIter.hasNext()) {
                NodeData currNode = nodeIter.next();
                System.out.println(currNode.getKey());
                Point2D currPoint = new Point2D.Double(currNode.getLocation().x(), currNode.getLocation().y());
                currPoint = scale.convert(currPoint);
                Circle circle = new Circle(currPoint.getX(), currPoint.getY(), radius);
                checkBounds(circle);
                circle.setFill(javafx.scene.paint.Color.RED);
                root.getChildren().add(circle);
                nodeList.add(circle);
                System.out.println(circle.getCenterX() + " , " + circle.getCenterY());
            }
            stop();
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                // Do nothing
            }
        }

        private void checkBounds(Circle circle) {
            double centerX = circle.getCenterX();
            double centerY = circle.getCenterY();
            if (centerY > HEIGHT - radius) {
                circle.setCenterY(centerY - radius);
            }
            if (centerY < 0 + radius) {
                circle.setCenterY(centerY + radius);
            }
            if (centerX > WIDTH - radius) {
                circle.setCenterX(centerX - radius);
            }
            if (centerX < 0 + radius) {
                circle.setCenterX(centerX + radius);
            }
        }
    }

    public static void main(String[] args) {
        launch(args);

    }
}
