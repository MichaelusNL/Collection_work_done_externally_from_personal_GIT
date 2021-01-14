package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.scene.web.HTMLEditor;

import javax.lang.model.util.Elements;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;
import javax.xml.transform.Source;
import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.ResourceBundle;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Controller_Projectscherm implements Initializable {
    @FXML
    public TableView<Main.Project_inhoud> TV_Entry_Data;
    @FXML
    public TableColumn<Main.Projecten, String> Entry_naam;
    @FXML
    public TableColumn<Main.Projecten, String> Entry_aanmaak;
    @FXML
    public TableColumn<Main.Projecten, String> Entry_bewerkt;
    @FXML
    public TableColumn<Main.Projecten, Integer> Entry_woord_aantal;
    @FXML
    private AnchorPane AP_Main;
    @FXML
    public static Label LB_project;
    @FXML
    private Button BT_terug;
    @FXML
    private Button BT_Statistieken;
    @FXML
    private Button BT_NieuweEntry;
    @FXML
    private Button BT_OpenEntry;

    private Scene ProjectenScene;
    private Scene StatistiekenScene;
    private Scene EntryScene;

    public Controller_Projectscherm() throws IOException {
    }

    public void setProjectenScene(Scene scene) {
        ProjectenScene = scene;
    }

    public void setStatistiekenScene(Scene scene) {
        StatistiekenScene = scene;
    }

    public void setEntryScene(Scene scene) {
        EntryScene = scene;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Entry_naam.setCellValueFactory(new PropertyValueFactory<>("Entry_naam"));
        Entry_aanmaak.setCellValueFactory(new PropertyValueFactory<>("Entry_aanmaak"));
        Entry_bewerkt.setCellValueFactory(new PropertyValueFactory<>("Entry_bewerkt"));
        Entry_woord_aantal.setCellValueFactory(new PropertyValueFactory<>("Entry_woord_aantal"));
        TV_Entry_Data.setRowFactory(tv -> {
            TableRow<Main.Project_inhoud> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty() && event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
                    Main.Project_inhoud clickedrow = row.getItem();
                    try {
                        this.openEntryClicked(clickedrow);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            });
            return row;
        });
    }
    Main x = new Main();
    public void openProjectenScene(ActionEvent actionEvent) {
        Stage primaryStage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        primaryStage.setTitle("Projecten scherm");
        primaryStage.setScene(ProjectenScene);
    }

    public void openEntryClicked(Main.Project_inhoud clickedrow) throws FileNotFoundException {
        Path Main_path = new File(Main.class.getProtectionDomain().getCodeSource().getLocation().getPath()).toPath();
        Path Parent = Main_path.getParent().getParent().getParent();
        Label text = (Label) Controller_Projectscherm.this.AP_Main.lookup("#LB_project");
        File Directory = new File(Parent.toString()+"/Projecten/"+text.getText()+"/"+clickedrow.getEntry_naam());
        Stage primaryStage = (Stage) BT_terug.getScene().getWindow();
        StringBuilder contentBuilder = new StringBuilder();
        try {
            BufferedReader in = new BufferedReader(new FileReader(Directory));
            String str;
            while ((str = in.readLine()) != null) {
                contentBuilder.append(str);
            }
            in.close();
        } catch (IOException e) {
        }
        String content = contentBuilder.toString();
        HTMLEditor editor = (HTMLEditor) EntryScene.lookup("#editor");
        editor.setHtmlText(content);
        primaryStage.setTitle("Entry scherm");
        primaryStage.setScene(EntryScene);
    }

    public void openStatistiekenScene(ActionEvent actionEvent) {
        Stage primaryStage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        primaryStage.setTitle("Statistieken scherm");
        primaryStage.setScene(StatistiekenScene);
    }

    public void openEntryScene(ActionEvent actionEvent) {
        Stage primaryStage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        HTMLEditor editor = (HTMLEditor) EntryScene.lookup("#editor");
        editor.setHtmlText("");
        primaryStage.setTitle("Entry scherm");
        primaryStage.setScene(EntryScene);

    }

    public void initialize() {}

    public ObservableList getdata(File Directory) throws IOException {
        File[] files = new File(Directory.getPath()).listFiles(File::isFile);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/YYYY");
        ObservableList<Main.Project_inhoud> Project_data = FXCollections.observableArrayList();
        for(File f : files) {
            String lasttime = sdf.format(f.lastModified());
            BasicFileAttributes a = null;
            try {
                a = Files.readAttributes(Paths.get(f.toURI()), BasicFileAttributes.class);
            } catch (IOException e) {
                e.printStackTrace();
            }
            String creationTime = sdf.format(a.creationTime().toMillis());
            FileInputStream fis = new FileInputStream(f);
            InputStreamReader Ir = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(Ir);
            int wordamount = 0;
            String line;
            while((line=br.readLine())!=null){
                if(!line.equals("")&&!line.isEmpty()){
                    line = line.replaceAll("\\<[ph123456]*>", "\n");
                    line = line.replaceAll("\\<[^>]*>","");
                    line = line.replaceAll("&nbsp;", "\n");
                    String[] wordlist = line.split("\\s+");
                    for (String i : wordlist) {
                        if(!i.equals("")){
//                            System.out.println(i);
                            wordamount += 1;
                        }
                    }
                }
            }
            Project_data.add(x.new Project_inhoud(f.getName(), creationTime, lasttime, wordamount));

        }
        return Project_data;
    }
}