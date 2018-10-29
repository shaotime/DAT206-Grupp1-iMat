package iMat;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import se.chalmers.ait.dat215.project.Order;
import se.chalmers.ait.dat215.project.ShoppingItem;

import java.io.IOException;
import java.util.Random;

public class PreviouslyBoughtController extends AnchorPane {

    private Order order;

    @FXML private Label date;
    @FXML private Label nrOfArticles;
    @FXML private Label sum;

    @FXML private ImageView img1;
    @FXML private ImageView img2;
    @FXML private ImageView img3;
    @FXML private ImageView img4;
    @FXML private ImageView img5;
    @FXML private ImageView img6;
    @FXML private ImageView img7;
    @FXML private ImageView img8;
    @FXML private ImageView img9;

    private ImageView[] images;

    public PreviouslyBoughtController(Order order) {

        this.order = order;
        FXMLLoader fxmlLoader = new FXMLLoader(
                getClass().getResource("fxml/previouslyBought.fxml"));

        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        this.nrOfArticles.setText("Antal artiklar: " + order.getItems().size());

        double total = 0;
        for(ShoppingItem si : this.order.getItems()) {
            total += si.getTotal();
        }

        this.sum.setText(Utilities.formatPrice(total));



        this.date.setText(Utilities.formatPreviousBoughtDate(this.order));
        //         this.productImage.setImage(BackendHandler.getInstance().getFXImage(product));



        images = new ImageView[9];
        images[0] = img1;
        images[1] = img2;
        images[2] = img3;
        images[3] = img4;
        images[4] = img5;
        images[5] = img6;
        images[6] = img7;
        images[7] = img8;
        images[8] = img9;

        if(this.order.getItems().size() >= 1) { img1.setImage(BackendHandler.getInstance().getFXImage(this.order.getItems().get(0).getProduct())); }
        if(this.order.getItems().size() >= 2) { img2.setImage(BackendHandler.getInstance().getFXImage(this.order.getItems().get(1).getProduct())); }
        if(this.order.getItems().size() >= 3) { img3.setImage(BackendHandler.getInstance().getFXImage(this.order.getItems().get(2).getProduct())); }
        if(this.order.getItems().size() >= 4) { img4.setImage(BackendHandler.getInstance().getFXImage(this.order.getItems().get(3).getProduct())); }
        if(this.order.getItems().size() >= 5) { img5.setImage(BackendHandler.getInstance().getFXImage(this.order.getItems().get(4).getProduct())); }
        if(this.order.getItems().size() >= 6) { img6.setImage(BackendHandler.getInstance().getFXImage(this.order.getItems().get(5).getProduct())); }
        if(this.order.getItems().size() >= 7) { img7.setImage(BackendHandler.getInstance().getFXImage(this.order.getItems().get(6).getProduct())); }
        if(this.order.getItems().size() >= 8) { img8.setImage(BackendHandler.getInstance().getFXImage(this.order.getItems().get(7).getProduct())); }
        if(this.order.getItems().size() >= 9) { img9.setImage(BackendHandler.getInstance().getFXImage(this.order.getItems().get(8).getProduct())); }

        Random rand = new Random();

        for(ImageView imageView : images) {
            imageView.setRotate(rand.nextInt(120) - 60.0);
        }


    }

    public Order getOrder() {
        return this.order;
    }


}
