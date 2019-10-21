import org.json.JSONObject;
import org.junit.Assert;

import static org.junit.jupiter.api.Assertions.*;

class RequestTest {

    @org.junit.jupiter.api.Test
    void getHTML() throws Exception {
        String url = "http://httpbin.org/get";
        Request req = new Request(new String[] { "" }, url);
        String result = req.getHTML(req.request_url);
        JSONObject jsonObject = new JSONObject(result);
        result = jsonObject.getString("url");
        String expected = "https://httpbin.org/get";
        Assert.assertEquals(result, expected);
    }

    @org.junit.jupiter.api.Test
    void seekCheapestFlight()
    {

    }

    @org.junit.jupiter.api.Test
    void parserJSONCheapestFlight()
    {

    }
}