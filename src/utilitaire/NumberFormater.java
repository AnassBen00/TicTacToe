package utilitaire;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

public class NumberFormater {

    // Format big numbers into more readable ones
    public static String formatNumber(int number) {
        Locale locale  = new Locale("en", "US");
        String pattern = "###,###.###";
        DecimalFormat decimalFormat = (DecimalFormat) NumberFormat.getNumberInstance(locale);
        decimalFormat.applyPattern(pattern);
        return (decimalFormat.format(number));
    }
}
