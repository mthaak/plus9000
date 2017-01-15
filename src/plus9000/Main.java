package plus9000;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.jfree.chart.fx.ChartViewer;
import plus9000.gui.CandlestickChartPanel;
import plus9000.gui.LineChart;
import plus9000.gui.StockSelector;

import java.util.Timer;
import java.util.TimerTask;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Plus9000");
        primaryStage.setWidth(1024);
        primaryStage.setHeight(768);
        BorderPane root = FXMLLoader.load(getClass().getResource("main.fxml"));

        LineChart lineChart = new LineChart();
        ChartViewer lineChartViewer = new ChartViewer(lineChart.getChart());
        lineChartViewer.addChartMouseListener(lineChart);

        CandlestickChartPanel candlestickChartPanel = new CandlestickChartPanel();

        StockSelector stockSelector = new StockSelector();
        stockSelector.addListener(candlestickChartPanel);
        root.setLeft(stockSelector);

        VBox plots = (VBox) root.lookup("#plots");
        final VBox stackedTitledPanes = new VBox();
        stackedTitledPanes.getChildren().setAll(
                new TitledPane("OHLC price per day", candlestickChartPanel),
                new TitledPane("Price per tick", lineChartViewer)
        );
        ((TitledPane) stackedTitledPanes.getChildren().get(0)).setExpanded(true);
        plots.getChildren().add(stackedTitledPanes);

        primaryStage.setScene(new Scene(root, 1024, 768));
        primaryStage.show();

        // Update line chart every second
        new Timer().schedule(new UpdateLineChartTask(lineChart), 1000, 1000);
    }

}

class UpdateLineChartTask extends TimerTask {
    final LineChart lineChart;

    UpdateLineChartTask(LineChart lineChart) {
        this.lineChart = lineChart;
    }

    @Override
    public void run() {
        Platform.runLater(() -> lineChart.update()); // to make sure update is executed from JavaFX thread
    }
}
