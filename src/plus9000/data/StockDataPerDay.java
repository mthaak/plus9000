package plus9000.data;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.InputMismatchException;
import java.util.Locale;
import java.util.Scanner;

/**
 * Created by Martin on 17-12-2016.
 */
public class StockDataPerDay {
    /* date -> open price, high price, low price, close price, volume */
    private OrderedHashMap<Date, DataOfDay> stockDataPerDay;

    public StockDataPerDay() {
        this.stockDataPerDay = new OrderedHashMap<>();
    }

    public void loadFromFile(String filePath) {
        File file = new File(filePath);
        try {
            Scanner scanner = new Scanner(file);
            scanner.useDelimiter(",|\\n");
            scanner.useLocale(Locale.ENGLISH);

            scanner.nextLine(); // skip header

            DateFormat df = new SimpleDateFormat("dd-MMM-yy", Locale.ENGLISH);
            while (scanner.hasNext()) {
                try {
                    Date day = df.parse(scanner.next());
                    DataOfDay dataOfDay = new DataOfDay(scanner.nextDouble(), scanner.nextDouble(),
                            scanner.nextDouble(), scanner.nextDouble(), scanner.nextInt());
                    this.stockDataPerDay.put(day, dataOfDay);
                } catch (ParseException | InputMismatchException e) {
                    // ignore data
                }
            }
            scanner.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public int numDays() {
        return stockDataPerDay.size();
    }

    public Date getDate(int i) {
        return stockDataPerDay.getEntry(i).getKey();
    }

    public double getOpenPrice(int i) {
        return stockDataPerDay.getValue(i).openPrice;
    }

    public double getOpenPrice(Date date) {
        return stockDataPerDay.get(date).openPrice;
    }

    public double getHighPrice(int i) {
        return stockDataPerDay.getValue(i).highPrice;
    }

    public double getHighPrice(Date date) {
        return stockDataPerDay.get(date).highPrice;
    }

    public double getLowPrice(int i) {
        return stockDataPerDay.getValue(i).lowPrice;
    }

    public double getLowPrice(Date date) {
        return stockDataPerDay.get(date).lowPrice;
    }

    public double getClosePrice(int i) {
        return stockDataPerDay.getValue(i).closePrice;
    }

    public double getClosePrice(Date date) {
        return stockDataPerDay.get(date).closePrice;
    }

    public double getVolume(int i) {
        return stockDataPerDay.getValue(i).volume;
    }

    public double getVolume(Date date) {
        return stockDataPerDay.get(date).volume;
    }
}

class DataOfDay {
    public double openPrice, highPrice, lowPrice, closePrice;
    public int volume;
    public DataOfDay(double openPrice, double highPrice, double lowPrice, double closePrice, int volume) {
        this.openPrice = openPrice;
        this.highPrice = highPrice;
        this.lowPrice = lowPrice;
        this.closePrice = closePrice;
        this.volume = volume;
    }
}