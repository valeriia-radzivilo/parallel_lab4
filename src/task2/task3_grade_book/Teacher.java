package task2.task3_grade_book;

import java.util.Random;

public class Teacher implements Runnable {
    private final String name;
    private final GradeBook journal;
    private final Random random = new Random();

    public Teacher(GradeBook journal, String name) {
        this.name = name;
        this.journal = journal;
    }

    @Override
    public void run() {
        for (int i = 0; i < 10; i++) {
            int studentId = random.nextInt(90) + 1;
            int grade = random.nextInt(100) + 1;
            journal.putGrade(studentId, Integer.parseInt(name.split(" ")[1]) - 1, grade);

        }
    }
}