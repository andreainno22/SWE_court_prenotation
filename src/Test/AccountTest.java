package Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import Management.AccountManager;
import  org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.engine.*;


// make all tests
public class AccountTest {
    private static AccountManager account;

    @BeforeAll
    public static void setUp() {
        account = new AccountManager();
    }

    @Test
    public void registrationTest() {
        String simulatedUserInput = "Name" + System.getProperty("line.separator")
                + "Surname" + System.getProperty("line.separator") +
                "Email" + System.getProperty("line.separator") +
                "Password" + System.getProperty("line.separator") +
                "123" + System.getProperty("line.separator");

        InputStream savedStandardInputStream = System.in;
        System.setIn(new ByteArrayInputStream(simulatedUserInput.getBytes()));
        System.setIn(savedStandardInputStream);

        assertDoesNotThrow(() -> account.registerAccount());
    }
}

