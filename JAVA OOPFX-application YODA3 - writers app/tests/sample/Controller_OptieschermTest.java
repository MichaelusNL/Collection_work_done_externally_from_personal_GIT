package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.loadui.testfx.GuiTest;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class Controller_OptieschermTest extends GuiTest{

    @Override
    protected Parent getRootNode() {
        Parent parent = null;
        try {
            parent = FXMLLoader.load(getClass().getResource("sample_Optiescherm.fxml"));

        } catch (IOException ex) {
            ;
        }
        return parent;
    }


    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }


    @Test
    public void terug(){
        click("#BT_terug");
        assertTrue(stage.getTitle().matches("Welkom scherm"));
    }

    @Test
    public void themeSwitchDay() throws Exception{
        /*
        FXMLLoader WelkomPageLoader = new FXMLLoader(getClass().getResource("sample_Welkomscherm.fxml"));
        Parent WelkomPane = WelkomPageLoader.load();
        Scene WelkomScene = new Scene(WelkomPane, 630, 620);
*/
        ChoiceBox cb_thema = find("#CB_thema");
        click("#CB_thema");
        type(KeyCode.DOWN);
        type(KeyCode.ENTER);
        assertEquals((int)cb_thema.getSelectionModel().selectedIndexProperty().getValue(), 1);
        assertEquals(cb_thema.getValue(), "Dag modus");
        AnchorPane mainPane = find("#AP_Main");
        assertEquals(mainPane.getStyle(), "-fx-background-color: #ffffff");
        assertEquals(mainPane.getScene().lookup("#BT_terug").getStyle(), "-fx-background-color: #05acd8");
        Label LB_thema = find("#LB_thema");
        assertEquals(LB_thema.getTextFill(), Color.web("#8fb56e"));
        Label LB_tijdDag = find("#LB_tijd");
        assertEquals(LB_tijdDag.getTextFill(), Color.web("#8fb56e"));
        Label LB_titel = find("#LB_titel");
        assertEquals(LB_titel.getTextFill(), Color.web("#8fb56e"));
    }



    @Test
    public void themeSwitchColour(){
        ChoiceBox cb_thema = find("#CB_thema");
        click("#CB_thema");
        type(KeyCode.DOWN);
        type(KeyCode.DOWN);
        type(KeyCode.ENTER);
        assertEquals((int)cb_thema.getSelectionModel().selectedIndexProperty().getValue(), 2);
        assertEquals("Kleur modus", cb_thema.getValue());
        AnchorPane mainPane = find("#AP_Main");
        assertEquals(mainPane.getStyle(), "-fx-background-color: #ffff00");
        assertEquals(mainPane.getScene().lookup("#BT_terug").getStyle(), "-fx-background-color: #0000ff");
        Label LB_thema = find("#LB_thema");
        assertEquals(LB_thema.getTextFill(), Color.web("#00ff00"));
        Label LB_tijd = find("#LB_tijd");
        assertEquals(LB_tijd.getTextFill(), Color.web("#00ff00"));
        Label LB_titel = find("#LB_titel");
        assertEquals(LB_titel.getTextFill(), Color.web("#00ff00"));
    }

    @Test
    public void themeSwitchNight() throws Exception{
        /*
        FXMLLoader WelkomPageLoader = new FXMLLoader(getClass().getResource("sample_Welkomscherm.fxml"));
        Parent WelkomPane = WelkomPageLoader.load();
        Scene WelkomScene = new Scene(WelkomPane, 630, 620);
*/
        ChoiceBox cb_thema = find("#CB_thema");
        click("#CB_thema");
        type(KeyCode.DOWN);
        type(KeyCode.ENTER);
        click("#CB_thema");
        type(KeyCode.UP);
        type(KeyCode.ENTER);
        assertEquals((int)cb_thema.getSelectionModel().selectedIndexProperty().getValue(), 0);
        assertEquals(cb_thema.getValue(), "Nacht modus");
        AnchorPane mainPane = find("#AP_Main");
        assertEquals(mainPane.getStyle(), "-fx-background-color: #000000");
        assertEquals(mainPane.getScene().lookup("#BT_terug").getStyle(),
                "-fx-background-color: #05acd8");
        Label LB_thema = find("#LB_thema");
        assertEquals(LB_thema.getTextFill(), Color.web("#8fb56e"));
        Label LB_tijdDag = find("#LB_tijd");
        assertEquals(LB_tijdDag.getTextFill(), Color.web("#8fb56e"));
        Label LB_titel = find("#LB_titel");
        assertEquals(LB_titel.getTextFill(), Color.web("#8fb56e"));
    }

    @Test public void timeSwitchDdMmYyyy(){
        ChoiceBox cb_tijd = find("#CB_tijd");
        click("#CB_tijd");
        type(KeyCode.DOWN);
        type(KeyCode.UP);
        type(KeyCode.ENTER);
        assertEquals((int)cb_tijd.getSelectionModel().selectedIndexProperty().getValue(), 0);
        assertEquals(cb_tijd.getValue(), "dd/mm/yyyy");

    }

    @Test public void timeSwitchMmDdYyyy(){
        ChoiceBox cb_tijd = find("#CB_tijd");
        click("#CB_tijd");
        type(KeyCode.DOWN);
        type(KeyCode.ENTER);
        assertEquals((int)cb_tijd.getSelectionModel().selectedIndexProperty().getValue(), 1);
        assertEquals(cb_tijd.getValue(), "mm/dd/yyyy");

    }

    @Test public void timeSwitchYyyyMmDd(){
        ChoiceBox cb_tijd = find("#CB_tijd");
        click("#CB_tijd");
        type(KeyCode.DOWN);
        type(KeyCode.DOWN);
        type(KeyCode.ENTER);
        assertEquals((int)cb_tijd.getSelectionModel().selectedIndexProperty().getValue(), 2);
        assertEquals(cb_tijd.getValue(), "yyyy/mm/dd");

    }

    @Test
    public void checkLabels(){
        Label thema = find("#LB_thema");
        assertEquals(thema.getText(), "Thema's:");

        Label tijd = find("#LB_tijd");
        assertEquals(tijd.getText(), "Tijd opties:");

        Label titel = find("#LB_titel");
        assertEquals(titel.getText(), "Opties");

    }

    @Test
    public void checkButton(){
        Button terug = find("#BT_terug");
        assertEquals(terug.getText(), "Terug");

    }

    @Test
    public void checkChoiceBoxContents(){
        ChoiceBox cb_thema = find("#CB_thema");
        assertEquals(cb_thema.getItems(), FXCollections.observableArrayList("Nacht modus", "Dag modus", "Kleur modus"));

        ChoiceBox cb_tijd = find("#CB_tijd");
        assertEquals(cb_tijd.getItems(), FXCollections.observableArrayList("dd/mm/yyyy", "mm/dd/yyyy", "yyyy/mm/dd"));
    }

    @Test
    public void checkChoiceBoxValues(){
        ChoiceBox cb_thema = find("#CB_thema");
        assertEquals(cb_thema.getValue(), "Nacht modus");

        ChoiceBox cb_tijd = find("#CB_tijd");
        assertEquals(cb_tijd.getValue(), "dd/mm/yyyy");
    }

}