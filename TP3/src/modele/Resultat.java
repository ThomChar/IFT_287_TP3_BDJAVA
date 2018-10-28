package modele;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

@Entity
public class Resultat {
 
	//Attributes
	
	@Id
    @GeneratedValue
    
    @ManyToMany						//Ne sais pas quoi mettre cascade ou non
	private Equipe equipeA;
	@ManyToMany						//Ne sais pas quoi mettre cascade ou non
	private Equipe equipeB;
	
	private int scoreEquipeA;
	private int scoreEquipeB;
		
	//Builders
	
	public Resultat() {
	}
	
	public Resultat(Equipe equipeA, Equipe equipeB, int scoreEquipeA, int scoreEquipeB) {
		super();
		this.equipeA = equipeA;
		this.equipeB = equipeB;
		
		this.scoreEquipeA = scoreEquipeA;
		this.scoreEquipeB = scoreEquipeB;
	}
	
	//Getters & Setters
	
	public Equipe getEquipeA() {
		return equipeA;
	}
	
	public void setEquipeA(Equipe equipeA) {
		this.equipeA = equipeA;
	}
	public Equipe getEquipeB() {
		return equipeB;
	}
	public void setEquipeB(Equipe equipeB) {
		this.equipeB = equipeB;
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

	@Override
	public String toString() {
		return "\nResultat [nomEquipeA=" + equipeA.getNomEquipe() + ", nomEquipeB=" + equipeB.getNomEquipe() + ", scoreEquipeA=" + scoreEquipeA
				+ ", scoreEquipeB=" + scoreEquipeB + "]";
	}
	
	
	
}
