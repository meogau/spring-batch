package thainv.research.springbatch;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionException;

@SpringBootApplication
@RequiredArgsConstructor
public class SpringBatchApplication {


	private final JobLauncher jobLauncher;

    final Job job;

	public static void main(String[] args) {
		SpringApplication.run(SpringBatchApplication.class, args);
	}

	@Bean
	public CommandLineRunner run() {
		return _ -> {
			try {
				var startTime = System.currentTimeMillis();
				jobLauncher.run(job, new JobParameters());
				var endTime = System.currentTimeMillis();
				System.out.println("Job execution time: " + (endTime - startTime));
			} catch (JobExecutionException e) {
				System.out.println("Job failed");
				e.printStackTrace();
			}
		};
	}
}
