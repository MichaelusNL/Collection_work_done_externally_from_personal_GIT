package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Arrays;

public class Controller_Entryscherm_popup_terug {

    private Scene ProjectScene;
    private FXMLLoader EntryOpslagPageLoader;

    public void setProjectScene(Scene scene) {
        ProjectScene = scene;
    }

    public void setEntryLoader(FXMLLoader loader) { EntryOpslagPageLoader = loader;}

    public void openProjectScene(ActionEvent actionEvent) {
        Controller_Entryscherm entryController = EntryOpslagPageLoader.getController();
//        System.out.println(Arrays.toString(entryController.getClass().getSuperclass().getMethods()));
        Stage primaryStage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        primaryStage.hide();
    }

    public void verder_werken(ActionEvent event){
        ((Node)(event.getSource())).getScene().getWindow().hide();
    }
}
