package carsharing.data;

import carsharing.model.Car;
import carsharing.model.Customer;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CustomerDaoImp implements CustomerDao {

    private Database databaseCarsharing;
    private Connection connection;

    public CustomerDaoImp(Database databaseCarsharing) {
        this.databaseCarsharing = databaseCarsharing;
        this.connection = databaseCarsharing.getConnection();
    }

    @Override
    public void createTable() {
        try {
            Statement statement = connection.createStatement();
            String sql =  "CREATE TABLE IF NOT EXISTS CUSTOMER " +
                    "(id INTEGER AUTO_INCREMENT, " +
                    "name VARCHAR(255) UNIQUE NOT NULL," +
                    "rented_car_id  INTEGER," +
                    "PRIMARY KEY (id)," +
                    "FOREIGN KEY (rented_car_id) REFERENCES CAR(id))";
            statement.executeUpdate(sql);
            statement.close();
        } catch (SQLException se) {
            se.printStackTrace();
        }
    }

    @Override
    public void insertCustomer(String name) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "INSERT INTO CUSTOMER (name)" +
                            "VALUES (?)");
            preparedStatement.setString(1, name);
            preparedStatement.executeUpdate();
            preparedStatement.close();
        } catch (SQLException se) {
            se.printStackTrace();
        }
    }

    @Override
    public List<Customer> selectCustomer(int customerId) {
        List<Customer> customerList = new ArrayList<>();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT * FROM CUSTOMER WHERE id = ?");
            preparedStatement.setInt(1, customerId);
            ResultSet resultSet = preparedStatement.executeQuery();

            while(resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                int car = resultSet.getInt("rented_car_id");
                customerList.add(new Customer(id, name, car));
            }
            preparedStatement.close();
        } catch (SQLException se) {
            se.printStackTrace();
        }
        return customerList;
    }

    @Override
    public List<Customer> selectAllCustomer() {
        List<Customer> customerList = new ArrayList<>();
        try {
            Statement statement = connection.createStatement();
            String sql =  "SELECT * FROM CUSTOMER";
            ResultSet resultSet = statement.executeQuery(sql);

            while(resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                customerList.add(new Customer(id, name));
            }
            statement.close();
        } catch (SQLException se) {
            se.printStackTrace();
        }
        return customerList;
    }

    @Override
    public void deleteTable() {
        try {
            Statement statement = connection.createStatement();
            String sql =  "DROP TABLE CUSTOMER";
            statement.executeUpdate(sql);
            statement.close();
        } catch (SQLException se) {
            se.printStackTrace();
        }
    }

    @Override
    public Car selectRentedCar(int customerId) {
        Car car = new Car();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT * FROM CAR " +
                            "INNER JOIN CUSTOMER " +
                            "ON CAR.id = CUSTOMER.rented_car_id " +
                            "WHERE CUSTOMER.id = ?");
            preparedStatement.setInt(1, customerId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                car.setId(resultSet.getInt("id"));
                car.setName(resultSet.getString("name"));
                car.setCompanyId(resultSet.getInt("company_id"));
            }
            preparedStatement.close();
        } catch (SQLException se) {
            se.printStackTrace();
        }
        return car;
    }

    public void returnCar(int customerId) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "UPDATE CUSTOMER " +
                            "SET rented_car_id = NULL " +
                            "WHERE id = ?");
            preparedStatement.setInt(1, customerId);
            preparedStatement.executeUpdate();
            preparedStatement.close();
        } catch (SQLException se) {
            se.printStackTrace();
        }
    }

    public void rentCar(int id, int carId) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "UPDATE CUSTOMER " +
                            "SET rented_car_id = ? " +
                            "WHERE id = ?");
            preparedStatement.setInt(1, carId);
            preparedStatement.setInt(2, id);
            preparedStatement.executeUpdate();
            preparedStatement.close();
        } catch (SQLException se) {
            se.printStackTrace();
        }
    }
}
