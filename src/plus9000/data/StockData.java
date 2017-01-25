package plus9000.data;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Martin on 20-Jan-17.
 */
public class StockData {
    private List<Stock> stocks;

    public StockData() {
        this.stocks = new ArrayList();
    }

    public static StockData loadedFromFile(String filename) {
        StockData stockData = new StockData();
        stockData.loadFromFile(filename);
        return stockData;
    }

    public void loadFromFile(String filename) {
        File file = new File(filename);
        try {
            Scanner scanner = new Scanner(file);
            scanner.useDelimiter("\t|\\r\\n");
            scanner.useLocale(Locale.ENGLISH);

            scanner.nextLine(); // skip header


            while (scanner.hasNextLine()) {
                try {
                    Stock stock = new Stock();
                    stock.exchange = scanner.next();
                    stock.symbol = scanner.next().replaceAll("\\..*", ""); // remove exchange, e.g. AGO.AX -> AGO
                    stock.name = scanner.next();
                    stock.industry = scanner.next();
                    stock.sector = scanner.next();
                    stock.cap = scanner.next();
                    stock.last = scanner.next();
                    stock.change = scanner.next();
                    stock.changePer = scanner.next();
                    stock.volume = scanner.next();
                    stock.setDataAvailable();
                    this.stocks.add(stock);
                } catch (InputMismatchException e) {
                    //
                }
            }

            scanner.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public List<String> getExhanges() {
        return this.stocks.stream().map(stock -> stock.exchange).distinct().collect(Collectors.toList());
    }

    public List<Stock> getStocksOfExchange(String exchange) {
        return this.stocks.stream().filter(stock -> stock.exchange.equals(exchange)).collect(Collectors.toCollection(LinkedList::new));
    }

    public String getExchangeName(String exchange) {
        if (exchange.equals("NYSE")) return "New York Stock Exchange";
        else if (exchange.equals("NASDAQ")) return "NASDAQ";
        else if (exchange.equals("TSX")) return "Toronto Stock Exchange";
        else if (exchange.equals("XETRA")) return "TODO";
        else if (exchange.equals("HKSE")) return "Hong Kong Stock Exchange";
        else if (exchange.equals("BESE")) return "Bombay Stock Exchange";
        else if (exchange.equals("ASX")) return "Australian Securities Exchange";
        else if (exchange.equals("LSE")) return "London Stock Exchange";
        else throw new InputMismatchException();
    }
}


