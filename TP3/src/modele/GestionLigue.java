package modele;

import java.sql.SQLException;

import CentreSportif.Connexion;
import CentreSportif.IFT287Exception;

public class GestionLigue {

  	private TableLigues ligue;
  	private TableEquipes equipe;
  	private TableParticipants participant;
    private Connexion cx;

    /**
     * Creation d'une instance
     */
    public GestionLigue(TableLigues ligue, TableEquipes equipe, TableParticipants participant) throws IFT287Exception
    {
        this.cx = ligue.getConnexion();
        if (participant.getConnexion() != ligue.getConnexion() || equipe.getConnexion() != ligue.getConnexion())
            throw new IFT287Exception("Les instances de ligue, particpant et equipe n'utilisent pas la même connexion au serveur");
        this.ligue = ligue;
        this.equipe = equipe;
        this.participant = participant;
    }

    /**
     * Ajout d'une nouvelle ligue vide dans la base de données. S'il existe déjà , une
     * exception est levée.
     * 
     * @throws SQLException, IFT287Exception, Exception
     */		
    public void ajouterLigueEmpty(String nomLigue, int nbJoueurMaxParEquipe) throws SQLException, IFT287Exception, Exception
    {
        try
        {
            // Vérifie si la ligue existe déjà
            if (ligue.existe(nomLigue))
                throw new IFT287Exception("Ligue "+nomLigue+" existe déjà : ");

            // Ajout d'une ligue vide dans la table des ligues
            ligue.creationEmptyLigue(nomLigue, nbJoueurMaxParEquipe);
            
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
     * Ajout d'une nouvelle ligue dans la base de données. S'il existe déjà , une
     * exception est levée.
     * 
     *  @throws SQLException, IFT287Exception, Exception
     */		
    public void ajouterLigue(String nomLigue, int nbJoueurMaxParEquipe) throws SQLException, IFT287Exception, Exception
    {
    	try
        {
        	Ligue tupleLigue = new Ligue(nomLigue, nbJoueurMaxParEquipe);
            // Vérifie si la ligue existe déjà
        	
            if (ligue.existe(nomLigue))
                throw new IFT287Exception("Ligue "+nomLigue+" existe déjà : ");
            if (!tupleLigue.testNewEquipes(nomLigue))
                throw new IFT287Exception("Ligue "+nomLigue+" comprend une équipe déjà dans une autre ligue ");

            // Ajout de la ligue dans la table des ligues
            ligue.creationEmptyLigue(nomLigue, nbJoueurMaxParEquipe);
            
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
     * Modifier le nombre de joueur max par equipe pour une ligue dans la base de données. 
     * 
     *  @throws SQLException, IFT287Exception, Exception
     */		
    public void modifierNombreJoueurMax(String nomLigue, int nbJoueurMaxParEquipe) throws SQLException, IFT287Exception, Exception
    {
        try
        {
            // Vérifie si la ligue existe déjà
            if (ligue.existe(nomLigue))
                throw new IFT287Exception("Ligue "+nomLigue+" existe déjà : ");
            
            // Ajout de la ligue dans la table des ligues
            ligue.modifierNbJoueursMaxParEquipe(nomLigue, nbJoueurMaxParEquipe);;
            
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
     * Supprime Ligue de la base de données.
     * 
     *  @throws SQLException, IFT287Exception, Exception
     */
    public void supprime(String nomLigue) throws SQLException, IFT287Exception, Exception
    {
        try
        {
            // Validation
            Ligue tupleLigue = ligue.getLigue(nomLigue);
            if (tupleLigue == null)
                throw new IFT287Exception("Ligue inexistant: " + nomLigue);
            if (participant.nombreMembresLigue(nomLigue) > 0)
                throw new IFT287Exception("Ligue " + nomLigue + "a encore des participants actifs");
            
            // Suppression des equipes de la ligue.
            @SuppressWarnings("unused")
			int nbEquipe = equipe.supprimerEquipesLigue(nomLigue);
            // Suppression de la ligue.
            int nbLique = ligue.supprimer(nomLigue);
            if (nbLique == 0)
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
}
