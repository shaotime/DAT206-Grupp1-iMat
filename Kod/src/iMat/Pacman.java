package iMat;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class Pacman extends AnchorPane {

    @FXML private ImageView pacmanImage;
    @FXML private AnchorPane pacmanAnchorPane;

    public Pacman() {

        FXMLLoader fxmlLoader = new FXMLLoader(
                getClass().getResource("fxml/pacman.fxml"));

        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }


    }

    public ImageView getPacmanImage() {
        return this.pacmanImage;
    }

    public AnchorPane getPacmanAnchorPane() {
        return this.pacmanAnchorPane;
    }
}
