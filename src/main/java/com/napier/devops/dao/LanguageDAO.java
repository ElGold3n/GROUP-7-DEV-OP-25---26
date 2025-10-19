package com.napier.devops.dao;

import com.napier.devops.models.Language;
import java.sql.*;
import java.util.*;

public class LanguageDAO {
    private final Connection conn;

    public LanguageDAO(Connection conn) {
        this.conn = conn;
    }

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
     * Utility to safely get a column value if it exists in the ResultSet.
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
