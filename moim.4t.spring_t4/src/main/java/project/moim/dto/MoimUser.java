package project.moim.dto;

public class MoimUser {
	private String id;
	private String user_img;
	
	public MoimUser() {
		super();
	}

	public MoimUser(String id, String user_img) {
		super();
		this.id = id;
		this.user_img = user_img;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public String getUser_id() {
		return id;
	}

	public void setUser_id(String user_id) {
		this.id = user_id;
	}

	public String getUser_img() {
		return user_img;
	}

	public void setUser_img(String user_img) {
		this.user_img = user_img;
	}
}
