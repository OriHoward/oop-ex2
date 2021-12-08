package GUI;

import algos.DirectedGraphAlgorithms;
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
import javafx.stage.Window;

import java.io.File;
import java.util.ArrayList;

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

        MenuButton run = new MenuButton("RunAlgorithm");
        MenuItem isConnected = new MenuItem("isConnected");
        MenuItem shortestPathDist = new MenuItem("shortestPathDist");
        MenuItem shortestPath = new MenuItem("shortestPath");
        MenuItem center = new MenuItem("center");
        MenuItem tsp = new MenuItem("tsp");
        run.setLayoutX(50);


        run.getItems().addAll(isConnected, shortestPathDist, shortestPath, center, tsp);
        file.getItems().addAll(load, save, exit);
        Text text = new Text(10, 10, "click on each node to find more info");
        text.setFont(new Font(40));
        text.setFill(GREEN);
        text.setFont(Font.font("Helvetica", FontPosture.ITALIC, 30));
        text.setY(50);
        text.setX(50);
        text.setVisible(true);

        Group group = new Group(root, text, file, run);

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
            TextField userInput = new TextField("enter source and press OK ");
            Button ok = new Button("OK");
            ok.setOnAction(e -> isInt(userInput,userInput.getText()));
            VBox layout = new VBox(10);
            layout.setPadding(new Insets(20,20,20,20));
            layout.getChildren().addAll(userInput,ok);
            Scene sc = new Scene(layout,300,300);
            stage.setScene(sc);
            stage.show();
        });


    }

    private void isInt(TextField userInput, String number) {
        try {
            int num = Integer.parseInt(number);
            userInput.clear();
            userInput.setStyle(".text-field.error {\n" +
                    "  -fx-text-box-border: green ;\n" +
                    "  -fx-focus-color: green ;\n" +
                    "}");
            System.out.println("num is:" + number);
        }catch (NumberFormatException e) {
            userInput.setStyle(".text-field.error {\n" +
                    "  -fx-text-box-border: red ;\n" +
                    "  -fx-focus-color: red ;\n" +
                    "}");

            System.out.println("ERROR");
        }
    }

    private void savePopWindow(Boolean isSaved) {
        Stage popWindow = new Stage();
        popWindow.initModality(Modality.APPLICATION_MODAL);
        Label label;
        if (isSaved) {
            label = new Label("Graph saved successfully!");
        }
        else {
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
