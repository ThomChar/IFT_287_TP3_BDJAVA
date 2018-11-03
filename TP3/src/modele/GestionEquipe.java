package modele;

import java.util.List;
import CentreSportif.Connexion;
import CentreSportif.IFT287Exception;

public class GestionEquipe {

	private Equipes equipes;
	private Ligues ligues;
	private Participants participants;
	private Resultats resultats;
	private Connexion cx;

	/**
	 * Creation d'une instance
	 */
	public GestionEquipe(Equipes equipes, Participants participants, Ligues ligues, Resultats resultats)
			throws IFT287Exception {
		this.cx = equipes.getConnexion();
		if (equipes.getConnexion() == ligues.getConnexion() && participants.getConnexion() == equipes.getConnexion()
				&& equipes.getConnexion() == resultats.getConnexion()
				&& ligues.getConnexion() == resultats.getConnexion()) {
			this.equipes = equipes;
			this.participants = participants;
			this.ligues = ligues;
			this.resultats = resultats;
		} else {
			throw new IFT287Exception(
					"Les instances de participant et de resultat n'utilisent pas la même connexion au serveur");
		}
	}

	/**
	 * Ajout d'une nouvelle equipe dans la base de données. Si elle existe déjà ,
	 * une exception est levée.
	 * 
	 * @throws IFT287Exception,
	 *             Exception
	 */
	public void ajouter(String nomEquipe, String matriculeCap, String nomLigue) throws IFT287Exception, Exception {

		try {
			cx.demarreTransaction();
			
			// Vérifie si l equipe existe déjà
			if (equipes.testDejaCapitaine(matriculeCap))
				throw new IFT287Exception("Ce participant est deja capitaine : ");
			if (!ligues.existe(nomLigue))
				throw new IFT287Exception("Ligue " + nomLigue + " n'existe pas : ");
			if (!participants.existe(matriculeCap))
				throw new IFT287Exception("Ligue " + nomLigue + " n'existe pas : ");

			Participant capitaine = participants.getParticipant(matriculeCap);
			Ligue ligue = ligues.getLigue(nomLigue);
			Equipe tupleEquipe = new Equipe(ligue, nomEquipe, capitaine);

			// Ajout de l equipe dans la table des equipes
			equipes.creer(tupleEquipe);
			// Commit
			cx.commit();
		} catch (Exception e) {
			cx.rollback();
			throw e;
		}
	}

	/**
	 * Supprime Equipe de la base de données.
	 * 
	 * @throws SQLException,
	 *             IFT287Exception, Exception
	 */
	public void supprime(String nomEquipe) throws IFT287Exception, Exception {
		try {
			cx.demarreTransaction();
			
			// Validation
			Equipe tupleEquipe = equipes.getEquipe(nomEquipe);
			if (tupleEquipe == null)
				throw new IFT287Exception("Equipe inexistant: " + nomEquipe);
			if (!tupleEquipe.isActive())
				throw new IFT287Exception("Equipe " + nomEquipe + "a encore des participants actifs");

			// Suppression de l'equipe.
			boolean testExiste = equipes.supprimer(tupleEquipe);
			if (testExiste == false)
				throw new IFT287Exception("Equipe " + nomEquipe + " inexistante");

			// Commit
			cx.commit();
		} catch (Exception e) {
			cx.rollback();
			throw e;
		}
	}

	/**
	 * Change le capitaine de l'equipe.
	 * 
	 * @throws IFT287Exception,
	 *             Exception
	 */
	public void changerCapitaine(String nomEquipe, String matriculeCap) throws IFT287Exception, Exception {
		try {
			cx.demarreTransaction();
			
			// Vérifie si l equipe existe déjà
			if (!equipes.existe(nomEquipe))
				throw new IFT287Exception("Equipe " + nomEquipe + " n'existe pas : ");
			if (!participants.existe(matriculeCap))
				throw new IFT287Exception("Participant " + matriculeCap + " n'existe pas : ");
			if (!(participants.getParticipant(matriculeCap).getEquipe().getNomEquipe().equals(nomEquipe)
					&& participants.getParticipant(matriculeCap).getStatut().equals("ACCEPTE")))
				throw new IFT287Exception("Ce Particpant " + matriculeCap + " ne peut pas devenir captaine de "
						+ nomEquipe + " car il n'est pas dans l'equipe");
			if ((participants.getParticipant(matriculeCap).getEquipe().getNomEquipe().equals(nomEquipe)
					&& participants.getParticipant(matriculeCap).getStatut().equals("ACCEPTE")))
				throw new IFT287Exception("Ce Particpant " + matriculeCap + " ne peut pas devenir captaine de "
						+ nomEquipe + " car il n'est pas dans l'equipe");

			// Recherche equipe correspondante à nomEquipe
			Equipe tupleEquipe = equipes.getEquipe(nomEquipe);
			// Recherche particpant correspondant à matriculeCap
			Participant capitaine = participants.getParticipant(matriculeCap);
			// Modification du capitaine sur le tuple temporaire
			tupleEquipe.setCapitaine(capitaine);
			// Modification du tuple dans la BD
			equipes.modifierEquipe(tupleEquipe);

			// Commit
			cx.commit();
		} catch (Exception e) {
			cx.rollback();
			throw e;
		}
	}

	/**
	 * Affichage d'une equipe, de ses participants et de ses resultats
	 * 
	 * @throws IFT287Exception,
	 *             Exception
	 */
	public void affichageEquipe(String nomEquipe) throws IFT287Exception, Exception {
		try {
			cx.demarreTransaction();
			
			// Validation
			Equipe tupleEquipe = equipes.getEquipe(nomEquipe);
			if (tupleEquipe == null)
				throw new IFT287Exception("Equipe inexistante: " + nomEquipe);

			// Normalement il n y a plus besoins de rechercher les listes puisqu'elles sont
			// directement dans les equipes en OO

			// tupleEquipe.setListParticipants(participants.lectureParticipants(nomEquipe));
			// tupleEquipe.setListResultats(resultats.lectureResultats(nomEquipe));

			System.out.println(tupleEquipe.toString());

			// Commit
			cx.commit();
		} catch (Exception e) {
			cx.rollback();
			throw e;
		}
	}

	/**
	 * Lecture des equipes d'une ligue
	 * 
	 * @throws IFT287Exception,
	 *             Exception
	 */
	public List<Equipe> lectureEquipesLigue(String nomLigue) throws IFT287Exception, Exception {
		try {
			cx.demarreTransaction();
			
			// Validation
			Ligue tupleLigue = ligues.getLigue(nomLigue);
			if (tupleLigue == null)
				throw new IFT287Exception("Ligue inexistant: " + nomLigue);

			List<Equipe> listEquipes = equipes.calculerListeEquipesLigue(nomLigue);

			// Commit
			cx.commit();
			return listEquipes;
		} catch (Exception e) {
			cx.rollback();
			throw e;
		}
	}

	/**
	 * Affichage de l'ensemble des equipes de la table.
	 * 
	 * @throws IFT287Exception,
	 *             Exception
	 */
	public void affichageEquipes() throws IFT287Exception, Exception {
		/*
		 * try {
		 * 
		 * System.out.println("Equipe ["); for (Equipe eq :
		 * equipes.calculerListeEquipes()) { System.out.println("nomEquipe=" +
		 * eq.getNomEquipe() + ", matriculeCap=" + eq.getCapitaine().getMatricule() +
		 * ", nomLigue=" + eq.getLigue().getNomLigue()); } System.out.println("]");
		 * 
		 * // Commit cx.commit(); } catch (Exception e) { cx.rollback(); throw e; }
		 */

		cx.demarreTransaction();

		List<Equipe> list = equipes.calculerListeEquipes();

		for (Equipe eq : list) {
			System.out.println(eq.toString());
		}

		cx.commit();

	}

	/**
	 * Affichage de l'ensemble des equipes d'une ligue ainsi que le nombre de matchs
	 * gagnés, perdus et nulls.
	 * 
	 * @throws IFT287Exception,
	 *             Exception
	 */
	public void afficherEquipesLigue(String nomLigue) throws IFT287Exception, Exception {
		try {
			cx.demarreTransaction();
			
			// Validation
			Ligue tupleLigue = ligues.getLigue(nomLigue);
			if (tupleLigue == null)
				throw new IFT287Exception("Ligue inexistante: " + nomLigue);

			// Affichage
			System.out.println("\nLigue " + nomLigue + "(nombre max de joueurs="
					+ ligues.getLigue(nomLigue).getNbJoueurMaxParEquipe() + ") :");
			for (Equipe eq : equipes.calculerListeEquipesLigue(nomLigue)) {
				System.out
						.println("nomEquipe=" + eq.getNomEquipe() + ", matriculeCap=" + eq.getCapitaine().getMatricule()
								+ ", nombreDeMatchsGagnés=" + resultats.ObtenirNbMGagne(eq.getNomEquipe())
								+ ", nombreDeMatchsPerdus=" + resultats.ObtenirNbMPerdu(eq.getNomEquipe())
								+ ", nombreDeMatchsNulls=" + resultats.ObtenirNbMNul(eq.getNomEquipe()));
			}
			cx.commit();
		} catch (Exception e) {
			cx.rollback();
			throw e;
		}
	}

}
