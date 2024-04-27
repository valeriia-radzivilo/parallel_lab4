package task1;

import java.util.Arrays;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

public class TextAnalysis {
    private static final ForkJoinPool forkJoinPool = new ForkJoinPool();

    public static void analyzeText(String text) {
        String[] words = text.split("\\s+");
        int[] wordLengths = Arrays.stream(words).mapToInt(String::length).toArray();

        double mean = forkJoinPool.invoke(new MeanTask(wordLengths));
        double variance = forkJoinPool.invoke(new VarianceTask(wordLengths, mean));

        System.out.println("Mean word length: " + mean);
        System.out.println("Variance of word length: " + variance);
    }

    private static class MeanTask extends RecursiveTask<Double> {
        private final int[] wordLengths;

        MeanTask(int[] wordLengths) {
            this.wordLengths = wordLengths;
        }

        @Override
        protected Double compute() {
            return Arrays.stream(wordLengths).average().orElse(0);
        }
    }

    private static class VarianceTask extends RecursiveTask<Double> {
        private final int[] wordLengths;
        private final double mean;

        VarianceTask(int[] wordLengths, double mean) {
            this.wordLengths = wordLengths;
            this.mean = mean;
        }

        @Override
        protected Double compute() {
            return Arrays.stream(wordLengths)
                    .mapToDouble(i -> Math.pow(i - mean, 2))
                    .average().orElse(0);
        }
    }
}