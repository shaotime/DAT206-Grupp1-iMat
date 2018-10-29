package iMat;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import se.chalmers.ait.dat215.project.Product;
import se.chalmers.ait.dat215.project.ShoppingCart;
import se.chalmers.ait.dat215.project.ShoppingItem;

import java.io.IOException;

public class ProductController extends AnchorPane {

    private Product product;
    private int amount;
    @FXML private Label productNameLabel;
    @FXML private ImageView productImage;
    @FXML private TextField quantity;
    @FXML private Label price;
    @FXML private Label type;
    @FXML private ImageView favoriteIcon;
    @FXML private Label unit;

    public ProductController(Product product) {

        this.product = product;
        FXMLLoader fxmlLoader = new FXMLLoader(
                getClass().getResource("fxml/product.fxml"));

        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
        this.productNameLabel.setText(this.product.getName());
        this.quantity.setText("1");

        this.productImage.setImage(BackendHandler.getInstance().getFXImage(product));

        if(BackendHandler.getInstance().isFavorite(this.getProduct())) {
            favoriteIcon.setImage(new Image("iMat/img/heart_on.jpg"));
        }

        this.price.setText(Utilities.formatPrice(this.product.getPrice()));
        this.type.setText(this.product.getUnit());
        this.unit.setText(this.product.getUnitSuffix());

        this.quantity.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (!newValue) {
                    quantity.setStyle("-fx-border-color: none;");
                }
            }
        });
    }

    public Product getProduct() {
        return this.product;
    }

public void buy() {

        if(!Utilities.isQuantityInputValid(this.quantity.getText())) {
            this.quantity.requestFocus();
            this.quantity.setText("1");
            this.quantity.setStyle("-fx-border-color: red;");

        } else {

            ShoppingCartItemController.setUpdateQuantity(false);
            ShoppingCart shoppingCart = BackendHandler.getInstance().getShoppingCart();

            boolean containsItem = false;
            int amountToAdd = Integer.parseInt(this.quantity.getText());

            for(ShoppingItem shoppingItem : shoppingCart.getItems()) {
                if(shoppingItem.getProduct().equals(product)) {
                    containsItem = true;
                }
            }

            if(!containsItem) {
                BackendHandler.getInstance().getShoppingCart().addItem(new ShoppingItem(product, amountToAdd));
            } else {
                ShoppingItem itemToChange;
                for(ShoppingItem item : BackendHandler.getInstance().getShoppingCart().getItems()) {
                    if(item.getProduct().equals(this.product)) {
                        item.setAmount((int)item.getAmount() + amountToAdd);
                        BackendHandler.getInstance().getShoppingCart().fireShoppingCartChanged(item, true);
                        break;
                    }
                }
            }
        }
    }

    public void increaseAmount() {
        this.amount++;
        this.quantity.setText(Integer.toString(amount));
    }

    public void decreaseAmount() {
        if(this.amount > 1) {
            this.amount--;
            this.quantity.setText(Integer.toString(amount));
        }
    }
    public void addToFavorites () {
        if(!BackendHandler.getInstance().isFavorite(this.getProduct())) {
            BackendHandler.getInstance().favorites().add(this.getProduct());
            favoriteIcon.setImage(new Image("iMat/img/heart_on.jpg"));
        } else {
            BackendHandler.getInstance().favorites().remove(this.getProduct());
            favoriteIcon.setImage(new Image("iMat/img/heart_off.jpg"));
        }
        BackendHandler.getInstance().shutDown();
    }

}
