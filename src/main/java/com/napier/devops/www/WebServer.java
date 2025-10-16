package com.napier.devops.www;

import static spark.Spark.*;

import com.google.gson.Gson;
import com.napier.devops.dao.*;
import com.napier.devops.db.DatabaseManager;
import com.napier.devops.models.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class WebServer {
    private final CountryDAO countryDAO;
    private final CityDAO cityDAO;
    private final CapitalCityDAO capitalDAO;
    private final PopulationDAO populationDAO;
    private final LanguageDAO languageDAO;
    private final Gson gson = new Gson();

    public WebServer() throws SQLException {
        Connection conn = DatabaseManager.getConnection();
        this.countryDAO = new CountryDAO(conn);
        this.cityDAO = new CityDAO(conn);
        this.capitalDAO = new CapitalCityDAO(conn);
        this.populationDAO = new PopulationDAO(conn);
        this.languageDAO = new LanguageDAO(conn);
    }
    public void start() {
        port(8081);
        staticFiles.location("/public");

        // Root
        get("/", (req, res) -> "🌍 World Reporting App is running on port 8081");

        // --- Countries ---
        get("/reports/countries", (req, res) -> {
            res.type("application/json");
            String scope = req.queryParams("scope");   // continent, region, etc.
            int limit = parseLimit(req.queryParams("limit"));

            List<Country> countries;
            if ("continent".equalsIgnoreCase(scope)) {
                String continent = req.queryParams("name"); // e.g. ?scope=continent&name=Asia
                countries = countryDAO.getCountriesInContinent(continent, limit);
            } else if ("region".equalsIgnoreCase(scope)) {
                String region = req.queryParams("name");
                countries = countryDAO.getCountriesInRegion(region, limit);
            } else {
                countries = countryDAO.getCountriesByPopulation(limit);
            }
            return gson.toJson(countries);
        });

        // --- Cities ---
        get("/reports/cities", (req, res) -> {
            res.type("application/json");
            String scope = req.queryParams("scope");
            int limit = parseLimit(req.queryParams("limit"));

            List<City> cities;
            if ("continent".equalsIgnoreCase(scope)) {
                cities = cityDAO.getCitiesInContinent(req.queryParams("name"), limit);
            } else if ("country".equalsIgnoreCase(scope)) {
                cities = cityDAO.getCitiesInCountry(req.queryParams("name"), limit);
            } else {
                cities = cityDAO.getCitiesByPopulation(limit);
            }
            return gson.toJson(cities);
        });

        // --- Capitals ---
        get("/reports/capitals", (req, res) -> {
            res.type("application/json");
            String scope = req.queryParams("scope");
            int limit = parseLimit(req.queryParams("limit"));

            List<CapitalCity> capitals;
            if ("region".equalsIgnoreCase(scope)) {
                capitals = capitalDAO.getCapitalCitiesInRegion(req.queryParams("name"), limit);
            } else {
                capitals = capitalDAO.getCapitalCitiesByPopulation(limit);
            }
            return gson.toJson(capitals);
        });

        // --- Populations ---
        get("/reports/populations", (req, res) -> {
            res.type("application/json");
            String scope = req.queryParams("scope"); // continent, region, country
            List<Population> pops = null;

            if ("continent".equalsIgnoreCase(scope)) {
                pops = populationDAO.getContinentPopulations();
            }
            if ("region".equalsIgnoreCase(scope)) {
                pops = populationDAO.getRegionPopulations();
            }
            if ("country".equalsIgnoreCase(scope)) {
                pops = populationDAO.getCountryPopulations();
            }

            return gson.toJson(pops);
        });

        // --- Languages ---
        get("/reports/languages", (req, res) -> {
            res.type("application/json");
            String scope = req.queryParams("scope");
            int limit = parseLimit(req.queryParams("limit"));

            List<Language> langs;
            if ("continent".equalsIgnoreCase(scope)) {
                langs = languageDAO.getLanguagesByContinent(req.queryParams("name"));
            } else if ("country".equalsIgnoreCase(scope)) {
                langs = languageDAO.getLanguagesByCountry(req.queryParams("name"));
            } else {
                langs = languageDAO.getLanguagesByPopulation();
            }

            // Apply limit manually if DAO doesn’t support it
            if (limit > 0 && langs.size() > limit) {
                langs = langs.subList(0, limit);
            }
            return gson.toJson(langs);
        });
    }

    private int parseLimit(String limitParam) {
        try {
            return limitParam != null ? Integer.parseInt(limitParam) : 10;
        } catch (NumberFormatException e) {
            return 10;
        }
    }
}
