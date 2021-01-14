package sample;

import javafx.application.Application;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.text.SimpleDateFormat;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Path Main_path = new File(Main.class.getProtectionDomain().getCodeSource().getLocation().getPath()).toPath();
        Path Parent = Main_path.getParent().getParent().getParent();
        File Directory = new File(Parent.toString()+"/Projecten");
        if (!Directory.exists()){
            Directory.mkdir();
        }
        File test = new File(Directory.getPath()+"/Test");
        if (!test.exists()) {
            test.mkdir();
        }
        String file = new String(Directory.getPath()+"/Test/test.txt");
        BufferedWriter writer = new BufferedWriter(new FileWriter(file));
        writer.write("<P>Dit is een test tekst</P>");
        writer.close();

/*
        1. Welkomscherm                         --> Optiescherm, Projectenscherm
        2. Optiescherm                          --> Welkomscherm
        3. Projectenscherm                      --> Welkomscherm, Projectscherm
        4. Projectenscherm_popup                .
        5. Projectscherm                        --> Projectenscherm, Statistiekenscherm, Entryscherm
        6. Statistiekenscherm                   --> Projectscherm
        7. Entryscherm                          --> Projectscherm
        8. Entryscherm_popup_opslag             .
        9. Entryscherm_popup_onopgeslagenwerk   .
 */

        FXMLLoader WelkomPageLoader = new FXMLLoader(getClass().getResource("sample_Welkomscherm.fxml"));
        Parent WelkomPane = WelkomPageLoader.load();
        Scene WelkomScene = new Scene(WelkomPane, 630, 620);

        FXMLLoader OptiePageLoader = new FXMLLoader(getClass().getResource("sample_Optiescherm.fxml"));
        Parent OptiePane = OptiePageLoader.load();
        Scene OptieScene = new Scene(OptiePane, 630, 620);

        FXMLLoader ProjectenPaneLoader = new FXMLLoader(getClass().getResource("sample_Projectenscherm.fxml"));
        Parent ProjectenPane = ProjectenPaneLoader.load();
        Scene ProjectenScene = new Scene(ProjectenPane, 630, 620);

        FXMLLoader ProjectenPopupPaneLoader = new FXMLLoader(getClass().getResource("sample_Projectenscherm_popup.fxml"));
        Parent ProjectenPopupPane = ProjectenPopupPaneLoader.load();
        Scene ProjectenPopupScene = new Scene(ProjectenPopupPane, 370, 200);

        FXMLLoader ProjectPaneLoader = new FXMLLoader(getClass().getResource("sample_Projectscherm.fxml"));
        Parent ProjectPane = ProjectPaneLoader.load();
        Scene ProjectScene = new Scene(ProjectPane, 630, 620);

        FXMLLoader StatistiekenPageLoader = new FXMLLoader(getClass().getResource("sample_Statistiekenscherm.fxml"));
        Parent StatistiekenPane = StatistiekenPageLoader.load();
        Scene StatistiekenScene = new Scene(StatistiekenPane, 630, 620);

        FXMLLoader EntryPageLoader = new FXMLLoader(getClass().getResource("sample_Entryscherm.fxml"));
        Parent EntryPane = EntryPageLoader.load();
        Scene EntryScene = new Scene(EntryPane, 630, 620);

        FXMLLoader EntryOpslagPageLoader = new FXMLLoader(getClass().getResource("sample_Entryscherm_popup_opslag.fxml"));
        Parent EntryOpslagPane = EntryOpslagPageLoader.load();
        Scene EntryOpslagScene = new Scene(EntryOpslagPane);

        FXMLLoader EntryTerugPageLoader = new FXMLLoader(getClass().getResource("sample_Entryscherm_popup_terug.fxml"));
        Parent EntryTerugPane = EntryTerugPageLoader.load();
        Scene EntryTerugScene = new Scene(EntryTerugPane);

        Controller_Welkomscherm WelkomPaneController = WelkomPageLoader.getController();
        WelkomPaneController.setProjectenScene(ProjectenScene);
        WelkomPaneController.setOptieScene(OptieScene);

        Controller_Optiescherm OptiePaneController = OptiePageLoader.getController();
        OptiePaneController.setWelkomScene(WelkomScene);
        OptiePaneController.setProjectenScene(ProjectenScene);
        OptiePaneController.setProjectenPopupScene(ProjectenPopupScene);
        OptiePaneController.setProjectScene(ProjectScene);
        OptiePaneController.setStatistiekenScene(StatistiekenScene);
        OptiePaneController.setEntryScene(EntryScene);
        OptiePaneController.setEntryOpslagScene(EntryOpslagScene);

        Controller_Projectenscherm ProjectenPaneController = ProjectenPaneLoader.getController();
        ProjectenPaneController.setWelkomScene(WelkomScene);
        ProjectenPaneController.setProjectScene(ProjectScene);
        ProjectenPaneController.setPopupScene(ProjectenPopupScene);
        ProjectenPaneController.setOptieScene(OptieScene);
        ProjectenPaneController.setOpslagScene(EntryOpslagScene);
        OptiePaneController.setProjectenController(ProjectenPaneController);

        Controller_Projectscherm ProjectPaneController = ProjectPaneLoader.getController();
        ProjectPaneController.setProjectenScene(ProjectenScene);
        ProjectPaneController.setStatistiekenScene(StatistiekenScene);
        ProjectPaneController.setEntryScene(EntryScene);

        Controller_Statistiekenscherm StatistiekenPaneController = StatistiekenPageLoader.getController();
        StatistiekenPaneController.setProjectScene(ProjectScene);

        Controller_Entryscherm EntryPaneController = EntryPageLoader.getController();
//        EntryPaneController.setProjectScene(ProjectScene);
        EntryPaneController.setOpslagScene(EntryOpslagScene);
        EntryPaneController.setTerugScene(EntryTerugScene);
        EntryPaneController.setOpslagControl(EntryOpslagPageLoader);
        EntryPaneController.setProjectControl(ProjectPaneLoader);
        EntryPaneController.setProjectScene(ProjectScene);

//        Controller_Entryscherm_popup_opslag EntryOpslagPaneController = EntryOpslagPageLoader.getController();
//        EntryOpslagPaneController.setProjectController(ProjectPaneController);

        Controller_Entryscherm_popup_terug EntryTerugPaneController = EntryTerugPageLoader.getController();
        EntryTerugPaneController.setProjectScene(ProjectScene);
        EntryTerugPaneController.setEntryLoader(EntryPageLoader);

        primaryStage.setResizable(false);
        primaryStage.setTitle("Welkom scherm");
        primaryStage.setScene(WelkomScene);
        primaryStage.show();
    }

    public class Projecten {
        private SimpleStringProperty Project_naam;
        private SimpleStringProperty Project_aanmaak;
        private SimpleStringProperty Project_bewerkt;
        private SimpleIntegerProperty Project_entries;
        public Projecten(String Project_naam, String Project_aanmaak, String Project_bewerkt, Integer Project_entries) {
            this.Project_naam = new SimpleStringProperty(Project_naam);
            this.Project_aanmaak = new SimpleStringProperty(Project_aanmaak);
            this.Project_bewerkt = new SimpleStringProperty(Project_bewerkt);
            this.Project_entries = new SimpleIntegerProperty(Project_entries);
        }
        public String getProject_naam() {
            return Project_naam.get();
        }
        public void setProject_naam(String Project_naam) {
            this.Project_naam = new SimpleStringProperty(Project_naam);
        }
        public String getProject_aanmaak() {
            return Project_aanmaak.get();
        }
        public void setProject_aanmaak(String Project_aanmaak) {
            this.Project_aanmaak = new SimpleStringProperty(Project_aanmaak);
        }
        public String getProject_bewerkt() {
            return Project_bewerkt.get();
        }
        public void setProject_bewerkt(String Project_bewerkt) {
            this.Project_bewerkt = new SimpleStringProperty(Project_bewerkt);
        }
        public Integer getProject_entries() {
            return Project_entries.get();
        }
        public void setProject_entries(Integer Project_entries) {
            this.Project_entries = new SimpleIntegerProperty(Project_entries);
        }
    }
    public class Project_inhoud{
        private SimpleStringProperty Entry_naam;
        private SimpleStringProperty Entry_aanmaak;
        private SimpleStringProperty Entry_bewerkt;
        private SimpleIntegerProperty Entry_woord_aantal;
        public Project_inhoud(String Entry_naam, String Entry_aanmaak, String Entry_bewerkt, Integer Entry_woord_aantal) {
            this.Entry_naam = new SimpleStringProperty(Entry_naam);
            this.Entry_aanmaak = new SimpleStringProperty(Entry_aanmaak);
            this.Entry_bewerkt = new SimpleStringProperty(Entry_bewerkt);
            this.Entry_woord_aantal = new SimpleIntegerProperty(Entry_woord_aantal);
        }
        public String getEntry_naam() {
            return Entry_naam.get();
        }
        public void setEntry_naam(String Entry_naam) {
            this.Entry_naam = new SimpleStringProperty(Entry_naam);
        }
        public String getEntry_aanmaak() {
            return Entry_aanmaak.get();
        }
        public void setEntry_aanmaak(String Entry_aanmaak) {
            this.Entry_aanmaak = new SimpleStringProperty(Entry_aanmaak);
        }
        public String getEntry_bewerkt() {
            return Entry_bewerkt.get();
        }
        public void setEntry_bewerkt(String Entry_bewerkt) {
            this.Entry_bewerkt = new SimpleStringProperty(Entry_bewerkt);
        }
        public Integer getEntry_woord_aantal() {
            return Entry_woord_aantal.get();
        }
        public void setEntry_woord_aantal(Integer Entry_woord_aantal) {
            this.Entry_woord_aantal = new SimpleIntegerProperty(Entry_woord_aantal);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}