package iMat;

import se.chalmers.ait.dat215.project.ShoppingItem;

import java.util.Comparator;

public class ShoppingItemComparator implements Comparator<ShoppingItem> {
    public int compare(ShoppingItem s1, ShoppingItem s2) {
        return s1.getProduct().getName().compareTo(s2.getProduct().getName());
    }
}
