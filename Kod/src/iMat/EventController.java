package iMat;

public class EventController {

    private static EventController instance;

    private int width;
    private int height;

    private MainController mainController;

    private EventController() {
    }

    public static synchronized EventController getInstance() {
        if(instance == null) {
            instance = new EventController();
        }
        return instance;
    }

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    public void setWidth(int width) {
        this.width = width;
        if(mainController != null) {
            mainController.setNewWidth(this.width - 450);
        }
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }


}
