package fr.gouv.finances.dgfip.celinio_batch.persistance;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "recette")
public class Recette {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "recette_id")
	private Long id;

	@Column(name = "nom", length = 50, nullable = false)
	private String nom;

	@Column(name = "type_cuisine", length = 30)
	private String typeCuisine;

	@Column(name = "difficulte", length = 20)
	private String difficulte;

	@Column(name = "temps_preparation")
	private Integer tempsPreparation;

	@Column(name = "ingredients", columnDefinition = "TEXT")
	private String ingredients;

	@Column(name = "instructions", columnDefinition = "TEXT")
	private String instructions;

	// Constructeur par défaut requis par JPA
	public Recette() {
	}

	// Constructeur avec tous les champs sauf l'id
	public Recette(String nom, String typeCuisine, String difficulte, Integer tempsPreparation, String ingredients,
			String instructions) {
		this.nom = nom;
		this.typeCuisine = typeCuisine;
		this.difficulte = difficulte;
		this.tempsPreparation = tempsPreparation;
		this.ingredients = ingredients;
		this.instructions = instructions;
	}

	// Getters et Setters
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public String getTypeCuisine() {
		return typeCuisine;
	}

	public void setTypeCuisine(String typeCuisine) {
		this.typeCuisine = typeCuisine;
	}

	public String getDifficulte() {
		return difficulte;
	}

	public void setDifficulte(String difficulte) {
		this.difficulte = difficulte;
	}

	public Integer getTempsPreparation() {
		return tempsPreparation;
	}

	public void setTempsPreparation(Integer tempsPreparation) {
		this.tempsPreparation = tempsPreparation;
	}

	public String getIngredients() {
		return ingredients;
	}

	public void setIngredients(String ingredients) {
		this.ingredients = ingredients;
	}

	public String getInstructions() {
		return instructions;
	}

	public void setInstructions(String instructions) {
		this.instructions = instructions;
	}

	@Override
	public String toString() {
		return "Recette{" + "id=" + id + ", nom='" + nom + '\'' + ", typeCuisine='" + typeCuisine + '\''
				+ ", difficulte='" + difficulte + '\'' + ", tempsPreparation=" + tempsPreparation + ", ingredients='"
				+ ingredients + '\'' + ", instructions='" + instructions + '\'' + '}';
	}
}
