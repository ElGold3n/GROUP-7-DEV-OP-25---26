package com.napier.devops.dao;

import com.napier.devops.models.Language;
import java.sql.*;
import java.util.*;

/**
 * Data Access Object (DAO) for querying language statistics and population data.
 * Provides methods to retrieve language information grouped by various geographic scopes:
 * global, continent, region, and country.
 */
public class LanguageDAO {
    private final Connection conn;

    /**
     * Constructs a LanguageDAO with a database connection.
     * @param conn The database connection to use for queries
     */
    public LanguageDAO(Connection conn) {
        this.conn = conn;
    }

    /**
     * Retrieves all languages ranked by total number of speakers globally.
     * @return List of Language objects sorted by total_speakers in descending order
     */
    public List<Language> getLanguagesByPopulation() {
        String sql = "SELECT cl.Language, " +
                "SUM(co.Population * cl.Percentage / 100) AS total_speakers, " +
                "(SELECT SUM(co.Population * cl.Percentage / 100) * 100) " +
                " / " +
                "(SELECT SUM(Population) FROM country) " +
                "AS percent_of_global " +
                "FROM countrylanguage cl " +
                "JOIN country co ON cl.CountryCode = co.Code " +
                "GROUP BY cl.Language " +
                "ORDER BY total_speakers DESC";
        return queryLanguages(sql);
    }

    /**
     * Retrieves the top N languages ranked by total number of speakers globally.
     * @param n The maximum number of languages to return
     * @return List of up to N Language objects sorted by total_speakers in descending order
     */
    public List<Language> getLanguagesByPopulation(int n) {
        String sql = "SELECT cl.Language, " +
                "SUM(co.Population * cl.Percentage / 100) AS total_speakers, " +
                "(SELECT SUM(co.Population * cl.Percentage / 100) * 100) " +
                " / " +
                "(SELECT SUM(Population) FROM country) " +
                "AS percent_of_global " +
                "FROM countrylanguage cl " +
                "JOIN country co ON cl.CountryCode = co.Code " +
                "GROUP BY cl.Language " +
                "ORDER BY total_speakers DESC " +
                "LIMIT ?";
        return queryLanguages(sql, n);
    }

    /**
     * Retrieves all languages spoken in a specific continent, ranked by speaker count.
     * Includes both continental and global percentage metrics.
     * @param continent The continent name to filter by (e.g., "Europe", "Asia")
     * @return List of Language objects for the specified continent
     */
    public List<Language> getLanguagesByContinent(String continent) {
        String sql = "SELECT co.Continent AS Continent, cl.Language, " +
                "SUM(co.Population * cl.Percentage / 100) AS total_speakers, " +
                "(SUM(co.Population * cl.Percentage / 100) * 100.0) / " +
                "(SELECT SUM(c1.Population) FROM country c1 WHERE c1.Continent = co.Continent) AS percent_of_continent, " +
                "(SUM(co.Population * cl.Percentage / 100) * 100.0) / " +
                "(SELECT SUM(Population) FROM country) AS percent_of_global " +
                "FROM countrylanguage cl " +
                "JOIN country co ON cl.CountryCode = co.Code " +
                "WHERE co.Continent = ?" +
                "GROUP BY co.Continent, cl.Language " +
                "ORDER BY co.Continent, total_speakers DESC";
        return queryLanguages(sql, continent);
    }

    /**
     * Retrieves the top N languages spoken in a specific continent.
     * @param continent The continent name to filter by
     * @param n The maximum number of languages to return per continent
     * @return List of up to N Language objects for the specified continent
     */
    public List<Language> getLanguagesByContinent(String continent, int n) {
        String sql = "SELECT co.Continent AS Continent, cl.Language, " +
                "SUM(co.Population * cl.Percentage / 100) AS total_speakers, " +
                "(SUM(co.Population * cl.Percentage / 100) * 100.0) / " +
                "(SELECT SUM(c1.Population) FROM country c1 WHERE c1.Continent = co.Continent) AS percent_of_continent, " +
                "(SUM(co.Population * cl.Percentage / 100) * 100.0) / " +
                "(SELECT SUM(Population) FROM country) AS percent_of_global " +
                "FROM countrylanguage cl " +
                "JOIN country co ON cl.CountryCode = co.Code " +
                "WHERE co.Continent = ?" +
                "GROUP BY co.Continent, cl.Language " +
                "ORDER BY co.Continent, total_speakers DESC " +
                "LIMIT ?";
        return queryLanguages(sql, continent, n);
    }

    /**
     * Retrieves all languages spoken in a specific region, ranked by speaker count.
     * Includes both regional and global percentage metrics.
     * @param region The region name to filter by
     * @return List of Language objects for the specified region
     */
    public List<Language> getLanguagesByRegion(String region) {
        String sql = "SELECT co.Region AS Region, cl.Language, " +
                "SUM(co.Population * cl.Percentage / 100) AS total_speakers, " +
                "(SUM(co.Population * cl.Percentage / 100) * 100.0) / " +
                "(SELECT SUM(c1.Population) FROM country c1 WHERE c1.Region = co.Region) AS percent_of_region, " +
                "(SUM(co.Population * cl.Percentage / 100) * 100.0) / " +
                "(SELECT SUM(Population) FROM country) AS percent_of_global " +
                "FROM countrylanguage cl " +
                "JOIN country co ON cl.CountryCode = co.Code " +
                "WHERE co.Region = ?" +
                "GROUP BY co.Region, cl.Language " +
                "ORDER BY co.Region, total_speakers DESC";
        return queryLanguages(sql, region);
    }

    /**
     * Retrieves the top N languages spoken in a specific region.
     * @param region The region name to filter by
     * @param n The maximum number of languages to return per region
     * @return List of up to N Language objects for the specified region
     */
    public List<Language> getLanguagesByRegion(String region, int n) {
        String sql = "SELECT co.Region AS Region, cl.Language, " +
                "SUM(co.Population * cl.Percentage / 100) AS total_speakers, " +
                "(SUM(co.Population * cl.Percentage / 100) * 100.0) / " +
                "(SELECT SUM(c1.Population) FROM country c1 WHERE c1.Region = co.Region) AS percent_of_region, " +
                "(SUM(co.Population * cl.Percentage / 100) * 100.0) / " +
                "(SELECT SUM(Population) FROM country) AS percent_of_global " +
                "FROM countrylanguage cl " +
                "JOIN country co ON cl.CountryCode = co.Code " +
                "WHERE co.Region = ?" +
                "GROUP BY co.Region, cl.Language " +
                "ORDER BY co.Region, total_speakers DESC " +
                "LIMIT ?";
        return queryLanguages(sql, region, n);
    }

    /**
     * Retrieves all languages spoken in a specific country (by name).
     * Includes country-level, continental, and global percentage metrics.
     * @param country The country name to filter by
     * @return List of Language objects for the specified country
     */
    public List<Language> getLanguagesByCountry(String country) {
        String sql = "SELECT co.Name AS Country, cl.Language, " +
                "SUM(co.Population * cl.Percentage / 100) AS total_speakers, " +
                "(SUM(co.Population * cl.Percentage / 100) * 100.0) / " +
                "(SELECT c1.Population FROM country c1 WHERE c1.Code = co.Code) AS percent_of_country, " +
                "(SUM(co.Population * cl.Percentage / 100) * 100.0) / " +
                "(SELECT SUM(Population) FROM country) AS percent_of_global " +
                "FROM countrylanguage cl " +
                "JOIN country co ON cl.CountryCode = co.Code " +
                "WHERE co.Name = ? " +
                "GROUP BY co.Code, co.Name, cl.Language " +
                "ORDER BY co.Name, total_speakers DESC";
        return queryLanguages(sql, country);
    }

    /**
     * Retrieves the top N languages spoken in a specific country (by name).
     * @param country The country name to filter by
     * @param n The maximum number of languages to return
     * @return List of up to N Language objects for the specified country
     */
    public List<Language> getLanguagesByCountry(String country, int n) {
        String sql = "SELECT co.Name AS Country, cl.Language, " +
                "SUM(co.Population * cl.Percentage / 100) AS total_speakers, " +
                "(SUM(co.Population * cl.Percentage / 100) * 100.0) / " +
                "(SELECT c1.Population FROM country c1 WHERE c1.Code = co.Code) AS percent_of_country, " +
                "(SUM(co.Population * cl.Percentage / 100) * 100.0) / " +
                "(SELECT SUM(Population) FROM country) AS percent_of_global " +
                "FROM countrylanguage cl " +
                "JOIN country co ON cl.CountryCode = co.Code " +
                "WHERE co.Name = ? " +
                "GROUP BY co.Code, co.Name, cl.Language " +
                "ORDER BY co.Name, total_speakers DESC " +
                "LIMIT ?";
        return queryLanguages(sql, country, n);
    }

    /**
     * Retrieves all languages spoken in a specific country (by country code).
     * Country codes are typically ISO 3-letter codes (e.g., "USA", "GBR").
     * @param country The country code to filter by
     * @return List of Language objects for the specified country code
     */
    public List<Language> getLanguagesByCountryCode(String country) {
        String sql = "SELECT co.Name AS Country, cl.Language, " +
                "SUM(co.Population * cl.Percentage / 100) AS total_speakers, " +
                "(SUM(co.Population * cl.Percentage / 100) * 100.0) / " +
                "(SELECT c1.Population FROM country c1 WHERE c1.Code = co.Code) AS percent_of_country, " +
                "(SUM(co.Population * cl.Percentage / 100) * 100.0) / " +
                "(SELECT SUM(Population) FROM country) AS percent_of_global " +
                "FROM countrylanguage cl " +
                "JOIN country co ON cl.CountryCode = co.Code " +
                "WHERE co.Code = ? " +
                "GROUP BY co.Code, co.Name, cl.Language " +
                "ORDER BY co.Name, total_speakers DESC";
        return queryLanguages(sql, country);
    }

    /**
     * Retrieves the top N languages spoken in a specific country (by country code).
     * @param country The country code to filter by
     * @param n The maximum number of languages to return
     * @return List of up to N Language objects for the specified country code
     */
    public List<Language> getLanguagesByCountryCode(String country, int n) {
        String sql = "SELECT co.Name AS Country, cl.Language, " +
                "SUM(co.Population * cl.Percentage / 100) AS total_speakers, " +
                "(SUM(co.Population * cl.Percentage / 100) * 100.0) / " +
                "(SELECT c1.Population FROM country c1 WHERE c1.Code = co.Code) AS percent_of_country, " +
                "(SUM(co.Population * cl.Percentage / 100) * 100.0) / " +
                "(SELECT SUM(Population) FROM country) AS percent_of_global " +
                "FROM countrylanguage cl " +
                "JOIN country co ON cl.CountryCode = co.Code " +
                "WHERE co.Code = ? " +
                "GROUP BY co.Code, co.Name, cl.Language " +
                "ORDER BY co.Name, total_speakers DESC " +
                "LIMIT ?";
        return queryLanguages(sql, country, n);
    }

    /**
     * Queries the total world population from the country table.
     * Used to calculate global percentage metrics for all language queries.
     * @return The sum of all country populations, or 0 if query fails
     */
    private long getGlobalPopulation() {
        String sql = "SELECT SUM(Population) AS WorldPop FROM country";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getLong("WorldPop");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * Core query execution method that processes language statistics.
     * Dynamically detects the query scope (continent, region, country, or global)
     * by checking which geographic columns are present in the result set,
     * then constructs appropriate Language objects with the correct metrics.
     * 
     * @param sql The prepared SQL statement
     * @param params Variable arguments to bind to the PreparedStatement (? placeholders)
     * @return List of Language objects populated from the query result set
     */
    private List<Language> queryLanguages(String sql, Object... params) {
        List<Language> results = new ArrayList<>();

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            for (int i = 0; i < params.length; i++) {
                stmt.setObject(i + 1, params[i]);
            }

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    // Detect which scope columns are present
                    String continentType = safeGet(rs, "Continent");
                    String regionType    = safeGet(rs, "Region");
                    String countryType   = safeGet(rs, "Country");

                    String language = rs.getString("Language");
                    long speakers = rs.getLong("total_speakers");
                    String percentOfGlobal = String.format("%.2f", rs.getDouble("percent_of_global"));


                    if (continentType != null) {
                        String percentOfContinent = String.format("%.2f", rs.getDouble("percent_of_continent"));
                        results.add(new Language("Continent", language, speakers, percentOfContinent, percentOfGlobal, rs.getString("Continent")));
                    } else if (regionType != null) {
                        String percentOfRegion = String.format("%.2f", rs.getDouble("percent_of_region"));
                        results.add(new Language("Region", language, speakers, percentOfRegion, percentOfGlobal, rs.getString("Region")));
                    } else if (countryType != null) {
                        String percentOfCountry = String.format("%.2f", rs.getDouble("percent_of_country"));
                        results.add(new Language("Country", language, speakers, percentOfCountry, percentOfGlobal, rs.getString("Country")));
                    } else {
                        // Default: global report
                        results.add(new Language(language, speakers, percentOfGlobal));
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Inject global population into each Language object if needed
        long worldPop = getGlobalPopulation();
        results.forEach(l -> l.setGlobalPopulation(worldPop));

        return results;
    }

    /**
     * Utility method to safely check if a column exists in the ResultSet.
     * Prevents SQLException when attempting to access columns that don't exist
     * in the result set (useful for detecting optional geographic columns).
     * 
     * @param rs The ResultSet to check
     * @param column The column name to search for
     * @return The column name if found, null if the column doesn't exist
     */
    private String safeGet(ResultSet rs, String column) {
        try {
            rs.findColumn(column); // throws if not present
            return column;
        } catch (SQLException e) {
            return null;
        }
    }

}
