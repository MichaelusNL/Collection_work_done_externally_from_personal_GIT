package sample;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import org.junit.Test;
import org.loadui.testfx.GuiTest;

import java.io.IOException;
import java.nio.file.Path;
import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

public class ControllerTest extends GuiTest {
    @Override
    protected Parent getRootNode() {
        Parent parent = null;
        try {
            parent = FXMLLoader.load(getClass().getResource("sample_Entryscherm_popup_opslag.fxml"));

        } catch (IOException ex) {
            //go fuck yourself
            ;
        }
        return parent;
    }

    @Test
    public void opslag_pop_up_layout(){
        Label label = find("#labelt");
        assertEquals(label.getText(),"Vul een titel in:");
        Button opslaan = find("#BT_opslaan");
        assertEquals(opslaan.getText(),"Nu opslaan");
        Button verder = find("#BT_verder");
        assertEquals(verder.getText(),"Verder werken zonder op te slaan");
        TextField titel = find("#titel");
    }
    @Test
    public void Naaminvoer_opslaan(){
        TextField titel = find("#titel");
        assertEquals("", titel.getText());
        click("#titel",MouseButton.PRIMARY).press(KeyCode.SHIFT).press(KeyCode.SLASH).release(KeyCode.SLASH)
                .release(KeyCode.SHIFT).press(KeyCode.A,KeyCode.SLASH,KeyCode.BACK_SLASH,KeyCode.B, KeyCode.C);
        assertEquals("abc", titel.getText());
        click("#BT_opslaan", MouseButton.PRIMARY);
        Path main_path = new File(Main.class.getProtectionDomain().getCodeSource().getLocation().getPath()).toPath();
        Path parent = main_path.getParent().getParent().getParent();
        String title = titel.getText();
        File file = new File(parent+"\\"+title+".txt");
        assertEquals(true, file.exists());
        AnchorPane pane = find("#AP_pane");
        assertEquals(false, pane.getScene().getWindow().isShowing());

    }
    @Test
    public void verder_test(){
        AnchorPane pane = find("#AP_pane");
        click("#BT_verder", MouseButton.PRIMARY);
        assertEquals(false, pane.getScene().getWindow().isShowing());

    }
}