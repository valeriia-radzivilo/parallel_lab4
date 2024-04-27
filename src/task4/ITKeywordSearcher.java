package task4;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

public class ITKeywordSearcher {

    // IT keywords list
    private static final String[] _keywords = {
            "abstract", "assert", "boolean", "break", "byte", "case", "catch", "char", "class", "const",
            "continue", "default", "double", "enum", "extends", "final", "finally", "float",
            "goto", "implements", "import", "instanceof", "int", "interface", "long", "native",
            "package", "protected", "return", "short", "static", "strictfp",
            "super", "switch", "synchronized", "transient", "void", "volatile",
            "parallel", "stream", "lambda", "module", "var", "record", "sealed", "non-sealed", "permits",
            "Java", "JVM", "JRE", "JDK", "JAR", "JSP", "JSF", "JDBC", "JPA", "JAX-RS", "JAX-WS", "JAX-B", "JAXP",
            "Kotlin", "Groovy", "Scala", "Clojure", "JRuby", "Jython", "Gosu", "Frege", "Xtend", "Ceylon", "Mirah",
            "Dart", "Flutter", "TypeScript", "CoffeeScript", "Elm", "PureScript", "ReasonML", "Fable", "BuckleScript",
            "Computer", "Software", "Programming", "Algorithms", "Data", "Operating", "systems", "Databases", "Networks",
            "Engineering",
    };

    private static final String[] keywords = Arrays.stream(_keywords).map(String::toLowerCase).toArray(String[]::new);

    public static void main(String[] args) throws Exception {
        try {
            Path folder = Paths.get("assets");
            List<Path> files = Files.walk(folder)
                    .filter(Files::isRegularFile)
                    .toList();

            ForkJoinPool forkJoinPool = new ForkJoinPool();

            for (Path file : files) {
                String document = new String(Files.readAllBytes(file));
                List<String> words = Arrays.asList(document.split("\\s+"));

                ITKeywordSearchTask task = new ITKeywordSearchTask(words, file.getFileName().toString());
                boolean containsITKeywords = forkJoinPool.invoke(task);

                System.out.println("Document " + file + " contains IT keywords: " + containsITKeywords);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static class ITKeywordSearchTask extends RecursiveTask<Boolean> {
        private final List<String> words;

        private final String fileName;

        ITKeywordSearchTask(List<String> words, String fileName) {
            this.words = words;
            this.fileName = fileName;
        }

        @Override
        protected Boolean compute() {
            if (words.size() == 1) {

                // remove punctuation and convert to lowercase
                words.set(0, words.get(0).replaceAll("[^a-zA-Z]", "").toLowerCase());
              
                return Arrays.asList(keywords).contains(words.get(0));
            } else {
                ITKeywordSearchTask task1 = new ITKeywordSearchTask(words.subList(0, words.size() / 2), fileName);
                ITKeywordSearchTask task2 = new ITKeywordSearchTask(words.subList(words.size() / 2, words.size()), fileName);

                task1.fork();
                return task2.compute() || task1.join();
            }
        }
    }
}