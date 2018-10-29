package iMat;

import se.chalmers.ait.dat215.project.*;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class BackendHandler {
    private static BackendHandler instance;
    private IMatDataHandler handler;

    private BackendHandler() {
        this.handler = IMatDataHandler.getInstance().getInstance();
    }

    public static synchronized BackendHandler getInstance() {
        if(instance == null) {
            instance = new BackendHandler();
        }
        return instance;
    }

    public List<ProductCategory> getProductCategory(Category category) {

        List<ProductCategory> categoriesList = new ArrayList<ProductCategory>();

        switch (category) {
            case BRÖD:
                categoriesList.add(ProductCategory.BREAD);
                break;
            case FRUKT:
                categoriesList.add(ProductCategory.BERRY);
                categoriesList.add(ProductCategory.FRUIT);
                categoriesList.add(ProductCategory.CITRUS_FRUIT);
                categoriesList.add(ProductCategory.EXOTIC_FRUIT);
                categoriesList.add(ProductCategory.MELONS);
                break;
            case DRYCK:
                categoriesList.add(ProductCategory.COLD_DRINKS);
                break;
            case SKAFFERI:
                categoriesList.add(ProductCategory.FLOUR_SUGAR_SALT);
                categoriesList.add(ProductCategory.PASTA);
                categoriesList.add(ProductCategory.HERB);
                categoriesList.add(ProductCategory.POTATO_RICE);
                categoriesList.add(ProductCategory.NUTS_AND_SEEDS);
                categoriesList.add(ProductCategory.HOT_DRINKS);
                break;
            case GRÖNSAKER:
                categoriesList.add(ProductCategory.POD);
                categoriesList.add(ProductCategory.VEGETABLE_FRUIT);
                categoriesList.add(ProductCategory.ROOT_VEGETABLE);
                categoriesList.add(ProductCategory.CABBAGE);
                break;
            case MEJERI:
                categoriesList.add(ProductCategory.DAIRIES);
                break;
            case GODIS:
                categoriesList.add(ProductCategory.SWEET);
                break;
            case KÖTT:
                categoriesList.add(ProductCategory.MEAT);
                categoriesList.add(ProductCategory.FISH);
                break;
        }

        return categoriesList;
    }

    public void shutDown() {
        handler.shutDown();
    }

    public boolean isFirstRun() {
        return this.handler.isFirstRun();
    }

    public void resetFirstRun() {
        this.handler.resetFirstRun();
    }

    public boolean isCustomerComplete() {
        return this.handler.isCustomerComplete();
    }

    public void reset() {
        this.handler.reset();
    }

    public Customer getCustomer() {
        return this.handler.getCustomer();
    }

    public User getUser() {
        return this.handler.getUser();
    }

    public CreditCard getCreditCard() {
        return this.handler.getCreditCard();
    }

    public ShoppingCart getShoppingCart() {
        return this.handler.getShoppingCart();
    }

    public Order placeOrder() {
        return this.handler.placeOrder();
    }

    public Order placeOrder(boolean clearShoppingCart) {
        return this.handler.placeOrder(clearShoppingCart);
    }

    public List<Order> getOrders() {
        return this.handler.getOrders();
    }

    public Product getProduct(int idNbr) {
        return this.handler.getProduct(idNbr);
    }

    public List<Product> getProducts() {
        return this.handler.getProducts();
    }

    public List<Product> getProducts(ProductCategory pc) {
        return this.handler.getProducts(pc);
    }

    public List<Product> findProducts(String s) {
        return this.handler.findProducts(s);
    }

    public void addProduct(Product p) {
        this.handler.addProduct(p);
    }

    public void removeProduct(Product p) {
        this.handler.removeProduct(p);
    }

    public void addFavorite(Product p) {
        this.handler.addFavorite(p);
    }

    public void addFavorite(int idNbr) {
        this.handler.addFavorite(idNbr);
    }

    public void removeFavorite(Product p) {
        this.handler.removeFavorite(p);
    }

    public boolean isFavorite(Product p) {
        return this.handler.isFavorite(p);
    }

    public List<Product> favorites() {
        return this.handler.favorites();
    }

    public boolean hasImage(Product p) {
        return this.handler.hasImage(p);
    }

    public ImageIcon getImageIcon(Product p) {
        return this.handler.getImageIcon(p);
    }

    public ImageIcon getImageIcon(Product p, Dimension d) {
        return this.handler.getImageIcon(p, d);
    }

    public ImageIcon getImageIcon(Product p, int width, int height) {
        return this.handler.getImageIcon(p, width, height);
    }

    public javafx.scene.image.Image getFXImage(Product p) {
        return this.handler.getFXImage(p);
    }

    public javafx.scene.image.Image getFXImage(Product p, double width, double height) {
        return this.handler.getFXImage(p, width, height);
    }

    public javafx.scene.image.Image getFXImage(Product p, double requestedWidth, double requestedHeight, boolean preserveRatio) {
        return this.handler.getFXImage(p, requestedWidth, requestedHeight, preserveRatio);
    }

    public void setProductImage(Product p, File f) {
        this.handler.setProductImage(p, f);
    }

    public String imatDirectory() {
        return this.handler.imatDirectory();
    }

}
