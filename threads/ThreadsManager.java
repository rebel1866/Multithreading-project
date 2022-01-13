package by.emaptc.stanislavmelnikov.multithreading.threads;

import by.emaptc.stanislavmelnikov.multithreading.matrixstate.MatrixState;
import by.emaptc.stanislavmelnikov.multithreading.tempresults.TempResultsKeeper;
import by.emaptc.stanislavmelnikov.multithreading.entity.Matrix;
import by.emaptc.stanislavmelnikov.multithreading.outputinput.Writer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

public class ThreadsManager {
    private ReentrantLock lock;
    private Writer writer;
    private CountDownLatch latch;
    private MatrixState matrixState;
    private Logger logger = LogManager.getLogger(ThreadsManager.class);

    public ThreadsManager(ReentrantLock lock, Writer writer, MatrixState matrixState) {
        this.lock = lock;
        this.writer = writer;
        this.matrixState = matrixState;
    }

    public void runThreads(int y, int n) throws InterruptedException, IOException {
        int amountThreads = y * n;
        ExecutorService executorService = Executors.newFixedThreadPool(amountThreads);
        latch = new CountDownLatch(n);
        for (int i = 1; i < amountThreads + 1; i++) {
            MatrixAction action = new MatrixAction(i, lock, latch, matrixState);
            executorService.submit(action);
            TimeUnit.MILLISECONDS.sleep(100);
            if (i % n == 0) {
                writeIntermediateResults(n);
            }
        }
        executorService.shutdown();
    }

    public void writeIntermediateResults(int n) throws IOException, InterruptedException {
        TempResultsKeeper keeper = TempResultsKeeper.getInstance();
        Matrix matrix = Matrix.getInstance();
        Map<Integer, String> tempResults = keeper.getTempResults();
        writer.writeTemp(tempResults);
        logger.info("Starting to write");
        int[][] array = matrix.getMatrix();
        writer.writeArray(array);
        logger.info("Writing is done.");
        tempResults.clear();
        latch.await();
        latch = new CountDownLatch(n);
    }
}
