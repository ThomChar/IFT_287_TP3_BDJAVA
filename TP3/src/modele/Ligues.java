package modele;

import java.util.List;
import javax.persistence.TypedQuery;
import CentreSportif.Connexion;

public class Ligues {

	private TypedQuery<Ligue> stmtExiste;
	private TypedQuery<Ligue> stmtListToutesLigues;
	private Connexion cx;

	/**
	 * Creation d'une instance.
	 */
	public Ligues(Connexion cx)  {
		this.cx = cx;
		stmtExiste = cx.getConnection().createQuery("select l from Ligue l where l.nomLigue = :nomLigue", Ligue.class);
		stmtListToutesLigues = cx.getConnection().createQuery("select l from Ligue l", Ligue.class);
	}

	/**
	 * Retourner la connexion associee.
	 */
	public Connexion getConnexion() {
		return cx;
	}

	/**
	 * Ajout d'une nouvelle ligue
	 * @param ligue
	 * @return la ligue créée
	 */
    public Ligue ajouter(Ligue ligue)
    {
        // Ajout de la ligue.
        cx.getConnection().persist(ligue);
        
        return ligue;
    }
    
    /**
     * Verifie si une ligue existe.
     * @param nomLigue
     * @return vrai ou faux
     */
	public boolean existe(String nomLigue) {
		stmtExiste.setParameter("nomLigue", nomLigue);
		return !stmtExiste.getResultList().isEmpty();
	}
	
	/**
	 * Suppression d'une ligue.
	 * @param ligue
	 * @return vrai ou faux
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

    /**
     * Lecture d'une Ligue.
     * @param nomLigue
     * @return la ligue correspondant au nomLigue, ou null
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
	 * Retourne l'ensemble des ligues de la base de données
	 * @return toutes les ligues
	 */
    public List<Ligue> calculerListeLigues()
    {
        return stmtListToutesLigues.getResultList();
    }
}
