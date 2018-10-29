package iMat;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import se.chalmers.ait.dat215.project.IMatDataHandler;
import se.chalmers.ait.dat215.project.Product;

import java.io.IOException;

public class Candy extends AnchorPane {

    @FXML private ImageView candy;

    public Candy(Product product) {

        FXMLLoader fxmlLoader = new FXMLLoader(
                getClass().getResource("fxml/candy.fxml"));

        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        this.candy.setImage(IMatDataHandler.getInstance().getFXImage(product));

    }
}
