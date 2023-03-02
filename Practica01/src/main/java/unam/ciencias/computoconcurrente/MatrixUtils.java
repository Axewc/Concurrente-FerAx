package unam.ciencias.computoconcurrente;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class MatrixUtils {
    private int threads;
    private double[] partialAverages;
    public int secciones; 
    private static int[] posiblesMinimos; // Arreglo para que cada hilo guarde su minimo encontrado
    private static int[] matrixGlobal; 

    public MatrixUtils() {
        this.threads = 1;
    }

    public MatrixUtils(int threads) {
        this.threads = threads;
    }

    /**
     * Encuentra el promedio de una matriz de enteros utilizando hilos.
     * 
     * @param matrix la matriz de enteros
     * @return el promedio de la matriz
     * @throws InterruptedException si ocurre un error en la ejecución de los hilos
     */
    public double findAverage(int[][] matrix) throws InterruptedException {
        int rows = matrix.length;
        int cols = matrix[0].length;

        // Dividir la matriz en secciones para cada hilo
        int sectionSize = rows / threads;
        int remainingRows = rows % threads;
        int startIndex = 0;
        int endIndex;

        // Crear executor service para los hilos
        ExecutorService executor = Executors.newFixedThreadPool(threads);

        // Crear arreglo de promedios parciales
        partialAverages = new double[threads];

        // Crear hilos
        for (int i = 0; i < threads; i++) {
            endIndex = startIndex + sectionSize + (i < remainingRows ? 1 : 0);
            int[][] section = new int[endIndex - startIndex][cols];
            System.arraycopy(matrix, startIndex, section, 0, section.length);
            startIndex = endIndex;

            // Ejecutar hilo para calcular promedio de la sección
            executor.execute(new AverageCalculator(section, i));
        }

        // Esperar a que terminen todos los hilos
        executor.shutdown();
        executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);

        // Calcular promedio total
        double total = 0.0;
        for (double partial : partialAverages) {
            total += partial;
        }
        return (double)total / threads;
    }

    /**
     * Calcula el promedio de una sección de la matriz y guarda el resultado en el
     * arreglo de promedios parciales.
     */
    private class AverageCalculator implements Runnable {
        private int[][] section;
        private int index;

        public AverageCalculator(int[][] section, int index) {
            this.section = section;
            this.index = index;
        }

        @Override
        public void run() {
            int sum = 0;
            for (int[] row : section) {
                for (int value : row) {
                    sum += value;
                }
            }
            partialAverages[index] = (double) sum / (section.length * section[0].length);
        }
    }

    public void findMinimum(int[][] matrix) {
        int rows = matrix.length;
        int columns = matrix[0].length;
        int total = rows * columns;
        matrixGlobal = new int[total];
        int k = 0;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                matrixGlobal[k] = matrix[i][j];
                k++;
            }
        }
        secciones = total / threads;
        posiblesMinimos = new int[threads];  
        Thread[] hilos = new Thread[threads]; 
        for (int i = 0; i < threads; i++) { 
            hilos[i] = new Thread();
            hilos[i].start();
        }
        for (int i = 0; i < threads; i++) { 
            try {
                hilos[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        double minimo = posiblesMinimos[0]; //
        for (int i = 0; i < threads; i++) {
            if (posiblesMinimos[i] < minimo) {
                minimo = posiblesMinimos[i];
            }
        }
        System.out.println("El minimo es aa: " + minimo);
    }


}
