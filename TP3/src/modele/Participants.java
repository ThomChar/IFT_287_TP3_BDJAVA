package modele;

import java.util.List;

import javax.persistence.TypedQuery;

import CentreSportif.Connexion;

public class Participants {
	
	private Connexion cx;	
	private TypedQuery<Participant> stmtExiste;
	private TypedQuery<Participant> stmtDispByNomPrenomEquipeLigue;
    private TypedQuery<Participant> stmtDispParticipants;
    private TypedQuery<Participant> stmtDispParticipantsActifsEquipe;
    private TypedQuery<Long> stmtNombreMembresEquipe;
    private TypedQuery<Long> stmtNombreMembresLigue;

    /**
     * Creation d'une instance. Des annones SQL pour chaque requête sont précompilés.
     * @param cx
     */
    public Participants(Connexion cx)
    {
        this.cx = cx;
        stmtExiste = cx.getConnection().createQuery("SELECT P FROM Participant P WHERE matricule = :matricule", Participant.class);
        stmtDispByNomPrenomEquipeLigue = cx.getConnection().createQuery("SELECT P FROM Participant P WHERE nom LIKE :nom AND prenom LIKE :prenom, equipe.nomEquipe LIKE :nomEquipe, equipe.ligue.nomLigue LIKE :nomLigue", Participant.class);
        stmtDispParticipants = cx.getConnection().createQuery("select P from Participant P", Participant.class);
        stmtDispParticipantsActifsEquipe = cx.getConnection().createQuery("select P from Participant P where nomEquipe = :nomEquipe and statut = :statut", Participant.class);
        stmtNombreMembresEquipe = cx.getConnection().createQuery("SELECT COUNT(P) AS nb FROM Participant P WHERE nomEquipe = :nomEquipe and statut = :statut ", Long.class);
        stmtNombreMembresLigue = cx.getConnection().createQuery("SELECT COUNT(P) AS nb FROM Participant P WHERE P.equipe.ligue.nomLigue = :nomLigue AND statut = 'ACCEPTE'", Long.class);
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
     * @param p
     * @return le participant créé
     */
    public Participant creer(Participant p)
    {
    	cx.getConnection().persist(p);
    	return p;
    }

    /**
     * Vérifie si un participant existe.
     * @param matricule
     * @return vrai ou faux
     */
    public boolean existe(String matricule)
    {
    	stmtExiste.setParameter("matricule", matricule);
        return !stmtExiste.getResultList().isEmpty();
    }
    
    /**
     * Suppression d'un participant du complexe.
     * @param p
     * @return vrai ou faux
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
     * Lecture d'un participant.
     * @param matricule
     * @return un participant
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
     * Lecture de tous les participants.
     * @return liste de participants
     */
    public List<Participant> lectureParticipants()
    {
    	return stmtDispParticipants.getResultList();
    }
    
    /**
     * Lecture des participants de l'équipe
     * @param nomEquipe
     * @return liste de particpants
     */
	public List<Participant> lectureParticipants(String nomEquipe)
	{
		stmtDispParticipantsActifsEquipe.setParameter("nomEquipe", nomEquipe);
		stmtDispParticipantsActifsEquipe.setParameter("statut", "ACCEPTE");
        return stmtDispParticipantsActifsEquipe.getResultList();
	}
	
	/**
	 * Compter nombres de particpants dans une équipe
	 * @param nomEquipe
	 * @return nombre de membres dans une équipe
	 */
	public long nombreMembresEquipe(String nomEquipe)
	{
		stmtNombreMembresEquipe.setParameter("nomEquipe", nomEquipe);
		stmtNombreMembresEquipe.setParameter("statut", "ACCEPTE");
		return stmtNombreMembresEquipe.getSingleResult();
	}
	
	/**
	 * Compter nombres de particpants dans une ligue
	 * @param nomLigue
	 * @return nombre de membres dans une ligue
	 */
	public long nombreMembresLigue(String nomLigue)
	{
		stmtNombreMembresLigue.setParameter("nomLigue", nomLigue);
		stmtNombreMembresLigue.setParameter("statut", "ACCEPTE");
		return stmtNombreMembresLigue.getSingleResult();
	}
	
    /**
     * retourne la liste des partcipants pour une recherche sur le nom et prénom participant, le nom de l'équipe et le nom de la ligue
     * @param nom
     * @param prenom
     * @param nomEquipe
     * @param nomLigue
     * @return
     */
    public List<Participant> dispParticipantByNomPrenomEquipeLigue(String nom, String prenom, String nomEquipe, String nomLigue)
    {
    	stmtDispByNomPrenomEquipeLigue.setParameter("nom", "%"+nom+"%");
    	stmtDispByNomPrenomEquipeLigue.setParameter("prenom", "%"+prenom+"%");
    	stmtDispByNomPrenomEquipeLigue.setParameter("nomEquipe", "%"+nomEquipe+"%");
    	stmtDispByNomPrenomEquipeLigue.setParameter("nomLigue", "%"+nomLigue+"%");
    	return stmtDispByNomPrenomEquipeLigue.getResultList();
    }
 }
