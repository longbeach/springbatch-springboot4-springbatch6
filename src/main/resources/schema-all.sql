DROP TABLE IF EXISTS recette;

CREATE TABLE recette  (
    recette_id  SERIAL PRIMARY KEY,
    nom VARCHAR(50),
    type_cuisine VARCHAR(30),
    difficulte VARCHAR(20),
    temps_preparation INTEGER,
    ingredients TEXT,
    instructions TEXT
);