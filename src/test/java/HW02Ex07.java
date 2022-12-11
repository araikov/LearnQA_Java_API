import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

public class HW02Ex07 {

    @Test
    public void testHelloWorld() {


        String link = "https://playground.learnqa.ru/api/long_redirect";
        int statusCode;
        do {
            Response response = RestAssured
                    .given()
                    .redirects()
                    .follow(false)
                    .get(link);
            //     .andReturn();

            // response.prettyPrint();

            String locationHeader = response.getHeader("Location");
            System.out.println(locationHeader);
            link = locationHeader;

            statusCode = response.getStatusCode();
            //System.out.println(statusCode);

        } while (statusCode == 301);

    }
}
