import io.restassured.response.Response;
import models.User;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static org.hamcrest.Matchers.*;


public class DeleteUserTests extends TestBase {

    private static User user;

    @BeforeClass
    public static void generateTestData() {
        user = new User("Wesley", "wesley@teste.com", "123test", "true");
        user.registerUserRequest();
    }

    @AfterClass
    public void removeTestData() {
        user.deleteUserRequest(spec);
    }

    @Test
    public void shouldRemoveUserAndReturnMessageAndStatus200() {
        Response deleteUserResponse = user.deleteUserRequest(spec);
        deleteUserResponse.
                then().
                    assertThat().
                    statusCode(200).
                    body("message", equalTo("Registro excluído com sucesso"));
    }
}
