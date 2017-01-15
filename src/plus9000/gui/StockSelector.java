package plus9000.gui;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TitledPane;
import javafx.scene.control.cell.CheckBoxListCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

import java.util.ArrayList;
import java.util.List;

public class StockSelector extends TitledPane {
    List<StockSelectorListener> listeners;

    public StockSelector() {
        this.listeners = new ArrayList<>();

        final List<StockListItem> stocks = new ArrayList<>();
        stocks.add(new StockListItem("Apple", "aapl", true, true));
        stocks.add(new StockListItem("Amazon", "amzn", false, false));
        stocks.add(new StockListItem("Bank of America", "bac", false, false));
        stocks.add(new StockListItem("Facebook", "fb", false, false));
        stocks.add(new StockListItem("General Electrics", "ge", false, false));
        stocks.add(new StockListItem("Google/Alphabet", "googl", false, false));
        stocks.add(new StockListItem("Intel", "intc", false, false));
        stocks.add(new StockListItem("Johnson & Johnson", "jnj", false, false));
        stocks.add(new StockListItem("Microsoft", "msft", false, false));
        stocks.add(new StockListItem("Exxon Mobil", "xom", false, false));

        final ListView<StockListItem> listView = new ListView();
        listView.setItems(FXCollections.observableArrayList(stocks));

        // Set stock focused listener
        listView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<StockListItem>() {
            @Override
            public void changed(ObservableValue<? extends StockListItem> observableValue, StockListItem prevFocusedStock, StockListItem newFocusedStock) {
                if (prevFocusedStock != null && !prevFocusedStock.isChecked()) {
                    notifyListenersStockUnchecked(prevFocusedStock.getSymbol());
                }
                // Check and focus
                notifyListenersStockChecked(newFocusedStock.getSymbol()); // even if it's not actually checked
                notifyListenersStockFocused(newFocusedStock.getSymbol());
            }
        });

        // Set stock checked/unchecked listener
        listView.setCellFactory(CheckBoxListCell.forListView(new Callback<StockListItem, ObservableValue<Boolean>>() {
            @Override
            public ObservableValue<Boolean> call(StockListItem stock) {
                stock.getCheckedProperty().addListener((obs, wasChecked, isNowChecked) -> {
                    if (!wasChecked && isNowChecked && !stock.getFocusedProperty().getValue())
                                notifyListenersStockChecked(stock.getSymbol());
                    else if (wasChecked && !isNowChecked && !stock.getFocusedProperty().getValue())
                                notifyListenersStockUnchecked(stock.getSymbol());
                        }
                );
                return stock.getCheckedProperty();
            }
        }));

        Button showAllButton = new Button("Show all");
        showAllButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                for (StockListItem stock : stocks)
                    stock.getCheckedProperty().setValue(true);
                notifyListenersAllStocksChecked();
            }
        });

        Button hideAllButton = new Button("Hide all");
        hideAllButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                for (StockListItem stock : stocks)
                    stock.getCheckedProperty().setValue(false);
                notifyListenersAllStocksUnchecked();
            }
        });

        this.setText("US stocks");
        this.setCollapsible(false);
        VBox vBox = new VBox();
        this.setContent(vBox);
        vBox.setPadding(new Insets(0));

        listView.setStyle("-fx-background-color:transparent;"); // remove border
        vBox.getChildren().add(listView);

        HBox hBox = new HBox();
        hBox.getChildren().add(showAllButton);
        hBox.getChildren().add(hideAllButton);
        vBox.getChildren().add(hBox);
    }

    public void addListener(StockSelectorListener listener){
        this.listeners.add(listener);
    }

    private void notifyListenersStockFocused(String symbol){
        for (StockSelectorListener listener : this.listeners)
            listener.stockFocused(symbol);
    }

    private void notifyListenersStockChecked(String symbol){
        for (StockSelectorListener listener : this.listeners)
            listener.stockChecked(symbol);
    }

    private void notifyListenersStockUnchecked(String symbol){
        for (StockSelectorListener listener : this.listeners)
            listener.stockUnchecked(symbol);
    }

    private void notifyListenersAllStocksChecked() {
        for (StockSelectorListener listener : this.listeners)
            listener.allStocksChecked();
    }

    private void notifyListenersAllStocksUnchecked() {
        for (StockSelectorListener listener : this.listeners)
            listener.allStocksUnchecked();
    }


}

class StockListItem {
    private String name;
    private String symbol;
    private BooleanProperty focused;
    private BooleanProperty checked;

    public StockListItem(String name, String symbol, boolean focused, boolean checked) {
        this.name = name;
        this.symbol = symbol;
        this.focused = new SimpleBooleanProperty(focused);
        this.checked = new SimpleBooleanProperty(checked);
    }

    public String getSymbol(){
        return this.symbol;
    }

    public boolean isFocused() {
        return this.focused.getValue();
    }

    public boolean isChecked() {
        return this.checked.getValue();
    }

    public BooleanProperty getFocusedProperty() {
        return this.focused;
    }

    public BooleanProperty getCheckedProperty(){
        return this.checked;
    }

    public String toString(){
        return String.format("%s (%s)", this.name, this.symbol.toUpperCase());
    }
}