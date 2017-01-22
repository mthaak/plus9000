package plus9000.gui;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.ItemLabelAnchor;
import org.jfree.chart.labels.ItemLabelPosition;
import org.jfree.chart.labels.XYItemLabelGenerator;
import org.jfree.chart.labels.XYToolTipGenerator;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.TextAnchor;
import plus9000.data.Stock;
import plus9000.data.StockData;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

/**
 * Created by Martin on 15-Jan-17.
 */
public class WorldChart {
    private JFreeChart chart;
    private int lastHighlightedIndex;

    public WorldChart(StockData stockData) {
        XYSeriesCollection dataset = new XYSeriesCollection();
        for (String exchange : stockData.getExhanges()) {
            XYSeries series = new XYSeries(exchange);

            if (exchange.equals("NYSE"))
                series.add(0.20, 0.73); // NYSE
            else if (exchange.equals("NASDAQ"))
                continue; // located at the same point as NYSE
            else if (exchange.equals("TSX"))
                series.add(0.21, 0.81); // TSX
            else if (exchange.equals("XETRA"))
                series.add(0.43, 0.79); // XETRA
            else if (exchange.equals("HKSE"))
                series.add(0.73, 0.59); // HKSE
            else if (exchange.equals("BSE"))
                series.add(0.63, 0.55); // BSE
            else if (exchange.equals("ASX"))
                series.add(0.83, 0.17); // ASX
            else if (exchange.equals("LSE"))
                series.add(0.40, 0.81); // LSE

            dataset.addSeries(series);
        }

        this.chart = ChartFactory.createScatterPlot("", "", "", dataset);
        this.chart.removeLegend();
        try {
            this.chart.getXYPlot().setBackgroundImage(ImageIO.read(new File("data/world_map.png")));
            this.chart.getXYPlot().setBackgroundPaint(new Color(255, 255, 255));
        } catch (IOException e) {
            e.printStackTrace();
        }

        XYPlot plot = this.chart.getXYPlot();
        plot.getDomainAxis().setRange(0, 1);
        plot.getRangeAxis().setRange(0, 1);

        XYItemRenderer renderer = plot.getRenderer();
        // Prevent XETRA label from overlapping LSE label
        renderer.setSeriesPositiveItemLabelPosition(dataset.indexOf("XETRA"), new ItemLabelPosition(ItemLabelAnchor.OUTSIDE12, TextAnchor.TOP_LEFT));
        renderer.setBaseToolTipGenerator(new XYToolTipGenerator() {
            @Override
            public String generateToolTip(XYDataset xyDataset, int i, int i1) {
                return createToolTipForExchange((String) dataset.getSeries(i).getKey(), stockData);
            }
        });
        renderer.setBaseItemLabelGenerator(new XYItemLabelGenerator() {
            @Override
            public String generateLabel(XYDataset xyDataset, int i, int i1) {
                if (dataset.getSeries(i).getKey().equals("NYSE"))
                    return "NYSE/NASDAQ"; // located at the same point
                else
                    return (String) dataset.getSeries(i).getKey();
            }
        });
        renderer.setBaseItemLabelsVisible(true);
        for (String exchange : stockData.getExhanges()) {
            if (exchange.equals("NASDAQ"))
                continue; // located at same point as NYSE

            double change = 0;
            for (Stock stock : stockData.getStocksOfExchange(exchange)) {
                change += Double.parseDouble(stock.change);
            }
            Shape shape;
            Paint paint;
            if (change > 0) {
                shape = new Polygon(new int[]{0, 5, 10}, new int[]{0, 5, 0}, 3); // triangle
                paint = Color.GREEN;
            } else if (change < 0) {
                shape = new Polygon(new int[]{0, 5, 10}, new int[]{5, 0, 5}, 3); // triangle
                paint = Color.RED;
            } else {
                shape = new Rectangle(10, 3);
                paint = Color.BLACK;
            }
            renderer.setSeriesShape(dataset.indexOf(exchange), shape);
            renderer.setSeriesPaint(dataset.indexOf(exchange), paint);
        }

        this.highLightExchange("NYSE"); // highlight first exchange
    }

    public JFreeChart getChart() {
        return this.chart;
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
            label.append(stockName + space + stock.change + "\n");
        }

        if (exchange.equals("NYSE")) // NYSE and NASDAQ are located at the same point
            return label.toString() + createToolTipForExchange("NASDAQ", stockData);
        else
            return label.toString();
    }
}
