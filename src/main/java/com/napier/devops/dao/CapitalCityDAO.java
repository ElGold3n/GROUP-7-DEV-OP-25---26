package com.napier.devops.dao;

import com.napier.devops.models.CapitalCity;
import java.sql.*;
import java.util.*;

/**
 * Data Access Object (DAO) for retrieving capital city information from the database.
 * Provides methods to query capital cities filtered by various criteria such as
 * population, continent, and region, with optional limits on result count.
 */
public class CapitalCityDAO {
    private final Connection conn;

    /**
     * Constructs a CapitalCityDAO with the provided database connection.
     *
     * @param conn the database connection to use for queries
     */
    public CapitalCityDAO(Connection conn) {
        this.conn = conn;
    }

    /**
     * Retrieves all capital cities ordered by population in descending order.
     *
     * @return a list of CapitalCity objects sorted by population (highest first)
     */
    public List<CapitalCity> getCapitalCitiesByPopulation() {
        String sql = "SELECT city.ID, city.Name, country.Name AS Country, country.Continent, country.Region, city.Population " +
                "FROM country JOIN city ON country.Capital = city.ID " +
                "ORDER BY city.Population DESC";
        return queryCapitals(sql);
    }

    /**
     * Retrieves the top N capital cities ordered by population in descending order.
     *
     * @param n the maximum number of results to return
     * @return a list of up to N CapitalCity objects sorted by population (highest first)
     */
    public List<CapitalCity> getCapitalCitiesByPopulation(int n) {
        String sql = "SELECT city.ID, city.Name, country.Name AS Country, country.Continent, country.Region, city.Population " +
                "FROM country JOIN city ON country.Capital = city.ID " +
                "ORDER BY city.Population DESC LIMIT ?";
        return queryCapitals(sql, n);
    }

    /**
     * Retrieves all capital cities in a specific continent, ordered by population.
     *
     * @param continent the continent name to filter by
     * @return a list of CapitalCity objects in the specified continent sorted by population (highest first)
     */
    public List<CapitalCity> getCapitalCitiesInContinent(String continent) {
        String sql = "SELECT city.ID, city.Name,  country.Name AS Country, country.Continent, country.Region, city.Population " +
                "FROM country JOIN city ON country.Capital = city.ID " +
                "WHERE country.Continent = ? ORDER BY city.Population DESC";
        return queryCapitals(sql, continent);
    }

    /**
     * Retrieves the top N capital cities in a specific continent, ordered by population.
     *
     * @param continent the continent name to filter by
     * @param n the maximum number of results to return
     * @return a list of up to N CapitalCity objects in the specified continent sorted by population (highest first)
     */
    public List<CapitalCity> getCapitalCitiesInContinent(String continent, int n) {
        String sql = "SELECT city.ID, city.Name,  country.Name AS Country, country.Continent, country.Region, city.Population " +
                "FROM country JOIN city ON country.Capital = city.ID " +
                "WHERE country.Continent = ? ORDER BY city.Population DESC LIMIT ?";
        return queryCapitals(sql, continent, n);
    }

    /**
     * Retrieves all capital cities in a specific region, ordered by population.
     *
     * @param region the region name to filter by
     * @return a list of CapitalCity objects in the specified region sorted by population (highest first)
     */
    public List<CapitalCity> getCapitalCitiesInRegion(String region) {
        String sql = "SELECT city.ID, city.Name,  country.Name AS Country, country.Continent, country.Region, city.Population " +
                "FROM country JOIN city ON country.Capital = city.ID " +
                "WHERE country.Region = ? ORDER BY city.Population DESC";
        return queryCapitals(sql, region);
    }

    /**
     * Retrieves the top N capital cities in a specific region, ordered by population.
     *
     * @param region the region name to filter by
     * @param n the maximum number of results to return
     * @return a list of up to N CapitalCity objects in the specified region sorted by population (highest first)
     */
    public List<CapitalCity> getCapitalCitiesInRegion(String region, int n) {
        String sql = "SELECT city.ID, city.Name,  country.Name AS Country, country.Continent, country.Region, city.Population " +
                "FROM country JOIN city ON country.Capital = city.ID " +
                "WHERE country.Region = ? ORDER BY city.Population DESC LIMIT ?";
        return queryCapitals(sql, region, n);
    }

    /**
     * Executes a prepared SQL query and maps the results to CapitalCity objects.
     * This is a helper method used by all public query methods.
     *
     * @param sql the SQL query string (may contain ? placeholders for parameters)
     * @param params variable number of parameters to bind to the prepared statement
     * @return a list of CapitalCity objects populated from the query results;
     *         returns an empty list if no results are found or an error occurs
     */
    private List<CapitalCity> queryCapitals(String sql, Object... params) {
        List<CapitalCity> results = new ArrayList<>();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            // Bind each parameter to the prepared statement (1-indexed)
            for (int i=0; i<params.length; i++) stmt.setObject(i+1, params[i]);
            // Execute the query and process results
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                CapitalCity c = new CapitalCity();
                c.setName(rs.getString("Name"));
                c.setCountry(rs.getString("Country"));
                c.setContinent(rs.getString("Continent"));
                c.setRegion(rs.getString("Region"));
                c.setPopulation(rs.getLong("Population"));
                results.add(c);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return results;
    }
}
