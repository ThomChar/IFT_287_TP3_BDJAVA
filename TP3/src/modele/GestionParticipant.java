package modele;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import CentreSportif.IFT287Exception;
import CentreSportif.Connexion;

public class GestionParticipant {

	private Participants participants;
	private Equipes equipes;
	private Ligues ligues;
	private Connexion cx;

	/**
	 * Creation d'une instance
	 * 
	 */
	public GestionParticipant(Participants participants, Equipes equipes, Ligues ligues) throws IFT287Exception {
		this.cx = participants.getConnexion();
		
		if (participants.getConnexion() != equipes.getConnexion() && participants.getConnexion() != ligues.getConnexion())
			throw new IFT287Exception(
					"Les instances de participant, ligue et de equipe n'utilisent pas la m�me connexion au serveur");

		this.participants = participants;
		this.equipes = equipes;
		this.ligues = ligues;
	}

	/**
	 * Ajout d'un nouveau particpant dans la base de donn�es.
	 * S'il existe d�j�, une exception est lev�e.
	 * 
	 *  @throws IFT287Exception, Exception
	 */
	public void ajouter(String matricule, String prenom, String nom, String motDePasse)
			throws IFT287Exception, Exception {
		try {
			
			// V�rifie si le participant existe d�j�
			if (participants.existe(matricule))
				throw new IFT287Exception("Particpant existe d�j�: " + matricule);
			
			// Ajout du participant dans la table des participants
			Participant p = new Participant(matricule, prenom, nom, motDePasse);
			participants.creer(p);
			
			cx.commit();
		} catch (Exception e) {
			cx.rollback();
			throw e;
		}
	}

	/**
	 * Modifier nom et prenom d'un participant dans la base de donn�es.
	 * 
	 *  @throws SQLException, IFT287Exception, Exception
	 */
	public void modifierNomPrenom(String matricule, String prenom, String nom)
			throws IFT287Exception, Exception {
		try {
			
			// V�rifie si le participant existe bien
			if (!participants.existe(matricule))
				throw new IFT287Exception("Le particpant n'existe pas pour le matricule�: " + matricule);
		
			// modifications
			Participant p = participants.getParticipant(matricule);
			p.setNom(nom);
			p.setPrenom(prenom);

			cx.commit();
		} catch (Exception e) {
			cx.rollback();
			throw e;
		}
	}

	/**
	 * Modifier mot de passe d'un participant dans la base de donn�es.
	 * 
	 *  @throws SQLException, IFT287Exception, Exception
	 */
	public void modifierMotDePasse(String matricule, String motDePasse, String secondMotDePasse)
			throws IFT287Exception, Exception {
		
		try {
			// V�rifie si le participant existe bien
			if (!participants.existe(matricule))
				throw new IFT287Exception("Le particpant n'existe pas pour le matricule�: " + matricule);
			if (!motDePasse.equals(secondMotDePasse))
				throw new IFT287Exception("Le participant n'a pas �t� modifier. Les deux mots de passes ne sont pas identiques !");
			
			// modifications
			Participant p = participants.getParticipant(matricule);
			p.setMotDePasse(motDePasse);

			cx.commit();
		} catch (Exception e) {
			cx.rollback();
			throw e;
		}
	}

	/**
	 * Ajouter un participant dans une equipe du point de vu participant. S'il
	 * n'existe pas�, une exception est lev�e.
	 * 
	 *  @throws SQLException, IFT287Exception, Exception
	 */
	public void postulerAUneEquipe(String nomEquipe, String matricule) throws IFT287Exception, Exception {
		try {
			// V�rifie si le participant et l'�quipe existent
			if (!participants.existe(matricule))
				throw new IFT287Exception("Participant "+matricule+" n'existe pas");
			if (!equipes.existe(nomEquipe))
				throw new IFT287Exception("L'equipe selectionn�e " + nomEquipe + " est introuvable");
			
			Participant p = participants.getParticipant(matricule);
			Equipe e = equipes.getEquipe(nomEquipe);
			
			// v�rifier que le joueur n'est pas d�j� dans une �quipe
			if (p.getStatut().equals("EN ATTENTE")
					&& p.getEquipe() != null
					&& !p.getEquipe().getNomEquipe().equals(nomEquipe))
				throw new IFT287Exception("Le Participant selectionn� postule d�j� pour une autre �quipe : " + p.getEquipe().getNomEquipe());
			if (p.getStatut().equals("ACCEPTE")
					&& !p.getEquipe().getNomEquipe().equals(nomEquipe))
				throw new IFT287Exception("Le Participant selectionn� est d�j� dans une autre �quipe : " + p.getEquipe().getNomEquipe());
			if (p.getStatut().equals("ACCEPTE")
					&& p.getEquipe().getNomEquipe().equals(nomEquipe))
				throw new IFT287Exception("Le Participant selectionn� est d�j� dans cette equipe");
			if (participants.getParticipant(matricule).getStatut().equals("EN ATTENTE")
					&& p.getEquipe().getNomEquipe().equals(nomEquipe))
				throw new IFT287Exception("Le Participant postule d�j� pour cette equipe");
			
			
			p.setStatut("EN ATTENTE");
			p.setEquipe(e);

			// Commit
			cx.commit();
		} catch (Exception e) {
			cx.rollback();
			throw e;
		}
	}

	/**
	 * Accepter un participant dans une equipe du point de vu participant. S'il
	 * n'existe pas�, une exception est lev�e.
	 * 
	 *  @throws SQLException, IFT287Exception, Exception
	 */
	public void accepteParEquipe(String nomEquipe, String matricule) throws IFT287Exception, Exception {
		try {
			// V�rifie si le participant et l'�quipe existent
			if (!participants.existe(matricule))
				throw new IFT287Exception("Participant "+matricule+" n'existe pas");
			if (!equipes.existe(nomEquipe))
				throw new IFT287Exception("L'equipe selectionn�e " + nomEquipe + " est introuvable");
			
			Participant p = participants.getParticipant(matricule);
			Equipe e = equipes.getEquipe(nomEquipe);
			Ligue l = ligues.getLigue(e.getLigue().getNomLigue());
			
			// v�rification du nombre de joueurs max de l'�quipe
			if(e.getListParticipants().size() >= l.getNbJoueurMaxParEquipe())
				throw new IFT287Exception("Impossible d'ajouter un nouveau jour dans l'�quipe : " + nomEquipe + ", puisque nombre de joueurs max d�pass�.");
			
			// v�rifier que le joueur n'est pas d�j� dans une �quipe
			if (p.getStatut().equals("EN ATTENTE")
					&& !p.getEquipe().getNomEquipe().equals(nomEquipe))
				throw new IFT287Exception("Le Participant selectionn� postule d�j� pour une autre �quipe : " + p.getEquipe().getNomEquipe());
			if (p.getStatut().equals("ACCEPTE")
					&& !p.getEquipe().getNomEquipe().equals(nomEquipe))
				throw new IFT287Exception("Le Participant selectionn� est d�j� dans une autre �quipe : "+ p.getEquipe().getNomEquipe());
			if (p.getStatut().equals("ACCEPTE")
					&& p.getEquipe().getNomEquipe().equals(nomEquipe))
				throw new IFT287Exception("Le Participant selectionn� est d�j� dans votre equipe");
			
			p.setStatut("ACCEPTE");
			e.ajouterJoueur(p);

			// Commit
			cx.commit();
		} catch (Exception e) {
			cx.rollback();
			throw e;
		}
	}

	/**
	 * Refuse un participant dans une equipe du point de vu participant. S'il
	 * n'existe pas�, une exception est lev�e.
	 */
	public void refuseParEquipe(String nomEquipe, String matricule) throws IFT287Exception, Exception {
		try {
			// V�rifie si le participant existe
			if (!participants.existe(matricule))
				throw new IFT287Exception("Particpant n'existe pas�: " + matricule);
			if (!equipes.existe(nomEquipe))
				throw new IFT287Exception("L'equipe selectionn�e " + nomEquipe + " est introuvable");
			
			Participant p = participants.getParticipant(matricule);
			
			if (p.getStatut().equals("EN ATTENTE")
					&& !p.getEquipe().getNomEquipe().equals(nomEquipe))
				throw new IFT287Exception("Le Participant selectionn� est d�j� dans une autre �quipe : "+ p.getEquipe().getNomEquipe());
			if (p.getStatut().equals("REFUSE")
					&& p.getEquipe().getNomEquipe().equals(nomEquipe))
				throw new IFT287Exception("Le Participant selectionn� a deja refuse pour cette equipe");

			p.setStatut("REFUSE");

			// Commit
			cx.commit();
		} catch (Exception e) {
			cx.rollback();
			throw e;
		}
	}

	/**
	 * Supprimer un participant dans une equipe du point de vu participant. S'il
	 * n'existe pas�, une exception est lev�e.
	 * 
	 *  @throws SQLException, IFT287Exception, Exception
	 */
	public void supprimeParEquipe(String nomEquipe, String matricule) throws IFT287Exception, Exception {
		try {
			// V�rifie si le participant existe
			if (!participants.existe(matricule))
				throw new IFT287Exception("Participant n'existe pas�: " + matricule);
			if (!equipes.existe(nomEquipe))
				throw new IFT287Exception("L'equipe selectionn�e " + nomEquipe + " est introuvable");
			
			Participant p = participants.getParticipant(matricule);
			Equipe e = equipes.getEquipe(nomEquipe);
			
			if (p.getStatut().equals("EN ATTENTE")
					&& !p.getEquipe().getNomEquipe().equals(nomEquipe))
				throw new IFT287Exception("Le Participant selectionn� postule d�j� pour une autre �quipe : "
						+ p.getEquipe().getNomEquipe());
			if (p.getStatut().equals("EN ATTENTE")
					&& p.getEquipe().getNomEquipe().equals(nomEquipe))
				throw new IFT287Exception("Le Participant selectionn� postule pour cette equipe, il ne peut pas �tre supprim� mais peut �tre refus�");
			if (p.getStatut().equals("ACCEPTE")
					&& !p.getEquipe().getNomEquipe().equals(nomEquipe))
				throw new IFT287Exception("Le Participant selectionn� est dans une autre equipe");
			
			// on admet qu'une �quipe peut �tre provisoirement sans capitaine
			if(e.getCapitaine().getMatricule().equals(matricule)) {
				e.setCapitaine(null);
			}
			
			p.setStatut("SUPPRIME");
			e.supprimerJoueur(p);

			cx.commit();
		} catch (Exception e) {
			cx.rollback();
			throw e;
		}
	}

	/**
	 * Supprime Participant de la base de donn�e.
	 * 
	 *  @throws SQLException, IFT287Exception, Exception
	 */
	public void supprime(String matricule) throws IFT287Exception, Exception {
		try {
			
			// Validation
			Participant p = participants.getParticipant(matricule);
			if (p == null)
				throw new IFT287Exception("Particpant inexistant: " + matricule);
			if (p.isActive())
				throw new IFT287Exception("Particpant " + matricule + " est dans equipe "
						+ p.getEquipe().getNomEquipe() + "et ne peut pas �tre supprimer");

			// Suppression du participant.
			boolean operation = participants.supprimer(p);
			if (operation == false)
				throw new IFT287Exception("Particpant " + matricule + " inexistant");

			cx.commit();
		} catch (Exception e) {
			cx.rollback();
			throw e;
		}
	}

	/**
	 * Lecture des participants d'une �quipe
	 */
	public List<Participant> lectureParticipants(String nomEquipe)
			throws IFT287Exception, Exception {
		// Validation
		Equipe tupleEquipe = equipes.getEquipe(nomEquipe);
		if (tupleEquipe == null)
			throw new IFT287Exception("Equipe inexistant: " + nomEquipe);
		if (!tupleEquipe.isActive())
			throw new IFT287Exception("Equipe " + nomEquipe + "a encore des participants actifs");
		
		List<Participant> listeParticipant = participants.lectureParticipants(nomEquipe);
		
		// Commit
		cx.commit();
		return listeParticipant;

	}

	/**
	 * Lecture des participants d'une �quipe
	 * 
	 *  @throws SQLException, IFT287Exception, Exception
	 */
	public void affichageParticipants(String nomEquipe) throws IFT287Exception, Exception {
		// Validation
		Equipe tupleEquipe = equipes.getEquipe(nomEquipe);
		if (tupleEquipe == null)
			throw new IFT287Exception("Equipe inexistant: " + nomEquipe);
		if (!tupleEquipe.isActive())
			throw new IFT287Exception("Equipe " + nomEquipe + "a encore des participants actifs");
		
		List<Participant> listeParticipant = participants.lectureParticipants(nomEquipe);

		for(Participant p : listeParticipant)
        {
            System.out.println(p.toString());
        }
		
		cx.commit();
	}

	/**
	 * Affichage de l'ensemble des participants de la table.
	 * 
	 *  @throws SQLException, IFT287Exception, Exception
	 */
	public void affichageParticipants() throws IFT287Exception, Exception {
			List<Participant> listeParticipant = participants.lectureParticipants();
			
			for(Participant p : listeParticipant)
	        {
	            System.out.println(p.toString());
	        }
			
			cx.commit();
	}

}
