package carsharing;

import carsharing.data.CarDaoImp;
import carsharing.data.CompanyDaoImp;
import carsharing.data.CustomerDaoImp;
import carsharing.data.Database;
import carsharing.model.Car;
import carsharing.model.Company;
import carsharing.model.Customer;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.IntStream;

public class Main {

    public static void main(String[] args) {

        String databaseFileName = "carsharing";
        if (args.length > 0 && args[0].equals("-databaseFileName")) {
            databaseFileName = args[1];
        }
        Database databaseCarsharing = new Database(databaseFileName);
        CompanyDaoImp companyDaoImp = new CompanyDaoImp(databaseCarsharing);
        CarDaoImp carDaoImp = new CarDaoImp(databaseCarsharing);
        CustomerDaoImp customerDaoImp = new CustomerDaoImp(databaseCarsharing);

        //carDaoImp.deleteTable();
        //companyDaoImp.deleteTable();

        companyDaoImp.createTable();
        carDaoImp.createTable();
        customerDaoImp.createTable();


        Scanner scanner = new Scanner(System.in);
        startMenu();
        int option = scanner.nextInt();

        while (option != 0) {
            if (option == 1) {
                secondMenu();
                option = scanner.nextInt();

                while (option != 0) {
                    if (option == 1) {
                        List<Company> companyList = companyDaoImp.selectCompany();
                        if (companyList.isEmpty()) {
                            System.out.println("The company list is empty!");
                        } else {
                            System.out.println("Company list:");
                            companyList.forEach(System.out::println);
                            System.out.println("0. Back");
                            int companyNum = scanner.nextInt();
                            if (!(companyNum == 0)) {
                                String companyName = companyList.get(companyNum - 1).getName();
                                int companyId = companyList.get(companyNum - 1).getId();
                                carMenu(companyName);
                                option = scanner.nextInt();

                                while (option != 0) {
                                    if (option == 1) {
                                        List<Car> carList = carDaoImp.selectCar(companyId);
                                        if (carList.isEmpty()) {
                                            System.out.println("The car list is empty!");
                                        } else {
                                            System.out.println("'" + companyName + "' cars:");
                                            IntStream.iterate(1, i -> i <= carList.size(), i -> i + 1)
                                                    .forEach(i -> System.out.printf("%d. %s%n", i, carList.get(i - 1).getName()));
                                        }
                                    } else if (option == 2) {
                                        System.out.println("Enter the car name:");
                                        String name = scanner.nextLine();
                                        if (name.isEmpty()) {
                                            name = scanner.nextLine();
                                        }
                                        carDaoImp.insertCar(name, companyId);
                                        System.out.println("The car was added!");
                                    }
                                    carMenu(companyName);
                                    option = scanner.nextInt();
                                }
                            }
                        }
                    } else if (option == 2) {
                        System.out.println("Enter the company name:");
                        String name = scanner.nextLine();
                        if (name.isEmpty()) {
                            name = scanner.nextLine();
                        }
                        companyDaoImp.insertCompany(name);
                        System.out.println("The company was created!");
                    }
                    secondMenu();
                    option = scanner.nextInt();
                }
            } else if (option == 2) {

                List<Customer> customerList = customerDaoImp.selectAllCustomer();
                if (customerList.isEmpty()) {
                    System.out.println("The customer list is empty!");
                } else {
                    System.out.println("Choose a customer:");
                    IntStream.iterate(1, i -> i <= customerList.size(), i -> i + 1)
                            .forEach(i -> System.out.printf("%d. %s%n", i, customerList.get(i - 1).getName()));
                    System.out.println("0. Back");

                    int customerNum = scanner.nextInt();
                    if (!(customerNum == 0)) {
                        String customerName = customerList.get(customerNum - 1).getName();
                        int customerId = customerList.get(customerNum - 1).getId();
                        customerMenu();
                        option = scanner.nextInt();

                        while (option != 0) {
                            if (option == 1) {
                                Car car = customerDaoImp.selectRentedCar(customerId);
                                if (car.getName() != null) {
                                    System.out.println("You've already rented a car!");
                                } else {
                                    List<Company> companyList = companyDaoImp.selectCompany();
                                    if (companyList.isEmpty()) {
                                        System.out.println("The company list is empty!");
                                    } else {
                                        System.out.println("Choose a company:");
                                        companyList.forEach(System.out::println);
                                        System.out.println("0. Back");
                                        int companyNum = scanner.nextInt();
                                        if (!(companyNum == 0)) {
                                            String companyName = companyList.get(companyNum - 1).getName();
                                            int companyId = companyList.get(companyNum - 1).getId();

                                            List<Car> carList = carDaoImp.selectFreeCar(companyId);
                                            if (carList.isEmpty()) {
                                                System.out.println("No available cars in the " + companyName + " company");
                                            } else {
                                                System.out.println("Choose a car:");
                                                IntStream.iterate(1, i -> i <= carList.size(), i -> i + 1)
                                                        .forEach(i -> System.out.printf("%d. %s%n", i, carList.get(i - 1).getName()));
                                                System.out.println("0. Back");
                                                int carNum = scanner.nextInt();
                                                if (!(carNum == 0)) {
                                                    String carName = carList.get(carNum - 1).getName();
                                                    int carId = carList.get(carNum - 1).getId();
                                                    customerDaoImp.rentCar(customerId, carId);
                                                    System.out.println("You rented '" + carName + "'");
                                                }
                                            }
                                        }
                                    }
                                }
                            } else if (option == 2) {
                                Car car = customerDaoImp.selectRentedCar(customerId);
                                if (car.getName() != null) {
                                    customerDaoImp.returnCar(customerId);
                                    companyDaoImp.selectCompanyId(car.getCompanyId()).getName();
                                    System.out.println("You've returned a rented car!");
                                } else {
                                    System.out.println("You didn't rent a car!");
                                }

                            } else if (option == 3) {
                                Car car = customerDaoImp.selectRentedCar(customerId);
                                if (car.getName() != null) {
                                    System.out.println("Your rented car:");
                                    System.out.println(car.getName());
                                    System.out.println("Company:");
                                    String nameCompany = companyDaoImp.selectCompanyId(car.getCompanyId()).getName();
                                    System.out.println(nameCompany);
                                } else {
                                    System.out.println("You didn't rent a car!");
                                }
                            }
                            customerMenu();
                            option = scanner.nextInt();
                        }
                    }


                }

            } else if (option == 3) {
                System.out.println("Enter the customer name:");
                String name = scanner.nextLine();
                if (name.isEmpty()) {
                    name = scanner.nextLine();
                }
                customerDaoImp.insertCustomer(name);
                System.out.println("The customer was added!");
            }
            startMenu();
            option = scanner.nextInt();
        }

        databaseCarsharing.closeConnection();
    }

    public static void startMenu() {
        System.out.println("1. Log in as a manager\n" +
                "2. Log in as a customer\n" +
                "3. Create a customer\n" +
                "0. Exit");
    }

    private static void secondMenu() {
        System.out.println("1. Company list\n" +
                            "2. Create a company\n" +
                            "0. Back");
    }

    private static void carMenu(String companyName) {
        System.out.println("'" + companyName + "' company:\n" +
                            "1. Car list\n" +
                            "2. Create a car\n" +
                            "0. Back");
    }

    private static void customerMenu() {
        System.out.println("1. Rent a car\n" +
                "2. Return a rented car\n" +
                "3. My rented car\n" +
                "0. Back");
    }
}