package com.napier.devops.dao;

import com.napier.devops.models.Lookup;
import java.sql.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Data Access Object (DAO) for managing lookup/reference data queries.
 * Provides methods to retrieve geographical information (continents, regions, countries, districts)
 * from the database with optional search functionality.
 */
public class LookupDAO {
    private final Connection conn;
    private static final Logger logger = Logger.getLogger(CountryDAO.class.getName());

    /**
     * Constructs a LookupDAO with the provided database connection.
     * @param conn Database connection to be used for all queries
     */
    public LookupDAO(Connection conn) {
        this.conn = conn;
    }

    // --- GET methods ---
    /**
     * Retrieves all distinct continents from the database.
     * @return List of Lookup objects containing continent names
     */
    public List<Lookup> getAllContinents() {
        String sql = "SELECT DISTINCT Continent FROM country ORDER BY Continent";
        return querySingleColumnLookup(sql, "Continent");
    }

    /**
     * Retrieves all distinct regions from the database.
     * @return List of Lookup objects containing region names
     */
    public List<Lookup> getAllRegions() {
        String sql = "SELECT DISTINCT Region FROM country ORDER BY Region";
        return querySingleColumnLookup(sql, "Region");
    }

    /**
     * Retrieves all countries with their codes from the database.
     * @return List of Lookup objects containing country codes and names
     */
    public List<Lookup> getAllCountries() {
        String sql = "SELECT Code, Name FROM country ORDER BY Name";
        return queryCountryLookup(sql);
    }

    /**
     * Retrieves all distinct districts from the database.
     * @return List of Lookup objects containing district names
     */
    public List<Lookup> getAllDistricts() {
        String sql = "SELECT DISTINCT District FROM city ORDER BY District";
        return querySingleColumnLookup(sql, "District");
    }

    /**
     * Retrieves districts filtered by country code.
     * @param countryCode The ISO country code to filter by
     * @return List of Lookup objects containing districts for the specified country
     */
    public List<Lookup> getDistrictsByCountryCode(String countryCode) {
        String sql = "SELECT DISTINCT District FROM city WHERE CountryCode = ? ORDER BY District";
        List<Lookup> list = new ArrayList<>();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, countryCode);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                list.add(new Lookup("District", rs.getString("District")));
            }
        } catch (SQLException e) {
            System.err.println("Error fetching districts: " + e.getMessage());
        }
        return list;
    }

    /**
     * Retrieves districts filtered by country name.
     * Joins the city and country tables to match districts by country name.
     * @param countryName The name of the country to filter by
     * @return List of Lookup objects containing districts for the specified country
     */
    public List<Lookup> getDistrictsByCountryName(String countryName) {
        String sql = "SELECT DISTINCT district FROM city JOIN country ON city.CountryCode=country.Code WHERE country.Name= ? ORDER BY District";
        List<Lookup> list = new ArrayList<>();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, countryName);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                list.add(new Lookup("District", rs.getString("District")));
            }
        } catch (SQLException e) {
            System.err.println("Error fetching districts: " + e.getMessage());
        }
        return list;
    }

    // --- Search methods ---
    /**
     * Searches for continents matching the provided search term (case-insensitive).
     * @param term The search term to match against continent names
     * @return List of Lookup objects containing matching continents
     */
    public List<Lookup> searchContinents(String term) {
        String sql = "SELECT DISTINCT Continent FROM country WHERE Continent LIKE ? ORDER BY Continent";
        return querySingleColumnLookup(sql, "Continent", "%" + term + "%");
    }

    /**
     * Searches for regions matching the provided search term (case-insensitive).
     * @param term The search term to match against region names
     * @return List of Lookup objects containing matching regions
     */
    public List<Lookup> searchRegions(String term) {
        String sql = "SELECT DISTINCT Region FROM country WHERE Region LIKE ? ORDER BY Region";
        return querySingleColumnLookup(sql, "Region", "%" + term + "%");
    }

    /**
     * Searches for countries matching the provided search term (case-insensitive).
     * @param term The search term to match against country names
     * @return List of Lookup objects containing matching countries with their codes
     */
    public List<Lookup> searchCountries(String term) {
        String sql = "SELECT Code, Name FROM country WHERE Name LIKE ? ORDER BY Name";
        return queryCountryLookup(sql, "%" + term + "%");
    }

    /**
     * Searches for districts matching the provided search term (case-insensitive).
     * @param term The search term to match against district names
     * @return List of Lookup objects containing matching districts
     */
    public List<Lookup> searchDistricts(String term) {
        String sql = "SELECT DISTINCT District FROM city WHERE District LIKE ? ORDER BY District";
        return querySingleColumnLookup(sql, "District", "%" + term + "%");
    }

    // --- Shared query helpers ---
    /**
     * Helper method to execute queries that return a single column of data.
     * Used for lookups like continents, regions, and districts.
     * @param sql The SQL query to execute
     * @param type The type identifier for the Lookup object (e.g., "Continent", "Region")
     * @param params Variable number of query parameters to bind to prepared statement
     * @return List of Lookup objects containing the query results
     */
    private List<Lookup> querySingleColumnLookup(String sql, String type, Object... params) {
        List<Lookup> results = new ArrayList<>();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            // Bind all parameters to the prepared statement
            for (int i = 0; i < params.length; i++) {
                stmt.setObject(i + 1, params[i]);
            }
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                // Extract first column and create Lookup object with provided type
                results.add(new Lookup(type, rs.getString(1)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return results;
    }

    /**
     * Helper method to execute queries that return country data (Code and Name).
     * Specialized for country lookups where both code and name are needed.
     * @param sql The SQL query to execute (should select Code and Name columns)
     * @param params Variable number of query parameters to bind to prepared statement
     * @return List of Lookup objects containing country codes and names
     */
    private List<Lookup> queryCountryLookup(String sql, Object... params) {
        List<Lookup> results = new ArrayList<>();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            // Bind all parameters to the prepared statement
            for (int i = 0; i < params.length; i++) {
                stmt.setObject(i + 1, params[i]);
            }
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String code = rs.getString("Code");
                String name = rs.getString("Name");
                // Create Lookup with code as type and name as value
                results.add(new Lookup(code, name));
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        }
        return results;
    }
}
