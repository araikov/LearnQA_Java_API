import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import java.time.Duration;

import java.util.HashMap;
import java.util.Map;

public class HW02Ex08 {

    @Test
    public void testHelloWorld() {

        JsonPath response = RestAssured
                .given()
                .get("https://playground.learnqa.ru/ajax/api/longtime_job")
                .jsonPath();

        String token = response.getString("token");
        int seconds = response.getInt("seconds");

        System.out.println(token);
        System.out.println(seconds);

        Map<String, String> params = new HashMap<>();
        params.put("token",token);

        Response responseTokenNotReady = RestAssured
                .given()
                .queryParams(params)
                .get("https://playground.learnqa.ru/ajax/api/longtime_job")
                .andReturn();

        responseTokenNotReady.print();


        try {
        Thread.sleep(seconds * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Response responseToken = RestAssured
                .given()
                .queryParams(params)
                .get("https://playground.learnqa.ru/ajax/api/longtime_job")
                .andReturn();

        responseToken.print();
    }
}
