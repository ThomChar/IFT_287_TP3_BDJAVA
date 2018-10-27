package modele;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import CentreSportif.Connexion;

public class TableEquipes {

	private PreparedStatement stmtExiste;
	private PreparedStatement stmtExisteCapitaine;
	private PreparedStatement stmtInsert;
	private PreparedStatement stmtUpdate;
	private PreparedStatement stmtDelete;
	@SuppressWarnings("unused")
	private PreparedStatement stmtDispEquipes;
	private PreparedStatement stmtDispEquipesLigue;
	@SuppressWarnings("unused")
	private PreparedStatement stmtDispParticipants;
	private PreparedStatement stmtDispEquipesParLigue;
	@SuppressWarnings("unused")
	private PreparedStatement stmtNombreMembresEquipe;
	private PreparedStatement stmtDeleteEquipesLigue;
	private Connexion cx;

	/**
	 * Creation d'une instance. Precompilation d'annonces SQL.
	 */
	public TableEquipes(Connexion cx) throws SQLException {
		this.cx = cx;
		stmtExiste = cx.getConnection()
				.prepareStatement("select nomEquipe, matriculeCapitaine, nomLigue from Equipe where nomEquipe = ?");
		stmtExisteCapitaine = cx.getConnection()
				.prepareStatement("select nomEquipe, matriculeCapitaine, nomLigue from Equipe where matriculeCapitaine = ?");
		stmtInsert = cx.getConnection()
				.prepareStatement("insert into Equipe (nomEquipe, matriculeCapitaine, nomLigue) " + "values (?,?,?)");
		stmtUpdate = cx.getConnection()
				.prepareStatement("update Equipe set matriculeCapitaine = ? where nomEquipe = ?");
		stmtDelete = cx.getConnection().prepareStatement("delete from Equipe where nomEquipe = ?");
		stmtDispEquipes = cx.getConnection().prepareStatement("select nomEquipe, matriculeCapitaine, nomLigue from Equipe");
		stmtDispEquipesLigue = cx.getConnection().prepareStatement("select * from Equipe where nomLigue = ?");
		stmtDispEquipesParLigue = cx.getConnection().prepareStatement("select * from Equipe order by nomLigue");
		stmtDeleteEquipesLigue = cx.getConnection().prepareStatement("delete from Equipe where nomLigue = ?");
		stmtNombreMembresEquipe = cx.getConnection().prepareStatement("select COUNT(*) AS nb FROM Participant WHERE nomEquipe = ?");
	}

	/**
	 * Retourner la connexion associée.
	 */
	public Connexion getConnexion() {
		return cx;
	}

	/**
	 * Vérifie si une Equipe existe.
	 * 
	 *  @throws SQLException
	 */
	public boolean existe(String nomEquipe) throws SQLException {
		stmtExiste.setString(1, nomEquipe);
		ResultSet rset = stmtExiste.executeQuery();
		boolean equipeExiste = rset.next();
		rset.close();
		return equipeExiste;
	}
	
	/**
	 * Verifie si un participant est deja capitain d'une equipe.
	 * 
	 * @throws SQLException
	 */
	public boolean testDejaCapitaine(String matricule) throws SQLException {
		stmtExisteCapitaine.setString(1, matricule);
		ResultSet rset = stmtExisteCapitaine.executeQuery();
		boolean capiatineExiste = rset.next();
		rset.close();
		return capiatineExiste;
	}

	/**
	 * Lecture d'une Equipe.
	 * 
	 * @throws SQLException
	 */
	public Equipe getEquipe(String nomEquipe) throws SQLException {
		stmtExiste.setString(1, nomEquipe);
		ResultSet rset = stmtExiste.executeQuery();
		if (rset.next()) {
			Equipe tupleEquipe = new Equipe();
			tupleEquipe.setNomEquipe(nomEquipe);
			tupleEquipe.setMatriculeCap(rset.getString(2));
			tupleEquipe.setNomLigue(rset.getString(3));
			rset.close();
			return tupleEquipe;
		} else {
			return null;
		}
	}

	/**
	 * Ajout d'une nouvelle equipe non vide.
	 * 
	 * @throws SQLException
	 */
	public void creer(String nomEquipe, String matriculeCap, String nomLigue) throws SQLException {
		/* Ajout de l'equipe. */
		stmtInsert.setString(1, nomEquipe);
		stmtInsert.setString(2, matriculeCap);
		stmtInsert.setString(3, nomLigue);
		stmtInsert.executeUpdate();
	}

	/**
	 * Suppression d'une equipe.
	 * 
	 * @throws SQLException
	 */
	public int supprimer(String nomEquipe) throws SQLException {
		stmtDelete.setString(1, nomEquipe);
		return stmtDelete.executeUpdate();
	}
	
	/**
	 * Change le capitaine de l'equipe d'une equipe.
	 * 
	 * @throws SQLException 
	 */
	public void changerCapitaine(String nomEquipe, String matriculeCap) throws SQLException {
		stmtUpdate.setString(1, matriculeCap);
		stmtUpdate.setString(2, nomEquipe);
		stmtUpdate.executeUpdate();
	}
	
	/**
	 * Suppression des �quipes d'une ligue.
	 * 
	 * @throws SQLException
	 */
	public int supprimerEquipesLigue(String nomLigue) throws SQLException {
		stmtDeleteEquipesLigue.setString(1, nomLigue);
		return stmtDeleteEquipesLigue.executeUpdate();
	}

	/**
	 * Lecture des equipes d'une ligue
	 * 
	 * @throws SQLException
	 */
	public ArrayList<Equipe> lectureEquipesLigue(String nomLigue) throws SQLException {
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
	}
	
	/**
	 * Lecture des equipes de la table
	 * 
	 * @throws SQLException
	 */
	public ArrayList<Equipe> lectureEquipes() throws SQLException {
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
	}

}
