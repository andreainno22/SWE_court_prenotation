package Test;

import java.io.ByteArrayInputStream;

import ApplicationLayer.GraphicInterfaceManager;
import Context.Client;
import Database.ClientDaoImpl;
import Database.ReservationDaoImpl;
import Management.AccountManager;
import org.junit.jupiter.api.*;
import org.junitpioneer.jupiter.DisableIfTestFails;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Date;
import java.util.ArrayList;


// make all tests
@DisableIfTestFails
@TestMethodOrder(MethodOrderer.MethodName.class)
public class AccountTest {
    //private static AccountManager account;
    private static GraphicInterfaceManager gim;
    private String simulatedUserInput;
    private static Client testClient;

    public void setUp() {
        System.setIn(new ByteArrayInputStream(simulatedUserInput.getBytes()));
        //account = new AccountManager();
        //assertDoesNotThrow(() -> account.startMenu());
        gim = new GraphicInterfaceManager();
        assertDoesNotThrow(() -> gim.startMenu());
    }

    @Test
    public void TestARegistration() {
        simulatedUserInput = "2\nName\nSurname\ntest1@email\npassword\n123\n3\n";
        ClientDaoImpl clientDao = new ClientDaoImpl();
        setUp();
        Client client = clientDao.getClient("test1@email", "password");
        assertNotNull(client);
        testClient = client;
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
    public void TestEMakeReservationWithoutMoney() {
        simulatedUserInput = "1\ntest1@email\npassword\n1\n2025-01-02\n1\n2\n3\n0\n7\n3\n";
        setUp();
    }

    @Test
    public void TestFAddMoney() {
        simulatedUserInput = "1\ntest1@email\npassword\n4\ny\n150\n7\n3\n";
        setUp();

    }

    @Test
    public void TestGMakeReservationStandard() {
        simulatedUserInput = "1\ntest1@email\npassword\n1\n2025-01-02\n1\n2\n3\n0\n7\n3\n";
        setUp();
    }

    @Test
    public void TestHMakeReservationStandardRentingKit() {
        simulatedUserInput = "1\ntest1@email\npassword\n1\n2025-01-02\n2\n2\n3\n1\n7\n3\n";
        setUp();
    }

    @Test
    public void TestIUpgradePremium() {
        simulatedUserInput = "1\ntest1@email\npassword\n5\ny\n7\n3\n";
        setUp();
    }


    @Test
    public void TestJMakeReservationPremium() {
        simulatedUserInput = "1\ntest1@email\npassword\n1\n2025-01-02\n1\n2\n2\n0\n7\n3\n";
        setUp();
    }

    @Test
    public void TestKMakeReservationPremiumRentingKit() {
        simulatedUserInput = "1\ntest1@email\npassword\n1\n2025-01-02\n3\n2\n3\n1\n7\n3\n";
        setUp();
    }

    @Test
    public void TestLMakeReservationOnHoliday() {
        String date = "2025-01-01";
        simulatedUserInput = "1\ntest1@email\npassword\n1\n" + date + "\n7\n3\n";
        setUp();
        ReservationDaoImpl reservationDao = new ReservationDaoImpl();
        assertFalse(reservationDao.checkTestReservation(testClient, Date.valueOf(date)));
    }

    @Test
    public void TestMDeleteReservation() {
        ReservationDaoImpl reservationDao = new ReservationDaoImpl();
        ArrayList<Integer> reservationsId = reservationDao.getReservationsId(testClient.getId());
        int reservationId = reservationsId.get(0);
        simulatedUserInput = "1\ntest1@email\npassword\n2\n" + reservationId + "\n7\n3\n";
        setUp();
        int premiumReservationId = reservationsId.get(1);
        simulatedUserInput = "1\ntest1@email\npassword\n2\n" + premiumReservationId + "\n7\n3\n";
        setUp();
    }

    @Test
    public void TestNRenewPremium() {
        simulatedUserInput = "1\n" + testClient.getEmail() + "\npassword\n5\ny\n7\n3\n";
        setUp();
    }

    @AfterAll
    static void tearDown() {
        ClientDaoImpl clientDao = new ClientDaoImpl();
        clientDao.deleteTestClient("test1@email");
    }

}

