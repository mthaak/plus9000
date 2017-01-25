package plus9000.gui;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.*;
import org.jfree.chart.renderer.xy.CandlestickRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import plus9000.data.OHLCDataCollection;
import plus9000.data.StockData;
import plus9000.util.Period;

import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Plus9000
 * Created by Martin on 18-12-2016.
 */
public class CandlestickChart {
    private OHLCDataCollection dataset;
    private JFreeChart chart;

    private boolean hasFocus;
    private int focusedSeries;
    private Paint beforeFocusPaint;

    public CandlestickChart(StockData stockData) {
        this.dataset = OHLCDataCollection.loadedFromStockData(stockData);

        this.chart = ChartFactory.createCandlestickChart("", "", "price ($)", this.dataset, true);

        ((CandlestickRenderer) this.chart.getXYPlot().getRenderer()).setAutoWidthFactor(1000); // very high such that we always use max width
        this.setFocus("aapl"); // focus Apple by default
        this.setPeriod(Period.DAY); // default period is day
        this.resetZoom();
    }

    public JFreeChart getChart() {
        return this.chart;
    }

    public void setFocus(String symbol) {
        int index = this.dataset.indexOf(symbol);
        if (index != -1) {

            // Restore paint of previous focused
            CandlestickRenderer renderer = (CandlestickRenderer) this.chart.getXYPlot().getRenderer();
            if (this.hasFocus) {
                renderer.setSeriesPaint(this.focusedSeries, this.beforeFocusPaint);
            }
            this.focusedSeries = index;
            this.beforeFocusPaint = renderer.getSeriesPaint(index);
            renderer.setSeriesPaint(index, new Color(0, 0, 0));
            this.hasFocus = true;
        }
    }

    public void unfocus() {
        XYItemRenderer renderer = this.chart.getXYPlot().getRenderer();
        if (this.hasFocus) {
            renderer.setSeriesPaint(this.focusedSeries, this.beforeFocusPaint);
        }
        this.hasFocus = false;
    }

    public void show(String symbol) {
        this.dataset.include(symbol);
    }

    public void hide(String symbol) {
        this.dataset.exclude(symbol);
    }

    public void showAll() {
        this.dataset.includeAll();
    }

    public void hideAll() {
        this.dataset.excludeAll();
    }

    public void setPeriod(Period period) {
        this.dataset.setPeriod(period);
        this.setUnitsAndFormat();
        this.resetZoom();
    }

    public void resetZoom() {
        DateAxis dateAxis = (DateAxis) this.chart.getXYPlot().getDomainAxis();
        CandlestickRenderer renderer = (CandlestickRenderer) this.chart.getXYPlot().getRenderer();
        Period period = this.dataset.getPeriod();
        if (period == Period.DAY) {
            dateAxis.setRange(new Date(116, 11, 1), new Date(116, 11, 15)); // show last month
        } else if (period == Period.WEEK) {
            dateAxis.setRange(new Date(116, 6, 1), new Date(117, 0, 1)); // show last half year
        } else if (period == Period.MONTH) {
            dateAxis.setRange(new Date(115, 0, 1), new Date(117, 0, 1)); // show last two years
        } else if (period == Period.YEAR) {
            dateAxis.setRange(new Date(110, 0, 1), new Date(117, 0, 1)); // show all years
        }

        // Force auto update
        ValueAxis rangeAxis = this.chart.getXYPlot().getRangeAxis();
        rangeAxis.setAutoRange(false);
        rangeAxis.setAutoRange(true);
    }

    public void seeAll() {
        DateAxis dateAxis = (DateAxis) this.chart.getXYPlot().getDomainAxis();
        dateAxis.setRange(new Date(110, 0, 1), new Date(117, 0, 1)); // show all dates

        // Force auto update
        ValueAxis rangeAxis = this.chart.getXYPlot().getRangeAxis();
        rangeAxis.setAutoRange(false);
        rangeAxis.setAutoRange(true);
    }

    private void setUnitsAndFormat() {
        DateAxis dateAxis = (DateAxis) this.chart.getXYPlot().getDomainAxis();
        TickUnits tickUnits = new TickUnits();

        CandlestickRenderer renderer = (CandlestickRenderer) this.chart.getXYPlot().getRenderer();

        Period period = this.dataset.getPeriod();

        if (period == Period.DAY) {
            tickUnits.add(new DateTickUnit(DateTickUnitType.DAY, 1));
            tickUnits.add(new DateTickUnit(DateTickUnitType.DAY, 7));
            tickUnits.add(new DateTickUnit(DateTickUnitType.DAY, 14));
            tickUnits.add(new DateTickUnit(DateTickUnitType.MONTH, 1));
            tickUnits.add(new DateTickUnit(DateTickUnitType.MONTH, 3));
            tickUnits.add(new DateTickUnit(DateTickUnitType.MONTH, 6));
            tickUnits.add(new DateTickUnit(DateTickUnitType.YEAR, 1));
            tickUnits.add(new DateTickUnit(DateTickUnitType.YEAR, 5));
            dateAxis.setStandardTickUnits(tickUnits);

            dateAxis.setDateFormatOverride(new SimpleDateFormat("dd-MMM-yy"));

            renderer.setMaxCandleWidthInMilliseconds(64800000d); // 3/4 day

            this.chart.getLegend().setMargin(0, 0, 5, 0);
        } else if (period == Period.WEEK) {
            tickUnits.add(new DateTickUnit(DateTickUnitType.DAY, 7));
            tickUnits.add(new DateTickUnit(DateTickUnitType.DAY, 14));
            tickUnits.add(new DateTickUnit(DateTickUnitType.DAY, 28));
            tickUnits.add(new DateTickUnit(DateTickUnitType.DAY, 56));
            tickUnits.add(new DateTickUnit(DateTickUnitType.DAY, 112));
            tickUnits.add(new DateTickUnit(DateTickUnitType.DAY, 224));
            dateAxis.setStandardTickUnits(tickUnits);

            dateAxis.setDateFormatOverride(new SimpleDateFormat("dd-MMM-yy\n('week' ww)"));

            renderer.setMaxCandleWidthInMilliseconds(453600000d); // 3/4 week

            this.chart.getLegend().setMargin(15, 0, 5, 0);
        } else if (period == Period.MONTH) {
            tickUnits.add(new DateTickUnit(DateTickUnitType.MONTH, 1));
            tickUnits.add(new DateTickUnit(DateTickUnitType.MONTH, 3));
            tickUnits.add(new DateTickUnit(DateTickUnitType.MONTH, 6));
            tickUnits.add(new DateTickUnit(DateTickUnitType.YEAR, 1));
            tickUnits.add(new DateTickUnit(DateTickUnitType.YEAR, 5));
            dateAxis.setStandardTickUnits(tickUnits);

            dateAxis.setDateFormatOverride(new SimpleDateFormat("MMM\nyyyy"));

            renderer.setMaxCandleWidthInMilliseconds(2008800000d); // 3/4 month

            this.chart.getLegend().setMargin(15, 0, 5, 0);
        } else if (period == Period.YEAR) {
            tickUnits.add(new DateTickUnit(DateTickUnitType.YEAR, 1));
            tickUnits.add(new DateTickUnit(DateTickUnitType.YEAR, 5));
            dateAxis.setStandardTickUnits(tickUnits);

            dateAxis.setDateFormatOverride(new SimpleDateFormat("yyyy"));

            renderer.setMaxCandleWidthInMilliseconds(23716800000d); // 3/4 year

            this.chart.getLegend().setMargin(0, 0, 5, 0);
        }
    }
}
