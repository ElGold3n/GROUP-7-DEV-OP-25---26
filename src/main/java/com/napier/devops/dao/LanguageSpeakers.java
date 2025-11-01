public class LanguageSpeakers {
    public static void main(String[] args) {
        // Updated data (user-provided)
        // Arrays store language names and their corresponding number of speakers
        String[] languages = {"Chinese", "Hindi", "Spanish", "English", "Arabic"};
        long[] speakers = {
            1191843539L,  // Chinese - most speakers
            405633070L,   // Hindi
            355029462L,   // Spanish
            347077876L,   // English
            233839238L    // Arabic - least speakers among the five
        };

        // Approximate current world population in billions
        double worldPopulation = 8000000000.0; // Approximate 8 billion people

        // Calculate the percentage of world population that speaks each language
        double[] worldPercent = new double[languages.length];
        for (int i = 0; i < languages.length; i++) {
            // Formula: (speakers of language / total world population) * 100
            worldPercent[i] = (speakers[i] / worldPopulation) * 100;
        }

        // Print table header with column names and spacing
        System.out.printf("%-10s | %-15s | %-20s%n", "Language", "Speakers", "% of World Population");
        // Print separator line for table formatting
        System.out.println("-------------------------------------------------------------");

        // Print data rows (already sorted from largest to smallest number of speakers)
        for (int i = 0; i < languages.length; i++) {
            // Format: language name | number of speakers | percentage (2 decimal places)
            System.out.printf("%-10s | %-15d | %-20.2f%n", languages[i], speakers[i], worldPercent[i]);
        }
    }
}
