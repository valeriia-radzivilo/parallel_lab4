package task2.task3_grade_book;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainGradeBook {
    public static void main(String[] args) {
        GradeBook journal = new GradeBook(30, 3, 4);

        method(journal);


    }

    public static void method(GradeBook journal) {
        ExecutorService executor = Executors.newFixedThreadPool(4);

        for (int i = 0; i < 4; i++) {
            executor.execute(new Teacher(journal, "Teacher " + (i + 1)));
        }

        executor.shutdown();
    }

}