package iMat;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class ProfileController extends AnchorPane {
    private BackendHandler handler = BackendHandler.getInstance();

    @FXML TextField firstName;
    @FXML TextField lastName;
    @FXML TextField phoneNumber;
    @FXML TextField email;
    @FXML TextField address;
    @FXML TextField postCode;
    @FXML TextField postAddress;
    Overlay caller;

    public ProfileController(Overlay caller) {
        this.caller = caller;

        FXMLLoader fxmlLoader = new FXMLLoader(
                getClass().getResource("fxml/profileScene.fxml"));

        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

    }

    @FXML
    public void close() {
        caller.close();
    }

    @FXML
    public void save() {
        this.handler.getCustomer().setFirstName(this.firstName.getText());
        this.handler.getCustomer().setLastName(this.lastName.getText());
        this.handler.getCustomer().setPhoneNumber(this.phoneNumber.getText());
        this.handler.getCustomer().setEmail(this.email.getText());
        this.handler.getCustomer().setAddress(this.address.getText());
        this.handler.getCustomer().setPostCode(this.postCode.getText());
        this.handler.getCustomer().setPostAddress(this.postAddress.getText());



        close();
    }

    public void open() {
        this.firstName.setText(handler.getCustomer().getFirstName());
        this.lastName.setText(handler.getCustomer().getLastName());
        this.phoneNumber.setText(handler.getCustomer().getPhoneNumber());
        this.email.setText(handler.getCustomer().getEmail());
        this.address.setText(handler.getCustomer().getAddress());
        this.postCode.setText(handler.getCustomer().getPostCode());
        this.postAddress.setText(handler.getCustomer().getPostAddress());
    }

}
