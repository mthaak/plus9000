package plus9000.gui;

import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.jfree.chart.fx.ChartViewer;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Martin on 15-Jan-17.
 */
public class LineChartPanel extends VBox implements StockSelectorListener {
    private LineChart lineChart;

    public LineChartPanel() {
        this.lineChart = new LineChart();
        ChartViewer viewer = new ChartViewer(lineChart.getChart());

        Button lastMinute = new Button("Last minute");
        lastMinute.setOnAction(event -> lineChart.changeRange(600000));

        Button last10Minutes = new Button("Last 10 minutes");
        last10Minutes.setOnAction(event -> lineChart.changeRange(6000000));

        Button lastHour = new Button("Last hour");
        lastHour.setOnAction(event -> lineChart.changeRange(3600000));

        Button lastDayButton = new Button("Last day");
        lastDayButton.setOnAction(event -> {
            lineChart.changeRange(28800000); // 8 hours
        });

        HBox hBox = new HBox();
        hBox.getChildren().add(lastMinute);
        hBox.getChildren().add(last10Minutes);
        hBox.getChildren().add(lastHour);
        hBox.getChildren().add(lastDayButton);
        this.getChildren().add(hBox);

        this.getChildren().add(viewer);

        // Update line chart every second
        new Timer().schedule(new UpdateLineChartTask(this.lineChart), 1000, 1000);
    }

    @Override
    public void stockFocused(String symbol) {
        this.lineChart.changeStock(symbol);
    }

    @Override
    public void stockChecked(String symbol) {
        // Do nothing
    }

    @Override
    public void stockUnchecked(String symbol) {
        // Do nothing
    }

    @Override
    public void allStocksChecked() {
        // Do nothing
    }

    @Override
    public void allStocksUnchecked() {
        // Do nothing
    }
}

class UpdateLineChartTask extends TimerTask {
    private final LineChart lineChart;

    UpdateLineChartTask(LineChart lineChart) {
        this.lineChart = lineChart;
    }

    @Override
    public void run() {
        Platform.runLater(() -> lineChart.update()); // to make sure update is executed from JavaFX thread
    }
}