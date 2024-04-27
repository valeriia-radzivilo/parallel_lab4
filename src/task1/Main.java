package task1;

import java.nio.file.Files;
import java.nio.file.Paths;

public class Main {
    public static void main(String[] args) {
        try {
            String text = new String(Files.readAllBytes(Paths.get("assets/large_text_task1.txt")));
            long startTime = System.currentTimeMillis();
            TextAnalysis.analyzeText(text);
            long endTime = System.currentTimeMillis();

            long durationWithForkJoin = endTime - startTime;
            System.out.println("Execution time with ForkJoin: " + durationWithForkJoin + " ms");

            // Now run the same analysis without ForkJoin and compare the times
            // You would need to implement a similar analyzeText method without using ForkJoin
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}