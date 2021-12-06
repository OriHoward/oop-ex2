package GUI;

import algos.DirectedGraphAlgorithms;
import api.EdgeData;
import api.NodeData;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.stage.Stage;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import javafx.scene.text.*;
import java.awt.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Iterator;

public class GraphGUI extends Application {
    final int WIDTH = 1080;
    final int HEIGHT = 800;
    DirectedGraphAlgorithms algos;
    ArrayList<Button> nodeList;
    Pane root;
    double radius = 32;


    public GraphGUI() {
        algos = new DirectedGraphAlgorithms();
        algos.load("data/G1.json");
        nodeList = new ArrayList<>();
    }


    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Graph");
        root = new Pane();
        Button exitBtn = new Button("exit");
        exitBtn.setLayoutX(0);
        exitBtn.setLayoutY(0);
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
                int srcNode = algos.getGraph().getNode(currEdge.getSrc()).getKey();
                int destNode = algos.getGraph().getNode(currEdge.getDest()).getKey();
                double startX = nodeList.get(srcNode).getLayoutX();
                double startY = nodeList.get(srcNode).getLayoutY();
                double endX = nodeList.get(destNode).getLayoutX();
                double endY = nodeList.get(destNode).getLayoutY();
                Line line = new Line();
                line.setStyle("-fx-stroke: blue;");
                line.setStartX(startX);
                line.setStartY(startY);
                line.setEndX(endX);
                line.setEndY(endY);
                drawArrow(startX,startY,endX,endY);
                root.getChildren().add(line);
            }
        }

        private void drawArrow(double strPx, double strPy, double endPx,double endPy) {
//            double strPx = startPoint.getCenterX();
//            double strPy = startPoint.getCenterY();
//            double endPx = endPoint.getCenterX();
//            double endPy = endPoint.getCenterY();
            double dist = calculateDist(strPx,strPy,endPx,endPy);
//            int angelNumber = checkAngle(startPoint, endPoint);
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
            root.getChildren().addAll(leftLine, rightLine);
        }

//        private void changePointsByRatio(Circle startPoint, Circle endPoint, double endPx, double endPy) {
        //            if (angelNumber == 1) {
//                endPx = endPx - radius;
//                endPy = endPy + radius;
//            }
//            if (angelNumber == 2) {
//                endPx = endPx + radius;
//                endPy = endPy + radius;
//            }
//            if (angelNumber == 3) {
//                endPx = endPx + radius;
//                endPy = endPy - radius;
//            }
//            if (angelNumber == 4) {
//                endPx = endPx - radius;
//                endPy = endPy - radius;
//            }
//            if (angelNumber == 5) {
//                endPy = endPy - radius;
//            }
//            if (angelNumber == 6) {
//                endPy = endPy + radius;
//            }
//            if (angelNumber == 7) {
//                endPx = endPx - radius;
//            }
//            if (angelNumber == 8){
//                endPx = endPx + radius;
//            }
//    }
//


        private int checkAngle(Circle startPoint, Circle endPoint) {
            if (endPoint.getCenterX() > startPoint.getCenterX() && endPoint.getCenterY() < startPoint.getCenterY()) {
                return 1;
            }
            if (endPoint.getCenterY() < startPoint.getCenterY() && endPoint.getCenterX() < startPoint.getCenterX()) {
                return 2;
            }
            if (endPoint.getCenterY() > startPoint.getCenterY() && endPoint.getCenterX() < startPoint.getCenterX()) {
                return 3;
            }
            if (endPoint.getCenterX() > startPoint.getCenterX() && endPoint.getCenterY() > startPoint.getCenterY()) {
                return 4;
            }
            // 5 - if end point is arrow up
            if (endPoint.getCenterX() == startPoint.getCenterX() && endPoint.getCenterY() < startPoint.getCenterY()) {
                return 5;
            }
            // 6 - if end point is arrow down
            if (endPoint.getCenterX() == startPoint.getCenterX() && endPoint.getCenterY() > startPoint.getCenterY()) {
                return 6;
            }
            // 7 - if end point is arrow right
            if (endPoint.getCenterX() > startPoint.getCenterX() && endPoint.getCenterY() == startPoint.getCenterY()) {
                return 7;
            }
            // 8 - if end point is arrow left
            if (endPoint.getCenterX() < startPoint.getCenterX() && endPoint.getCenterY() == startPoint.getCenterY()) {
                return 8;
            }
            return 0;
        }

        private double calculateDist(double strPx, double strPy, double endPx,double endPy) {
            double distX = Math.pow(Math.abs(strPx - endPx), 2);
            double distY = Math.pow(Math.abs(strPy - endPy), 2);
            return Math.sqrt(distX + distY);
        }

        private void drawNodes() {
            GraphScale scale = new GraphScale(algos, WIDTH, HEIGHT);
            Iterator<NodeData> nodeIter = algos.getGraph().nodeIter();
            while (nodeIter.hasNext()) {
                NodeData currNode = nodeIter.next();
                Point2D currPoint = new Point2D.Double(currNode.getLocation().x(), currNode.getLocation().y());
                currPoint = scale.convert(currPoint);
//                Circle circle = new Circle(currPoint.getX(), currPoint.getY(), radius);
//                checkBounds(circle);

//                circle.setId("1");

//                circle.setFill(javafx.scene.paint.Color.RED);

                Button button = new Button();
                button.setStyle(
                        "-fx-background-radius: 10em; " +
                                "-fx-min-width: 30px; " +
                                "-fx-min-height: 30px; " +
                                "-fx-max-width: 30px; " +
                                "-fx-max-height: 30px;"
                );
                button.setText(String.valueOf(currNode.getKey()));
                button.setLayoutX(currPoint.getX());
                button.setLayoutY(currPoint.getY());
                checkBounds(button);
                button.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent actionEvent) {
                        Stage stage = new Stage();
                        stage.setTitle("Info about the Node");
                        Text text = new Text(50,50,currNode.getInfo());
                        text.setFont(new Font(40));
                        text.setVisible(true);
                        Scene scene = new Scene(new Group(text));
                        stage.setScene(scene);
                        stage.show();
                    }
                });start();

//                root.getChildren().add(circle);
                root.getChildren().add(button);
                nodeList.add(button);
            }
            stop();
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                // Do nothing
            }
        }

        private void checkBounds(Button button) {
            double centerX = button.getLayoutX();
            double centerY = button.getLayoutY();
            if (centerY > HEIGHT - radius) {
                button.setLayoutY(centerY - radius);
            }
            if (centerY < 0 + radius) {
                button.setLayoutY(centerY + radius);
            }
            if (centerX > WIDTH - radius) {
                button.setLayoutX(centerX - radius);
            }
            if (centerX < 0 + radius) {
                button.setLayoutX(centerX + radius);
            }
        }
    }

    public static void main(String[] args) {
        launch(args);

    }
}
