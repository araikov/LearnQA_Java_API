package notes;

import io.restassured.RestAssured;
import io.restassured.http.Headers;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class HW03Ex12 {

    @Test
    public void testAuthUser(){
        Response response = RestAssured
                .given()
                .get("https://playground.learnqa.ru/api/homework_header")
                .andReturn();


        Headers headers = response.getHeaders();
        String xSecretHomeworkHeader = response.getHeader("x-secret-homework-header");

        assertTrue(headers.hasHeaderWithName("x-secret-homework-header"), "Response doesn't have 'x-secret-homework-header' headers");
        assertEquals("Some secret value", xSecretHomeworkHeader, "Cookie 'x-secret-homework-header' != 'Some secret value'");
    }

}
