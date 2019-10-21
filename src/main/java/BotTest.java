import org.junit.Assert;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BotTest {

    @Test
    void getBotToken()
    {
        Bot bot = new Bot();
        Assert.assertEquals("882526722:AAHk_SAlTXS2eM6B6uGgWapn3fRWizpndUc", bot.getBotToken());
    }

    @Test
    void onUpdateReceived()
    {

    }

    @Test
    void getBotUsername()
    {
        Bot bot = new Bot();
        Assert.assertEquals("fly_seeker_bot", bot.getBotUsername());
    }
}