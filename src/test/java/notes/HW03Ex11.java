package notes;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.path.json.JsonPath;
import org.junit.jupiter.api.Test;
import io.restassured.http.Headers;
import io.restassured.RestAssured;
import io.restassured.response.Response;

import javax.naming.ldap.PagedResultsResponseControl;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class HW03Ex11 {

    @Test
    public void testAuthUser(){
        Response response = RestAssured
            .given()
            .get("https://playground.learnqa.ru/api/homework_cookie")
            .andReturn();


        String responseCookie = response.getCookie("HomeWork");
        //System.out.println(responseCookie);

        Map<String, String> cookies = response.getCookies();
        assertTrue(cookies.containsKey("HomeWork"),"Response doesn't have 'HomeWork' cookie");
        assertEquals("hw_value", responseCookie, "Cookie HomeWork != hw_value");
    }

}