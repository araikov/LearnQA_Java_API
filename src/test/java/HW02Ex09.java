import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class HW02Ex09 {

    @Test
    public void testHelloWorld() {

        Response responseForCheck;
        Response response;
        String result = "You are NOT authorized";
        String passwordFromList = "test";
        int i = 0;
        String[] arrayPassword = new String[82];
        arrayPassword[i] = "123456";
        arrayPassword[i++] = "123456789";
        arrayPassword[i++] = "qwerty";
        arrayPassword[i++] = "password";
        arrayPassword[i++] = "1111111";
        arrayPassword[i++] = "12345678";
        arrayPassword[i++] = "abc123";
        arrayPassword[i++] = "1234567";
        arrayPassword[i++] = "password1";
        arrayPassword[i++] = "12345";
        arrayPassword[i++] = "1234567890";
        arrayPassword[i++] = "123123";
        arrayPassword[i++] = "0";
        arrayPassword[i++] = "Iloveyou";
        arrayPassword[i++] = "1234";
        arrayPassword[i++] = "1q2w3e4r5t";
        arrayPassword[i++] = "Qwertyuiop";
        arrayPassword[i++] = "123";
        arrayPassword[i++] = "Monkey";
        arrayPassword[i++] = "Dragon";
        arrayPassword[i++] = "12345679";
        arrayPassword[i++] = "111111";
        arrayPassword[i++] = "987654321";
        arrayPassword[i++] = "mynoob";
        arrayPassword[i++] = "123321";
        arrayPassword[i++] = "666666";
        arrayPassword[i++] = "18atcskd2w";
        arrayPassword[i++] = "7777777";
        arrayPassword[i++] = "1q2w3e4r";
        arrayPassword[i++] = "654321";
        arrayPassword[i++] = "555555";
        arrayPassword[i++] = "3rjs1la7qe";
        arrayPassword[i++] = "google";
        arrayPassword[i++] = "123qwe";
        arrayPassword[i++] = "zxcvbnm";
        arrayPassword[i++] = "1q2w3e";
        arrayPassword[i++] = "letmein";
        arrayPassword[i++] = "baseball";
        arrayPassword[i++] = "trustno1";
        arrayPassword[i++] = "sunshine";
        arrayPassword[i++] = "master";
        arrayPassword[i++] = "welcome";
        arrayPassword[i++] = "shadow";
        arrayPassword[i++] = "ashley";
        arrayPassword[i++] = "football";
        arrayPassword[i++] = "jesus";
        arrayPassword[i++] = "michael";
        arrayPassword[i++] = "ninja";
        arrayPassword[i++] = "mustang";
        arrayPassword[i++] = "bailey";
        arrayPassword[i++] = "passw0rd";
        arrayPassword[i++] = "superman";
        arrayPassword[i++] = "qazwsx";
        arrayPassword[i++] = "access";
        arrayPassword[i++] = "696969";
        arrayPassword[i++] = "batman";
        arrayPassword[i++] = "adobe123[a]";
        arrayPassword[i++] = "admin";
        arrayPassword[i++] = "photoshop[a]";
        arrayPassword[i++] = "princess";
        arrayPassword[i++] = "azerty";
        arrayPassword[i++] = "qwerty123";
        arrayPassword[i++] = "lovely";
        arrayPassword[i++] = "888888";
        arrayPassword[i++] = "aa12345678";
        arrayPassword[i++] = "password123";
        arrayPassword[i++] = "!@#$%^&*";
        arrayPassword[i++] = "charlie";
        arrayPassword[i++] = "aa123456";
        arrayPassword[i++] = "donald";
        arrayPassword[i++] = "login";
        arrayPassword[i++] = "starwars";
        arrayPassword[i++] = "hello";
        arrayPassword[i++] = "freedom";
        arrayPassword[i++] = "whatever";
        arrayPassword[i++] = "solo";
        arrayPassword[i++] = "121212";
        arrayPassword[i++] = "flower";
        arrayPassword[i++] = "hottie";
        arrayPassword[i++] = "loveme";
        arrayPassword[i++] = "zaq1zaq1";
        arrayPassword[i++] = "1qaz2wsx";




        for (i = 0; i < arrayPassword.length; i++) {

            Map<String, Object> data = new HashMap<>();
            data.put("login", "super_admin");
            data.put("password", arrayPassword[i]);

            response = RestAssured
                    .given()
                    .body(data)
                    .post("https://playground.learnqa.ru/ajax/api/get_secret_password_homework")
                    .andReturn();

          // response.print();
            String responseCookie = response.getCookie("auth_cookie");

           /* Map<String, String> cookies = new HashMap<>();
            if (responseCookie != null) {
                cookies.put("auth_cookie", responseCookie);
            }
            System.out.println(cookies);
*/
            responseForCheck = RestAssured
                    .given()
                    .body(data)
                    .cookies("auth_cookie", responseCookie)
                    .when()
                    .post("https://playground.learnqa.ru/ajax/api/check_auth_cookie")
                    .andReturn();

           // responseForCheck.print();

           result = responseForCheck.asString();

            if (!result.equals("You are NOT authorized"))
                System.out.println(arrayPassword[i] + "\n" + result);
                else {}

        }

    }
}
