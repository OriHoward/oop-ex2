import api.NodeData;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Group;
import javafx.scene.shape.Circle;

import java.awt.geom.Point2D;
import java.util.Iterator;

public class GraphGUI extends Application {
    final int WIDTH = 800;
    final int HEIGHT = 600;
    DirectedGraph g;

    public GraphGUI() {
        g = new DirectedGraph();
    }


    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Graph");
        Group root = new Group();
//        MenuBar menuBar = new MenuBar();
//        VBox layout = new VBox(menuBar);
//        Menu runMenu = new Menu("Run");
//        Menu editMenu = new Menu("Edit");
//        menuBar.getMenus().add(runMenu);
//        menuBar.getMenus().add(editMenu);

        GraphScale scale = new GraphScale(g,WIDTH,HEIGHT);
        Iterator<NodeData> nodeIter = g.nodeIter();
//        NodeData currNode = nodeIter.next();
//        Point2D currPoint = new Point2D.Double(currNode.getLocation().x(),currNode.getLocation().y());
//        currPoint = scale.convert(currPoint);
//        Circle circle = new Circle(currPoint.getX(),currPoint.getY(),5);
//        root.getChildren().add(circle);

        new AnimationTimer() {
            @Override
            public void handle(long l) {
                while (nodeIter.hasNext()) {
                    NodeData currNode = nodeIter.next();
                    Point2D currPoint = new Point2D.Double(currNode.getLocation().x(),currNode.getLocation().y());
                    currPoint = scale.convert(currPoint);
                    Circle circle = new Circle(currPoint.getX(),currPoint.getY(),5);
                    root.getChildren().add(circle);
                    System.out.println("hello");
                }
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    // Do nothing
                }
            }
        }.start();


        Scene scene = new Scene(root, WIDTH, HEIGHT);
        primaryStage.setScene(scene);
        primaryStage.show();


    }
}
