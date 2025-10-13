package com.napier.devops.dao;

import com.napier.devops.models.Lookup;
import java.sql.*;
import java.util.*;

public class LookupDAO {
    private final Connection conn;

    public LookupDAO(Connection conn) {
        this.conn = conn;
    }

    // --- GET methods ---
    public List<Lookup> getAllContinents() {
        String sql = "SELECT DISTINCT Continent FROM country ORDER BY Continent";
        return querySingleColumnLookup(sql, "Continent");
    }

    public List<Lookup> getAllRegions() {
        String sql = "SELECT DISTINCT Region FROM country ORDER BY Region";
        return querySingleColumnLookup(sql, "Region");
    }

    public List<Lookup> getAllCountries() {
        String sql = "SELECT Code, Name FROM country ORDER BY Name";
        return queryCountryLookup(sql);
    }

    public List<Lookup> getAllDistricts() {
        String sql = "SELECT DISTINCT District FROM city ORDER BY District";
        return querySingleColumnLookup(sql, "District");
    }

    public List<Lookup> getDistrictsByCountry(String countryCode) {
        String sql = "SELECT DISTINCT District FROM city WHERE CountryCode = ? ORDER BY District";
        List<Lookup> list = new ArrayList<>();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, countryCode);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                list.add(new Lookup("District", rs.getString("District")));
            }
        } catch (SQLException e) {
            System.err.println("❌ Error fetching districts: " + e.getMessage());
        }
        return list;
    }

    // --- Search methods ---
    public List<Lookup> searchContinents(String term) {
        String sql = "SELECT DISTINCT Continent FROM country WHERE Continent LIKE ? ORDER BY Continent";
        return querySingleColumnLookup(sql, "Continent", "%" + term + "%");
    }

    public List<Lookup> searchRegions(String term) {
        String sql = "SELECT DISTINCT Region FROM country WHERE Region LIKE ? ORDER BY Region";
        return querySingleColumnLookup(sql, "Region", "%" + term + "%");
    }

    public List<Lookup> searchCountries(String term) {
        String sql = "SELECT Code, Name FROM country WHERE Name LIKE ? ORDER BY Name";
        return queryCountryLookup(sql, "%" + term + "%");
    }

    public List<Lookup> searchDistricts(String term) {
        String sql = "SELECT DISTINCT District FROM city WHERE District LIKE ? ORDER BY District";
        return querySingleColumnLookup(sql, "District", "%" + term + "%");
    }

    // --- Shared query helpers ---
    private List<Lookup> querySingleColumnLookup(String sql, String type, Object... params) {
        List<Lookup> results = new ArrayList<>();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            for (int i = 0; i < params.length; i++) {
                stmt.setObject(i + 1, params[i]);
            }
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                results.add(new Lookup(type, rs.getString(1)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return results;
    }

    private List<Lookup> queryCountryLookup(String sql, Object... params) {
        List<Lookup> results = new ArrayList<>();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            for (int i = 0; i < params.length; i++) {
                stmt.setObject(i + 1, params[i]);
            }
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String code = rs.getString("Code");
                String name = rs.getString("Name");
                // Store "Code" as type, "Name" as value, or combine if you prefer
                results.add(new Lookup(code, name));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return results;
    }
}
