package by.emaptc.stanislavmelnikov.multithreading.outputinput;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;

public class Writer {
    private final static String filePath = "D://info.txt";
    private final static String lineBreak = "\r\n";

    public void writeText(String text) throws IOException {
        FileOutputStream fileOutputStream = new FileOutputStream(filePath, true);
        byte[] buffer = text.getBytes();
        fileOutputStream.write(buffer);
        fileOutputStream.flush();
        fileOutputStream.close();
    }

    public void writeArray(int[][] array) throws IOException {
        String arrayString = prepareArrayForWriting(array);
        writeText(arrayString);
    }

    public void writeTemp(Map<Integer, String> tempResults) throws IOException {
        String temp = prepareTempForWriting(tempResults);
        writeText(temp);
    }

    public String prepareTempForWriting(Map<Integer, String> tempResults) {
        StringBuilder text = new StringBuilder();
        tempResults.entrySet().stream().sorted(Map.Entry.comparingByKey()).forEach((l) -> text.append("Thread ").
                append(l).append(lineBreak));
        return text.toString();
    }

    public String prepareArrayForWriting(int[][] array) {
        StringBuilder arrayBuilder = new StringBuilder();
        String space = " ";
        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array[i].length; j++) {
                arrayBuilder.append(array[i][j]).append(space);
            }
            arrayBuilder.append(lineBreak);
        }
        arrayBuilder.append(lineBreak).append(lineBreak);
        return arrayBuilder.toString();
    }
}
