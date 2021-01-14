package sample;

import com.sun.javafx.robot.FXRobot;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runners.Parameterized;
import org.loadui.testfx.GuiTest;
import org.testfx.api.FxAssert;
import org.testfx.api.FxRobot;
import org.testfx.api.FxToolkit;
import org.testfx.matcher.base.NodeMatchers;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.concurrent.TimeoutException;

import static com.sun.deploy.perf.DeployPerfUtil.write;
import static org.junit.jupiter.api.Assertions.*;
import static org.loadui.testfx.Assertions.verifyThat;
import static org.testfx.matcher.control.TextMatchers.hasText;
import org.testfx.matcher.base.NodeMatchers;
import org.testfx.matcher.control.LabeledMatchers;
import org.testfx.robot.KeyboardRobot;
import sun.invoke.empty.Empty;

public class Controller_Projectenscherm_popupTest extends GuiTest {
    AnchorPane AnchorPane;
    Button AanmaakButton;
    Button AnnuleerButton;
    Label LB_naam;
    TextField ProjectName;
    Label LeegWaarschuwing;
    Label BestaatWaarschuwing;


    @Override
    protected Parent getRootNode() {
        Parent parent = null;
        try {
            parent = FXMLLoader.load(getClass().getResource("sample_Projectenscherm_popup.fxml"));

        } catch (IOException ex) {
            //go fuck yourself
        }
        return parent;
    }
    @Before
    public void setupall(){
        AnchorPane = find("#AnchorPane");
        AanmaakButton = find("#AanmaakButton");
        AnnuleerButton = find("#AnnuleerButton");
        LB_naam = find("#LB_naam");
        ProjectName = find("#ProjectName");
        LeegWaarschuwing = (Label) AnchorPane.getChildren().get(4);
        BestaatWaarschuwing = (Label) AnchorPane.getChildren().get(5);
    }
    @BeforeEach
    void init(){

    }

    @Test
    public void AnnuleerClicked(){
        assertTrue(AnchorPane.getScene().getWindow().isShowing());
        click("#AnnuleerButton", MouseButton.PRIMARY).release(MouseButton.PRIMARY);
        assertFalse(AnchorPane.getScene().getWindow().isShowing());
    }
    @Test
    public void Naaminvoer(){
        assertEquals("", ProjectName.getText());
        click("#ProjectName",MouseButton.PRIMARY).press(KeyCode.SHIFT).press(KeyCode.SLASH).release(KeyCode.SLASH)
                .release(KeyCode.SHIFT).press(KeyCode.A,KeyCode.SLASH,KeyCode.BACK_SLASH,KeyCode.B,KeyCode.C);
        assertEquals("abc", ProjectName.getText());
    }
    @Test
    public void AanmaakClicked() {
        click("#AanmaakButton",MouseButton.PRIMARY).release(MouseButton.PRIMARY);
        assertTrue(stage.isShowing());
        assertTrue(LeegWaarschuwing.isVisible());
        assertFalse(BestaatWaarschuwing.isVisible());
        Path Main_path = new File(Main.class.getProtectionDomain().getCodeSource().getLocation().getPath()).toPath();
        Path Parent = Main_path.getParent().getParent().getParent();
        File file = new File(Parent+"\\Projecten\\Test");
        if(!file.exists()) {
            file.mkdir();
        }
        click("#ProjectName",MouseButton.PRIMARY).type("Test");
        click("#AanmaakButton",MouseButton.PRIMARY);
        assertTrue(stage.isShowing());
        assertFalse(LeegWaarschuwing.isVisible());
        assertTrue(BestaatWaarschuwing.isVisible());
        ProjectName.setText("");
        click("#ProjectName",MouseButton.PRIMARY).type("Nieuw");
        File Nieuw = new File(Parent+"\\Projecten\\Nieuw");
        Nieuw.delete();
        click("#AanmaakButton",MouseButton.PRIMARY).release(MouseButton.PRIMARY);
        assertFalse(stage.isShowing());
        assertFalse(LeegWaarschuwing.isVisible());
        assertFalse(BestaatWaarschuwing.isVisible());
    }

    @AfterEach
    void tearDown(){
    }
}