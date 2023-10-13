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
import java.sql.Date;
import java.util.ArrayList;


// make all tests
@DisableIfTestFails
@TestMethodOrder(MethodOrderer.MethodName.class)
public class AccountTest {
    private static AccountManager account;
    private String simulatedUserInput;
    private Client client;

    public void setClient(Client client) {
        this.client = client;
    }

    public void setUp() {
        System.setIn(new ByteArrayInputStream(simulatedUserInput.getBytes()));
        account = new AccountManager();
        assertDoesNotThrow(() -> account.startMenu());
    }

    @Test
    public void TestARegistration() {
        simulatedUserInput = "2\nName\nSurname\ntest1@email\npassword\n123\n3\n";
        Database_management db = new Database_management();
        Client client = db.getClient("test1@email", "password");
        setClient(client);
        setUp();
    }

    @Test
    public void TestBRegistrationWithAlreadyUsedEmail() {
        // tests if inserting another time the same email makes the program doesn't accept it
        simulatedUserInput = "2\nName\nSurname\ntest1@email\npassword\n123\n0\n3\n";
        setUp();
    }

    @Test
    public void TestCRegistrationInvalidEmail() {
        // tests if inserting another time the same email makes the program doesn't accept it
        simulatedUserInput = "2\nName\nSurname\nemail\npassword\n123\n0\n3\n";
        setUp();
    }

    @Test   // login
    public void TestDLogin() {
        simulatedUserInput = "1\ntest1@email\npassword\n7\n3\n";
        setUp();
    }

    @Test
    public void TestEAddMoney() {
        simulatedUserInput = "1\ntest1@email\npassword\n4\ny\n100\n7\n3\n";
        setUp();

    }

    @Test
    public void TestGUpgradePremium() {
        simulatedUserInput = "1\ntest1@email\npassword\n5\ny\n7\n3\n";
        setUp();
    }

    @Test
    public void TestHMakeReservationPremium() {
        simulatedUserInput = "1\ntest1@email\npassword\n1\n2025-01-02\n1\n2\n2\n0\n7\n3\n";
        setUp();
    }

    @Test
    public void TestIMakeReservationOnHoliday() {
        String date = "2025-01-01";
        simulatedUserInput = "1\ntest1@email\npassword\n1\n" + date + "\n7\n3\n";
        setUp();
        Database_management db = new Database_management();
        assertFalse(db.checkTestReservation(client, Date.valueOf(date)));
    }

    @Test
    public void TestLDeleteReservation() {
        Database_management db = new Database_management();
        /*int testClientId = db.getTestClientIdByMail("test1@email");
        assertNotEquals(0, testClientId);*/
        ArrayList<Integer> reservationsId = db.getReservationsId(client.getId());
        int reservationId = reservationsId.get(0);
        simulatedUserInput = "1\ntest1@email\npassword\n2\n" + reservationId + "\n7\n3\n";
        setUp();
        int premiumReservationId = reservationsId.get(1);
        simulatedUserInput = "1\ntest1@email\npassword\n2\n" + premiumReservationId + "\n7\n3\n";
        setUp();
    }

    @AfterAll
    static void tearDown() {
        Database_management db = new Database_management();
        db.deleteTestClient("test1@email");
    }

}

