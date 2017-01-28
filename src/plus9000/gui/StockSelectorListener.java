package plus9000.gui;

/**
 * Plus9000
 * Created by Martin on 12-1-2017.
 */
public interface StockSelectorListener {
    void stockFocused(String symbol);

    void stockUnfocused(String symbol, boolean uncheck);

    void stockChecked(String symbol);

    void stockUnchecked(String symbol);

    void allStocksChecked();

    void allStocksUnchecked();

    void exchangeChanged(String exchange);
}
