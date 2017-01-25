package plus9000.data;


import java.io.File;
import java.io.FileNotFoundException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * Created by Martin on 17-12-2016.
 */
public class StockDataPerTick {
    private OrderedHashMap<Date, Double> stockDataPerTick;

    public StockDataPerTick() {
        this.stockDataPerTick = new OrderedHashMap<>();
    }

    public static StockDataPerTick loadedFromFile(String symbol) {
        StockDataPerTick stockDataPerTick = new StockDataPerTick();
        boolean success = stockDataPerTick.loadFromFile(symbol);
        if (success)
            return stockDataPerTick;
        else
            return null;
    }

    public boolean loadFromFile(String symbol) {
        try {
            File file = new File(String.format("data/tick/%s.csv", symbol.replace(':', '_')));
            Scanner fileScanner = new Scanner(file);

            fileScanner.nextLine(); // skip header

            DateFormat df = new SimpleDateFormat("kk:mm:ss", Locale.ENGLISH);

            Scanner lineScanner = null;
            while (fileScanner.hasNextLine()) {
                try {
                    lineScanner = new Scanner(fileScanner.nextLine());
                    lineScanner.useDelimiter(",");
                    lineScanner.useLocale(Locale.ENGLISH);
                    lineScanner.next(); // ignore ticker
                    lineScanner.next(); // ignore per
                    lineScanner.next(); // ignore date
                    Date time = df.parse(lineScanner.next());
                    double price = lineScanner.nextDouble();
                    lineScanner.next(); // ignore volume
                    this.stockDataPerTick.put(time, price); // if there is already a price for time, then it is updated

                } catch (NoSuchElementException | ParseException e) {
                    // ignore line
                } finally {
                    if (lineScanner != null) lineScanner.close();
                }
            }
            fileScanner.close();
            return true;
        } catch (FileNotFoundException e) {
            return false;
        }
    }

    public double getPrice(Date time) {
        return stockDataPerTick.get(time);
    }

    public double getPrice(int i) {
        return stockDataPerTick.getValue(i);
    }

    public Date getTime(int i) {
        return stockDataPerTick.getEntry(i).getKey();
    }

    public int getNumTicks() {
        return stockDataPerTick.size();
    }
}
