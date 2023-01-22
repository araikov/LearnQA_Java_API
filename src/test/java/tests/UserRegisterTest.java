package tests;

import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import lib.ApiCoreRequests;
import lib.Assertions;
import lib.BaseTestCase;
import lib.DataGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;


import java.util.HashMap;
import java.util.Map;

@Feature("Registration")

public class UserRegisterTest extends BaseTestCase {

    String cookie;
    String header;
    int userIdOnAuth;
    private final ApiCoreRequests apiCoreRequests = new ApiCoreRequests();

    @Test
    @DisplayName("Create User With Existing Email")
    @Severity(SeverityLevel.BLOCKER)
    public void testCreateUserWithExistingEmail(){
        String email = "vinkotoc@example.com";

        Map<String, String> userData = new HashMap<>();
        userData.put("email", email);
        userData = DataGenerator.getRegistrationData(userData);

        Response responseCreateAuth = RestAssured
                .given()
                .body(userData)
                .post("https://playground.learnqa.ru/api/user")
                .andReturn();

        Assertions.assertResponseCodeEquals(responseCreateAuth, 400);
        Assertions.assertResponseTextEquals(responseCreateAuth, "Users with email '" + email + "' already exists");
    }

    @Test
    @DisplayName("Create User")
    @Severity(SeverityLevel.BLOCKER)
    public void testCreateUserSuccesfully(){
        String email = DataGenerator.getRandomEmail();

        Map<String, String> userData = DataGenerator.getRegistrationData();


        Response responseCreateAuth = RestAssured
                .given()
                .body(userData)
                .post("https://playground.learnqa.ru/api/user")
                .andReturn();

        Assertions.assertResponseCodeEquals(responseCreateAuth, 200);
        Assertions.assertJsonHasField(responseCreateAuth, "id");
    }

    @Test
    @DisplayName("Create User with email without at")
    @Severity(SeverityLevel.NORMAL)
    public void testCreateUserWithoutAt(){
        String email = "vinkotocexample.com";

        Map<String, String> userData = new HashMap<>();
        userData.put("email", email);
        userData = DataGenerator.getRegistrationData(userData);

        Response responseCreateAuth = RestAssured
                .given()
                .body(userData)
                .post("https://playground.learnqa.ru/api/user")
                .andReturn();

        Assertions.assertResponseCodeEquals(responseCreateAuth, 400);
        Assertions.assertResponseTextEquals(responseCreateAuth, "Invalid email format");
    }



        @ParameterizedTest
        @DisplayName("Create user without obligate field")
        @Severity(SeverityLevel.NORMAL)
        @ValueSource(strings = {"username", "firstName", "lastName", "email", "password"})
            public void testCreateUserWithoutObligateField(String parameter){

                String email = DataGenerator.getRandomEmail();

                Map<String, String> userData = DataGenerator.getRegistrationDataWithoutObligateParameter(parameter);

            Response responseCreateAuth = apiCoreRequests
                    .makePostRegistrationWithBody(
                            "https://playground.learnqa.ru/api/user",
                            userData
                    );

                Assertions.assertResponseCodeEquals(responseCreateAuth, 400);
            Assertions.assertResponseTextEquals(responseCreateAuth, "The following required params are missed: " + parameter);

        }

    @Test
    @DisplayName("Create User With Short Name")
    @Severity(SeverityLevel.NORMAL)
    public void testCreateUserWithShortName(){
        String username = "a";

        Map<String, String> userData = new HashMap<>();
        userData.put("username", username);
        userData = DataGenerator.getRegistrationData(userData);

        Response responseCreateAuth = apiCoreRequests
                .makePostRegistrationWithBody(
                        "https://playground.learnqa.ru/api/user",
                        userData
                );

        Assertions.assertResponseCodeEquals(responseCreateAuth, 400);
        Assertions.assertResponseTextEquals(responseCreateAuth, "The value of 'username' field is too short");
    }

    @Test
    @DisplayName("Create User With Long Name")
    @Severity(SeverityLevel.NORMAL)
    public void testCreateUserWithLongName(){
        String username = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa";

        Map<String, String> userData = new HashMap<>();
        userData.put("username", username);
        userData = DataGenerator.getRegistrationData(userData);

        Response responseCreateAuth = apiCoreRequests
                .makePostRegistrationWithBody(
                        "https://playground.learnqa.ru/api/user",
                        userData
                );

        Assertions.assertResponseCodeEquals(responseCreateAuth, 400);
        Assertions.assertResponseTextEquals(responseCreateAuth, "The value of 'username' field is too long");
    }
}
