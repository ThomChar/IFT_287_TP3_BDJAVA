package modele;

import java.util.List;
import CentreSportif.Connexion;
import CentreSportif.IFT287Exception;

public class GestionLigue {

  	private Ligues ligues;
  	@SuppressWarnings("unused")
	private Equipes equipe;			// unused si suppression en cascade
  	private Participants participant;
    private Connexion cx;

    /**
     * Creation d'une instance
     */
    public GestionLigue(Ligues ligues, Equipes equipe, Participants participant) throws IFT287Exception
    {
        this.cx = ligues.getConnexion();
        if (participant.getConnexion() != ligues.getConnexion() || equipe.getConnexion() != ligues.getConnexion())
            throw new IFT287Exception("Les instances de ligue, particpant et equipe n'utilisent pas la m�me connexion au serveur");
        this.ligues = ligues;
        this.equipe = equipe;
        this.participant = participant;
    }

    
    /**
     * Ajout d'une nouvelle ligue dans la base de donn�es. S'il existe d�j�, une
     * exception est lev�e.
     * 
     *  @throws SQLException, IFT287Exception, Exception
     */		
    public void ajouterLigue(String nomLigue, int nbJoueurMaxParEquipe) throws IFT287Exception, Exception
    {
    	try
        {
    		cx.demarreTransaction();
        	
            // V�rifie si la ligue existe d�j�
            if (ligues.existe(nomLigue))
                throw new IFT287Exception("Ligue "+nomLigue+" existe d�j�: ");
            
            Ligue tupleLigue = new Ligue(nomLigue, nbJoueurMaxParEquipe);
            
            // Ajout de la ligue dans la table des ligues
            ligues.ajouter(tupleLigue);
            
            cx.commit();
        }
        catch (Exception e)
        {
            cx.rollback();
            throw e;
        }
    }
    
    /**
     * Modifier le nombre de joueur max par equipe pour une ligue dans la base de donn�es. 
     * 
     *  @throws SQLException, IFT287Exception, Exception
     */		
    public void modifierNombreJoueurMax(String nomLigue, int nbJoueurMaxParEquipe) throws IFT287Exception, Exception
    {
        try
        {
        	cx.demarreTransaction();
        	
            // V�rifie si la ligue existe d�j�
            if (!ligues.existe(nomLigue))
                throw new IFT287Exception("Ligue "+nomLigue+" existe d�j�: ");
            
            Ligue tupleLigue = ligues.getLigue(nomLigue);
        	tupleLigue.setNbJoueurMaxParEquipe(nbJoueurMaxParEquipe);
            
            // Ajout de la ligue dans la table des ligues
            ligues.modifierLigue(tupleLigue);
            
            cx.commit();
        }
        catch (Exception e)
        {
            cx.rollback();
            throw e;
        }
    }
        
    
    /**
     * Supprime Ligue de la base de donn�es.
     * 
     *  @throws SQLException, IFT287Exception, Exception
     */
    public void supprime(String nomLigue) throws IFT287Exception, Exception
    {
        try
        {
        	cx.demarreTransaction();
        	
            // Validation
            Ligue tupleLigue = ligues.getLigue(nomLigue);
            if (tupleLigue == null)
                throw new IFT287Exception("Ligue inexistant: " + nomLigue);
            if (participant.nombreMembresLigue(nomLigue) > 0)
                throw new IFT287Exception("Ligue " + nomLigue + "a encore des participants actifs");
            
            // Suppression des equipes de la ligue.
            /*@SuppressWarnings("unused")
			int nbEquipe = equipe.supprimerEquipesLigue(nomLigue);*/				// Demander pour suppression cascade
            // Suppression de la ligue.
            boolean testLigue = ligues.supprimer(tupleLigue);
            if (testLigue == false)
                throw new IFT287Exception("Ligue " + nomLigue + " inexistante");
            
            // Commit
            cx.commit();
        }
        catch (Exception e)
        {
            cx.rollback();
            throw e;
        }
    }
    
    /**
     * Affiche toutes les ligues de la BD
     */
    public void listerLigues()
    {
        cx.demarreTransaction();
        
        List<Ligue> l = ligues.calculerListeLigues();
                
        for(Ligue li : l)
        {
            System.out.println(li.toString());
        }
        
        cx.commit();
    }
}
