package com.napier.devops;

import com.napier.devops.dao.*;
import com.napier.devops.db.Database;

import java.sql.Connection;
import java.util.Scanner;

public class App {
    public static void main(String[] args) {

        Database db = new Database();
        db.connect();
        Connection conn = db.getConnection();

        Scanner scanner=new Scanner(System.in);

        // Wire DAOs
        CountryDAO countryDAO = new CountryDAO(conn);
        CityDAO cityDAO = new CityDAO(conn);
        CapitalCityDAO capitalDAO = new CapitalCityDAO(conn);
        PopulationDAO populationDAO = new PopulationDAO(conn);
        LookupDAO lookupDAO = new LookupDAO(conn);

        // Delegate to MenuManager
        MenuManager menu = new MenuManager(scanner, countryDAO, cityDAO, capitalDAO, populationDAO, lookupDAO);
        menu.start();

        db.disconnect();
        System.out.println("👋 Goodbye!");
    }
}
