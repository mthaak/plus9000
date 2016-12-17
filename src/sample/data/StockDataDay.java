package sample.data;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Martin on 17-12-2016.
 */
public class StockDataDay {
    /* date -> open price, high price, low price, close price */
    private Map<Date, double[]> dayPrices;
    /* date -> volume */
    private Map<Date, Integer> dayVolume;

    public StockDataDay() {
        dayPrices = new HashMap<>();
        dayVolume = new HashMap<>();
    }

    public void readFromFile(String filePath) {
        File file = new File(filePath);
        try {
            Scanner scanner = new Scanner(file);
            scanner.useDelimiter(",|\\n");
            scanner.useLocale(Locale.ENGLISH);

            scanner.nextLine(); // skip header

            DateFormat df = new SimpleDateFormat("dd-MMM-yy", Locale.ENGLISH);
            while (scanner.hasNext()) {
                Date day = df.parse(scanner.next());

                double prices[] = {scanner.nextDouble(), scanner.nextDouble(),
                        scanner.nextDouble(), scanner.nextDouble()};
                dayPrices.put(day, prices);

                int volume = scanner.nextInt();
                dayVolume.put(day, volume);
            }
            scanner.close();

        } catch (FileNotFoundException | ParseException e) {
            e.printStackTrace();
        }
    }

    public double getOpenPrice(Date date) {
        return dayPrices.get(date)[0];
    }

    public double getHighPrice(Date date) {
        return dayPrices.get(date)[1];
    }

    public double getLowPrice(Date date) {
        return dayPrices.get(date)[2];
    }

    public double getClosePrice(Date date) {
        return dayPrices.get(date)[3];
    }

    public double getVolume(Date date) {
        return dayPrices.get(date)[4];
    }
}
