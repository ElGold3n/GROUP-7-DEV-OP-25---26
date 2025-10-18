package com.napier.devops.services;

import java.sql.*;
import java.util.*;

public class CountryService {
    private final Connection conn;

    public CountryService(Connection conn) {
        this.conn = conn;
    }

    public List<Map<String,Object>> getAllCountriesByPopulation() {
        String sql = "SELECT Code, Name, Continent, Region, Population, Capital " +
                "FROM country ORDER BY Population DESC";
        return queryCountries(sql);
    }

    public List<Map<String,Object>> getCountriesInContinent(String continent) {
        String sql = "SELECT Code, Name, Continent, Region, Population, Capital " +
                "FROM country WHERE Continent=? ORDER BY Population DESC";
        return queryCountries(sql, continent);
    }

    public List<Map<String,Object>> getCountriesInRegion(String region) {
        String sql = "SELECT Code, Name, Continent, Region, Population, Capital " +
                "FROM country WHERE Region=? ORDER BY Population DESC";
        return queryCountries(sql, region);
    }

    public List<Map<String,Object>> getTopNCountries(int n) {
        String sql = "SELECT Code, Name, Continent, Region, Population, Capital " +
                "FROM country ORDER BY Population DESC LIMIT ?";
        return queryCountries(sql, n);
    }

    public void printAllCountriesByPopulation() { getAllCountriesByPopulation().forEach(System.out::println); }
    public void printCountriesInContinent(String continent) { getCountriesInContinent(continent).forEach(System.out::println); }
    public void printCountriesInRegion(String region) { getCountriesInRegion(region).forEach(System.out::println); }
    public void printTopNCountries(int n) { getTopNCountries(n).forEach(System.out::println); }

    private List<Map<String,Object>> queryCountries(String sql, Object... params) {
        List<Map<String,Object>> results = new ArrayList<>();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            for (int i=0; i<params.length; i++) stmt.setObject(i+1, params[i]);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Map<String,Object> row = new HashMap<>();
                row.put("Code", rs.getString("Code"));
                row.put("Name", rs.getString("Name"));
                row.put("Continent", rs.getString("Continent"));
                row.put("Region", rs.getString("Region"));
                row.put("Population", rs.getLong("Population"));
                row.put("Capital", rs.getInt("Capital"));
                results.add(row);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return results;
    }
}
