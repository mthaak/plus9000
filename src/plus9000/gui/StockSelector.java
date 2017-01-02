package plus9000.gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class StockSelector extends VBox {
    CandlestickChart candleStickChart;
    LineChart lineChart;

    public StockSelector() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(
                "stock_selector.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    public void setCandleStickChart(CandlestickChart candleStickChart) {
        this.candleStickChart = candleStickChart;
    }

    public void setLineChart(LineChart lineChart) {
        this.lineChart = lineChart;
    }

    @FXML
    protected void clickedApple() {
        this.candleStickChart.changeStock("aapl");
        this.lineChart.changeStock("aapl");
    }

    @FXML
    protected void clickedAmazon() {
        this.candleStickChart.changeStock("amzn");
        // no tick data so no linechart update
    }

    @FXML
    protected void clickedFacebook() {
        this.candleStickChart.changeStock("fb");
        // no tick data so no linechart update
    }

    @FXML
    protected void clickedGoogle() {
        this.candleStickChart.changeStock("googl");
        // no tick data so no linechart update
    }

    @FXML
    protected void clickedMicrosoft() {
        this.candleStickChart.changeStock("msft");
        this.lineChart.changeStock("msft");
    }

    @FXML
    protected void clickedBank() {
        this.candleStickChart.changeStock("bac");
        this.lineChart.changeStock("bac");
    }
}