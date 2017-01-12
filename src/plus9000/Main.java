package plus9000;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.jfree.chart.fx.ChartViewer;
import plus9000.gui.CandlestickChartViewer;
import plus9000.gui.LineChart;
import plus9000.gui.StockSelector;

import java.util.Timer;
import java.util.TimerTask;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        int i;
        for (i = 0; i < 0; i++) {
            System.out.println(i);
        }

        primaryStage.setTitle("Plus9000");
        primaryStage.setWidth(1024);
        primaryStage.setHeight(768);
        BorderPane root = FXMLLoader.load(getClass().getResource("main.fxml"));

        LineChart lineChart = new LineChart();
        ChartViewer lineChartViewer = new ChartViewer(lineChart.getChart());
//        lineChartViewer.setMinWidth(1000);
        lineChartViewer.addChartMouseListener(lineChart);

        CandlestickChartViewer candlestickChartViewer = new CandlestickChartViewer();

        StockSelector stockSelector = new StockSelector();
        root.setLeft(stockSelector);

        VBox plots = (VBox) root.lookup("#plots");
        plots.getChildren().add(candlestickChartViewer);
        plots.getChildren().add(lineChartViewer);

        primaryStage.setScene(new Scene(root, 1024, 768));
        primaryStage.show();


        // Update line chart every second
        new Timer().schedule(new UpdateLineChartTask(lineChart), 0, 1000);
    }

}

class UpdateLineChartTask extends TimerTask {
    LineChart lineChart;

    UpdateLineChartTask(LineChart lineChart) {
        this.lineChart = lineChart;
    }

    @Override
    public void run() {
        this.lineChart.update();
    }
}
