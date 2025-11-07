package com.napier.devops.dao;

import com.napier.devops.models.Country;
import java.sql.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Data Access Object (DAO) for Country entities.
 * Handles all the database operations related to country queries,
 * including filtering by population, continent, and region.
 */
public class CountryDAO {
    private final Connection conn;
    private static final Logger logger = Logger.getLogger(CountryDAO.class.getName());

    /**
     * Constructs a CountryDAO with the provided database connection.
     * 
     * @param conn the database connection to use for queries
     */
    public CountryDAO(Connection conn) {
        this.conn = conn;
    }

    /**
     * Retrieves all countries sorted by population in descending order.
     * 
     * @return a list of all countries ordered by population
     */
    public List<Country> getCountriesByPopulation() {
        String sql = "SELECT country.Code, country.Name, country.Continent, country.Region, country.Population, city.Name AS Capital " +
                "FROM country LEFT JOIN city ON country.Capital=city.ID ORDER BY country.Population DESC";
        return queryCountries(sql);
    }

    /**
     * Retrieves the top N countries by population.
     * 
     * @param n the maximum number of countries to return
     * @return a list of up to N countries ordered by population
     */
    public List<Country> getCountriesByPopulation(int n) {
        String sql = "SELECT country.Code, country.Name, country.Continent, country.Region, country.Population, city.Name AS Capital " +
                "FROM country LEFT JOIN city ON country.Capital=city.ID ORDER BY country.Population DESC LIMIT ?";
        return queryCountries(sql, n);
    }

    /**
     * Retrieves all countries grouped by continent and sorted by population.
     * 
     * @return a list of all countries ordered by continent, then population
     */
    public List<Country> getCountriesInContinent() {
        String sql = "SELECT country.Code, country.Name, country.Continent AS Continent, country.Region, country.Population, city.Name AS Capital " +
                "FROM country LEFT JOIN city ON country.Capital=city.ID " +
                "ORDER BY UPPER(country.Continent) ASC, country.Population DESC";
        return queryCountries(sql);
    }

    /**
     * Retrieves the top N countries grouped by continent and sorted by population.
     * 
     * @param n the maximum number of countries to return
     * @return a list of up to N countries ordered by continent, then population
     */
    public List<Country> getCountriesInContinent(int n) {
        String sql = "SELECT country.Code, country.Name, country.Continent, country.Region, country.Population, city.Name AS Capital " +
                "FROM country LEFT JOIN city ON country.Capital=city.ID " +
                "ORDER BY UPPER(country.Continent) ASC, country.Population DESC LIMIT ?";
        return queryCountries(sql, n);
    }

    /**
     * Retrieves all countries in a specific continent sorted by population.
     * 
     * @param continent the continent to filter by
     * @return a list of countries in the specified continent
     */
    public List<Country> getCountriesInContinent(String continent) {
        String sql = "SELECT country.Code, country.Name, country.Continent, country.Region, country.Population, city.Name AS Capital " +
                "FROM country LEFT JOIN city ON country.Capital=city.ID " +
                "WHERE Continent=? ORDER BY country.Population DESC";
        return queryCountries(sql, continent);
    }

    /**
     * Retrieves the top N countries in a specific continent sorted by population.
     * 
     * @param continent the continent to filter by
     * @param n the maximum number of countries to return
     * @return a list of up to N countries in the specified continent
     */
    public List<Country> getCountriesInContinent(String continent, int n) {
        String sql = "SELECT country.Code, country.Name, country.Continent, country.Region, country.Population, city.Name AS Capital " +
                "FROM country LEFT JOIN city ON country.Capital=city.ID " +
                "WHERE Continent=? ORDER BY country.Population DESC LIMIT ?";
        return queryCountries(sql, continent, n);
    }

    /**
     * Retrieves all countries grouped by region and sorted by population.
     * 
     * @return a list of all countries ordered by region, then population
     */
    public List<Country> getCountriesInRegion() {
        String sql = "SELECT country.Code, country.Name, country.Continent, country.Region, country.Population, city.Name AS Capital " +
                "FROM country LEFT JOIN city ON country.Capital=city.ID " +
                "ORDER BY UPPER(TRIM(country.Region)) ASC, country.Population DESC";
        return queryCountries(sql);
    }

    /**
     * Retrieves the top N countries grouped by region and sorted by population.
     * 
     * @param n the maximum number of countries to return
     * @return a list of up to N countries ordered by region, then population
     */
    public List<Country> getCountriesInRegion(int n) {
        String sql = "SELECT country.Code, country.Name, country.Continent, country.Region, country.Population, city.Name AS Capital " +
                "FROM country LEFT JOIN city ON country.Capital=city.ID " +
                "ORDER BY UPPER(TRIM(country.Region)) ASC, country.Population DESC LIMIT ?";
        return queryCountries(sql, n);
    }

    /**
     * Retrieves all countries in a specific region sorted by population.
     * 
     * @param region the region to filter by
     * @return a list of countries in the specified region
     */
    public List<Country> getCountriesInRegion(String region) {
        String sql = "SELECT country.Code, country.Name, country.Continent, country.Region, country.Population, city.Name AS Capital " +
                "FROM country LEFT JOIN city ON country.Capital=city.ID WHERE Region=? ORDER BY country.Population DESC";
        return queryCountries(sql, region);
    }

    /**
     * Retrieves the top N countries in a specific region sorted by population.
     * 
     * @param region the region to filter by
     * @param n the maximum number of countries to return
     * @return a list of up to N countries in the specified region
     */
    public List<Country> getCountriesInRegion(String region, int n) {
        String sql = "SELECT country.Code, country.Name, country.Continent, country.Region, country.Population, city.Name AS Capital " +
                "FROM country LEFT JOIN city ON country.Capital=city.ID WHERE Region=? ORDER BY country.Population DESC Limit ?";
        return queryCountries(sql, region, n);
    }

    /**
     * Executes a query and maps the results to Country objects.
     * Handles parameterized queries to prevent SQL injection.
     * 
     * @param sql the SQL query to execute
     * @param params variable arguments to bind to the query (? placeholders)
     * @return a list of Country objects from the query results, or an empty list if an error occurs
     */
    private List<Country> queryCountries(String sql, Object... params) {
        List<Country> results = new ArrayList<>();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            // Bind parameters to the prepared statement
            for (int i=0; i<params.length; i++) stmt.setObject(i+1, params[i]);
            ResultSet rs = stmt.executeQuery();
            // Map each result row to a Country object
            while (rs.next()) {
                Country c = new Country(
                rs.getString("Code"),
                rs.getString("Name"),
                rs.getString("Continent"),
                rs.getString("Region"),
                rs.getLong("Population"),
                rs.getString("Capital")
                );
                results.add(c);
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        }
        return results;
    }
}
