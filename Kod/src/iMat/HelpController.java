package iMat;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class HelpController extends AnchorPane {
    private BackendHandler handler = BackendHandler.getInstance();


    Overlay caller;

    public HelpController(Overlay caller) {
        this.caller = caller;

        FXMLLoader fxmlLoader = new FXMLLoader(
                getClass().getResource("fxml/helpScene.fxml"));

        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

    }


    @FXML
    public void close() {
        caller.close();
    }

    public void open() {
    }

}
