package task2.task3_grade_book;

import java.util.Arrays;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class GradeBook {
    private final int[][] grades;
    private final Lock[][] locks;

    public GradeBook(int studentsPerGroup, int numGroups, int numTeachers) {
        grades = new int[studentsPerGroup * numGroups][numTeachers];
        locks = new ReentrantLock[studentsPerGroup * numGroups][numTeachers];
        for (int i = 0; i < studentsPerGroup * numGroups; i++) {
            for (int j = 0; j < numTeachers; j++) {
                locks[i][j] = new ReentrantLock();
            }
        }
    }

    public void putGrade(int studentId, int teacherId, int grade) {
        locks[studentId][teacherId].lock();
        try {
            if (Arrays.stream(grades[studentId]).anyMatch(e -> e > 0)) {
                return;
            }
            grades[studentId][teacherId] = grade;
//            System.out.println("Teacher " + (teacherId + 1) + " gave student " + studentId + " a grade of " + grade);
        } finally {
            locks[studentId][teacherId].unlock();
        }
    }
}