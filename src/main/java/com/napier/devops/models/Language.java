package com.napier.devops.models;

public class Language {
    private String language;
    private long speakers;
    private long totalPopulation;
    private long globalPopulation;

    public Language(String language, long speakers, long totalPopulation) {
        this.language = language;
        this.speakers = speakers;
        this.totalPopulation = totalPopulation;
        this.globalPopulation = 0;
    }

    public Language(String language, long speakers, long totalPopulation, long globalPopulation) {
        this(language, speakers,  totalPopulation);
        this.globalPopulation = globalPopulation;
    }

    // --- Getters ---
    public String getLanguage() {
        return language;
    }

    public long getSpeakers() {
        return speakers;
    }

    public long getTotalPopulation() {
        return totalPopulation;
    }

    public long getGlobalPopulation() {
        return globalPopulation;
    }

    // --- Setters ---
    public void setGlobalPopulation(long globalPopulation) {
        this.globalPopulation = globalPopulation;
    }

    // --- Percentage Calculations ---
    /** Percentage of the population in the current scope (country/continent/region). */
    public double getPercentageOfPopulation() {
        return totalPopulation == 0 ? 0 : (speakers * 100.0 / totalPopulation);
    }

    /** Percentage of the global population that speaks this language. */
    public double getPercentageOfGlobal() {
        return globalPopulation == 0 ? 0 : (speakers * 100.0 / globalPopulation);
    }

    // --- Debugging / Logging ---
    @Override
    public String toString() {
        return String.format(
                "Language: %s | Speakers: %d | %% of Population: %.2f%% | %% of Global: %.2f%%",
                language, speakers, getPercentageOfPopulation(), getPercentageOfGlobal()
        );
    }
}
