package by.emaptc.stanislavmelnikov.multithreading.entity;

import by.emaptc.stanislavmelnikov.multithreading.outputinput.PropertyLoader;

import java.util.Arrays;

public class Matrix {
    private int[][] matrix;
    private final static String keyN = "key.n";
    private final static String arrayFillValue = "array.fill.value";

    private Matrix() {
        int n = Integer.parseInt(PropertyLoader.getProperty(keyN));
        int fillValue = Integer.parseInt(PropertyLoader.getProperty(arrayFillValue));
        matrix = new int[n][n];
        for (int[] array : matrix) {
            Arrays.fill(array, fillValue);
        }
    }

    private static class MatrixHolder {
        private final static Matrix instance = new Matrix();
    }

    public static Matrix getInstance() {
        return MatrixHolder.instance;
    }

    public int[][] getMatrix() {
        return matrix;
    }

    public void printMatrix() {
        String space = " ";
        String lineBreak = "\n";
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                System.out.print(matrix[i][j] + space);
            }
            System.out.print(lineBreak);
        }
        System.out.println(lineBreak + lineBreak);
    }
}
