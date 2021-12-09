package GUI;

import algos.DirectedGraphAlgorithms;
import algos.GraphNode;
import algos.NodeLocation;
import api.DirectedWeightedGraph;
import api.NodeData;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
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
import java.util.*;

import static javafx.scene.paint.Color.GREEN;

public class GraphGUI extends Application {
    final int WIDTH = 1080;
    final int HEIGHT = 800;
    static DirectedGraphAlgorithms algos;
    HashMap<Integer, Button> nodeMap = new HashMap<>();
    Pane root;
    double radius = 10;
    DirectedWeightedGraph originalGraphCopy = algos.copy();

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Graph");
        root = new Pane();
        nodeMap.clear();

        FileChooser fileChooser = new FileChooser();
        FileChooser saveFileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("All Files", "*.*"));

        MenuButton file = new MenuButton("File");
        MenuItem load = new MenuItem("Load");
        MenuItem save = new MenuItem("Save");
        MenuItem exit = new MenuItem("Exit");

        MenuButton edit = new MenuButton("Edit");
        Menu nodes = new Menu("Nodes");
        Menu edges = new Menu("Edges");

        MenuItem addNode = new MenuItem("addNode");
        MenuItem removeNode = new MenuItem("removeNode");
        MenuItem connect = new MenuItem("connect");
        MenuItem removeEdge = new MenuItem("removeEdge");
        nodes.getItems().addAll(addNode, removeNode);
        edges.getItems().addAll(connect, removeEdge);
        edit.getItems().addAll(nodes, edges);


        MenuButton run = new MenuButton("RunAlgorithm");
        MenuItem isConnected = new MenuItem("isConnected");
        MenuItem shortestPathDist = new MenuItem("shortestPathDist");
        MenuItem shortestPath = new MenuItem("shortestPath");
        MenuItem center = new MenuItem("center");
        MenuItem tsp = new MenuItem("tsp");
        Button clean = new Button("clean");
        Button reset = new Button("reset");

        ToolBar toolBar = new ToolBar();
        toolBar.getItems().addAll(file, edit, run, clean, reset);
        run.getItems().addAll(isConnected, shortestPathDist, shortestPath, center, tsp);
        file.getItems().addAll(load, save, exit);

        Text text = new Text(10, 10, "click on each node to find more info");
        text.setFont(new Font(40));
        text.setFill(GREEN);
        text.setFont(Font.font("Helvetica", FontPosture.ITALIC, 18));
        text.setX(30);
        text.setY(60);
        text.setVisible(true);

        Group group = new Group(root, toolBar, text);

        AnimationTimer tm = new MyTimer(algos, root, radius, nodeMap, WIDTH, HEIGHT);
        tm.start();
        Scene scene = new Scene(group, WIDTH, HEIGHT);
        primaryStage.setScene(scene);
        primaryStage.show();



        cleanAction(clean);
        resetAction(primaryStage, reset);


        // File options:
        loadAction(primaryStage, fileChooser, load);
        saveAction(primaryStage, fileChooser, saveFileChooser, save);
        exit.setOnAction(e -> Platform.exit());


        // run Algorithms options:
        isConnected.setOnAction(actionEvent -> isConnectedPopWindow(algos.isConnected()));
        shortestPathAction(shortestPath);
        shortestPathDistAction(shortestPathDist);
        centerAction(center);
        tspAction(tsp);

        //edit graph options:
        addNodeAction(primaryStage, addNode);
        removeNodeAction(primaryStage, removeNode);
        connectAction(primaryStage, connect);
        removeEdgeAction(primaryStage, removeEdge);

    }

    private void connectAction(Stage primaryStage, MenuItem connect) {
        connect.setOnAction(actionEvent -> {
            Stage stage = new Stage();
            Label srcEdge = new Label("enter source in rage: 0 - " + (algos.getGraph().nodeSize() - 1));
            Label destEdge = new Label("enter dest in range: 0 - " + (algos.getGraph().nodeSize() - 1));
            Label weightEdge = new Label("enter weight: ");
            TextField textFieldSrc = new TextField();
            TextField textFieldDest = new TextField();
            TextField textFieldWeight = new TextField();
            Button connectButton = new Button("connectEdge");
            connectButton.setOnAction(actionEventRemove -> {
                if (isInt(textFieldSrc, textFieldDest) && isDouble(textFieldWeight.getText())) {
                    int src = Integer.parseInt(textFieldSrc.getText());
                    int dest = Integer.parseInt(textFieldDest.getText());
                    double weight = Double.parseDouble(textFieldWeight.getText());
                    if (src < 0 || src > algos.getGraph().nodeSize() || dest < 0 || dest > algos.getGraph().nodeSize()) {
                        stage.setAlwaysOnTop(false);
                        errorPopUp();
                    } else {
                        algos.getGraph().connect(src,dest,weight);
                        stage.setAlwaysOnTop(true);
                        updateGraph(primaryStage);
                    }
                }
            });
            VBox layout = new VBox(10);
            layout.setPadding(new Insets(20, 20, 20, 20));
            layout.getChildren().addAll(srcEdge, textFieldSrc, destEdge, textFieldDest,weightEdge,textFieldWeight,connectButton);
            Scene sc = new Scene(layout, 600, 300);
            stage.setScene(sc);
            stage.show();
        });
    }

    private void removeEdgeAction(Stage primaryStage, MenuItem removeEdge) {
        removeEdge.setOnAction(actionEvent -> {
            Stage stage = new Stage();
            Label srcEdge = new Label("enter source in rage: 0 - " + (algos.getGraph().nodeSize() - 1));
            Label destEdge = new Label("enter dest in range: 0 - " + (algos.getGraph().nodeSize() - 1));
            TextField textFieldSrc = new TextField();
            TextField textFieldDest = new TextField();
            Button removeButton = new Button("RemoveEdge");
            removeButton.setOnAction(actionEventRemove -> {
                if (isInt(textFieldSrc, textFieldDest)) {
                    int src = Integer.parseInt(textFieldSrc.getText());
                    int dest = Integer.parseInt(textFieldDest.getText());
                    if (src < 0 || src > algos.getGraph().nodeSize() || dest < 0 || dest > algos.getGraph().nodeSize()) {
                        stage.setAlwaysOnTop(false);
                        errorPopUp();
                    } else {
                        algos.getGraph().removeEdge(src, dest);
                        stage.setAlwaysOnTop(true);
                        updateGraph(primaryStage);
                    }
                }
            });
            VBox layout = new VBox(10);
            layout.setPadding(new Insets(20, 20, 20, 20));
            layout.getChildren().addAll(srcEdge, textFieldSrc, destEdge, textFieldDest, removeButton);
            Scene sc = new Scene(layout, 600, 300);
            stage.setScene(sc);
            stage.show();
        });
    }

    private void popWindow(Stage stage, Label label, TextField textFieldId, Button removeButton) {
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20, 20, 20, 20));
        layout.getChildren().addAll(label, textFieldId, removeButton);
        Scene sc = new Scene(layout, 600, 300);
        stage.setScene(sc);
        stage.show();
    }

    private void removeNodeAction(Stage primaryStage, MenuItem removeNode) {
        removeNode.setOnAction(actionEvent -> {
            Stage stage = new Stage();
            Label label = new Label("enter node ID: ");
            TextField textFieldId = new TextField();

            Button removeButton = new Button("Remove Node");
            removeButton.setOnAction(actionEventRemove -> {
                if (isInt(textFieldId)) {
                    int nodeId = Integer.parseInt(textFieldId.getText());
                    if (!nodeMap.containsKey(nodeId)) {
                        stage.setAlwaysOnTop(false);
                        removedNodeErrorMessage();
                    } else {
                        NodeData removedNode = algos.getGraph().removeNode(nodeId);
                        if (removedNode != null) {
                            stage.setAlwaysOnTop(true);
                            updateGraph(primaryStage);
                        }
                    }
                }
            });
            popWindow(stage, label, textFieldId, removeButton);
        });
    }

    private void removedNodeErrorMessage() {
        Stage stage = new Stage();
        Label label = new Label("Node Id does not exist");
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20, 20, 20, 20));
        layout.getChildren().addAll(label);

        Scene sc = new Scene(layout, 300, 300);
        stage.setScene(sc);
        stage.show();

    }

    private void centerAction(MenuItem center) {
        center.setOnAction(actionEvent -> {
            NodeData nodeCenter = algos.center();
            if (nodeCenter != null) {
                int nodeCenterNum = nodeCenter.getKey();
                Button button = nodeMap.get(nodeCenterNum);
                button.setStyle(" -fx-background-color: black;" +
                        "-fx-background-radius: 8em; " +
                        "-fx-min-width: 20px; " +
                        "-fx-min-height: 20px; " +
                        "-fx-max-width: 20px; " +
                        "-fx-max-height: 20px;"
                );
            }
            else {
                Stage stage = new Stage();
                Label label = new Label("the graph must be connected !");
                VBox layout = new VBox(10);
                layout.setPadding(new Insets(20, 20, 20, 20));
                layout.getChildren().addAll(label);

                Scene sc = new Scene(layout, 300, 300);
                stage.setScene(sc);
                stage.show();
            }
        });
    }

    private void shortestPathDistAction(MenuItem shortestPathDist) {
        shortestPathDist.setOnAction(actionEvent -> {
            Stage stage = new Stage();
            Label source = new Label("enter source between: 0 - " + (algos.getGraph().nodeSize() - 1));
            Label dest = new Label("enter dest between: 0 - " + (algos.getGraph().nodeSize() - 1));
            TextField sourceInput = new TextField();
            TextField destInput = new TextField();
            Button button = new Button("ENTER");
            button.setOnAction(e -> {
                shortestPathDist(algos.getGraph().nodeSize(), sourceInput, destInput);
            });
            VBox layout = new VBox(10);
            layout.setPadding(new Insets(20, 20, 20, 20));
            layout.getChildren().addAll(source, sourceInput, dest, destInput, button);
            Scene sc = new Scene(layout, 300, 300);
            stage.setScene(sc);
            stage.show();
        });
    }

    private void shortestPathAction(MenuItem shortestPath) {
        shortestPath.setOnAction(actionEvent -> {
            Stage stage = new Stage();
            Label source = new Label("enter source between: 0 - " + (algos.getGraph().nodeSize() - 1));
            Label dest = new Label("enter dest between: 0 - " + (algos.getGraph().nodeSize() - 1));
            TextField sourceInput = new TextField();
            TextField destInput = new TextField();
            Button button = new Button("ENTER");
            button.setOnAction(e -> {
                shortestPath(algos.getGraph().nodeSize(), sourceInput, destInput);
            });
            VBox layout = new VBox(10);
            layout.setPadding(new Insets(20, 20, 20, 20));
            layout.getChildren().addAll(source, sourceInput, dest, destInput, button);

            Scene sc = new Scene(layout, 300, 300);
            stage.setScene(sc);
            stage.show();
        });
    }

    private void addNodeAction(Stage primaryStage, MenuItem addNode) {
        addNode.setOnAction(actionEvent -> {
            Stage stage = new Stage();
            Point2D minPoint = findMinPoint();
            Point2D maxPoint = findMaxPoint();
            Label valueX = new Label("enter coordinate of X in range: " + minPoint.getX() + " - " + maxPoint.getX());
            Label valueY = new Label("enter coordinate of Y in range: " + minPoint.getY() + " - " + maxPoint.getY());
            Label id = new Label("enter node ID in range: 0 - " + algos.getGraph().nodeSize());
            TextField textFieldX = new TextField();
            TextField textFieldY = new TextField();
            TextField textFieldId = new TextField();

            Button button = new Button("ENTER");
            button.setOnAction(e -> {
                if (isDouble(textFieldX.getText(), textFieldY.getText()) && isInt(textFieldId)) {
                    double x = Double.parseDouble(textFieldX.getText());
                    double y = Double.parseDouble(textFieldY.getText());
                    int nodeId = Integer.parseInt(textFieldId.getText());
                    if (x > maxPoint.getX() || x < minPoint.getX() || y > maxPoint.getY() || y < minPoint.getY() || nodeId < 0 || nodeId > algos.getGraph().nodeSize()) {
                        stage.setAlwaysOnTop(false);
                        errorPopUp();
                    } else {
                        GraphNode newNode = new GraphNode(new NodeLocation(x, y, 0), nodeId);
                        algos.getGraph().addNode(newNode);
                        updateGraph(primaryStage);
                        stage.setAlwaysOnTop(true);

                    }
                }
            });
            VBox layout = new VBox(10);
            layout.setPadding(new Insets(20, 20, 20, 20));
            layout.getChildren().addAll(valueX, textFieldX, valueY, textFieldY, id, textFieldId, button);

            Scene sc = new Scene(layout, 600, 300);
            stage.setScene(sc);
            stage.show();
        });
    }

    private void updateGraph(Stage primaryStage) {
        primaryStage.close();
        try {
            start(primaryStage);
        } catch (Exception exep) {
            exep.printStackTrace();
        }
    }

    private void saveAction(Stage primaryStage, FileChooser fileChooser, FileChooser saveFileChooser, MenuItem save) {
        save.setOnAction(actionEvent -> {
            File saveFile = saveFileChooser.showSaveDialog(primaryStage);
            fileChooser.setInitialDirectory(saveFile.getParentFile());
            String path = saveFile.getAbsolutePath();
            savePopWindow(algos.save(path));
        });
    }


    private void resetAction(Stage primaryStage, Button reset) {
        reset.setOnAction(actionEvent -> {
            algos.init(originalGraphCopy);
            updateGraph(primaryStage);
        });
    }

    private void cleanAction(Button clean) {
        clean.setOnAction(actionEvent -> {
            for (int i = 0; i < nodeMap.size(); i++) {
                Button currButton = nodeMap.get(i);
                if (currButton != null) {
                    currButton.setStyle(
                            "-fx-background-radius: 8em; " +
                                    "-fx-min-width: 20px; " +
                                    "-fx-min-height: 20px; " +
                                    "-fx-max-width: 20px; " +
                                    "-fx-max-height: 20px;"
                    );
                }
            }
        });
    }

    private void loadAction(Stage primaryStage, FileChooser fileChooser, MenuItem load) {
        load.setOnAction(actionEvent -> {
            File chosenFile = fileChooser.showOpenDialog(primaryStage);
            if (chosenFile != null) {
                String path = chosenFile.getAbsolutePath();
                algos.load(path);
                updateGraph(primaryStage);

            }
        });
    }

    private void tspAction(MenuItem tsp) {
        tsp.setOnAction(actionEvent -> {
            Stage stage = new Stage();
            List<NodeData> addedNodes = new LinkedList<>();
            Label label = new Label("enter node number between: 0 - " + (algos.getGraph().nodeSize() - 1));
            TextField userInput = new TextField();
            Button button = new Button("Add");
            Button runAlgo = new Button("runAlgo");
            Label labelAdded = new Label();
            StringBuilder added = new StringBuilder();
            added.append("added Nodes: ");
            button.setOnAction(e -> {
                if (isInt(userInput)) {
                    int node = Integer.parseInt(userInput.getText());
                    added.append(userInput.getText());
                    added.append(" ,");
                    addedNodes.add(algos.getGraph().getNode(node));
                    userInput.clear();
                }
                labelAdded.setText(added.toString());
            });

            runAlgo.setOnAction(e -> {
                List<NodeData> result = algos.tsp(addedNodes);
                paintTrail(result);
            });
            VBox layout = new VBox(10);
            layout.setPadding(new Insets(20, 20, 20, 20));
            layout.getChildren().addAll(label, userInput, button, runAlgo, labelAdded);

            Scene sc = new Scene(layout, 300, 300);
            stage.setScene(sc);
            stage.show();
        });
    }


    private Point2D findMinPoint() {
        double minX = Integer.MAX_VALUE;
        double minY = Integer.MAX_VALUE;
        Iterator<NodeData> nodeIter = algos.getGraph().nodeIter();
        while (nodeIter.hasNext()) {
            NodeData currNode = nodeIter.next();
            minX = Math.min(currNode.getLocation().x(), minX);
            minY = Math.min(currNode.getLocation().y(), minY);
        }
        return new Point2D(minX, minY);
    }

    private Point2D findMaxPoint() {
        double maxX = Integer.MIN_VALUE;
        double maxY = Integer.MIN_VALUE;
        Iterator<NodeData> nodeIter = algos.getGraph().nodeIter();
        while (nodeIter.hasNext()) {
            NodeData currNode = nodeIter.next();
            maxX = Math.max(currNode.getLocation().x(), maxX);
            maxY = Math.max(currNode.getLocation().y(), maxY);
        }
        return new Point2D(maxX, maxY);
    }

    private void errorPopUp() {
        Stage stage = new Stage();
        Label label = new Label("Error - please enter numbers in range");

        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20, 20, 20, 20));
        layout.getChildren().addAll(label);

        Scene sc = new Scene(layout, 300, 300);
        stage.setScene(sc);
        stage.show();
    }

    private boolean isDouble(String valueX, String valueY) {
        try {
            Double.parseDouble(valueX);
            Double.parseDouble(valueY);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    private boolean isDouble(String num) {
        try {
            Double.parseDouble(num);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }


    private void paintTrail(List<NodeData> result) {
        Stage popWindow = new Stage();
        Label label;
        if (result == null || result.size() == 0) {
            label = new Label("There is no path going through all nodes");
        } else {
            for (int i = 0; i < result.size(); i++) {
                int nodeKey = result.get(i).getKey();
                Button currNode = nodeMap.get(nodeKey);
                currNode.setStyle(" -fx-background-color: black;" +
                        "-fx-background-radius: 8em; " +
                        "-fx-min-width: 20px; " +
                        "-fx-min-height: 20px; " +
                        "-fx-max-width: 20px; " +
                        "-fx-max-height: 20px;");
            }
            StringBuilder prev = new StringBuilder();
            prev.append("Fastest tsp path: ");
            for (int i = 0; i < result.size() - 1; i++) {
                prev.append(result.get(i).getKey()).append("-> ");
            }
            prev.append(result.get(result.size() - 1).getKey());
            label = new Label(prev.toString());
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

    private void shortestPath(int maxNum, TextField sourceInput, TextField destInput) {
        Stage popWindow = new Stage();
        Label label = new Label();
        if (isInt(sourceInput, destInput)) {
            int src = Integer.parseInt(sourceInput.getText());
            int dest = Integer.parseInt(destInput.getText());
            List<NodeData> trail = algos.shortestPath(src, dest);
            if (trail == null) {
                label.setText("please enter 2 different numbers in range");
            } else {
                sourceInput.clear();
                destInput.clear();
                for (int i = 0; i < trail.size(); i++) {
                    int nodeKey = trail.get(i).getKey();
                    Button currNode = nodeMap.get(nodeKey);
                    currNode.setStyle(" -fx-background-color: black;" +
                            "-fx-background-radius: 8em; " +
                            "-fx-min-width: 20px; " +
                            "-fx-min-height: 20px; " +
                            "-fx-max-width: 20px; " +
                            "-fx-max-height: 20px;");
                }
                StringBuilder prev = new StringBuilder();
                prev.append("Fastest path: ");
                for (int i = 0; i < trail.size() - 1; i++) {
                    prev.append(trail.get(i).getKey()).append("-> ");
                }
                prev.append(trail.get(trail.size() - 1).getKey());
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

    public void shortestPathDist(int maxNum, TextField sourceInput, TextField destInput) {
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
                label = new Label("shortest distance: " + algos.shortestPathDist(src, dest));
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

    private Boolean isInt(TextField sourceInput, TextField destInput) {
        try {
            Integer.parseInt(sourceInput.getText());
            Integer.parseInt(destInput.getText());
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private Boolean isInt(TextField userInput) {
        try {
            Integer.parseInt(userInput.getText());
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
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
