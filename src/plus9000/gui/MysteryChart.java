package plus9000.gui;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.XYToolTipGenerator;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Random;

/**
 * Created by Martin on 15-Jan-17.
 */
public class MysteryChart {
    private JFreeChart chart;

    public MysteryChart() {
        XYSeriesCollection dataset = new XYSeriesCollection();
        XYSeries series = new XYSeries("Random");
        Random r = new Random();
        for (int i = 0; i <= 10; i++) {
            double x = r.nextDouble();
            double y = r.nextDouble();
            series.add(x, y);
        }
        dataset.addSeries(series);

        this.chart = ChartFactory.createScatterPlot("", "", "", dataset);
        try {
            this.chart.getXYPlot().setBackgroundImage(ImageIO.read(new File("data/world_map.png")));
            this.chart.getXYPlot().setBackgroundPaint(new Color(255, 255, 255));
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.chart.getXYPlot().getRenderer().setBaseToolTipGenerator(new XYToolTipGenerator() {
            @Override
            public String generateToolTip(XYDataset xyDataset, int i, int i1) {
                return "item " + Integer.toString(i1);
            }
        });
    }

    public JFreeChart getChart() {
        return this.chart;
    }
}
