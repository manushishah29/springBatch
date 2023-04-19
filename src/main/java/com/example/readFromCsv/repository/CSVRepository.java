package com.example.readFromCsv.repository;

import com.example.readFromCsv.entity.TestEntity;

import java.util.List;

public interface CSVRepository{

    void savedata(TestEntity testEntity);

    List<TestEntity> findAll(Integer startScore, Integer endScore);
}
