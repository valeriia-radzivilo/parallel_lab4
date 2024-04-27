package task1;

import java.nio.file.Files;
import java.nio.file.Paths;

public class Main {
    public static void main(String[] args) {
        try {
            // task1_small_text_to_check.txt is a small text file for testing
            // task1_large_text.txt is a large text file for testing
            final String text = new String(Files.readAllBytes(Paths.get("assets/task1_large_text.txt")));

            final long startTime = System.currentTimeMillis();
            TextAnalysis.analyzeText(text);
            final long endTime = System.currentTimeMillis();

            long durationWithForkJoin = endTime - startTime;
            System.out.println("Execution time with ForkJoin: " + durationWithForkJoin + " ms");

            System.out.println("\n\n");

            // Linear implementation
            final long startLinear = System.currentTimeMillis();
            TextAnalysis.analyzeTextLinear(text);
            final long endLinear = System.currentTimeMillis();
            System.out.println("Execution time with linear implementation: " + (endLinear - startLinear) + " ms");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}