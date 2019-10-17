package project.moim.dto;

public class Schedule {
	private int sch_schnum;
	private int sch_moimcode;
	private String sch_year;
	private String sch_month;
	private String sch_day;
	private String sch_title;
	private String sch_time;
	private String sch_sub;
	private String sch_amount;
	private double sch_lat;
	private double sch_lot;
	
	public Schedule() {
		super();
	}

	public int getSch_schnum() {
		return sch_schnum;
	}

	public void setSch_schnum(int sch_schnum) {
		this.sch_schnum = sch_schnum;
	}

	public int getSch_moimcode() {
		return sch_moimcode;
	}

	public void setSch_moimcode(int sch_moimcode) {
		this.sch_moimcode = sch_moimcode;
	}

	public String getSch_year() {
		return sch_year;
	}

	public void setSch_year(String sch_year) {
		this.sch_year = sch_year;
	}

	public String getSch_month() {
		return sch_month;
	}

	public void setSch_month(String sch_month) {
		this.sch_month = sch_month;
	}

	public String getSch_day() {
		return sch_day;
	}

	public void setSch_day(String sch_day) {
		this.sch_day = sch_day;
	}

	public String getSch_time() {
		return sch_time;
	}

	public void setSch_time(String sch_time) {
		this.sch_time = sch_time;
	}

	public String getSch_title() {
		return sch_title;
	}

	public void setSch_title(String sch_title) {
		this.sch_title = sch_title;
	}

	public String getSch_sub() {
		return sch_sub;
	}

	public void setSch_sub(String sch_sub) {
		this.sch_sub = sch_sub;
	}

	public String getSch_amount() {
		return sch_amount;
	}

	public void setSch_amount(String sch_amount) {
		this.sch_amount = sch_amount;
	}

	public double getSch_lat() {
		return sch_lat;
	}

	public void setSch_lat(double sch_lat) {
		this.sch_lat = sch_lat;
	}

	public double getSch_lot() {
		return sch_lot;
	}

	public void setSch_lot(double sch_lot) {
		this.sch_lot = sch_lot;
	}
}
