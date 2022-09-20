package cz.daiton.foodsquare.configuration;

import cz.daiton.foodsquare.IO.CustomReader;
import cz.daiton.foodsquare.IO.CustomWriter;
import cz.daiton.foodsquare.ingredient.Ingredient;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableBatchProcessing
public class BatchConfig {

    @Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;

    @Autowired
    CustomReader customReader;

    @Autowired
    CustomWriter customWriter;

    @Bean
    public Job importJob() {
        return jobBuilderFactory
                .get("IngredientJob")
                .incrementer(new RunIdIncrementer())
                .flow(importStep()).end().build();
    }

    @Bean
    public Step importStep() {
        return stepBuilderFactory
                .get("IngredientStep")
                .<Ingredient, Ingredient> chunk(50)
                .reader(customReader)
                .writer(customWriter)
                .build();
    }
}
