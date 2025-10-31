package com.napier.devops.www;

import static spark.Spark.*;

import com.google.gson.Gson;
import com.napier.devops.dao.*;
import com.napier.devops.db.DatabaseManager;
import com.napier.devops.models.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WebServer {
    private final CountryDAO countryDAO;
    private final CityDAO cityDAO;
    private final CapitalCityDAO capitalDAO;
    private final PopulationDAO populationDAO;
    private final LanguageDAO languageDAO;
    private final LookupDAO lookupDAO;
    private final Gson gson = new Gson();

    public WebServer() throws SQLException {
        Connection conn = DatabaseManager.getConnection();
        this.countryDAO = new CountryDAO(conn);
        this.cityDAO = new CityDAO(conn);
        this.capitalDAO = new CapitalCityDAO(conn);
        this.populationDAO = new PopulationDAO(conn);
        this.languageDAO = new LanguageDAO(conn);
        this.lookupDAO = new LookupDAO(conn);
    }
    public void start() {
        port(8081);
        staticFiles.location("/public");

        // Root
        get("/", (req, res) -> "World Reporting App is running on port 8081");

        // --- Countries ---
        get("/reports/countries", (req, res) -> {
            res.type("application/json");
            String scope = req.queryParams("scope");   // global, continent, region
            String name  = req.queryParams("name");    // e.g. "Asia"
            int limit    = parseLimit(req.queryParams("limit"));

            List<Country> countries;
            if ("continent".equalsIgnoreCase(scope)) {
                if(limit < 0) {
                    if ( name == null || name.isEmpty() ) {
                        countries = countryDAO.getCountriesInContinent();
                    } else {
                        countries = countryDAO.getCountriesInContinent(name);
                    }
                }
                else {
                    if ( name == null || name.isEmpty() ) {
                        countries = countryDAO.getCountriesInContinent(limit);
                    } else {
                        countries = countryDAO.getCountriesInContinent(name, limit);
                    }
                }
            } else if ("region".equalsIgnoreCase(scope)) {
                if(limit < 0) {
                    if ( name == null || name.isEmpty() ) {
                        countries = countryDAO.getCountriesInRegion();
                    } else {
                        countries = countryDAO.getCountriesInRegion(name);
                    }
                }
                else {
                    if ( name == null || name.isEmpty() ) {
                        countries = countryDAO.getCountriesInRegion(limit);
                    } else {
                        countries = countryDAO.getCountriesInRegion(name, limit);
                    }
                }
            } else {
                if (limit < 0) {
                    countries = countryDAO.getCountriesByPopulation();
                }
                else {
                    countries = countryDAO.getCountriesByPopulation(limit);
                }
            }
            return gson.toJson(countries);
        });

        // --- Cities ---
        get("/reports/cities", (req, res) -> {
            res.type("application/json");
            String scope = req.queryParams("scope");   // global, continent, region, country, district
            String name  = req.queryParams("name");
            String district = req.queryParams("district");
            int limit = parseLimit(req.queryParams("limit"));

            List<City> cities;
            if ("continent".equalsIgnoreCase(scope)) {
                if(limit < 0) {
                    if (name == null || name.isEmpty() ) {
                        cities = cityDAO.getCitiesByContinent();
                    }
                    else {
                        cities = cityDAO.getCitiesInContinent(name);
                    }
                }
                else {
                    if (name == null || name.isEmpty() ) {
                        cities = cityDAO.getCitiesByContinent(limit);
                    }
                    else {
                        cities = cityDAO.getCitiesInContinent(name, limit);
                    }
                }
            } else if ("country".equalsIgnoreCase(scope)) {
                if(limit < 0) {
                    if( district == null || district.equals("") ) {
                        cities = cityDAO.getCitiesInCountryByName(name);
                    }
                    else {
                        cities = cityDAO.getCitiesInDistrict(district);
                    }
                }
                else {
                    if( district == null || district.equals("") ) {
                        cities = cityDAO.getCitiesInCountryByName(name, limit);
                    }
                    else {
                        cities = cityDAO.getCitiesInDistrict(district, limit);
                    }
                }
            } else if ("region".equalsIgnoreCase(scope)) {
                if(limit < 0) {
                    cities = cityDAO.getCitiesInRegion(name);
                }
                else {
                    cities = cityDAO.getCitiesInRegion(name, limit);
                }
            } else if ("district".equalsIgnoreCase(scope)) {
                if(limit < 0) {
                    cities = cityDAO.getCitiesInDistrict(name);
                }
                else {
                    cities = cityDAO.getCitiesInDistrict(name, limit);
                }
            } else {
                if (limit < 0) {
                    cities = cityDAO.getCitiesByPopulation();
                }
                else {
                    cities = cityDAO.getCitiesByPopulation(limit);
                }
            }
            return gson.toJson(cities);
        });

        // --- Capitals ---
        get("/reports/capitals", (req, res) -> {
            res.type("application/json");
            String scope = req.queryParams("scope");   // global, continent, region
            String name  = req.queryParams("name");
            int limit = parseLimit(req.queryParams("limit"));

            List<CapitalCity> capitals;
            if ("continent".equalsIgnoreCase(scope)) {
                if (limit < 0) {
                    if ( name == null || name.isEmpty() ) {
                        capitals = capitalDAO.getCapitalCitiesByPopulation();
                    }
                    else {
                        capitals = capitalDAO.getCapitalCitiesInContinent(name);
                    }
                }
                else {
                    if (name == null || name.isEmpty() ) {
                        capitals = capitalDAO.getCapitalCitiesByPopulation(limit);
                    }
                    else {
                        capitals = capitalDAO.getCapitalCitiesInContinent(name, limit);
                    }
                }
            } else if ("region".equalsIgnoreCase(scope)) {
                if (limit < 0) {
                    if ( name == null || name.isEmpty() ) {
                        capitals = capitalDAO.getCapitalCitiesByPopulation();
                    }
                    else {
                        capitals = capitalDAO.getCapitalCitiesInRegion(name);
                    }
                }
                else {
                    if (name == null || name.isEmpty() ) {
                        capitals = capitalDAO.getCapitalCitiesByPopulation(limit);
                    }
                    else {
                        capitals = capitalDAO.getCapitalCitiesInRegion(name, limit);
                    }
                }
            } else {
                if (limit < 0) {
                    capitals = capitalDAO.getCapitalCitiesByPopulation();
                }
                else {
                    capitals = capitalDAO.getCapitalCitiesByPopulation(limit);
                }
            }
            return gson.toJson(capitals);
        });

// --- Populations ---
        get("/reports/populations", (req, res) -> {
            res.type("application/json");
            String scope = req.queryParams("scope");   // global, continent, region, country
            String name  = req.queryParams("name");
            int limit = parseLimit(req.queryParams("limit"));

            List<Population> pops;
            if ("continent".equalsIgnoreCase(scope)) {
                if(limit < 0) {
                    if (name == null || name.isEmpty()) {
                        // Fetch populations grouped at the global/continent level
                        pops = populationDAO.getGlobalPopulations();
                    } else {
                        // If a specific continent name is provided, fetch that continent’s population
                        pops = populationDAO.getContinentPopulations(name);
                    }
                }
                else {
                    if (name == null || name.isEmpty()) {
                        // Fetch populations grouped at the global/continent level
                        pops = populationDAO.getGlobalPopulations(limit);
                    } else {
                        // If a specific continent name is provided, fetch that continent’s population
                        pops = populationDAO.getContinentPopulations(name, limit);
                    }
                }
            } else if ("region".equalsIgnoreCase(scope)) {
                if (limit < 0) {
                    if (name == null || name.isEmpty()) {
                        // If a specific region name is not provided, fetch all region populations
                        pops = populationDAO.getRegionPopulations();
                    } else {
                        // Otherwise, fetch that region’s population
                        pops = populationDAO.getRegionPopulations(name);
                    }
                }
                else {
                    if (name == null || name.isEmpty()) {
                        // If a specific region name is not provided, fetch all region populations
                        pops = populationDAO.getRegionPopulations(limit);
                    } else {
                        // Otherwise, fetch all region populations
                        pops = populationDAO.getRegionPopulations(name, limit);
                    }
                }
            } else if ("country".equalsIgnoreCase(scope)) {
                if (limit < 0) {
                    if (name == null || name.isEmpty()) {
                        // If a specific country name is provided, fetch that country’s population
                        pops = populationDAO.getCountryPopulations();
                    } else {
                        // Otherwise, fetch all country populations
                        pops = populationDAO.getCountryPopulations(name);
                    }
                }
                else {
                    if (name == null || name.isEmpty()) {
                        // If a specific country name is provided, fetch that country’s population
                        pops = populationDAO.getCountryPopulations(limit);
                    } else {
                        // Otherwise, fetch all country populations
                        pops = populationDAO.getCountryPopulations(name, limit);
                    }
                }
            } else {
                // Default: return global populations (grouped by continent)
                pops = populationDAO.getGlobalPopulations();
            }


            Map<String,Object> response = new HashMap<>();
            // Sum across all rows for global context
            long globalPop = populationDAO.getGlobalPopulation();
            response.put("globalPopulation", globalPop);
            response.put("data", pops);

            return gson.toJson(response);
        });

        // --- Languages ---
        get("/reports/languages", (req, res) -> {
            res.type("application/json");
            String scope = req.queryParams("scope");   // global, continent, region, country
            String name  = req.queryParams("name");
            int limit = parseLimit(req.queryParams("limit"));

            List<Language> langs;
            if ("continent".equalsIgnoreCase(scope)) {
                if(limit < 0) {
                    if (name == null || name.isEmpty()) {
                        langs = languageDAO.getLanguagesByPopulation();
                    } else {
                        langs = languageDAO.getLanguagesByContinent(name);
                    }
                }
                else {
                    if (name == null || name.isEmpty()) {
                        langs = languageDAO.getLanguagesByPopulation(limit);
                    } else {
                        langs = languageDAO.getLanguagesByContinent(name, limit);
                    }
                }
            } else if ("region".equalsIgnoreCase(scope)) {
                if (limit < 0) {
                    if (name == null || name.isEmpty()) {
                        langs = languageDAO.getLanguagesByPopulation();
                    } else {
                        langs = languageDAO.getLanguagesByRegion(name);
                    }
                }
                else {
                    if (name == null || name.isEmpty()) {
                        langs = languageDAO.getLanguagesByPopulation(limit);
                    } else {
                        langs = languageDAO.getLanguagesByRegion(name, limit);
                    }
                }
            } else if ("country".equalsIgnoreCase(scope)) {
                if (limit < 0) {
                    if (name == null || name.isEmpty()) {
                        langs = languageDAO.getLanguagesByPopulation();
                    } else {
                        langs = languageDAO.getLanguagesByCountry(name);
                    }
                }
                else {
                    if (name == null || name.isEmpty()) {
                        langs = languageDAO.getLanguagesByPopulation(limit);
                    } else {
                        langs = languageDAO.getLanguagesByCountry(name, limit);
                    }
                }
            } else {
                langs = languageDAO.getLanguagesByPopulation();
            }

            // Apply limit manually if DAO doesn’t support it
            if (limit > 0 && langs.size() > limit) {
                langs = langs.subList(0, limit);
            }
            return gson.toJson(langs);
        });


// --- Lookups ---

// Continents
        get("/lookups/continents", (req, res) -> {
            res.type("application/json");
            List<Lookup> continents = lookupDAO.getAllContinents();
            return gson.toJson(continents);
        });

// Regions
        get("/lookups/regions", (req, res) -> {
            res.type("application/json");
            List<Lookup> regions = lookupDAO.getAllRegions();
            return gson.toJson(regions);
        });

// Countries
        get("/lookups/countries", (req, res) -> {
            res.type("application/json");
            List<Lookup> countries = lookupDAO.getAllCountries();
            return gson.toJson(countries);
        });

// Districts (requires country name)
        get("/lookups/districts", (req, res) -> {
            res.type("application/json");
            String countryName = req.queryParams("country");
            if (countryName == null || countryName.isEmpty()) {
                res.status(400);
                return gson.toJson(Collections.singletonMap("error", "Missing country parameter"));
            }
            List<Lookup> districts = lookupDAO.getDistrictsByCountryName(countryName);
            return gson.toJson(districts);
        });


    }

    private int parseLimit(String limitParam) {
        try {
            return limitParam != null ? Integer.parseInt(limitParam) : -1;
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}
