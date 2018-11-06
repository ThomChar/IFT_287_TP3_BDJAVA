// Travail fait par :
//   Charlet Thomas - 18-146-249
//   Hilleriteau R�mi - 18-146-347

package CentreSportif;

import java.io.*;
import java.util.StringTokenizer;

import modele.GestionEquipe;
import modele.GestionLigue;
import modele.GestionParticipant;
import modele.GestionResultat;
import modele.Equipes;
import modele.Ligues;
import modele.Participants;
import modele.Resultats;

import java.sql.*;
//Les param�tres pour ex�cuter notre programme dinf ift287_23db ift287_23 paique
/**
 * Fichier de base pour le TP2 du cours IFT287
 *
 * <pre>
 * 
 * Vincent Ducharme
 * Universite de Sherbrooke
 * Version 1.0 - 16 août 2018
 * IFT287 - Exploitation de BD relationnelles et OO
 * 
 * Ce programme permet d'appeler des transactions d'un systeme
 * de gestion utilisant une base de donnees.
 *
 * Paramètres du programme
 * 0- site du serveur SQL ("local" ou "dinf")
 * 1- nom de la BD
 * 2- user id pour etablir une connexion avec le serveur SQL
 * 3- mot de passe pour le user id
 * 4- fichier de transaction [optionnel]
 *           si non spécifié, les transactions sont lues au
 *           clavier (System.in)
 *
 * Pré-condition
 *   - La base de donnees doit exister
 *
 * Post-condition
 *   - Le programme effectue les mises à jour associees à chaque
 *     transaction
 * </pre>
 */
public class CentreSportif
{
    private static Connexion cx;
    private static GestionEquipe gestionEquipe;
    private static GestionLigue gestionLigue;
    private static GestionParticipant gestionParticipant;
    private static GestionResultat gestionResultat;
    private static Equipes Equipes;
    private static Ligues Ligues;
    private static Participants Participants;
    private static Resultats Resultats;

    /**
     * @param args
     */
    public static void main(String[] args) throws Exception
    {
    	if (args.length < 4)
        {
            System.out.println("Usage: java CentreSportif.CentreSportif <serveur> <bd> <user> <password> [<fichier-transactions>]");
            return;
        }
        
        cx = null;
        
        try
        {
        	
            // Il est possible que vous ayez à déplacer la connexion ailleurs.
            // N'hésitez pas à le faire!
        	cx = new Connexion(args[0], args[1], args[2], args[3]);
            BufferedReader reader = ouvrirFichier(args);  
            String transaction = lireTransaction(reader);
            Init();
            while (!finTransaction(transaction))
            {
                executerTransaction(transaction);
                transaction = lireTransaction(reader);
            }
        }
        finally
        {
            if (cx != null)
                cx.fermer();
        }
    }
    
    static void Init() throws SQLException, IFT287Exception
    {
    	Ligues = new Ligues(cx);
    	Equipes = new Equipes(cx);
    	Participants = new Participants(cx);
    	Resultats = new Resultats(cx);
        gestionLigue = new GestionLigue(Ligues, Equipes, Participants);
		gestionEquipe = new GestionEquipe(Equipes,Participants,Ligues,Resultats);
        gestionParticipant = new GestionParticipant(Participants, Equipes, Ligues);
        gestionResultat = new GestionResultat(Resultats, Equipes);
    }

    /**
     * Decodage et traitement d'une transaction
     */
    static void executerTransaction(String transaction) throws Exception, IFT287Exception
    {
        try
        {
            System.out.print(transaction);
            // Decoupage de la transaction en mots
            StringTokenizer tokenizer = new StringTokenizer(transaction, " ");
            if (tokenizer.hasMoreTokens())
            {
                String command = tokenizer.nextToken();
                // Vous devez remplacer la chaine "commande1" et "commande2" par
                // les commandes de votre programme. Vous pouvez ajouter autant
                // de else if que necessaire. Vous n'avez pas a traiter la
                // commande "quitter".
               if(command.equals("inscrireParticipant")) 
                {
                	String prenom = readString(tokenizer);
                	String nom = readString(tokenizer);
                	String motDePasse = readString(tokenizer);
                	String matricule = readString(tokenizer);
                	gestionParticipant.ajouter(matricule, prenom, nom, motDePasse);
                }
                else if(command.equals("supprimerParticipant")) 
                {
                	String matricule = readString(tokenizer);
                	gestionParticipant.supprimer(matricule);
                }
                else if(command.equals("ajouterLigue")) 
                {
                	String nomLigue = readString(tokenizer);
                	int nbJoueurMax = readInt(tokenizer);
                	gestionLigue.ajouterLigue(nomLigue, nbJoueurMax);
                }
                else if(command.equals("supprimerLigue"))
                {
                	String nomLigue = readString(tokenizer);
                	gestionLigue.supprimer(nomLigue);
                }
                else if(command.equals("ajouterEquipe"))
                {
                	String nomLigue = readString(tokenizer);
                	String nomEquipe = readString(tokenizer);
                	String matriculeCap = readString(tokenizer);
                	gestionEquipe.ajouter(nomEquipe, matriculeCap, nomLigue);
                }
                else if(command.equals("changerCapitaine"))
                {
                	String nomEquipe = readString(tokenizer);
                	String matriculeCap = readString(tokenizer);
                	gestionEquipe.changerCapitaine(nomEquipe, matriculeCap);
                }
                else if(command.equals("ajouterJoueur"))	
                {
                	String nomEquipe = readString(tokenizer);
                	String matricule = readString(tokenizer);
                	gestionParticipant.postulerAUneEquipe(nomEquipe, matricule);
                }
                else if(command.equals("accepterJoueur"))	
                {
                	String nomEquipe = readString(tokenizer);
                	String matricule = readString(tokenizer);
                	gestionParticipant.accepteParEquipe(nomEquipe, matricule);
                }
                else if(command.equals("refuserJoueur"))	
                {
                	String nomEquipe = readString(tokenizer);
                	String matricule = readString(tokenizer);
                	gestionParticipant.refuseParEquipe(nomEquipe, matricule);
                }
                else if(command.equals("supprimerJoueur"))	
                {
                	String nomEquipe = readString(tokenizer);
                	String matricule = readString(tokenizer);
                	gestionParticipant.supprimeParEquipe(nomEquipe, matricule);
                }
                else if(command.equals("afficherEquipes")) 
                {
                	gestionEquipe.affichageEquipes();
                }
                else if(command.equals("afficherEquipe")) 
                {
                	String nomEquipe = readString(tokenizer);
                	gestionEquipe.affichageEquipe(nomEquipe);
                }
                else if(command.equals("afficherLigue")) 
                {
                	String nomLigue = readString(tokenizer);
                	gestionEquipe.afficherEquipesLigue(nomLigue);
                }
                else if(command.equals("ajouterResultat"))	
                {
                	String nomEquipeA = readString(tokenizer);
                	int scoreEquipeA = readInt(tokenizer);
                	String nomEquipeB = readString(tokenizer);
                	int scoreEquipeB = readInt(tokenizer);
                	gestionResultat.InscrireResulat(nomEquipeA, nomEquipeB, scoreEquipeA, scoreEquipeB);
                }
                else if(command.equals("supprimerResultat"))	
                {
                	String nomEquipeA = readString(tokenizer);
                	String nomEquipeB = readString(tokenizer);
                	gestionResultat.supprimerResultat(nomEquipeA, nomEquipeB);
                }
                else
                {
                    System.out.println(" : Transaction non reconnue");
                }
                
            }
        }
        catch (Exception e)
        {
            System.out.println(" " + e.toString());
        }
    }

    
    // ****************************************************************
    // *   Les methodes suivantes n'ont pas besoin d'etre modifiees   *
    // ****************************************************************

    /**
     * Ouvre le fichier de transaction, ou lit à partir de System.in
     */
    public static BufferedReader ouvrirFichier(String[] args) throws FileNotFoundException
    {
        if (args.length < 5)
            // Lecture au clavier
            return new BufferedReader(new InputStreamReader(System.in));
        else
            // Lecture dans le fichier passe en parametre
            return new BufferedReader(new InputStreamReader(new FileInputStream(args[4])));
    }

    /**
     * Lecture d'une transaction
     */
    static String lireTransaction(BufferedReader reader) throws IOException
    {
        return reader.readLine();
    }

    /**
     * Verifie si la fin du traitement des transactions est atteinte.
     */
    static boolean finTransaction(String transaction)
    {
        // fin de fichier atteinte
        return (transaction == null || transaction.equals("quitter"));
    }

    /** Lecture d'une chaine de caracteres de la transaction entree a l'ecran */
    static String readString(StringTokenizer tokenizer) throws Exception
    {
        if (tokenizer.hasMoreElements())
            return tokenizer.nextToken();
        else
            throw new Exception("Autre parametre attendu");
    }

    /**
     * Lecture d'un int java de la transaction entree a l'ecran
     */
    static int readInt(StringTokenizer tokenizer) throws Exception
    {
        if (tokenizer.hasMoreElements())
        {
            String token = tokenizer.nextToken();
            try
            {
                return Integer.valueOf(token).intValue();
            }
            catch (NumberFormatException e)
            {
                throw new Exception("Nombre attendu a la place de \"" + token + "\"");
            }
        }
        else
            throw new Exception("Autre parametre attendu");
    }

    static Date readDate(StringTokenizer tokenizer) throws Exception
    {
        if (tokenizer.hasMoreElements())
        {
            String token = tokenizer.nextToken();
            try
            {
                return Date.valueOf(token);
            }
            catch (IllegalArgumentException e)
            {
                throw new Exception("Date dans un format invalide - \"" + token + "\"");
            }
        }
        else
            throw new Exception("Autre parametre attendu");
    }

}
