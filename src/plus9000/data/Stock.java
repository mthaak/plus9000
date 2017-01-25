package plus9000.data;

import java.io.File;

/**
 * Created by Martin on 20-Jan-17.
 */
public class Stock {
    public String exchange;
    public String symbol;
    public String name;
    public String industry;
    public String sector;
    public String cap;
    public String last;
    public String change;
    public String changePer;
    public String volume;
    public boolean hasDay;
    public boolean hasTick;

    public Stock() {
    }

    public Stock(String exchange, String symbol, String name, String industry, String sector, String cap, String last,
                 String change, String changePer, String volume) {
        this.exchange = exchange;
        this.symbol = symbol;
        this.name = name;
        this.industry = industry;
        this.sector = sector;
        this.cap = cap;
        this.last = last;
        this.change = change;
        this.changePer = changePer;
        this.volume = volume;
    }

    public Stock(Stock stock) {
        this.exchange = stock.exchange;
        this.symbol = stock.symbol;
        this.name = stock.name;
        this.industry = stock.industry;
        this.sector = stock.sector;
        this.cap = stock.cap;
        this.last = stock.last;
        this.change = stock.change;
        this.changePer = stock.changePer;
        this.volume = stock.volume;
        this.hasDay = stock.hasDay;
        this.hasTick = stock.hasTick;
    }

    public void setDataAvailable() {
        this.hasDay = new File(String.format("data/day/%s_%s.csv", this.exchange, this.symbol)).isFile();
        this.hasTick = new File(String.format("data/tick/%s_%s.csv", this.exchange, this.symbol)).isFile();
    }

    public String toString() {
        return String.format("%s (%s)", this.name, this.symbol);
    }

    public String getFullSymbol() {
        return String.format("%s:%s", this.exchange, this.symbol);
    }
}