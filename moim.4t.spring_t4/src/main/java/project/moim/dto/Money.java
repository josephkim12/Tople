package project.moim.dto;

public class Money {
	private int schnum;
	private String id;
	private String amount;
	private String flag;
	
	public Money() {
		super();
	}

	public Money(int schnum, String id, String amount, String flag) {
		super();
		this.schnum = schnum;
		this.id = id;
		this.amount = amount;
		this.flag = flag;
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

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}
}
