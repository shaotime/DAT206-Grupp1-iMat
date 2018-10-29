package iMat;

import javafx.scene.control.Button;
import se.chalmers.ait.dat215.project.IMatDataHandler;
import se.chalmers.ait.dat215.project.Order;
import se.chalmers.ait.dat215.project.Product;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

import javafx.scene.control.TextField;


public class Utilities {

    public static String formatPrice(double price) {

        String formattedString;
        BigDecimal bigPrice = new BigDecimal(price);
        bigPrice = bigPrice.setScale(2, BigDecimal.ROUND_HALF_UP);
        price = bigPrice.doubleValue();

        if (price == Math.round(price)) {
            formattedString = (int) price + ":-";
        } else {
            String ss = price + "";
            formattedString = ss.replace(".", ":");
            if (formattedString.charAt(formattedString.length()-2) == ':') {
                formattedString += "0";
            }
        }
        return formattedString;
    }

    public static String formatPreviousBoughtDate(Order order) {
        String[] weekdays = { "", "Söndag", "Måndag", "Tisdag", "Onsdag", "Torsdag", "Fredag", "Lördag" };
        String[] months = { "januari", "februari", "mars", "april", "maj", "juni", "juli", "augusti", "september", "oktober", "november", "decemebr" };

        String dateString = "";
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(order.getDate());
        dateString += weekdays[calendar.get(Calendar.DAY_OF_WEEK)];
        dateString += " " + calendar.get(Calendar.DATE);
        dateString += " " + months[calendar.get(Calendar.MONTH)];
        dateString += " " + calendar.get(Calendar.YEAR);

        return dateString;
    }

    public static String getDeliveryDay(int offset) {
        String[] weekdays = { "", "söndag", "måndag", "tisdag", "onsdag", "torsdag", "fredag", "lördag" };
        String[] months = { "januari", "februari", "mars", "april", "maj", "juni", "juli", "augusti", "september", "oktober", "november", "decemebr" };

        Date date = new Date();

        String dateString = "";
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, offset);

        dateString += "\n" + calendar.get(Calendar.DATE);
        dateString += " " + months[calendar.get(Calendar.MONTH)];

        return dateString;
    }

    public static String getDeliveryWeekday(int offset) {
        String[] weekdays = { "", "Söndag", "Måndag", "Tisdag", "Onsdag", "Torsdag", "Fredag", "Lördag" };

        Date date = new Date();

        String dateString = "";
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, offset);

        dateString += weekdays[calendar.get(Calendar.DAY_OF_WEEK)];

        return dateString;
    }

    //check if positive numeric input value
    public static boolean isQuantityInputValid(String tf) {
        if (tf.length() < 1) {
            return false;
        }
        try {
            int amount = Integer.parseInt(tf);
            if (amount < 1){
                return false;
            }
        }catch(NumberFormatException e){
            return false;
        }
        return true;
    }

    public static void setDisableButton(Button button) {
        button.setDisable(true);
        button.setStyle("-fx-opacity: 0.5");
    }

    public static void setEnableButton(Button button) {
        button.setDisable(false);
        button.setStyle("-fx-opacity: 1");
    }
}


