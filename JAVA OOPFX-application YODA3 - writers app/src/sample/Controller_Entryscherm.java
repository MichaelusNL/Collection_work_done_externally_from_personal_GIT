package sample;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.web.HTMLEditor;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

public class Controller_Entryscherm {
    @FXML
    private HTMLEditor editor;
    @FXML
    private Button BT_terug;
    @FXML
    private TableView<Main.Project_inhoud> TV_Entry_Data;

//    private Scene ProjectScene;
    private Scene EntryOpslagScene;
    private Scene EntryTerugScene;
    private FXMLLoader EntryOpslagPageLoader;
    public FXMLLoader ProjectPageLoader;
    public Scene ProjectScene;

/*    public void setProjectScene(Scene scene) {
        ProjectScene = scene;
    } */

    public void setOpslagScene (Scene scene) { EntryOpslagScene = scene; }

    public void setTerugScene (Scene scene) { EntryTerugScene = scene; }

    public void setEntryOpslagScene(Scene scene) {EntryOpslagScene = scene; }

    public void setOpslagControl(FXMLLoader loader) { EntryOpslagPageLoader = loader;}

    public void setProjectControl(FXMLLoader loader) { ProjectPageLoader = loader;}

    public void setProjectScene(Scene scene) { ProjectScene = scene;}

/*    public void openProjectScene(ActionEvent actionEvent) {
        Stage primaryStage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        primaryStage.setTitle("Project scherm");
        primaryStage.setScene(ProjectScene);
    } */

    public void initialize() {}

    public void opslaan(ActionEvent event){
        Controller_Entryscherm_popup_opslag control = EntryOpslagPageLoader.getController();
        Stage stage = new Stage();
        stage.setScene(EntryOpslagScene);
        stage.setTitle("Opslag pop-up");
        stage.show();
        control.getOptions(editor.getHtmlText());
        Controller_Entryscherm_popup_opslag thecontroller = EntryOpslagPageLoader.getController();
        thecontroller.getOptions(editor.getHtmlText());
        stage.setOnHiding(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                Path Main_path = new File(Main.class.getProtectionDomain().getCodeSource().getLocation().getPath()).toPath();
                Path Parent = Main_path.getParent().getParent().getParent();
                Label text = (Label) EntryOpslagScene.lookup("#LB_naam");
                File Directory = new File(Parent.toString()+"/Projecten/"+text.getText());
                Controller_Projectscherm scherm = ProjectPageLoader.getController();
                try {
                    scherm.TV_Entry_Data.setItems(scherm.getdata(Directory));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void openTerug(ActionEvent event){
        Stage Primarystage = (Stage)BT_terug.getScene().getWindow();
        Stage stage = new Stage();
        stage.setScene(EntryTerugScene);
        stage.setTitle("Terug controle pop-up");
        stage.show();
        stage.setOnHiding(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                Primarystage.setScene(ProjectScene);
            }
        });
    }

}
