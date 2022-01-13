package by.emaptc.stanislavmelnikov.multithreading.runner;

import by.emaptc.stanislavmelnikov.multithreading.matrixstate.MatrixState;
import by.emaptc.stanislavmelnikov.multithreading.threads.ThreadsManager;
import by.emaptc.stanislavmelnikov.multithreading.outputinput.PropertyLoader;
import by.emaptc.stanislavmelnikov.multithreading.outputinput.Writer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.concurrent.locks.ReentrantLock;

public class Runner {
    private static final String keyN = "key.n";
    private static final String keyY = "key.y";

    public static void main(String[] args) {
        Writer writer = new Writer();
        ReentrantLock lock = new ReentrantLock();
        int n = Integer.parseInt(PropertyLoader.getProperty(keyN));
        int y = Integer.parseInt(PropertyLoader.getProperty(keyY));
        MatrixState matrixState = new MatrixState(n);
        matrixState.setStateDefault();
        ThreadsManager tManager = new ThreadsManager(lock, writer, matrixState);
        try {
            tManager.runThreads(y, n);
        } catch (InterruptedException | IOException e) {
            Logger logger = LogManager.getLogger(Runner.class);
            logger.error("exception while running threads");
            System.exit(0);
        }
    }
}