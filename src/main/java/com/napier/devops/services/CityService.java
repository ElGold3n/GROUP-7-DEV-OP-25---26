package com.napier.devops.services;

import java.sql.*;
import java.util.*;

/**
 * Service for querying city information from the database.
 * All queries return cities sorted by population in descending order.
 */
public class CityService {
    private final Connection conn;

    public CityService(Connection conn) {
        this.conn = conn;
    }

    public List<Map<String,Object>> getAllCitiesByPopulation() {
        String sql = "SELECT city.ID, city.Name, country.Name AS Country, District, city.Population " +
                "FROM city JOIN country ON city.CountryCode = country.Code " +
                "ORDER BY city.Population DESC";
        return queryCities(sql);
    }

    public List<Map<String,Object>> getCitiesInContinent(String continent) {
        String sql = "SELECT city.ID, city.Name, country.Name AS Country, District, city.Population " +
                "FROM city JOIN country ON city.CountryCode = country.Code " +
                "WHERE country.Continent=? ORDER BY city.Population DESC";
        return queryCities(sql, continent);
    }

    public List<Map<String,Object>> getCitiesInRegion(String region) {
        String sql = "SELECT city.ID, city.Name, country.Name AS Country, District, city.Population " +
                "FROM city JOIN country ON city.CountryCode = country.Code " +
                "WHERE country.Region=? ORDER BY city.Population DESC";
        return queryCities(sql, region);
    }

    public List<Map<String,Object>> getCitiesInCountry(String country) {
        String sql = "SELECT city.ID, city.Name, country.Name AS Country, District, city.Population " +
                "FROM city JOIN country ON city.CountryCode = country.Code " +
                "WHERE country.Name=? ORDER BY city.Population DESC";
        return queryCities(sql, country);
    }

    public List<Map<String,Object>> getCitiesInDistrict(String district) {
        String sql = "SELECT city.ID, city.Name, country.Name AS Country, District, city.Population " +
                "FROM city JOIN country ON city.CountryCode = country.Code " +
                "WHERE city.District=? ORDER BY city.Population DESC";
        return queryCities(sql, district);
    }

    public List<Map<String,Object>> getTopNCities(int n) {
        String sql = "SELECT city.ID, city.Name, country.Name AS Country, District, city.Population " +
                "FROM city JOIN country ON city.CountryCode = country.Code " +
                "ORDER BY city.Population DESC LIMIT ?";
        return queryCities(sql, n);
    }

    public void printAllCitiesByPopulation() { getAllCitiesByPopulation().forEach(System.out::println); }
    public void printCitiesInContinent(String continent) { getCitiesInContinent(continent).forEach(System.out::println); }
    public void printCitiesInRegion(String region) { getCitiesInRegion(region).forEach(System.out::println); }
    public void printCitiesInCountry(String country) { getCitiesInCountry(country).forEach(System.out::println); }
    public void printCitiesInDistrict(String district) { getCitiesInDistrict(district).forEach(System.out::println); }
    public void printTopNCities(int n) { getTopNCities(n).forEach(System.out::println); }

    /**
     * Runs a SQL query and returns city data as a list of maps.
     *
     * @param sql SQL query with ? placeholders for parameters
     * @param params Values to fill in the placeholders
     * @return List of cities, or empty list if query fails
     */
    private List<Map<String,Object>> queryCities(String sql, Object... params) {
        List<Map<String,Object>> results = new ArrayList<>();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            for (int i=0; i<params.length; i++) stmt.setObject(i+1, params[i]);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Map<String,Object> row = new HashMap<>();
                row.put("ID", rs.getInt("ID"));
                row.put("Name", rs.getString("Name"));
                row.put("Country", rs.getString("Country"));
                row.put("District", rs.getString("District"));
                row.put("Population", rs.getLong("Population"));
                results.add(row);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return results;
    }
}