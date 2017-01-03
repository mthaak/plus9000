package plus9000.gui;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.cell.CheckBoxListCell;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

import java.util.HashMap;
import java.util.Map;

public class StockSelector extends VBox {

    public StockSelector(final LineChart lineChart, final CandlestickChart candleStickChart) {
        Label label = new Label("American stocks");
        this.getChildren().add(label);

        final Map<String, String> stocksWithSymbols = new HashMap<>();
        stocksWithSymbols.put("Apple (AAPL)", "aapl");
        stocksWithSymbols.put("Amazon (AMZN)", "amzn");
        stocksWithSymbols.put("Bank of America (BAC)", "bac");
        stocksWithSymbols.put("Facebook (FB)", "fb");
        stocksWithSymbols.put("General Electrics (GE)", "ge");
        stocksWithSymbols.put("Google/Alphabet (GOOGL)", "googl");
        stocksWithSymbols.put("Intel (INTC)", "intc");
        stocksWithSymbols.put("Johnson & Johnson (JNJ)", "jnj");
        stocksWithSymbols.put("Microsoft (MSFT)", "msft");
        stocksWithSymbols.put("Exxon Mobil (XOM)", "xom");
        ObservableList<String> stocks = FXCollections.observableArrayList(stocksWithSymbols.keySet());

        final ListView listView = new ListView(stocks);
        listView.setFocusTraversable(false);

        // Set checkbox enable/disable listener
        listView.setCellFactory(CheckBoxListCell.forListView(new Callback<String, ObservableValue<Boolean>>() {
            @Override
            public ObservableValue<Boolean> call(String stock) {
                BooleanProperty observable = new SimpleBooleanProperty();
                observable.addListener((obs, wasSelected, isNowSelected) ->
                        candleStickChart.changeStock(stocksWithSymbols.get(stock))
                );
                return observable;
            }
        }));

        // Set select listener
        listView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String oldValue, String newValue) {
                candleStickChart.changeStock(stocksWithSymbols.get(observableValue.getValue()));
            }
        });

        this.getChildren().add(listView);
    }
}