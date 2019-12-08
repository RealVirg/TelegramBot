
import org.joda.time.LocalDate;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Bot extends TelegramLongPollingBot {
//    private List<String> origin = new ArrayList<>();
//    private List<String> destination = new ArrayList<>();
//    private String currency = "RUB";
//    private LocalDate currentDate = LocalDate.now();
//    private int calendarMonth = currentDate.getMonthOfYear();
//    private int calendarYear = currentDate.getYear();
    private HashMap<Long, List<String>> usersOrigin = new HashMap<Long, List<String>>();
    private HashMap<Long, List<String>> usersDestination = new HashMap<Long, List<String>>();
    private HashMap<Long, String> usersCurrency = new HashMap<Long, String>();
    private HashMap<Long, Integer> usersMonth = new HashMap<Long, Integer>();
    private HashMap<Long, Integer> usersYear = new HashMap<Long, Integer>();


    @Override
    public String getBotToken() {
        return "882526722:AAHk_SAlTXS2eM6B6uGgWapn3fRWizpndUc";
    }

    @Override
    public void onUpdateReceived(Update update) {
        try {
            if (update.hasMessage() && update.getMessage().hasText()) {
                Long id = update.getMessage().getChatId();
                SqliteDB conn = new SqliteDB();
                conn.CreateLogTableDay();
                conn.CreateLogTableMonth();
                if (conn.co != null)
                {
                    try
                    {
                        conn.co.close();
                    }
                    catch (SQLException e)
                    {
                        e.printStackTrace();
                    }
                }

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
                    SqliteDB db = new SqliteDB();
                    try {
                        LocalDate currentDate = LocalDate.now();
                        int calendarMonth = currentDate.getMonthOfYear();
                        int calendarYear = currentDate.getYear();
                        usersMonth.put(id, calendarMonth);
                        usersYear.put(id, calendarYear);
                        usersOrigin.put(id, db.getCode(oddr[1]));
                        //origin = db.getCode(oddr[1]);
                        usersDestination.put(id, db.getCode(oddr[2]));
                        //destination = db.getCode(oddr[2]);
                        usersCurrency.put(id, oddr[3]);
                        //currency = oddr[3];
//                        Request r = new Request(oddr, "http://api.travelpayouts.com/v1/prices/direct?");
//                        r.seekCheapestFlight(conn, true);
//                        outMessage.setText(r.reply);
                        try {
                            execute(CalendarUtil.sendInlineKeyBoardMessage(update.getMessage().getChatId(),
                                    usersMonth.get(id), usersYear.get(id)));
                        }
                        catch (Exception e) {
                            e.printStackTrace();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    finally
                    {
                        if (db.co != null)
                        {
                            try
                            {
                                conn.co.close();
                            }
                            catch (SQLException e)
                            {
                                e.printStackTrace();
                            }
                        }
                    }
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
//                else if (oddr[0].equals("/calendar"))
//                {
//                    try {
//                        execute(CalendarUtil.sendInlineKeyBoardMessage(update.getMessage().getChatId(),
//                                CalendarUtil.currentDate.getMonthOfYear(), CalendarUtil.currentDate.getYear()));
//
//                    }
//                    catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }

                else {
                    outMessage.setText("I can't understand you.\nPlease use /help for check commands.");
                    execute(outMessage);
                }
            }
            else if (update.hasCallbackQuery())
            {
                Long id = update.getCallbackQuery().getMessage().getChatId();
                try {
                    if (!update.getCallbackQuery().getData().equals("nothing") &&
                              !update.getCallbackQuery().getData().equals("<") &&
                              !update.getCallbackQuery().getData().equals(">"))
                    {
                        Request r = new Request("http://api.travelpayouts.com/v1/prices/direct?",
                                usersOrigin.get(id), usersDestination.get(id),
                                update.getCallbackQuery().getData(),
                                usersCurrency.get(id));
                        execute(new SendMessage().setText(r.seekCheapestFlight()).setChatId(update.getCallbackQuery().getMessage().getChatId()));
                    }
                    else if (update.getCallbackQuery().getData().equals("<"))
                    {
                        usersMonth.put(id, usersMonth.get(id) - 1);
                        if (usersMonth.get(id) == 0)
                        {
//                            calendarYear--;
//                            calendarMonth = 12;
                            usersYear.put(id, usersYear.get(id) - 1);
                            usersMonth.put(id, 12);
                        }
                        execute(CalendarUtil.sendInlineKeyBoardMessage(update.getCallbackQuery().getMessage().getChatId(),
                                usersMonth.get(id), usersYear.get(id)));
                    }
                    else if (update.getCallbackQuery().getData().equals(">"))
                    {
                        usersMonth.put(id, usersMonth.get(id) + 1);
                        if (usersMonth.get(id) == 13)
                        {
                            usersYear.put(id, usersYear.get(id) + 1);
                            usersMonth.put(id, 1);
                        }
                        execute(CalendarUtil.sendInlineKeyBoardMessage(update.getCallbackQuery().getMessage().getChatId(),
                                usersMonth.get(id), usersYear.get(id)));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
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