package plus9000.data;

import org.jfree.data.DomainOrder;
import org.jfree.data.general.DatasetChangeEvent;
import org.jfree.data.general.DatasetChangeListener;
import org.jfree.data.general.DatasetGroup;
import org.jfree.data.xy.XYDataset;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Plus9000
 * Created by Martin on 30-12-2016.
 */
public class StockDataPerTickRTAdapter implements XYDataset {
    private StockDataPerTick stockDataPerTick;
    private Date currentTime; // ms
    private long range; // time difference (ms) between lower and upper bound index
    private int lowerBoundIndex;
    private int upperBoundIndex;
    private List<DatasetChangeListener> changeListeners;

    public StockDataPerTickRTAdapter(StockDataPerTick stockDataPerTick) {
        this.stockDataPerTick = stockDataPerTick;

        // The fictive time at which the program starts
        this.currentTime = new Date(43200000); // 13:00pm

        this.range = 600000; // 10 minutes by default

        this.upperBoundIndex = indexForTime(this.currentTime);
        // -1 so that one point is outside left side of screen
        this.lowerBoundIndex = indexForTime(new Date(this.currentTime.getTime() - range)) - 1;

        this.changeListeners = new ArrayList<>();
    }

    /* Needs to be called once every second */
    public void update() {
        this.currentTime.setTime(this.currentTime.getTime() + 1000); // increment second

        boolean datasetChanged = false;
        // Change indexes depending on current time
        Date lowerBoundTime = new Date(this.currentTime.getTime() - range);
        if (lowerBoundIndex < stockDataPerTick.getNumTicks() && stockDataPerTick.getTime(lowerBoundIndex + 1).before(lowerBoundTime)) {
            lowerBoundIndex++;
            datasetChanged = true;
        }

        if (upperBoundIndex < stockDataPerTick.getNumTicks() && stockDataPerTick.getTime(upperBoundIndex + 1).before(this.currentTime)) {
            upperBoundIndex++;
            datasetChanged = true;
        }

        // Notify change listeners
        if (datasetChanged) {
            this.notifyListeners();
        }
    }

    public Date getCurrentTime() {
        return this.currentTime;
    }

    public void setRange(long range) {
        this.range = range;
        // Update lower bound
        this.lowerBoundIndex = indexForTime(new Date(this.currentTime.getTime() - range)) - 1;
        this.notifyListeners();
    }

    private int indexForTime(Date time) {
        int index;
        for (index = 0; index < stockDataPerTick.getNumTicks()
                && stockDataPerTick.getTime(index).before(time); index++)
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
        else // if i == 1
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
                return this.currentTime.getTime();
            else
                return (double) this.stockDataPerTick.getTime(i1 + lowerBoundIndex).getTime();
        else { // current price line
            double xLineStart = this.stockDataPerTick.getTime(lowerBoundIndex).getTime();
            double xLineEnd = this.stockDataPerTick.getTime(upperBoundIndex).getTime();
            if (i1 == 0)
                return xLineStart;
            else if (i1 == 1)
                return xLineStart + (xLineEnd - xLineStart) * 0.1; // right of line start
            else // if i1 == 2
                return xLineEnd;
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
        return 2;
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
