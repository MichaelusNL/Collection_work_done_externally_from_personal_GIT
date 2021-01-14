package sample;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.jupiter.api.AfterEach;
import org.loadui.testfx.GuiTest;
import org.testfx.toolkit.PrimaryStageApplication;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class Controller_ProjectenschermTest extends GuiTest {
    AnchorPane Anchorpane;
    Label label;
    Button BackButton;
    Button NewProjectButton;
    TableView tbdata;
    TableColumn Project_naam;
    TableColumn Project_aanmaak;
    TableColumn Project_bewerkt;
    TableColumn Project_entries;

    @Override
    protected Parent getRootNode() {
        Parent parent = null;
        try {
            parent = FXMLLoader.load(getClass().getResource("sample_Projectenscherm.fxml"));
        } catch (IOException ex) {
            //go fuck yourself
            ;
        }
        return parent;
    }
    @Before
    public void setUpAll(){
        Anchorpane = find("#Anchorpane");
        label = find("#label");
        BackButton = find("#BackButton");
        NewProjectButton = find("#NewProjectButton");
        tbdata = find("#tbData");
    }

    @Test
    public void BackClicked() throws IOException {
        assertTrue(stage.isShowing());
        click("#BackButton",MouseButton.PRIMARY);
        Parent welkomscherm = FXMLLoader.load(getClass().getResource("sample_Welkomscherm.fxml"));
        assertTrue(welkomscherm.isVisible());

    }
    @Test
    public void NewProjectClicked() throws IOException {
        assertTrue(stage.isShowing());
        click("#NewProjectButton",MouseButton.PRIMARY);
        assertTrue(stage.isShowing());
        Parent popup = FXMLLoader.load(getClass().getResource("sample_Projectenscherm_popup.fxml"));
        assertTrue(popup.isVisible());
    }
    @Test
    public void RowDubbelClicked() throws IOException {
        assertTrue(stage.isShowing());
        click("#tbData",MouseButton.PRIMARY).release(MouseButton.PRIMARY).click(MouseButton.PRIMARY);
        Parent project = FXMLLoader.load(getClass().getResource("sample_Projectscherm.fxml"));
        assertTrue(project.isVisible());
    }
    @AfterEach
    void tearDown(){
    }
}