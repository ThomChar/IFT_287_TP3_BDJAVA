package modele;

import java.util.LinkedList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;

@Entity
public class Equipe {
	
	//Attributes
	
	@Id
    @GeneratedValue
	private String nomEquipe;
	
	@OneToMany(mappedBy = "equipe", cascade = CascadeType.ALL)
	@OrderBy("nom")
	private List<Participant> listParticipants;
	
	@OneToOne								// Demander si n�cessaire pour un seul objet li�
	private Participant capitaine;
	
	@ManyToOne(fetch = FetchType.LAZY)
	private Ligue ligue;
	
	@ManyToMany								// Demander si c'est la bonne expression
	private List<Resultat> listResultats;
	
	//Builders
	
	public Equipe() {
	}
	
	public Equipe(Ligue ligue, String nomEquipe, Participant capitaine) {
		super();
		this.nomEquipe = nomEquipe;
		this.listParticipants = new LinkedList<Participant>();
		this.capitaine = capitaine;
		this.ligue = ligue;
		this.listResultats = new LinkedList<Resultat>();
	}

	//Getters & Setters

	public String getNomEquipe() {
		return nomEquipe;
	}

	public void setNomEquipe(String nomEquipe) {
		this.nomEquipe = nomEquipe;
	}

	public List<Participant> getListParticipants() {
		return listParticipants;
	}

	public void setListParticipants(List<Participant> listParticipants) {
		this.listParticipants = listParticipants;
	}

	public Participant getCapitaine() {
		return capitaine;
	}

	public void setCapitaine(Participant capitaine) {
		this.capitaine = capitaine;
	}

	public Ligue getLigue() {
		return ligue;
	}

	public void setLigue(Ligue ligue) {
		this.ligue = ligue;
	}

	public List<Resultat> getListResultats() {
		return listResultats;
	}

	public void setListResultats(List<Resultat> listResultat) {
		this.listResultats = listResultat;
	}
	
	/**
	 * Ajoute un participant � la liste de participants d'une equipe
	 * @param equipe
	 */
	public void ajouterJoueur(Participant participant) {
		listParticipants.add(participant);
	}
	
	/**
	 * Supprime un participant de la liste de participants d'une equipe
	 * @param equipe
	 */
	public void supprimerJoueur(Participant participant) {
		listParticipants.remove(participant);
	}
	
	/**
	 * Ajoute un resultat � la liste de resultats d'une equipe
	 * @param equipe
	 */
	public void ajouterResultat(Resultat resultat) {
		listResultats.add(resultat);
	}
	
	/**
	 * Supprime un resultat de la liste de resultats d'une equipe
	 * @param equipe
	 */
	public void supprimerResultat(Resultat resultat) {
		listResultats.remove(resultat);
	}
	
	public boolean isActive() {
		boolean testIsActive = true;
		if(this.getListParticipants().size() == 0) {
			testIsActive = false;
		}
		return testIsActive;
	}

	@Override
	public String toString() {
		return "\nEquipe [nomEquipe=" + nomEquipe + ", matriculeCap="+ capitaine.getMatricule() + ", nomLigue=" + ligue.getNomLigue() + ",\nlistParticipants=" + listParticipants + ",\nlistResultats=" + listResultats + "]";
	}
	public String toStringSimpleEquipe() {
		return "\nEquipe [nomEquipe=" + nomEquipe + ", matriculeCap="+ capitaine.getMatricule() + ", nomLigue=" + ligue.getNomLigue() + "]";
	}

}
