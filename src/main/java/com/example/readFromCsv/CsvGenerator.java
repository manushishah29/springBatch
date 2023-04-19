package com.example.readFromCsv;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;


public class CsvGenerator {
    public static void main(String[] args) {
        String csvFilePath = "output.csv"; // file path where CSV file will be saved
        int rowCount = 100; // number of rows in the CSV file

        try {
            FileWriter csvWriter = new FileWriter(csvFilePath);


            Random random = new Random();
            for (int i = 1; i <= rowCount; i++) {
                // generate random values for each column
                String colA = String.valueOf(i);
                String colB = generateRandomWords(random);
                String colC = generateRandomNumbers(random, 3);
                String colD = generateRandomNumbers(random, 5);

                // add the row to the CSV file
                csvWriter.append(colA).append(",").append(colB).append(",").append(colC).append(",").append(colD).append("\n");
            }

            csvWriter.flush();
            csvWriter.close();
            System.out.println("CSV file generated successfully.");
        } catch (IOException e) {
            System.err.println("Failed to generate CSV file: " + e.getMessage());
        }
    }

    // Generates randomom words
    private static String generateRandomWords(Random random) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 3; i++) {
            char c1 = (char)(random.nextInt(26) + 'A');

            sb.append(c1);

        }
        return sb.toString();

    }

    // Generates randomom numbers
    private static String generateRandomNumbers(Random random, int numDigits) {
        StringBuilder sb = new StringBuilder();
        for (int i = 1; i <= numDigits; i++) {
            sb.append(random.nextInt(10));
        }
        return sb.toString();
    }
}