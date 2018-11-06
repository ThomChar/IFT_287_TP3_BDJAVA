package modele;

import java.util.LinkedList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;

@Entity
public class Equipe {
	@Id
    @GeneratedValue
    private long id_equipe;
	
	//Attributes
	private String nomEquipe;
	
	@OneToMany(mappedBy = "equipe", cascade = CascadeType.ALL)
	@OrderBy("nom")
	private List<Participant> listParticipants;
	
	@OneToOne
	private Participant capitaine;
	
	@ManyToOne(fetch = FetchType.LAZY)
	private Ligue ligue;
	
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<Resultat> listResultats;
	
	/**
	 * Constructeur par défaut
	 */
	public Equipe() {
	}
	
	/**
	 * Constructeur de confort à 3 éléments
	 * @param ligue
	 * @param nomEquipe
	 * @param capitaine
	 */
	public Equipe(Ligue ligue, String nomEquipe, Participant capitaine) {
		this.nomEquipe = nomEquipe;
		this.listParticipants = new LinkedList<Participant>();
		this.capitaine = capitaine;
		this.ligue = ligue;
		this.listResultats = new LinkedList<Resultat>();
	}

	/**
	 * Les getteurs et setteurs
	 */
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
	 * Ajoute un participant à la liste de participants d'une equipe
	 * @param participant
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
	 * Ajoute un resultat à la liste de resultats d'une equipe
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
		String cap = "null";
		if(capitaine != null)
			cap = capitaine.getMatricule();
		
		return "\nEquipe [nomEquipe=" + nomEquipe + ", matriculeCap="+ cap + ", nomLigue=" + ligue.getNomLigue() + ",\nlistParticipants=" + listParticipants + ",\nlistResultats=" + listResultats + "]";
	}
	public String toStringSimpleEquipe() {
		String cap = "null";
		if(capitaine != null)
			cap = capitaine.getMatricule();
		
		return "\nEquipe [nomEquipe=" + nomEquipe + ", matriculeCap="+ cap + ", nomLigue=" + ligue.getNomLigue() + "]";
	}
}
