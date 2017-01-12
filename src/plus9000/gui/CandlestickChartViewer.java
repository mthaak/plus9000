package plus9000.gui;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import org.jfree.chart.fx.ChartViewer;

/**
 * Plus9000
 * Created by Martin on 12-1-2017.
 */
public class CandlestickChartViewer extends VBox implements StockSelectorListener{
    final private CandlestickChart candlestickChart;

    public CandlestickChartViewer(){
        this.candlestickChart = new CandlestickChart();
        this.candlestickChart.show("aapl"); // show Apple by default
        ChartViewer viewer = new ChartViewer(candlestickChart.getChartChart());

        Label label = new Label("Period");

        Button perDayButton = new Button("Day");
        perDayButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                candlestickChart.setPeriod("day");
            }
        });

        Button perWeekButton = new Button("Week");
        perWeekButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                candlestickChart.setPeriod("week");
            }
        });

        Button perMonthButton = new Button("Month");
        perMonthButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                candlestickChart.setPeriod("month");
            }
        });

        Button perYearButton = new Button("Year");
        perYearButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                candlestickChart.setPeriod("year");
            }
        });

        this.getChildren().add(label);
        this.getChildren().add(perDayButton);
        this.getChildren().add(perWeekButton);
        this.getChildren().add(perMonthButton);
        this.getChildren().add(viewer);
    }

    @Override
    public void stockFocused(String symbol){
        this.candlestickChart.setFocus(symbol);
    }

    @Override
    public void stockChecked(String symbol) {
        this.candlestickChart.show(symbol);
    }

    @Override
    public void stockUnchecked(String symbol) {
        this.candlestickChart.hide(symbol);
    }
}
