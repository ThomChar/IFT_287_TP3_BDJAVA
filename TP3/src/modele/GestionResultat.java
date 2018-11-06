package modele;

import java.util.List;

import CentreSportif.Connexion;
import CentreSportif.IFT287Exception;

public class GestionResultat {

	private Resultats resultats;
	private Equipes equipes;
	private Connexion cx;

	/**
	 * Creation d'une instance
	 * @param resultats
	 * @param equipes
	 * @throws IFT287Exception
	 */
	public GestionResultat(Resultats resultats, Equipes equipes) throws IFT287Exception {
		this.cx = resultats.getConnexion();
		if (equipes.getConnexion() != resultats.getConnexion())
    		throw new IFT287Exception("Les différents gestionnaires (equipes et résultats) n'utilisent pas la même connexion au serveur");

		this.resultats = resultats;
		this.equipes = equipes;
	}

	/**
	 * Inscrit un resultat enntre deux equipes
	 * @param nomEquipeA
	 * @param nomEquipeB
	 * @param scoreEquipeA
	 * @param scoreEquipeB
	 * @throws IFT287Exception
	 * @throws Exception
	 */
	public void InscrireResulat(String nomEquipeA, String nomEquipeB, int scoreEquipeA, int scoreEquipeB) throws IFT287Exception, Exception {
		try {
			cx.demarreTransaction();
			
			Resultat tupleResultat = resultats.getResultat(nomEquipeA,nomEquipeB);
			Resultat tupleResultat2 = resultats.getResultat(nomEquipeB,nomEquipeA);
			
			// Validations
			if (tupleResultat != null)
				throw new IFT287Exception("Resultat deja existant: " + nomEquipeA + " contre " + nomEquipeB );
			if (tupleResultat2 != null)
				throw new IFT287Exception("Resultat deja existant: " + nomEquipeB + " contre " + nomEquipeA );

			Equipe eqA = equipes.getEquipe(nomEquipeA);
			Equipe eqB = equipes.getEquipe(nomEquipeB);
			
			// ajout du résulat
			Resultat tupleNewResultat = new Resultat(eqA, eqB, scoreEquipeA, scoreEquipeB);
			resultats.creer(tupleNewResultat);
			eqA.ajouterResultat(tupleNewResultat);
			eqB.ajouterResultat(tupleNewResultat);

			cx.commit();
		} catch (Exception e) {
			cx.rollback();
			throw e;
		}
	}

	/**
	 * Modifier resultat. Le resultat doit exister
	 * @param nomEquipeA
	 * @param nomEquipeB
	 * @param scoreEquipeA
	 * @param scoreEquipeB
	 * @throws IFT287Exception
	 * @throws Exception
	 */
	public void modifierResultat(String nomEquipeA, String nomEquipeB, int scoreEquipeA, int scoreEquipeB) throws IFT287Exception, Exception {
		try {
			cx.demarreTransaction();
			
			// Vérification
			if (!resultats.existe(nomEquipeA, nomEquipeB))
				throw new IFT287Exception("Resultat entre " + nomEquipeA + " et " + nomEquipeB + " n'existe pas");
			
			Resultat r = resultats.getResultat(nomEquipeA, nomEquipeB);
			
			// modifications
			r.setScoreEquipeA(scoreEquipeA);
			r.setScoreEquipeB(scoreEquipeB);
		
			cx.commit();
		} catch (Exception e) {
			cx.rollback();
			throw e;
		}
	}
	
	/**
	 * Supprimer résultat
	 * @param nomEquipeA
	 * @param nomEquipeB
	 * @throws IFT287Exception
	 * @throws Exception
	 */
	public void supprimerResultat(String nomEquipeA, String nomEquipeB) throws IFT287Exception, Exception {
		try {
			cx.demarreTransaction();
			
			Resultat r = resultats.getResultat(nomEquipeA, nomEquipeB);
			
			// Vérification
			if (resultats.supprimer(r) == false)
				throw new IFT287Exception("Resultat entre " + nomEquipeA + " et " + nomEquipeB + " n'existe pas");
			
			Equipe eqA = equipes.getEquipe(nomEquipeA);
			Equipe eqB = equipes.getEquipe(nomEquipeB);
			
			// supprimer le résultat de la liste
			eqA.supprimerResultat(r);
			eqB.supprimerResultat(r);
			resultats.supprimer(r);

			cx.commit();
		} catch (Exception e) {
			cx.rollback();
			throw e;
		}
	}
	
	/**
	 * Affichage de l'ensemble des résultats
	 * @throws IFT287Exception
	 * @throws Exception
	 */
	public void affichageResultats() throws IFT287Exception, Exception {
		List<Resultat> list = resultats.calculerListeResultats();

		for (Resultat r : list) {
			System.out.println(r.toString());
		}
	}
	
	/**
	 * Affichage de l'ensemble des résultats d'une equipe.
	 * @param nomEquipe
	 * @throws IFT287Exception
	 * @throws Exception
	 */
	public void affichageResultatsEquipe(String nomEquipe) throws IFT287Exception, Exception {
		List<Resultat> list = resultats.calculerListeResultatsEquipe(nomEquipe);

		for (Resultat r : list) {
			System.out.println(r.toString());
		}
	}
}