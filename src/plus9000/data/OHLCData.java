package plus9000.data;

import org.jfree.data.time.Day;
import org.jfree.data.time.Month;
import org.jfree.data.time.Week;
import org.jfree.data.time.Year;
import org.jfree.data.time.ohlc.OHLCItem;
import org.jfree.data.time.ohlc.OHLCSeries;

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
 * Created by Martin on 12-Jan-17.
 */
public class OHLCData {
    private OHLCSeries perDay;
    private OHLCSeries perWeek;
    private OHLCSeries perMonth;
    private OHLCSeries perYear;

    public OHLCData(String symbol) {
        this.perDay = new OHLCSeries(symbol);
        this.perWeek = new OHLCSeries(symbol);
        this.perMonth = new OHLCSeries(symbol);
        this.perYear = new OHLCSeries(symbol);
    }

    public static OHLCData loadedFromFile(String symbol) {
        OHLCData stockData = new OHLCData(symbol);
        File file = new File(String.format("data/%s.csv", symbol));
        try {
            Scanner scanner = new Scanner(file);
            scanner.useDelimiter(",|\\r\\n");
            scanner.useLocale(Locale.ENGLISH);

            scanner.nextLine(); // skip header

            boolean firstLine = true;

            DateFormat df = new SimpleDateFormat("dd-MMM-yy", Locale.ENGLISH);

            Week week = new Week(new Date(0));
            double weekOpen = 0, weekHigh = Double.MIN_VALUE, weekLow = Double.MAX_VALUE, weekClose = 0;

            Month month = new Month(new Date(0));
            double monthOpen = 0, monthHigh = Double.MIN_VALUE, monthLow = Double.MAX_VALUE, monthClose = 0;

            Year year = new Year(new Date(0));
            double yearOpen = 0, yearHigh = Double.MIN_VALUE, yearLow = Double.MAX_VALUE, yearClose = 0;

            while (scanner.hasNext()) {
                try {
                    Date date = df.parse(scanner.next());
                    double open = scanner.nextDouble();
                    double high = scanner.nextDouble();
                    double low = scanner.nextDouble();
                    double close = scanner.nextDouble();
                    int volume = scanner.nextInt(); // not used at the moment

                    // Day
                    OHLCItem dayOhlc = new OHLCItem(new Day(date), open, high, low, close);
                    stockData.getPerDay().add(dayOhlc);

                    // Week
                    Week dateWeek = new Week(date);
                    if (dateWeek.equals(week)) {
                        weekHigh = Math.max(weekHigh, high);
                        weekLow = Math.min(weekLow, low);
                        weekClose = close;
                    } else {
                        if (!firstLine) {
                            OHLCItem weekOhlc = new OHLCItem(week, weekOpen, weekHigh, weekLow, weekClose);
                            stockData.getPerWeek().add(weekOhlc);
                        }
                        week = dateWeek;
                        weekOpen = open;
                        weekHigh = high;
                        weekLow = low;
                        weekClose = close;
                    }

                    // Month
                    Month dateMonth = new Month(date);
                    if (dateMonth.equals(month)) {
                        monthHigh = Math.max(monthHigh, high);
                        monthLow = Math.min(monthLow, low);
                        monthClose = close;
                    } else {
                        if (!firstLine) {
                            OHLCItem monthOhlc = new OHLCItem(month, monthOpen, monthHigh, monthLow, monthClose);
                            stockData.getPerMonth().add(monthOhlc);
                        }
                        month = dateMonth;
                        monthOpen = open;
                        monthHigh = high;
                        monthLow = low;
                        monthClose = close;
                    }

                    // Year
                    Year dateYear = new Year(date);
                    if (dateYear.equals(year)) {
                        yearHigh = Math.max(yearHigh, high);
                        yearLow = Math.min(yearLow, low);
                        yearClose = close;
                    } else {
                        if (!firstLine) {
                            OHLCItem yearOhlc = new OHLCItem(year, yearOpen, yearHigh, yearLow, yearClose);
                            stockData.getPerYear().add(yearOhlc);
                        }
                        year = dateYear;
                        yearOpen = open;
                        yearHigh = high;
                        yearLow = low;
                        yearClose = close;
                    }

                    firstLine = false;

                } catch (ParseException | InputMismatchException e) {
                    // ignore data
                }
            }

            // Add last week, month and year
            OHLCItem weekOhlc = new OHLCItem(week, weekOpen, weekHigh, weekLow, weekClose);
            stockData.getPerWeek().add(weekOhlc);
            OHLCItem monthOhlc = new OHLCItem(month, monthOpen, monthHigh, monthLow, monthClose);
            stockData.getPerMonth().add(monthOhlc);
            OHLCItem yearOhlc = new OHLCItem(year, yearOpen, yearHigh, yearLow, yearClose);
            stockData.getPerYear().add(yearOhlc);

            scanner.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return stockData;
    }

    public OHLCSeries getPerDay() {
        return this.perDay;
    }

    public OHLCSeries getPerWeek() {
        return this.perWeek;
    }

    public OHLCSeries getPerMonth() {
        return this.perMonth;
    }

    public OHLCSeries getPerYear() {
        return this.perYear;
    }
}
