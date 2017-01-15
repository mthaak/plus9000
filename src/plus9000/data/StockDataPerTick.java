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

    public void loadFromFile(String filePath) {
        File file = new File(filePath);
        try {
            Scanner scanner = new Scanner(file);
            scanner.useDelimiter(",|\\n");
            scanner.useLocale(Locale.ENGLISH);

            scanner.nextLine(); // skip header

            DateFormat df = new SimpleDateFormat("kk:mm:ss", Locale.ENGLISH);
            while (scanner.hasNextLine()) {
                try {
                    scanner.next(); // ignore ticker
                    scanner.next(); // ignore per
                    scanner.next(); // ignore date
                    Date time = df.parse(scanner.next());
                    double price = scanner.nextDouble();
                    scanner.next(); // ignore volume
                    this.stockDataPerTick.put(time, price); // if there is already a price for time, then it is updated
                } catch (NoSuchElementException | ParseException e) {
                    // ignore line
                }
            }
            scanner.close();

        } catch (FileNotFoundException e) {
            // ignore file
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
