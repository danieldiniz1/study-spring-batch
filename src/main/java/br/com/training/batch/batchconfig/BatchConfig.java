package br.com.training.batch.batchconfig;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.*;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@EnableBatchProcessing
@Configuration
public class BatchConfig extends DefaultBatchConfigurer {

    public static Logger LOGGER = LogManager.getLogger();

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;


    @Bean
    public Job imprimeOJob(){
        return  jobBuilderFactory
                .get("imprimeOJob")
                .start(imprimeOlaStep())
                .incrementer(new RunIdIncrementer()) // permite a execucao de um job mais de uma vez sempre incrementando novas infos
                .build();
    }

    public Step imprimeOlaStep(){
        return stepBuilderFactory
                .get("imprimeOJob")
                .tasklet(imprimeOlaTasklet(null))
                .build();
    }

    @Bean // tem que ser um bean disponível para o spring
    @StepScope // coloca método no contexto para pegar o parametro
    public Tasklet imprimeOlaTasklet(@Value("#{jobParameters['nome']}") String nome) { // valor é buscado de um parametro quando app inicializa
        return new Tasklet() {
            @Override
            public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
                LOGGER.info("Olá, " + nome +  "!");
                return RepeatStatus.FINISHED;
            }
        };
    }

}
