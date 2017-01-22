package plus9000.gui;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.jfree.chart.fx.ChartViewer;
import plus9000.data.StockData;
import plus9000.util.Period;

/**
 * Plus9000
 * Created by Martin on 12-1-2017.
 */
public class CandlestickChartPanel extends VBox implements StockSelectorListener {
    final private CandlestickChart candlestickChart;

    public CandlestickChartPanel(StockData stockData) {
        this.candlestickChart = new CandlestickChart(stockData);
        this.candlestickChart.show("aapl"); // show Apple by default
        ChartViewer viewer = new ChartViewer(candlestickChart.getChart());

        Button perDayButton = new Button("Per day");
        perDayButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                candlestickChart.setPeriod(Period.DAY);
            }
        });

        Button perWeekButton = new Button("Per week");
        perWeekButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                candlestickChart.setPeriod(Period.WEEK);
            }
        });

        Button perMonthButton = new Button("Per month");
        perMonthButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                candlestickChart.setPeriod(Period.MONTH);
            }
        });

        Button perYearButton = new Button("Per year");
        perYearButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                candlestickChart.setPeriod(Period.YEAR);
            }
        });

        Button defaultZoomButton = new Button("Default zoom");
        defaultZoomButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                candlestickChart.resetZoom();
            }
        });

        Button allTimeButton = new Button("All time");
        allTimeButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                candlestickChart.seeAll();
            }
        });

        HBox hBox = new HBox();
        hBox.getChildren().add(perDayButton);
        hBox.getChildren().add(perWeekButton);
        hBox.getChildren().add(perMonthButton);
        hBox.getChildren().add(perYearButton);
        hBox.getChildren().add(defaultZoomButton);
        hBox.getChildren().add(allTimeButton);
        this.getChildren().add(hBox);

        this.getChildren().add(viewer);
    }

    @Override
    public void stockFocused(String symbol) {
        this.candlestickChart.setFocus(symbol);
    }

    @Override
    public void stockUnfocused() {
        this.candlestickChart.unfocus();
    }

    @Override
    public void stockChecked(String symbol) {
        this.candlestickChart.show(symbol);
    }

    @Override
    public void stockUnchecked(String symbol) {
        this.candlestickChart.hide(symbol);
    }

    @Override
    public void allStocksChecked() {
        this.candlestickChart.showAll();
    }

    @Override
    public void allStocksUnchecked() {
        this.candlestickChart.hideAll();
    }

    @Override
    public void exchangeChanged(String exchange) {
        // Do nothing
    }
}
