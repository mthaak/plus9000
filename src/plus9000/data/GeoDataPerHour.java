package plus9000.data;

import java.io.*;
import java.net.MalformedURLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.net.URL;
import java.time.LocalDateTime;

/**
 * Created by Jellevanmiltenburg on 12/01/17.
 */
public class GeoDataPerHour {
    public static void LoadFromAPI (String symbol){
        String url_String = String.format("https://www.google.com/finance/getprices?i=60&p=10d&f=d,o,h,l,c,v&df=cpct&q=%s", symbol);
        List<DataOfHour> dataPerHour = new ArrayList<>();
        try { URL url = new URL(url_String);
        BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
        String str;
        
        Scanner scanner = new Scanner(in);
            scanner.useDelimiter(",|\\n");
            scanner.useLocale(Locale.ENGLISH);

            scanner.nextLine(); // ignore Ticker
            scanner.nextLine(); // ignore Open_minute
            scanner.nextLine(); // ignore Close_minute
            scanner.nextLine(); // ignore Interval
            scanner.nextLine(); // ignore Columns
            scanner.nextLine(); // ignore Data
            String timezone = scanner.nextLine(); // ignore timezone_offset

            DateFormat df = new SimpleDateFormat("kk:mm:ss", Locale.ENGLISH);

            int hoursBack = 0;

            while (scanner.hasNextLine()) try {
                String interval = scanner.next();
                double openPrice = scanner.nextDouble();
                double highPrice = scanner.nextDouble();
                double lowPrice = scanner.nextDouble();
                double closePrice = scanner.nextDouble();
                int volume = scanner.nextInt();
                LocalDateTime date = LocalDateTime.now().minusHours(hoursBack);
                DataOfHour dataOfHour = new DataOfHour(openPrice, highPrice, lowPrice, closePrice, volume, date);
                dataPerHour.add(dataOfHour);
                hoursBack += 1;
            } catch (NoSuchElementException e) {
                // ignore line
            }
            scanner.close();
            in.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();

        }
        catch (MalformedURLException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

    }


}


class DataOfHour {
    public LocalDateTime date;
    public double openPrice, highPrice, lowPrice, closePrice;
    public int volume;
    public DataOfHour(double openPrice, double highPrice, double lowPrice, double closePrice, int volume, LocalDateTime date) {
        this.openPrice = openPrice;
        this.highPrice = highPrice;
        this.lowPrice = lowPrice;
        this.closePrice = closePrice;
        this.volume = volume;
        this.date = date;
    }
}