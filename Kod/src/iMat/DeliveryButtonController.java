package iMat;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import se.chalmers.ait.dat215.project.ShoppingItem;

import java.io.IOException;

public class DeliveryButtonController extends Pane {

    @FXML private Label textWeekday;
    @FXML private Label textDate;
    @FXML private Label textTime;
    private CheckoutController caller;
    private int type;
    @FXML private Pane button;

    public DeliveryButtonController(int type, String textTime, CheckoutController caller) {

        FXMLLoader fxmlLoader = new FXMLLoader(
                getClass().getResource("fxml/deliveryButton.fxml"));

        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        this.textTime.setText(textTime);
        this.textDate.setVisible(false);
        this.textWeekday.setVisible(false);
        this.caller = caller;
        this.type = type;

    }

    public DeliveryButtonController(int type, String weekDay, String date, CheckoutController caller) {

        FXMLLoader fxmlLoader = new FXMLLoader(
                getClass().getResource("fxml/deliveryButton.fxml"));

        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        this.textTime.setVisible(false);
        this.textDate.setText(date);
        this.textWeekday.setText(weekDay);
        this.caller = caller;
        this.type = type;

    }

    public void setActive() {
        if(type == 1) {
            this.caller.setAllDatesInactive();
            this.caller.setChosenDeliveryDate(this.textWeekday.getText() + " " + this.textDate.getText());
        } else {
            this.caller.setChosenDeliveryTime(this.textTime.getText());
            this.caller.setAllTimesInactive();
        }
        this.button.getStyleClass().clear();
        this.button.getStyleClass().add("activePane");
    }

    public void setInactive() {
        this.button.getStyleClass().clear();
        this.button.getStyleClass().add("inactivePane");
    }
}
