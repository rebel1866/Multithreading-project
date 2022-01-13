package by.emaptc.stanislavmelnikov.multithreading.threads;

import by.emaptc.stanislavmelnikov.multithreading.matrixstate.MatrixState;
import by.emaptc.stanislavmelnikov.multithreading.tempresults.TempResultsKeeper;
import by.emaptc.stanislavmelnikov.multithreading.entity.Matrix;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.ReentrantLock;


public class MatrixAction implements Runnable {
    private int threadName;
    private ReentrantLock lock;
    private CountDownLatch latch;
    private MatrixState matrixState;


    public MatrixAction(int threadName, ReentrantLock lock, CountDownLatch latch, MatrixState matrixState) {
        this.threadName = threadName;
        this.lock = lock;
        this.latch = latch;
        this.matrixState = matrixState;
    }

    @Override
    public void run() {
        Logger logger = LogManager.getLogger(MatrixAction.class);
        logger.info("Thread number " + threadName + " has been started ");
        lock.lock();
        logger.info("Thread number " + threadName + " has started working");
        Matrix matrix = Matrix.getInstance();
        int[][] array = matrix.getMatrix();
        Map<String, Integer> diagonalCoords = findFreeDiagonalElement(array);
        if (diagonalCoords.get("row") != null) {
            updateMatrix(array, diagonalCoords);
        }
        logger.info("Thread number " + threadName + " has finished working");
        logger.info("by.emaptc.stanislavmelnikov.multithreading.entity.Matrix state after thread number " + threadName + " is finished: ");
        matrix.printMatrix();
        latch.countDown();
        lock.unlock();
    }

    public void updateMatrix(int[][] array, Map<String, Integer> diagonalCoords) {
        int diagonalRowCoord = diagonalCoords.get("row");
        int diagonalColumnCoord = diagonalCoords.get("column");
        array[diagonalRowCoord][diagonalColumnCoord] = threadName;
        matrixState.changeElementState(diagonalRowCoord, diagonalColumnCoord);
        Map<String, Integer> freeColumnOrRowCoords = findFreeColumnOrRowElement(diagonalRowCoord, diagonalColumnCoord, array);
        int freeRowElement = freeColumnOrRowCoords.get("row");
        int freeColumnElement = freeColumnOrRowCoords.get("column");
        array[freeRowElement][freeColumnElement] = threadName;
        matrixState.changeElementState(freeRowElement, freeColumnElement);
        int rowSum = countRowSum(array, diagonalRowCoord);
        int columnSum = countColumnSum(array, diagonalColumnCoord);
        String threadWork = "Thread changed diagonal element with coords: row = %s, column = %s and free array element " +
                "with coords: row = %s, column = %s. Row sum is: %s, column sum is: %s";
        String threadWorkInfo = String.format(threadWork, diagonalRowCoord, diagonalColumnCoord, freeRowElement,
                freeColumnElement, rowSum, columnSum);
        TempResultsKeeper tempResultsKeeper = TempResultsKeeper.getInstance();
        Map<Integer, String> tempResults = tempResultsKeeper.getTempResults();
        tempResults.put(threadName, threadWorkInfo);
    }


    public Map<String, Integer> findFreeDiagonalElement(int[][] array) {
        Map<String, Integer> coords = new HashMap<>();
        for (int i = 0; i < array.length; i++) {
            if (!matrixState.isBeenChanged(i, i)) {
                coords.put("row", i);
                coords.put("column", i);
                return coords;
            }
            if (!matrixState.isBeenChanged(array.length - i - 1, i)) {
                coords.put("row", array.length - i - 1);
                coords.put("column", i);
                return coords;
            }
        }
        return coords;
    }

    public Map<String, Integer> findFreeColumnOrRowElement(int rowNumber, int columnNumber, int[][] array) {
        Map<String, Integer> coords = new HashMap<>();
        for (int i = 0; i < array.length; i++) {
            if (!matrixState.isBeenChanged(rowNumber, i)) {
                coords.put("row", rowNumber);
                coords.put("column", i);
                return coords;
            }
        }
        for (int i = 0; i < array.length; i++) {
            if (!matrixState.isBeenChanged(i, columnNumber)) {
                coords.put("row", i);
                coords.put("column", columnNumber);
                return coords;
            }
        }
        return coords;
    }

    public int countRowSum(int[][] array, int rowNumber) {
        return Arrays.stream(array[rowNumber]).sum();
    }

    public int countColumnSum(int[][] array, int columnNumber) {
        return Arrays.stream(array).map((l) -> l[columnNumber]).reduce(Integer::sum).get();
    }
}
