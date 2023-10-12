package Test;
import java.io.ByteArrayInputStream;

import Database.Database_management;
import Database.Database_management.*;

import Management.AccountManager;
import  org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.io.PrintStream;
import java.io.ByteArrayOutputStream;


// make all tests
@TestMethodOrder(MethodOrderer.MethodName.class)
public class AccountTest {
    private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private ByteArrayInputStream inputStream;
    private static AccountManager account;



    @BeforeAll
    public static void setUp() {
        //account = new AccountManager();
    }

    @Test
    public void registrationTestA() {
        //BSoftAssertions softly = new SoftAssertions();
        String simulatedUserInput = "2\nName\nSurname\nemail@email\npassword\n123\n3\n";

        System.setIn(new ByteArrayInputStream(simulatedUserInput.getBytes()));

        account = new AccountManager();
        assertDoesNotThrow(() -> account.startMenu());
    }

    @Test
    public void registrationTestB() {
        // tests if inserting another time the same email makes the program doesn't accept it
        String simulatedUserInput = "2\nName\nSurname\ntest1@email\npassword\n123\ntest2@email\n3\n";
        System.setIn(new ByteArrayInputStream(simulatedUserInput.getBytes()));
        account = new AccountManager();
        assertDoesNotThrow(() -> account.startMenu());

    }

    @AfterAll void tearDown(){
        Database_management db = new Database_management();
        db.deleteTestClient("email@email");
        db.deleteTestClient("test2@email");
    }


}

