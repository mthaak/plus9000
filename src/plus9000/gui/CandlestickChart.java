package plus9000.gui;

import javafx.scene.input.MouseEvent;
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
import plus9000.data.StockDataPerDay;
import plus9000.data.StockDataPerDayAdapter;

import java.awt.*;
import java.util.Date;

/**
 * Plus9000
 * Created by Martin on 18-12-2016.
 */
public class CandlestickChart {
    private JFreeChart chart;
    private StockDataPerDayAdapter dataset;
    private boolean hasFocus;

    public CandlestickChart() {
        this.dataset = this.createDataset();

        this.chart = ChartFactory.createCandlestickChart(
                "Stock OHLC price per day", "date",
                "price ($)", this.dataset, true);
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

        plot.setDomainPannable(true);
        plot.setRangePannable(true);

        this.hasFocus = true;
    }

    public JFreeChart getChartChart() {
        return this.chart;
    }

    public void setFocus(String stockSymbol){
        this.dataset.setFocus(stockSymbol);
        this.hasFocus = true;
    }

    public void unfocus(){
        // TODO
        this.hasFocus = false;
    }

    public void show(String stockSymbol){
     this.dataset.show(stockSymbol);
    }

    public void hide(String stockSymbol){
        this.dataset.hide(stockSymbol);
    }

    public void showAll(){
        this.dataset.showAll();
    }

    public void hideAll(){
        this.hideAll();
    }

    private StockDataPerDayAdapter createDataset() {
        StockDataPerDayAdapter dataset = new StockDataPerDayAdapter();
        dataset.add("aapl", StockDataPerDay.loadedFromFile("data/aapl.csv"));
        dataset.add("amzn", StockDataPerDay.loadedFromFile("data/amzn.csv"));
        dataset.add("bac", StockDataPerDay.loadedFromFile("data/bac.csv"));
        dataset.add("fb", StockDataPerDay.loadedFromFile("data/fb.csv"));
        dataset.add("ge", StockDataPerDay.loadedFromFile("data/ge.csv"));
        dataset.add("googl", StockDataPerDay.loadedFromFile("data/googl.csv"));
        dataset.add("intc", StockDataPerDay.loadedFromFile("data/intc.csv"));
        dataset.add("jnj", StockDataPerDay.loadedFromFile("data/jnj.csv"));
        dataset.add("msft", StockDataPerDay.loadedFromFile("data/msft.csv"));
        dataset.add("xom", StockDataPerDay.loadedFromFile("data/xom.csv"));
        return dataset;
    }

}
