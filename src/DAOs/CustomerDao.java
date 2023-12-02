package DAOs;

import DomainModel.Customer;

import java.sql.Date;
import java.sql.Statement;
import java.util.List;

public interface CustomerDao extends Dao {
    void deleteTestCustomer(String email);
    int insertCustomer(Customer customer);
    Customer getCustomer(String email, String password);
    Date getPremiumExpiration(Customer customer);
    void updatePoints(int points, Customer customer, Statement transactionStmt);
    boolean modifyPremiumExpiration(Customer customer);
    boolean modifyPremium(Customer customer);
    List<Customer> getAllCustomers();
}
