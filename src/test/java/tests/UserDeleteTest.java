package tests;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import lib.ApiCoreRequests;
import lib.Assertions;
import lib.BaseTestCase;
import lib.DataGenerator;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class UserDeleteTest extends BaseTestCase {

    String cookie;
    String header;
    int userIdOnAuth;
    private final ApiCoreRequests apiCoreRequests = new ApiCoreRequests();


    @Test
    public void testDeleteProtectUser() {


        Map<String, String> authData = new HashMap<>();
        authData.put("email", "vinkotov@example.com");
        authData.put("password", "1234");

        Response responseGetAuth = apiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api/user/login", authData);

        this.cookie = this.getCookie(responseGetAuth, "auth_sid");
        this.header = this.getHeader(responseGetAuth, "x-csrf-token");

        Response responseDeleteAuth = apiCoreRequests
                .makeDeleteRequest(
                        "https://playground.learnqa.ru/api/user/2",
                        header,
                        cookie);

        Assertions.assertResponseCodeEquals(responseDeleteAuth, 400);
    }

        @Test
        public void testDeleteUser(){

        //GENERATE USER
        Map<String, String> userData = DataGenerator.getRegistrationData();

            JsonPath responsePostAuth = apiCoreRequests
                    .makePostRequestJson("https://playground.learnqa.ru/api/user", userData);


        String userId = responsePostAuth.getString("id");

        //LOGIN
        Map<String, String> authData = new HashMap<>();
        authData.put("email", userData.get("email"));
        authData.put("password", userData.get("password"));

            Response responseGetAuth = apiCoreRequests
                    .makePostRequest("https://playground.learnqa.ru/api/user/login", authData);

            this.cookie = this.getCookie(responseGetAuth, "auth_sid");
            this.header = this.getHeader(responseGetAuth, "x-csrf-token");

        //DELETE
            Response responseDeleteAuth = apiCoreRequests
                    .makeDeleteRequest(
                            "https://playground.learnqa.ru/api/user/" + userId,
                            header,
                            cookie);



        //GET
        Response responseUserData = apiCoreRequests
                .makeGetRequest(
                        "https://playground.learnqa.ru/api/user/" + userId,
                        header,
                        cookie);


            Assertions.assertResponseTextEquals(responseUserData, "User not found");
        Assertions.assertResponseCodeEquals(responseUserData, 404);

    }

    @Test
    public void testDeleteByAnotherUser(){

        //GENERATE USER
        Map<String, String> userData = DataGenerator.getRegistrationData();

        JsonPath responsePostAuth = apiCoreRequests
                .makePostRequestJson("https://playground.learnqa.ru/api/user", userData);

        String userIdNewUser = responsePostAuth.getString("id");


        //LOGIN
        Map<String, String> authData = new HashMap<>();
        authData.put("email", "vinkotov@example.com");
        authData.put("password", "1234");

        Response responseGetAuth = apiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api/user/login", authData);

        cookie = this.getCookie(responseGetAuth, "auth_sid");
        header = this.getHeader(responseGetAuth, "x-csrf-token");

        //DELETE

        Response responseDeleteAuth = apiCoreRequests
                .makeDeleteRequest(
                        "https://playground.learnqa.ru/api/user/" + userIdNewUser,
                        header,
                        cookie);


        //GET
        Response responseUserData = apiCoreRequests
                .makeGetRequest(
                        "https://playground.learnqa.ru/api/user/" + userIdNewUser,
                        header,
                        cookie);



        Assertions.assertResponseTextEquals(responseUserData, "{\"username\":\"learnqa\"}");
        Assertions.assertResponseCodeEquals(responseUserData, 200);

    }
}