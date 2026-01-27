package fr.gouv.finances.dgfip.celinio_batch.batch;

import org.springframework.batch.infrastructure.item.file.FlatFileItemReader;
import org.springframework.batch.infrastructure.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.infrastructure.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import fr.gouv.finances.dgfip.celinio_batch.persistance.Recette;

@Configuration
public class MonFlatFileItemReader {

    @Value("${file.input}")
    private Resource fileInput; // Injecter le fichier CSV
    
	@Bean
	public FlatFileItemReader<Recette> reader() {
	    return new FlatFileItemReaderBuilder<Recette>().name("recetteReader")
	      .resource(fileInput)
	      .delimited()
	      .names("nom", "typeCuisine", "difficulte", "tempsPreparation", "ingredients", "instructions")
	      .fieldSetMapper(new BeanWrapperFieldSetMapper<>() {{
	          setTargetType(Recette.class);
	      }})
	      .linesToSkip(1) // <-- Pour ignorer l'entête du fichier liste-recettes.csv
	      .build();
	} 
}
