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

	// Attributes

	@Id
	@GeneratedValue
	private long id_ligue;
	private String nomLigue;
	
	@OneToMany(mappedBy = "ligue", cascade = CascadeType.ALL)
	@OrderBy("nomEquipe")
	private List<Equipe> listEquipes;
	private int nbJoueurMaxParEquipe;

	// Builders

	public Ligue() {
	}

	public Ligue(String nomLigue, List<Equipe> listEquipes, int nbJoueurMaxParEquipe) {
		super();
		this.nomLigue = nomLigue;
		this.listEquipes = listEquipes;
		this.setNbJoueurMaxParEquipe(nbJoueurMaxParEquipe);
	}

	public Ligue(String nomLigue, int nbJoueurMaxParEquipe) {
		super();
		this.nomLigue = nomLigue;
		this.listEquipes = new LinkedList<Equipe>();
		this.setNbJoueurMaxParEquipe(nbJoueurMaxParEquipe);
	}

	// Getters & Setters

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

	/**
	 * Véririe si
	 * 
	 * @return
	 */
	public boolean isActive() {
		boolean testIsActive = true;
		if (this.getListEquipes().size() == 0) {
			testIsActive = false;
		}
		return testIsActive;
	}

	public boolean testNewEquipes(String nomLigue) {
		boolean testNewEquipe = true;
		for (Equipe equipe : this.listEquipes) {
			if (!equipe.getLigue().getNomLigue().equals(nomLigue)) {
				testNewEquipe = false;
			}
		}
		return testNewEquipe;
	}

}
