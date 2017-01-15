package plus9000.gui;

import javafx.scene.layout.AnchorPane;
import org.jfree.chart.fx.ChartViewer;

/**
 * Created by Martin on 15-Jan-17.
 */
public class MysteryChartPanel extends AnchorPane {
    MysteryChart mysteryChart;

    public MysteryChartPanel() {
        this.mysteryChart = new MysteryChart();
        ChartViewer viewer = new ChartViewer(mysteryChart.getChart());
        viewer.setMaxSize(300, 300);
        this.getChildren().add(viewer);
    }

}
