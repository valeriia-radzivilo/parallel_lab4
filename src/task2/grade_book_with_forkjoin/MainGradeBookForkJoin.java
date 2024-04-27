package task2.grade_book_with_forkjoin;


import task2.task3_grade_book.GradeBook;

import java.util.Random;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

public class MainGradeBookForkJoin {
    public static void main(String[] args) {
        GradeBook journal = new GradeBook(30, 3, 4);

        method(journal);
    }


    public static void method(GradeBook journal) {
        ForkJoinPool forkJoinPool = new ForkJoinPool();

        for (int i = 0; i < 4; i++) {
            forkJoinPool.execute(new TeacherTask(journal, "Teacher " + (i + 1)));
        }

        forkJoinPool.shutdown();
    }

    private static class TeacherTask extends RecursiveAction {
        private final String name;
        private final GradeBook journal;
        private final Random random = new Random();

        TeacherTask(GradeBook journal, String name) {
            this.name = name;
            this.journal = journal;
        }

        @Override
        protected void compute() {
            for (int i = 0; i < 10; i++) {
                int studentId = random.nextInt(90) + 1;
                int grade = random.nextInt(100) + 1;
                journal.putGrade(studentId, Integer.parseInt(name.split(" ")[1]) - 1, grade);
            }
        }
    }
}