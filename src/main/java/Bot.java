import org.apache.http.client.methods.HttpGet;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import java.io.*;
import java.net.*;


public class Bot extends TelegramLongPollingBot {

    public static String getHTML(String urlToRead) throws Exception {
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

    public static String SeekFly(String[] oddr)
    {
        if (oddr.length > 3 && oddr.length < 6){
            String token = "5d146c60846217f62771c7faaddbff4b";
            String origin = "";
            String destination = "";
            String depart_date = "";
            String return_date = "";
            String currency = "RUB";
            try {
                origin = oddr[0];
                destination = oddr[1];
                depart_date = oddr[2];
                return_date = oddr[3];
                if (oddr.length == 5)
                {
                    currency = oddr[4];
                }

            } catch (Exception e) {
                e.printStackTrace();
                return "fail";
            }
            String url = "http://api.travelpayouts.com/v1/prices/cheap?" +
                    "origin=" + origin +
                    "&destination=" + destination +
                    "&depart_date=" + depart_date +
                    "&return_date=" + return_date +
                    "&token=" + token +
                    "&currency=" + currency;
            String request = "";
            try {
                request = getHTML(url);
            } catch (Exception e) {
                e.printStackTrace();
                return "fail";
            }
            return request;
        }
        return "fail";
    }

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
                /*else if (inMessage.getText().equals("/setting"))
                {
                    outMessage.setText("setting_string");
                    execute(outMessage);
                }*/
                else
                {
                    try {
                        outMessage.setText(SeekFly(oddr));
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