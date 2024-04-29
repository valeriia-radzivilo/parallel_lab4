package task1;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

public class TextAnalysis {

    private static final int SEQUENTIAL_THRESHOLD = 500;

    public static void analyzeText(String text, boolean showLogs) {
        List<String> words = Arrays.asList(text.split("\\s+"));
        ForkJoinPool forkJoinPool = new ForkJoinPool();
        Map<Integer, Integer> wordLengths = forkJoinPool.invoke(new WordLengthTask(words, 0, words.size()));


        long totalWords = wordLengths.values().stream().mapToInt(e -> e).sum();
        double averageLength = wordLengths.entrySet().stream().mapToDouble(e -> e.getKey() * e.getValue()).sum() / totalWords;

        double variance = wordLengths.entrySet().stream().mapToDouble(e -> e.getValue() * Math.pow(e.getKey() - averageLength, 2)).sum() / totalWords;
        double standardDeviation = Math.sqrt(variance);

        if (showLogs) {
            System.out.println("Average word length: " + averageLength);
            System.out.println("Variance of word length: " + variance);
            System.out.println("Standard deviation of word length: " + standardDeviation);
        }
    }

    // linear implementation
    public static void analyzeTextLinear(String fileText) {
        final String text = fileText;
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


        int varianceResult = (int) variance;
        int standardDeviationResult = (int) standardDeviation;


        System.out.println("Average word length: " + averageLength);
        System.out.println("Variance of word length: " + varianceResult);
        System.out.println("Standard deviation of word length: " + standardDeviationResult);
    }

    static class WordLengthTask extends RecursiveTask<Map<Integer, Integer>> {
        private final List<String> words;
        private final int start;
        private final int end;

        public WordLengthTask(List<String> words, int start, int end) {
            this.words = words;
            this.start = start;
            this.end = end;
        }

        @Override
        protected Map<Integer, Integer> compute() {
            if (end - start <= SEQUENTIAL_THRESHOLD) {

                Map<Integer, Integer> wordLengths = new HashMap<>();
                if (start < end) {
                    int length = words.get(start).length();
                    wordLengths.put(length, wordLengths.getOrDefault(length, 0) + 1);
                }
                return wordLengths;
            } else {
                // Divide the task into smaller subtasks
                int mid = start + (end - start) / 2;
                WordLengthTask leftTask = new WordLengthTask(words, start, mid);
                WordLengthTask rightTask = new WordLengthTask(words, mid, end);

                // Fork and execute the subtasks concurrently
                leftTask.fork();
                Map<Integer, Integer> rightResult = rightTask.compute();
                Map<Integer, Integer> leftResult = leftTask.join();

                // Merge the results from the subtasks
                mergeMaps(leftResult, rightResult);

                return leftResult;
            }
        }

        private void mergeMaps(Map<Integer, Integer> map1, Map<Integer, Integer> map2) {
            for (Map.Entry<Integer, Integer> entry : map2.entrySet()) {
                int key = entry.getKey();
                int value = entry.getValue();
                map1.put(key, map1.getOrDefault(key, 0) + value);
            }
        }
    }
}