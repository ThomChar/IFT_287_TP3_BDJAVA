package modele;

import java.util.List;
import javax.persistence.TypedQuery;
import CentreSportif.Connexion;

public class Resultats {

	private TypedQuery<Resultat> stmtExiste;
	private TypedQuery<Integer> stmtNbMGagne;
	private TypedQuery<Integer> stmtNbMPerdu;
	private TypedQuery<Integer> stmtNbMNul;
	private TypedQuery<Resultat> stmtListTousResultats;
	private TypedQuery<Resultat> stmtListTousResultatsEquipe;

	// private TypedQuery<Resultat> stmtUpdate;
	// private TypedQuery<Resultat> stmtDelete;

	private Connexion cx;

	/**
	 * Creation d'une instance.
	 */
	public Resultats(Connexion cx) {
		this.cx = cx;
		stmtExiste = cx.getConnection().createQuery(
				"select r from Resultat r where equipeA.nomEquipe = :nomEquipeA and equipeB.nomEquipe = :nomEquipeB",
				Resultat.class);
		/*
		 * stmtUpdate = cx.getConnection().getConnection(
		 * "update Resultat set scoreEquipeA = ?, scoreEquipeB = ? where nomEquipeA = ? and nomEquipeA = ?"
		 * ,Resulat.class); stmtDelete = cx.getConnection()
		 * .getConnection("delete from Resultat where (nomEquipeA = ? and nomEquipeB = ?) or (nomEquipeB = ? and nomEquipeA = ?)"
		 * ,Resulat.class);
		 */
		stmtListTousResultats = cx.getConnection().createQuery("select r from Resultat r", Resultat.class);
		stmtNbMGagne = cx.getConnection().createQuery(
				"select count(*) AS nb from Resultat r where (equipeA.nomEquipe = :nomEquipeA and scoreEquipeA > scoreEquipeB) or (equipeB.nomEquipe = :nomEquipeB and scoreEquipeA < scoreEquipeB)",
				Integer.class);
		stmtNbMPerdu = cx.getConnection().createQuery(
				"select count(*) AS nb from Resultat r where (equipeA.nomEquipe = :nomEquipeA and scoreEquipeA < scoreEquipeB) or (equipeB.nomEquipe = :nomEquipeB and scoreEquipeA > scoreEquipeB)",
				Integer.class);
		stmtNbMNul = cx.getConnection().createQuery(
				"select count(*) AS nb from Resultat r where (equipeA.nomEquipe = :nomEquipeA or equipeB.nomEquipe = :nomEquipeB) and scoreEquipeA = scoreEquipeB",
				Integer.class);
		stmtListTousResultatsEquipe = cx.getConnection().createQuery(
				"select r from Resultat r where equipeA.nomEquipe = :nomEquipeA or equipeB.nomEquipe = :nomEquipeB",
				Resultat.class);

	}

	/**
	 * Retourner la connexion associée.
	 */
	public Connexion getConnexion() {
		return cx;
	}

	/**
	 * Vérifie si un resultat existe.
	 * 
	 */
	public boolean existe(String nomEquipeA, String nomEquipeB) {
		stmtExiste.setParameter("nomEquipeA", nomEquipeA);
		stmtExiste.setParameter("nomEquipeB", nomEquipeB);
		return !stmtExiste.getResultList().isEmpty();
	}

	/**
	 * Lecture d'un resultat.
	 * 
	 */
	public Resultat getResultat(String nomEquipeA, String nomEquipeB) {
		stmtExiste.setParameter("nomEquipeA", nomEquipeA);
		stmtExiste.setParameter("nomEquipeB", nomEquipeB);
		List<Resultat> resultats = stmtExiste.getResultList();
		if (!resultats.isEmpty()) {
			return resultats.get(0);
		} else {
			return null;
		}
	}

	/**
	 * Ajout d'un nouveau resultat
	 * 
	 */
	public Resultat creer(Resultat resultat) {
		 // Ajout d'une equipe.
        cx.getConnection().persist(resultat);
        
        return resultat;
	}

	 /**
     * Suppression d'un resultat.
     */
    public boolean supprimer(Resultat resultat)
    {
        if(resultat != null)
        {
            cx.getConnection().remove(resultat);
            return true;
        }
        return false;
    }
	
    /**
     * Modifie un resultat dans la base de donnees.
     */
    public Resultat modifierResultat(Resultat resultat)
    {
        // Ajout de la ligue.
        cx.getConnection().persist(resultat);
        
        return resultat;
    }

	/**
	 * Modifier le resultat pour un match.
	 * 
	 */

	/*public int modifier(String nomEquipeA, String nomEquipeB, int scoreEquipeA, int scoreEquipeB) {
		stmtUpdate.setString(3, nomEquipeA);
		stmtUpdate.setString(4, nomEquipeB);
		stmtUpdate.setInt(1, scoreEquipeA);
		stmtUpdate.setInt(2, scoreEquipeB);
		return stmtUpdate.executeUpdate();
	}*/


	/**
	 * Obtenir nombre match gagné d'une équipe.
	 * 
	 */
	public int ObtenirNbMGagne(String nomEquipe) {
		stmtNbMGagne.setParameter("nomEquipeA", nomEquipe);
		stmtNbMGagne.setParameter("nomEquipeB", nomEquipe);
		return stmtNbMGagne.getSingleResult();
	}

	/**
	 * Obtenir nombre match perdu d'une équipe.
	 * 
	 */
	public int ObtenirNbMPerdu(String nomEquipe) {
		stmtNbMPerdu.setParameter("nomEquipeA", nomEquipe);
		stmtNbMPerdu.setParameter("nomEquipeB", nomEquipe);
		return stmtNbMPerdu.getSingleResult();
	}

	/**
	 * Obtenir nombre match nul d'une équipe.
	 * 
	 */
	public int ObtenirNbMNul(String nomEquipe) {
		stmtNbMNul.setParameter("nomEquipeA", nomEquipe);
		stmtNbMNul.setParameter("nomEquipeB", nomEquipe);
		return stmtNbMNul.getSingleResult();
	}

	 /**
     * Retourne l'ensemble des resultats d'une equipe de la base de données
     * @return
     */
    public List<Resultat> calculerListeResultatsEquipe(String nomEquipe)
    {
    	stmtListTousResultatsEquipe.setParameter("nomEquipeA", nomEquipe);
    	stmtListTousResultatsEquipe.setParameter("nomEquipeB", nomEquipe);
        return stmtListTousResultatsEquipe.getResultList();
    }    

    /**
     * Retourne l'ensemble des resultats de la base de données
     * @return
     */
    public List<Resultat> calculerListeResultats()
    {
        return stmtListTousResultats.getResultList();
    }
    
	/**
	 * Afficher les resultats pour un match.
	 */
	/*public void afficher() {
		ResultSet rset = stmtDispResultat.executeQuery();
		rset.close();
	}*/

	/**
	 * Lecture des resultats de l'équipe
	 * 
	 */
	/*public ArrayList<Resultat> lectureResultats(String nomEquipe) {
		stmtDispResultatsEquipe.setString(1, nomEquipe);
		stmtDispResultatsEquipe.setString(2, nomEquipe);

		ResultSet rset = stmtDispResultatsEquipe.executeQuery();
		ArrayList<Resultat> listResultats = new ArrayList<Resultat>();

		while (rset.next()) {
			Resultat tupleResultat = new Resultat();
			tupleResultat.setNomEquipeA(rset.getString("nomEquipeA"));
			tupleResultat.setNomEquipeB(rset.getString("nomEquipeB"));
			tupleResultat.setScoreEquipeA(rset.getInt("scoreEquipeA"));
			tupleResultat.setScoreEquipeB(rset.getInt("scoreEquipeB"));
			listResultats.add(tupleResultat);
		}
		rset.close();
		return listResultats;
	}*/

}
