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
        String sql = "SELECT countrylanguage.Language, SUM(country.Population * countrylanguage.Percentage / 100) AS Speakers, " +
                "SUM(country.Population) AS TotalPopulation FROM countrylanguage " +
                "JOIN country ON countrylanguage.CountryCode = country.Code GROUP BY countrylanguage.Language ORDER BY Speakers DESC";
        return queryLanguages(sql);
    }

    public List<Language> getLanguagesByContinent(String continent) {
        String sql = "SELECT countrylanguage.Language, SUM(country.Population * countrylanguage.Percentage / 100) AS Speakers, " +
                "SUM(country.Population) AS TotalPopulation FROM countrylanguage " +
                "JOIN country ON countrylanguage.CountryCode = country.Code WHERE country.Continent = ? " +
                "GROUP BY countrylanguage.Language ORDER BY Speakers DESC";
        return queryLanguages(sql, continent);
    }

    public List<Language> getLanguagesByCountry(String country) {
        String sql = "SELECT countrylanguage.Language, SUM(country.Population * countrylanguage.Percentage / 100) AS Speakers, country.Population AS TotalPopulation " +
                "FROM countrylanguage JOIN country ON countrylanguage.CountryCode = country.Code WHERE country.Code = ? " +
                "GROUP BY countrylanguage.Language, country.Population ORDER BY Speakers DESC";
        return queryLanguages(sql, country);
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
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String language = rs.getString("Language");
                long speakers = rs.getLong("Speakers");
                long total = rs.getLong("TotalPopulation");
                results.add(new Language(language, speakers, total));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return results;
    }
}
