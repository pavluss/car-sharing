package carsharing.data;

import carsharing.model.Company;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public interface CompanyDao {
    public void createTable();
    public void insertCompany(String name);
    public List<Company> selectCompany();

    Company selectCompanyId(int companyId);

    void deleteTable();
}
