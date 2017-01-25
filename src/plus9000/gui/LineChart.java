package plus9000.gui;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import plus9000.data.Stock;
import plus9000.data.StockData;
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
    private StockDataPerTickRTAdapter dataset;
    private Map<String, StockDataPerTick> stocks;
    private String currentSymbol;
    private long range;
    private long currentTime;

    public LineChart(StockData stockData) {
        stocks = new HashMap<>();

        // Load stock data
        for (String exchange : stockData.getExhanges()) {
            for (Stock stock : stockData.getStocksOfExchange(exchange)) {
                StockDataPerTick stockDataPerTick = StockDataPerTick.loadedFromFile(stock.getFullSymbol());
                if (stockDataPerTick != null)
                    stocks.put(stock.getFullSymbol(), stockDataPerTick);
            }
        }

        this.currentSymbol = null; // show none by default
        this.range = 600000; // 10 minutes
        this.currentTime = 43200000; // 13:00pm

        this.dataset = new StockDataPerTickRTAdapter();
        this.chart = ChartFactory.createTimeSeriesChart(
                "", "time", "price ($)", this.dataset, false, true, false);
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

        this.currentTime += 1000;

        // Update plot area
        DateAxis dateAxis = (DateAxis) plot.getDomainAxis();
        long lowerBound = Math.max(this.currentTime - this.range, 28800000); // do not show before 9:00
        dateAxis.setLowerBound(lowerBound);
        dateAxis.setUpperBound(this.currentTime);

        // Update dataset
        this.dataset.update();
    }

    public JFreeChart getChart() {
        return this.chart;
    }

    public void showStock(String symbol) {
        if (this.currentSymbol == null && this.stocks.containsKey(symbol)) {
            this.currentSymbol = symbol;
            this.range = 600000; // reset range
            this.dataset.setRange(this.range);
            this.dataset.changeStock(this.stocks.get(symbol));

            // Force auto update
            ValueAxis rangeAxis = this.chart.getXYPlot().getRangeAxis();
            rangeAxis.setAutoRange(false);
            rangeAxis.setAutoRange(true);
        }
    }

    public void hideStock() {
        this.currentSymbol = null;
        this.dataset.changeStock(null); // removes stock data series
    }

    public void changeRange(long range) {
        this.range = range;
        this.dataset.setRange(this.range);
        this.update();

        // Force auto update
        ValueAxis rangeAxis = this.chart.getXYPlot().getRangeAxis();
        rangeAxis.setAutoRange(false);
        rangeAxis.setAutoRange(true);
    }
}
