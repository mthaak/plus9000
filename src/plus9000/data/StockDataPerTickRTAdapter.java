package plus9000.data;

import org.jfree.data.DomainOrder;
import org.jfree.data.general.DatasetChangeEvent;
import org.jfree.data.general.DatasetChangeListener;
import org.jfree.data.general.DatasetGroup;
import org.jfree.data.xy.XYDataset;

import java.util.ArrayList;
import java.util.List;

/**
 * Plus9000
 * Created by Martin on 30-12-2016.
 */
public class StockDataPerTickRTAdapter implements XYDataset {
    private StockDataPerTick stockDataPerTick;
    private long currentTime; // ms
    private long range; // ms
    private int lowerBoundIndex;
    private int upperBoundIndex;
    private List<DatasetChangeListener> changeListeners;

    public StockDataPerTickRTAdapter() {
        this.stockDataPerTick = null;

        // The fictive time at which the program starts
        this.currentTime = 43200000; // 13:00pm
        this.range = 600000; // 10 minutes by default

        this.changeListeners = new ArrayList<>();
    }

    /* Needs to be called once every second */
    public void update() {
        this.currentTime += 1000; // increment second

        if (this.stockDataPerTick != null) {
            boolean datasetChanged = false;
            // Change indexes depending on current time
            if (lowerBoundIndex < stockDataPerTick.getNumTicks() && stockDataPerTick.getTime(lowerBoundIndex + 1).getTime() < this.currentTime - this.range) {
                lowerBoundIndex++;
                datasetChanged = true;
            }

            if (upperBoundIndex < stockDataPerTick.getNumTicks() && stockDataPerTick.getTime(upperBoundIndex + 1).getTime() < this.currentTime) {
                upperBoundIndex++;
                datasetChanged = true;
            }

            // Notify change listeners
            if (datasetChanged) {
                this.notifyListeners();
            }
        }
    }

    public void changeStock(StockDataPerTick stockDataPerTick) {
        this.stockDataPerTick = stockDataPerTick;
        if (stockDataPerTick != null) {
            // -1 so that one point is outside left side of screen
            this.lowerBoundIndex = indexForTime(this.currentTime - this.range, 0) - 1;

            this.upperBoundIndex = indexForTime(this.currentTime, lowerBoundIndex);

            this.notifyListeners(); // notify data changed
        }
    }

    public void setRange(long range) {
        this.range = range;
        if (this.stockDataPerTick != null) {
            // Update lower bound
            this.lowerBoundIndex = indexForTime(this.currentTime - range, 0);
            this.notifyListeners();
        }
    }

    private int indexForTime(long time, int startIndex) {
        int index;
        for (index = startIndex; index < stockDataPerTick.getNumTicks()
                && stockDataPerTick.getTime(index).getTime() < time; index++)
            ;
        if (index > 0)
            return index - 1;
        else
            return 0;
    }

    private void notifyListeners() {
        for (DatasetChangeListener changeListener : this.changeListeners) {
            changeListener.datasetChanged(new DatasetChangeEvent(this, this));
        }
    }

    @Override
    public DomainOrder getDomainOrder() {
        return null;
    }

    @Override
    public int getItemCount(int i) {
        if (i == 0)
            return this.upperBoundIndex - lowerBoundIndex + 1; // dynamic
        else // if i == 0
            return 3;
    }

    @Override
    public Number getX(int i, int i1) {
        return this.getXValue(i, i1);
    }

    @Override
    public double getXValue(int i, int i1) {
        if (i == 0)
            if (i1 == this.upperBoundIndex - lowerBoundIndex) // last item
                return this.currentTime;
            else
                return (double) this.stockDataPerTick.getTime(i1 + lowerBoundIndex).getTime();
        else { // current price line
            if (i1 == 0)
                return this.currentTime;
            else if (i1 == 1)
                return this.currentTime - 0.9 * this.range;
            else // if i1 == 2
                return this.currentTime - this.range;
        }
    }

    @Override
    public Number getY(int i, int i1) {
        return this.getYValue(i, i1);
    }

    @Override
    public double getYValue(int i, int i1) {
        if (i == 0)
            if (i1 == this.upperBoundIndex - lowerBoundIndex) // last item
                return this.stockDataPerTick.getPrice(upperBoundIndex - 1);
            else
                return this.stockDataPerTick.getPrice(i1 + lowerBoundIndex);
        else { // current price line
            return this.stockDataPerTick.getPrice(upperBoundIndex - 1); // straight line
        }
    }

    @Override
    public int getSeriesCount() {
        if (this.stockDataPerTick != null)
            return 2;
        else
            return 0;
    }

    @Override
    public Comparable getSeriesKey(int i) {
        return i;
    }

    @Override
    public int indexOf(Comparable comparable) {
        return (int) comparable;
    }

    @Override
    public void addChangeListener(DatasetChangeListener datasetChangeListener) {
        this.changeListeners.add(datasetChangeListener);
    }

    @Override
    public void removeChangeListener(DatasetChangeListener datasetChangeListener) {
        this.changeListeners.remove(datasetChangeListener);
    }

    @Override
    public DatasetGroup getGroup() {
        return null;
    }

    @Override
    public void setGroup(DatasetGroup datasetGroup) {

    }
}
