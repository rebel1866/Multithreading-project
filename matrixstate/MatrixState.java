package by.emaptc.stanislavmelnikov.multithreading.matrixstate;

import java.util.HashMap;
import java.util.Map;

public class MatrixState {
    private int n;
    private Map<String, Boolean> elementStates = new HashMap<>();

    public MatrixState(int n) {
        this.n = n;
    }

    public void setStateDefault() {
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                String key = generateKey(i, j);
                elementStates.put(key, false);
            }
        }
    }

    public boolean isBeenChanged(int rowElement, int colElement) {
        String key = generateKey(rowElement, colElement);
        return elementStates.get(key);
    }

    public void changeElementState(int rowElement, int colElement) {
        String key = generateKey(rowElement, colElement);
        elementStates.replace(key, true);
    }

    public String generateKey(int key1, int key2) {
        String space = " ";
        return key1 + space + key2;
    }
}
