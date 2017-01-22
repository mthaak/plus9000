package plus9000;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import plus9000.data.StockData;
import plus9000.gui.CandlestickChartPanel;
import plus9000.gui.LineChartPanel;
import plus9000.gui.StockSelector;
import plus9000.gui.WorldChartPanel;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Data
        StockData stockData = StockData.loadedFromFile("data/stocks.tsv");

        // Left
        StockSelector stockSelector = new StockSelector(stockData);

        WorldChartPanel worldChart = new WorldChartPanel(stockData);
        stockSelector.addListener(worldChart);

        VBox left = new VBox();
        left.getChildren().setAll(stockSelector, worldChart);

        // Center
        CandlestickChartPanel candlestickChart = new CandlestickChartPanel(stockData);
        stockSelector.addListener(candlestickChart);

        LineChartPanel lineChart = new LineChartPanel(stockData);
        stockSelector.addListener(lineChart);

        VBox center = new VBox();
        center.getChildren().setAll(
                new TitledPane("OHLC price per day", candlestickChart),
                new TitledPane("Price per tick", lineChart)
        );
        ((TitledPane) center.getChildren().get(0)).setExpanded(true);

        // Root
        BorderPane root = new BorderPane();
        root.setLeft(left);
        root.setCenter(center);

        // Stage
        primaryStage.setTitle("Plus9000");
        primaryStage.setWidth(1600);
        primaryStage.setHeight(1024);
        primaryStage.setScene(new Scene(root, 1024, 768));
        primaryStage.show();
    }
}


