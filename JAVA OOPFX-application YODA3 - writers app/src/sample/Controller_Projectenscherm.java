package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.scene.input.MouseButton;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.WindowEvent;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Controller_Projectenscherm implements Initializable {
    @FXML
    private TableView<Main.Projecten> tbData;
    @FXML
    public TableColumn<Main.Projecten, String> Project_naam;
    @FXML
    public TableColumn<Main.Projecten, String> Project_aanmaak;
    @FXML
    public TableColumn<Main.Projecten, String> Project_bewerkt;
    @FXML
    public TableColumn<Main.Projecten, Integer> Project_entries;
    @FXML
    public Button BackButton;
    @FXML
    public Button NewProjectButton;

    private Scene WelkomScene;
    private Scene ProjectScene;
    private Scene ProjectenPopupScene;
    private Scene OptieScene;
    private Scene OpslagScene;
    private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/YYYY");

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Project_naam.setCellValueFactory(new PropertyValueFactory<>("Project_naam"));
        Project_aanmaak.setCellValueFactory(new PropertyValueFactory<>("Project_aanmaak"));
        Project_bewerkt.setCellValueFactory(new PropertyValueFactory<>("Project_bewerkt"));
        Project_entries.setCellValueFactory(new PropertyValueFactory<>("Project_entries"));
        tbData.setItems(Projecten_data);
        tbData.setRowFactory(tv -> {
            TableRow<Main.Projecten> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty() && event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
                    Main.Projecten clickedrow = row.getItem();
                    try {
                        this.openProjectScene(clickedrow);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            });
            return row;
        });
    }
    Main x = new Main();
    ObservableList<Main.Projecten> Projecten_data = getdata();
    public ObservableList<Main.Projecten> getdata(){
        Path Main_path = new File(Main.class.getProtectionDomain().getCodeSource().getLocation().getPath()).toPath();
        Path Parent = Main_path.getParent().getParent().getParent();
        File Directory = new File(Parent.toString()+"/Projecten");
        File[] directories = new File(Directory.getPath()).listFiles(File::isDirectory);
        //SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/YYYY");
        ObservableList<Main.Projecten> Projecten_data = FXCollections.observableArrayList();
        for(File f : directories) {
            String lasttime = sdf.format(f.lastModified());
            BasicFileAttributes a = null;
            try {
                a = Files.readAttributes(Paths.get(f.toURI()), BasicFileAttributes.class);
            } catch (IOException e) {
                e.printStackTrace();
            }
            String creationTime = sdf.format(a.creationTime().toMillis());
            int entryAmount = new File(f.getPath()).list().length;
            Projecten_data.add(x.new Projecten(f.getName(),creationTime,lasttime,entryAmount));
        }
        return Projecten_data;
    }

    public void setWelkomScene(Scene scene) {
        WelkomScene = scene;
    }

    public void setProjectScene(Scene scene) {
        ProjectScene = scene;
    }

    public void setPopupScene(Scene scene) { ProjectenPopupScene  = scene; }

    public void setOptieScene(Scene scene) {OptieScene = scene; }

    public void setOpslagScene(Scene scene) {OpslagScene = scene;}

    public void openWelkomScene(ActionEvent actionEvent) {
        Stage primaryStage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        primaryStage.setTitle("Welkom scherm");
        primaryStage.setScene(WelkomScene);
    }
    public void openPopupScene(ActionEvent actionEvent) {
        Stage primaryStage = new Stage();
        primaryStage.setTitle("Nieuw Project");
        primaryStage.setScene(ProjectenPopupScene);
        primaryStage.show();
        primaryStage.setOnHiding(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                tbData.setItems(getdata());
            }
        });
    }
    public void openProjectScene(Main.Projecten clickedrow) throws IOException {
        Path Main_path = new File(Main.class.getProtectionDomain().getCodeSource().getLocation().getPath()).toPath();
        Path Parent = Main_path.getParent().getParent().getParent();
        File Directory = new File(Parent.toString()+"/Projecten/"+clickedrow.getProject_naam());
        //FXMLLoader loader = new FXMLLoader(getClass().getResource("sample_Projectscherm.fxml"));
        //Controller_Projectscherm control = loader.load();
        TableView table = (TableView) ProjectScene.lookup("#TV_Entry_Data");
        table.setItems(new Controller_Projectscherm().getdata(Directory));
        Label text = (Label) OpslagScene.lookup("#LB_naam");
        text.setText(clickedrow.getProject_naam());
        Stage stage = (Stage) BackButton.getScene().getWindow();
//        stage.setTitle(clickedrow.getProject_naam());
        stage.setTitle("Project scherm");
        Label titel = (Label) ProjectScene.lookup("#LB_project");
        titel.setText(clickedrow.getProject_naam());
        stage.setScene(ProjectScene);
    }

    public void setSdf(String newSdf){
        sdf = new SimpleDateFormat(newSdf);
        ObservableList<Main.Projecten> Projecten_data = getdata();
        tbData.setItems(Projecten_data);
    }
}