package iMat;

import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.MenuItem;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.*;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.*;
import javafx.scene.text.Font;
import javafx.util.Duration;
import se.chalmers.ait.dat215.project.*;
import sun.management.Util;
import sun.plugin.javascript.navig.Anchor;

import java.awt.*;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.*;
import java.util.List;

public class MainController implements Initializable, ShoppingCartListener, Overlay {

    private final static int POPOVER_X_LAYOUT = 140;
    private final static int POPOVER_Y_LAYOUT = 80;

    private AnchorPane header;

    private BackendHandler handler;
    FlowPane shoppingCartFlowPane;
    @FXML private AnchorPane categoriesPane;
    @FXML private Line categoriesPanelLine;
    @FXML private AnchorPane productsGridPane;
    @FXML private ScrollPane productsPane;
    @FXML private ScrollPane shoppingCart;
    @FXML private FlowPane productFlowPane;
    @FXML private Label sum;
    @FXML private Button checkoutButton;

    @FXML
    private AnchorPane firstRunWelcomePane;
    @FXML
    private GridPane mainGridPane;
    @FXML
    private AnchorPane darkOverlay;
    @FXML
    private AnchorPane opaqueOverlay;
    @FXML
    private AnchorPane centerStageWrapper;
    @FXML
    private StackPane popOver;

    @FXML private TextField searchTextField;

    @FXML private AnchorPane startPane;

    @FXML private Menu menuCategories;
    @FXML private Menu menuPreviouslyBought;


    // pacman Mode
    @FXML private StackPane pacmanStackPane;
    @FXML private AnchorPane pacmanAnchorPane;
    @FXML private Pacman pacman;
    @FXML private ImageView pacmanHeader;
    private Label pointsLabel;
    private char direction = 'd';
    private javax.swing.Timer timer;
    private int nrOfCandy;
    private static final int NR_OF_CANDY = 20;

    private Category currentCategory;
    private List<CategoryMenuItemController> categoriesMenuItemsControllers;

    ProfileController profileView = new ProfileController(this);
    HelpController helpView = new HelpController(this);
    CheckoutController checkoutView = new CheckoutController(this);

    public MainController() {
        this.handler = BackendHandler.getInstance();
    }



    @Override
    public void initialize(URL url, ResourceBundle rb) {

        this.productFlowPane.setMinWidth(650);
//        showProducts(Category.FAVORITER);

        createCategories();

        goToStart();

        // Create shopping cart
        this.shoppingCartFlowPane = new FlowPane();
        this.shoppingCartFlowPane.setStyle("-fx-background: #fff");
        this.shoppingCart.setStyle("-fx-background: #fff");
        this.shoppingCartFlowPane.setPrefWrapLength(180);
        this.shoppingCart.setContent(shoppingCartFlowPane);
        reloadShoppingCart(null);

        // Add listener to shoppingcart
        this.handler.getShoppingCart().addShoppingCartListener(this);
        EventController.getInstance().setMainController(this);

        this.productFlowPane.setStyle("-fx-background-color: #fff;");


    }

    public void setNewWidth(int width) {
        this.productFlowPane.setPrefWrapLength(width);
        this.productFlowPane.setMinWidth(width);
        this.productFlowPane.setMaxWidth(width);

//        this.product.setPrefWrapLength(width);
        this.productFlowPane.setMinWidth(width);
        this.productFlowPane.setMaxWidth(width);

        this.checkoutView.setLayoutX(EventController.getInstance().getWidth() / 2 - 400);
        this.checkoutView.setLayoutY(EventController.getInstance().getHeight() / 2 - 250);
        this.profileView.setLayoutX(EventController.getInstance().getWidth() / 2 - 400);
        this.profileView.setLayoutY(EventController.getInstance().getHeight() / 2 - 250);
        this.helpView.setLayoutX(EventController.getInstance().getWidth() / 2 - 400);
        this.helpView.setLayoutY(EventController.getInstance().getHeight() / 2 - 250);
    }

    @FXML
    public void goToStart() {
        setActiveCategoryButton(null);
        this.startPane.toFront();
    }

    private void createCategories() {
//        this.categoriesPanelLine.setVisible(false);
        this.currentCategory = Category.FAVORITER;
        this.categoriesMenuItemsControllers = new ArrayList<CategoryMenuItemController>();

        FlowPane categoryFlowPane = new FlowPane();
        categoryFlowPane.setPrefWrapLength(205);
        this.categoriesPane.getChildren().add(categoryFlowPane);
        for (Category c : Category.values()) {
            CategoryMenuItemController cc = new CategoryMenuItemController(c);
            this.categoriesMenuItemsControllers.add(cc);
            cc.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    CategoryMenuItemController chosenCategory = (CategoryMenuItemController) event.getSource();
                    showProducts(chosenCategory.getCategory());
                    setActiveCategoryButton(chosenCategory);
                }
            });

            categoryFlowPane.getChildren().add(cc);

            MenuItem menuItem = new MenuItem(Category.getName(c));

            menuItem.setOnAction(new EventHandler() {
                public void handle(Event e) {

                    showProducts(c);
                }
            });

            this.menuCategories.getItems().add(menuItem);

     }

        updatePreviouslyBoughtMenu();
        setActiveCategoryButton(categoriesMenuItemsControllers.get(0));
    }

    public void updatePreviouslyBoughtMenu() {
        this.menuPreviouslyBought.getItems().clear();

        ListIterator<Order> li = handler.getOrders().listIterator(handler.getOrders().size());


        int i = 0;

        while (li.hasPrevious() && i <= 10) {

            i++;

            Order order = li.previous();

            MenuItem menuItem = new MenuItem(Utilities.formatPreviousBoughtDate(order) + " - " + order.getItems().size() + " artiklar");

            List<Product> productList = new ArrayList<Product>();
            for (ShoppingItem si : order.getItems()) {
                productList.add(si.getProduct());
            }
            showProducts(productList, order);

            menuItem.setOnAction(new EventHandler() {
                public void handle(Event e) {

                    showProducts(productList, order);
                }
            });

            this.menuPreviouslyBought.getItems().add(menuItem);

            if(i == 10) {
                menuItem = new MenuItem("Visa alla ...");
                menuItem.setOnAction(new EventHandler() {
                    public void handle(Event e) {

                        showPreviouslyBought();
                    }
                });

                this.menuPreviouslyBought.getItems().add(menuItem);
            }

        }


        if(i == 0) {
            MenuItem menuItem = new MenuItem("Inga tidigare köp...");
            menuItem.setDisable(true);

            this.menuPreviouslyBought.getItems().add(menuItem);
        }

    }

    private void setActiveCategoryButton(CategoryMenuItemController category) {
        for (CategoryMenuItemController c : this.categoriesMenuItemsControllers) {
            if (category == null || !c.getCategory().equals(category.getCategory())) {
                c.setActive(false);
            } else {
                c.setActive(true);
            }
        }
    }

    @Override
    public void shoppingCartChanged(CartEvent cartEvent) {
        reloadShoppingCart(cartEvent.getShoppingItem());
    }

    private void reloadShoppingCart(ShoppingItem addedItem) {
        double sum = 0;
        this.shoppingCartFlowPane.getChildren().clear();

        if(handler.getShoppingCart().getItems().size() == 0) {
            Utilities.setDisableButton(this.checkoutButton);
        } else {
            Utilities.setEnableButton(this.checkoutButton);
        }

        ListIterator<ShoppingItem> li = handler.getShoppingCart().getItems().listIterator(handler.getShoppingCart().getItems().size());

        while (li.hasPrevious()) {
            ShoppingItem shoppingItem = li.previous();
            sum += shoppingItem.getTotal();
            ShoppingCartItemController itemToAdd = new ShoppingCartItemController(shoppingItem);
            this.shoppingCartFlowPane.getChildren().add(itemToAdd);
            if (addedItem != null && shoppingItem.equals(addedItem)) {
                itemToAdd.setUpdated();
            }
        }
        this.sum.setText(Utilities.formatPrice(sum) + "");
    }

    public void searchButtonPressed() {
        this.centerStageWrapper.setStyle("-fx-background-color: #404040");
        for (CategoryMenuItemController c : this.categoriesMenuItemsControllers) {
            c.setActive(false);

        }
        List<Product> results = this.handler.findProducts(this.searchTextField.getText());
        showProducts(results, null);
    }

    private void showProducts(Category category) {
        this.centerStageWrapper.toFront();


        if (category.equals(Category.TIDIGARE_KÖP)) {
            showPreviouslyBought();
            productsPane.setPadding(new Insets(0, 0, 0, 0));
        } else {

            this.searchTextField.clear();
            this.categoriesPane.requestFocus();
            List<Product> foodList = new ArrayList<Product>();

            if (category.equals(Category.FAVORITER)) {
                foodList.addAll(this.handler.favorites());
                Collections.sort(foodList, new ProductComparator());
            } else if (category.equals(Category.MEST_KOPT)) {
                foodList.addAll(getMostBought());
            } else {
                List<ProductCategory> productCategories = this.handler.getProductCategory(category);
                for (ProductCategory pc : productCategories) {
                    foodList.addAll(this.handler.getProducts(pc));
                }
                Collections.sort(foodList, new ProductComparator());
            }


            this.showProducts(foodList, null);

        }

        //change border colours depending on chosen category
        String colour = Category.getColour(category);
//        categoriesPanelLine.setStyle("-fx-stroke: " + colour + " ;");
        this.centerStageWrapper.setStyle("-fx-background-color: " + colour + ";");

        productsPane.toFront();
    }

    @FXML public void clearShoppingCart() {
        handler.getShoppingCart().clear();
    }

    private List<Product> getMostBought() {
        HashMap<Product, Integer> productsMap = new HashMap<Product, Integer>();

        for (Product product : handler.getProducts()) {
            productsMap.put(product, 0);
        }

        for (Order order : handler.getOrders()) {
            for (ShoppingItem shoppingItem : order.getItems()) {
                Product product = shoppingItem.getProduct();
//                Integer amount = (int)shoppingItem.getAmount();
                Integer alreadyBought = productsMap.get(product);
                alreadyBought = alreadyBought + 1;
                productsMap.put(product, alreadyBought);
            }
        }

        List<Map.Entry<Product, Integer>> returnList = new ArrayList<Map.Entry<Product, Integer>>(productsMap.entrySet());

        Collections.sort(returnList, new Comparator() {
            public int compare(Object o1, Object o2) {
                return ((Comparable)((Map.Entry)(o2)).getValue()).compareTo(((Map.Entry)(o1)).getValue());
            }
        });


        List<Product> pList = new ArrayList<Product>();

        for (int i = 0; i < 21; i++) {
            Map.Entry<Product, Integer> mp = (Map.Entry<Product, Integer>) returnList.get(i);
            Product p = (Product) ((Map.Entry<Product, Integer>) mp).getKey();
            if(mp.getValue() > 0) {
                pList.add(p);
            }
        }

        return pList;
    }

    private void showProducts(List<Product> products, Order order) {

        this.productFlowPane.getChildren().clear();

        if (order != null) {
            header = new AnchorPane();
            header.setStyle("-fx-background-color: #efefef;");
            Label label = new Label();
            label.setText("Visar varor inköpta " + Utilities.formatPreviousBoughtDate(order));
            label.setStyle("-fx-font-size: 18; -fx-padding: 10px");
            header.getChildren().add(0, label);
            centerStageWrapper.getChildren().add(header);
            AnchorPane.setLeftAnchor(header, 11.0);
            AnchorPane.setRightAnchor(header, 16.0);
            AnchorPane.setTopAnchor(header, 11.0);
            productsPane.setPadding(new Insets(45, 0, 0, 0));
            this.centerStageWrapper.setStyle("-fx-background-color: " + Category.getColour(Category.TIDIGARE_KÖP));
            this.categoriesMenuItemsControllers.get(1).setActive(false);
        } else {
            productsPane.setPadding(new Insets(0, 0, 0, 0));
        }

        for (Product p : products) {
            productFlowPane.getChildren().add(new ProductController(p));
        }
    }

    private void showPreviouslyBought() {

        this.productFlowPane.getChildren().clear();
        ListIterator<Order> li = handler.getOrders().listIterator(handler.getOrders().size());
        while (li.hasPrevious()) {
            Order order = li.previous();
            PreviouslyBoughtController previouslyBoughtController = new PreviouslyBoughtController(order);

            previouslyBoughtController.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    List<Product> productList = new ArrayList<Product>();
                    for (ShoppingItem si : order.getItems()) {
                        productList.add(si.getProduct());
                    }
                    showProducts(productList, order);
                }
            });

            productFlowPane.getChildren().add(previouslyBoughtController);
        }
    }

    @FXML
    public void openProfile() {

        this.profileView.open();

        this.opaqueOverlay.getChildren().clear();
        this.opaqueOverlay.getChildren().add(this.profileView);

        this.popOver.toFront();

        this.profileView.setLayoutX(EventController.getInstance().getWidth() / 2 - 400);
        this.profileView.setLayoutY(EventController.getInstance().getHeight() / 2 - 250);
        this.popOver.setVisible(false);

        FadeTransition fader = new FadeTransition(Duration.millis(200));

        fader.setNode(this.popOver);

        fader.setFromValue(0.0);
        fader.setToValue(1.0);
        fader.setCycleCount(1);
        fader.setAutoReverse(false);

        fader.playFromStart();
        this.popOver.setVisible(true);
    }

    @FXML
    public void openAbout() {

        this.helpView.open();

        this.opaqueOverlay.getChildren().clear();
        this.opaqueOverlay.getChildren().add(this.helpView);

        this.popOver.toFront();

        this.helpView.setLayoutX(EventController.getInstance().getWidth() / 2 - 400);
        this.helpView.setLayoutY(EventController.getInstance().getHeight() / 2 - 250);
        this.popOver.setVisible(false);

        FadeTransition fader = new FadeTransition(Duration.millis(200));

        fader.setNode(this.popOver);

        fader.setFromValue(0.0);
        fader.setToValue(1.0);
        fader.setCycleCount(1);
        fader.setAutoReverse(false);

        fader.playFromStart();
        this.popOver.setVisible(true);
    }

    public void close() {

        FadeTransition fader = new FadeTransition(Duration.millis(200));

        fader.setNode(this.popOver);

        fader.setFromValue(1.0);
        fader.setToValue(0.0);
        fader.setCycleCount(1);
        fader.setAutoReverse(false);

        fader.playFromStart();


        fader.setNode(this.darkOverlay);
        fader.setOnFinished(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                mainGridPane.toFront();
            }
        });
    }

    @FXML
    public void checkout() {
        this.checkoutView.updateShoppingCart();
        this.opaqueOverlay.getChildren().clear();
        this.checkoutView.open();
        this.opaqueOverlay.getChildren().add(this.checkoutView);

        this.popOver.toFront();

        this.checkoutView.setLayoutX(EventController.getInstance().getWidth() / 2 - 400);
        this.checkoutView.setLayoutY(EventController.getInstance().getHeight() / 2 - 250);

        this.popOver.setVisible(false);

        FadeTransition fader = new FadeTransition(Duration.millis(250));

        fader.setNode(this.popOver);

        fader.setFromValue(0.0);
        fader.setToValue(1.0);
        fader.setCycleCount(1);
        fader.setAutoReverse(false);

        fader.playFromStart();
        this.popOver.setVisible(true);
    }

    @FXML public void clearAll() {
        handler.favorites().clear();
        handler.getOrders().clear();
        handler.getShoppingCart().clear();
        handler.reset();
        handler.resetFirstRun();
        goToStart();
    }

    @FXML
    public void activatePacmanMode() {// Pacman
        this.pacman = new Pacman();
        timer = new javax.swing.Timer(10, new ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                if (nrOfCandy == 0) {
                    if (direction == 'd') pacman.getPacmanImage().setRotate(270);
                    if (direction == 'a') pacman.getPacmanImage().setRotate(90);
                    if (direction == 's') pacman.getPacmanImage().setRotate(180);
                    timer.stop();
                    win();
                }

                for (Object candy : pacmanAnchorPane.getChildren()) {
                    if (candy instanceof Candy && ((Candy) candy).isVisible()) {
                        if (((Candy) candy).getBoundsInParent().intersects(pacman.getBoundsInParent())) {
                            ((Candy) candy).setVisible(false);
                            nrOfCandy--;
                            Toolkit.getDefaultToolkit().beep();
                            pointsLabel.setText("9");
                        }
                    }
                }

                switch (direction) {
                    case 'w':
                        pacman.setRotate(0);
                        if (pacman.getLayoutY() > 135) {
                            pacman.setLayoutY(pacman.getLayoutY() - 5);
                        }
                        break;
                    case 'a':
                        pacman.setRotate(270);
                        if (pacman.getLayoutX() > 0) {
                            pacman.setLayoutX(pacman.getLayoutX() - 5);
                        }
                        break;
                    case 's':
                        pacman.setRotate(180);
                        if (pacman.getLayoutY() < 600) {
                            pacman.setLayoutY(pacman.getLayoutY() + 5);
                        }
                        break;
                    case 'd':
                        pacman.setRotate(90);
                        if (pacman.getLayoutX() < 1000) {
                            pacman.setLayoutX(pacman.getLayoutX() + 5);
                        }
                        break;
                }
                if (direction == 'q') {
                    timer.stop();
                }
            }
        });
        this.pacmanStackPane.toFront();
        this.pacmanAnchorPane.requestFocus();
        this.pacmanAnchorPane.getChildren().clear();
        this.pacmanAnchorPane.getChildren().add(pacmanHeader);
        this.nrOfCandy = 0;
        Random r = new Random();
        double width = this.pacmanAnchorPane.getWidth();
        double height = this.pacmanAnchorPane.getHeight();
//        for (int i = 0; i < this.handler.getProducts().size()-1; i++) {
        for (int i = 0; i < NR_OF_CANDY; i++) {
            if (handler.getProduct(i) != null) {
                Candy candy = new Candy(handler.getProduct(i));
                candy.setLayoutX(r.nextInt((int) width) - 40);
                candy.setLayoutY(r.nextInt((int) height - 200) + 150);
                this.pacmanAnchorPane.getChildren().add(candy);
                this.nrOfCandy++;
            }
        }

        pointsLabel = new Label();
        pointsLabel.setFont(new Font("DS-Digital Bold", 96.0));
        pointsLabel.setTextFill(Color.WHITE);
        pointsLabel.setLayoutX(500);
        pointsLabel.setLayoutY(300);
//        this.pacmanAnchorPane.getChildren().add(pointsLabel);
        pointsLabel.toFront();
        this.pacman.setLayoutY(300);
        this.pacman.setLayoutX(30);
        pacman.setScaleX(1);
        pacman.setScaleY(1);
        pointsLabel.setText("0");
        this.pacmanAnchorPane.getChildren().add(this.pacman);
        timer.start();

    }

    @FXML
    public void keyPressed(KeyEvent keyEvent) {
        if (!timer.isRunning()) {
            timer.start();
        }
        switch (keyEvent.getText()) {
            case "w":
                this.direction = 'w';
                break;
            case "a":
                this.direction = 'a';
                break;
            case "s":
                this.direction = 's';
                break;
            case "d":
                this.direction = 'd';
                break;
            case "q":
                stopPacman();
                this.timer.stop();
                break;
        }

    }

    private void win() {
        timer = new javax.swing.Timer(16, new ActionListener() {
            int duration = 300;

            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                pacman.setScaleX(pacman.getScaleX() * 1.015);
                pacman.setScaleY(pacman.getScaleY() * 1.015);
                double rot = 0;
                if(duration > 100) {
                    rot = duration/100;
                } else {
                    rot = 0.5;
                }
                pacman.setRotate(pacman.getRotate() + rot);
                duration--;
                if (duration < 0) {
                    timer.stop();
                }
            }
        });
        timer.start();
    }

    private void stopPacman() {
        this.pacmanStackPane.toBack();
        this.direction='q';
    }

    @FXML public void exit() {
        System.exit(0);
    }

}
