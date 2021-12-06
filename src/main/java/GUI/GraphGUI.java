package GUI;

import algos.DirectedGraphAlgorithms;
import api.NodeData;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Iterator;

public class GraphGUI extends Application {
    final int WIDTH = 800;
    final int HEIGHT = 600;
    DirectedGraphAlgorithms algos;
    ArrayList<Circle> Nodes;
    Pane root;
    double r = 5;


    public GraphGUI() {
        algos = new DirectedGraphAlgorithms();
        algos.load("data/G1.json");
        Nodes = new ArrayList<>();
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
            doHandle();
        }

        private void doHandle() {
            GraphScale scale = new GraphScale(algos, WIDTH, HEIGHT);
            Iterator<NodeData> nodeIter = algos.getGraph().nodeIter();
            while (nodeIter.hasNext()) {
                NodeData currNode = nodeIter.next();
                System.out.println(currNode.getKey());
                Point2D currPoint = new Point2D.Double(currNode.getLocation().x(), currNode.getLocation().y());
                currPoint = scale.convert(currPoint);
                Circle circle = new Circle(currPoint.getX(), currPoint.getY(), r);
                checkIfOutOfBounds(circle);
                root.getChildren().add(circle);
                System.out.println(circle.getCenterX() + " , " + circle.getCenterY());
            }
            stop();
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                // Do nothing
            }
        }

        private void checkIfOutOfBounds(Circle circle) {
            double centerX = circle.getCenterX();
            double centerY = circle.getCenterY();
            if (centerY> HEIGHT - r) {
                circle.setCenterY(centerY- r);
            }
            if (centerY < 0 + r) {
                circle.setCenterY(centerY + r);
            }
            if (centerX > WIDTH -r) {
                circle.setCenterX(centerX -r);
            }
            if (centerX < 0 + r) {
                circle.setCenterX(centerX + r);
            }
        }
    }

    public static void main(String[] args) {
        launch(args);

    }
}
