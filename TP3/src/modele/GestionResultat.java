package modele;

import java.util.List;

import CentreSportif.Connexion;
import CentreSportif.IFT287Exception;

public class GestionResultat {

	private Resultats resultats;
	private Equipes equipes;
	private Connexion cx;

	public GestionResultat(Resultats resultats, Equipes equipes) throws IFT287Exception {
		this.cx = resultats.getConnexion();
		if (equipes.getConnexion() == resultats.getConnexion()) {
		this.resultats = resultats;
		this.equipes = equipes;
		}

	}

	/**
	 *  Inscrit un resultat enntre deux equipes
	 *  
	 *  @throws IFT287Exception, Exception
	 */
	public void InscrireResulat(String nomEquipeA, String nomEquipeB, int scoreEquipeA, int scoreEquipeB)
			throws IFT287Exception, Exception {
		try {
			cx.demarreTransaction();
			
			// Verifier si resultat equipeA contre EquipeB existe
			Resultat tupleResultat = resultats.getResultat(nomEquipeA,nomEquipeB);
			//Si pas de match retour n'est autorisé
			Resultat tupleResultat2 = resultats.getResultat(nomEquipeB,nomEquipeA);
			if (tupleResultat != null)
				throw new IFT287Exception("Resultat deja existant: " + nomEquipeA + " contre " + nomEquipeB );
			if (tupleResultat2 != null)
				throw new IFT287Exception("Resultat deja existant: " + nomEquipeB + " contre " + nomEquipeA );

			//Recherche de l'equipe correspondant à l'equipe nomEquipeA
			Equipe EquipeA = equipes.getEquipe(nomEquipeA);
			//Recherche de l'equipe correspondant à l'equipe nomEquipeB
			Equipe EquipeB = equipes.getEquipe(nomEquipeB);
			
			Resultat tupleNewResultat = new Resultat(EquipeA, EquipeB, scoreEquipeA, scoreEquipeB);
			
			// Creation du resultat dans la BD
			resultats.creer(tupleNewResultat);

			// Commit
			cx.commit();
		} catch (Exception e) {
			cx.rollback();
			throw e;
		}
	}


	/**
	 * Supprime resultat. Le resultat doit exister
	 * 
	 *  @throws IFT287Exception, Exception
	 */
	public void supprimerResultat(String nomEquipeA, String nomEquipeB) throws IFT287Exception, Exception {
		try {
			cx.demarreTransaction();
			
			Resultat tupleNewResultat = resultats.getResultat(nomEquipeA, nomEquipeB);
			// Verifier si resultat existe
			if (resultats.supprimer(tupleNewResultat) == false)
				throw new IFT287Exception("Resultat entre " + nomEquipeA + " et " + nomEquipeB + " n'existe pas");

			// Commit
			cx.commit();
		} catch (Exception e) {
			cx.rollback();
			throw e;
		}
	}
	
	/**
	 * Modifier resultat. Le resultat doit exister
	 * 
	 *  @throws IFT287Exception, Exception
	 */
	public void modifierResultat(String nomEquipeA, String nomEquipeB, int scoreEquipeA, int scoreEquipeB) throws IFT287Exception, Exception {
		try {
			cx.demarreTransaction();
			
			// Verifier si resultat existe
			if (!resultats.existe(nomEquipeA, nomEquipeB))
				throw new IFT287Exception("Resultat entre " + nomEquipeA + " et " + nomEquipeB + " n'existe pas");
			
			//Recherche de l'equipe correspondant à l'equipe nomEquipeA
			Equipe equipeA = equipes.getEquipe(nomEquipeA);
			//Recherche de l'equipe correspondant à l'equipe nomEquipeB
			Equipe equipeB = equipes.getEquipe(nomEquipeB);
			
			Resultat tupleNewResultat = resultats.getResultat(nomEquipeA, nomEquipeB);
			//Modification effectué sur le contenu du tuple
			tupleNewResultat.setEquipeA(equipeA);
			tupleNewResultat.setEquipeB(equipeB);
			tupleNewResultat.setScoreEquipeA(scoreEquipeA);
			tupleNewResultat.setScoreEquipeB(scoreEquipeB);
			
			//MOdification effectue sur la BD
			resultats.modifierResultat(tupleNewResultat);
						
			// Commit
			cx.commit();
		} catch (Exception e) {
			cx.rollback();
			throw e;
		}
	}
	
	/**
	 * Affichage de l'ensemble des résultats de la table.
	 * 
	 *  @throws IFT287Exception, Exception
	 */
	public void affichageResultats() throws IFT287Exception, Exception {
		
		cx.demarreTransaction();

		List<Resultat> list = resultats.calculerListeResultats();

		for (Resultat r : list) {
			System.out.println(r.toString());
		}

		cx.commit();
		/*try {
			resultats.afficher();
					
			// Commit
			cx.commit();
		} catch (Exception e) {
			cx.rollback();
			throw e;
		}*/
	}
	
	/**
	 * Affichage de l'ensemble des résultats d'une equipe.
	 * 
	 *  @throws IFT287Exception, Exception
	 */
	public void affichageResultatsEquipe(String nomEquipe) throws IFT287Exception, Exception {
		
		cx.demarreTransaction();

		List<Resultat> list = resultats.calculerListeResultatsEquipe(nomEquipe);

		for (Resultat r : list) {
			System.out.println(r.toString());
		}

		cx.commit();
		/*try {
			resultats.afficher();
					
			// Commit
			cx.commit();
		} catch (Exception e) {
			cx.rollback();
			throw e;
		}*/
	}
	

}
