package com.napier.devops.models;

public class Population {
    private String label;             // e.g. Continent, Region, or Country name
    private long globalPopulation;    // total global
    private long totalPopulation;     // total population
    private long cityPopulation;      // people living in cities
    private double cityPercentage;
    private long nonCityPopulation;   // people not living in cities
    private double nonCityPercentage;

    // --- Constructors ---
    // Full constructor with global population
    public Population(String label,
                      long totalPopulation,
                      long nonCityPopulation,
                      double nonCityPercentage,
                      long cityPopulation,
                      double cityPercentage,
                      long globalPopulation) {
        setLabel(label);
        setTotalPopulation(totalPopulation);
        setCityPopulation(cityPopulation);
        setCityPercentage(cityPercentage);
        setNonCityPopulation(nonCityPopulation);
        setNonCityPercentage(nonCityPercentage);
        setGlobalPopulation(globalPopulation);
    }

    // 6‑argument constructor (matches your test)
    public Population(String label,
                      long totalPopulation,
                      long cityPopulation,
                      double cityPercentage,
                      long nonCityPopulation,
                      double nonCityPercentage) {
        setLabel(label);
        setTotalPopulation(totalPopulation);
        setCityPopulation(cityPopulation);
        setCityPercentage(cityPercentage);
        setNonCityPopulation(nonCityPopulation);
        setNonCityPercentage(nonCityPercentage);
    }

    // Simplified constructor
    public Population(String label, long totalPopulation, long cityPopulation, long nonCityPopulation) {
        setLabel(label);
        setTotalPopulation(totalPopulation);
        setCityPopulation(cityPopulation);
        setNonCityPopulation(nonCityPopulation);
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

    public double getCityPercentage() { return totalPopulation == 0 ? 0 : ((double) cityPopulation / totalPopulation * 100.0); }
    public void setCityPercentage(double cityPercentage) { this.cityPercentage = cityPercentage; }

    public double getNonCityPercentage() { return totalPopulation == 0 ? 0 : ((double) nonCityPopulation / totalPopulation * 100.0); }
    public void setNonCityPercentage(double nonCityPercentage) { this.nonCityPercentage = nonCityPercentage; }
}
