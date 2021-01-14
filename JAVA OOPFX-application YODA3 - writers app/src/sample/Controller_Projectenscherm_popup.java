package sample;

import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

public class Controller_Projectenscherm_popup {
    @FXML
    public Button AanmaakButton;
    @FXML
    public Button AnnuleerButton;
    @FXML
    public TextField ProjectName;
    @FXML
    public Label LB_alert;

    @FXML
    public void HandleAnnuleerButton(MouseEvent event) {
        ProjectName.setText("");
        LB_alert.setVisible(false);
        Stage stage = (Stage) AnnuleerButton.getScene().getWindow();
        stage.close();
    }

    public void initialize() {
        LB_alert.setVisible(false);
        AanmaakButton.setOnAction(event -> {
            if (ProjectName.getText() != null && !ProjectName.getText().isEmpty()) {
                LB_alert.setVisible(false);
                Path Main_path = new File(Main.class.getProtectionDomain().getCodeSource().getLocation().getPath()).toPath();
                Path Parent = Main_path.getParent().getParent().getParent();
                String location = Parent+"\\Projecten\\"+ProjectName.getText();
                File directory = new File(location);
                if (!directory.exists()) {
                    directory.mkdir();
                    ProjectName.setText("");
                    Stage stage = (Stage) AanmaakButton.getScene().getWindow();
                    stage.hide();
                } else {
                    LB_alert.setVisible(true);
                    LB_alert.setText("Dit project bestaat al.");
                }
            }
            else {
                LB_alert.setVisible(true);
                LB_alert.setText("De projectnaam mag niet leeg zijn.");
            }
        });
        ProjectName.textProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue.matches(".*[/\\\\:*?\"<>|].*")) {
                ((StringProperty)observable).setValue(oldValue);
            }
        });
    }
}