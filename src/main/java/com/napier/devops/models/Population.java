package com.napier.devops.models;

public class Population {
    private String label;             // e.g. Continent, Region, or Country name
    private long globalPopulation;     // total global
    private long totalPopulation;     // total population
    private long cityPopulation;      // people living in cities
    private long nonCityPopulation;   // people not living in cities

    // --- Constructors ---
    public Population(long globalPopulation) {
        this.globalPopulation = globalPopulation;
    }

    public Population(String label, long totalPopulation, long cityPopulation, long nonCityPopulation) {
        this.label = label;
        this.totalPopulation = totalPopulation;
        this.cityPopulation = cityPopulation;
        this.nonCityPopulation = nonCityPopulation;
    }

    // --- Getters & Setters ---
    public String getLabel() { return label; }
    public void setLabel(String label) { this.label = label; }

    public long getGlobalPopulation() { return globalPopulation; }
    public void setGlobalPopulation(long globalPopulation) { this.globalPopulation = globalPopulation; }

    public long getTotalPopulation() { return totalPopulation; }
    public void setTotalPopulation(long totalPopulation) { this.totalPopulation = totalPopulation; }

    public long getCityPopulation() { return cityPopulation; }
    public void setCityPopulation(long cityPopulation) { this.cityPopulation = cityPopulation; }

    public long getNonCityPopulation() { return nonCityPopulation; }
    public void setNonCityPopulation(long nonCityPopulation) { this.nonCityPopulation = nonCityPopulation; }

    // --- Percentage helpers ---
    public double getCityPercentage() {
        return totalPopulation == 0 ? 0 : ((double) cityPopulation / totalPopulation * 100.0);
    }

    public double getNonCityPercentage() {
        return totalPopulation == 0 ? 0 : ((double) nonCityPopulation / totalPopulation * 100.0);
    }

    @Override
    public String toString() {
        return String.format(
                "%s | Total: %d | In Cities: %d (%.2f%%) | Not in Cities: %d (%.2f%%)",
                label,
                totalPopulation,
                cityPopulation, getCityPercentage(),
                nonCityPopulation, getNonCityPercentage()
        );
    }


}
