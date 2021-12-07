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
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Iterator;

import static javafx.scene.paint.Color.GREEN;

public class GraphGUI extends Application {
    final int WIDTH = 1080;
    final int HEIGHT = 800;
    DirectedGraphAlgorithms algos;
    ArrayList<Button> nodeList;
    Pane root;
    double radius = 16;


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
        exitBtn.setOnAction(e -> Platform.exit());
        root.getChildren().add(exitBtn);
        MenuButton menuButton = new MenuButton("Options");
        MenuItem firstItem = new MenuItem("reload different graph");
        MenuItem secondItem = new MenuItem("clean");
        firstItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {

            }
        });

        menuButton.getItems().addAll(firstItem,secondItem);
        Text text = new Text(10, 10, "click on each node to find more info");
        text.setFont(new Font(40));
        text.setFill(GREEN);
        text.setFont(Font.font("Helvetica", FontPosture.ITALIC, 30));
        text.setY(50);
        text.setX(50);
        text.setVisible(true);

        Group group = new Group(root, text,menuButton);


        AnimationTimer tm = new MyTimer();
        tm.start();

        Scene scene = new Scene(group, WIDTH, HEIGHT);
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
                double startX = nodeList.get(srcNode).getLayoutX() + radius;
                double startY = nodeList.get(srcNode).getLayoutY()+ radius;
                double endX = nodeList.get(destNode).getLayoutX()+ radius;
                double endY = nodeList.get(destNode).getLayoutY()+ radius;
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
                root.getChildren().add(line);
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
            double newPosX = Math.round((float) (x + Math.cos(angle) * radius));
            double newPosY = Math.round((float) (y + Math.sin(angle) * radius));
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
            root.getChildren().addAll(leftLine, rightLine);
        }

        private double calculateDist(double strPx, double strPy, double endPx, double endPy) {
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
                Button button = new Button();
                button.setStyle(
                        "-fx-background-radius: 10em; " +
                                "-fx-min-width: 30px; " +
                                "-fx-min-height: 30px; " +
                                "-fx-max-width: 30px; " +
                                "-fx-max-height: 30px;"
                );
                button.setText(String.valueOf(currNode.getKey()));
                button.setLayoutX(currPoint.getX() - radius);
                button.setLayoutY(currPoint.getY() - radius);
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
                root.getChildren().add(button);
                nodeList.add(button);
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
            if (centerY > HEIGHT - radius * 2) {
                button.setLayoutY(centerY - radius * 2);
            }
            if (centerY < 0 + radius * 2) {
                button.setLayoutY(centerY + radius * 2);
            }
            if (centerX > WIDTH - radius * 2) {
                button.setLayoutX(centerX - radius * 2);
            }
            if (centerX < 0 + radius * 2) {
                button.setLayoutX(centerX + radius * 2);
            }
        }
    }

    public static void main(String[] args) {
        launch(args);

    }
}
