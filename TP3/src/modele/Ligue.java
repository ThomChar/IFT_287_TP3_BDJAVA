package modele;

import java.util.LinkedList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;

@Entity
public class Ligue {
	
	@Id
	@GeneratedValue
	private long id_ligue;
	
	// Attributes
	private String nomLigue;
	
	@OneToMany(mappedBy = "ligue", cascade = CascadeType.ALL)
	@OrderBy("nomEquipe")
	private List<Equipe> listEquipes;
	private int nbJoueurMaxParEquipe;

	/**
	 * constructeur par défaut
	 */
	public Ligue() {
	}
	
	/**
	 * constructeur de confort à 2 arguments
	 * @param nomLigue
	 * @param nbJoueurMaxParEquipe
	 */
	public Ligue(String nomLigue, int nbJoueurMaxParEquipe) {
		super();
		this.nomLigue = nomLigue;
		this.listEquipes = new LinkedList<Equipe>(); //new LinkedList<Equipe>()
		this.setNbJoueurMaxParEquipe(nbJoueurMaxParEquipe);
	}
	
	/**
	 * Constructeur de confort à 3 arguments
	 * @param nomLigue
	 * @param listEquipes
	 * @param nbJoueurMaxParEquipe
	 */
	public Ligue(String nomLigue, List<Equipe> listEquipes, int nbJoueurMaxParEquipe) {
		super();
		this.nomLigue = nomLigue;
		this.listEquipes = listEquipes;
		this.setNbJoueurMaxParEquipe(nbJoueurMaxParEquipe);
	}

	/**
	 * Les getters et setters
	 */
	public String getNomLigue() {
		return nomLigue;
	}

	public void setNomLigue(String nomLigue) {
		this.nomLigue = nomLigue;
	}

	public List<Equipe> getListEquipes() {
		return listEquipes;
	}

	public void setListEquipes(List<Equipe> listEquipes) {
		this.listEquipes = listEquipes;
	}

	public int getNbJoueurMaxParEquipe() {
		return nbJoueurMaxParEquipe;
	}

	public void setNbJoueurMaxParEquipe(int nbJoueurMaxParEquipe) {
		this.nbJoueurMaxParEquipe = nbJoueurMaxParEquipe;
	}

	/**
	 * Ajout une equipe à la liste d'equipe d'une ligue
	 * @param equipe
	 */
	public void ajouteEquipe(Equipe equipe) {
		listEquipes.add(equipe);
	}

	/**
	 * Supprime une equipe de la liste d'equipe d'une ligue
	 * @param equipe
	 */
	public void supprimerEquipe(Equipe equipe) {
		listEquipes.remove(equipe);
	}

	@Override
	public String toString() {
		return "Ligue [nomLigue=" + nomLigue + ", listEquipes=" + listEquipes + ", nbJoueurMaxParEquipe="
				+ nbJoueurMaxParEquipe + "]";
	}
}