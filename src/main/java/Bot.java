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

    @Override
    public String getBotToken() {
        return "882526722:AAHk_SAlTXS2eM6B6uGgWapn3fRWizpndUc";
    }

    @Override
    public void onUpdateReceived(Update update) {
        try {
            String token = "5d146c60846217f62771c7faaddbff4b";
            String origin = "MOW";
            String destination = "JFK";
            String depart_date = "2019-11";
            String return_date = "2019-12";
            String url = "http://api.travelpayouts.com/v1/prices/cheap?" +
                    "origin=" + origin +
                    "&destination=" + destination +
                    "&depart_date=" + depart_date +
                    "&return_date=" + return_date +
                    "&token=" + token;
            HttpGet request = new HttpGet(url);
            if (update.hasMessage() && update.getMessage().hasText()) {
                Message inMessage = update.getMessage();
                SendMessage outMessage = new SendMessage();
                outMessage.setChatId(inMessage.getChatId());
                try {
                    outMessage.setText(getHTML(url));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                execute(outMessage);
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