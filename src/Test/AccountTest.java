package Test;

import java.io.ByteArrayInputStream;

import Context.Client;
import Database.Database_management;
import Database.Database_management.*;

import Management.AccountManager;
import org.junit.jupiter.api.*;
import org.junitpioneer.jupiter.DisableIfTestFails;

import javax.xml.crypto.Data;

import static org.junit.jupiter.api.Assertions.*;

import java.io.PrintStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;


// make all tests
@DisableIfTestFails
@TestMethodOrder(MethodOrderer.MethodName.class)
public class AccountTest {
    private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private ByteArrayInputStream inputStream;
    private static AccountManager account;


    @BeforeAll
    public static void setUp() {

    }

    @Test
    public void TestARegistration() {
        String simulatedUserInput = "2\nName\nSurname\ntest1@email\npassword\n123\n3\n";

        System.setIn(new ByteArrayInputStream(simulatedUserInput.getBytes()));

        account = new AccountManager();
        assertDoesNotThrow(() -> account.startMenu());
    }

    @Test
    public void TestBRegistration() {
        // tests if inserting another time the same email makes the program doesn't accept it
        String simulatedUserInput = "2\nName\nSurname\ntest1@email\npassword\n123\ntest2@email\n3\n";
        System.setIn(new ByteArrayInputStream(simulatedUserInput.getBytes()));
        account = new AccountManager();
        assertDoesNotThrow(() -> account.startMenu());
    }

    @Test   // login
    public void TestCLogin() {
        String simulatedInput = "1\ntest1@email\npassword\n7\n3\n";
        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));
        account = new AccountManager();
        assertDoesNotThrow(() -> account.startMenu());
    }

    @Test
    public void TestDAddMoney() {
        String simulatedInput = "1\ntest1@email\npassword\n4\ny\n100\n7\n3\n";
        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));
        account = new AccountManager();
        assertDoesNotThrow(() -> account.startMenu());
    }

    @Test
    public void TestEMakeReservation() {
        String simulatedInput = "1\ntest1@email\npassword\n1\n2025-01-01\n1\n2\n1\n0\n7\n3\n";
        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));
        account = new AccountManager();
        assertDoesNotThrow(() -> account.startMenu());
    }

    @Test
    public void TestFDeleteReservation() {
        Database_management db = new Database_management();
        int testClientId = db.getTestClientIdByMail("test1@email");
        assertNotEquals(0, testClientId);
        ArrayList<Integer> reservationsId = db.getReservationsId(testClientId);
        int reservationId = reservationsId.get(0);
        String simulatedInput = "1\ntest1@email\npassword\n2\n" + reservationId + "\n7\n3\n";
        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));
        account = new AccountManager();
        assertDoesNotThrow(() -> account.startMenu());
    }

    @AfterAll
    static void tearDown() {
        Database_management db = new Database_management();
        db.deleteTestClient("test1@email");
        db.deleteTestClient("test2@email");
    }

}

