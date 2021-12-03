package GUI;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class LoadGraphGUI extends Application {

    final int WIDTH = 800;
    final int HEIGHT = 600;

    @Override
    public void start(Stage stage) {
        stage.setTitle("Graph Algo Simulator");

        Text scenetitle = new Text("Choose a Graph File");
        scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 17));

        final FileChooser fileChooser = new FileChooser();

        final Button openButton = new Button("Choose a Graph File");

        openButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(final ActionEvent e) {
                File file = fileChooser.showOpenDialog(stage);
                if (file != null) {
                    System.out.println(file);
                }
            }
        });


        final GridPane inputGridPane = new GridPane();

        inputGridPane.add(scenetitle, 3, 0, 2, 1);
        GridPane.setConstraints(openButton, 4, 5);
        inputGridPane.setHgap(10);
        inputGridPane.setVgap(10);
        inputGridPane.getChildren().addAll(openButton);
        final Pane rootGroup = new VBox(12);
        inputGridPane.setAlignment(Pos.CENTER);

        rootGroup.getChildren().addAll(inputGridPane);
        rootGroup.setPadding(new Insets(30, 30, 30, 30));

        stage.setScene(new Scene(rootGroup));
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

}
