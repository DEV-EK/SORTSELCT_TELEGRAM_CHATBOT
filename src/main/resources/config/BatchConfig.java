package config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class BatchConfig {
	private final JobBuilderFactory jobBuilderFactory;
	private final StepBuilderFactory stepBuilderFactory;
	
	@Bean
	public Job exampleJob(Step coupangJob) throws Exception{
		return jobBuilderFactory.get("coupangJob")
				.incrementer(new RunIdIncrementer())
				.start(coupangJob)
				.build();
	}
	
	@Bean
	@JobScope
	public Step coupangJob(Tasklet tasklet) throws Exception{
		return stepBuilderFactory.get("helloStep")
				.tasklet(tasklet)
				.build();
	}
	
	@Bean
	@StepScope
	public Tasklet tasklet() {
		return (contribution, chunkContext) ->{
			System.out.println("Hello Spring batch");
			return RepeatStatus.FINISHED;
		};
	}
}
