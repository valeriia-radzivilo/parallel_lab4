package task2.fox;

class FoxAlgorithmThread extends Thread {
    private final int[][] multiplied;
    private final int[][] multiplicator;
    private final int[][] result;
    private final int offsetRow;
    private final int offsetColumn;

    public FoxAlgorithmThread(
            int[][] multiplied,
            int[][] multiplicator,
            int[][] result,
            int offsetRow,
            int offsetColumn) {
        this.multiplied = multiplied;
        this.multiplicator = multiplicator;
        this.result = result;
        this.offsetRow = offsetRow;
        this.offsetColumn = offsetColumn;
    }

    @Override
    public void run() {
        int[][] blockRes = new int[multiplied.length][multiplicator.length];
        for (int i = 0; i < multiplied.length; i++) {
            for (int j = 0; j < multiplicator.length; j++) {
                for (int k = 0; k < multiplied.length; k++) {
                    blockRes[i][j] += multiplied[i][k] * multiplicator[k][j];
                }
            }
        }

        for (int i = 0; i < blockRes.length; i++) {
            for (int j = 0; j < blockRes.length; j++) {
                result[i + offsetRow][j + offsetColumn] += blockRes[i][j];
            }
        }
    }
}