package carsharing.data;

import carsharing.model.Car;
import carsharing.model.Company;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CarDaoImp implements CarDao{

    private Database databaseCarsharing;
    private Connection connection;

    public CarDaoImp(Database databaseCarsharing) {
        this.databaseCarsharing = databaseCarsharing;
        this.connection = databaseCarsharing.getConnection();
    }

    @Override
    public void createTable() {
        try {
            Statement statement = connection.createStatement();
            String sql =  "CREATE TABLE IF NOT EXISTS CAR " +
                    "(id INTEGER AUTO_INCREMENT, " +
                    "name VARCHAR(255) UNIQUE NOT NULL," +
                    "company_id  INTEGER NOT NULL," +
                    "PRIMARY KEY (id)," +
                    "FOREIGN KEY (company_id) REFERENCES COMPANY(id))";
            statement.executeUpdate(sql);
            statement.close();
        } catch (SQLException se) {
            se.printStackTrace();
        }
    }

    @Override
    public void insertCar(String name, int companyId) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "INSERT INTO CAR (name, company_id)" +
                    "VALUES (?, ?)");
            preparedStatement.setString(1, name);
            preparedStatement.setInt(2, companyId);
            preparedStatement.executeUpdate();
            preparedStatement.close();
        } catch (SQLException se) {
            se.printStackTrace();
        }
    }

    @Override
    public List<Car> selectCar(int companyId) {
        List<Car> companyCar = new ArrayList<>();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT * FROM CAR WHERE company_id = ?");
            preparedStatement.setInt(1, companyId);
            ResultSet resultSet = preparedStatement.executeQuery();

            while(resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                int company = resultSet.getInt("company_id");
                companyCar.add(new Car(id, name, company));
            }
            preparedStatement.close();
        } catch (SQLException se) {
            se.printStackTrace();
        }
        return companyCar;
    }

    @Override
    public void deleteTable() {
        try {
            Statement statement = connection.createStatement();
            String sql =  "DROP TABLE CAR";
            statement.executeUpdate(sql);
            statement.close();
        } catch (SQLException se) {
            se.printStackTrace();
        }
    }

    public List<Car> selectFreeCar(int companyId) {
        List<Car> companyCar = new ArrayList<>();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT * FROM CAR " +
                            "LEFT JOIN CUSTOMER " +
                            "ON CAR.id = CUSTOMER.rented_car_id " +
                            "WHERE (CAR.company_id = ? AND CUSTOMER.rented_car_id IS NULL)");

            preparedStatement.setInt(1, companyId);
            ResultSet resultSet = preparedStatement.executeQuery();

            while(resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                int company = resultSet.getInt("company_id");
                companyCar.add(new Car(id, name, company));
            }
            preparedStatement.close();
        } catch (SQLException se) {
            se.printStackTrace();
        }
        return companyCar;
    }
}
