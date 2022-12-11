import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class HW02Ex05 {

    @Test
    public void testHelloWorld() {
        Map<String, String> params = new HashMap<>();

        JsonPath response = RestAssured
                .given()
                .queryParams(params)
                .get("https://playground.learnqa.ru/api/get_json_homework")
                .jsonPath();

        String answer = response.getString("messages[1].message");
        System.out.println(answer);
    }
}

