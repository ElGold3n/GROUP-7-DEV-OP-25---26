package com.napier.devops;

import com.napier.devops.dao.*;
import com.napier.devops.db.Database;
import com.napier.devops.util.TablePrinter;
import com.napier.devops.www.WebServer;

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
        LanguageDAO languageDAO = new LanguageDAO(conn);

        if (System.getenv("WEB_MODE") != null &&  System.getenv("WEB_MODE").equals("true")) {
            try {
                new WebServer().start();
            } catch (Exception e) {
                e.printStackTrace();
            }        }

        if (System.getenv("BATCH_MODE") != null  && System.getenv("BATCH_MODE").equals("true")) {
            System.out.println(System.getenv("BATCH_MODE"));
            runBatchReports(countryDAO, cityDAO, capitalDAO, populationDAO, languageDAO);
        }
        else {
            Scanner scanner = new Scanner(System.in);
            MenuManager menu = new MenuManager(scanner, countryDAO, cityDAO, capitalDAO, populationDAO, lookupDAO, languageDAO);
            menu.start();
        }

        db.disconnect();
        System.out.println("Goodbye!");
    }

    public static void runBatchReports(
            CountryDAO countryDAO,
            CityDAO cityDAO,
            CapitalCityDAO capitalDAO,
            PopulationDAO populationDAO, LanguageDAO languageDAO) {

        System.out.println("Running in batch mode...");

        // Generate top N countries by population
        var topCountries = countryDAO.getCountriesByPopulation(10);
        TablePrinter tp1 = new TablePrinter("Code", "Name", "Continent", "Region", "Population");
        topCountries.forEach(c -> tp1.addRow(c.getCode(), c.getName(), c.getContinent(), c.getRegion(), c.getPopulation()));
        System.out.println("\nTop 10 Countries by Population");
        tp1.print(20);

        // Generate top N cities
        var topCities = cityDAO.getCitiesByPopulation(10);
        TablePrinter tp2 = new TablePrinter( "Capital", "Country", "District", "Population");
        topCities.forEach(c -> tp2.addRow( c.getName(), c.getCountry(), c.getDistrict(), c.getPopulation()));
        System.out.println("\nTop 10 Cities by Population");
        tp2.print(20);

        // Generate top N capital cities
        var topCapitals = capitalDAO.getCapitalCitiesByPopulation(10);
        TablePrinter tp3 = new TablePrinter("ID", "Capital", "Country", "Continent", "Region", "Population");
        topCapitals.forEach(c -> tp3.addRow(c.getId(), c.getName(), c.getCountry(), c.getContinent(), c.getRegion(), c.getPopulation()));
        System.out.println("\nTop 10 Capital Cities by Population");
        tp2.print(20);

        // Generate top N populations
        var populations = populationDAO.getGlobalPopulations(10);
        TablePrinter tp4 = new TablePrinter("Continent", "Total", "In Cities", "Not in Cities");
        populations.forEach(p -> tp4.addRow(
                p.getLabel(),
                p.getTotalPopulation(),
                p.getCityPopulation(),
                p.getNonCityPopulation()
        ));
        System.out.println("\nTop 10 Populations Globally");
        tp4.print(20);

        // Generate top N Languages
        var languages = languageDAO.getLanguagesByPopulation(5);
        TablePrinter tp5 = new TablePrinter("Language", "Speakers", "% of Global Population");
        languages.forEach(r -> tp5.addRow(
                r.getLanguage(),
                r.getSpeakers(),
                r.getPercentOfGlobalPopulation()
        ));
        System.out.println("\nTop 5 Languages");
        tp5.print(20);
    }

}
