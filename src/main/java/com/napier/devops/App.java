package com.napier.devops;

import com.napier.devops.dao.*;
import com.napier.devops.db.Database;
import com.napier.devops.util.TablePrinter;
import com.napier.devops.www.WebServer;

import java.sql.Connection;
import java.util.Scanner;


/**
 * This is the main application entry point for the World Population Reporting System.

 * This application provides three modes of operation listed below:


 * 1. WEB_MODE - Runs a REST API web server for remote access
 * 2. BATCH_MODE - Generates predefined reports and outputs to console
 * 3. Interactive Mode (default) - Provides a menu-driven console interface

 * Environment variables:
 * - WEB_MODE=true - Enables web server mode
 * - BATCH_MODE=true - Enables batch report generation mode

 * @author DevOps Group 7
 * @version 1.0
 */



public class App {
    /**
     * This is the main method that initializes the database connection,
     * sets up Data Access Objects (DAOs), and determines the application mode.

     * @param args Command line arguments (currently not used)
     */
    public static void main(String[] args) {
        // Initialize database connection
        Database db = new Database();
        db.connect();
        Connection conn = db.getConnection();
        
        // Initialize Data Access Objects (DAOs) for database operations
        // Each DAO handles queries for a specific domain entity
        CountryDAO countryDAO = new CountryDAO(conn);
        CityDAO cityDAO = new CityDAO(conn);
        CapitalCityDAO capitalDAO = new CapitalCityDAO(conn);
        PopulationDAO populationDAO = new PopulationDAO(conn);
        LookupDAO lookupDAO = new LookupDAO(conn);
        LanguageDAO languageDAO = new LanguageDAO(conn);

        // Check if web server mode is enabled via environment variable
        if (System.getenv("WEB_MODE") != null &&  System.getenv("WEB_MODE").equals("true")) {
            try {

                // Start the web server on port 8081
                new WebServer().start();
            } catch (Exception e) {
                e.printStackTrace();
            }        }

        // Check if batch mode is enabled via environment variable
        if (System.getenv("BATCH_MODE") != null  && System.getenv("BATCH_MODE").equals("true")) {
            System.out.println(System.getenv("BATCH_MODE"));
            // Run predefined batch reports
            runBatchReports(countryDAO, cityDAO, capitalDAO, populationDAO, languageDAO);
        }
        else {
            // Run predefined batch reports
            Scanner scanner = new Scanner(System.in);
            MenuManager menu = new MenuManager(scanner, countryDAO, cityDAO, capitalDAO, populationDAO, lookupDAO, languageDAO);
            menu.start();
        }

        // Clean up: Close database connection before exit
        db.disconnect();
        System.out.println("Goodbye!");
    }    
    // ... exiting code ...


    /**
     * This Section Generates and displays a predefined set of population reports in batch mode.
     * This is the primary method used for automated report generation and testing.

     * Generated Reports:
     * - Top 10 countries by population
     * - Top 10 cities by population
     * - Top 10 capital cities by population
     * - Top 10 global populations by continent
     * - Top 5 languages by number of speakers.

     * @param countryDAO Data access object for country queries
     * @param cityDAO Data access object for city queries
     * @param capitalDAO Data access object for capital city queries
     * @param populationDAO Data access object for population queries
     * @param languageDAO Data access object for language queries.
     */


    private static void runBatchReports(
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
        tp3.print(20);

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
