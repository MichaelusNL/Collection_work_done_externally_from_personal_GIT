package sample;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.text.SimpleDateFormat;

public class Controller_Optiescherm {
    @FXML
    private ChoiceBox CB_thema;
    @FXML
    private ChoiceBox CB_tijd;
    @FXML
    private AnchorPane AP_Main;
    @FXML
    private Label LB_thema;
    @FXML
    private Label LB_tijd;
    @FXML
    private Label LB_titel;

    private Scene WelkomScene;
    private Scene ProjectenScene;
    private Scene ProjectenPopupScene;
    private Scene ProjectScene;
    private Scene StatistiekenScene;
    private Scene EntryScene;
    private Scene EntryOpslagScene;
    private Controller_Projectenscherm ProjectenPaneController;

    public void setWelkomScene(Scene scene) {
        WelkomScene = scene;
    }

    public void setProjectenScene(Scene scene) {
        ProjectenScene = scene;
    }

    public void setProjectenPopupScene(Scene scene) { ProjectenPopupScene = scene; }

    public void setProjectScene(Scene scene) {
        ProjectScene = scene;
    }

    public void setStatistiekenScene(Scene scene) {
        StatistiekenScene = scene;
    }

    public void setEntryScene(Scene scene) {
        EntryScene = scene;
    }

    public void setEntryOpslagScene(Scene scene) { EntryOpslagScene = scene; }

    public void setProjectenController(Controller_Projectenscherm controller) {ProjectenPaneController = controller; }

    public void openWelkomScene(ActionEvent actionEvent) {
        Stage primaryStage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        primaryStage.setTitle("Welkom scherm");
        primaryStage.setScene(WelkomScene);
    }

    public void initialize() {
        CB_thema.setItems(FXCollections.observableArrayList("Nacht modus", "Dag modus", "Kleur modus"));
        CB_thema.setValue("Nacht modus");
        CB_thema.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) -> {
            this.changeWelkomScherm(newValue);
            this.changeOptieScherm(newValue);
            this.changeProjectenScherm(newValue);
            this.changeProjectScherm(newValue);
            this.changeStatistiekenScherm(newValue);
            this.changeEntryScherm(newValue);
            this.changeProjectenPopupScherm(newValue);
            this.changeEntryOpslagScherm(newValue);
        });
        CB_tijd.setItems(FXCollections.observableArrayList("dd/mm/yyyy", "mm/dd/yyyy", "yyyy/mm/dd"));
        CB_tijd.setValue("dd/mm/yyyy");
        CB_tijd.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) -> {
            this.ProjectenSchermTimeSwitch(newValue);
        });
    }

    @FXML
    private void changeWelkomScherm(Number newValue) {
        if (newValue.equals(0)) {
            WelkomScene.getRoot().setStyle("-fx-background-color: #000000;");
            WelkomScene.lookup("#BT_Projecten").setStyle("-fx-background-color: #05acd8");
            WelkomScene.lookup("#BT_Sluiten").setStyle("-fx-background-color: #05acd8");
            WelkomScene.lookup("#BT_Opties").setStyle("-fx-background-color: #05acd8");
            Label welkom = (Label) WelkomScene.lookup("#LB_welkom");
            welkom.setTextFill(Color.web("#8fb56e"));
            Label textfield = (Label)WelkomScene.lookup("#textfield");
            textfield.setTextFill(Color.web("#8fb56e"));
        }
        else if (newValue.equals(1)) {
            WelkomScene.getRoot().setStyle("-fx-background-color: #ffffff;");
            WelkomScene.lookup("#BT_Projecten").setStyle("-fx-background-color: #05acd8");
            WelkomScene.lookup("#BT_Sluiten").setStyle("-fx-background-color: #05acd8");
            WelkomScene.lookup("#BT_Opties").setStyle("-fx-background-color: #05acd8");
            Label welkom = (Label) WelkomScene.lookup("#LB_welkom");
            welkom.setTextFill(Color.web("#8fb56e"));
            Label textfield = (Label)WelkomScene.lookup("#textfield");
            textfield.setTextFill(Color.web("#8fb56e"));
        }
        else {
            WelkomScene.getRoot().setStyle("-fx-background-color: #ffff00;");
            WelkomScene.lookup("#BT_Projecten").setStyle("-fx-background-color: #0000ff");
            WelkomScene.lookup("#BT_Sluiten").setStyle("-fx-background-color: #0000ff");
            WelkomScene.lookup("#BT_Opties").setStyle("-fx-background-color: #0000ff");
            Label welkom = (Label) WelkomScene.lookup("#LB_welkom");
            welkom.setTextFill(Color.web("#00ff00"));
            Label textfield = (Label)WelkomScene.lookup("#textfield");
            textfield.setTextFill(Color.web("#00ff00"));
        }
    }
    @FXML
    private void changeOptieScherm(Number newValue) {
        if (newValue.equals(0)) {
            AP_Main.getScene().getRoot().setStyle("-fx-background-color: #000000");
            AP_Main.getScene().lookup("#BT_terug").setStyle("-fx-background-color: #05acd8");
            LB_thema.setTextFill(Color.web("#8fb56e"));
            LB_tijd.setTextFill(Color.web("#8fb56e"));
            LB_titel.setTextFill(Color.web("#8fb56e"));
        }
        else if (newValue.equals(1)) {
            AP_Main.getScene().getRoot().setStyle("-fx-background-color: #ffffff");
            AP_Main.getScene().lookup("#BT_terug").setStyle("-fx-background-color: #05acd8");
            LB_thema.setTextFill(Color.web("#8fb56e"));
            LB_tijd.setTextFill(Color.web("#8fb56e"));
            LB_titel.setTextFill(Color.web("#8fb56e"));
        }
        else {
            AP_Main.getScene().getRoot().setStyle("-fx-background-color: #ffff00");
            AP_Main.getScene().lookup("#BT_terug").setStyle("-fx-background-color: #0000ff");
            LB_thema.setTextFill(Color.web("#00ff00"));
            LB_tijd.setTextFill(Color.web("#00ff00"));
            LB_titel.setTextFill(Color.web("#00ff00"));
        }
    }
    @FXML
    private void changeProjectenScherm(Number newValue) {
        if (newValue.equals(0)) {
            ProjectenScene.getRoot().setStyle("-fx-background-color: #000000;");
            ProjectenScene.lookup("#BackButton").setStyle("-fx-background-color: #05acd8");
            ProjectenScene.lookup("#NewProjectButton").setStyle("-fx-background-color: #05acd8");
            Label project = (Label) ProjectenScene.lookup("#label");
            project.setTextFill(Color.web("#8fb56e"));
        } else if (newValue.equals(1)) {
            ProjectenScene.getRoot().setStyle("-fx-background-color: #ffffff;");
            ProjectenScene.lookup("#BackButton").setStyle("-fx-background-color: #05acd8");
            ProjectenScene.lookup("#NewProjectButton").setStyle("-fx-background-color: #05acd8");
            Label project = (Label) ProjectenScene.lookup("#label");
            project.setTextFill(Color.web("#8fb56e"));
        } else {
            ProjectenScene.getRoot().setStyle("-fx-background-color: #ffff00;");
            ProjectenScene.lookup("#BackButton").setStyle("-fx-background-color: #0000ff");
            ProjectenScene.lookup("#NewProjectButton").setStyle("-fx-background-color: #0000ff");
            Label project = (Label) ProjectenScene.lookup("#label");
            project.setTextFill(Color.web("#00ff00"));
        }
    }
    @FXML
    private void changeProjectenPopupScherm(Number newValue) {
        if (newValue.equals(0)) {
            ProjectenPopupScene.getRoot().setStyle("-fx-background-color: #000000");
            ProjectenPopupScene.lookup("#AanmaakButton").setStyle("-fx-background-color: #05acd8");
            ProjectenPopupScene.lookup("#AnnuleerButton").setStyle("-fx-background-color: #05acd8");
            Label project = (Label) ProjectenPopupScene.lookup("#LB_naam");
            project.setTextFill(Color.web("#8fb56e"));

        } else if (newValue.equals(1)) {
            ProjectenPopupScene.getRoot().setStyle("-fx-background-color: #ffffff");
            ProjectenPopupScene.lookup("#AanmaakButton").setStyle("-fx-background-color: #05acd8");
            ProjectenPopupScene.lookup("#AnnuleerButton").setStyle("-fx-background-color: #05acd8");
            Label project = (Label) ProjectenPopupScene.lookup("#LB_naam");
            project.setTextFill(Color.web("#8fb56e"));

        } else {
            ProjectenPopupScene.getRoot().setStyle("-fx-background-color: #ffff00");
            ProjectenPopupScene.lookup("#AanmaakButton").setStyle("-fx-background-color: #0000ff");
            ProjectenPopupScene.lookup("#AnnuleerButton").setStyle("-fx-background-color: #0000ff");
            Label project = (Label) ProjectenPopupScene.lookup("#LB_naam");
            project.setTextFill(Color.web("#00ff00"));
        }
    }
    @FXML
    private void changeProjectScherm(Number newValue) {
        if (newValue.equals(0)) {
            ProjectScene.getRoot().setStyle("-fx-background-color: #000000;");
            ProjectScene.lookup("#BT_terug").setStyle("-fx-background-color: #05acd8");
            ProjectScene.lookup("#BT_Statistieken").setStyle("-fx-background-color: #05acd8");
            ProjectScene.lookup("#BT_NieuweEntry").setStyle("-fx-background-color: #05acd8");
            Label project = (Label) ProjectScene.lookup("#LB_project");
            project.setTextFill(Color.web("#8fb56e"));
        } else if (newValue.equals(1)) {
            ProjectScene.getRoot().setStyle("-fx-background-color: #ffffff;");
            ProjectScene.lookup("#BT_terug").setStyle("-fx-background-color: #05acd8");
            ProjectScene.lookup("#BT_Statistieken").setStyle("-fx-background-color: #05acd8");
            ProjectScene.lookup("#BT_NieuweEntry").setStyle("-fx-background-color: #05acd8");
            Label project = (Label) ProjectScene.lookup("#LB_project");
            project.setTextFill(Color.web("#8fb56e"));
        } else {
            ProjectScene.getRoot().setStyle("-fx-background-color: #ffff00;");
            ProjectScene.lookup("#BT_terug").setStyle("-fx-background-color: #0000ff");
            ProjectScene.lookup("#BT_Statistieken").setStyle("-fx-background-color: #0000ff");
            ProjectScene.lookup("#BT_NieuweEntry").setStyle("-fx-background-color: #0000ff");
            Label project = (Label) ProjectScene.lookup("#LB_project");
            project.setTextFill(Color.web("#00ff00"));
        }
    }
    @FXML
    private void changeStatistiekenScherm(Number newValue) {
        if (newValue.equals(0)) {
            StatistiekenScene.getRoot().setStyle("-fx-background-color: #000000;");
            StatistiekenScene.lookup("#BT_terug").setStyle("-fx-background-color: #05acd8");
            Label statistieken = (Label) StatistiekenScene.lookup("#LB_statistieken");
            statistieken.setTextFill(Color.web("8fb56e"));
        }
        else if (newValue.equals(1)) {
            StatistiekenScene.getRoot().setStyle("-fx-background-color: #ffffff;");
            StatistiekenScene.lookup("#BT_terug").setStyle("-fx-background-color: #05acd8");
            Label statistieken = (Label) StatistiekenScene.lookup("#LB_statistieken");
            statistieken.setTextFill(Color.web("8fb56e"));
        }
        else {
            StatistiekenScene.getRoot().setStyle("-fx-background-color: #ffff00;");
            StatistiekenScene.lookup("#BT_terug").setStyle("-fx-background-color: #0000ff");
            Label statistieken = (Label) StatistiekenScene.lookup("#LB_statistieken");
            statistieken.setTextFill(Color.web("00ff00"));
        }
    }
    @FXML
    private void changeEntryScherm(Number newValue) {
        if (newValue.equals(0)) {
            EntryScene.getRoot().setStyle("-fx-background-color: #000000");
            EntryScene.lookup("#BT_terug").setStyle("-fx-background-color: #05acd8");
            EntryScene.lookup("#BT_opslag").setStyle("-fx-background-color: #05acd8");
        }
        else if (newValue.equals(1)) {
            EntryScene.getRoot().setStyle("-fx-background-color: #ffffff");
            EntryScene.lookup("#BT_terug").setStyle("-fx-background-color: #05acd8");
            EntryScene.lookup("#BT_opslag").setStyle("-fx-background-color: #05acd8");
        }
        else {
            EntryScene.getRoot().setStyle("-fx-background-color: #ffff00");
            EntryScene.lookup("#BT_terug").setStyle("-fx-background-color: #0000ff");
            EntryScene.lookup("#BT_opslag").setStyle("-fx-background-color: #0000ff");
        }
    }
    @FXML
    private void changeEntryOpslagScherm(Number newValue) {
        if (newValue.equals(0)) {
            EntryOpslagScene.getRoot().setStyle("-fx-background-color: #000000");
            EntryOpslagScene.lookup("#BT_verder").setStyle("-fx-background-color: #05acd8");
            EntryOpslagScene.lookup("#BT_opslaan").setStyle("-fx-background-color: #05acd8");
            Label titelinvul = (Label) EntryOpslagScene.lookup("#labelt");
            titelinvul.setTextFill(Color.web("#8fb56e"));
        }
        else if (newValue.equals(1)) {
            EntryOpslagScene.getRoot().setStyle("-fx-background-color: #ffffff");
            EntryOpslagScene.lookup("#BT_verder").setStyle("-fx-background-color: #05acd8");
            EntryOpslagScene.lookup("#BT_opslaan").setStyle("-fx-background-color: #05acd8");
            Label titelinvul = (Label) EntryOpslagScene.lookup("#labelt");
            titelinvul.setTextFill(Color.web("#8fb56e"));
        }
        else {
            EntryOpslagScene.getRoot().setStyle("-fx-background-color: #ffff00");
            EntryOpslagScene.lookup("#BT_verder").setStyle("-fx-background-color: #0000ff");
            EntryOpslagScene.lookup("#BT_opslaan").setStyle("-fx-background-color: #0000ff");
            Label titelinvul = (Label) EntryOpslagScene.lookup("#labelt");
            titelinvul.setTextFill(Color.web("#00ff00"));
        }
    }

    private void ProjectenSchermTimeSwitch(Number newValue) {
        if (newValue.equals(0)) {
            ProjectenPaneController.setSdf("dd/MM/YYYY");

        } else if (newValue.equals(1)) {
            ProjectenPaneController.setSdf("MM/dd/YYYY");

        } else {
            ProjectenPaneController.setSdf("YYYY/MM/dd");

        }
    }
    @FXML
    private void ProjectSchermTimeSwitch(Number newValue) {
        if (newValue.equals(0)) {
            ProjectenPopupScene.getRoot().setStyle("-fx-background-color: #000000");
            ProjectenPopupScene.lookup("#AanmaakButton").setStyle("-fx-background-color: #05acd8");
            ProjectenPopupScene.lookup("#AnnuleerButton").setStyle("-fx-background-color: #05acd8");
            ProjectenPopupScene.lookup("#LB_naam").setStyle("-fx-background-color: #05acd8");
            ProjectenPopupScene.lookup("#LB_opslag").setStyle("-fx-background-color: #05acd8");

        } else if (newValue.equals(1)) {
            ProjectenPopupScene.getRoot().setStyle("-fx-background-color: #ffffff");
            ProjectenPopupScene.lookup("#AanmaakButton").setStyle("-fx-background-color: #05acd8");
            ProjectenPopupScene.lookup("#AnnuleerButton").setStyle("-fx-background-color: #05acd8");
            ProjectenPopupScene.lookup("#LB_naam").setStyle("-fx-background-color: #05acd8");
            ProjectenPopupScene.lookup("#LB_opslag").setStyle("-fx-background-color: #05acd8");

        } else {
            ProjectenPopupScene.getRoot().setStyle("-fx-background-color: #ffff00");
            ProjectenPopupScene.lookup("#AanmaakButton").setStyle("-fx-background-color: #0000ff");
            ProjectenPopupScene.lookup("#AnnuleerButton").setStyle("-fx-background-color: #0000ff");
            ProjectenPopupScene.lookup("#LB_naam").setStyle("-fx-background-color: #0000ff");
            ProjectenPopupScene.lookup("#LB_opslag").setStyle("-fx-background-color: #0000ff");
        }
    }
}

