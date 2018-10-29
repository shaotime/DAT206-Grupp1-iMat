package iMat;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class Main extends Application {


    @Override
    public void start(Stage stage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("fxml/mainScene.fxml"));

        Scene scene = new Scene(root, 1095, 667);

        stage.setTitle("iMat");
        stage.setScene(scene);
        stage.setMinWidth(880);
        stage.setMinHeight(667);
        EventController.getInstance().setWidth(1095);
        EventController.getInstance().setHeight(667);
        stage.show();
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                BackendHandler.getInstance().shutDown();
            }
        });

        scene.widthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                EventController.getInstance().setWidth(newValue.intValue());
            }
        });

        scene.heightProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                EventController.getInstance().setHeight(newValue.intValue());
            }
        });
    }



    public static void main(String[] args) {
        launch(args);
    }
}
