package modele;

import java.util.List;
import javax.persistence.TypedQuery;
import CentreSportif.Connexion;

public class Ligues {

	private TypedQuery<Ligue> stmtExiste;
	private TypedQuery<Ligue> stmtListToutesLigues;
	// private TypedQuery<Equipe> stmtListEquipesLigue;
	// private TypedQuery<Ligue> stmtUpdate;
	// private TypedQuery<Ligue> stmtDelete;
	private Connexion cx;

	/**
	 * Creation d'une instance.
	 */
	public Ligues(Connexion cx)  {
		this.cx = cx;
		stmtExiste = cx.getConnection().createQuery("select l from Ligue l where l.nomLigue = :nomLigue", Ligue.class);
		stmtListToutesLigues = cx.getConnection().createQuery("select l from Ligue l", Ligue.class);
		/*stmtListEquipesLigue = cx.getConnection()
				.createQuery("select e from Equipe e where e.ligue.nomLigue = :nomLigue", Equipe.class);*/
		/*
		 * stmtUpdate = cx.getConnection()
		 * .createQuery("UPDATE Ligue SET l.nbJoueurMaxParEquipe = :nbJoueurMaxParEquipe"
		 * , Ligue.class);
		 */
		// stmtDelete = cx.getConnection().createQuery("DELETE FROM Ligue",
		// Ligue.class);
	}

	/**
	 * Retourner la connexion associee.
	 */
	public Connexion getConnexion() {
		return cx;
	}

	/**
	 * Verifie si une ligue existe.
	 * 
	 * @throws SQLException
	 */
	public boolean existe(String nomLigue) {
		stmtExiste.setParameter("nomLigue", nomLigue);
		return !stmtExiste.getResultList().isEmpty();
	}

	/**
	 * Lecture d'une Ligue.
	 * 
	 * @throws SQLException
	 */
	public Ligue getLigue(String nomLigue) {
		stmtExiste.setParameter("nomLigue", nomLigue);
		List<Ligue> ligues = stmtExiste.getResultList();
		if (!ligues.isEmpty()) {
			return ligues.get(0);
		} else {
			return null;
		}
	}

	 /**
     * Ajout d'une nouvelle ligue dans la base de donnees.
     */
    public Ligue ajouter(Ligue ligue)
    {
        // Ajout de la ligue.
        cx.getConnection().persist(ligue);
        
        return ligue;
    }
    
    /**
     * Modifie le nombre de joueur max d'une ligue dans la base de donnees.
     */
    public Ligue modifierLigue(Ligue ligue)
    {
        // Ajout de la ligue.
        cx.getConnection().persist(ligue);
        
        return ligue;
    }
    
    /**
     * Suppression d'une ligue.
     */
    public boolean supprimer(Ligue ligue)
    {
        if(ligue != null)
        {
            cx.getConnection().remove(ligue);
            return true;
        }
        return false;
    }

    /*public List<Equipe> calculerListeEquipe(Ligue ligue)
    {
      
    	stmtListEquipesLigue.setParameter("ligue", ligue);
        return stmtListEquipesLigue.getResultList();
    }*/
    
    /**
     * Retourne l'ensemble des ligues de la base de données
     * @return
     */
    public List<Ligue> calculerListeLigues()
    {
        return stmtListToutesLigues.getResultList();
    }
    
	/**
	 * Modifier le nombre de Joueur max par equipe pour une ligue.
	 * 
	 * @throws IFT287Exception
	 */
	/*public void modifierNbJoueursMaxParEquipe(String nomLigue, int nbJoueurMaxParEquipe)
			throws SQLException, IFT287Exception {
		stmtUpdate.setInt(1, nbJoueurMaxParEquipe);
		stmtUpdate.setString(2, nomLigue);
		stmtUpdate.executeUpdate();
	}*/

	/**
	 * Modifier le contenu de la liste des equipe pour une ligue.
	 * 
	 * @throws IFT287Exception
	 */
	/*public void modifierListEquipes(String nomLigue, ArrayList<Equipe> listEquipes)
			throws SQLException, IFT287Exception {
		stmtUpdateListEquipes.setArray(1, (Array) listEquipes);
		stmtUpdateListEquipes.setString(2, nomLigue);
		stmtUpdateListEquipes.executeUpdate();
	}*/

	/**
	 * Suppression d'une ligue. regarder si ligue n'est pas active avant de
	 * supprimer
	 * 
	 * @throws SQLException
	 */
	/*public int supprimer(String nomLigue) throws SQLException {
		stmtDelete.setString(1, nomLigue);
		return stmtDelete.executeUpdate();
	}*/

	/**
	 * Afficher toutes les equipes d'une ligue.
	 * 
	 * @throws IFT287Exception
	 */
	/*public void afficher(String nomLigue) throws SQLException, IFT287Exception {

		stmtExiste.setParameter("nomLigue", nomLigue);
		
	}*/
}
