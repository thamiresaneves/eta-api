import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import models.User;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import static org.hamcrest.Matchers.*;

import static io.restassured.RestAssured.*;

public class GetUserTests extends TestBase {

    private static User validUser1;
    private static User validUser2;
    private static User validUser3;
    private static User invalidUser1;

    @BeforeClass
    public static void generateTestData() {
        validUser1 = new User("Cicrano", "cicrano@email.com", "123abc", "true");
        validUser1.registerUserRequest();
        validUser2 = new User("Joao", "joao@email.com", "123abc", "false");
        validUser2.registerUserRequest();
        validUser3 = new User("Cicrano", "cicranosilva@email.com", "lalala", "false");
        validUser3.registerUserRequest();
        invalidUser1 = new User("Pedro", "pedro@email.com", "minhasenha", "0");
    }

    @DataProvider(name = "userData")
    public Object[][] createTestData() {
        return new Object[][] {
                { "?nome=" + validUser1.name, 2 },
                { "?password=" + validUser2.password, 2 },
                { "?email=" + validUser3.email, 1 },
                { "?nome=" + invalidUser1.name, "&email=" + invalidUser1.email, 0 }
        };
    }

    @Test(dataProvider = "userData")
    public void test_shouldReturnUsersAndStatus200(String query, int totalUsers) {
        given().
                spec(spec).
        when().
                get("usuarios" + query).
        then().
                assertThat().
                statusCode(200).
                body("quantidade", equalTo(totalUsers));
    }

    @Test(dataProvider = "userData")
    public void test_shouldReturnAllUsersAndStatus200(String query, int totalUsers) {
        given().
                spec(spec).
        when().
                get("usuarios").
        then().
                assertThat().
                statusCode(200).
                body("quantidade", equalTo(totalUsers));
    }
}
