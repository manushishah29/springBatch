package com.example.readFromCsv.repositoryImpl;


import com.example.readFromCsv.entity.TestEntity;
import com.example.readFromCsv.repository.CSVRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Repository
public class CSVRepositoryImpl implements CSVRepository {
    @Autowired
    RedisTemplate redisTemplate;

    @Override
    public void savedata(TestEntity testEntity) {

        System.out.println("testEntity = " + testEntity.getParam1());
        redisTemplate.opsForZSet().add("ReadData", testEntity, testEntity.getParam1());
    }

    @Override
    public List<TestEntity> findAll(Integer startScore, Integer endScore) {
        Set<TestEntity> entities = redisTemplate.opsForZSet().rangeByScore("TestTemplate", startScore, endScore);
        List<TestEntity> testEntityList = new ArrayList<>();
        assert entities != null;
        for (TestEntity entity : entities) {
            testEntityList.add(entity);
        }

        System.out.println("testEntityList" + testEntityList);
        return testEntityList;
    }
}
