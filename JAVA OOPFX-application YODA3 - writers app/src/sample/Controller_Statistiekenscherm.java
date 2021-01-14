package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller_Statistiekenscherm implements Initializable{
    public TableView<Statistieken> tableview;
    public TableColumn<Statistieken,String> statistiek;
    public TableColumn<Statistieken,Float> waarde;

    private Scene ProjectScene;

    public void setProjectScene(Scene scene) {
        ProjectScene = scene;
    }

    public void openProjectScene(ActionEvent actionEvent) {
        Stage primaryStage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        primaryStage.setTitle("Project scherm");
        primaryStage.setScene(ProjectScene);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources){
        statistiek.setCellValueFactory(new PropertyValueFactory<>("statistiek"));
        waarde.setCellValueFactory(new PropertyValueFactory<>("waarde"));
        tableview.setItems(observableList);
    }

    ObservableList<Statistieken> observableList = FXCollections.observableArrayList(
            new Statistieken("Aantal woorden", "327"),
            new Statistieken("Grootste file size", "Hodor.txt"),
            new Statistieken("Hoogste aantal woorden", "140")
    );

}
