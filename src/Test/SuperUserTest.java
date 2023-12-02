package Test;

import ApplicationLayer.GraphicInterfaceManager;
import DomainModel.SuperUser;
import DAOs.CourtDaoImpl;

import DAOs.SuperUserDaoImpl;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junitpioneer.jupiter.DisableIfTestFails;

import java.io.ByteArrayInputStream;
import static org.junit.jupiter.api.Assertions.*;

@DisableIfTestFails
@TestMethodOrder(MethodOrderer.MethodName.class)
//todo implementare i test
public class SuperUserTest {
    // make all tests
    private static GraphicInterfaceManager gim;
    private String simulatedUserInput;
    private static final SuperUserDaoImpl superUserDao = new SuperUserDaoImpl();
    private static final CourtDaoImpl courtDao = new CourtDaoImpl();
    private SuperUser superUser;

    public void setUp() {
        superUser = new SuperUser("name", "surname", "test@test", "password", 0);
        superUserDao.insertSuperUser(superUser);
    }

    public void assertion() {
        System.setIn(new ByteArrayInputStream(simulatedUserInput.getBytes()));
        gim = new GraphicInterfaceManager();
        assertDoesNotThrow(() -> gim.startMenu());
    }

    @Test   // login
    public void TestALogin() {
        simulatedUserInput = "1\na@b\nandre\n9\n3\n";
        assertion();
    }

    @Test
    public void TestBInsertCourt() {
        simulatedUserInput = "1\na@b\nandre\n1\n20\nclay\n9\n3\n";
        assertion();
    }

    @Test
    public void TestCDeleteCourt() {
        simulatedUserInput = "1\na@b\nandre\n2\n20\n9\n3\n";
        assertion();

    }

    @Test
    public void TestDModifyPrice() {
        String type = "clay";
        float price = courtDao.getPrice(type);
        simulatedUserInput = "1\na@b\nandre\n3\nclay\n25\n9\n3\n";
        assertion();
        simulatedUserInput = "1\na@b\nandre\n3\nclay\n" + price + "\n9\n3\n";
        assertion();
    }

    @Test
    public void TestEInsertTimeSlot() {
        simulatedUserInput = "1\na@b\nandre\n4\n20\n23\n24\n9\n3\n";
        assertion();
    }

    @Test
    public void TestFDeleteTimeSlot() {
        simulatedUserInput = "1\na@b\nandre\n5\n20\n9\n3\n";
        assertion();
    }

    @AfterAll
    static void tearDown() {
        superUserDao.deleteSuperUser("test@test");
    }

}


