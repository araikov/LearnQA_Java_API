package tests;

import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import lib.ApiCoreRequests;
import lib.Assertions;
import lib.BaseTestCase;
import lib.DataGenerator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

@Feature("Edit")

public class UserEditTest extends BaseTestCase {

    String cookie;
    String header;
    int userIdOnAuth;
    private final ApiCoreRequests apiCoreRequests = new ApiCoreRequests();


    @Test
    @DisplayName("Edit user")
    @Severity(SeverityLevel.BLOCKER)
    public void testEditJustCreatedTest(){
    //GENERATE USER
        Map<String, String> userData = DataGenerator.getRegistrationData();

        JsonPath responseCreateAuth = RestAssured
                .given()
                .body(userData)
                .post("https://playground.learnqa.ru/api/user")
                .jsonPath();

        String userId = responseCreateAuth.getString("id");

        //LOGIN
        Map<String, String> authData = new HashMap<>();
        authData.put("email", userData.get("email"));
        authData.put("password", userData.get("password"));

        Response responseGetAuth = RestAssured
                .given()
                .body(authData)
                .post("https://playground.learnqa.ru/api/user/login")
                .andReturn();

        //EDIT
        String newName = "Changed Name";
        Map<String, String> editData = new HashMap<>();
        editData.put("firstName", newName);

        Response responseEditUser = RestAssured
                .given()
                .header("x-csrf-token", this.getHeader(responseGetAuth, "x-csrf-token"))
                .cookie("auth_sid", this.getCookie(responseGetAuth, "auth_sid"))
                .body(editData)
                .put("https://playground.learnqa.ru/api/user/" + userId)
                .andReturn();

        //GET
        Response responseUserData = RestAssured
                .given()
                .header("x-csrf-token", this.getHeader(responseGetAuth, "x-csrf-token"))
                .cookie("auth_sid", this.getCookie(responseGetAuth, "auth_sid"))
                .get("https://playground.learnqa.ru/api/user/" + userId)
                .andReturn();

        Assertions.asserJsonByName(responseUserData, "firstName", newName);
    }

    @Test
    @DisplayName("Edit user by not autorised user")
    @Severity(SeverityLevel.CRITICAL)
    public void testEditNotAutorised(){
        //GENERATE USER
        Map<String, String> userData = DataGenerator.getRegistrationData();

        JsonPath responseCreateAuth = apiCoreRequests
                .makePostRequestJson("https://playground.learnqa.ru/api/user", userData);


        String userId = ((JsonPath) responseCreateAuth).getString("id");


        //EDIT
        String newName = "Changed Name";
        Map<String, String> editData = new HashMap<>();
        editData.put("username", newName);


        Response responseEditUser = apiCoreRequests
                .makePutWithoutAuth("https://playground.learnqa.ru/api/user/"  + userId, editData);

        //GET


        Response responseUserData = apiCoreRequests
                .makeGetRequestWithoutTokenAndCookie("https://playground.learnqa.ru/api/user/" + userId);


        Assertions.asserJsonByName(responseUserData, "username", "learnqa");

    }

    @Test
    @DisplayName("Edit user by another user")
    @Severity(SeverityLevel.NORMAL)
    public void testEditAutorisedAnotherUser(){
        //GENERATE USER
        Map<String, String> userData = DataGenerator.getRegistrationData();

        String password_1 = userData.get("password");
        String email_1 = userData.get("email");



        JsonPath responseCreateAuth = apiCoreRequests
                .makePostRequestJson("https://playground.learnqa.ru/api/user", userData);

        //GENERATE USER2
        Map<String, String> userData2 = DataGenerator.getRegistrationData();

        String password_2 = userData2.get("password");
        String email_2 = userData2.get("email");

        JsonPath responseCreateAuth2 = apiCoreRequests
                .makePostRequestJson("https://playground.learnqa.ru/api/user", userData2);

        String userId2 = responseCreateAuth2.getString("id");


        //LOGIN
        Map<String, String> authData = new HashMap<>();
        authData.put("email", email_1);
        authData.put("password", password_1);


        Response responseGetAuth = apiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api/user/login", authData);

        //EDIT
        String newName = "Changed Name";
        Map<String, String> editData = new HashMap<>();
        editData.put("firstName", newName);

        Response responseEditUser = apiCoreRequests
                .makePutWithoutAuth("https://playground.learnqa.ru/api/user/"  + userId2, editData);

        //LOGIN
        Map<String, String> authData2 = new HashMap<>();
        authData2.put("email", email_2);
        authData2.put("password", password_2);

        Response responseGetAuth2 = apiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api/user/login", authData2);

        String header = getHeader(responseGetAuth2, "x-csrf-token");
        String cookie = getCookie(responseGetAuth2, "auth_sid");


        //GET

        Response responseUserData = apiCoreRequests
                .makeGetRequest("https://playground.learnqa.ru/api/user/" + userId2,this.getHeader(responseGetAuth2, "x-csrf-token"),this.getCookie(responseGetAuth2, "auth_sid") );

        Assertions.asserJsonByName(responseUserData, "firstName", "learnqa");
    }

    @Test
    @DisplayName("Put not valid email")
    @Severity(SeverityLevel.NORMAL)
    public void testEditEmailWithoutAdSameUser(){
        //GENERATE USER
        Map<String, String> userData = DataGenerator.getRegistrationData();

        JsonPath responseCreateAuth = apiCoreRequests
                .makePostRequestJson("https://playground.learnqa.ru/api/user", userData);


        String userId = responseCreateAuth.getString("id");


        //LOGIN
        Map<String, String> authData = new HashMap<>();
        authData.put("email", userData.get("email"));
        authData.put("password", userData.get("password"));

        Response responseGetAuth = apiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api/user/login", authData);



        //EDIT
        String newEmail = "testexample.com";
        Map<String, String> editData = new HashMap<>();
        editData.put("email", newEmail);

        Response responseEditUser = apiCoreRequests
                .makePutWithAuth("https://playground.learnqa.ru/api/user/"  + userId, editData, this.getHeader(responseGetAuth, "x-csrf-token"), this.getCookie(responseGetAuth, "auth_sid"));


        Assertions.assertResponseTextEquals(responseEditUser.andReturn(), "Invalid email format");

        //GET

        Response responseUserData = apiCoreRequests
                .makeGetRequest("https://playground.learnqa.ru/api/user/" + userId,this.getHeader(responseGetAuth, "x-csrf-token"),this.getCookie(responseGetAuth, "auth_sid") );


        Assertions.asserJsonByNameNotEquals(responseUserData, "email", newEmail);
    }

    @Test
    @DisplayName("Put not valid name")
    @Severity(SeverityLevel.NORMAL)
    public void testEditShortNameSameUser(){
        //GENERATE USER
        Map<String, String> userData = DataGenerator.getRegistrationData();

        JsonPath responseCreateAuth = apiCoreRequests
                .makePostRequestJson("https://playground.learnqa.ru/api/user", userData);


        String userId = responseCreateAuth.getString("id");

        //LOGIN
        Map<String, String> authData = new HashMap<>();
        authData.put("email", userData.get("email"));
        authData.put("password", userData.get("password"));

        Response responseGetAuth = apiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api/user/login", authData);



        //EDIT
        String shortName = "a";
        Map<String, String> editData = new HashMap<>();
        editData.put("firstName", shortName);

        Response responseEditUser = apiCoreRequests
                .makePutWithAuth("https://playground.learnqa.ru/api/user/"  + userId, editData, this.getHeader(responseGetAuth, "x-csrf-token"), this.getCookie(responseGetAuth, "auth_sid"));


        Assertions.assertResponseTextEquals(responseEditUser.andReturn(), "{\"error\":\"Too short value for field firstName\"}");

        //GET

        Response responseUserData = apiCoreRequests
                .makeGetRequest("https://playground.learnqa.ru/api/user/" + userId,this.getHeader(responseGetAuth, "x-csrf-token"),this.getCookie(responseGetAuth, "auth_sid") );


        Assertions.asserJsonByNameNotEquals(responseUserData, "email", shortName);
    }

}
