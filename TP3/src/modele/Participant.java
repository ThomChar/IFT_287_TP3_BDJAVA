package modele;

public class Participant {
	
	//Attributes

	private String matricule;
	private String prenom;
	private String nom;
	private String motDePasse;
	private String nomEquipe;
	private Equipe equipe;
	private String statut;
	
	//Builders
	
	public Participant(String matricule, String prenom, String nom, String motDePasse) {
		super();
		this.matricule = matricule;
		this.prenom = prenom;
		this.nom = nom;
		this.motDePasse = motDePasse;
		this.equipe = null;
		this.nomEquipe = null;
		this.statut = null;
	}
	
	public Participant(String matricule, String prenom, String nom, String motDePasse, String nomEquipe ) {
		super();
		this.matricule = matricule;
		this.prenom = prenom;
		this.nom = nom;
		this.motDePasse = motDePasse;
		this.nomEquipe = nomEquipe;
		this.equipe = null;
		this.statut = null;
	}
	
	// Getters & Setters

	public Participant() {
	}

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

	public String getNomEquipe() {
		return nomEquipe;
	}

	public void setNomEquipe(String nomEquipe) {
		this.nomEquipe = nomEquipe;
	}
	
	@Override
	public String toString() {
		return "\nParticipant [matricule=" + matricule + ", prenom=" + prenom + ", nom=" + nom + ", motDePasse="
				+ motDePasse + " statut=" + statut + "]";
	}

	public boolean isActive() {
		boolean testIsActive = true;
		if(this.getStatut() == null || this.getStatut().equals("SUPPRIME") || this.getStatut().equals("REFUSE")) {
			testIsActive = false;
		}
		return testIsActive;
	}
	
	public boolean equipeIsActive() {
		boolean testIsActive = true;
		if(this.getStatut() == null || this.getStatut().equals("SUPPRIME") || this.getStatut().equals("REFUSE")) {
			testIsActive = false;
		}
		return testIsActive;
	}

	
}
