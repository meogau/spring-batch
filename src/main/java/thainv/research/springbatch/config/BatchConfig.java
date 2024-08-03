package thainv.research.springbatch.config;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;
import thainv.research.springbatch.entity.Data;
import thainv.research.springbatch.entity.ProcessedData;

import javax.sql.DataSource;

@Configuration
@EnableBatchProcessing
@RequiredArgsConstructor
public class BatchConfig {


    @Bean
    public Job processJob(JobRepository jobRepository, Step step1) {
        return new JobBuilder("processJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .flow(step1)
                .end()
                .build();
    }

    @Bean
    public Step step1(JobRepository jobRepository, PlatformTransactionManager transactionManager, ItemReader<Data> reader, ItemProcessor<Data, ProcessedData> processor, ItemWriter<ProcessedData> writer) {
        return new StepBuilder("step1", jobRepository)
                .<Data, ProcessedData>chunk(1, transactionManager)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .build();
    }

    @Bean
    public JdbcCursorItemReader<Data> reader(DataSource dataSource) {
        JdbcCursorItemReader<Data> reader = new JdbcCursorItemReader<>();
        reader.setDataSource(dataSource);
        reader.setSql("SELECT id, data FROM data");
        reader.setRowMapper(new BeanPropertyRowMapper<>(Data.class));
        return reader;
    }

    @Bean
    public ItemProcessor<Data, ProcessedData> processor() {
        return data -> {
            ProcessedData processedData = new ProcessedData();
            processedData.setData(data.getData());
            processedData.setId(data.getId());
            processedData.setStatus(1);
            return processedData;
        };
    }

    @Bean
    public JdbcBatchItemWriter<ProcessedData> writer(DataSource dataSource) {
        JdbcBatchItemWriter<ProcessedData> writer = new JdbcBatchItemWriter<>();
        writer.setDataSource(dataSource);
        writer.setSql("INSERT INTO processed_data (id,data,status) VALUES (:id, :data, :status)");
        writer.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>());
        return writer;
    }
    @Bean
    public TaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(100);
        taskExecutor.setMaxPoolSize(200);
        taskExecutor.setQueueCapacity(3000);
        taskExecutor.afterPropertiesSet();
        return taskExecutor;
    }
}