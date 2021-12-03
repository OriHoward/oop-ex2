import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.awt.*;

public class GraphGUI extends Application {
    final int WIDTH = 800;
    final int HEIGHT = 600;
    DirectedGraph algo;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        algo = new DirectedGraph();
        algo.loadGraph("data/G1.json");
        primaryStage.setTitle("Graph");
        StackPane layout = new StackPane();
        Scene scene = new Scene(layout, WIDTH, HEIGHT);
        primaryStage.setScene(scene);
        primaryStage.show();
        for (int i = 0; i < algo.nodeMap.size(); i++) {
            Button button = new Button();
            button.setLabel("node: " + i);
        }


    }
}
