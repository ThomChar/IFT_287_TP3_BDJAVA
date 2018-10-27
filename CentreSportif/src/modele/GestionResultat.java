package modele;

import java.sql.SQLException;

import CentreSportif.Connexion;
import CentreSportif.IFT287Exception;

public class GestionResultat {

	private TableResultats resultat;
	private Connexion cx;

	public GestionResultat(TableResultats resultat) throws IFT287Exception {
		this.cx = resultat.getConnexion();
		this.resultat = resultat;

	}

	/**
	 *  Inscrit un resultat enntre deux equipes
	 *  
	 *  @throws SQLException, IFT287Exception, Exception
	 */
	public void InscrireResulat(String nomEquipeA, String nomEquipeB, int scoreEquipeA, int scoreEquipeB)
			throws SQLException, IFT287Exception, Exception {
		try {
			// Verifier si resultat equipeA contre EquipeB existe
			Resultat tupleResultat = resultat.getResultat(nomEquipeA,nomEquipeB);
			//Si pas de match retour n'est autorisé
			Resultat tupleResultat2 = resultat.getResultat(nomEquipeB,nomEquipeA);
			if (tupleResultat != null)
				throw new IFT287Exception("Resultat deja existant: " + nomEquipeA + " contre " + nomEquipeB );
			if (tupleResultat2 != null)
				throw new IFT287Exception("Resultat deja existant: " + nomEquipeB + " contre " + nomEquipeA );

			// Creation du resultat
			resultat.ajouter(nomEquipeA, nomEquipeB, scoreEquipeA, scoreEquipeB);

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
	 *  @throws SQLException, IFT287Exception, Exception
	 */
	public void supprimerResultat(String nomEquipeA, String nomEquipeB) throws SQLException, IFT287Exception, Exception {
		try {
			
			// Verifier si resultat existe
			if (resultat.supprimer(nomEquipeA, nomEquipeB) == 0)
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
	 *  @throws SQLException, IFT287Exception, Exception
	 */
	public void modifierResultat(String nomEquipeA, String nomEquipeB, int scoreEquipeA, int scoreEquipeB) throws SQLException, IFT287Exception, Exception {
		try {
			
			// Verifier si resultat existe
			if (!resultat.existe(nomEquipeA, nomEquipeB))
				throw new IFT287Exception("Resultat entre " + nomEquipeA + " et " + nomEquipeB + " n'existe pas");
			
			resultat.modifier(nomEquipeA, nomEquipeB, scoreEquipeA, scoreEquipeB);
			
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
	 *  @throws SQLException, IFT287Exception, Exception
	 */
	public void affichageResultats() throws SQLException, IFT287Exception, Exception {
		try {
			resultat.afficher();
					
			// Commit
			cx.commit();
		} catch (Exception e) {
			cx.rollback();
			throw e;
		}
	}
	

}
