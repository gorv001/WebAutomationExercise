import org.testng.annotations.DataProvider;

import java.util.Random;

public class TestData {
    public static  String email = "user" + new Random().nextInt(1000) + "@test.com";
    @DataProvider(name = "registrationData")
    public Object[][] getRegistrationData() {
        return new Object[][] {
                {"GouravKumar", email, "password123"},
//                {"Sourav kumar", "sorv.smith@example.com", "securePass"},
//                {"Vikas Panchal", "vikas.test@example.com", "test@1234"}
        };
    }
}
