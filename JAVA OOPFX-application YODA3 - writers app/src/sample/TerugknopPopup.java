package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.web.HTMLEditor;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
public class TerugknopPopup {
    public Button batton;


public void ja(ActionEvent event){
    try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("sample_Projectscherm.fxml"));
        Parent root = (Parent) loader.load();
        Controller_Projectscherm Controller_Projectscherm = loader.getController();
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setTitle("Projectscherm");
        stage.setResizable(false);
        stage.show();
        batton.getScene().getWindow().hide();
        ((Node)(event.getSource())).getScene().getWindow().hide();


    } catch (IOException e) {
        e.printStackTrace();
    }


}

public void getOptions(Button batton){
    this.batton = batton;

}
public void nee(ActionEvent event){

    ((Node)(event.getSource())).getScene().getWindow().hide();

}
}
