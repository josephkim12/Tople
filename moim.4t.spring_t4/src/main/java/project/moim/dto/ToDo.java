package project.moim.dto;

public class ToDo {
	private int schnum;
	private String id;
	private String ex;
	private String flag;
	
	public ToDo() {
		super();
	}

	public ToDo(int schnum, String id, String ex, String flag) {
		super();
		this.schnum = schnum;
		this.id = id;
		this.ex = ex;
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

	public String getEx() {
		return ex;
	}

	public void setEx(String ex) {
		this.ex = ex;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}
}
