package plus9000.gui;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.DateTickUnit;
import org.jfree.chart.axis.DateTickUnitType;
import org.jfree.chart.axis.TickUnits;
import org.jfree.chart.fx.interaction.ChartMouseEventFX;
import org.jfree.chart.fx.interaction.ChartMouseListenerFX;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.xy.OHLCDataset;
import plus9000.data.StockDataPerDay;
import plus9000.data.StockDataPerDayAdapter;

import java.awt.*;
import java.util.Date;

/**
 * Plus9000
 * Created by Martin on 18-12-2016.
 */
public class CandlestickChart implements ChartMouseListenerFX {
    private JFreeChart chart;

    public CandlestickChart() {
        OHLCDataset dataset = this.createDataset("aapl"); // use Apple by default

        this.chart = ChartFactory.createCandlestickChart(
                "Stock OHLC price per day", "date",
                "price ($)", dataset, false);
        this.chart.setBackgroundPaint(Color.white);
        XYPlot plot = this.chart.getXYPlot();
        DateAxis dateAxis = (DateAxis) plot.getDomainAxis();
        dateAxis.setRange(new Date(1451606400000l), new Date(1481846400000l)); // default range
        TickUnits tickUnits = new TickUnits();
        tickUnits.add(new DateTickUnit(DateTickUnitType.DAY, 1));
        tickUnits.add(new DateTickUnit(DateTickUnitType.DAY, 7));
        tickUnits.add(new DateTickUnit(DateTickUnitType.DAY, 15));
        tickUnits.add(new DateTickUnit(DateTickUnitType.MONTH, 1));
        tickUnits.add(new DateTickUnit(DateTickUnitType.MONTH, 3));
        tickUnits.add(new DateTickUnit(DateTickUnitType.MONTH, 6));
        tickUnits.add(new DateTickUnit(DateTickUnitType.YEAR, 1));
        tickUnits.add(new DateTickUnit(DateTickUnitType.YEAR, 5));
        tickUnits.add(new DateTickUnit(DateTickUnitType.YEAR, 10));
        tickUnits.add(new DateTickUnit(DateTickUnitType.YEAR, 25));
        dateAxis.setStandardTickUnits(tickUnits);

        XYItemRenderer renderer = (XYItemRenderer) plot.getRenderer();
        renderer.setSeriesPaint(0, (new Color(50, 50, 50)));
    }

    public JFreeChart getChartChart() {
        return this.chart;
    }

    public void changeStock(String symbol) {
        OHLCDataset dataset = this.createDataset(symbol);
        XYPlot plot = this.chart.getXYPlot();
        plot.setDataset(dataset);
    }

    private OHLCDataset createDataset(String symbol) {
        StockDataPerDay stocks = new StockDataPerDay();
        stocks.loadFromFile("data/" + symbol + ".csv");

        OHLCDataset adapter = new StockDataPerDayAdapter(stocks);
        return adapter;
    }

    @Override
    public void chartMouseClicked(ChartMouseEventFX event) {
    }

    @Override
    public void chartMouseMoved(ChartMouseEventFX event) {
    }
}
