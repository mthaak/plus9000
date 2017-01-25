package plus9000.gui;

import javafx.application.Platform;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.AnchorPane;
import org.jfree.chart.fx.ChartViewer;
import plus9000.data.StockData;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Martin on 15-Jan-17.
 */
public class WorldChartPanel extends TitledPane implements StockSelectorListener {
    WorldChart worldChart;
    ChartViewer viewer;

    public WorldChartPanel(StockData stockData) {
        this.worldChart = new WorldChart(stockData);
        this.viewer = new ChartViewer(worldChart.getChart());
        this.worldChart.setViewer(viewer);
        viewer.setMaxSize(400, 400);

        AnchorPane anchorPane = new AnchorPane();
        anchorPane.getChildren().add(viewer);

        this.setText("World");
        this.setCollapsible(false);
        this.setContent(anchorPane);

        // Show circles after 500 ms (since chart needs to be rendered at least once first)
        new Timer().schedule(new ShowCirclesWorldChartTask(this.worldChart), 500);
    }


    @Override
    public void stockFocused(String symbol) {
        // Do nothing
    }

    @Override
    public void stockUnfocused() {
        // Do nothing
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

    @Override
    public void exchangeChanged(String exchange) {
        this.worldChart.highLightExchange(exchange);
    }
}

class ShowCirclesWorldChartTask extends TimerTask {
    private final WorldChart worldChart;

    ShowCirclesWorldChartTask(WorldChart worldChart) {
        this.worldChart = worldChart;
    }

    @Override
    public void run() {
        Platform.runLater(worldChart::showCircles); // to make sure update is executed from JavaFX thread
    }
}
