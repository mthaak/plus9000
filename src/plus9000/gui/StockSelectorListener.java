package plus9000.gui;

/**
 * Plus9000
 * Created by Martin on 12-1-2017.
 */
public interface StockSelectorListener {
    void stockFocused(String symbol);

    void stockChecked(String symbol);

    void stockUnchecked(String symbol);
}
