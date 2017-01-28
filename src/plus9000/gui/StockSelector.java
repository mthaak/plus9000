package plus9000.gui;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Text;
import plus9000.data.Stock;
import plus9000.data.StockData;

import java.util.ArrayList;
import java.util.List;

public class StockSelector extends TabPane {
    StockData stockData;
    List<StockSelectorListener> listeners;
    List<StockListItem> allStockListItems;

    public StockSelector(StockData stockData) {
        this.stockData = stockData;
        this.listeners = new ArrayList<>();
        allStockListItems = new ArrayList<>();

        this.setMinWidth(400);

        List<String> exchanges = this.stockData.getExhanges();
        for (String exchange : exchanges) {
            List<StockListItem> stockListItems = new ArrayList<>();
            List<Stock> stocks = this.stockData.getStocksOfExchange(exchange);
            for (Stock stock : stocks) {
                StockListItem stockListItem = new StockListItem(stock, false, false);
                stockListItems.add(stockListItem);
                allStockListItems.add(stockListItem);
            }

            // ListView
            final ListView<StockListItem> listView = new ListView();
            listView.setItems(FXCollections.observableArrayList(stockListItems));
            listView.setStyle("-fx-background-color:transparent;"); // remove border
            listView.setCellFactory(param -> new StockListCell(this));

            // Button
            Button hideAllButton = new Button("Clear all");
            hideAllButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    for (StockListItem stock : allStockListItems)
                        stock.getCheckedProperty().setValue(false);
                    notifyListenersAllStocksUnchecked();
                }
            });

            // HBox
            HBox hbox = new HBox();
            hbox.getChildren().setAll(hideAllButton);

            // VBox
            VBox vBox = new VBox();
            vBox.getChildren().setAll(listView, hbox);

            // Tab
            Tab tab = new Tab(exchange);
            tab.setContent(vBox);
            tab.setClosable(false);
            this.getTabs().add(tab);
        }
        this.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                notifyListenersExchangeChanged(getTabs().get((int) newValue).getText());
            }
        });
    }

    public void addListener(StockSelectorListener listener) {
        this.listeners.add(listener);
    }

    public void notifyListenersStockFocused(String symbol) {
        for (StockSelectorListener listener : this.listeners)
            listener.stockFocused(symbol);
    }

    public void notifyListenersStockUnfocused(String symbol, boolean uncheck) {
        for (StockSelectorListener listener : this.listeners)
            listener.stockUnfocused(symbol, uncheck);
    }

    public void notifyListenersStockChecked(String symbol) {
        for (StockSelectorListener listener : this.listeners)
            listener.stockChecked(symbol);
    }

    public void notifyListenersStockUnchecked(String symbol) {
        for (StockSelectorListener listener : this.listeners)
            listener.stockUnchecked(symbol);
    }

    public void notifyListenersAllStocksChecked() {
        for (StockSelectorListener listener : this.listeners)
            listener.allStocksChecked();
    }

    public void notifyListenersAllStocksUnchecked() {
        for (StockSelectorListener listener : this.listeners)
            listener.allStocksUnchecked();
    }

    public void notifyListenersExchangeChanged(String exchange) {
        for (StockSelectorListener listener : this.listeners)
            listener.exchangeChanged(exchange);
    }
}

class StockListItem extends Stock {
    public Background oldBackground;
    private BooleanProperty focused;
    private BooleanProperty checked;

    public StockListItem(Stock stock, boolean focused, boolean checked) {
        super(stock);
        this.focused = new SimpleBooleanProperty(focused);
        this.checked = new SimpleBooleanProperty(checked);
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

    public BooleanProperty getCheckedProperty() {
        return this.checked;
    }
}

class StockListCell extends ListCell<StockListItem> {
    final CheckBox checkBox = new CheckBox();
    final StockSelector stockSelector;

    public StockListCell(StockSelector stockSelector) {
        super();
        this.stockSelector = stockSelector;
    }

    @Override
    public void updateItem(final StockListItem stockListItem, boolean empty) {
        if (stockListItem == null)
            return;

        // Checkbox
        checkBox.setText(stockListItem.toString());
        checkBox.selectedProperty().bindBidirectional(stockListItem.getCheckedProperty());
        checkBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean wasChecked, Boolean isNowChecked) {
                if (!wasChecked && isNowChecked && !stockListItem.getFocusedProperty().getValue()) {
                    stockSelector.notifyListenersStockChecked(stockListItem.getFullSymbol());
                } else if (wasChecked && !isNowChecked && !stockListItem.getFocusedProperty().getValue()) {
                    stockSelector.notifyListenersStockUnchecked(stockListItem.getFullSymbol());
                }
            }
        });

        // Day/tick data available indicator
        Text dataAvailableText;
        if (stockListItem.hasDay)
            if (stockListItem.hasTick)
                dataAvailableText = new Text("   [d/t]");
            else
                dataAvailableText = new Text("   [d]");
        else if (stockListItem.hasTick)
            dataAvailableText = new Text("   [t]");
        else
            dataAvailableText = new Text("   []");
        dataAvailableText.setFill(Color.DARKGRAY);

        // Cell
        this.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                checkBox.requestFocus(); // so checkbox is already focused when you enter the cell
                stockListItem.oldBackground = getBackground();
                setBackground(new Background(new BackgroundFill(Color.DARKGRAY, CornerRadii.EMPTY, Insets.EMPTY)));
                stockSelector.notifyListenersStockFocused(stockListItem.getFullSymbol());
            }
        });
        this.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                setBackground(stockListItem.oldBackground);
                stockSelector.notifyListenersStockUnfocused(stockListItem.getFullSymbol(), !stockListItem.isChecked());
            }
        });
        this.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                checkBox.requestFocus(); // keep focus
                checkBox.fire();
            }
        });

        double change = Double.parseDouble(stockListItem.changePer);

        // Indicator
        Shape indicator;
        Text indicatorText;
        if (change > 0)
            indicatorText = new Text("+" + stockListItem.change + "%  ");
        else
            indicatorText = new Text(stockListItem.change + "%  ");
        if (change > 0) {
            indicator = new Polygon(0.0, -5.0, 5.0, -10.0, 10.0, -5.0);
            indicator.setFill(Color.GREEN);
            indicatorText.setFill(Color.GREEN);
        } else if (change < 0) {
            indicator = new Polygon(0.0, -5.0, 5.0, 0.0, 10.0, -5.0);
            indicator.setFill(Color.RED);
            indicatorText.setFill(Color.RED);
        } else {
            indicator = new Rectangle(10, 3, Color.BLACK);
            indicatorText.setFill(Color.BLACK);
        }

        // Space
        Pane space = new Pane();
        HBox.setHgrow(space, Priority.ALWAYS); // such that indicator floats right

        // HBox
        HBox hBox = new HBox();
        hBox.getChildren().setAll(checkBox, dataAvailableText, space, indicatorText, indicator);
        hBox.setAlignment(Pos.BASELINE_LEFT);

        setGraphic(hBox);
    }
}