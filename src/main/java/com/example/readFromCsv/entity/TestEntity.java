package com.example.readFromCsv.entity;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@RedisHash("TestEntity")

public class TestEntity implements Serializable {


    @Id
    private Integer param1;

    @Indexed
    private String param2;

    @Indexed
    private Integer param3;

    @Indexed
    private Integer param4;


}
