package com.napier.devops;

import com.napier.devops.dao.*;
import com.napier.devops.models.*;
import com.napier.devops.util.TablePrinter;

import java.util.*;

public class MenuManager {
    private final Scanner scanner;
    private final CountryDAO countryDAO;
    private final CityDAO cityDAO;
    private final CapitalCityDAO capitalDAO;
    private final PopulationDAO populationDAO;
    private final LookupDAO lookupDAO;
    private final LanguageDAO languageDAO;
    private final Deque<String> breadcrumb = new ArrayDeque<>();

    public MenuManager(
            Scanner scanner, CountryDAO countryDAO, CityDAO cityDAO, CapitalCityDAO capitalDAO,
            PopulationDAO populationDAO, LookupDAO lookupDAO, LanguageDAO languageDAO
    ) {
        this.scanner = scanner;
        this.countryDAO = countryDAO;
        this.cityDAO = cityDAO;
        this.capitalDAO = capitalDAO;
        this.populationDAO = populationDAO;
        this.lookupDAO = lookupDAO;
        this.languageDAO = languageDAO;
    }


    // --- Main Menu ---
    public void start() {
        breadcrumb.push("Main Menu");
        boolean running = true;
        while (running) {
            showBreadcrumb();
            System.out.println("\n--- Main Menu ---");
            System.out.println("1. Countries");
            System.out.println("2. Cities");
            System.out.println("3. Capital Cities");
            System.out.println("4. Population Distribution");
            System.out.println("5. Languages");
            System.out.println("0. Exit");
            System.out.print("Choose an option: ");

            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1" -> showCountries();
                case "2" -> showCities();
                case "3" -> showCapitals();
                case "4" -> showPopulations();
                case "5" -> showLanguages();
                case "0" -> running = false;
                default -> System.out.println("Invalid choice.");
            }
        }
        breadcrumb.pop();
    }


    // --- Countries Submenu ---
    private void showCountries() {
        breadcrumb.push("Countries");
        boolean running = true;
        while (running) {
            showBreadcrumb();
            System.out.println("\n--- Country Reports ---");
            System.out.println("1. Global Reports");
            System.out.println("2. Continental Reports");
            System.out.println("3. Regional Reports");
            System.out.println("0. Back");
            System.out.print("Choose an option: ");

            switch (scanner.nextLine().trim()) {
                case "1" -> showGlobalCountryReports();
                case "2" -> showContinentalCountryReports();
                case "3" -> showRegionalCountryReports();
                case "0" -> running = false;
                default -> System.out.println("Invalid choice.");
            }
        }
        breadcrumb.pop();
    }

    private void showGlobalCountryReports() {
        breadcrumb.push("Global Reports");
        boolean running = true;
        while (running) {
            showBreadcrumb();
            System.out.println("\n--- Global Country Reports ---");
            System.out.println("1. All countries (largest → smallest)");
            System.out.println("2. Top N populated countries");
            System.out.println("0. Back");
            System.out.print("Choose an option: ");

            switch (scanner.nextLine().trim()) {
                case "1" -> printCountries(countryDAO.getCountriesByPopulation());
                case "2" -> {
                    System.out.print("Enter N: ");
                    int n = Integer.parseInt(scanner.nextLine());
                    printCountries(countryDAO.getCountriesByPopulation(n));
                }
                case "0" -> running = false;
                default -> System.out.println("Invalid choice.");
            }
        }
        breadcrumb.pop();
    }

    private void showContinentalCountryReports() {
        breadcrumb.push("Continental Reports");
        boolean running = true;
        while (running) {
            showBreadcrumb();
            System.out.println("\n--- Continental Country Reports ---");
            System.out.println("1. All countries in a continent");
            System.out.println("2. Top N populated countries in a continent");
            System.out.println("0. Back");
            System.out.print("Choose an option: ");

            switch (scanner.nextLine().trim()) {
                case "1" -> {
                    String continent = chooseContinent();
                    if (continent != null) printCountries(countryDAO.getCountriesInContinent(continent));
                }
                case "2" -> {
                    String continent = chooseContinent();
                    if (continent != null) {
                        System.out.print("Enter N: ");
                        int n = Integer.parseInt(scanner.nextLine());
                        printCountries(countryDAO.getCountriesInContinent(continent, n));
                    }
                }
                case "0" -> running = false;
                default -> System.out.println("Invalid choice.");
            }
        }
        breadcrumb.pop();
    }

    private void showRegionalCountryReports() {
        breadcrumb.push("Regional Reports");
        boolean running = true;
        while (running) {
            showBreadcrumb();
            System.out.println("\n--- Regional Country Reports ---");
            System.out.println("1. All countries in a region");
            System.out.println("2. Top N populated countries in a region");
            System.out.println("0. Back");
            System.out.print("Choose an option: ");

            switch (scanner.nextLine().trim()) {
                case "1" -> {
                    String region = chooseRegion();
                    if (region != null) printCountries(countryDAO.getCountriesInRegion(region));
                }
                case "2" -> {
                    String region = chooseRegion();
                    if (region != null) {
                        System.out.print("Enter N: ");
                        int n = Integer.parseInt(scanner.nextLine());
                        printCountries(countryDAO.getCountriesInRegion(region, n));
                    }
                }
                case "0" -> running = false;
                default -> System.out.println("Invalid choice.");
            }
        }
        breadcrumb.pop();
    }


    // --- Cities ---
    private void showCities() {
        breadcrumb.push("Cities");
        boolean running = true;
        while (running) {
            showBreadcrumb();
            System.out.println("\n--- City Reports ---");
            System.out.println("1. Global Reports");
            System.out.println("2. Continental Reports");
            System.out.println("3. Regional Reports");
            System.out.println("4. Country Reports");
            System.out.println("5. District Reports");
            System.out.println("0. Back");
            System.out.print("Choose an option: ");

            switch (scanner.nextLine().trim()) {
                case "1" -> showGlobalCityReports();
                case "2" -> showContinentalCityReports();
                case "3" -> showRegionalCityReports();
                case "4" -> showCountryCityReports();
                case "5" -> showDistrictCityReports();
                case "0" -> running = false;
                default -> System.out.println("Invalid choice.");
            }
        }
        breadcrumb.pop();
    }

    private void showGlobalCityReports() {
        breadcrumb.push("Global Reports");
        boolean running = true;
        while (running) {
            showBreadcrumb();
            System.out.println("\n--- Global City Reports ---");
            System.out.println("1. All cities (largest → smallest)");
            System.out.println("2. Top N populated cities");
            System.out.println("0. Back");
            System.out.print("Choose an option: ");

            switch (scanner.nextLine().trim()) {
                case "1" -> printCities(cityDAO.getCitiesByPopulation());
                case "2" -> {
                    System.out.print("Enter N: ");
                    int n = Integer.parseInt(scanner.nextLine());
                    printCities(cityDAO.getCitiesByPopulation(n));
                }
                case "0" -> running = false;
                default -> System.out.println("Invalid choice.");
            }
        }
        breadcrumb.pop();
    }

    private void showContinentalCityReports() {
        breadcrumb.push("Continental Reports");
        boolean running = true;
        while (running) {
            showBreadcrumb();
            System.out.println("\n--- Continental City Reports ---");
            System.out.println("1. All cities in a continent");
            System.out.println("2. Top N populated cities in a continent");
            System.out.println("0. Back");
            System.out.print("Choose an option: ");

            switch (scanner.nextLine().trim()) {
                case "1" -> {
                    String continent = chooseContinent();
                    if (continent != null) printCities(cityDAO.getCitiesInContinent(continent));
                }
                case "2" -> {
                    String continent = chooseContinent();
                    if (continent != null) {
                        System.out.print("Enter N: ");
                        int n = Integer.parseInt(scanner.nextLine());
                        printCities(cityDAO.getCitiesInContinent(continent, n));
                    }
                }
                case "0" -> running = false;
                default -> System.out.println("Invalid choice.");
            }
        }
        breadcrumb.pop();
    }

    private void showRegionalCityReports() {
        breadcrumb.push("Regional Reports");
        boolean running = true;
        while (running) {
            showBreadcrumb();
            System.out.println("\n--- Regional City Reports ---");
            System.out.println("1. All cities in a region");
            System.out.println("2. Top N populated cities in a region");
            System.out.println("0. Back");
            System.out.print("Choose an option: ");

            switch (scanner.nextLine().trim()) {
                case "1" -> {
                    String region = chooseRegion();
                    if (region != null) printCities(cityDAO.getCitiesInRegion(region));
                }
                case "2" -> {
                    String region = chooseRegion();
                    if (region != null) {
                        System.out.print("Enter N: ");
                        int n = Integer.parseInt(scanner.nextLine());
                        printCities(cityDAO.getCitiesInRegion(region, n));
                    }
                }
                case "0" -> running = false;
                default -> System.out.println("Invalid choice.");
            }
        }
        breadcrumb.pop();
    }

    private void showCountryCityReports() {
        breadcrumb.push("Country Reports");
        boolean running = true;
        while (running) {
            showBreadcrumb();
            System.out.println("\n--- Countrywide City Reports ---");
            System.out.println("1. All cities in a country");
            System.out.println("2. Top N populated cities in a country");
            System.out.println("0. Back");
            System.out.print("Choose an option: ");

            switch (scanner.nextLine().trim()) {
                case "1" -> {
                    String countryCode = chooseCountry();
                    if (countryCode != null) printCities(cityDAO.getCitiesInCountryByCode(countryCode));
                }
                case "2" -> {
                    String countryCode = chooseCountry();
                    if (countryCode != null) {
                        System.out.print("Enter N: ");
                        int n = Integer.parseInt(scanner.nextLine());
                        printCities(cityDAO.getCitiesInCountryByCode(countryCode, n));
                    }
                }
                case "0" -> running = false;
                default -> System.out.println("Invalid choice.");
            }
        }
        breadcrumb.pop();
    }

    private void showDistrictCityReports() {
        breadcrumb.push("District Reports");
        boolean running = true;
        while (running) {
            showBreadcrumb();
            System.out.println("\n--- District City Reports ---");
            System.out.println("1. All cities in a district (largest → smallest)");
            System.out.println("2. Top N populated cities in a district");
            System.out.println("0. Back");
            System.out.print("Choose an option: ");

            switch (scanner.nextLine().trim()) {
                case "1" -> {
                    String countryCode = chooseCountry();
                    if (countryCode != null) {
                        String district = chooseDistrict(countryCode);
                        if (district != null) {
                            printCities(cityDAO.getCitiesInDistrict(district));
                        }
                    }
                }
                case "2" -> {
                    String countryCode = chooseCountry();
                    if (countryCode != null) {
                        String district = chooseDistrict(countryCode);
                        if (district != null) {
                            System.out.print("Enter N: ");
                            int n = Integer.parseInt(scanner.nextLine());
                            printCities(cityDAO.getCitiesInDistrict(district, n));
                        }
                    }
                }
                case "0" -> running = false;
                default -> System.out.println("Invalid choice.");
            }
        }
        breadcrumb.pop();
    }


    // --- Capitals ---
    private void showCapitals() {
        breadcrumb.push("Capital City Reports");
        boolean running = true;
        while (running) {
            showBreadcrumb();
            System.out.println("\n--- Capital City Reports ---");
            System.out.println("1. Global Reports");
            System.out.println("2. Continental Reports");
            System.out.println("3. Regional Reports");
            System.out.println("0. Back");
            System.out.print("Choose an option: ");

            switch (scanner.nextLine().trim()) {
                case "1" -> showGlobalCapitalReports();
                case "2" -> showContinentalCapitalReports();
                case "3" -> showRegionalCapitalReports();
                case "0" -> running = false;
                default -> System.out.println("Invalid choice.");
            }
        }
        breadcrumb.pop();
    }

    private void showGlobalCapitalReports() {
        breadcrumb.push("Global Capital Reports");
        boolean running = true;
        while (running) {
            showBreadcrumb();
            System.out.println("\n--- Global Capital Reports ---");
            System.out.println("1. All capital cities (largest → smallest)");
            System.out.println("2. Top N populated capital cities");
            System.out.println("0. Back");
            System.out.print("Choose an option: ");

            switch (scanner.nextLine().trim()) {
                case "1" -> printCapitalCities(capitalDAO.getCapitalCitiesByPopulation());
                case "2" -> {
                    System.out.print("Enter N: ");
                    int n = Integer.parseInt(scanner.nextLine());
                    printCapitalCities(capitalDAO.getCapitalCitiesByPopulation(n));
                }
                case "0" -> running = false;
                default -> System.out.println("Invalid choice.");
            }
        }
        breadcrumb.pop();
    }

    private void showContinentalCapitalReports() {
        breadcrumb.push("Continental Capital Reports");
        boolean running = true;
        while (running) {
            showBreadcrumb();
            System.out.println("\n--- Continental Capital Reports ---");
            System.out.println("1. All capital cities in a continent");
            System.out.println("2. Top N populated capital cities in a continent");
            System.out.println("0. Back");
            System.out.print("Choose an option: ");

            switch (scanner.nextLine().trim()) {
                case "1" -> {
                    String continent = chooseContinent();
                    if (continent != null) printCapitalCities(capitalDAO.getCapitalCitiesInContinent(continent));
                }
                case "2" -> {
                    String continent = chooseContinent();
                    if (continent != null) {
                        System.out.print("Enter N: ");
                        int n = Integer.parseInt(scanner.nextLine());
                        printCapitalCities(capitalDAO.getCapitalCitiesInContinent(continent, n));
                    }
                }
                case "0" -> running = false;
                default -> System.out.println("Invalid choice.");
            }
        }
        breadcrumb.pop();
    }

    private void showRegionalCapitalReports() {
        breadcrumb.push("Regional Capital Reports");
        boolean running = true;
        while (running) {
            showBreadcrumb();
            System.out.println("\n--- Regional Capital Reports ---");
            System.out.println("1. All capital cities in a region");
            System.out.println("2. Top N populated capital cities in a region");
            System.out.println("0. Back");
            System.out.print("Choose an option: ");

            switch (scanner.nextLine().trim()) {
                case "1" -> {
                    String region = chooseRegion();
                    if (region != null) printCapitalCities(capitalDAO.getCapitalCitiesInRegion(region));
                }
                case "2" -> {
                    String region = chooseRegion();
                    if (region != null) {
                        System.out.print("Enter N: ");
                        int n = Integer.parseInt(scanner.nextLine());
                        printCapitalCities(capitalDAO.getCapitalCitiesInRegion(region, n));
                    }
                }
                case "0" -> running = false;
                default -> System.out.println("Invalid choice.");
            }
        }
        breadcrumb.pop();
    }


    // --- Languages ---
    private void showLanguages() {
        breadcrumb.push("Language Reports");
        boolean running = true;
        while (running) {
            showBreadcrumb();
            System.out.println("\n--- Language Reports ---");
            System.out.println("1. Global Language Reports (by speakers)");
            System.out.println("2. Language Reports by Continent");
            System.out.println("3. Language Reports by Region");
            System.out.println("4. Language Reports by Country");
            System.out.println("0. Back");
            System.out.print("Choose an option: ");

            switch (scanner.nextLine().trim()) {
                case "1" -> showGlobalLanguageReports();
                case "2" -> showContinentalLanguageReports();
                case "3" -> showRegionalLanguageReports();
                case "4" -> showCountryLanguageReports();
                case "0" -> running = false;
                default -> System.out.println("Invalid choice.");
            }
        }
        breadcrumb.pop();
    }

    private void showGlobalLanguageReports() {
        breadcrumb.push("Global Language Reports");
        boolean running = true;
        while (running) {
            showBreadcrumb();
            System.out.println("\n--- Global Language Reports ---");
            System.out.println("1. All spoken languages globally (most → least)");
            System.out.println("2. Top N spoken languages");
            System.out.println("0. Back");
            System.out.print("Choose an option: ");

            switch (scanner.nextLine().trim()) {
                case "1" -> printLanguages(languageDAO.getLanguagesByPopulation());
                case "2" -> {
                    System.out.print("Enter N: ");
                    int n = Integer.parseInt(scanner.nextLine());
                    printLanguages(languageDAO.getLanguagesByPopulation(n));
                }
                case "0" -> running = false;
                default -> System.out.println("Invalid choice.");
            }
        }
        breadcrumb.pop();
    }

    private void showContinentalLanguageReports() {
        breadcrumb.push("Continental Language Reports");
        boolean running = true;
        while (running) {
            showBreadcrumb();
            System.out.println("\n--- Continental Language Reports ---");
            System.out.println("1. All languages in a continent");
            System.out.println("2. Top N languages in a continent");
            System.out.println("0. Back");
            System.out.print("Choose an option: ");

            switch (scanner.nextLine().trim()) {
                case "1" -> {
                    String continent = chooseContinent();
                    if (continent != null) printLanguages(languageDAO.getLanguagesByContinent(continent));
                }
                case "2" -> {
                    String continent = chooseContinent();
                    if (continent != null) {
                        System.out.print("Enter N: ");
                        int n = Integer.parseInt(scanner.nextLine());
                        printLanguages(languageDAO.getLanguagesByContinent(continent, n));
                    }
                }
                case "0" -> running = false;
                default -> System.out.println("Invalid choice.");
            }
        }
        breadcrumb.pop();
    }

    private void showRegionalLanguageReports() {
        breadcrumb.push("Regional Language Reports");
        boolean running = true;
        while (running) {
            showBreadcrumb();
            System.out.println("\n--- Regional Language Reports ---");
            System.out.println("1. All languages in a region");
            System.out.println("2. Top N languages in a region");
            System.out.println("0. Back");
            System.out.print("Choose an option: ");

            switch (scanner.nextLine().trim()) {
                case "1" -> {
                    String region = chooseRegion();
                    if (region != null) printLanguages(languageDAO.getLanguagesByRegion(region));
                }
                case "2" -> {
                    String region = chooseRegion();
                    if (region != null) {
                        System.out.print("Enter N: ");
                        int n = Integer.parseInt(scanner.nextLine());
                        printLanguages(languageDAO.getLanguagesByRegion(region, n));
                    }
                }
                case "0" -> running = false;
                default -> System.out.println("Invalid choice.");
            }
        }
        breadcrumb.pop();
    }

    private void showCountryLanguageReports() {
        breadcrumb.push("Country Language Reports");
        boolean running = true;
        while (running) {
            showBreadcrumb();
            System.out.println("\n--- Country Language Reports ---");
            System.out.println("1. All languages in a country");
            System.out.println("2. Top N languages in a country");
            System.out.println("0. Back");
            System.out.print("Choose an option: ");

            switch (scanner.nextLine().trim()) {
                case "1" -> {
                    String countryCode = chooseCountry();
                    if (countryCode != null) printLanguages(languageDAO.getLanguagesByCountryCode(countryCode));
                }
                case "2" -> {
                    String countryCode = chooseCountry();
                    if (countryCode != null) {
                        System.out.print("Enter N: ");
                        int n = Integer.parseInt(scanner.nextLine());
                        printLanguages(languageDAO.getLanguagesByCountryCode(countryCode, n));
                    }
                }
                case "0" -> running = false;
                default -> System.out.println("Invalid choice.");
            }
        }
        breadcrumb.pop();
    }


    // --- Populations ---
    private void showPopulations() {
        breadcrumb.push("Population Distribution");
        boolean running = true;
        while (running) {
            showBreadcrumb();
            System.out.println("\n--- Population Distribution ---");
            System.out.println("1. Global Population");
            System.out.println("2. Population by Continent");
            System.out.println("3. Population by Region");
            System.out.println("4. Population by Country");
            System.out.println("0. Back");
            System.out.print("Choose an option: ");

            switch (scanner.nextLine().trim()) {
                case "1" -> showGlobalPopulationReports();
                case "2" -> showContinentalPopulationReports();
                case "3" -> showRegionalPopulationReports();
                case "4" -> showCountryPopulationReports();
                case "0" -> running = false;
                default -> System.out.println("Invalid choice.");
            }
        }
        breadcrumb.pop();
    }

    private void showGlobalPopulationReports() {
        breadcrumb.push("Global Population Reports");
        boolean running = true;
        while (running) {
            showBreadcrumb();
            System.out.println("\n--- Global Population Reports ---");
            System.out.println("1. All populations (largest → smallest)");
            System.out.println("2. Top N populations");
            System.out.println("0. Back");
            System.out.print("Choose an option: ");

            switch (scanner.nextLine().trim()) {
                case "1" -> printPopulations("Global", populationDAO.getGlobalPopulations());
                case "2" -> {
                    System.out.print("Enter N: ");
                    int n = Integer.parseInt(scanner.nextLine());
                    printPopulations("Global", populationDAO.getGlobalPopulations(n));
                }
                case "0" -> running = false;
                default -> System.out.println("Invalid choice.");
            }
        }
        breadcrumb.pop();
    }

    private void showContinentalPopulationReports() {
        breadcrumb.push("Continental Population Reports");
        boolean running = true;
        while (running) {
            showBreadcrumb();
            System.out.println("\n--- Continental Population Reports ---");
            System.out.println("1. All populations in a continent");
            //System.out.println("2. Top N populations in a continent");
            System.out.println("0. Back");
            System.out.print("Choose an option: ");

            switch (scanner.nextLine().trim()) {
                case "1" -> {
                    String continent = chooseContinent();
                    if (continent != null) printPopulations("Continent", populationDAO.getContinentPopulations(continent));
                }
                /*case "2" -> {
                    String continent = chooseContinent();
                    if (continent != null) {
                        System.out.print("Enter N: ");
                        int n = Integer.parseInt(scanner.nextLine());
                        printPopulations("Continent", populationDAO.getContinentPopulations(continent, n));
                    }
                }*/
                case "0" -> running = false;
                default -> System.out.println("Invalid choice.");
            }
        }
        breadcrumb.pop();
    }

    private void showRegionalPopulationReports() {
        breadcrumb.push("Regional Population Reports");
        boolean running = true;
        while (running) {
            showBreadcrumb();
            System.out.println("\n--- Regional Population Reports ---");
            System.out.println("1. All populations in a region");
            //System.out.println("2. Top N populations in a region");
            System.out.println("0. Back");
            System.out.print("Choose an option: ");

            switch (scanner.nextLine().trim()) {
                case "1" -> {
                    String region = chooseRegion();
                    if (region != null) printPopulations("Region", populationDAO.getRegionPopulations(region));
                }
                /*case "2" -> {
                    String region = chooseRegion();
                    if (region != null) {
                        System.out.print("Enter N: ");
                        int n = Integer.parseInt(scanner.nextLine());
                        printPopulations("Region", populationDAO.getRegionPopulations(region, n));
                    }
                }*/
                case "0" -> running = false;
                default -> System.out.println("Invalid choice.");
            }
        }
        breadcrumb.pop();
    }

    private void showCountryPopulationReports() {
        breadcrumb.push("Country Population Reports");
        boolean running = true;
        while (running) {
            showBreadcrumb();
            System.out.println("\n--- Country Population Reports ---");
            System.out.println("1. All populations in a country");
            //System.out.println("2. Top N populations in a country");
            System.out.println("0. Back");
            System.out.print("Choose an option: ");

            switch (scanner.nextLine().trim()) {
                case "1" -> {
                    String country = chooseCountry();
                    if (country != null) printPopulations("Country", populationDAO.getCountryPopulationByCode(country));
                }
                /*case "2" -> {
                    String country = chooseCountry();
                    if (country != null) {
                        System.out.print("Enter N: ");
                        int n = Integer.parseInt(scanner.nextLine());
                        printPopulations("Country", populationDAO.getCountryPopulationByCode(country, n));
                    }
                }*/
                case "0" -> running = false;
                default -> System.out.println("Invalid choice.");
            }
        }
        breadcrumb.pop();
    }


    // --- Print Utilities ---
    private void printCountries(List<Country> countries) {
        TablePrinter tp = new TablePrinter("Code", "Name", "Continent", "Region", "Population", "Capital");
        countries.forEach(c -> tp.addRow(c.getCode(), c.getName(), c.getContinent(), c.getRegion(), c.getPopulation(), c.getCapital()));
        tp.print(20, scanner);
    }

    private void printCities(List<City> cities) {
        TablePrinter tp = new TablePrinter("Name", "Country", "District", "Population");
        cities.forEach(c -> tp.addRow(c.getName(), c.getCountry(), c.getDistrict(), c.getPopulation()));
        tp.print(20, scanner);
    }

    private void printCapitalCities(List<CapitalCity> cities) {
        TablePrinter tp = new TablePrinter("Name", "Country", "Continent", "Region", "Population");
        cities.forEach(c -> tp.addRow(
                c.getName(),
                c.getCountry(),
                c.getContinent(),
                c.getRegion(),
                c.getPopulation()
        ));
        tp.print(20, scanner);
    }

    private void printPopulations(String label, List<Population> populations) {
        TablePrinter tp = new TablePrinter(
                label,
                "Total Population",
                "Living in Cities",
                "Living in Cities (%)",
                "Not Living in Cities",
                "Not Living in Cities (%)"
        );

        populations.forEach(p -> tp.addRow(
                p.getLabel(),
                p.getTotalPopulation(),
                p.getCityPopulation(),
                String.format("%.2f%%", p.getCityPercentage()),
                p.getNonCityPopulation(),
                String.format("%.2f%%", p.getNonCityPercentage())
        ));

        tp.print(20, scanner);
    }

    private void printLanguages(List<Language> reports) {
        if (reports == null || reports.isEmpty()) {
            System.out.println("No language data available.");
            return;
        }

        // Dynamically decide headers based on what the Language model exposes
        TablePrinter tp;

        // Example: if continent/region/country is included in the report
        boolean hasContinent = reports.get(0).isContinent();
        boolean hasRegion    = reports.get(0).isRegion();
        boolean hasCountry   = reports.get(0).isCountry();

        if (hasContinent == true) {
            tp = new TablePrinter("Continent", "Language", "Speakers", "% of Continent", "% of Global");
            reports.forEach(r -> tp.addRow(
                    r.getContinent(),
                    r.getLanguage(),
                    r.getSpeakers(),
                    r.getPercentOfContinentPopulation(),
                    r.getPercentOfGlobalPopulation()
            ));
            tp.print(20, scanner);
        } else if (hasRegion == true) {
            tp = new TablePrinter("Region", "Language", "Speakers", "% of Region", "% of Global");
            reports.forEach(r -> tp.addRow(
                    r.getRegion(),
                    r.getLanguage(),
                    r.getSpeakers(),
                    r.getPercentOfRegionPopulation(),
                    r.getPercentOfGlobalPopulation()
            ));
            tp.print(20, scanner);
        } else if (hasCountry == true) {
            tp = new TablePrinter("Country", "Language", "Speakers", "% of Country", "% of Global");
            reports.forEach(r -> tp.addRow(
                    r.getCountry(),
                    r.getLanguage(),
                    r.getSpeakers(),
                    r.getPercentOfCountryPopulation(),
                    r.getPercentOfGlobalPopulation()
            ));
            tp.print(20, scanner);
        } else {
            // Default: global report
            tp = new TablePrinter("Language", "Speakers (Global)", "% of Global Population");
            reports.forEach(r -> tp.addRow(
                    r.getLanguage(),
                    r.getSpeakers(),
                    r.getPercentOfGlobalPopulation()
            ));
        }

        tp.print(20, scanner);
    }


    // --- Input/Selection Utilities ---
    private String chooseContinent() {
        List<Lookup> continents = lookupDAO.getAllContinents();
        for (int i = 0; i < continents.size(); i++) {
            System.out.println((i + 1) + ". " + continents.get(i).getValue());
        }
        System.out.print("Select a continent (0 = Cancel): ");
        int choice = Integer.parseInt(scanner.nextLine());
        return choice == 0 ? null : continents.get(choice - 1).getValue();
    }

    private String chooseRegion() {
        List<Lookup> regions = lookupDAO.getAllRegions();
        for (int i = 0; i < regions.size(); i++) {
            System.out.println((i + 1) + ". " + regions.get(i).getValue());
        }
        System.out.print("Select a region (0 = Cancel): ");
        int choice = Integer.parseInt(scanner.nextLine());
        return choice == 0 ? null : regions.get(choice - 1).getValue();
    }

    private String chooseCountry() {
        List<Lookup> countries = lookupDAO.getAllCountries();

        System.out.println("\n--- Available Countries ---");
        for (Lookup c : countries) {
            System.out.printf("%-5s : %s%n", c.getType(), c.getValue());
        }

        System.out.print("Enter a country code (0 = Cancel): ");
        String input = scanner.nextLine().trim().toUpperCase();

        if (input.equals("0")) return null;

        boolean exists = countries.stream()
                .anyMatch(c -> c.getType().equalsIgnoreCase(input));
        if (!exists) {
            System.out.println("Invalid country code. Try again.");
            return chooseCountry();
        }

        return input;
    }

    private String chooseDistrict(String countryCode) {
        List<Lookup> districts = lookupDAO.getDistrictsByCountryCode(countryCode);

        if (districts.isEmpty()) {
            System.out.println("No districts found for this country.");
            return null;
        }

        System.out.println("\n--- Available Districts in " + countryCode + " ---");
        for (int i = 0; i < districts.size(); i++) {
            System.out.println((i + 1) + ". " + districts.get(i).getValue());
        }

        System.out.print("Select a district (0 = Cancel): ");
        int choice;
        try {
            choice = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a number.");
            return chooseDistrict(countryCode); // retry
        }

        if (choice == 0) return null;
        if (choice < 1 || choice > districts.size()) {
            System.out.println("Invalid choice. Try again.");
            return chooseDistrict(countryCode); // retry
        }

        return districts.get(choice - 1).getValue();
    }

    private void showBreadcrumb() {
        System.out.println("\n" + String.join(" > ", breadcrumb));
    }
}
