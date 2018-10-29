package iMat;

import javafx.animation.FadeTransition;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;
import se.chalmers.ait.dat215.project.ShoppingItem;
import sun.plugin.javascript.navig.Anchor;

import java.io.IOException;

public class SummaryShoppingCartItemController extends AnchorPane {


    private ShoppingItem item;
    @FXML private Label itemNameLabel;
    @FXML private Label quantityLabel;
    @FXML private Label priceLabel;

    public SummaryShoppingCartItemController(ShoppingItem shoppingItem) {
        this.item = shoppingItem;

        FXMLLoader fxmlLoader = new FXMLLoader(
                getClass().getResource("fxml/summaryShoppingCartItem.fxml"));

        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        this.itemNameLabel.setText(item.getProduct().getName());
        this.quantityLabel.setText((int)this.item.getAmount() + " " + item.getProduct().getUnitSuffix());
        this.priceLabel.setText(Utilities.formatPrice(this.item.getTotal()) + "");



    }
}
