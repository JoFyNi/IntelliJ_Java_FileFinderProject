package componenten;

import java.sql.Driver;
import java.sql.DriverManager;
import java.util.Enumeration;
import java.util.ServiceLoader;

public class driverGetter {
    public driverGetter() {
        ServiceLoader<Driver> loadedDrivers = ServiceLoader.load(Driver.class);
        for (Driver driver : loadedDrivers) {
            Enumeration<Driver> drivers = DriverManager.getDrivers();
            System.out.printf(drivers + "");
            System.out.printf(driver + "");
        }
    }
}
