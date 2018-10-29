package iMat;

import javafx.animation.FadeTransition;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;
import se.chalmers.ait.dat215.project.ShoppingItem;

import java.io.IOException;

public class ShoppingCartItemController extends StackPane {

    private final static int FADE_DURATION = 500;

    private ShoppingItem item;
    private static boolean changedQuantity = false;
    private int amount;
    @FXML private Label itemNameLabel;
    @FXML private TextField quantityTextField;
    @FXML private Label priceLabel;
    @FXML private AnchorPane flash;
    @FXML private AnchorPane itemView;
    @FXML private AnchorPane whiteBackground;
    @FXML private Label unit;

    public ShoppingCartItemController(ShoppingItem shoppingItem) {
        this.item = shoppingItem;

        FXMLLoader fxmlLoader = new FXMLLoader(
                getClass().getResource("fxml/shoppingCartItem.fxml"));

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
        this.unit.setText(this.item.getProduct().getUnitSuffix());
        this.priceLabel.setText(Utilities.formatPrice(this.item.getTotal()) + "");
        this.itemView.toFront();



        this.quantityTextField.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (!newValue) {
                    quantityTextField.setStyle("-fx-border-color: none;");

                    if(!Utilities.isQuantityInputValid(quantityTextField.getText())) {
                        quantityTextField.requestFocus();
                        quantityTextField.setText("" + amount);
                        quantityTextField.setStyle("-fx-border-color: red;");

                    } else {

                        amount = Integer.parseInt(quantityTextField.getText());
                        item.setAmount(amount);
                        BackendHandler.getInstance().getShoppingCart().fireShoppingCartChanged(item, true);
                    }
                }
            }
        });

    }

    public static void setUpdateQuantity(boolean value) {
        changedQuantity = value;
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

    @FXML
    public void setUpdated() {
        System.out.println("changedQ 2: " + this.changedQuantity);
        System.out.println("Updating");
        if(!changedQuantity) {
            changedQuantity = true;
            this.flash.toFront();
            this.itemView.toFront();

            FadeTransition fader = new FadeTransition(Duration.millis(FADE_DURATION));

            fader.setNode(this.flash);

            fader.setFromValue(1.0);
            fader.setToValue(0.0);
            fader.setCycleCount(1);
            fader.setAutoReverse(false);

            fader.playFromStart();
            this.flash.setVisible(true);
        }

    }

}
