package com.napier.devops.models;

public class Country {
    private String code;
    private String name;
    private String continent;
    private String region;
    private long population;
    private String capital;

    // Getters & Setters
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getContinent() { return continent; }
    public void setContinent(String continent) { this.continent = continent; }

    public String getRegion() { return region; }
    public void setRegion(String region) { this.region = region; }

    public long getPopulation() { return population; }
    public void setPopulation(long population) { this.population = population; }

    public String getCapital() { return capital; }
    public void setCapital(String capital) { this.capital = capital; }

    @Override
    public String toString() {
        return String.format("(%s) %s - %s, %s | Pop: %d | Capital: %s",
                code, name, continent, region, population, capital);
    }
}
