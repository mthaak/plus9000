package plus9000.data;

import org.jfree.data.DomainOrder;
import org.jfree.data.general.DatasetChangeEvent;
import org.jfree.data.general.DatasetChangeListener;
import org.jfree.data.general.DatasetGroup;
import org.jfree.data.xy.OHLCDataset;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Plus9000
 * Created by Martin on 18-12-2016.
 */
public class StockDataPerDayAdapter implements OHLCDataset {
    Map<String, StockDataPerDay> stocks;
    List<String> visibleStocks;
    List<DatasetChangeListener> changeListeners;

    public StockDataPerDayAdapter() {
        this.stocks = new HashMap();
        this.visibleStocks = new ArrayList();
        this.changeListeners = new ArrayList();
    }

    /**
     * Adds a stock.
     * @param stockDataPerDay
     */
    public void add(String stockSymbol, StockDataPerDay stockDataPerDay){
        if (!this.stocks.containsKey(stockSymbol)) {
            this.stocks.put(stockSymbol, stockDataPerDay);
            notifyChangeListeners();
        }
    }

    /**
     * Sets focus on a stock.
     * @param stockSymbol
     */
    public void setFocus(String stockSymbol){
        // Put focussed stock on series 0
        if (this.visibleStocks.contains(stockSymbol))
            this.visibleStocks.remove(stockSymbol);
        this.visibleStocks.add(0, stockSymbol);
        notifyChangeListeners();
    }

    /**
     * Shows a stock.
     * @param stockSymbol
     */
    public void show(String stockSymbol){
        if (!this.visibleStocks.contains(stockSymbol)) {
            this.visibleStocks.add(stockSymbol);
            notifyChangeListeners();
        }
    }

    /**
     * Hides a stock.
     * @param stockSymbol
     */
    public void hide(String stockSymbol){
        if (this.visibleStocks.contains(stockSymbol)) {
            this.visibleStocks.remove(stockSymbol);
            notifyChangeListeners();
        }
    }

    /**
     * Shows all stocks.
     */
    public void showAll(){
        this.visibleStocks.addAll(this.stocks.keySet());
        notifyChangeListeners();
    }

    /**
     * Hides all stocks.
     */
    public void hideAll(){
        this.visibleStocks.clear();
        notifyChangeListeners();
    }

    private void notifyChangeListeners(){
        for (DatasetChangeListener changeListener : this.changeListeners) {
            changeListener.datasetChanged(new DatasetChangeEvent(this, this));
        }
    }

    @Override
    public Number getHigh(int i, int i1) {
        return this.getHighValue(i, i1);
    }

    @Override
    public double getHighValue(int i, int i1) {
            return this.stocks.get(this.visibleStocks.get(i)).getHighPrice(i1);
    }

    @Override
    public Number getLow(int i, int i1) {
        return this.getLowValue(i, i1);
    }

    @Override
    public double getLowValue(int i, int i1) {
            return this.stocks.get(this.visibleStocks.get(i)).getLowPrice(i1);
    }

    @Override
    public Number getOpen(int i, int i1) {
        return this.getOpenValue(i, i1);
    }

    @Override
    public double getOpenValue(int i, int i1) {
            return this.stocks.get(this.visibleStocks.get(i)).getOpenPrice(i1);
    }

    @Override
    public Number getClose(int i, int i1) {
        return this.getCloseValue(i, i1);
    }

    @Override
    public double getCloseValue(int i, int i1) {
            return this.stocks.get(this.visibleStocks.get(i)).getClosePrice(i1);
    }

    @Override
    public Number getVolume(int i, int i1) {
        return this.getVolumeValue(i, i1);
    }

    @Override
    public double getVolumeValue(int i, int i1) {
            return this.stocks.get(this.visibleStocks.get(i)).getVolume(i1);
    }

    @Override
    public DomainOrder getDomainOrder() {
        return DomainOrder.NONE;
    }

    @Override
    public int getItemCount(int i) {
            return this.stocks.get(this.visibleStocks.get(i)).numDays();
    }

    @Override
    public Number getX(int i, int i1) {
        return this.getXValue(i, i1);
    }

    @Override
    public double getXValue(int i, int i1) {
            return (double) this.stocks.get(this.visibleStocks.get(i)).getDate(i1).getTime();
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
            return this.visibleStocks.size();
    }

    @Override
    public Comparable getSeriesKey(int i) {
            return this.visibleStocks.get(i);
    }

    @Override
    public int indexOf(Comparable comparable) {
        int i;
        for (i = 0; i < this.stocks.size(); i++)
            if (this.visibleStocks.get(i) == comparable) break;
        return i;
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
