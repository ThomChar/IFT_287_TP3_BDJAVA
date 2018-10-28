package modele;

import java.util.List;
import javax.persistence.TypedQuery;
import CentreSportif.Connexion;

public class Equipes {

	private TypedQuery<Equipe> stmtExiste;
	private TypedQuery<Equipe> stmtExisteCapitaine;
	private TypedQuery<Equipe> stmtListToutesEquipes;
	private TypedQuery<Equipe> stmtListToutesEquipesLigue;
	//private TypedQuery<Equipe> stmtNombreMembresEquipe;
	
	//private TypedQuery<Equipe> stmtUpdate;
	//private TypedQuery<Equipe> stmtDelete;
	
	private Connexion cx;

	/**
	 * Creation d'une instance.
	 */
	public Equipes(Connexion cx) {
		this.cx = cx;
		stmtExiste = cx.getConnection()
				.createQuery("select e from Equipe e where e.nomEquipe = :nomEquipe",Equipe.class);
		stmtExisteCapitaine = cx.getConnection()
				.createQuery("select e from Equipe e where e.capitaine.matricule = :matriculeCap",Equipe.class);
		/*stmtUpdate = cx.getConnection()
				.createQuery("update Equipe set matriculeCapitaine = ? where nomEquipe = ?",Equipe.class);
		stmtDelete = cx.getConnection().createQuery("delete from Equipe where nomEquipe = ?",Equipe.class);*/
		stmtListToutesEquipes = cx.getConnection().createQuery("select e from Equipe e",Equipe.class);
		stmtListToutesEquipesLigue = cx.getConnection().createQuery("select e from Equipe e where e.ligue.nomLigue = :nomLigue",Equipe.class);
		//stmtNombreMembresEquipe = cx.getConnection().createQuery("select COUNT(*) AS nb from Participant p where p.equipe.nomEquipe = :nomEquipe",Equipe.class);
	}

	/**
	 * Retourner la connexion associÃ©e.
	 */
	public Connexion getConnexion() {
		return cx;
	}

	/**
	 * VÃ©rifie si une Equipe existe.
	 * 
	 */
	public boolean existe(String nomEquipe) {
		stmtExiste.setParameter("nomEquipe", nomEquipe);
		return !stmtExiste.getResultList().isEmpty();
	}
	
	/**
	 * Verifie si un participant est deja capitain d'une equipe.
	 * 
	 */
	public boolean testDejaCapitaine(String matricule) {
		stmtExisteCapitaine.setParameter("matriculeCap", matricule);
		return !stmtExisteCapitaine.getResultList().isEmpty();
	}

	/**
	 * Lecture d'une Equipe.
	 * 
	 */
	public Equipe getEquipe(String nomEquipe){
		stmtExiste.setParameter("nomEquipe", nomEquipe);
		List<Equipe> equipes = stmtExiste.getResultList();
		if (!equipes.isEmpty()) {
			return equipes.get(0);
		} else {
			return null;
		}
	}

	/**
	 * Ajout d'une nouvelle equipe non vide.
	 * 
	 */
	public Equipe creer(Equipe equipe) {
		 // Ajout d'une equipe.
        cx.getConnection().persist(equipe);
        
        return equipe;
	}

	 /**
     * Suppression d'une Equipe.
     */
    public boolean supprimer(Equipe equipe)
    {
        if(equipe != null)
        {
            cx.getConnection().remove(equipe);
            return true;
        }
        return false;
    }
	
    /**
     * Modifie une equipe dans la base de donnees.
     */
    public Equipe modifierEquipe(Equipe equipe)
    {
        // Ajout de la ligue.
        cx.getConnection().persist(equipe);
        
        return equipe;
    }
    
	/**
	 * Change le capitaine de l'equipe.
	 * 
	 */
	/*public void changerCapitaine(String nomEquipe, String matriculeCap) throws SQLException {
		stmtUpdate.setString(1, matriculeCap);
		stmtUpdate.setString(2, nomEquipe);
		stmtUpdate.executeUpdate();
	}*/
	
    /**
     * Retourne l'ensemble des equipes de la base de données
     * @return
     */
    public List<Equipe> calculerListeEquipes()
    {
        return stmtListToutesEquipes.getResultList();
    }
    
    /**
     * Retourne l'ensemble des equipes d'une ligue de la base de données
     * @return
     */
    public List<Equipe> calculerListeEquipesLigue(String nomLigue)
    {
    	stmtListToutesEquipesLigue.setParameter("nomLigue", nomLigue);
        return stmtListToutesEquipesLigue.getResultList();
    }
    
	/**
	 * Suppression des équipes d'une ligue.
	 * 
	 */
	/*public int supprimerEquipesLigue(String nomLigue) throws SQLException {
		stmtDeleteEquipesLigue.setString(1, nomLigue);
		return stmtDeleteEquipesLigue.executeUpdate();
	}*/

	/**
	 * Lecture des equipes d'une ligue
	 * 
	 */
	/*public ArrayList<Equipe> lectureEquipesLigue(String nomLigue) throws SQLException {
		stmtDispEquipesLigue.setString(1, nomLigue);
		ResultSet rset = stmtDispEquipesLigue.executeQuery();

		ArrayList<Equipe> listEquipes = new ArrayList<Equipe>();

		while (rset.next()) {
			Equipe tupleEquipe = new Equipe();
			tupleEquipe.setNomEquipe(rset.getString("nomEquipe"));
			tupleEquipe.setMatriculeCap(rset.getString("matriculeCapitaine"));
			tupleEquipe.setNomLigue(rset.getString("nomLigue"));
			listEquipes.add(tupleEquipe);
		}
		rset.close();
		return listEquipes;
	}*/
	
	/**
	 * Lecture des equipes de la table
	 * 
	 */
	/*public ArrayList<Equipe> lectureEquipes() throws SQLException {
		ResultSet rset = stmtDispEquipesParLigue.executeQuery();

		ArrayList<Equipe> listEquipes = new ArrayList<Equipe>();

		while (rset.next()) {
			Equipe tupleEquipe = new Equipe();
			tupleEquipe.setNomEquipe(rset.getString("nomEquipe"));
			tupleEquipe.setMatriculeCap(rset.getString("matriculeCapitaine"));
			tupleEquipe.setNomLigue(rset.getString("nomLigue"));
			//rset.close();
			listEquipes.add(tupleEquipe);
		}
		rset.close();
		return listEquipes;
	}*/

}
