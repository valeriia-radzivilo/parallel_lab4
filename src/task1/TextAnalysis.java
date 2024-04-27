package task1;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

public class TextAnalysis {

    private static final int SEQUENTIAL_THRESHOLD = 7000;

    public static void analyzeText(String text) {
        List<String> words = Arrays.asList(text.split("\\s+"));
        ForkJoinPool forkJoinPool = new ForkJoinPool();
        Map<Integer, Long> wordLengths = forkJoinPool.invoke(new WordLengthAnalysisTask(words));


        long totalWords = wordLengths.values().stream().mapToLong(Long::longValue).sum();
        double averageLength = wordLengths.entrySet().stream().mapToDouble(e -> e.getKey() * e.getValue()).sum() / totalWords;

        double variance = wordLengths.entrySet().stream().mapToDouble(e -> e.getValue() * Math.pow(e.getKey() - averageLength, 2)).sum() / totalWords;
        double standardDeviation = Math.sqrt(variance);

        System.out.println("Average word length: " + averageLength);
        System.out.println("Variance of word length: " + variance);
        System.out.println("Standard deviation of word length: " + standardDeviation);
    }

    // linear implementation
    public static void analyzeTextLinear(String text) {
        String[] words = text.split("\\s+");
        Map<Integer, Integer> wordLengths = new HashMap<>();
        for (String word : words) {
            int length = word.length();
            wordLengths.put(length, wordLengths.getOrDefault(length, 0) + 1);
        }

        long totalWords = wordLengths.values().stream().mapToLong(e -> e).sum();
        double averageLength = wordLengths.entrySet().stream().mapToDouble(e -> e.getKey() * e.getValue()).sum() / totalWords;

        double variance = wordLengths.entrySet().stream().mapToDouble(e -> e.getValue() * Math.pow(e.getKey() - averageLength, 2)).sum() / totalWords;
        double standardDeviation = Math.sqrt(variance);

        System.out.println("Average word length: " + averageLength);
        System.out.println("Variance of word length: " + variance);
        System.out.println("Standard deviation of word length: " + standardDeviation);
    }

    private static class WordLengthAnalysisTask extends RecursiveTask<Map<Integer, Long>> {
        private final List<String> words;

        WordLengthAnalysisTask(List<String> words) {
            this.words = words;
        }

        @Override
        protected Map<Integer, Long> compute() {
            if (words.size() <= SEQUENTIAL_THRESHOLD) {
                Map<Integer, Long> result = new HashMap<>();
                for (String word : words) {
                    int length = word.length();
                    result.put(length, result.getOrDefault(length, 0L) + 1);
                }
                return result;
            } else {
                WordLengthAnalysisTask task1 = new WordLengthAnalysisTask(words.subList(0, words.size() / 2));
                WordLengthAnalysisTask task2 = new WordLengthAnalysisTask(words.subList(words.size() / 2, words.size()));

                task1.fork();
                Map<Integer, Long> result1 = task2.compute();
                Map<Integer, Long> result2 = task1.join();

                for (Map.Entry<Integer, Long> entry : result2.entrySet()) {
                    result1.merge(entry.getKey(), entry.getValue(), Long::sum);
                }

                return result1;
            }
        }
    }
}