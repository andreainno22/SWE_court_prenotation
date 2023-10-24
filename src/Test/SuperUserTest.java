package Test;

import ApplicationLayer.GraphicInterfaceManager;
import Context.Client;
import Database.ClientDaoImpl;
import Database.ReservationDaoImpl;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junitpioneer.jupiter.DisableIfTestFails;

import java.io.ByteArrayInputStream;
import java.sql.Date;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

@DisableIfTestFails
@TestMethodOrder(MethodOrderer.MethodName.class)
//todo implementare i test
public class SuperUserTest {
    // make all tests
    private static GraphicInterfaceManager gim;
    private String simulatedUserInput;
    private static Client testClient;

    public void setUp() {

    }
    public void assertion() {
        System.setIn(new ByteArrayInputStream(simulatedUserInput.getBytes()));
        gim = new GraphicInterfaceManager();
        assertDoesNotThrow(() -> gim.startMenu());
    }

    @Test
    public void TestBRegistrationWithAlreadyUsedEmail() {
        // tests if inserting another time the same email makes the program doesn't accept it
        simulatedUserInput = "2\nName\nSurname\ntest1@email\npassword\n123\n0\n3\n";
        assertion();
    }

    @Test
    public void TestCRegistrationInvalidEmail() {
        // tests if inserting another time the same email makes the program doesn't accept it
        simulatedUserInput = "2\nName\nSurname\nemail\npassword\n123\n0\n3\n";
        assertion();
    }

    @Test   // login
    public void TestDLogin() {
        simulatedUserInput = "1\ntest1@email\npassword\n7\n3\n";
        assertion();
    }

    @Test
    public void TestEMakeReservationWithoutMoney() {
        simulatedUserInput = "1\ntest1@email\npassword\n1\n2025-01-02\n1\n2\n3\n0\n7\n3\n";
        assertion();
    }

    @Test
    public void TestFAddMoney() {
        simulatedUserInput = "1\ntest1@email\npassword\n4\ny\n150\n7\n3\n";
        assertion();

    }

    @Test
    public void TestGMakeReservationStandard() {
        simulatedUserInput = "1\ntest1@email\npassword\n1\n2025-01-02\n1\n2\n3\n0\n7\n3\n";
        assertion();
    }

    @Test
    public void TestHMakeReservationStandardRentingKit() {
        simulatedUserInput = "1\ntest1@email\npassword\n1\n2025-01-02\n2\n2\n3\n1\n7\n3\n";
        assertion();
    }

    @Test
    public void TestIUpgradePremium() {
        simulatedUserInput = "1\ntest1@email\npassword\n5\ny\n7\n3\n";
        assertion();
    }


    @Test
    public void TestJMakeReservationPremium() {
        simulatedUserInput = "1\ntest1@email\npassword\n1\n2025-01-02\n1\n2\n2\n0\n7\n3\n";
        assertion();
    }

    @Test
    public void TestKMakeReservationAlreadyBookedSlot() {
        simulatedUserInput = "1\n" + testClient.getEmail() + "\npassword\n1\n2025-01-02\n1\n2\n2\n7\n0\n7\n3\n";
        assertion();
    }

    @Test
    public void TestLMakeReservationPremiumRentingKit() {
        simulatedUserInput = "1\ntest1@email\npassword\n1\n2025-01-02\n3\n2\n3\n1\n7\n3\n";
        assertion();
    }

    @Test
    public void TestMMakeReservationOnHoliday() {
        String date = "2025-01-01";
        simulatedUserInput = "1\ntest1@email\npassword\n1\n" + date + "\n7\n3\n";
        assertion();
        ReservationDaoImpl reservationDao = new ReservationDaoImpl();
        assertFalse(reservationDao.checkTestReservation(testClient, Date.valueOf(date)));
    }

    @Test
    public void TestNDeleteReservation() {
        ReservationDaoImpl reservationDao = new ReservationDaoImpl();
        ArrayList<Integer> reservationsId = reservationDao.getReservationsId(testClient.getId());
        int reservationId = reservationsId.get(0);
        simulatedUserInput = "1\ntest1@email\npassword\n2\n" + reservationId + "\n7\n3\n";
        assertion();
        int premiumReservationId = reservationsId.get(1);
        simulatedUserInput = "1\ntest1@email\npassword\n2\n" + premiumReservationId + "\n7\n3\n";
        assertion();
        int resAfterAlreadyBookedSlot = reservationsId.get(2);
        simulatedUserInput = "1\n" + testClient.getEmail() + "\npassword\n2\n" + resAfterAlreadyBookedSlot + "\n7\n3\n";
        assertion();
    }

    @Test
    public void TestORenewPremium() {
        simulatedUserInput = "1\n" + testClient.getEmail() + "\npassword\n5\ny\n7\n3\n";
        assertion();
    }

    @AfterAll
    static void tearDown() {
        ClientDaoImpl clientDao = new ClientDaoImpl();
        clientDao.deleteTestClient("test1@email");
    }

}


