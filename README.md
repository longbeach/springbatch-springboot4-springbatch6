# Spring Batch Application

[[_TOC_]]

## Pile technologique

- Spring Boot 4.0.0
- Spring Batch 6.0.0
- JDK 17

## Initialisation de la base de données

### 📄 Tables applicatives : schema-all.sql
Le script `schema-all.sql` est exécuté par Spring Boot au démarrage de l'application grâce à la propriété spring.sql.init.mode : `spring.sql.init.mode=always`

Il crée la table applicative `RECETTE`.

### ⚙️ Tables metadata de Spring Batch
- On peut créer automatiquement les tables de métadonnées `BATCH_*` nécessaires au suivi des jobs (JobInstance, JobExecution, StepExecution, etc.).
Spring Batch utilise une base de données pour stocker l'état des jobs, comme les métadonnées, les étapes de jobs, l'avancement des traitements, et pour faciliter les reprises en cas d'échec.

Ce sont les tables suivantes :

- BATCH_JOB_INSTANCE
- BATCH_JOB_EXECUTION
- BATCH_JOB_EXECUTION_PARAM
- BATCH_STEP_EXECUTION
- BATCH_STEP_EXECUTION_CONTEXT
- BATCH_JOB_EXECUTION_CONTEXT

![alt text](./src/main/resources/images/TablesBatch.png)

Dans application.properties, il faut positionner la propriété spring.batch.jdbc.initialize-schema à always :
`spring.batch.jdbc.initialize-schema=always`

https://docs.spring.io/spring-boot/how-to/data-initialization.html#howto.data-initialization.batch

- Quelles sont les dépendances requises pour générer les tables METADATA (BATCH_*) ?

```xml
<dependency>
<groupId>org.springframework.boot</groupId>
<artifactId>spring-boot-starter-batch-jdbc</artifactId>
</dependency>
```

> [!important]
> Pour que Spring Boot garde le contrôle sur l'autoconfiguration, il est important de ne pas utiliser l'annotation `@EnableBatchProcessing`.

> [!note]
> Autre scénario : si l'on souhaite insérer les instructions SQL de création des tables BATCH_* dans le fichier `schema-all.sql` alors il faut positionner la propriété `spring.batch.jdbc.initialize-schema` à `never`.
On peut récupérer les instructions de création des tables depuis l'archive spring-batch-core-6.0.0-sources.jar: \org\springframework\batch\core\schema-postgresql.sql 

## Que fait ce batch ?

Il lit les recettes du fichier liste-recettes.csv pour les insérer en base de données, dans la table RECETTE.

![alt text](./src/main/resources/images/TableRecette.png)

## Spring Batch 6 — Mode Resourceless

Depuis Spring Batch 6, il est possible d’exécuter des jobs sans base de données, en utilisant un mode appelé resourceless.

Cela signifie que Spring Batch ne persiste plus les métadonnées d’exécution (jobs, steps, statuts, paramètres) dans des tables SQL.

C'est le comportement par défaut (resourceless) <br/>
https://docs.spring.io/spring-batch/reference/whatsnew.html#_resourceless_batch_infrastructure_by_default

Nouveautés : <br/>
https://docs.spring.io/spring-batch/reference/whatsnew.html

Spring Batch 6 introduit un JobRepository en mémoire (in-memory) :
- Aucune table SQL
- Aucune base de données requise
- Aucune persistance disque

Les métadonnées sont conservées uniquement :

- En mémoire RAM
- Pendant la durée d’exécution du job
- Une fois l’application arrêtée : tout est perdu.

Avantages du mode resourceless

- Démarrage plus rapide
- Configuration simplifiée
- Pas besoin de PostgreSQL / MySQL

Idéal pour :
- Jobs simples
- Traitements ponctuels
- Batchs stateless
- Environnements cloud/serverless
- Tests automatisés

Limitations importantes

Le mode resourceless implique :

- Pas de redémarrage possible (restart)
- Pas d’historique des exécutions
- Pas de reprise après crash
- Pas de monitoring via tables Batch


Comment activer le mode resourceless ?

Avec Spring Batch 6, il suffit de ne pas configurer de datasource Batch.


## Liens

https://github.com/spring-projects/spring-boot/issues/48233


