package task2.fox_fork_join;

import org.apache.commons.lang3.time.StopWatch;

import java.util.Arrays;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;

public class FoxForkJoin {

    final StopWatch timer;
    final int[][] matrix1;
    final int[][] matrix2;
    final int threads;

    public FoxForkJoin(StopWatch timer, int threads, int[][] matrix1, int[][] matrix2) {
        this.timer = timer;
        this.matrix1 = matrix1;
        this.matrix2 = matrix2;
        this.threads = threads;
    }

    public int[][] multiply() {
        timer.start();
        MatrixMultiplierTask task = new MatrixMultiplierTask(matrix1, matrix2);
        ForkJoinPool pool = new ForkJoinPool();
        final int[][] res = pool.invoke(task);
        timer.stop();
        return res;
    }

    private static class MatrixMultiplierTask extends RecursiveTask<int[][]> {

        private final int[][] matrix1;
        private final int[][] matrix2;

        MatrixMultiplierTask(int[][] matrix1, int[][] matrix2) {
            this.matrix1 = matrix1;
            this.matrix2 = matrix2;
        }

        @Override
        protected int[][] compute() {
            int size = matrix1.length;

            if (size == 1) {
                return new int[][]{{matrix1[0][0] * matrix2[0][0]}};
            } else {
                int newSize = size / 2;

                // Create tasks for each block
                MatrixMultiplierTask task11 = new MatrixMultiplierTask(
                        Arrays.copyOfRange(matrix1, 0, newSize),
                        Arrays.copyOfRange(matrix2, 0, newSize)
                );
                MatrixMultiplierTask task12 = new MatrixMultiplierTask(
                        Arrays.copyOfRange(matrix1, 0, newSize),
                        Arrays.copyOfRange(matrix2, newSize, size)
                );
                MatrixMultiplierTask task21 = new MatrixMultiplierTask(
                        Arrays.copyOfRange(matrix1, newSize, size),
                        Arrays.copyOfRange(matrix2, 0, newSize)
                );
                MatrixMultiplierTask task22 = new MatrixMultiplierTask(
                        Arrays.copyOfRange(matrix1, newSize, size),
                        Arrays.copyOfRange(matrix2, newSize, size)
                );

                // Run tasks in parallel
                ForkJoinTask.invokeAll(task11, task12, task21, task22);

                // Combine the results
                int[][] result = new int[size][size];
                for (int i = 0; i < newSize; i++) {
                    System.arraycopy(task11.join()[i], 0, result[i], 0, newSize);
                    System.arraycopy(task12.join()[i], 0, result[i], newSize, newSize);
                    System.arraycopy(task21.join()[i], 0, result[i + newSize], 0, newSize);
                    System.arraycopy(task22.join()[i], 0, result[i + newSize], newSize, newSize);
                }

                return result;
            }
        }
    }
}