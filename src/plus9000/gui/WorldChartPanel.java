package plus9000.gui;

import javafx.scene.control.TitledPane;
import javafx.scene.layout.AnchorPane;
import org.jfree.chart.fx.ChartViewer;
import plus9000.data.StockData;

/**
 * Created by Martin on 15-Jan-17.
 */
public class WorldChartPanel extends TitledPane implements StockSelectorListener {
    WorldChart worldChart;

    public WorldChartPanel(StockData stockData) {
        this.worldChart = new WorldChart(stockData);
        ChartViewer viewer = new ChartViewer(worldChart.getChart());
        viewer.setMaxSize(500, 500);

        AnchorPane anchorPane = new AnchorPane();
        anchorPane.getChildren().add(viewer);

        this.setText("World");
        this.setCollapsible(false);
        this.setContent(anchorPane);
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
