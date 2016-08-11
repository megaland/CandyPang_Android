package usnschool.com.candypangproject;

import java.io.Serializable;

public class RankerData implements Serializable {
	private static final long serialVersionUID = 1L;
	int num;
	int rank;
	String id;
	int score;
	
	public int getNum() {
		return num;
	}
	public void setNum(int num) {
		this.num = num;
	}
	public int getRank() {
		return rank;
	}
	public void setRank(int rank) {
		this.rank = rank;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public int getScore() {
		return score;
	}
	public void setScore(int score) {
		this.score = score;
	}
	
	
}
