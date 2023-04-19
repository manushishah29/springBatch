package com.example.readFromCsv.config;

import com.example.readFromCsv.entity.TestEntity;
import com.example.readFromCsv.repository.CSVRepository;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.core.io.UrlResource;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

@Configuration


public class BatchConfig {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final CSVRepository mainRepository;
    public static String CSV_FILE_PATH = "/E:/demo/output.csv";
    public static String LARGE_CSV_FILE_PATH = "";

    public BatchConfig(JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory, CSVRepository mainRepository) {
        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;
        this.mainRepository = mainRepository;
    }

    @Bean
    public FlatFileItemReader<TestEntity> itemReader() throws MalformedURLException {
        FlatFileItemReader<TestEntity> reader = new FlatFileItemReader<>();
        reader.setResource(new UrlResource(new URL("file://" + CSV_FILE_PATH)));
        reader.setLineMapper(new DefaultLineMapper<>() {{
            setLineTokenizer(new DelimitedLineTokenizer() {{
                setDelimiter(",");
                setStrict(false);
                setNames("param1", "param2","param3","param4");
            }});
            setFieldSetMapper(new BeanWrapperFieldSetMapper<TestEntity>() {{
                setTargetType(TestEntity.class);
            }});
        }});
        reader.setMaxItemCount(1000000); // read 1000 records at a time return reader;
        return reader;
    }
    @Bean public Step step1 () throws MalformedURLException {
        return stepBuilderFactory.get("csv-step").<TestEntity, TestEntity>chunk(10)
                .reader(itemReader()).writer(writer()).taskExecutor(taskExecutor()).build();
    }
    @Bean public ItemWriter<TestEntity> writer () {
        return new ItemWriter<TestEntity>() {
            @Override
            public void write(List<? extends TestEntity> list) throws Exception {
                list.forEach(mainRepository::savedata);
            }
        };
    }
    @Bean public TaskExecutor taskExecutor () {
        SimpleAsyncTaskExecutor asyncTaskExecutor = new SimpleAsyncTaskExecutor();
        asyncTaskExecutor.setConcurrencyLimit(10);
        return asyncTaskExecutor;
    }
    @Bean public Job runJob () throws MalformedURLException {
        return jobBuilderFactory.get("importCustomers")
                .flow(step1()).end().build();
    }

}
