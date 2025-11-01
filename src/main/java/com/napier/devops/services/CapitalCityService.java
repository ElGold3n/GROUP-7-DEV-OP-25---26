package com.napier.devops.services;

import java.sql.*;
import java.util.*;

/**
 * Service for querying capital city information from the database.
 * All queries return capitals sorted by population in descending order.
 */
public class CapitalCityService {
    private final Connection conn;

    public CapitalCityService(Connection conn) {
        this.conn = conn;
    }

    public List<Map<String,Object>> getAllCapitalCitiesByPopulation() {
        String sql = "SELECT city.ID, city.Name, country.Name AS Country, city.Population " +
                "FROM city JOIN country ON city.ID = country.Capital " +
                "ORDER BY city.Population DESC";
        return queryCapitals(sql);
    }

    public List<Map<String,Object>> getCapitalCitiesInContinent(String continent) {
        String sql = "SELECT city.ID, city.Name, country.Name AS Country, city.Population " +
                "FROM city JOIN country ON city.ID = country.Capital " +
                "WHERE country.Continent=? ORDER BY city.Population DESC";
        return queryCapitals(sql, continent);
    }

    public List<Map<String,Object>> getCapitalCitiesInRegion(String region) {
        String sql = "SELECT city.ID, city.Name, country.Name AS Country, city.Population " +
                "FROM city JOIN country ON city.ID = country.Capital " +
                "WHERE country.Region=? ORDER BY city.Population DESC";
        return queryCapitals(sql, region);
    }

    public List<Map<String,Object>> getTopNCapitalCities(int n) {
        String sql = "SELECT city.ID, city.Name, country.Name AS Country, city.Population " +
                "FROM city JOIN country ON city.ID = country.Capital " +
                "ORDER BY city.Population DESC LIMIT ?";
        return queryCapitals(sql, n);
    }

    // --- PRINT wrappers ---
    public void printAllCapitalCitiesByPopulation() { getAllCapitalCitiesByPopulation().forEach(System.out::println); }
    public void printCapitalCitiesInContinent(String continent) { getCapitalCitiesInContinent(continent).forEach(System.out::println); }
    public void printCapitalCitiesInRegion(String region) { getCapitalCitiesInRegion(region).forEach(System.out::println); }
    public void printTopNCapitalCities(int n) { getTopNCapitalCities(n).forEach(System.out::println); }

    /**
     * Runs a SQL query and returns capital city data as a list of maps.
     *
     * @param sql SQL query with ? placeholders for parameters
     * @param params Values to fill in the placeholders
     * @return List of capital cities, or empty list if query fails
     */
    private List<Map<String,Object>> queryCapitals(String sql, Object... params) {
        List<Map<String,Object>> results = new ArrayList<>();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            for (int i=0; i<params.length; i++) stmt.setObject(i+1, params[i]);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Map<String,Object> row = new HashMap<>();
                row.put("ID", rs.getInt("ID"));
                row.put("Name", rs.getString("Name"));
                row.put("Country", rs.getString("Country"));
                row.put("Population", rs.getLong("Population"));
                results.add(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return results;
    }
}