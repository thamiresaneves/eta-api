import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import models.User;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import static org.hamcrest.Matchers.*;

import static io.restassured.RestAssured.given;

public class PostLoginTests extends TestBase{

    private static User user;
    private static User invalidUser;

    @BeforeClass
    public static void generateTestData() {
        user = new User("Maria", "maria@gmail.com", "123abc", "true");
        user.registerUserRequest();

        invalidUser = new User ("Maria", "maria@gmail.com", "aaaaa", "true");
        invalidUser.registerUserRequest();
    }

    @Test
    public void shouldReturnSuccessMessageAuthTokenAndStatus200() {
        given().
                spec(spec).
                header("Content-Type", "application/json").
        and().
                body(user.getUserCredentials()).
        when().
                post("login").
        then().assertThat().
                statusCode(200).
                body("message", equalTo("Login realizado com sucesso")).
                body("authorization", notNullValue());
    }

    @Test
    public void shouldReturnSuccessMessageAuthTokenAndStatus200_2() {
        Response loginResponse = user.authenticateUser();
        loginResponse.then().assertThat().
                statusCode(200).
                body("message", equalTo("Login realizado com sucesso"));
    }

    @Test
    public void shouldReturnErrorMessageAndStatus401(){
        given().
                spec(spec).
                header("Content-Type", "application/json").
        and().
                body(invalidUser.getUserCredentials()).
        when().
                post("login").
        then().
                assertThat().
                statusCode(401).
                body("message", equalTo("Email e/ou senha inválidos")).
                body("authorization", nullValue());
    }

    @Test
    public void shouldReturnSuccessAuthTokenAutomatically() {
        String credentials = user.getUserCredentials();

        Response loginResponse = given().
                                        spec(spec).
                                        header("Content-Type", "application/json").
                                and().
                                        body(credentials).
                                when().
                                        post("login");

        JsonPath jsonPathEvaluator = loginResponse.jsonPath();
        String authToken = jsonPathEvaluator.get("authorization");
        user.setUserAuthToken(authToken);
    }

}
