package com.napier.devops.services;

import java.sql.*;
import java.util.*;

public class PopulationService {
    private final Connection conn;

    public PopulationService(Connection conn) {
        this.conn = conn;
    }

    // --- GET methods ---
    public List<Map<String,Object>> getContinentPopulations() {
        String sql = "SELECT Continent, SUM(Population) AS Population " +
                "FROM country GROUP BY Continent ORDER BY Population DESC";
        return queryPopulation(sql, "Continent");
    }

    public List<Map<String,Object>> getRegionPopulations() {
        String sql = "SELECT Region, SUM(Population) AS Population " +
                "FROM country GROUP BY Region ORDER BY Population DESC";
        return queryPopulation(sql, "Region");
    }

    public List<Map<String,Object>> getCountryPopulations() {
        String sql = "SELECT Name, Population FROM country ORDER BY Population DESC";
        return queryPopulation(sql, "Name");
    }

    // --- PRINT wrappers ---
    public void printContinentPopulations() {
        getContinentPopulations().forEach(System.out::println);
    }

    public void printRegionPopulations() {
        getRegionPopulations().forEach(System.out::println);
    }

    public void printCountryPopulations() {
        getCountryPopulations().forEach(System.out::println);
    }

    // --- Shared query helper ---
    private List<Map<String,Object>> queryPopulation(String sql, String label) {
        List<Map<String,Object>> results = new ArrayList<>();
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Map<String,Object> row = new HashMap<>();
                row.put(label, rs.getString(1));
                row.put("Population", rs.getLong(2));
                results.add(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return results;
    }
}
