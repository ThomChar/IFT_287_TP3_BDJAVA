package modele;

import java.util.List;
import javax.persistence.TypedQuery;
import CentreSportif.Connexion;

public class Equipes {

	private TypedQuery<Equipe> stmtExiste;
	private TypedQuery<Equipe> stmtExisteCapitaine;
	private TypedQuery<Equipe> stmtListToutesEquipes;
	private TypedQuery<Equipe> stmtListToutesEquipesLigue;
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
		stmtListToutesEquipes = cx.getConnection().createQuery("select e from Equipe e",Equipe.class);
		stmtListToutesEquipesLigue = cx.getConnection().createQuery("select e from Equipe e where e.ligue.nomLigue = :nomLigue",Equipe.class);
	}

	/**
	 * Retourner la connexion associée.
	 */
	public Connexion getConnexion() {
		return cx;
	}
	
	/**
	 * Ajout d'une nouvelle equipe non vide.
	 * @param equipe
	 * @return l'équipe créée
	 */
	public Equipe creer(Equipe equipe) {
        cx.getConnection().persist(equipe);
        
        return equipe;
	}

	/**
	 * Vérifie si une Equipe existe.
	 * 
	 */
	public boolean existe(String nomEquipe) {
		stmtExiste.setParameter("nomEquipe", nomEquipe);
		return !stmtExiste.getResultList().isEmpty();
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
	 * Verifie si un participant est deja capitain d'une equipe.
	 * 
	 */
	public boolean testDejaCapitaine(String matricule) {
		stmtExisteCapitaine.setParameter("matriculeCap", matricule);
		List<Equipe> equipe = stmtExisteCapitaine.getResultList();
		if(!equipe.isEmpty())
        {
            return true;
        }
        else
        {
            return false;
        }
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
}
