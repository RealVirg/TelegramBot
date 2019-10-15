import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonObjectFormatVisitor;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import java.io.*;
import java.net.*;

import org.json.*;



public class Bot extends TelegramLongPollingBot {

//    public static String getHTML(String urlToRead) throws Exception {
//        StringBuilder result = new StringBuilder();
//        URL url = new URL(urlToRead);
//        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//        conn.setRequestMethod("GET");
//        BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
//        String line;
//        while ((line = rd.readLine()) != null) {
//            result.append(line);
//        }
//        rd.close();
//        return result.toString();
//    }
//
//    public static String makeRequest(Integer lowestPrice, String lowestPriceAirline,
//                                     Integer lowestPriceFlightNumber, String lowestPriceDepartureAt,
//                                     String lowestPriceReturnAt, String lowestPriceExpiresAt,
//                                     String lowestPriceCurrency)
//    {
//        String result;
//        result = "Price: " + lowestPrice + " " + lowestPriceCurrency + "\n" +
//                "Airline: " + lowestPriceAirline + "\n" +
//                "FlightNumber: " + lowestPriceFlightNumber + "\n" +
//                "Departure at: " + lowestPriceDepartureAt + "\n" +
//                "Return at: " + lowestPriceReturnAt + "\n" +
//                "Expires at: " + lowestPriceExpiresAt + "\n";
//        return result;
//    }
//
//    public static String JsonParser(String stringForParse, String destination){
//       JSONObject jsonObject  = new JSONObject(stringForParse);
//       boolean success = jsonObject.getBoolean("success");
//       Object temp = jsonObject.get("data");
//       JSONObject jsonObject_1 = new JSONObject(temp.toString());
//       try {
//           Object temp_1 = jsonObject_1.get(destination);
//           JSONObject jsonObject_2 = new JSONObject(temp_1.toString());
//           Object temp_2 = jsonObject_2.get("0");
//           JSONObject jsonObject_3 = new JSONObject(temp_2.toString());
//           Integer lowestPrice = jsonObject_3.getInt("price");
//           String lowestPriceAirline = jsonObject_3.getString("airline");
//           Integer lowestPriceFlightNumber = jsonObject_3.getInt("flight_number");
//           String lowestPriceDepartureAt = jsonObject_3.getString("departure_at");
//           String lowestPriceReturnAt = jsonObject_3.getString("return_at");
//           String lowestPriceExpiresAt = jsonObject_3.getString("expires_at");
//           String lowestPriceCurrency = jsonObject.getString("currency");
//           return makeRequest(lowestPrice, lowestPriceAirline,
//                   lowestPriceFlightNumber, lowestPriceDepartureAt,
//                   lowestPriceReturnAt, lowestPriceExpiresAt,
//                   lowestPriceCurrency);
//       } catch (Exception e){
//           e.printStackTrace();
//           return "No flight";
//       }
//    }
//
//    public static String SeekFly(String[] oddr)
//    {
//        if (oddr.length > 3 && oddr.length < 6){
//            String token = "5d146c60846217f62771c7faaddbff4b";
//            String origin = "";
//            String destination = "";
//            String depart_date = "";
//            String return_date = "";
//            String currency = "RUB";
//            try {
//                origin = oddr[0];
//                destination = oddr[1];
//                depart_date = oddr[2];
//                return_date = oddr[3];
//                if (oddr.length == 5)
//                {
//                    currency = oddr[4];
//                }
//
//            } catch (Exception e) {
//                e.printStackTrace();
//                return "/help";
//            }
//            String url = "http://api.travelpayouts.com/v1/prices/cheap?" +
//                    "origin=" + origin +
//                    "&destination=" + destination +
//                    "&depart_date=" + depart_date +
//                    "&return_date=" + return_date +
//                    "&token=" + token +
//                    "&currency=" + currency;
//            String request = "";
//            try {
//                request = JsonParser(getHTML(url), destination).toString();
//            } catch (Exception e) {
//                e.printStackTrace();
//                return "/help";
//            }
//            return request;
//        }
//        return "/help";
//    }

    @Override
    public String getBotToken() {
        return "882526722:AAHk_SAlTXS2eM6B6uGgWapn3fRWizpndUc";
    }

    @Override
    public void onUpdateReceived(Update update) {
        try {
            if (update.hasMessage() && update.getMessage().hasText()) {
                Message inMessage = update.getMessage();
                SendMessage outMessage = new SendMessage();
                outMessage.setChatId(inMessage.getChatId());
                String[] oddr = inMessage.getText().split(" ");
                if (inMessage.getText().equals("/help"))
                {
                    outMessage.setText("Bot to seek for cheap flights.\n" +
                            "Request to bot: IATA-code origin, IATA-code destination," +
                            " depart date(YYYY-MM), return date(YYYY-MM), currency(RUB/USD/EUR).\n" +
                            "Example: SVX MOW 2020-01 2020-02.");
                    execute(outMessage);
                }
                else
                {
                    try {
//                        outMessage.setText(SeekFly(oddr));
                        Request r = new Request(oddr);
                        r.SeekFly();
                        outMessage.setText(r.request);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    execute(outMessage);
                }
            }
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getBotUsername() {
        return "fly_seeker_bot";
    }
}