package project.moim.dto;

public class Schemem {
	private int schnum;
	private String id;
	private String todo;
	private String ex;
	private String isdo;
	private String amount;
	private String ispay;
	
	public Schemem() {
		super();
	}

	public int getSchnum() {
		return schnum;
	}

	public void setSchnum(int schnum) {
		this.schnum = schnum;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTodo() {
		return todo;
	}

	public void setTodo(String todo) {
		this.todo = todo;
	}

	public String getEx() {
		return ex;
	}

	public void setEx(String ex) {
		this.ex = ex;
	}

	public String getIsdo() {
		return isdo;
	}

	public void setIsdo(String isdo) {
		this.isdo = isdo;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getIspay() {
		return ispay;
	}

	public void setIspay(String ispay) {
		this.ispay = ispay;
	}
}
