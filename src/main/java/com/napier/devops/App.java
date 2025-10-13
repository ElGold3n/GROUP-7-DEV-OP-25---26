package com.napier.devops;

import com.napier.devops.dao.*;
import com.napier.devops.db.Database;
import com.napier.devops.util.TablePrinter;

import java.sql.Connection;
import java.util.Scanner;

public class App {
    public static void main(String[] args) {
        Database db = new Database();
        db.connect();
        Connection conn = db.getConnection();

        // Wire DAOs
        CountryDAO countryDAO = new CountryDAO(conn);
        CityDAO cityDAO = new CityDAO(conn);
        CapitalCityDAO capitalDAO = new CapitalCityDAO(conn);
        PopulationDAO populationDAO = new PopulationDAO(conn);
        LookupDAO lookupDAO = new LookupDAO(conn);

        if (System.getenv("BATCH_MODE") != null) {
            runBatchReports(countryDAO, cityDAO, capitalDAO, populationDAO);
        } else {
            Scanner scanner = new Scanner(System.in);
            MenuManager menu = new MenuManager(scanner, countryDAO, cityDAO, capitalDAO, populationDAO, lookupDAO);
            menu.start();
        }

        db.disconnect();
        System.out.println("👋 Goodbye!");
    }

    private static void runBatchReports(
            CountryDAO countryDAO,
            CityDAO cityDAO,
            CapitalCityDAO capitalDAO,
            PopulationDAO populationDAO) {

        System.out.println("Running in batch mode...");

        // Example: generate top 10 countries by population
        var topCountries = countryDAO.getCountriesByPopulation(10);
        TablePrinter tp1 = new TablePrinter("Code", "Name", "Continent", "Region", "Population");
        topCountries.forEach(c -> tp1.addRow(c.getCode(), c.getName(), c.getContinent(), c.getRegion(), c.getPopulation()));
        tp1.print(20, new Scanner(System.in));

        // Example: generate top 10 capital cities
        var topCapitals = capitalDAO.getCapitalCitiesByPopulation(10);
        TablePrinter tp2 = new TablePrinter("ID", "Capital", "Country", "Continent", "Region", "Population");
        topCapitals.forEach(c -> tp2.addRow(c.getId(), c.getName(), c.getCountry(), c.getContinent(), c.getRegion(), c.getPopulation()));
        tp2.print(20, new Scanner(System.in));

        // Example: population by continent
        var populations = populationDAO.getContinentPopulations();
        TablePrinter tp3 = new TablePrinter("Continent", "Total", "In Cities", "Not in Cities");
        populations.forEach(p -> tp3.addRow(
                p.getLabel(),
                p.getTotalPopulation(),
                p.getCityPopulation(),
                p.getNonCityPopulation()
        ));
        tp3.print(20, new Scanner(System.in));
    }

}
