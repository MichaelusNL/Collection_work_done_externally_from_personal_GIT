package sample;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.junit.Before;
import org.junit.Test;
import org.loadui.testfx.GuiTest;
import java.io.IOException;
import static org.junit.jupiter.api.Assertions.*;

public class Controller_WelkomschermTest extends GuiTest{
    AnchorPane AnchorPane;
    Button BT_Projecten;

    @Override
    protected Parent getRootNode() {
        Parent parent = null;
        try {
            parent = FXMLLoader.load(getClass().getResource("sample_Welkomscherm.fxml"));

        } catch (IOException ex) {
//            System.out.println("kan niet fxml vinden");
        }
        return parent;
    }

    @Before
    public void setupall() {
        AnchorPane = find("#AP_Main");
        BT_Projecten = find("#BT_Projecten");
    }

/*    @Test
    public void checkProjectButton() throws Exception{
//        assertEquals(true, AnchorPane.getScene().getWindow().isShowing());
        assertTrue(stage.isShowing());
        click("#BT_Projecten",MouseButton.PRIMARY);
//        assertEquals(true, stage.getTitle().matches("Welkom scherm"));
        assertEquals(false, AnchorPane.getScene().getWindow().isShowing());

    } */

    @Test
    public void checkOptiesButton() {
        assertTrue(AnchorPane.getScene().getWindow().isShowing());
        click("#BT_Projecten", MouseButton.PRIMARY);
        assertEquals(false,AnchorPane.getScene().getWindow().hasProperties());

//        click("#BT_Projecten", MouseButton.PRIMARY); //.release(MouseButton.PRIMARY);
//        assertTrue(AnchorPane.getScene().getWindow().isShowing());
//        Stage stage = (Stage) BT_Projecten.getScene().getWindow();



//        assertTrue(stage.getScene().getWindow().getTitle().matches("Welkom scherm"));
    }

}