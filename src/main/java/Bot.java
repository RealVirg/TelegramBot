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
                        execute(sendInlineKeyBoardMessage(update.getMessage().getChatId(),
                                CalendarUtil.currentDate.getMonthOfYear(), CalendarUtil.currentDate.getYear()));
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                else if (update.hasCallbackQuery())
                {
                    try {
                        //if (update.getCallbackQuery().getData().equals("<") || update.getCallbackQuery().getData().equals(">"))
                        execute(new SendMessage().setText(update.getCallbackQuery().getData()).setChatId(update.getCallbackQuery().getMessage().getChatId()));
                    } catch (Exception e) {
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

    public static SendMessage sendInlineKeyBoardMessage(long chatId, int month, int year)
    {
        /*
        int tmp = CalendarUtil.currentDate.getMonthOfYear();
        String currentMonth = CalendarUtil.months[tmp-1];

        int currentMonthDay = CalendarUtil.currentDate.getDayOfMonth();
        int currentWeekDay = CalendarUtil.currentDate.getDayOfWeek();
        int currentYear = CalendarUtil.currentDate.getYear();

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        InlineKeyboardButton leftArrow = new InlineKeyboardButton();
        leftArrow.setText("<");
        leftArrow.setCallbackData("Button \"<\" has been pressed");

        InlineKeyboardButton buttonMonth = new InlineKeyboardButton();
        buttonMonth.setText(currentMonth + " " + currentYear);
        buttonMonth.setCallbackData("Button \"Month\" has been pressed");

        InlineKeyboardButton rightArrow = new InlineKeyboardButton();
        rightArrow.setText(">");
        rightArrow.setCallbackData("Button \">\" has been pressed");

        List<InlineKeyboardButton> row1 = new ArrayList<>();
        row1.add(leftArrow);
        row1.add(buttonMonth);
        row1.add(rightArrow);

        List<InlineKeyboardButton> row2 = new ArrayList<>();

        for (int i=0;i<=6;i++)
        {
            InlineKeyboardButton button = new InlineKeyboardButton();
            button.setText(CalendarUtil.days[i]);
            button.setCallbackData("Button \"weekDay " + CalendarUtil.days[i] + "\" has been pressed");

            row2.add(button);
        }

        List<List<InlineKeyboardButton>> rowList= new ArrayList<>();
        rowList.add(row1);
        rowList.add(row2);

        for (int line = 0; line <= 4; line++)
        {
            List<InlineKeyboardButton> row = new ArrayList<>();

            for (int col = 0; col <= 6; col++)
            {
                InlineKeyboardButton button = new InlineKeyboardButton();
                button.setCallbackData("Button \"day\" has been pressed");
                button.setText("gg");
                row.add(button);
            }

            rowList.add(row);
        }

        inlineKeyboardMarkup.setKeyboard(rowList);


         */
        //InlineKeyboardMarkup inlineKeyboardMarkup = CalendarUtil.createMonth(11, 2019, CalendarUtil.currentDate);
        InlineKeyboardMarkup inlineKeyboardMarkup = CalendarUtil.createMonth(month, year, CalendarUtil.currentDate);

        return new SendMessage().setChatId(chatId).setText("Calendar").setReplyMarkup(inlineKeyboardMarkup);
    }

    @Override
    public String getBotUsername() {
        return "fly_seeker_bot";
    }
}