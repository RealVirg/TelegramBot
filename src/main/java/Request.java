import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class Request {
    String[] input;
    String des;
    String reply;
    String request_url;
    String wrongInput = "Please, can you write your request right?\n" +
            "If you don't know how, you can use /seekCheapestFlight for check some example or read a documentation.";
    String notFound = "Sorry, but there are no results for your search.\n" +
            "Check your input information. Maybe you entered the wrong date?";

    Request(String[] st, String url) {
        input = st;
        des = "";
        request_url = url;
    }

    String getHTTP(String urlToRead) throws Exception {
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

    void seekCheapestFlight(SqliteDB conn, boolean flag)
    {
        if (input.length == 5 || input.length == 6 || input.length == 4){
            boolean withoutReturnDate = false;
            if (((input[input.length - 1].equals("RUB") ||
                    input[input.length - 1].equals("EUR") ||
                    input[input.length - 1].equals("USD")) && input.length == 5) ||
                    input.length == 4) {
                withoutReturnDate = true;
            }
            String token = "5d146c60846217f62771c7faaddbff4b";
            String origin = "";
            String destination = "";
            String depart_date = "";
            String return_date = "";
            String currency = "RUB";
            try {
                if (!withoutReturnDate){
                    origin = input[1];
                    destination = input[2];
                    des = destination;
                    depart_date = input[3];
                    return_date = input[4];
                    if (input.length == 6)
                    {
                        currency = input[5];
                    }
                }
                else
                {
                    origin = input[1];
                    destination = input[2];
                    des = destination;
                    depart_date = input[3];
                    if (input.length == 5)
                    {
                        currency = input[4];
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
                reply = wrongInput;
            }
            String url;
            if (!withoutReturnDate) {
                url = request_url +
                        "origin=" + origin +
                        "&destination=" + destination +
                        "&depart_date=" + depart_date +
                        "&return_date=" + return_date +
                        "&token=" + token +
                        "&currency=" + currency;
            }
            else {
                url = request_url +
                        "origin=" + origin +
                        "&destination=" + destination +
                        "&depart_date=" + depart_date +
                        "&token=" + token +
                        "&currency=" + currency;
            }
            try {
                this.parserJSONCheapestFlight(getHTTP(url));
            } catch (Exception e) {
                e.printStackTrace();
                reply = wrongInput;
            }
            if (flag) {
                int code_replay;
                String full_request = "";
                for (int i = 0; i < input.length; i++) {
                    full_request += input[i];
                    full_request += " ";
                }
                if (reply.equals(wrongInput))
                    code_replay = -1;
                else if (reply.equals(notFound))
                    code_replay = 0;
                else
                    code_replay = 1;
                if (code_replay != -1)
                {
                    conn.InsertLogLineInTableDay(origin, destination, depart_date, return_date, currency, code_replay, full_request);
                    conn.InsertLogLineInTableMonth(origin, destination, depart_date, return_date, currency, code_replay, full_request);
                }
                else {
                    conn.InsertLogLineInTableDay("", "", "", "", "", -1, full_request);
                    conn.InsertLogLineInTableMonth("", "", "", "", "", -1, full_request);
                }
            }
        }
        else
        {
            reply = wrongInput;
            if (flag) {
                String full_request = "";
                for (int i = 0; i < input.length; i++) {
                    full_request += input[i];
                    full_request += " ";
                }
                conn.InsertLogLineInTableDay("", "", "", "", "", -1, full_request);
                conn.InsertLogLineInTableMonth("", "", "", "", "", -1, full_request);
            }
        }
    }

    void parserJSONCheapestFlight(String jsonCode) {
        JSONObject jsonObject = new JSONObject(jsonCode);
        boolean success = jsonObject.getBoolean("success");
        Object temp = jsonObject.get("data");
        JSONObject jsonObject_1 = new JSONObject(temp.toString());

        try {
            Object temp_1 = jsonObject_1.get(des);
            JSONObject jsonObject_2 = new JSONObject(temp_1.toString());
            Object temp_2 = jsonObject_2.get("0");
            JSONObject jsonObject_3 = new JSONObject(temp_2.toString());
            int lowestPrice = jsonObject_3.getInt("price");
            String lowestPriceAirline = jsonObject_3.getString("airline");
            int lowestPriceFlightNumber = jsonObject_3.getInt("flight_number");
            String lowestPriceDepartureAt = jsonObject_3.getString("departure_at");
            String lowestPriceReturnAt = jsonObject_3.getString("return_at");
            String lowestPriceExpiresAt = jsonObject_3.getString("expires_at");
            String lowestPriceCurrency = jsonObject.getString("currency");

            reply = "Price: " + lowestPrice + " " + lowestPriceCurrency + "\n" +
                    "Airline: " + lowestPriceAirline + "\n" +
                    "FlightNumber: " + lowestPriceFlightNumber + "\n" +
                    "Departure at: " + lowestPriceDepartureAt + "\n" +
                    "Return at: " + lowestPriceReturnAt + "\n" +
                    "Expires at: " + lowestPriceExpiresAt + "\n" +
                    "http://pics.avs.io/200/200/" + lowestPriceAirline + ".png";
        } catch (Exception e) {
            e.printStackTrace();
            reply = notFound;
        }
    }
}