package plus9000.gui;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.cell.CheckBoxListCell;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StockSelector extends VBox {

    public StockSelector(final LineChart lineChart, final CandlestickChart candleStickChart) {
        Label label = new Label("American stocks");
        this.getChildren().add(label);

        final List<CheckedStock> stocks = new ArrayList<>();
        stocks.add(new CheckedStock("Apple", "aapl", new SimpleBooleanProperty(true)));
        stocks.add(new CheckedStock("Amazon", "amzn", new SimpleBooleanProperty(false)));
        stocks.add(new CheckedStock("Bank of America", "bac", new SimpleBooleanProperty(false)));
        stocks.add(new CheckedStock("Facebook", "fb", new SimpleBooleanProperty(false)));
        stocks.add(new CheckedStock("General Electrics", "ge", new SimpleBooleanProperty(false)));
        stocks.add(new CheckedStock("Google/Alphabet", "googl", new SimpleBooleanProperty(false)));
        stocks.add(new CheckedStock("Intel", "intc", new SimpleBooleanProperty(false)));
        stocks.add(new CheckedStock("Johnson & Johnson", "jnj", new SimpleBooleanProperty(false)));
        stocks.add(new CheckedStock("Microsoft", "msft", new SimpleBooleanProperty(false)));
        stocks.add(new CheckedStock("Exxon Mobil", "xom", new SimpleBooleanProperty(false)));



        final ListView<CheckedStock> listView = new ListView();
        listView.setItems(FXCollections.observableArrayList(stocks));
//        listView.setFocusTraversable(false);
//        listView.getSelectionModel().clearSelection(); // select nothing
//        listView.getSelectionModel().select(0); // select first (Apple)
        candleStickChart.show("aapl"); // show Apple by default

        // Set checkbox enable/disable listener
        listView.setCellFactory(CheckBoxListCell.forListView(new Callback<CheckedStock, ObservableValue<Boolean>>() {
            @Override
            public ObservableValue<Boolean> call(CheckedStock stock) {
                stock.getCheckedProperty().addListener((obs, wasSelected, isNowSelected) -> {
                            if (!wasSelected && isNowSelected)
                                candleStickChart.show(stock.getSymbol());
                            else if (wasSelected && !isNowSelected)
                                candleStickChart.hide(stock.getSymbol());
                        }
                );
                return stock.getCheckedProperty();
            }
        }));

        // Set select listener
        listView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<CheckedStock>() {
            @Override
            public void changed(ObservableValue<? extends CheckedStock> observableValue, CheckedStock oldStock, CheckedStock newStock) {
                if (oldStock != null) // if some other stock was selected
                    oldStock.getCheckedProperty().setValue(false);
                newStock.getCheckedProperty().setValue(true);
                candleStickChart.setFocus(newStock.getSymbol());
            }
        });

        this.getChildren().add(listView);
    }
}

class CheckedStock{
    private String name;
    private String symbol;
    private BooleanProperty checked;

    public CheckedStock(String name, String symbol, BooleanProperty checked){
        this.name = name;
        this.symbol = symbol;
        this.checked = checked;
    }

    public String getSymbol(){
        return this.symbol;
    }

    public BooleanProperty getCheckedProperty(){
        return this.checked;
    }

    public String toString(){
        return String.format("%s (%s)", this.name, this.symbol.toUpperCase());
    }
}