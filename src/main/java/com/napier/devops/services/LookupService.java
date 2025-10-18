package com.napier.devops.services;

import java.sql.*;
import java.util.*;

public class LookupService {
    private final Connection conn;

    public LookupService(Connection conn) {
        this.conn = conn;
    }

    // --- GET methods ---
    public List<String> getAllContinents() {
        String sql = "SELECT DISTINCT Continent FROM country ORDER BY Continent";
        return queryLookup(sql);
    }

    public List<String> getAllRegions() {
        String sql = "SELECT DISTINCT Region FROM country ORDER BY Region";
        return queryLookup(sql);
    }

    public List<String> getAllCountries() {
        String sql = "SELECT Name FROM country ORDER BY Name";
        return queryLookup(sql);
    }

    public List<String> getAllDistricts() {
        String sql = "SELECT DISTINCT District FROM city ORDER BY District";
        return queryLookup(sql);
    }

    // --- Search methods ---
    public List<String> searchContinents(String term) {
        String sql = "SELECT DISTINCT Continent FROM country WHERE Continent LIKE ? ORDER BY Continent";
        return queryLookup(sql, "%" + term + "%");
    }

    public List<String> searchRegions(String term) {
        String sql = "SELECT DISTINCT Region FROM country WHERE Region LIKE ? ORDER BY Region";
        return queryLookup(sql, "%" + term + "%");
    }

    public List<String> searchCountries(String term) {
        String sql = "SELECT Name FROM country WHERE Name LIKE ? ORDER BY Name";
        return queryLookup(sql, "%" + term + "%");
    }

    public List<String> searchDistricts(String term) {
        String sql = "SELECT DISTINCT District FROM city WHERE District LIKE ? ORDER BY District";
        return queryLookup(sql, "%" + term + "%");
    }


    // --- Shared query helper ---
    private List<String> queryLookup(String sql, Object... params) {
        List<String> results = new ArrayList<>();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            for (int i=0; i<params.length; i++) {
                stmt.setObject(i+1, params[i]);
            }
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                results.add(rs.getString(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return results;
    }
}
