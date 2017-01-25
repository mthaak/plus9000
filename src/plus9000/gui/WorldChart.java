package plus9000.gui;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.fx.ChartViewer;
import org.jfree.chart.labels.ItemLabelAnchor;
import org.jfree.chart.labels.ItemLabelPosition;
import org.jfree.chart.labels.XYItemLabelGenerator;
import org.jfree.chart.labels.XYToolTipGenerator;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.RectangleEdge;
import org.jfree.ui.TextAnchor;
import plus9000.data.Stock;
import plus9000.data.StockData;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Martin on 15-Jan-17.
 */
public class WorldChart {
    private JFreeChart chart;
    private ChartViewer viewer;
    private int lastHighlightedIndex;
    private Map<String, Double> changePerExchange;

    public WorldChart(final StockData stockData) {
        // Create data set
        XYSeriesCollection dataset = new XYSeriesCollection();
        for (String exchange : stockData.getExhanges()) {
            XYSeries series = new XYSeries(exchange);

            if (exchange.equals("NYSE"))
                series.add(0.21, 0.73); // NYSE
            else if (exchange.equals("NASDAQ"))
                continue; // located at the same point as NYSE
            else if (exchange.equals("TSX"))
                series.add(0.22, 0.81); // TSX
            else if (exchange.equals("XETRA"))
                series.add(0.44, 0.77); // XETRA
            else if (exchange.equals("HKSE"))
                series.add(0.76, 0.59); // HKSE
            else if (exchange.equals("BSE"))
                series.add(0.64, 0.55); // BSE
            else if (exchange.equals("ASX"))
                series.add(0.84, 0.17); // ASX
            else if (exchange.equals("LSE"))
                series.add(0.41, 0.80); // LSE

            dataset.addSeries(series);
            try {
                XYSeries series2 = (XYSeries) series.clone();
                series2.setKey(series.getKey() + "2");
                dataset.addSeries(series2);
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
        }

        // Create chart
        this.chart = ChartFactory.createScatterPlot("", "", "", dataset);
        this.chart.removeLegend();
        try {
            this.chart.getXYPlot().setBackgroundImage(ImageIO.read(new File("data/world_map.png")));
            this.chart.getXYPlot().setBackgroundPaint(new Color(255, 255, 255));
        } catch (IOException e) {
            e.printStackTrace();
        }

        XYPlot plot = this.chart.getXYPlot();

        // Set up axis
        plot.getDomainAxis().setRange(0, 1);
        plot.getRangeAxis().setRange(0, 1);
        plot.getDomainAxis().setVisible(false);
        plot.getRangeAxis().setVisible(false);

        // Calculate exchange change
        this.changePerExchange = new HashMap<>();
        for (String exchange : stockData.getExhanges()) {
            double change = 0;
            for (Stock stock : stockData.getStocksOfExchange(exchange)) {
                change += Double.parseDouble(stock.changePer);
            }

            // Add NASDAQ since exchanges have the same locations
            if (exchange.equals("NYSE")) {
                for (Stock stock : stockData.getStocksOfExchange("NASDAQ")) {
                    change += Double.parseDouble(stock.changePer);
                }
            }

            this.changePerExchange.put(exchange, change);
        }

        XYItemRenderer renderer = plot.getRenderer();

        // Prevent XETRA label from overlapping LSE label
        renderer.setSeriesPositiveItemLabelPosition(dataset.indexOf("XETRA"), new ItemLabelPosition(ItemLabelAnchor.OUTSIDE12, TextAnchor.TOP_LEFT));

        renderer.setBaseToolTipGenerator(null); // no base tooltips

        renderer.setBaseItemLabelGenerator(new XYItemLabelGenerator() {
            @Override
            public String generateLabel(XYDataset xyDataset, int i, int i1) {
                if (dataset.getSeries(i).getKey().equals("NYSE"))
                    if (changePerExchange.get("NYSE") >= 0)
                        return String.format("NYSE/NASDAQ +%.1f%%", changePerExchange.get("NYSE")); // located at the same point
                    else
                        return String.format("NYSE/NASDAQ %.1f%%", changePerExchange.get("NYSE"));
                else {
                    String exchange = (String) dataset.getSeries(i).getKey();
                    if (changePerExchange.get(exchange) >= 0)
                        return String.format("%s +%.1f%%", exchange, changePerExchange.get(exchange));
                    else
                        return String.format("%s %.1f%%", exchange, changePerExchange.get(exchange));
                }
            }
        });

        for (int series = 0; series < this.chart.getXYPlot().getSeriesCount(); series++) {
            // "2" is added to some keys to prevent duplicates
            String exchange = ((String) dataset.getSeriesKey(series)).replace("2", "");

            // Tooltips
            if (series % 2 == 0)
                renderer.setSeriesToolTipGenerator(series, null); // no tooltips
            else // series % 2 == 1
                renderer.setSeriesToolTipGenerator(series, new XYToolTipGenerator() {
                    @Override
                    public String generateToolTip(XYDataset xyDataset, int i, int i1) {
                        return createToolTipForExchange(((String) dataset.getSeries(i).getKey()).replace("2", ""), stockData);
                    }
                });

            // Item labels
            if (series % 2 == 0)
                renderer.setSeriesItemLabelsVisible(series, true);
            else
                renderer.setSeriesItemLabelsVisible(series, false);

            if (exchange.equals("NASDAQ"))
                continue;

            double change = this.changePerExchange.get(exchange);

            // Paint
            Paint paint;
            if (change > 0)
                paint = Color.GREEN;
            else if (change < 0)
                paint = Color.RED;
            else
                paint = Color.BLACK;

            // Shape
            Shape shape;
            if (series % 2 == 0) {
                if (change > 0)
                    shape = new Polygon(new int[]{-5, 0, 5}, new int[]{2, -3, 2}, 3); // triangle
                else if (change < 0)
                    shape = new Polygon(new int[]{-5, 0, 5}, new int[]{-3, 2, -3}, 3); // triangle
                else
                    shape = new Rectangle(10, 3);
            } else { // series % 2 == 1
                shape = new Rectangle(0, 0); // no shape
            }

            renderer.setSeriesShape(series, shape);
            renderer.setSeriesPaint(series, paint);
        }

        this.highLightExchange("NYSE"); // highlight first exchange
    }

    public JFreeChart getChart() {
        return this.chart;
    }

    public void setViewer(ChartViewer viewer) {
        this.viewer = viewer;
    }

    public void showCircles() {
        if (this.viewer != null && this.viewer.getRenderingInfo() != null) {
            for (int series = 0; series < this.chart.getXYPlot().getDataset().getSeriesCount(); series++) {
                if (series % 2 == 1) {
                    String exchange = ((String) this.chart.getXYPlot().getDataset().getSeriesKey(series)).replace("2", "");

                    Rectangle2D rectangle2D = this.viewer.getRenderingInfo().getPlotInfo().getDataArea();

                    // We need to find the x and y coordinates of each point using the rendering info
                    double xPoint = this.chart.getXYPlot().getDataset().getX(series, 0).doubleValue();
                    RectangleEdge rectangleEdgeX = this.chart.getXYPlot().getDomainAxisEdge();
                    double x = chart.getXYPlot().getDomainAxis().valueToJava2D(xPoint, rectangle2D, rectangleEdgeX);

                    double yPoint = this.chart.getXYPlot().getDataset().getY(series, 0).doubleValue();
                    RectangleEdge rectangleEdgeY = this.chart.getXYPlot().getRangeAxisEdge();
                    double y = chart.getXYPlot().getDomainAxis().valueToJava2D(yPoint, rectangle2D, rectangleEdgeY);

                    Point2D center = new Point2D.Double(x, y);
                    float radius = 30;
                    float[] dist = {0.0f, 1.0f};
                    new Color(255, 255, 0);
                    Color[] colors;
                    double change = this.changePerExchange.get(exchange);
                    int changeCorr = (int) Math.min(Math.abs(change) / 40.0 * 255, 255);
                    if (change >= 0)
                        colors = new Color[]{new Color(255 - changeCorr, 255, 255 - changeCorr, 255), new Color(0, changeCorr, 0, 0)};
                    else // if (change < 0)
                        colors = new Color[]{new Color(255, 255 - changeCorr, 255 - changeCorr, 255), new Color(changeCorr, 0, 0, 0)};

                    RadialGradientPaint paint = new RadialGradientPaint(center, radius, dist, colors);

                    XYItemRenderer renderer = this.chart.getXYPlot().getRenderer();
                    renderer.setSeriesPaint(series, paint);

                    Shape shape = new Ellipse2D.Double(-50, -50, 100, 100);
                    renderer.setSeriesShape(series, shape);
                }
            }
        }
    }

    public void highLightExchange(String exchange) {
        if (exchange.equals("NASDAQ"))
            exchange = "NYSE"; // located at the same point

        XYItemRenderer renderer = this.chart.getXYPlot().getRenderer();
        renderer.setSeriesItemLabelFont(lastHighlightedIndex, null);

        int index = this.chart.getXYPlot().getDataset().indexOf(exchange);
        Font boldFont = new Font("Tahoma", Font.BOLD, 14);
        renderer.setSeriesItemLabelFont(index, boldFont);
        this.lastHighlightedIndex = index;
    }

    private String createToolTipForExchange(String exchange, StockData stockData) {
        StringBuilder label = new StringBuilder();
        int longestNameLength = 0;
        // First find longest name
        for (Stock stock : stockData.getStocksOfExchange(exchange)) {
            int nameLength = stock.toString().length();
            if (nameLength > longestNameLength)
                longestNameLength = nameLength;
        }
        for (Stock stock : stockData.getStocksOfExchange(exchange)) {
            String stockName = stock.toString();
            String space = new String(new char[longestNameLength + 4 - stockName.length()]).replace('\0', ' ');
            if (Double.parseDouble(stock.changePer) > 0)
                label.append(stockName + space + "+" + stock.changePer + "%\n");
            else
                label.append(stockName + space + stock.changePer + "%\n");
        }

        if (exchange.equals("NYSE")) // NYSE and NASDAQ are located at the same point
            return label.toString() + createToolTipForExchange("NASDAQ", stockData);
        else
            return label.toString();
    }
}
