package plus9000.gui;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.fx.interaction.ChartMouseEventFX;
import org.jfree.chart.fx.interaction.ChartMouseListenerFX;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.XYDataset;
import plus9000.data.StockDataPerTick;
import plus9000.data.StockDataPerTickRTAdapter;

/**
 * Plus9000
 * Created by Martin on 29-12-2016.
 */
public class LineChart implements ChartMouseListenerFX {
    private JFreeChart chart;

    public LineChart() {
        XYDataset dataset = this.createDataset("aapl"); // use Apple by default

        this.chart = ChartFactory.createTimeSeriesChart(
                "", "time", "price ($)", dataset, false, true, false);
        XYPlot plot = this.chart.getXYPlot();
        plot.getRangeAxis().setAutoRange(true);
//        plot.datasetChanged(new DatasetChangeEvent(this, dataset));
    }

    /* Needs to be called once every second */
    public void update() {
        XYPlot plot = this.chart.getXYPlot();
        // Update dataset
        StockDataPerTickRTAdapter dataset = (StockDataPerTickRTAdapter) plot.getDataset();
        dataset.update();

        // Update plot area
        DateAxis dateAxis = (DateAxis) plot.getDomainAxis();
        long currentTime = dataset.getCurrentTime().getTime();
        dateAxis.setLowerBound(currentTime - 600000); // keep last 10 minutes in view
        dateAxis.setUpperBound(currentTime);
        plot.configureDomainAxes();
    }

    public JFreeChart getChart() {
        return this.chart;
    }

    public void changeStock(String symbol) {
        StockDataPerTickRTAdapter dataset = this.createDataset(symbol);
        XYPlot plot = this.chart.getXYPlot();
        plot.setDataset(dataset);
    }

    private StockDataPerTickRTAdapter createDataset(String symbol) {
        StockDataPerTick stocks = new StockDataPerTick();
        stocks.loadFromFile("data/" + symbol.toUpperCase() + "_161212_161212.csv");
        return new StockDataPerTickRTAdapter(stocks);
    }

    @Override
    public void chartMouseClicked(ChartMouseEventFX chartMouseEventFX) {

    }

    @Override
    public void chartMouseMoved(ChartMouseEventFX chartMouseEventFX) {

    }
}
