package sample;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Controller_Welkomscherm {

    private Scene ProjectenScene;
    private Scene OptieScene;

    public void setProjectenScene(Scene scene) {
        ProjectenScene = scene;
    }

    public void setOptieScene(Scene scene) {
        OptieScene = scene;
    }

    public void openProjectenScene(ActionEvent actionEvent) {
        Stage primaryStage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
//        System.out.println(primaryStage.toString());
        primaryStage.setTitle("Projecten scherm");
        primaryStage.setScene(ProjectenScene);
    }

    public void openOptieScene(ActionEvent actionEvent) {
        Stage primaryStage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
//        System.out.println(primaryStage.toString());
        primaryStage.setTitle("Optie scherm");
        primaryStage.setScene(OptieScene);
    }

    public void exitProgram() {
        System.exit(0);
    }

    public void initialize() {}

}

