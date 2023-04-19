package com.example.readFromCsv;

import java.io.*;

public class DevideCSV {
    public static void main(String[] args) {
        String inputFileName = "E:\\demo\\src\\main\\resources\\output.csv";
        int linesPerFile = 1000000;
        int fileCount = 1;
        try (BufferedReader br = new BufferedReader(new FileReader(inputFileName))) {
            String line;
            int lineCount = 1;
            BufferedWriter bw = new BufferedWriter(new FileWriter("E:\\demo\\src\\main\\resources\\output_" + fileCount + ".csv"));
            while ((line = br.readLine()) != null) {
                bw.write(line);
                bw.newLine();
                lineCount++;
                if (lineCount == linesPerFile) {
                    bw.close();
                    fileCount++;
                    if ((line = br.readLine()) != null) {
                        bw = new BufferedWriter(new FileWriter("E:\\demo\\src\\main\\resources\\output_" + fileCount + ".csv"));
                    }
                    lineCount = 0;
                }
            }
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
