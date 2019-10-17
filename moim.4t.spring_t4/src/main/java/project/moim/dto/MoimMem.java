package project.moim.dto;

public class MoimMem {
	private String id;
	private int moimcode;
	private String fav;
	private int permit;
	
	public MoimMem() {
		super();
	}

	public MoimMem(String id, int moimcode, String fav, int permit) {
		super();
		this.id = id;
		this.moimcode = moimcode;
		this.fav = fav;
		this.permit = permit;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getMoimcode() {
		return moimcode;
	}

	public void setMoimcode(int moimcode) {
		this.moimcode = moimcode;
	}

	public String getFav() {
		return fav;
	}

	public void setFav(String fav) {
		this.fav = fav;
	}

	public int getPermit() {
		return permit;
	}

	public void setPermit(int permit) {
		this.permit = permit;
	}
}
