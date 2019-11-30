import com.tbot.calendar.CalendarUtil;
import org.joda.time.LocalDate;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

public class Bot extends TelegramLongPollingBot {

    @Override
    public String getBotToken() {
        return "882526722:AAHk_SAlTXS2eM6B6uGgWapn3fRWizpndUc";
    }

    @Override
    public void onUpdateReceived(Update update) {
        try {
            if (update.hasMessage() && update.getMessage().hasText()) {
                SqliteDB conn = new SqliteDB();
                conn.CreateLogTableDay();
                conn.CreateLogTableMonth();
                Message inMessage = update.getMessage();
                SendMessage outMessage = new SendMessage();
                outMessage.setChatId(inMessage.getChatId());
                String[] oddr = inMessage.getText().split(" ");
                if (inMessage.getText().equals("/help"))
                {
                    outMessage.setText("Bot to seek for cheap flights.\n"+
                            "Commands: \n" + "/help\n/whatIsIATACode\n/seekCheapestFlight\n"
                    + "/getCountMonth\n" + "/getCountDay\n" + "/getMostPopularInMonth\n" + "/getMostPopularInDay\n" + "/calendar\n");
                    execute(outMessage);
                }
                else if (inMessage.getText().equals("/whatIsIATACode")) {
                    outMessage.setText("https://en.wikipedia.org/wiki/IATA_airport_code");
                    execute(outMessage);
                }
                else if (inMessage.getText().equals("/seekCheapestFlight"))
                {
                    outMessage.setText("Seek cheapest flight in selected date.\n" +
                            "First origin in format IATA-code,\n" +
                            "second destination in format IATA-code,\n" +
                            "third depart date in format YYYY-MM, \n" +
                            "fourth return date in format YYYY-MM(optional),\n" +
                            "fifth currency(you can choose from USD, EUR, RUB(standard), optional).\n" +
                            "Example:\n" +
                            "/seekCheapestFlight SVX MOW 2020-01 2020-02\n" +
                            "/seekCheapestFlight SVX MOW 2020-01 2020-02 EUR\n" +
                            "/seekCheapestFlight SVX MOW 2020-01\n" +
                            "/seekCheapestFlight SVX MOW 2020-01 USD\n");
                    execute(outMessage);
                }
                else if (oddr[0].equals("/seekCheapestFlight"))
                {
                    try {
                        Request r = new Request(oddr, "http://api.travelpayouts.com/v1/prices/direct?");
                        r.seekCheapestFlight(conn, true);
                        outMessage.setText(r.reply);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    execute(outMessage);
                }
                else if (oddr[0].equals("/getCountMonth"))
                {
                    try {
                        outMessage.setText(String.valueOf(conn.GetCountMonth(oddr[1], oddr[2])));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    execute(outMessage);
                }
                else if (oddr[0].equals("/getCountDay"))
                {
                    try {
                        outMessage.setText(String.valueOf(conn.GetCountDay(oddr[1], oddr[2])));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    execute(outMessage);
                }
                else if (oddr[0].equals("/getMostPopularInDay"))
                {
                    try {
                        outMessage.setText(conn.getMostPopularInDay(oddr[1]));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    execute(outMessage);
                }
                else if (oddr[0].equals("/getMostPopularInMonth"))
                {
                    try {
                        outMessage.setText(conn.getMostPopularInMonth(oddr[1]));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    execute(outMessage);
                }
                else if (oddr[0].equals("/calendar"))
                {
                    try {
                        execute(sendInlineKeyBoardMessage(update.getMessage().getChatId()));
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
                else {
                    outMessage.setText("I can't understand you.\nPlease use /help for check commands.");
                    execute(outMessage);
                }
            }
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public static SendMessage sendInlineKeyBoardMessage(long chatId)
    {
        CalendarUtil temp = new CalendarUtil();
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        while (true)
        {


            break;
        }

        return new SendMessage().setChatId(chatId).setText("qq").setReplyMarkup(inlineKeyboardMarkup);
        /*
        примеры кнопок
        InlineKeyboardButton inlineKeyboardButton1 = new InlineKeyboardButton();
        InlineKeyboardButton inlineKeyboardButton2 = new InlineKeyboardButton();
        inlineKeyboardButton1.setText(temp.generateKeyboard(LocalDate.now()));
        inlineKeyboardButton1.setCallbackData("Button \"Тык\" has been pressed");
        inlineKeyboardButton2.setText("Тык2");
        inlineKeyboardButton2.setCallbackData("Button \"Тык2\" has been pressed");
        List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<>();
        List<InlineKeyboardButton> keyboardButtonsRow2 = new ArrayList<>();
        keyboardButtonsRow1.add(inlineKeyboardButton1);
        keyboardButtonsRow1.add(new InlineKeyboardButton().setText("Fi4a").setCallbackData("CallFi4a"));
        keyboardButtonsRow2.add(inlineKeyboardButton2);
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        rowList.add(keyboardButtonsRow1);
        rowList.add(keyboardButtonsRow2);
        inlineKeyboardMarkup.setKeyboard(rowList);
        return new SendMessage().setChatId(chatId).setText("Пример").setReplyMarkup(inlineKeyboardMarkup);
         */
    }

    @Override
    public String getBotUsername() {
        return "fly_seeker_bot";
    }
}