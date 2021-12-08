package GUI;

import algos.DirectedGraphAlgorithms;
import api.NodeData;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static javafx.scene.paint.Color.GREEN;

public class GraphGUI extends Application {
    final int WIDTH = 1080;
    final int HEIGHT = 800;
    static DirectedGraphAlgorithms algos;
    ArrayList<Button> nodeList = new ArrayList<>();
    Pane root;
    double radius = 10;

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Graph");
        root = new Pane();
        nodeList.clear();
        FileChooser fileChooser = new FileChooser();

        MenuButton file = new MenuButton("File");
        MenuItem load = new MenuItem("Load");
        MenuItem save = new MenuItem("Save");
        MenuItem exit = new MenuItem("Exit");

        MenuButton edit = new MenuButton("Edit");
        Menu nodes = new Menu("Nodes");
        Menu edges = new Menu("Edges");

        MenuItem addNode = new MenuItem("addNode");
        MenuItem removeNode = new MenuItem("removeNode");
        MenuItem addEdge = new MenuItem("addEdge");
        MenuItem removeEdge = new MenuItem("removeEdge");
        nodes.getItems().addAll(addNode, removeNode);
        edges.getItems().addAll(addEdge, removeEdge);
        edit.getItems().addAll(nodes, edges);
        edit.setLayoutX(50);

        MenuButton run = new MenuButton("RunAlgorithm");
        MenuItem isConnected = new MenuItem("isConnected");
        MenuItem shortestPathDist = new MenuItem("shortestPathDist");
        MenuItem shortestPath = new MenuItem("shortestPath");
        MenuItem center = new MenuItem("center");
        MenuItem tsp = new MenuItem("tsp");
        run.setLayoutX(102);

        run.getItems().addAll(isConnected, shortestPathDist, shortestPath, center, tsp);
        file.getItems().addAll(load, save, exit);
        Text text = new Text(10, 10, "click on each node to find more info");
        text.setFont(new Font(40));
        text.setFill(GREEN);
        text.setFont(Font.font("Helvetica", FontPosture.ITALIC, 18));
        text.setX(30);
        text.setY(60);
        text.setVisible(true);

        Group group = new Group(root, text, file, edit, run);

        AnimationTimer tm = new MyTimer(algos, root, radius, nodeList, WIDTH, HEIGHT);
        tm.start();

        Scene scene = new Scene(group, WIDTH, HEIGHT);
        primaryStage.setScene(scene);
        primaryStage.show();

        // File options:
        load.setOnAction(actionEvent -> {
            File chosenFile = fileChooser.showOpenDialog(primaryStage);
            if (chosenFile != null) {
                String path = chosenFile.getAbsolutePath();
                algos.load(path);
                primaryStage.close();
                try {
                    start(primaryStage);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        save.setOnAction(actionEvent -> {
            savePopWindow(algos.save("myGraph"));
        });
        exit.setOnAction(e -> Platform.exit());


        // run Algorithms options:
        isConnected.setOnAction(actionEvent -> {
            isConnectedPopWindow(algos.isConnected());
        });


        shortestPath.setOnAction(actionEvent -> {
            Stage stage = new Stage();
            Label source = new Label("enter source between: 0 - " + (algos.getGraph().nodeSize() -1));
            Label dest = new Label("enter dest between: 0 - " + (algos.getGraph().nodeSize() -1));
            TextField sourceInput = new TextField();
            TextField destInput = new TextField();
            Button button = new Button("ENTER");
            button.setOnAction(e -> {
                checkInput(algos.getGraph().nodeSize(),sourceInput,destInput);
            });

            VBox layout = new VBox(10);
            layout.setPadding(new Insets(20, 20, 20, 20));
            layout.getChildren().addAll(source, sourceInput, dest, destInput, button);

            Scene sc = new Scene(layout, 300, 300);
            stage.setScene(sc);
            stage.show();
        });


    }

    private Boolean isInt(TextField sourceInput, TextField destInput) {
        try {
            Integer.parseInt(sourceInput.getText());
            Integer.parseInt(destInput.getText());
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private void checkInput(int maxNum, TextField sourceInput, TextField destInput) {
        Stage popWindow = new Stage();
        Label label;
        if (isInt(sourceInput, destInput)) {
            int src = Integer.parseInt(sourceInput.getText());
            int dest = Integer.parseInt(destInput.getText());
            if (src > maxNum || dest > maxNum || src < 0 || dest < 0) {
                label = new Label("please enter numbers in range");
            } else {
                sourceInput.clear();
                destInput.clear();
                List<NodeData> trail = algos.shortestPath(src,dest);
                StringBuilder prev = new StringBuilder();
                prev.append("Fastest path: ");
                for (int i = 0; i < trail.size() -1; i++) {
                    prev.append(trail.get(i).getKey()).append("-> ");
                }
                prev.append(trail.get(trail.size() -1).getKey());
                label = new Label(prev.toString());
            }
        } else {
            label = new Label("Please enter numbers only");
        }
        popWindow.initModality(Modality.APPLICATION_MODAL);
        Button button = new Button("close");
        button.setOnAction(e -> popWindow.close());
        VBox layout = new VBox(10);
        layout.getChildren().addAll(label, button);
        layout.setAlignment(Pos.CENTER);
        Scene popScene = new Scene(layout, 450, 400);
        popWindow.setScene(popScene);
        popWindow.showAndWait();
    }

    private void savePopWindow(Boolean isSaved) {
        Stage popWindow = new Stage();
        popWindow.initModality(Modality.APPLICATION_MODAL);
        Label label;
        if (isSaved) {
            label = new Label("Graph saved successfully!");
        } else {
            label = new Label("could not save graph");
        }
        Button button = new Button("close");
        button.setOnAction(e -> popWindow.close());
        VBox layout = new VBox(10);
        layout.getChildren().addAll(label, button);
        layout.setAlignment(Pos.CENTER);
        Scene popScene = new Scene(layout, 150, 100);
        popWindow.setScene(popScene);
        popWindow.showAndWait();
    }

    private void isConnectedPopWindow(Boolean isConnected) {
        Stage popWindow = new Stage();
        popWindow.initModality(Modality.APPLICATION_MODAL);
        Label label;
        if (isConnected) {
            label = new Label("the graph is connected!");
        } else {
            label = new Label("the graph is NOT connected!");
        }
        Button button = new Button("close");
        button.setOnAction(e -> popWindow.close());
        VBox layout = new VBox(10);
        layout.getChildren().addAll(label, button);
        layout.setAlignment(Pos.CENTER);
        Scene popScene = new Scene(layout, 250, 200);
        popWindow.setScene(popScene);
        popWindow.showAndWait();
    }


    public static void main(String[] args, DirectedGraphAlgorithms graphAlgo) {
        algos = graphAlgo;
        launch(args);
    }
}
