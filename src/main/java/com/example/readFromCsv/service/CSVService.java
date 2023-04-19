package com.example.readFromCsv.service;

import com.example.readFromCsv.entity.TestEntity;

import java.util.List;

public interface CSVService {

    void readCsv();


    List<TestEntity> finAll(Integer startScore, Integer endScore);

    void readData();
}
