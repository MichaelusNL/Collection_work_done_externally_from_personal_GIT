package sample;

//import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;

public class Controller_Entryscherm_popup_opslag {
    @FXML
    public TextField titel;
    @FXML
    public Label LB_alert;
    @FXML
    public Label LB_naam;
    @FXML
    public Button BT_opslaan;

    private Controller_Projectscherm Projectscene;
    private Controller_Entryscherm_popup_opslag Opslagscene;

    public String entry;

    public void setProjectController(Controller_Projectscherm control){
        Projectscene = control;
    }

    public void initialize() {
        LB_alert.setVisible(false);
        BT_opslaan.setOnAction(event -> {
            if (titel.getText() != null && !titel.getText().isEmpty()) {
                LB_alert.setVisible(false);
                Path Main_path = new File(Main.class.getProtectionDomain().getCodeSource().getLocation().getPath()).toPath();
                Path Parent = Main_path.getParent().getParent().getParent();
                String file = Parent.toString()+"\\Projecten\\"+LB_naam.getText()+"\\"+titel.getText()+".txt";
//                System.out.println(file);
                if(entry == null ){
                    entry = "";
                }
//                System.out.println(entry);
                try {
                    file_aanmaken(file, entry);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                ((Node)(event.getSource())).getScene().getWindow().hide();
            }
            else {
                LB_alert.setVisible(true);
                LB_alert.setText("Het tekstvak mag niet leeg zijn.");
            }
        });

        titel.textProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue.matches(".*[/\\\\:*?\"<>|].*")) {
                ((StringProperty)observable).setValue(oldValue);
            }
        });}

    public void verder_werken(ActionEvent event){
        LB_alert.setVisible(false);
        ((Node)(event.getSource())).getScene().getWindow().hide();
    }

/*    public void opslaan(ActionEvent event) throws IOException{
        LB_alert.setVisible(false);
        if(titel.getCharacters().length() <1){
            LB_alert.setVisible(true);
            LB_alert.setText("Het tekstvak mag niet leeg zijn.");
        }
        else{
            String file = titel.getCharacters().toString()+".txt";
            if(entry == null ){
                entry = "";
            }
            file_aanmaken(file, entry);
            ((Node)(event.getSource())).getScene().getWindow().hide();
        }
    }
    */

    public void file_aanmaken(String file, String entry)throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(file));
        writer.write(entry);
        writer.close();
    }

    public void getOptions(String entry){
        this.entry = entry;
    }


}
