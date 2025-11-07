package com.napier.devops.dao;

import com.napier.devops.models.Population;
import java.sql.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Data Access Object (DAO) for retrieving population statistics
 * from the database at various geographical levels (global, continent, region, country).
 */
public class PopulationDAO {
    private final Connection conn;
    private static final Logger logger = Logger.getLogger(PopulationDAO.class.getName());

    /**
     * Constructs a PopulationDAO with a database connection.
     * 
     * @param conn Database connection to be used for queries
     */
    public PopulationDAO(Connection conn) {
        this.conn = conn;
    }

    /**
     * Retrieves the total global population.
     * 
     * @return Total population across all countries, or 0 if query fails
     */
    public long getGlobalPopulation() {
        String sql = "SELECT SUM(country.Population) AS globalPopulation FROM country";
        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return rs.getLong("globalPopulation");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0L;
    }

    /**
     * Retrieves population breakdown by continent including total population,
     * urban (city) population, and rural (non-city) population.
     * Results are sorted by total population in descending order.
     * 
     * @return List of Population objects for all continents
     */
    public List<Population> getGlobalPopulations() {
        String sql = "SELECT c.Continent, " +
                "(SELECT SUM(co.Population) " +
                "FROM country co WHERE co.Continent = c.Continent) " +
                "AS total_population, " +

                "(SELECT SUM(ci.Population) " +
                "FROM city ci JOIN country co2 ON ci.CountryCode = co2.Code " +
                "WHERE co2.Continent = c.Continent) AS city_population, " +

                "(SELECT SUM(co.Population) " +
                "FROM country co " +
                "WHERE co.Continent = c.Continent)" +
                " - " +
                "(SELECT SUM(ci.Population) " +
                "FROM city ci " +
                "JOIN country co2 ON ci.CountryCode = co2.Code " +
                "WHERE co2.Continent = c.Continent) AS non_city_population " +

                "FROM country c " +
                "GROUP BY c.Continent " +
                "ORDER BY total_population DESC";
        return queryPopulation(sql);
    }

    /**
     * Retrieves population breakdown by continent, limited to top N results.
     * 
     * @param n Maximum number of continents to retrieve
     * @return List of top N continents by population
     */
    public List<Population> getGlobalPopulations(int n) {
        String sql = "SELECT c.Continent, " +
                "(SELECT SUM(co.Population) " +
                "FROM country co WHERE co.Continent = c.Continent) " +
                "AS total_population, " +

                "(SELECT SUM(ci.Population) " +
                "FROM city ci JOIN country co2 ON ci.CountryCode = co2.Code " +
                "WHERE co2.Continent = c.Continent) AS city_population, " +

                "(SELECT SUM(co.Population) " +
                "FROM country co " +
                "WHERE co.Continent = c.Continent)" +
                " - " +
                "(SELECT SUM(ci.Population) " +
                "FROM city ci " +
                "JOIN country co2 ON ci.CountryCode = co2.Code " +
                "WHERE co2.Continent = c.Continent) AS non_city_population " +

                "FROM country c " +
                "GROUP BY c.Continent " +
                "ORDER BY total_population DESC " +
                "LIMIT ?";
        return queryPopulation(sql, n);
    }

    /**
     * Retrieves population breakdown for a specific continent.
     * 
     * @param name Continent name to filter by
     * @return List of Population data for the specified continent
     */
    public List<Population> getContinentPopulations(String name) {
        String sql = "SELECT c.Continent, " +
                "(SELECT SUM(co.Population) " +
                "FROM country co WHERE co.Continent = c.Continent) " +
                "AS total_population, " +

                "(SELECT SUM(ci.Population) " +
                "FROM city ci JOIN country co2 ON ci.CountryCode = co2.Code " +
                "WHERE co2.Continent = c.Continent) AS city_population, " +

                "(SELECT SUM(co.Population) " +
                "FROM country co " +
                "WHERE co.Continent = c.Continent)" +
                " - " +
                "(SELECT SUM(ci.Population) " +
                "FROM city ci " +
                "JOIN country co2 ON ci.CountryCode = co2.Code " +
                "WHERE co2.Continent = c.Continent) AS non_city_population " +

                "FROM country c " +
                "WHERE c.Continent = ? " +
                "GROUP BY c.Continent " +
                "ORDER BY total_population DESC";
        return queryPopulation(sql, name);
    }

    /**
     * Retrieves population breakdown for a specific continent, limited to top N results.
     * 
     * @param name Continent name to filter by
     * @param n Maximum number of results to retrieve
     * @return List of top N population records for the specified continent
     */
    public List<Population> getContinentPopulations(String name, int n) {
        String sql = "SELECT c.Continent, " +
                "(SELECT SUM(co.Population) " +
                "FROM country co WHERE co.Continent = c.Continent) " +
                "AS total_population, " +

                "(SELECT SUM(ci.Population) " +
                "FROM city ci JOIN country co2 ON ci.CountryCode = co2.Code " +
                "WHERE co2.Continent = c.Continent) AS city_population, " +

                "(SELECT SUM(co.Population) " +
                "FROM country co " +
                "WHERE co.Continent = c.Continent)" +
                " - " +
                "(SELECT SUM(ci.Population) " +
                "FROM city ci " +
                "JOIN country co2 ON ci.CountryCode = co2.Code " +
                "WHERE co2.Continent = c.Continent) AS non_city_population " +

                "FROM country c " +
                "WHERE c.Continent = ? " +
                "GROUP BY c.Continent " +
                "ORDER BY total_population DESC " +
                "LIMIT ?";
        return queryPopulation(sql, name, n);
    }

    /**
     * Retrieves population breakdown by region including total, urban, and rural populations.
     * 
     * @return List of Population objects for all regions
     */
    public List<Population> getRegionPopulations() {
        String sql = "SELECT c.Region, " +
                "(SELECT SUM(co.Population) " +
                "FROM country co WHERE co.Region = c.Region) " +
                "AS total_population, " +

                "(SELECT SUM(ci.Population) " +
                "FROM city ci JOIN country co2 ON ci.CountryCode = co2.Code " +
                "WHERE co2.Region = c.Region) AS city_population, " +

                "(SELECT SUM(co.Population) " +
                "FROM country co " +
                "WHERE co.Region = c.Region)" +
                " - " +
                "(SELECT SUM(ci.Population) " +
                "FROM city ci " +
                "JOIN country co2 ON ci.CountryCode = co2.Code " +
                "WHERE co2.Region = c.Region) AS non_city_population " +

                "FROM country c " +
                "GROUP BY c.Region, total_population " +
                "ORDER BY total_population DESC";
        return queryPopulation(sql);
    }

    /**
     * Retrieves population breakdown by region, limited to top N results.
     * 
     * @param n Maximum number of regions to retrieve
     * @return List of top N regions by population
     */
    public List<Population> getRegionPopulations(int n) {
        String sql = "SELECT c.Region, " +
                "(SELECT SUM(co.Population) " +
                "FROM country co WHERE co.Region = c.Region) " +
                "AS total_population, " +

                "(SELECT SUM(ci.Population) " +
                "FROM city ci JOIN country co2 ON ci.CountryCode = co2.Code " +
                "WHERE co2.Region = c.Region) AS city_population, " +

                "(SELECT SUM(co.Population) " +
                "FROM country co " +
                "WHERE co.Region = c.Region)" +
                " - " +
                "(SELECT SUM(ci.Population) " +
                "FROM city ci " +
                "JOIN country co2 ON ci.CountryCode = co2.Code " +
                "WHERE co2.Region = c.Region) AS non_city_population " +

                "FROM country c " +
                "GROUP BY c.Region " +
                "ORDER BY total_population DESC " +
                "LIMIT ?";
        return queryPopulation(sql, n);
    }

    /**
     * Retrieves population breakdown for a specific region.
     * 
     * @param name Region name to filter by
     * @return List of Population data for the specified region
     */
    public List<Population> getRegionPopulations(String name) {
        String sql = "SELECT c.Region, " +
                "(SELECT SUM(co.Population) " +
                "FROM country co WHERE co.Region = c.Region) " +
                "AS total_population, " +

                "(SELECT SUM(ci.Population) " +
                "FROM city ci JOIN country co2 ON ci.CountryCode = co2.Code " +
                "WHERE co2.Region = c.Region) AS city_population, " +

                "(SELECT SUM(co.Population) " +
                "FROM country co " +
                "WHERE co.Region = c.Region)" +
                " - " +
                "(SELECT SUM(ci.Population) " +
                "FROM city ci " +
                "JOIN country co2 ON ci.CountryCode = co2.Code " +
                "WHERE co2.Region = c.Region) AS non_city_population " +

                "FROM country c " +
                "WHERE c.Region = ? " +
                "GROUP BY c.Region " +
                "ORDER BY total_population DESC";
        return queryPopulation(sql, name);
    }

    /**
     * Retrieves population breakdown for a specific region, limited to top N results.
     * 
     * @param name Region name to filter by
     * @param n Maximum number of results to retrieve
     * @return List of top N population records for the specified region
     */
    public List<Population> getRegionPopulations(String name, int n) {
        String sql = "SELECT c.Region, " +
                "(SELECT SUM(co.Population) " +
                "FROM country co WHERE co.Region = c.Region) " +
                "AS total_population, " +

                "(SELECT SUM(ci.Population) " +
                "FROM city ci JOIN country co2 ON ci.CountryCode = co2.Code " +
                "WHERE co2.Region = c.Region) AS city_population, " +

                "(SELECT SUM(co.Population) " +
                "FROM country co " +
                "WHERE co.Region = c.Region)" +
                " - " +
                "(SELECT SUM(ci.Population) " +
                "FROM city ci " +
                "JOIN country co2 ON ci.CountryCode = co2.Code " +
                "WHERE co2.Region = c.Region) AS non_city_population " +

                "FROM country c " +
                "WHERE c.Region = ? " +
                "GROUP BY c.Region " +
                "ORDER BY total_population DESC " +
                "LIMIT ?";
        return queryPopulation(sql, name, n);
    }

    /**
     * Retrieves population breakdown for all countries including total, urban, and rural populations.
     * 
     * @return List of Population objects for all countries
     */
    public List<Population> getCountryPopulations() {
        String sql = "SELECT c.Name AS Country, c.Population AS total_population, " +
        "LEAST(IFNULL(SUM(ci.Population), 0), c.Population) AS city_population, " +
        "c.Population - LEAST(IFNULL(SUM(ci.Population), 0), c.Population) AS non_city_population " +
        "FROM country c " +
        "LEFT JOIN city ci ON ci.CountryCode = c.Code " +
        "GROUP BY c.Code, c.Name, c.Continent, c.Region, c.Population " +
        "ORDER BY total_population DESC";
        return queryPopulation(sql);
    }

    /**
     * Retrieves population breakdown for all countries, limited to top N by population.
     * 
     * @param n Maximum number of countries to retrieve
     * @return List of top N countries by population
     */
    public List<Population> getCountryPopulations(int n) {
        String sql = "SELECT c.Name AS Country, c.Population AS total_population, " +
                "IFNULL(SUM(ci.Population), 0) AS city_population, " +
                "c.Population - IFNULL(SUM(ci.Population), 0) AS non_city_population " +
                "FROM country c " +
                "LEFT JOIN city ci ON ci.CountryCode = c.Code " +
                "GROUP BY c.Code, c.Name, c.Continent, c.Region, c.Population " +
                "ORDER BY total_population DESC " +
                "LIMIT ?";
        return queryPopulation(sql, n);
    }

    /**
     * Retrieves population data for a specific country using country code.
     * 
     * @param country Country code to filter by
     * @return List of Population data for the specified country
     */
    public List<Population> getCountryPopulationByCode(String country) {
        String sql = "SELECT c.Name AS Country, " +
                "(SELECT SUM(co.Population) " +
                "FROM country co WHERE co.Code = c.Code) " +
                "AS total_population, " +

                "(SELECT SUM(ci.Population) " +
                "FROM city ci JOIN country co2 ON ci.CountryCode = co2.Code " +
                "WHERE co2.Code  = c.Code) AS city_population, " +

                "(SELECT SUM(co.Population) " +
                "FROM country co " +
                "WHERE co.Code = c.Code)  - " +
                "(SELECT SUM(ci.Population) " +
                "FROM city ci " +
                "JOIN country co2 ON ci.CountryCode = co2.Code " +
                "WHERE co2.Code = c.Code) AS non_city_population " +

                "FROM country c " +
                "WHERE c.Code = ? " +
                "GROUP BY c.Code, total_population " +
                "ORDER BY total_population DESC";
        return queryPopulation(sql, country);
    }

    /**
     * Retrieves population data for a specific country using country code, limited to top N results.
     * 
     * @param country Country code to filter by
     * @param n Maximum number of results to retrieve
     * @return List of top N population records for the specified country
     */
    public List<Population> getCountryPopulationByCode(String country, int n) {
        String sql = "SELECT c.Name AS Country, " +
                "(SELECT SUM(co.Population) " +
                "FROM country co WHERE co.Code = c.Code) " +
                "AS total_population, " +

                "(SELECT SUM(ci.Population) " +
                "FROM city ci JOIN country co2 ON ci.CountryCode = co2.Code " +
                "WHERE co2.Code  = c.Code) AS city_population, " +

                "(SELECT SUM(co.Population) " +
                "FROM country co " +
                "WHERE co.Code = c.Code)  - " +
                "(SELECT SUM(ci.Population) " +
                "FROM city ci " +
                "JOIN country co2 ON ci.CountryCode = co2.Code " +
                "WHERE co2.Code = c.Code) AS non_city_population " +

                "FROM country c " +
                "WHERE c.Code = ? " +
                "GROUP BY c.Code, total_population " +
                "ORDER BY total_population DESC " +
                "LIMIT ?";
        return queryPopulation(sql, country, n);
    }

    /**
     * Retrieves population data for a specific country using country name.
     * 
     * @param country Country name to filter by
     * @return List of Population data for the specified country
     */
    public List<Population> getCountryPopulations(String country) {
        String sql = "SELECT c.Name AS Country, " +
                "(SELECT SUM(co.Population) " +
                "FROM country co WHERE co.Code = c.Code) " +
                "AS total_population, " +

                "(SELECT SUM(ci.Population) " +
                "FROM city ci JOIN country co2 ON ci.CountryCode = co2.Code " +
                "WHERE co2.Code  = c.Code) AS city_population, " +

                "(SELECT SUM(co.Population) " +
                "FROM country co " +
                "WHERE co.Code = c.Code)  - " +
                "(SELECT SUM(ci.Population) " +
                "FROM city ci " +
                "JOIN country co2 ON ci.CountryCode = co2.Code " +
                "WHERE co2.Code = c.Code) AS non_city_population " +

                "FROM country c " +
                "WHERE c.Name = ? " +
                "GROUP BY c.Code, total_population " +
                "ORDER BY total_population DESC";
        return queryPopulation(sql, country);
    }

    /**
     * Retrieves population data for a specific country using country name, limited to top N results.
     * 
     * @param country Country name to filter by
     * @param n Maximum number of results to retrieve
     * @return List of top N population records for the specified country
     */
    public List<Population> getCountryPopulations(String country, int n) {
        String sql = "SELECT c.Name AS Country, " +
                "(SELECT SUM(co.Population) " +
                "FROM country co WHERE co.Code = c.Code) " +
                "AS total_population, " +

                "(SELECT SUM(ci.Population) " +
                "FROM city ci JOIN country co2 ON ci.CountryCode = co2.Code " +
                "WHERE co2.Code  = c.Code) AS city_population, " +

                "(SELECT SUM(co.Population) " +
                "FROM country co " +
                "WHERE co.Code = c.Code)" +
                " - " +
                "(SELECT SUM(ci.Population) " +
                "FROM city ci " +
                "JOIN country co2 ON ci.CountryCode = co2.Code " +
                "WHERE co2.Code = c.Code) AS non_city_population " +

                "FROM country c " +
                "WHERE c.Name = ? " +
                "GROUP BY c.Code, total_population " +
                "ORDER BY total_population DESC " +
                "LIMIT ?";
        return queryPopulation(sql, country, n);
    }

    /**
     * Helper method that executes population queries and maps results to Population objects.
     * Handles parameterized queries to prevent SQL injection.
     * 
     * @param sql SQL query string with optional parameter placeholders (?)
     * @param params Optional variable arguments to bind to the query parameters
     * @return List of Population objects populated from query results
     */
    private List<Population> queryPopulation(String sql, Object... params) {
        List<Population> results = new ArrayList<>();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            // Bind all provided parameters to their corresponding placeholders
            for (int i = 0; i < params.length; i++) {
                stmt.setObject(i + 1, params[i]);
            }
            ResultSet rs = stmt.executeQuery();
            // Map each result row to a Population object
            while (rs.next()) {
                Population p = new Population(
                        rs.getString(1),       // label (continent/region/country name)
                        rs.getLong(2),         // total population
                        rs.getLong(3),         // urban/city population
                        rs.getLong(4)          // rural/non-city population
                );
                results.add(p);
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        }
        return results;
    }
}
