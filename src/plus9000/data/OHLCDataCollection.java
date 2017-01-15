package plus9000.data;

import org.jfree.data.time.ohlc.OHLCSeries;
import org.jfree.data.time.ohlc.OHLCSeriesCollection;
import plus9000.util.Period;

import java.util.*;

/**
 * Created by Martin on 12-Jan-17.
 */
public class OHLCDataCollection extends OHLCSeriesCollection {
    private Period period;
    private List<String> allSymbols;
    private List<String> includedSymbols;
    private Map<String, OHLCSeries> allSeriesPerDay;
    private Map<String, OHLCSeries> allSeriesPerWeek;
    private Map<String, OHLCSeries> allSeriesPerMonth;
    private Map<String, OHLCSeries> allSeriesPerYear;

    public OHLCDataCollection() {
        this.period = Period.DAY; // default period is day
        this.allSymbols = new ArrayList<>();
        this.includedSymbols = new ArrayList<>();
        this.allSeriesPerDay = new HashMap<>();
        this.allSeriesPerWeek = new HashMap<>();
        this.allSeriesPerMonth = new HashMap<>();
        this.allSeriesPerYear = new HashMap<>();
    }

    public static OHLCDataCollection loadedFromFiles() {
        OHLCDataCollection ohlcDataCollection = new OHLCDataCollection();
        ohlcDataCollection.loadFromFiles(new String[]{"aapl", "amzn", "bac", "fb", "ge", "googl", "intc", "jnj", "msft", "xom"});
        ohlcDataCollection.include("aapl"); // include Apple by default
        return ohlcDataCollection;
    }

    public void loadFromFiles(String[] symbols) {
        this.allSymbols.addAll(Arrays.asList(symbols));
        for (String symbol : symbols) {
            OHLCData ohlcData = OHLCData.loadedFromFile(symbol);
            this.allSeriesPerDay.put(symbol, ohlcData.getPerDay());
            this.allSeriesPerWeek.put(symbol, ohlcData.getPerWeek());
            this.allSeriesPerMonth.put(symbol, ohlcData.getPerMonth());
            this.allSeriesPerYear.put(symbol, ohlcData.getPerYear());
        }
    }

    public Period getPeriod() {
        return this.period;
    }

    public void setPeriod(Period period) {
        if (this.period != period) {
            this.period = period;

            this.removeAllSeries();
            this.includedSymbols.forEach(this::addSeriesOfSymbol);
        }
    }

    public void include(String symbol) {
        if (!this.includedSymbols.contains(symbol) && this.allSymbols.contains(symbol)) {
            this.includedSymbols.add(symbol);

            this.addSeriesOfSymbol(symbol);
        }
    }

    public void exclude(String symbol) {
        if (this.includedSymbols.contains(symbol)) {
            this.includedSymbols.remove(symbol);

            int index = this.indexOf(symbol);
            if (index != -1) this.removeSeries(index);
        }
    }

    public void includeAll() {
        this.allSymbols.forEach(this::include);
    }

    public void excludeAll() {
        this.includedSymbols.clear();
        this.removeAllSeries();
    }

    private void addSeriesOfSymbol(String symbol) {
        if (this.period == Period.DAY) {
            this.addSeries(this.allSeriesPerDay.get(symbol));
        } else if (this.period == Period.WEEK) {
            this.addSeries(this.allSeriesPerWeek.get(symbol));
        } else if (this.period == Period.MONTH) {
            this.addSeries(this.allSeriesPerMonth.get(symbol));
        } else if (this.period == Period.YEAR) {
            this.addSeries(this.allSeriesPerYear.get(symbol));
        }
    }
}
