import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDate;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;

public class CalendarUtil
{

    static String[] days = new String[] {"Mon","Tue","Wed","Thu","Fri","Sat","Sun"};

    static  String[] months = new String[] {"January","February","March","April","May","June","July","August","September","October","November","December"};

    static int[] maxDays = new int[] {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
    static int[] visMaxDays = new int[] {31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};

    static LocalDate currentDate = LocalDate.now();

    public static boolean visYear (int year)
    {
        return year%4==0 && year%100 !=0 || year%400==0;
    }

//    public static List<String> origin = new ArrayList<>();
//    public static List<String> destination = new ArrayList<>();

    public static InlineKeyboardMarkup createMonth (int createdMonth, int createdYear, LocalDate date)
    {
        boolean isVis = visYear(createdYear);
        //int currentMonthDay = date.getDayOfMonth();
        //int currentWeekDay = date.getDayOfWeek();
        //int currentYear = date.getYear();

        java.time.LocalDate tmp = java.time.LocalDate.of(createdYear,createdMonth,1);
        DayOfWeek tmpWeek =  tmp.getDayOfWeek();
        int tmpWeek1 = tmpWeek.getValue();

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();


        InlineKeyboardButton leftArrow = new InlineKeyboardButton();
        leftArrow.setText("<");
        leftArrow.setCallbackData("<");

        InlineKeyboardButton buttonMonth = new InlineKeyboardButton();
        buttonMonth.setText(months[createdMonth-1] + " " + createdYear);
        buttonMonth.setCallbackData("nothing");

        InlineKeyboardButton rightArrow = new InlineKeyboardButton();
        rightArrow.setText(">");
        rightArrow.setCallbackData(">");

        List<InlineKeyboardButton> row1 = new ArrayList<>();
        row1.add(leftArrow);
        row1.add(buttonMonth);
        row1.add(rightArrow);

        List<InlineKeyboardButton> row2 = new ArrayList<>();

        for (int i=0;i<=6;i++)
        {
            InlineKeyboardButton button = new InlineKeyboardButton();
            button.setText(CalendarUtil.days[i]);
            button.setCallbackData("nothing");

            row2.add(button);
        }

        List<List<InlineKeyboardButton>> rowList= new ArrayList<>();
        rowList.add(row1);
        rowList.add(row2);

        int day = 1;

        for (int line = 0; line <= 5; line++)
        {
            List<InlineKeyboardButton> row = new ArrayList<>();

            for (int col = 0; col <= 6; col++)
            {
                String text = " ";
                InlineKeyboardButton button = new InlineKeyboardButton();

                button.setCallbackData("nothing");
                //if (!text.equals(" "))
                    //button.setCallbackData(Integer.toString(day) + "-" + createdMonth + "-" + createdYear);
                //else
                    //button.setCallbackData("nothing");

                if (isVis)
                {
                    if ((line != 0 || col + 1 >= tmpWeek1) && day <= visMaxDays[createdMonth-1])
                    {
                        text = Integer.toString(day);
//                        button.setCallbackData(Integer.toString(day) + "-" + createdMonth + "-" + createdYear);
                        button.setCallbackData(createdYear + "-" + createdMonth + "-" + Integer.toString(day));
                        day++;
                    }
                }
                else
                {
                    if ((line != 0 || col + 1 >= tmpWeek1) && day <= maxDays[createdMonth-1])
                    {
                        text = Integer.toString(day);
//                        button.setCallbackData(Integer.toString(day) + "-" + createdMonth + "-" + createdYear);
                        button.setCallbackData(createdYear + "-" + createdMonth + "-" + Integer.toString(day));
                        day++;
                    }
                }

                button.setText(text);
                row.add(button);
            }

            rowList.add(row);
        }

        inlineKeyboardMarkup.setKeyboard(rowList);

        return inlineKeyboardMarkup;
    }


}
