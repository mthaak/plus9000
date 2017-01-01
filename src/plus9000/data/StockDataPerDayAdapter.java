package plus9000.data;

import org.jfree.data.DomainOrder;
import org.jfree.data.general.DatasetChangeListener;
import org.jfree.data.general.DatasetGroup;
import org.jfree.data.xy.OHLCDataset;

/**
 * Plus9000
 * Created by Martin on 18-12-2016.
 */
public class StockDataPerDayAdapter implements OHLCDataset {
    StockDataPerDay stockDataPerDay;

    public StockDataPerDayAdapter(StockDataPerDay stockDataPerDay) {
        this.stockDataPerDay = stockDataPerDay;
    }

    @Override
    public Number getHigh(int i, int i1) {
        return this.getHighValue(i, i1);
    }

    @Override
    public double getHighValue(int i, int i1) {
        return stockDataPerDay.getHighPrice(i1);
    }

    @Override
    public Number getLow(int i, int i1) {
        return this.getLowValue(i, i1);
    }

    @Override
    public double getLowValue(int i, int i1) {
        return stockDataPerDay.getLowPrice(i1);
    }

    @Override
    public Number getOpen(int i, int i1) {
        return this.getOpenValue(i, i1);
    }

    @Override
    public double getOpenValue(int i, int i1) {
        return stockDataPerDay.getOpenPrice(i1);
    }

    @Override
    public Number getClose(int i, int i1) {
        return this.getCloseValue(i, i1);
    }

    @Override
    public double getCloseValue(int i, int i1) {
        return stockDataPerDay.getClosePrice(i1);
    }

    @Override
    public Number getVolume(int i, int i1) {
        return this.getVolumeValue(i, i1);
    }

    @Override
    public double getVolumeValue(int i, int i1) {
        return stockDataPerDay.getVolume(i1);
    }

    @Override
    public DomainOrder getDomainOrder() {
        return DomainOrder.NONE;
    }

    @Override
    public int getItemCount(int i) {
        return stockDataPerDay.numDays();
    }

    @Override
    public Number getX(int i, int i1) {
        return this.getXValue(i, i1);
    }

    @Override
    public double getXValue(int i, int i1) {
        return (double) stockDataPerDay.getDate(i1).getTime();
    }

    @Override
    public Number getY(int i, int i1) {
        return null;
    }

    @Override
    public double getYValue(int i, int i1) {
        return 0;
    }

    @Override
    public int getSeriesCount() {
        return 1;
    }

    @Override
    public Comparable getSeriesKey(int i) {
        return "Stock open/high/low/close price per day";
    }

    @Override
    public int indexOf(Comparable comparable) {
        return 1;
    }

    @Override
    public void addChangeListener(DatasetChangeListener datasetChangeListener) {

    }

    @Override
    public void removeChangeListener(DatasetChangeListener datasetChangeListener) {

    }

    @Override
    public DatasetGroup getGroup() {
        return null;
    }

    @Override
    public void setGroup(DatasetGroup datasetGroup) {

    }
}
