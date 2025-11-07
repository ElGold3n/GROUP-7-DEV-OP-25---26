package com.napier.devops.dao;

import com.napier.devops.models.City;

import java.sql.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Data Access Object for City-related database operations.
 * Provides methods to query cities from the database with various filtering and sorting criteria.
 */
public class CityDAO {
    private final Connection conn;
    private static final Logger logger = Logger.getLogger(PopulationDAO.class.getName());

    /**
     * Constructs a CityDAO with a database connection.
     *
     * @param conn the database connection to use for queries
     */
    public CityDAO(Connection conn) {
        this.conn = conn;
    }

    /**
     * Retrieves all cities ordered by population in descending order.
     *
     * @return a list of cities sorted by population (highest first)
     */
    public List<City> getCitiesByPopulation() {
        String sql = "SELECT city.ID, city.Name, country.Name AS Country, country.Continent, city.District, city.Population " +
                "FROM city JOIN country ON city.CountryCode=country.Code ORDER BY city.Population DESC";
        return queryCities(sql);
    }

    /**
     * Retrieves the top N cities ordered by population in descending order.
     *
     * @param n the maximum number of cities to return
     * @return a list of up to N cities sorted by population (highest first)
     */
    public List<City> getCitiesByPopulation(int n) {
        String sql = "SELECT city.ID, city.Name, country.Name AS Country, country.Continent, city.District, city.Population " +
                "FROM city JOIN country ON city.CountryCode=country.Code ORDER BY city.Population DESC LIMIT ?";
        return queryCities(sql, n);
    }

    /**
     * Retrieves all cities by continent, ordered by continent in ascending order, then by population in descending order.
     *
     * @return a list of cities sorted by population
     */
    public List<City> getCitiesByContinent() {
        String sql = "SELECT city.ID, city.Name, country.Name AS Country, country.Continent, city.District, city.Population " +
                "FROM city JOIN country ON city.CountryCode = country.Code " +
                "ORDER BY UPPER(country.Continent) ASC, city.Population DESC";
        return queryCities(sql);
    }

    /**
     * Retrieves the top N cities by continent, ordered by continent in ascending order, then by population in descending order.
     *
     * @param n the maximum number of cities to return
     * @return a list of up to N cities sorted by population
     */
    public List<City> getCitiesByContinent(int n) {
        String sql = "SELECT city.ID, city.Name, country.Name AS Country, country.Continent, city.District, city.Population " +
                "FROM city JOIN country ON city.CountryCode = country.Code " +
                "ORDER BY UPPER(country.Continent) ASC, city.Population DESC LIMIT ?";
        return queryCities(sql, n);
    }

    /**
     * Retrieves all cities in a specified continent, ordered by population in descending order.
     *
     * @param continent the continent name to filter by
     * @return a list of cities in the specified continent sorted by population
     */
    public List<City> getCitiesInContinent(String continent) {
        String sql = "SELECT city.ID, city.Name, country.Name AS Country, country.Continent, District, city.Population " +
                "FROM city JOIN country ON city.CountryCode = country.Code " +
                "WHERE country.Continent=? ORDER BY city.Population DESC";
        return queryCities(sql, continent);
    }

    /**
     * Retrieves the top N cities in a specified continent, ordered by population in descending order.
     *
     * @param continent the continent name to filter by
     * @param n the maximum number of cities to return
     * @return a list of up to N cities in the specified continent sorted by population
     */
    public List<City> getCitiesInContinent(String continent, int n) {
        String sql = "SELECT city.ID, city.Name, country.Name AS Country, country.Continent, District, city.Population " +
                "FROM city JOIN country ON city.CountryCode = country.Code " +
                "WHERE country.Continent=? ORDER BY city.Population DESC LIMIT ?";
        return queryCities(sql, continent, n);
    }

    /**
     * Retrieves all cities in a specified region, ordered by population in descending order.
     *
     * @param region the region name to filter by
     * @return a list of cities in the specified region sorted by population
     */
    public List<City> getCitiesInRegion(String region) {
        String sql = "SELECT city.Name, city.District, country.Name AS Country, country.Continent, city.Population, country.Region " +
                "FROM city JOIN country ON city.CountryCode = country.Code " +
                "WHERE country.Region = ? ORDER BY city.Population DESC";
        return queryCities(sql, region);
    }

    /**
     * Retrieves the top N cities in a specified region, ordered by population in descending order.
     *
     * @param region the region name to filter by
     * @param n the maximum number of cities to return
     * @return a list of up to N cities in the specified region sorted by population
     */
    public List<City> getCitiesInRegion(String region, int n) {
        String sql = "SELECT city.Name, city.District, country.Name AS Country, country.Continent, city.Population, country.Region " +
                "FROM city JOIN country ON city.CountryCode = country.Code " +
                "WHERE country.Region = ? ORDER BY city.Population DESC LIMIT ?";
        return queryCities(sql, region, n);
    }

    /**
     * Retrieves all cities in a specified country by country code, ordered by population in descending order.
     *
     * @param country the country code to filter by
     * @return a list of cities in the specified country sorted by population
     */
    public List<City> getCitiesInCountryByCode(String country) {
        String sql = "SELECT city.ID, city.Name, country.Name AS Country, country.Continent, District, city.Population " +
                "FROM city JOIN country ON city.CountryCode = country.Code " +
                "WHERE country.Code=? ORDER BY city.Population DESC";
        return queryCities(sql, country);
    }

    /**
     * Retrieves the top N cities in a specified country by country code, ordered by population in descending order.
     *
     * @param country the country code to filter by
     * @param n the maximum number of cities to return
     * @return a list of up to N cities in the specified country sorted by population
     */
    public List<City> getCitiesInCountryByCode(String country, int n) {
        String sql = "SELECT city.ID, city.Name, country.Name AS Country, country.Continent, District, city.Population " +
                "FROM city JOIN country ON city.CountryCode = country.Code " +
                "WHERE country.Code=? ORDER BY city.Population DESC LIMIT ?";
        return queryCities(sql, country, n);
    }

    /**
     * Retrieves all cities in a specified country by country name, ordered by population in descending order.
     *
     * @param country the country name to filter by
     * @return a list of cities in the specified country sorted by population
     */
    public List<City> getCitiesInCountryByName(String country) {
        String sql = "SELECT city.ID, city.Name, country.Name AS Country, country.Continent, District, city.Population " +
                "FROM city JOIN country ON city.CountryCode = country.Code " +
                "WHERE country.Name=? ORDER BY city.Population DESC";
        return queryCities(sql, country);
    }

    /**
     * Retrieves the top N cities in a specified country by country name, ordered by population in descending order.
     *
     * @param country the country name to filter by
     * @param n the maximum number of cities to return
     * @return a list of up to N cities in the specified country sorted by population
     */
    public List<City> getCitiesInCountryByName(String country, int n) {
        String sql = "SELECT city.ID, city.Name, country.Name AS Country, country.Continent, District, city.Population " +
                "FROM city JOIN country ON city.CountryCode = country.Code " +
                "WHERE country.Name=? ORDER BY city.Population DESC LIMIT ?";
        return queryCities(sql, country, n);
    }

    /**
     * Retrieves all cities in a specified district, ordered by population in descending order.
     *
     * @param district the district name to filter by
     * @return a list of cities in the specified district sorted by population
     */
    public List<City> getCitiesInDistrict(String district) {
        String sql = "SELECT city.ID, city.Name, country.Name AS Country, country.Continent, city.District, city.Population " +
                "FROM city JOIN country ON city.CountryCode = country.Code " +
                "WHERE city.District=? ORDER BY city.Population DESC";
        return queryCities(sql, district);
    }

    /**
     * Retrieves the top N cities in a specified district, ordered by population in descending order.
     *
     * @param district the district name to filter by
     * @param n the maximum number of cities to return
     * @return a list of up to N cities in the specified district sorted by population
     */
    public List<City> getCitiesInDistrict(String district, int n) {
        String sql = "SELECT city.ID, city.Name, country.Name AS Country, country.Continent, city.District, city.Population " +
                "FROM city JOIN country ON city.CountryCode = country.Code " +
                "WHERE city.District=? ORDER BY city.Population DESC LIMIT ?";
        return queryCities(sql, district, n);
    }

    /**
     * Executes a city query with the given SQL and parameters, mapping results to City objects.
     * 
     * @param sql the SQL query to execute
     * @param params variable arguments representing query parameters (? placeholders)
     * @return a list of City objects populated from the query results
     */
    private List<City> queryCities(String sql, Object... params) {
        List<City> results = new ArrayList<>();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            // Set query parameters dynamically
            for (int i=0; i<params.length; i++) stmt.setObject(i+1, params[i]);
            ResultSet rs = stmt.executeQuery();
            // Map ResultSet rows to City objects
            while (rs.next()) {
                City c = new City();
                c.setName(rs.getString("Name"));
                c.setCountry(rs.getString("Country"));
                c.setContinent(rs.getString("Continent"));
                c.setDistrict(rs.getString("District"));
                c.setPopulation(rs.getLong("Population"));
                results.add(c);
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        }
        return results;
    }
}
