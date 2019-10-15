import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Request {
    public String[] input;
    public String des;
    public String request;

    Request(String[] st) {
        input = st;
        des = "";
    }

    public String getHTML(String urlToRead) throws Exception {
        StringBuilder result = new StringBuilder();
        URL url = new URL(urlToRead);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String line;
        while ((line = rd.readLine()) != null) {
            result.append(line);
        }
        rd.close();
        return result.toString();
    }

    public void SeekFly()
    {
        if (input.length > 3 && input.length < 6){
            String token = "5d146c60846217f62771c7faaddbff4b";
            String origin = "";
            String destination = "";
            String depart_date = "";
            String return_date = "";
            String currency = "RUB";
            try {
                origin = input[0];
                destination = input[1];
                des = destination;
                depart_date = input[2];
                return_date = input[3];
                if (input.length == 5)
                {
                    currency = input[4];
                }

            } catch (Exception e) {
                e.printStackTrace();
                request =  "/help";
            }
            String url = "http://api.travelpayouts.com/v1/prices/cheap?" +
                    "origin=" + origin +
                    "&destination=" + destination +
                    "&depart_date=" + depart_date +
                    "&return_date=" + return_date +
                    "&token=" + token +
                    "&currency=" + currency;
            try {
                this.parserJSON(getHTML(url));
            } catch (Exception e) {
                e.printStackTrace();
                request =  "/help";
            }
        }
        else
        {
            request = "/help";
        }
    }

    public void parserJSON(String something) {
        JSONObject jsonObject = new JSONObject(something);
        boolean success = jsonObject.getBoolean("success");
        Object temp = jsonObject.get("data");
        JSONObject jsonObject_1 = new JSONObject(temp.toString());
        try {
            Object temp_1 = jsonObject_1.get(des);
            JSONObject jsonObject_2 = new JSONObject(temp_1.toString());
            Object temp_2 = jsonObject_2.get("0");
            JSONObject jsonObject_3 = new JSONObject(temp_2.toString());
            Integer lowestPrice = jsonObject_3.getInt("price");
            String lowestPriceAirline = jsonObject_3.getString("airline");
            Integer lowestPriceFlightNumber = jsonObject_3.getInt("flight_number");
            String lowestPriceDepartureAt = jsonObject_3.getString("departure_at");
            String lowestPriceReturnAt = jsonObject_3.getString("return_at");
            String lowestPriceExpiresAt = jsonObject_3.getString("expires_at");
            String lowestPriceCurrency = jsonObject.getString("currency");
            this.makeRequest(lowestPrice, lowestPriceAirline,
                    lowestPriceFlightNumber, lowestPriceDepartureAt,
                    lowestPriceReturnAt, lowestPriceExpiresAt,
                    lowestPriceCurrency);
        } catch (Exception e) {
            e.printStackTrace();
            request =  "No flight";
        }
    }

    public void makeRequest(Integer lowestPrice, String lowestPriceAirline,
                              Integer lowestPriceFlightNumber, String lowestPriceDepartureAt,
                              String lowestPriceReturnAt, String lowestPriceExpiresAt,
                              String lowestPriceCurrency) {
        String result;
        result = "Price: " + lowestPrice + " " + lowestPriceCurrency + "\n" +
                "Airline: " + lowestPriceAirline + "\n" +
                "FlightNumber: " + lowestPriceFlightNumber + "\n" +
                "Departure at: " + lowestPriceDepartureAt + "\n" +
                "Return at: " + lowestPriceReturnAt + "\n" +
                "Expires at: " + lowestPriceExpiresAt + "\n" +
                "http://pics.avs.io/200/200/" + lowestPriceAirline + ".png";
        request = result;
    }
}