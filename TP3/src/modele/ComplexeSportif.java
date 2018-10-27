package modele;

import java.util.ArrayList;

import CentreSportif.IFT287Exception;

/* Les transactions demandées pour le tp2 ne sont pas gérées dans cette classe, 
 * Elle a toutefois été  créée pour une futur utilisation
 * */

public class ComplexeSportif {

	// Attributes

	private ArrayList<Ligue> listLigues;
	private ArrayList<Participant> listParticipants;
	private ArrayList<Resultat> listResultats;

	// Builders

	public ComplexeSportif() {
		super();
		this.listLigues = new ArrayList<Ligue>();
		this.listParticipants = new ArrayList<Participant>();
		this.listResultats = new ArrayList<Resultat>();
	}

	public ComplexeSportif(ArrayList<Ligue> listLigues, ArrayList<Participant> listParticipants) {
		super();
		this.listLigues = listLigues;
		this.listParticipants = listParticipants;
		this.listResultats = new ArrayList<Resultat>();
	}

	public ComplexeSportif(ArrayList<Ligue> listLigues, ArrayList<Participant> listParticipants,
			ArrayList<Resultat> listResultats) {
		super();
		this.listLigues = listLigues;
		this.listParticipants = listParticipants;
		this.listResultats = listResultats;
	}

	// Getters & Setters

	public ArrayList<Ligue> getListLigues() {
		return listLigues;
	}

	public void setListLigues(ArrayList<Ligue> listLigues) {
		this.listLigues = listLigues;
	}

	public ArrayList<Participant> getListParticipants() {
		return listParticipants;
	}

	public void setListParticipants(ArrayList<Participant> listParticipants) {
		this.listParticipants = listParticipants;
	}

	public ArrayList<Resultat> getListResultats() {
		return listResultats;
	}

	public void setListResultats(ArrayList<Resultat> listResultats) {
		this.listResultats = listResultats;
	}

	// Methods

	public void inscrireParticipant(String matricule, String prenom, String nom, String motDePasse)
			throws IFT287Exception {

		boolean trouve = false;
		for (Participant participant : listParticipants) {
			if (participant.getMatricule().equals(matricule)) {
				trouve = true;
			}
		}

		if (trouve == false) {
			Participant newParticipant = new Participant(matricule, prenom, nom, motDePasse);
			listParticipants.add(newParticipant);
		} else {
			throw new IFT287Exception("Le Participant selectionné existe déjà");
		}
	}

	public void supprimerParticipant(String matricule) throws IFT287Exception {

		boolean trouve = false;
		for (Participant participant : listParticipants) {
			if (participant.getMatricule().equals(matricule)) {
				if (participant.getEquipe() == null) {
					listParticipants.remove(participant);
					trouve = true;
				} else {
					throw new IFT287Exception("Le Participant est dans une equipe est donc ne peut pas être supprimé");
				}
			}
		}
		if (trouve == false) {
			throw new IFT287Exception("Le Participant selectionné est introuvable");
		}
	}

	public void ajouterLigue(String nomLigue, int nbJoueurMaxParEquipe) throws IFT287Exception {

		boolean trouve = false;
		for (Ligue ligue : listLigues) {
			if (ligue.getNomLigue().equals(nomLigue)) {
				trouve = true;
			}
		}

		if (trouve == false) {
			Ligue newLigue = new Ligue(nomLigue, nbJoueurMaxParEquipe);
			listLigues.add(newLigue);
		} else {
			throw new IFT287Exception("La Ligue selectionné existe déjà");
		}
	}

	public boolean RechercheLigueActive(String nomLigue) throws IFT287Exception {
		boolean ligueActive = false;
			for(Equipe equipe :  RechercheLigue(nomLigue).getListEquipes()) {
				for(Participant participant :  equipe.getListParticipants()) {
					if(participant.getStatut().equals("accepte")) {
						ligueActive = true;
					}
				}
			}
			return ligueActive;
	}

	public void supprimerLigue(String nomLigue) throws IFT287Exception {

		boolean trouve = false;
		
		if(RechercheLigueActive(nomLigue)) {
			throw new IFT287Exception("La Ligue selectionné a encore des joueurs actifs et donc ne peut être suprimé");
		}
		
		for (Ligue ligue : listLigues) {
			if (ligue.getNomLigue().equals(nomLigue)) {

				for (Resultat resultat : listResultats) {
					if (resultat.getEquipeA().getLigue().getNomLigue().equals(nomLigue)) {
						listResultats.remove(resultat);
					}
				}

				for (Equipe equipe : ligue.getListEquipes()) {
					ligue.getListEquipes().remove(equipe);
				}
				listLigues.remove(ligue);
				trouve = true;
			}
		}
		if (trouve == false) {
			throw new IFT287Exception("La Ligue selectionné est introuvable");
		}
	}

	public Ligue RechercheLigue(String nomLigue) throws IFT287Exception {
		Ligue ligueTrouve = null;
		for (Ligue ligue : listLigues) {
			if (ligue.getNomLigue().equals(nomLigue)) {
				ligueTrouve = ligue;
			}
		}
		return ligueTrouve;

	}

	public void ajouterEquipe(String nomLigue, String nomEquipe, String matriculeCap) throws IFT287Exception {

		boolean trouve = false;

		if (RechercheLigue(nomLigue).equals(null))
			throw new IFT287Exception("La Ligue selectionné est introuvable");

		for (Equipe equipe : RechercheLigue(nomLigue).getListEquipes()) {
			if (equipe.getNomEquipe().equals(nomEquipe)) {
				trouve = true;
			}
		}

		if (trouve == false) {
			Equipe newEquipe = new Equipe(nomLigue, nomEquipe, matriculeCap);
			RechercheLigue(nomLigue).getListEquipes().add(newEquipe);

		} else {
			throw new IFT287Exception("L'Equipe selectionné existe déjà");
		}
	}

	public Participant RechercheParticipant(String matricule) throws IFT287Exception {
		Participant participantTrouve = null;
		for (Participant participant : listParticipants) {
			if (participant.getMatricule().equals(matricule)) {
				participantTrouve = participant;
			}
		}
		return participantTrouve;

	}

	public Equipe RechercheEquipe(String nomEquipe) throws IFT287Exception {
		Equipe equipeTrouve = null;
		for (Ligue ligue : listLigues) {
			for (Equipe equipe : ligue.getListEquipes()) {
				if (equipe.getNomEquipe().equals(nomEquipe)) {
					equipeTrouve = equipe;
				}
			}
		}
		return equipeTrouve;

	}

	public void ajouterJoueur(String nomEquipe, String matricule) throws IFT287Exception {

		if (RechercheParticipant(matricule).equals(null))
			throw new IFT287Exception("Le Participant selectionné est introuvable");

		if (RechercheParticipant(matricule).getStatut().equals("en attente"))
			throw new IFT287Exception("Le Participant selectionné postule déjà pour une autre équipe");

		if (RechercheParticipant(matricule).getStatut().equals("accepte"))
			throw new IFT287Exception("Le Participant selectionné est déjà dans une autre équipe");

		if (RechercheEquipe(nomEquipe).equals(null))
			throw new IFT287Exception("L'Equipe selectionné est introuvable");

		RechercheParticipant(matricule).setStatut("en attente");
		RechercheParticipant(matricule).setEquipe(RechercheEquipe(nomEquipe));
		RechercheEquipe(nomEquipe).getListParticipants().add(RechercheParticipant(matricule));

	}

	public void accepterJoueur(String nomEquipe, String matricule) throws IFT287Exception {

		if (RechercheParticipant(matricule).equals(null))
			throw new IFT287Exception("Le Participant selectionné est introuvable");

		if (RechercheParticipant(matricule).getStatut().equals("accepte"))
			throw new IFT287Exception("Le Participant selectionné est déjà dans une équipe");

		if (RechercheEquipe(nomEquipe).equals(null))
			throw new IFT287Exception("L'Equipe selectionné est introuvable");

		if (RechercheEquipe(nomEquipe).getListParticipants()
				.size() > RechercheLigue(RechercheEquipe(nomEquipe).getLigue().getNomLigue()).getNbJoueurMaxParEquipe())
			throw new IFT287Exception("L'Equipe a déjà le nombre maximum de joueurs");

		RechercheParticipant(matricule).setStatut("accepte");
	}

	public void refuserJoueur(String nomEquipe, String matricule) throws IFT287Exception {

		if (RechercheParticipant(matricule).equals(null))
			throw new IFT287Exception("Le Participant selectionné est introuvable");

		if (RechercheParticipant(matricule).getStatut().equals("accepte"))
			throw new IFT287Exception("Le Participant selectionné est déjà dans une équipe");

		if (RechercheEquipe(nomEquipe).equals(null))
			throw new IFT287Exception("L'Equipe selectionné est introuvable");

		RechercheParticipant(matricule).setStatut("refuse");
		RechercheEquipe(nomEquipe).getListParticipants().remove(RechercheParticipant(matricule));
	}

	public void supprimerJoueur(String nomEquipe, String matricule) throws IFT287Exception {

		if (RechercheParticipant(matricule).equals(null))
			throw new IFT287Exception("Le Participant selectionné est introuvable");

		if (RechercheEquipe(nomEquipe).getCapitaine().equals(RechercheParticipant(matricule)))
			throw new IFT287Exception(
					"Capitaine selectionné, veuillez supprimer l'equipe si vous voulez supprimer le capitaine");

		if (RechercheEquipe(nomEquipe).equals(null))
			throw new IFT287Exception("L'Equipe selectionné est introuvable");

		RechercheParticipant(matricule).setStatut("supprime");
		RechercheEquipe(nomEquipe).getListParticipants().remove(RechercheParticipant(matricule));
	}

}
