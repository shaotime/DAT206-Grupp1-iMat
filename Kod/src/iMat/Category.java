package iMat;

public enum Category {
    FAVORITER,
    MEST_KOPT,
    TIDIGARE_KÖP,
    MEJERI,
    BRÖD,
    KÖTT,
    SKAFFERI,
    GRÖNSAKER,
    DRYCK,
    GODIS,
    FRUKT;

    public static String getColour(Category category) {
        switch (category) {
            case FAVORITER:
                return "#FF8081";
            case MEST_KOPT:
                return "#95bf81";
            case TIDIGARE_KÖP:
                return "#EADED2";
            case MEJERI:
                return "#B6D4DD";
            case BRÖD:
                return "#CCC693";
            case KÖTT:
                return "#E5ABAB";
            case SKAFFERI:
                return "#F1EEA2";
            case GRÖNSAKER:
                return "#B6E2B9";
            case DRYCK:
                return "#E8CCF0";
            case GODIS:
                return "#CCF0ED";
            case FRUKT:
                return "#F3F6C6";
        }
        return null;
    }

    public static String getName(Category category) {
        switch (category) {
            case FAVORITER:
                return "Favoriter";
            case MEST_KOPT:
                return "Mest köpt";
            case TIDIGARE_KÖP:
                return "Tidigare köp";
            case MEJERI:
                return "Mejeri";
            case BRÖD:
                return "Bröd";
            case KÖTT:
                return "Kött";
            case SKAFFERI:
                return "Skafferi";
            case GRÖNSAKER:
                return "Grönsaker";
            case DRYCK:
                return "Dryck";
            case GODIS:
                return "Godis";
            case FRUKT:
                return "Frukt";
        }
        return "fel";

    }
}