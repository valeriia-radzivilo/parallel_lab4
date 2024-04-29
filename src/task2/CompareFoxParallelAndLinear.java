package task2;

import org.apache.commons.lang3.time.StopWatch;
import task2.fox.FoxAlgorithm;
import task2.fox_fork_join.FoxForkJoin;

import java.util.Random;

public class CompareFoxParallelAndLinear {
    public static void main(String[] args) {
        final Random random = new Random();
        final int size = (int) Math.pow(2, 12);
        final int[][] matrix1 = generateRandomMatrix(size);
        final int[][] matrix2 = generateRandomMatrix(size);

//        System.out.println("Matrix 1:");
//        printMatrix(matrix1);
//        System.out.println("\n\nMatrix 2:");
//        printMatrix(matrix2);


        final StopWatch timer = new StopWatch();
        final FoxAlgorithm foxAlgorithm = new FoxAlgorithm(new StopWatch(), 10, matrix1, matrix2);
        timer.start();
        final int[][] result = foxAlgorithm.algorithm();
        timer.stop();
        System.out.println("Time: " + timer.getTime());

        timer.reset();
        final FoxForkJoin foxForkJoin = new FoxForkJoin(new StopWatch(), 10, matrix1, matrix2);
        timer.start();
        final int[][] res = foxForkJoin.multiply();
        timer.stop();
        System.out.println("Time ForkJoin: " + timer.getTime());
    }


    private static int[][] generateRandomMatrix(int size) {
        final Random random = new Random();

        final int[][] matrix = new int[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                matrix[i][j] = random.nextInt(1, 10);
            }
        }
        return matrix;
    }

    private static void printMatrix(int[][] matrix) {
        for (int[] ints : matrix) {
            for (int anInt : ints) {
                System.out.print(anInt + " ");
            }
            System.out.println();
        }
    }
}