package task2.grade_book_with_forkjoin;

import task2.task3_grade_book.GradeBook;
import task2.task3_grade_book.MainGradeBook;

public class TestForkJoinSpeedup {

    public static void main(String[] args) {
        GradeBook journal = new GradeBook(1000, 1000, 4);

        for (int i = 0; i < 10; i++) {
            final long startTime = System.currentTimeMillis();
            MainGradeBook.method(journal);
            final long endTime = System.currentTimeMillis();
            System.out.println("Time taken with ExecutorService: " + (endTime - startTime) + "ms");

            final long startTimeForkJoin = System.currentTimeMillis();
            MainGradeBookForkJoin.method(journal);
            final long endTimeForkJoin = System.currentTimeMillis();
            System.out.println("Time taken with ForkJoinPool: " + (endTimeForkJoin - startTimeForkJoin) + "ms");

            if (endTimeForkJoin - startTimeForkJoin == 0) {
                System.out.println("Speedup: " + (endTime - startTime));

            } else {
                final long speedup = (endTime - startTime) / (endTimeForkJoin - startTimeForkJoin);
                System.out.println("Speedup: " + speedup);
            }
        }
    }
}