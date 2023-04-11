package carsharing.data;

import carsharing.model.Car;
import carsharing.model.Company;

import java.util.List;

public interface CarDao {
    public void createTable();
    public void insertCar(String name,  int companyId);
    public List<Car> selectCar(int companyId);
    void deleteTable();
}
