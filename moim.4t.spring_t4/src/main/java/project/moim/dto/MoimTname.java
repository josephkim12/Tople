package project.moim.dto;

public class MoimTname {
	private int moimcode;
	private String tname;
	
	public MoimTname() {
		super();
	}

	public MoimTname(int moimcode, String tname) {
		super();
		this.moimcode = moimcode;
		this.tname = tname;
	}

	public int getMoimcode() {
		return moimcode;
	}

	public void setMoimcode(int moimcode) {
		this.moimcode = moimcode;
	}

	public String getTname() {
		return tname;
	}

	public void setTname(String tname) {
		this.tname = tname;
	}
}
