import org.json.JSONObject;
import org.junit.Assert;

import java.util.ArrayList;

class RequestTest {

    @org.junit.jupiter.api.Test
    void testGetHTTP() throws Exception
    {
        String url = "http://httpbin.org/get";
        Request req = new Request(new String[] { "" }, url);
        String result = req.getHTTP(req.request_url);
        JSONObject jsonObject = new JSONObject(result);
        result = jsonObject.getString("url");
        String expected = "https://httpbin.org/get";
        Assert.assertEquals(result, expected);
    }

    @org.junit.jupiter.api.Test
    void testSeekCheapestFlight()
    {
        Request req = new Request(new String[] { "/seekCheapestFlight", "SVX", "MOW", "2000-01" }, "http://api.travelpayouts.com/v1/prices/direct?");

        SqliteDB connection = new SqliteDB();

        req.seekCheapestFlight(connection, false);

        String expected = "Sorry, but there are no results for your search.\n" +
                "Check your input information. Maybe you entered the wrong date?";

        Assert.assertEquals(expected, req.reply);

        req = new Request(new String[] { "/seekCheapestFlight", "SVX", "MOW", "2000" }, "http://api.travelpayouts.com/v1/prices/direct?");

        connection = new SqliteDB();

        req.seekCheapestFlight(connection, false);

        expected = "Please, can you write your request right?\n" +
                "If you don't know how, you can use /seekCheapestFlight for check some example or read a documentation.";

        Assert.assertEquals(expected, req.reply);

    }

    @org.junit.jupiter.api.Test
    void testParserJSONCheapestFlight()
    {
        Request req = new Request(new String[] { "" }, "");

        req.des = "NYC";

        String testJSON = "{" +
                "\"success\":true," +
                "\"data\":" +
                    "{" +
                        "\"NYC\":" +
                            "{" +
                                "\"0\":" +
                                    "{" +
                                        "\"price\":27928," +
                                        "\"airline\":\"SU\"," +
                                        "\"flight_number\":102," +
                                        "\"departure_at\":\"2019-11-11T15:05:00Z\"," +
                                        "\"return_at\":\"2019-12-03T13:25:00Z\"," +
                                        "\"expires_at\":\"2019-10-15T08:50:07Z\"" +
                                    "}," +
                                "\"1\":" +
                                    "{" +
                                        "\"price\":22113," +
                                        "\"airline\":\"AF\"," +
                                        "\"flight_number\":8278," +
                                        "\"departure_at\":\"2019-11-21T06:50:00Z\"," +
                                        "\"return_at\":\"2019-12-05T18:40:00Z\"," +
                                        "\"expires_at\":\"2019-10-15T13:55:30Z\"" +
                                    "}" +
                            "}"+
                    "},"+
                "\"error\":null," +
                "\"currency\":\"rub\"" +
                "}";

        req.parserJSONCheapestFlight(testJSON);

        int expPrice = 27928;
        String expAirline = "SU";
        int expFlightNumber = 102;
        String expDepartureAt = "2019-11-11T15:05:00Z";
        String expReturnAt = "2019-12-03T13:25:00Z";
        String expExpiresAt = "2019-10-15T08:50:07Z";
        String expCurrency = "rub";

        String expected = "Price: " + expPrice + " " + expCurrency + "\n" +
                "Airline: " + expAirline + "\n" +
                "FlightNumber: " + expFlightNumber + "\n" +
                "Departure at: " + expDepartureAt + "\n" +
                "Return at: " + expReturnAt + "\n" +
                "Expires at: " + expExpiresAt + "\n" +
                "http://pics.avs.io/200/200/" + expAirline + ".png";

        Assert.assertEquals(expected, req.reply);
    }
}