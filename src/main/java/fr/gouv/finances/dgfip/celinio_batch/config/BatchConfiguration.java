package fr.gouv.finances.dgfip.celinio_batch.config;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.Job; 
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.job.parameters.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.Step;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.infrastructure.item.ItemReader;
import org.springframework.batch.infrastructure.item.ItemWriter;
import org.springframework.batch.infrastructure.item.database.JpaItemWriter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.transaction.PlatformTransactionManager;

import fr.gouv.finances.dgfip.celinio_batch.persistance.Recette;
import jakarta.persistence.EntityManagerFactory;

@Configuration
//@EnableBatchProcessing
// Il faut enlever @EnableBatchProcessing pour que Spring Boot prenne le contrôle sur la configuration Spring Batch, 
// y compris la création du schéma des tables Batch dans la datasource autoconfigurée
// https://github.com/spring-projects/spring-batch/issues/4252
public class BatchConfiguration {

	@Value("${file.input}")
	private Resource inputFile;

	/**
	 * Le writer est responsable de l'enregistrement des données traitées. 
	 * Ici, il utilise EntityManagerFactory pour sauvegarder toutes les recettes dans la base de données.
	 * @param emf
	 * @return
	 */
	@Bean
	public JpaItemWriter<Recette> recetteWriter(EntityManagerFactory emf) {
		return new org.springframework.batch.infrastructure.item.database.JpaItemWriter<>(emf);
	}

	/**
	 * Cette étape du job (step) se compose d'un lecteur (reader) et d'un écrivain (writer).
	 * Le type de traitement se fait par blocs (chunks). 
	 * Cela signifie que Spring Batch lira 10 enregistrements à la fois (grâce à chunk(10)), 
	 * les traitera via le processeur (il n'y en a pas ici), et les écrira ensuite. 
	 * StepBuilder permet de construire cette étape avec une gestion transactionnelle grâce à PlatformTransactionManager.
	 * @param jobRepository
	 * @param transactionManager
	 * @param recetteReader
	 * @param recetteWriter
	 * @return
	 */
	@Bean
	public Step importRecetteStep(JobRepository jobRepository, PlatformTransactionManager transactionManager,
			ItemReader<Recette> recetteReader, ItemWriter<Recette> recetteWriter) {
		return new StepBuilder("importRecettesStep", jobRepository).<Recette, Recette>chunk(10).reader(recetteReader)
				.writer(recetteWriter).transactionManager(transactionManager).build();
	}

	/**
	 * Un job est composé d'une ou plusieurs étapes (steps).
	 * @param jobRepository
	 * @param importRecetteStep
	 * @return
	 */
	@Bean
	public Job importRecetteJob(JobRepository jobRepository, Step importRecetteStep) {
		// Utilisation de incrementer(new RunIdIncrementer()) pour éviter l'erreur
		// suivante :
		// "org.springframework.batch.core.launch.JobInstanceAlreadyCompleteException:
		// A job instance already exists and is complete for identifying parameters={}.
		// If you want to run this job again, change the parameters."
		// RunIdIncrementer permet d'ajouter un identifiant unique à chaque exécution du job, 
		// ce qui est utile pour exécuter plusieurs fois le même job sans conflit.
		return new JobBuilder("importRecettesJob", jobRepository).incrementer(new RunIdIncrementer())
				.start(importRecetteStep).build();
	}
}