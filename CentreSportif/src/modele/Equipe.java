package modele;

import java.util.ArrayList;

public class Equipe {
	
	//Attributes

	private String nomEquipe;
	private ArrayList<Participant> listParticipants;
	private String matriculeCap;
	private Participant capitaine;
	private String nomLigue;
	private Ligue ligue;
	private ArrayList<Resultat> listResultats;
	
	//Builders
	
	public Equipe() {
	}
	
	public Equipe(String nomLigue, String nomEquipe, String matriculeCap) {
		super();
		this.setLigue(ligue);
		this.nomEquipe = nomEquipe;
		this.listParticipants = new ArrayList<Participant>();
		this.matriculeCap = matriculeCap;
		this.capitaine = null;
		this.nomLigue = nomLigue;
		this.ligue = null;
		this.listResultats = new ArrayList<Resultat>();
	}

	//Getters & Setters

	public String getNomEquipe() {
		return nomEquipe;
	}

	public void setNomEquipe(String nomEquipe) {
		this.nomEquipe = nomEquipe;
	}

	public ArrayList<Participant> getListParticipants() {
		return listParticipants;
	}

	public void setListParticipants(ArrayList<Participant> listParticipants) {
		this.listParticipants = listParticipants;
	}

	public Participant getCapitaine() {
		return capitaine;
	}

	public void setCapitaine(Participant capitaine) {
		this.capitaine = capitaine;
	}

	public Ligue getLigue() {
		return ligue;
	}

	public void setLigue(Ligue ligue) {
		this.ligue = ligue;
	}

	public ArrayList<Resultat> getListResultats() {
		return listResultats;
	}

	public void setListResultats(ArrayList<Resultat> listResultat) {
		this.listResultats = listResultat;
	}


	public String getMatriculeCap() {
		return matriculeCap;
	}


	public void setMatriculeCap(String matriculeCap) {
		this.matriculeCap = matriculeCap;
	}


	public String getNomLigue() {
		return nomLigue;
	}


	public void setNomLigue(String nomLigue) {
		this.nomLigue = nomLigue;
	}

	public boolean isActive() {
		boolean testIsActive = true;
		if(this.getListParticipants().size() == 0) {
			testIsActive = false;
		}
		return testIsActive;
	}

	@Override
	public String toString() {
		return "\nEquipe [nomEquipe=" + nomEquipe + ", matriculeCap="+ matriculeCap + ", nomLigue=" + nomLigue + ",\nlistParticipants=" + listParticipants + ",\nlistResultats=" + listResultats + "]";
	}
	public String toStringSimpleEquipe() {
		return "\nEquipe [nomEquipe=" + nomEquipe + ", matriculeCap="+ matriculeCap + ", nomLigue=" + nomLigue + "]";
	}

}
