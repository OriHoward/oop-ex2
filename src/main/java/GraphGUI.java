import api.DirectedWeightedGraph;
import api.EdgeData;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.util.Iterator;

public class GraphGUI extends Application {
    final int WIDTH = 800;
    final int HEIGHT = 600;
    DirectedWeightedGraph g;

    public GraphGUI() {
        g = new DirectedGraph();
    }


    public static void main(String[] args) {
        launch(args);
    }




    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Graph");
        Line line = new Line();
        line.setStartX(200);
        line.setStartY(200);
        line.setEndX(500);
        line.setEndY(200);
        line.setStrokeWidth(5);
//        Iterator<EdgeData> edgeDataIterator = g.edgeIter();
//        while (edgeDataIterator.hasNext()){
//            EdgeData currEdge = edgeDataIterator.next();
//            line.setStartX(currEdge.getSrc());
//            line.setStartY(currEdge.getSrc());
//        }

        MenuBar menuBar = new MenuBar();
        VBox layout = new VBox(menuBar);
        Menu runMenu = new Menu("Run");
        Menu editMenu = new Menu("Edit");

        menuBar.getMenus().add(runMenu);

        menuBar.getMenus().add(editMenu);
        layout.getChildren().addAll(line);
        Scene scene = new Scene(layout, WIDTH, HEIGHT);
        primaryStage.setScene(scene);
        primaryStage.show();


    }
}
