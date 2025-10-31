public class LanguageSpeakers {
    public static void main(String[] args) {
        // Updated data (user-provided)
        String[] languages = {"Chinese", "Hindi", "Spanish", "English", "Arabic"};
        long[] speakers = {
            1191843539L,  // Chinese
            405633070L,   // Hindi
            355029462L,   // Spanish
            347077876L,   // English
            233839238L    // Arabic
        };

        double worldPopulation = 8000000000.0; // Approximate 8 billion people

        // Calculate % of world population for each language
        double[] worldPercent = new double[languages.length];
        for (int i = 0; i < languages.length; i++) {
            worldPercent[i] = (speakers[i] / worldPopulation) * 100;
        }

        // Print header
        System.out.printf("%-10s | %-15s | %-20s%n", "Language", "Speakers", "% of World Population");
        System.out.println("-------------------------------------------------------------");

        // Print data (already sorted from largest to smallest)
        for (int i = 0; i < languages.length; i++) {
            System.out.printf("%-10s | %-15d | %-20.2f%n", languages[i], speakers[i], worldPercent[i]);
        }
    }
}
