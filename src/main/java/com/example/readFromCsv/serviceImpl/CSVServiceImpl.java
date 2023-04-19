package com.example.readFromCsv.serviceImpl;

import com.example.readFromCsv.entity.TestEntity;
import com.example.readFromCsv.repository.CSVRepository;
import com.example.readFromCsv.service.CSVService;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class CSVServiceImpl implements CSVService {

    @Autowired
    CSVRepository repository;
    public static String CSV_FILE_PATH = "E:\\demo\\src\\main\\resources\\output.csv";

    private static List<TestEntity> parseCsvFile(String csvFile) {
        List<TestEntity> dataList = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                TestEntity testEntity = new TestEntity(Integer.parseInt(values[0]), values[1], Integer.parseInt(values[2]), Integer.parseInt(values[3]));
                dataList.add(testEntity);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dataList;
    }

    @Override
    public void readCsv() {
        try (CSVReader csvReader = new CSVReader(new FileReader("E:\\demo\\src\\main\\resources\\output.csv"))) {
            String[] values = null;
            while ((values = csvReader.readNext()) != null) {
//                System.out.println(values[0] + " " + values[1] + " " + values[2] + " " + values[3]);
                TestEntity testEntity = new TestEntity(Integer.parseInt(values[0]), values[1], Integer.parseInt(values[2]), Integer.parseInt(values[3]));
                List<String> testEntityList = new ArrayList<>();
                testEntityList.addAll(List.of(values));
                System.out.print(testEntityList);


            }
        } catch (IOException | CsvValidationException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public List<TestEntity> finAll(Integer startScore, Integer endScore) {
        return repository.findAll(startScore, endScore);
    }

    @Override
    public void readData() {
        int numThreads = 10;
        List<TestEntity> dataList = parseCsvFile(CSV_FILE_PATH);
        int batchSize = dataList.size() / numThreads;
        ExecutorService executor = Executors.newFixedThreadPool(numThreads);
        for (int i = 0; i < numThreads; i++) {
            int start = i * batchSize;
            int end = (i == numThreads - 1) ? dataList.size() : (i + 1) * batchSize;
            List<TestEntity> subset = dataList.subList(start, end);
            executor.execute(new InsertDataRunnable(subset));
        }
        executor.shutdown();
    }

    private class InsertDataRunnable implements Runnable {
        private final List<TestEntity> dataList;

        public InsertDataRunnable(List<TestEntity> dataList) {
            this.dataList = dataList;
        }

        @Override
        public void run() {
            dataList.forEach(repository::savedata);
        }
    }

    public static List<String> split(String s, int chunkSize) {
        return IntStream.iterate(0, i -> i < s.length(), i -> i + chunkSize)
                .mapToObj(i -> s.substring(i, Math.min(s.length(), i + chunkSize)))
                .collect(Collectors.toList());
    }


}
