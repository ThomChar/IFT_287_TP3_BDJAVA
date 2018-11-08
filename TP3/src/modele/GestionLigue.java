package modele;

import java.util.List;
import CentreSportif.Connexion;
import CentreSportif.IFT287Exception;

public class GestionLigue {

  	private Ligues ligues;
  	@SuppressWarnings("unused")
	private Equipes equipe;
  	private Participants participant;
    private Connexion cx;

    /**
     * Creation d'une instance
     */
    public GestionLigue(Ligues ligues, Equipes equipe, Participants participant) throws IFT287Exception
    {
        this.cx = ligues.getConnexion();
        if (participant.getConnexion() != ligues.getConnexion() || equipe.getConnexion() != ligues.getConnexion())
        	throw new IFT287Exception("Les différents gestionnaires (equipes, partcipants, ligues) n'utilisent pas la même connexion au serveur");
        this.ligues = ligues;
        this.equipe = equipe;
        this.participant = participant;
    }

    /**
     * Ajout d'une nouvelle ligue. S'il existe déjà , une exception est levée.
     * @param nomLigue
     * @param nbJoueurMaxParEquipe
     * @throws IFT287Exception
     * @throws Exception
     */
    public void ajouterLigue(String nomLigue, int nbJoueurMaxParEquipe) throws IFT287Exception, Exception
    {
    	try
        {
    		cx.demarreTransaction();
        	
            // Vérifie si la ligue existe déjà
            if (ligues.existe(nomLigue))
                throw new IFT287Exception("La ligue "+nomLigue+" existe déjà.");
            
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
     * Modifier le nombre de joueur max par equipe pour une ligue dans la base de données.
     * @param nomLigue
     * @param nbJoueurMaxParEquipe
     * @throws IFT287Exception
     * @throws Exception
     */
    public void modifierNombreJoueurMax(String nomLigue, int nbJoueurMaxParEquipe) throws IFT287Exception, Exception
    {
        try
        {
        	cx.demarreTransaction();
        	
            // Vérifie si la ligue existe déjà
            if (!ligues.existe(nomLigue))
                throw new IFT287Exception("Ligue "+nomLigue+" existe déjà : ");
            
            Ligue tupleLigue = ligues.getLigue(nomLigue);
            
            // Modifier ligue
        	tupleLigue.setNbJoueurMaxParEquipe(nbJoueurMaxParEquipe);
            
            cx.commit();
        }
        catch (Exception e)
        {
            cx.rollback();
            throw e;
        }
    }
        
    /**
     * Supprime Ligue
     * @param nomLigue
     * @throws IFT287Exception
     * @throws Exception
     */
    public void supprimer(String nomLigue) throws IFT287Exception, Exception
    {
        try
        {
        	cx.demarreTransaction();
            // Validations
            Ligue tupleLigue = ligues.getLigue(nomLigue);
            if (tupleLigue == null)
                throw new IFT287Exception("Ligue inexistant: " + nomLigue);
            if (participant.nombreMembresLigue(nomLigue) > 0)
                throw new IFT287Exception("Ligue " + nomLigue + " a encore des participants actifs");
            
            // suppression de la ligue
            boolean testLigue = ligues.supprimer(tupleLigue);
            if (testLigue == false)
                throw new IFT287Exception("Ligue " + nomLigue + " inexistante");

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
    public void afficherLigues()
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
