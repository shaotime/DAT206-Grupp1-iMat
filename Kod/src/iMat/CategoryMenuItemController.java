package iMat;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;


public class CategoryMenuItemController extends AnchorPane {

    @FXML private Label categoryNameLabel;
    private String colour;
    private Category category;
    private boolean active;

    public CategoryMenuItemController(Category category) {
        this.category = category;

        FXMLLoader fxmlLoader = new FXMLLoader(
                getClass().getResource("fxml/categoryMenuItem.fxml"));

        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
        this.categoryNameLabel.setText(Category.getName(this.category));
        colour = Category.getColour(this.category);

        if(this.category.equals(Category.TIDIGARE_KÖP)) {
            this.setStyle("-fx-padding: 0 0 40px 0;");
        }
    }

    public void mouseInChangeColour () {
         if(!active) {
             this.setStyle("-fx-background-color: " + colour + ";");
         }
    }

    public void mouseOutChangeColour () {
        if(!active) {
            this.setStyle("-fx-background-color: transparent ");
        }
    }

    public void categoryPressed () {

    }

    public Category getCategory() {
        return this.category;
    }

    public void setActive(boolean active) {
        this.active = active;
        if(active) {
            this.setStyle("-fx-background-color: " + colour + ";");
            this.categoryNameLabel.setStyle("-fx-font-weight: bold;");
        } else {
            this.setStyle("-fx-background-color: transparent;");
            this.categoryNameLabel.setStyle("-fx-font-weight: normal;");
        }
    }

}
