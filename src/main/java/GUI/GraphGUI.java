package GUI;

import algos.DirectedGraphAlgorithms;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
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
        MenuButton file = new MenuButton("File");
        MenuItem load = new MenuItem("Load");
        MenuItem save = new MenuItem("Save");
        MenuItem exit = new MenuItem("Exit");
        exit.setOnAction(e -> Platform.exit());
        MenuButton run = new MenuButton("Run");
        MenuItem shortestPath = new MenuItem("shortest path");
        run.setLayoutX(50);


        final FileChooser fileChooser = new FileChooser();
        run.getItems().addAll(shortestPath);
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
            if (algos.save("myGraph")) {
                Stage popWindow=new Stage();
                popWindow.initModality(Modality.APPLICATION_MODAL);
                Label label= new Label("Graph saved successfully!");
                Button button= new Button("close");
                button.setOnAction(e -> popWindow.close());
                VBox layout= new VBox(10);
                layout.getChildren().addAll(label, button);
                layout.setAlignment(Pos.CENTER);
                Scene popScene= new Scene(layout, 150, 100);
                popWindow.setScene(popScene);
                popWindow.showAndWait();
            }
        });

//        shortestPath.setOnAction(actionEvent -> {
//            Stage stage = new Stage();
//            stage.setTitle("enter source and dest");
//            TextField textField = new TextField("enter source");
//            StackPane stackPane = new StackPane();
//            stackPane.getChildren().addAll(textField);
//            Scene scene = new Scene(stackPane, 300, 300);
//            stage.setScene(scene);
//            stage.show();
//            textField.setOnAction(e -> {
//                textField.setOnKeyPressed(keyEvent -> {
//                    if (keyEvent.getCode().equals(KeyCode.ENTER)) {
//                        String source = textField.getText();
//                        textField.clear();
//                        if (keyEvent.getCode().equals(KeyCode.ENTER)) {
//                            String dest = textField.getText();
//                            textField.setDisable(true);
//                            stage.hide();
//                            try {
//                                List<NodeData> nodeToPass = new ArrayList<>();
//                                nodeToPass = algos.shortestPath(Integer.parseInt(source), Integer.parseInt(dest));
//
//
//                            } catch (Exception exepction) {
//                                System.out.println("did not enter numbers");
//                            }
//                        }
//
//
//                    }
//
//                });
//
//
//            });
//
//
//        });

    }


    public static void main(String[] args, DirectedGraphAlgorithms graphAlgo) {
        algos = graphAlgo;
        launch(args);
    }
}
