package com.example.project_4t_tople.model;

public class Redat {
   private int  renum; //답글번호
   private int listnum;
   private String  id;
   private  int moimcode;
   private String reple;
   private String redate;

    public Redat() {
    }

    public Redat(int renum, int listnum, String id, int moimcode, String reple, String redate) {
        this.renum = renum;
        this.listnum = listnum;
        this.id = id;
        this.moimcode = moimcode;
        this.reple = reple;
        this.redate = redate;
    }

    public int getRenum() {
        return renum;
    }

    public void setRenum(int renum) {
        this.renum = renum;
    }

    public int getListnum() {
        return listnum;
    }

    public void setListnum(int listnum) {
        this.listnum = listnum;
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

    public String getReple() {
        return reple;
    }

    public void setReple(String reple) {
        this.reple = reple;
    }

    public String getRedate() {
        return redate;
    }

    public void setRedate(String redate) {
        this.redate = redate;
    }
}
