package iMat;

import com.sun.tools.javac.comp.Check;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.*;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import se.chalmers.ait.dat215.project.CartEvent;
import se.chalmers.ait.dat215.project.Customer;
import se.chalmers.ait.dat215.project.ShoppingCartListener;
import se.chalmers.ait.dat215.project.ShoppingItem;

import javax.rmi.CORBA.Util;
import java.util.*;
import java.text.SimpleDateFormat;

import java.awt.*;
import java.io.IOException;
import java.util.List;

public class CheckoutController extends AnchorPane implements ShoppingCartListener {
    private BackendHandler handler = BackendHandler.getInstance();
    private int state = 0;

    Overlay caller;

    @FXML private AnchorPane checkoutShoppingCart;
    @FXML private FlowPane checkoutShoppingCartFlowPane;
    @FXML private FlowPane summaryShoppingCartFlowPane;
    @FXML private AnchorPane checkoutDeliveryTime;
    @FXML private AnchorPane checkoutSummary;
    @FXML private AnchorPane checkoutPayment;
    @FXML private javafx.scene.control.Label totalSumLabel;

    @FXML private ScrollPane shoppingCartSummary;
    @FXML private Button previousButton;
    @FXML private Button nextButton;
    @FXML private Circle activityCircle;

    @FXML private RadioButton payWhenDelivered;
    @FXML private RadioButton payNow;

    @FXML private Pane creditcardPayment;
    @FXML private Text summaryText;

    @FXML private TextField firstName;
    @FXML private TextField lastName;
    @FXML private TextField phoneNumber;
    @FXML private TextField email;
    @FXML private TextField address;
    @FXML private TextField postCode;
    @FXML private TextField postAddress;

    @FXML private Text warningFirstNameLabel;
    @FXML private Text warningLastNameLabel;
    @FXML private Text warningPhoneNumberLabel;
    @FXML private Text warningEmailLabel;
    @FXML private Text warningAddressLabel;
    @FXML private Text warningPostCodeLabel;
    @FXML private Text warningPostAddressLabel;

    @FXML private TextField holdersName;
    @FXML private TextField validMonth;
    @FXML private TextField validYear;
    @FXML private TextField cardNumber1;
    @FXML private TextField cardNumber2;
    @FXML private TextField cardNumber3;
    @FXML private TextField cardNumber4;
    @FXML private TextField verificationCode;

    @FXML private Label lastPageSum;

    @FXML private FlowPane deliveryDatesFlowPanel;
    @FXML private FlowPane deliveryTimesFlowPanel;

    @FXML private GridPane checkoutGridPane;
    @FXML private AnchorPane checkoutFinishedPane;

    private boolean addressVerified;
    private boolean paymentVerified;

    private java.util.List<DeliveryButtonController> deliveryDatesButtons;
    private java.util.List<DeliveryButtonController> deliveryTimesButtons;

    private Map<TextField, Text> inputFields;

    private int paymentMethod = 0;

    private String chosenDeliveryDate;
    private String chosenDeliveryTime;

    public CheckoutController(Overlay caller) {
        this.caller = caller;
        BackendHandler.getInstance().getShoppingCart().addShoppingCartListener(this);

        FXMLLoader fxmlLoader = new FXMLLoader(
                getClass().getResource("fxml/checkoutScene.fxml"));

        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
        this.previousButton.setStyle("-fx-opacity: 0.5");

        updateShoppingCart();

        this.warningFirstNameLabel.setVisible(false);
        this.warningLastNameLabel.setVisible(false);
        this.warningPhoneNumberLabel.setVisible(false);
        this.warningEmailLabel.setVisible(false);
        this.warningAddressLabel.setVisible(false);
        this.warningPostCodeLabel.setVisible(false);
        this.warningPostAddressLabel.setVisible(false);

        inputFields = new HashMap<TextField, Text>();
        inputFields.put(this.firstName, this.warningFirstNameLabel);
        inputFields.put(this.lastName, this.warningLastNameLabel);;
        inputFields.put(this.address, this.warningAddressLabel);
        inputFields.put(this.postAddress, this.warningPostAddressLabel);
        inputFields.put(this.postCode, this.warningPostCodeLabel);
        inputFields.put(this.phoneNumber, this.warningPhoneNumberLabel);
        inputFields.put(this.email, this.warningEmailLabel);

        initDeliveryDates();

        initInputVerification();
    }

    public void initDeliveryDates() {

        String[] deliveryDates = { "Idag", "Imorgon", "Lördag" };
        String[] deliveryTimes = { "8-10", "10-12", "12-14", "14-16", "16-18", "18-20" };

        deliveryDatesButtons = new ArrayList<DeliveryButtonController>();
        deliveryTimesButtons = new ArrayList<DeliveryButtonController>();

        for(int i = 0; i < deliveryDates.length; i++) {
            DeliveryButtonController db = new DeliveryButtonController(1, Utilities.getDeliveryWeekday(i+1), Utilities.getDeliveryDay(i+1), this);
            deliveryDatesButtons.add(db);
            this.deliveryDatesFlowPanel.getChildren().add(db);
        }

        for(String s : deliveryTimes) {
            DeliveryButtonController db = new DeliveryButtonController(2, s, this);
            deliveryTimesButtons.add(db);
            this.deliveryTimesFlowPanel.getChildren().add(db);
        }
    }

    private void initInputVerification() {
        Iterator iterator = inputFields.entrySet().iterator();
        while(iterator.hasNext()) {
            Map.Entry pair = (Map.Entry)iterator.next();
            TextField textField = (TextField)pair.getKey();
            Text warningField = (Text)pair.getValue();
            textField.focusedProperty().addListener(new ChangeListener<Boolean>() {
                @Override
                public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                    if (!newValue && textField.getText().length() == 0) {
                        textField.setStyle("-fx-border-color: red;");
                        warningField.setVisible(true);
                    } else {
                        textField.setStyle("-fx-border-color: none;");
                        warningField.setVisible(false);
                    }
                }
            });
        }

    }

    public void setAllDatesInactive() {
        for(DeliveryButtonController db : deliveryDatesButtons) {
            db.setInactive();
        }
    }

    public void setAllTimesInactive() {
        for(DeliveryButtonController db : deliveryTimesButtons) {
            db.setInactive();
        }
    }

    public void setChosenDeliveryDate(String chosenDeliveryDate) {
        verifyAddress();
        this.chosenDeliveryDate = chosenDeliveryDate;
    }

    public void setChosenDeliveryTime(String chosenDeliveryTime) {
        verifyAddress();
        this.chosenDeliveryTime = chosenDeliveryTime;
    }

    public void updateShoppingCart() {
        this.checkoutShoppingCartFlowPane.getChildren().clear();
        this.summaryShoppingCartFlowPane.getChildren().clear();

        ListIterator<ShoppingItem> li = handler.getShoppingCart().getItems().listIterator(handler.getShoppingCart().getItems().size());

        boolean stripe = false;
        while (li.hasPrevious()) {
            System.out.println("Läggertill");
            ShoppingItem shoppingItem = li.previous();

            SummaryShoppingCartItemController itemToAdd1 = new SummaryShoppingCartItemController(shoppingItem);
            CheckoutShoppingCartItemController itemToAdd2 = new CheckoutShoppingCartItemController(shoppingItem);

            if(stripe) {
                itemToAdd1.setStyle("-fx-background-color: #f6f6f6;");
                itemToAdd2.setStyle("-fx-background-color: #f6f6f6;");
            }
            stripe = !stripe;

            this.summaryShoppingCartFlowPane.getChildren().add(itemToAdd1);
            this.checkoutShoppingCartFlowPane.getChildren().add(itemToAdd2);
        }

        this.totalSumLabel.setText("Summa: " + Utilities.formatPrice(BackendHandler.getInstance().getShoppingCart().getTotal()) + "");

    }

    public void updateSummary() {

        Customer c = BackendHandler.getInstance().getCustomer();

        String summaryString = c.getFirstName() +
                " " + c.getLastName() + "\n" + c.getAddress() + "\n" + c.getPostCode() + " " +
                c.getPostAddress() + "\n\nTelefon: " + c.getPhoneNumber();
        summaryString += "\n\nBetalningsmetod: ";

        if(paymentMethod == 1) {
            summaryString += "Med betalkort";
        } else if(paymentMethod == 2) {
            summaryString += "Vid leverans";
        }

        summaryString += "\n\nLeverans " + this.chosenDeliveryDate.toLowerCase() + " kl. " + this.chosenDeliveryTime;

        this.summaryText.setText(summaryString);

    }

    @FXML
    public void nextCardNumber1() {
        if(cardNumber1.getText().length() == 4) {
            cardNumber2.requestFocus();
        }
    }

    @FXML
    public void nextCardNumber2() {
        if(cardNumber2.getText().length() == 4) {
            cardNumber3.requestFocus();
        }
    }

    @FXML
    public void nextCardNumber3() {
        if(cardNumber3.getText().length() == 4) {
            cardNumber4.requestFocus();
        }
    }

    @FXML
    public void nextCardNumber4() {
        if(cardNumber4.getText().length() == 4) {
            validMonth.requestFocus();
        }
    }

    @FXML
    public void nextValidMonth() {
        if(validMonth.getText().length() == 2) {
            validYear.requestFocus();
        }
    }

    @FXML
    public void nextValidYear() {
        if(validYear.getText().length() == 4) {
            verificationCode.requestFocus();
        }
    }

    @FXML
    public void nextVerificationCode() {
        if(verificationCode.getText().length() == 3) {
            holdersName.requestFocus();
        }
    }

    private void saveUser() {
        this.handler.getCustomer().setFirstName(this.firstName.getText());
        this.handler.getCustomer().setLastName(this.lastName.getText());
        this.handler.getCustomer().setPhoneNumber(this.phoneNumber.getText());
        this.handler.getCustomer().setEmail(this.email.getText());
        this.handler.getCustomer().setAddress(this.address.getText());
        this.handler.getCustomer().setPostCode(this.postCode.getText());
        this.handler.getCustomer().setPostAddress(this.postAddress.getText());
    }

    public void open() {
        this.checkoutGridPane.toFront();
        this.state = 0;
        activateNextButton();
        changeState();
        this.activityCircle.setCenterX(0);
        this.previousButton.setDisable(true);
        this.previousButton.setStyle("-fx-opacity: 0.5");
        this.payNow.setSelected(false);
        this.payWhenDelivered.setSelected(false);
        this.creditcardPayment.setVisible(false);

        this.firstName.setText(handler.getCustomer().getFirstName());
        this.lastName.setText(handler.getCustomer().getLastName());
        this.phoneNumber.setText(handler.getCustomer().getPhoneNumber());
        this.email.setText(handler.getCustomer().getEmail());
        this.address.setText(handler.getCustomer().getAddress());
        this.postCode.setText(handler.getCustomer().getPostCode());
        this.postAddress.setText(handler.getCustomer().getPostAddress());
    }

    @FXML
    public void close() {
        this.state = 0;
        caller.close();
    }

    @FXML public void payWhenDeliveredSelected() {
        paymentVerified = true;
        this.nextButton.setStyle("-fx-opacity: 1");
        this.nextButton.setDisable(false);
        this.creditcardPayment.setVisible(false);
        this.paymentMethod = 2;
    }

    @FXML public void payNowSelected() {
        paymentVerified = true;
        this.nextButton.setStyle("-fx-opacity: 1");
        this.nextButton.setDisable(false);
        this.creditcardPayment.setVisible(true);
        this.paymentMethod = 1;
    }

    @FXML public void nextButtonClicked() {

        this.previousButton.setDisable(false);
        if(state == 3) {
            this.checkoutFinishedPane.toFront();
            this.handler.placeOrder(true);
            caller.updatePreviouslyBoughtMenu();
            caller.goToStart();
        } else {
            state++;
            this.activityCircle.setCenterX(this.activityCircle.getCenterX() + 160);
            this.previousButton.setStyle("-fx-opacity: 1");
        }
        changeState();
    }

    @FXML public void previousButtonClicked() {
        state--;
        activateNextButton();
        if(state == 0) {
            this.activityCircle.setCenterX(this.activityCircle.getCenterX() - 160);
            this.previousButton.setStyle("-fx-opacity: 0.5");
            this.previousButton.setDisable(true);
        } else {
            this.activityCircle.setCenterX(this.activityCircle.getCenterX() - 160);
            this.previousButton.setDisable(false);
        }
        changeState();
    }

    private void changeState() {
        System.out.println(state);
        switch (state) {
            case 0:
                this.nextButton.setText("Leverans >");
                checkoutShoppingCart.toFront();
                break;
            case 1:
                verifyAddress();

                this.nextButton.setText("Betalning >");
                checkoutDeliveryTime.toFront();
                break;
            case 2:
                this.saveUser();
                if(!paymentVerified) {
                    this.nextButton.setStyle("-fx-opacity: 0.5");
                    this.nextButton.setDisable(true);
                }
                this.nextButton.setText("Bekräftelse >");
                checkoutPayment.toFront();
                break;
            case 3:
                updateSummary();
                this.lastPageSum.setText("Summa: " + Utilities.formatPrice(BackendHandler.getInstance().getShoppingCart().getTotal()));
                this.nextButton.setText("Slutför köp");
                checkoutSummary.toFront();
                break;
        }
    }

    @Override
    public void shoppingCartChanged(CartEvent cartEvent) {
        updateShoppingCart();
    }

    @FXML
    public void setDeliveryDay() {

    }

    @FXML public void verifyAddress() {
        System.out.println("firstName: " + this.firstName.getText());
        if(this.chosenDeliveryTime != null && this.chosenDeliveryTime != null &&
                this.firstName.getText().length() != 0 &&
                this.lastName.getText().length() != 0 &&
                this.address.getText().length() != 0 &&
                this.postCode.getText().length() != 0 &&
                this.postAddress.getText().length() != 0 &&
                this.email.getText().length() != 0 &&
                this.phoneNumber.getText().length() != 0) {
            activateNextButton();
            this.addressVerified = true;
        } else {
            inactivateNextButton();
            this.addressVerified = false;
        }
    }

    private void activateNextButton() {
        this.nextButton.setStyle("-fx-opacity: 1.0");
        this.nextButton.setDisable(false);
    }

    private void inactivateNextButton() {
        this.nextButton.setStyle("-fx-opacity: 0.5");
        this.nextButton.setDisable(true);
    }

}
