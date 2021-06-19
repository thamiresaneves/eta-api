import models.User;
import org.testng.annotations.BeforeClass;

public class PostUsersTests extends TestBase {

    private static User validUser;
    private static User invalidUser;

    @BeforeClass
    public static void generateTestData() {
        validUser = new User("Tati", "tati@email.com", "134abc", "true");
        invalidUser = new User("Tati", "tati@email.com", "134abc", "true");
    }
}
