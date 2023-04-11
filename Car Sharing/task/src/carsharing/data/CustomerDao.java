package carsharing.data;


import carsharing.model.Car;
import carsharing.model.Customer;

import java.util.List;

public interface CustomerDao {

    public void createTable();
    public void insertCustomer(String name);
    public List<Customer> selectCustomer(int companyId);

    List<Customer> selectAllCustomer();

    void deleteTable();

    Car selectRentedCar(int customerId);
}
