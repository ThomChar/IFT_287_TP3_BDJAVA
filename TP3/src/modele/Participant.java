package modele;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Participant {
	
	@Id
    @GeneratedValue
    private long id_participant;
	
	//Attributes
	private String matricule;
	private String prenom;
	private String nom;
	private String motDePasse;
	private String statut;
	@ManyToOne(fetch = FetchType.LAZY)
	private Equipe equipe;
	
	/**
	 * constructeur par défaut
	 */
	public Participant() {
	}
	
	/**
	 * constructeur à 4 arguments
	 * @param matricule
	 * @param prenom
	 * @param nom
	 * @param motDePasse
	 */
	public Participant(String matricule, String prenom, String nom, String motDePasse) {
		super();
		this.matricule = matricule;
		this.prenom = prenom;
		this.nom = nom;
		this.motDePasse = motDePasse;
		this.equipe = null;
		this.statut = null;
	}
	
	/**
	 * constructeur à 5 éléments
	 * @param matricule
	 * @param prenom
	 * @param nom
	 * @param motDePasse
	 * @param equipe
	 */
	public Participant(String matricule, String prenom, String nom, String motDePasse, Equipe equipe) {
		super();
		this.matricule = matricule;
		this.prenom = prenom;
		this.nom = nom;
		this.motDePasse = motDePasse;
		this.equipe = equipe;
		this.statut = null;
	}
	
	/**
	 * Les getters et setters
	 */
	public String getMatricule() {
		return matricule;
	}
	
	public void setMatricule(String matricule) {
		this.matricule = matricule;
	}
	public String getPrenom() {
		return prenom;
	}
	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}
	public String getNom() {
		return nom;
	}
	public void setNom(String nom) {
		this.nom = nom;
	}
	public String getMotDePasse() {
		return motDePasse;
	}
	public void setMotDePasse(String motDePasse) {
		this.motDePasse = motDePasse;
	}
	
	public Equipe getEquipe() {
		return equipe;
	}

	public void setEquipe(Equipe equipe) {
		this.equipe = equipe;
	}
	
	public String getStatut() {
		return statut;
	}

	public void setStatut(String statut) {
		this.statut = statut;
	}
	
	/**
	 * Savoir si un joueur joue dans une équipe
	 * @return retourne vrai si le joueur joue dans une équipe, sinon faux
	 */
	public boolean isActive() {
		boolean testIsActive = true;
		if(this.getStatut() == null || this.getStatut().equals("SUPPRIME") || this.getStatut().equals("REFUSE")) {
			testIsActive = false;
		}
		return testIsActive;
	}
	
	@Override
	public String toString() {
		return "\nParticipant [matricule=" + matricule + ", prenom=" + prenom + ", nom=" + nom + ", motDePasse="
				+ motDePasse + " statut=" + statut + "]";
	}
}
