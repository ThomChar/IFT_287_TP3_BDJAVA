package modele;

public class Resultat {
 
	//Attributes
	
	private Equipe EquipeA;
	private Equipe EquipeB;
	private String nomEquipeA;
	private String nomEquipeB;
	private int scoreEquipeA;
	private int scoreEquipeB;
		
	//Builders
	
	public Resultat() {
	}
	
	public Resultat(String nomEquipeA, String nomEquipeB, int scoreEquipeA, int scoreEquipeB) {
		super();
		this.EquipeA = null;
		this.EquipeB = null;
		this.nomEquipeA = nomEquipeA;
		this.nomEquipeB = nomEquipeB;
		this.scoreEquipeA = scoreEquipeA;
		this.scoreEquipeB = scoreEquipeB;
	}
	
	//Getters & Setters
	
	public Equipe getEquipeA() {
		return EquipeA;
	}
	
	public void setEquipeA(Equipe equipeA) {
		EquipeA = equipeA;
	}
	public Equipe getEquipeB() {
		return EquipeB;
	}
	public void setEquipeB(Equipe equipeB) {
		EquipeB = equipeB;
	}
	public int getScoreEquipeA() {
		return scoreEquipeA;
	}
	public void setScoreEquipeA(int scoreEquipeA) {
		this.scoreEquipeA = scoreEquipeA;
	}
	public int getScoreEquipeB() {
		return scoreEquipeB;
	}
	public void setScoreEquipeB(int scoreEquipeB) {
		this.scoreEquipeB = scoreEquipeB;
	}

	public String getNomEquipeA() {
		return nomEquipeA;
	}

	public void setNomEquipeA(String nomEquipeA) {
		this.nomEquipeA = nomEquipeA;
	}

	public String getNomEquipeB() {
		return nomEquipeB;
	}

	public void setNomEquipeB(String nomEquipeB) {
		this.nomEquipeB = nomEquipeB;
	}

	@Override
	public String toString() {
		return "\nResultat [nomEquipeA=" + nomEquipeA + ", nomEquipeB=" + nomEquipeB + ", scoreEquipeA=" + scoreEquipeA
				+ ", scoreEquipeB=" + scoreEquipeB + "]";
	}
	
	
	
}
