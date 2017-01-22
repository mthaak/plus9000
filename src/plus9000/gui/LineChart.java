package plus9000.gui;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import plus9000.data.StockDataPerTick;
import plus9000.data.StockDataPerTickRTAdapter;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Plus9000
 * Created by Martin on 29-12-2016.
 */
public class LineChart {
    private JFreeChart chart;
    private Map<String, StockDataPerTickRTAdapter> datasets;
    private String currentSymbol;
    private long range;

    public LineChart() {
        datasets = new HashMap<>();
        String symbols[] = {"aapl", "amzn", "bac", "fb", "ge", "googl", "intc", "jnj", "msft", "xom"};
        for (String symbol : symbols) {
            datasets.put(symbol, this.createDataset(symbol));
        }

        this.currentSymbol = "aapl"; // show aapl by default
        this.range = 600000; // 10 minutes

        this.chart = ChartFactory.createTimeSeriesChart(
                "", "time", "price ($)", datasets.get(this.currentSymbol), false, true, false);
        this.chart.getXYPlot().getRangeAxis().setAutoRange(true);
        this.chart.getXYPlot().getDomainAxis().setAutoRange(false);

        XYItemRenderer renderer = this.chart.getXYPlot().getRenderer();
        renderer.setSeriesPaint(0, new Color(0, 0, 0));

        renderer.setSeriesItemLabelsVisible(1, true);
        renderer.setSeriesItemLabelGenerator(1, (xyDataset, i, i1) -> {
            if (i == 1 && i1 == 1)
                return "current price";
            else
                return "";
        });
        renderer.setSeriesItemLabelFont(1, new Font("Verdana", Font.PLAIN, 10));
        renderer.setSeriesPaint(1, new Color(255, 0, 0));
        renderer.setSeriesItemLabelPaint(1, new Color(255, 0, 0)); // text same color as line


    }

    /* Needs to be called once every second */
    public void update() {
        XYPlot plot = this.chart.getXYPlot();
        // Update dataset
        StockDataPerTickRTAdapter dataset = (StockDataPerTickRTAdapter) plot.getDataset();

        // Update plot area
        DateAxis dateAxis = (DateAxis) plot.getDomainAxis();
        long currentTime = dataset.getCurrentTime().getTime();
        dateAxis.setLowerBound(currentTime - this.range);
        dateAxis.setUpperBound(currentTime);

        dataset.update();
    }

    public JFreeChart getChart() {
        return this.chart;
    }

    public void changeStock(String symbol) {
        if (symbol.equals("aapl") || symbol.equals("bac") || symbol.equals("msft")) {
            this.currentSymbol = symbol;
            this.chart.getXYPlot().setDataset(this.datasets.get(symbol));
        }
    }

    public void changeRange(long range) {
        this.range = range;
        this.datasets.get(this.currentSymbol).setRange(range);
        this.update();
    }

    private StockDataPerTickRTAdapter createDataset(String symbol) {
        StockDataPerTick stocks = new StockDataPerTick();
        stocks.loadFromFile("data/tick/" + symbol.toUpperCase() + "_161212_161212.csv");
        return new StockDataPerTickRTAdapter(stocks);
    }

}
