package iMat;

import javafx.animation.FadeTransition;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;
import se.chalmers.ait.dat215.project.ShoppingItem;

import java.io.IOException;

public class CheckoutShoppingCartItemController extends AnchorPane {

    private final static int FADE_DURATION = 500;

    private ShoppingItem item;
    private int amount;
    @FXML private Label itemNameLabel;
    @FXML private TextField quantityTextField;
    @FXML private Label priceLabel;
    @FXML private Label unit;
    @FXML private ImageView productImage;

    public CheckoutShoppingCartItemController(ShoppingItem shoppingItem) {
        this.item = shoppingItem;

        FXMLLoader fxmlLoader = new FXMLLoader(
                getClass().getResource("fxml/checkoutShoppingCartItem.fxml"));

        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        this.itemNameLabel.setText(item.getProduct().getName());
        this.quantityTextField.setText((int)this.item.getAmount() + "");
        this.amount = (int)this.item.getAmount();
        this.priceLabel.setText(Utilities.formatPrice(this.item.getTotal()) + "");
        this.productImage.setImage(BackendHandler.getInstance().getFXImage(item.getProduct()));
        this.unit.setText(item.getProduct().getUnitSuffix());




    }



    @FXML
    public void removeFromShoppingCart() {
        BackendHandler.getInstance().getShoppingCart().removeItem(item);
    }

    @FXML
    public void increaseAmount() {
        this.amount++;
        this.quantityTextField.setText(Integer.toString(amount));
        this.item.setAmount(this.amount);
        BackendHandler.getInstance().getShoppingCart().fireShoppingCartChanged(item, true);
    }

    @FXML
    public void decreaseAmount() {
        if(this.amount > 1) {
            this.amount--;
            this.quantityTextField.setText(Integer.toString(amount));
            this.item.setAmount(this.amount);
            BackendHandler.getInstance().getShoppingCart().fireShoppingCartChanged(item, true);

        }
    }

}
