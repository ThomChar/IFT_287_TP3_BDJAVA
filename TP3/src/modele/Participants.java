package modele;

import java.sql.SQLException;
import java.util.List;

import javax.persistence.TypedQuery;

import CentreSportif.Connexion;

public class Participants {
	
	private Connexion cx;	
	private TypedQuery<Participant> stmtExiste;
	private TypedQuery<Participant> stmtDispByNomPrenomEquipeLigue;
    private TypedQuery<Participant> stmtDispParticipants;
    private TypedQuery<Participant> stmtDispParticipantsActifsEquipe;
    private TypedQuery<Integer> stmtNombreMembresEquipe;
    private TypedQuery<Integer> stmtNombreMembresLigue;

    /**
     * Creation d'une instance. Des annones SQL pour chaque requête sont
     * précompilés.
     */
    public Participants(Connexion cx) throws SQLException
    {
        this.cx = cx;
        stmtExiste = cx.getConnection().createQuery(
                "SELECT P FROM Participant P WHERE matricule = :matricule", Participant.class);
        stmtDispByNomPrenomEquipeLigue = cx.getConnection().createQuery(
        		"SELECT P FROM Participant P WHERE nom LIKE :nom AND prenom LIKE :prenom, equipe.nomEquipe LIKE :nomEquipe, equipe.ligue.nomLigue LIKE :nomLigue", Participant.class);

        stmtDispParticipants = cx.getConnection().createQuery("select P from Participant P", Participant.class);
        stmtDispParticipantsActifsEquipe = cx.getConnection().createQuery("select P from Participant P where nomEquipe = :nomEquipe and statut = :statut", Participant.class);
        // demander si possibilité de faire des naturals joins
        stmtNombreMembresEquipe = cx.getConnection().createQuery("SELECT COUNT(*) AS nb FROM Participant WHERE nomEquipe = :nomEquipe and statut = :statut ", Integer.class);
        stmtNombreMembresLigue = cx.getConnection().createQuery("SELECT COUNT(*) AS nb FROM Participant P WHERE P.equipe.ligue.nomLigue = :nomLigue AND statut = 'ACCEPTE'", Integer.class);
    }

    /**
     * Retourner la connexion associée.
     */
    public Connexion getConnexion()
    {
        return cx;
    }
    
    /**
     * Ajouter un participant
     */
    public Participant creer(Participant p)
    {
    	cx.getConnection().persist(p);
    	return p;
    }

    /**
     * Vérifie si un participant existe.
     * 
     * @throws SQLException
     */
    public boolean existe(String matricule)
    {
    	stmtExiste.setParameter("matricule", matricule);
        List<Participant> participants = stmtExiste.getResultList();
        if(!participants.isEmpty())
        {
            return true;
        }
        else
        {
            return false;
        }
    }
 
    
    /**
     * Vérifie si un participant existe pour un nom, un prénom, un nom d'équipe et un nom de ligue passé en paramètre
     * 
     * @throws SQLException
     */
    public List<Participant> dispParticipantByNomPrenomEquipeLigue(String nom, String prenom, String nomEquipe, String nomLigue)
    {
    	stmtDispByNomPrenomEquipeLigue.setParameter("nom", "%"+nom+"%");
    	stmtDispByNomPrenomEquipeLigue.setParameter("prenom", "%"+prenom+"%");
    	stmtDispByNomPrenomEquipeLigue.setParameter("nomEquipe", "%"+nomEquipe+"%");
    	stmtDispByNomPrenomEquipeLigue.setParameter("nomLigue", "%"+nomLigue+"%");
        List<Participant> rset = stmtDispByNomPrenomEquipeLigue.getResultList();
        return rset;
    }

    /**
     * Lecture d'un participant.
     */
    public Participant getParticipant(String matricule)
    {
    	stmtExiste.setParameter("matricule", matricule);
        List<Participant> participants = stmtExiste.getResultList();
        if(!participants.isEmpty())
        {
            return participants.get(0);
        }
        else
        {
            return null;
        }
    }

    /**
     * Suppression d'un participant du complexe.
     * 
     * @throws SQLException 
     */
    public boolean supprimer(Participant p)
    {
    	if(p != null) {
    		cx.getConnection().remove(p);
    		return true;
    	}
    	return false;
    }

    /**
     * Lecture les participants.
     * 
     * @throws SQLException
     */ 
    public List<Participant> lectureParticipants()
    {
    	List<Participant> rset = stmtDispParticipants.getResultList();
    	return rset;
    }
    
    
    /**
	 * Lecture des participants de l'équipe
	 * 
	 * @throws SQLException
	 */
	public List<Participant> lectureParticipants(String nomEquipe)
	{
		stmtDispParticipantsActifsEquipe.setParameter("nomEquipe", nomEquipe);
		stmtDispParticipantsActifsEquipe.setParameter("statut", "ACCEPTE");
        List<Participant> participants = stmtDispParticipantsActifsEquipe.getResultList();
        if(!participants.isEmpty())
        {
            return participants;
        }
        else
        {
            return null;
        }	
	}
	
	/**
	 * Compter nombres de particpants dans une équipe
	 * 
	 * @throws SQLException
	 */
	public int nombreMembresEquipe(String nomEquipe)
	{
		stmtNombreMembresEquipe.setParameter("nomEquipe", nomEquipe);
		stmtNombreMembresEquipe.setParameter("statut", "ACCEPTE");
        int nbParticipiants = stmtNombreMembresEquipe.getSingleResult();
        return nbParticipiants;
	}
	
	/**
	 * Compter nombres de particpants dans une ligue
	 * 
	 * @throws SQLException
	 */
	public int nombreMembresLigue(String nomLigue)
	{
		stmtNombreMembresLigue.setParameter("nomLigue", nomLigue);
		stmtNombreMembresLigue.setParameter("statut", "ACCEPTE");
        int nbParticipiants = stmtNombreMembresLigue.getSingleResult();
        return nbParticipiants;
	}
 }
