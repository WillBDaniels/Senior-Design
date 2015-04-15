package webapptest;

import com.google.gson.Gson;
import org.junit.After;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author William
 */
public class WebAppTestTest {

    public WebAppTestTest() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of postToServer method, of class WebAppTest.
     */
    @Test
    public void testPostToServer() {
        System.out.println("postToServer");
        DataPOJO data = new DataPOJO();
        data.setUsername("willtest");
        data.setPasswordHash(WebAppTest.MD5("ShiftyTestPassword"));
        Gson gson = new Gson();
        Type type = Type.SHIFTY;
        Action action = Action.GETMOBILEINFO;
        String json = gson.toJson(data, DataPOJO.class);
        String expResult = "";
        String result = WebAppTest.postToServer(type, action, json);
        System.out.println("this was the result: " + result);
        assertTrue(!result.isEmpty() && !result.contains("403"));

    }

}
