package task3;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

public class WordSearcher {
    public static void main(String[] args) throws Exception {
        String text1 = new String(Files.readAllBytes(Paths.get("assets/text_task3.txt")));
        String text2 = new String(Files.readAllBytes(Paths.get("assets/second_text_task3.txt")));

        List<String> words1 = Arrays.asList(text1.split("\\s+"));
        List<String> words2 = Arrays.asList(text2.split("\\s+"));

        ForkJoinPool forkJoinPool = new ForkJoinPool();
        WordSearchTask task1 = new WordSearchTask(words1);
        WordSearchTask task2 = new WordSearchTask(words2);

        Map<String, Integer> wordCounts1 = forkJoinPool.invoke(task1);
        Map<String, Integer> wordCounts2 = forkJoinPool.invoke(task2);

        Map<String, Integer> commonWords = new HashMap<>();
        wordCounts1.forEach((word, count) -> {
            if (wordCounts2.containsKey(word)) {
                commonWords.put(word, count + wordCounts2.get(word));
            }
        });

        commonWords.forEach((word, count) -> System.out.println(word + ": " + count));
    }

    private static class WordSearchTask extends RecursiveTask<Map<String, Integer>> {
        private final List<String> words;

        WordSearchTask(List<String> words) {
            this.words = words;
        }

        @Override
        protected Map<String, Integer> compute() {
            if (words.size() == 1) {
                Map<String, Integer> wordCount = new HashMap<>();
                words.set(0, words.get(0).replaceAll("[^a-zA-Z]", ""));
                words.set(0, words.get(0).toLowerCase());
                if (words.get(0).isEmpty()) {
                    return wordCount;
                }
                wordCount.put(words.get(0), 1);
                return wordCount;
            } else {
                WordSearchTask task1 = new WordSearchTask(words.subList(0, words.size() / 2));
                task1.fork();
                WordSearchTask task2 = new WordSearchTask(words.subList(words.size() / 2, words.size()));

                Map<String, Integer> result1 = task2.compute();
                Map<String, Integer> result2 = task1.join();

                result2.forEach((word, count) -> result1.merge(word, count, Integer::sum));
                return result1;
            }
        }
    }
}