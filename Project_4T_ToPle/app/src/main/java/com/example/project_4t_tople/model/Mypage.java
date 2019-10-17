package com.example.project_4t_tople.model;

import java.io.Serializable;

public class Mypage implements Serializable {
    private String prod; //  모임소개
    private String loca; // 위치
    private String color; //  색상
    private String moimname; //   모임명
    private int count;  // 인원
    private int permit;   // 권한
    private String fav;  // 즐겨찾기, true or false
    private String pic; //  filename 사진
    private int moimcode; //  number primary key  모임 코드

    public Mypage() {
    }

    public Mypage(String prod, String loca, String color, String moimname, int count, int permit, String fav, String pic, int moimcode, String user_id) {
        this.prod = prod;
        this.loca = loca;
        this.color = color;
        this.moimname = moimname;
        this.count = count;
        this.permit = permit;
        this.fav = fav;
        this.pic = pic;
        this.moimcode = moimcode;
    }

    public String getProd() {
        return prod;
    }

    public void setProd(String prod) {
        this.prod = prod;
    }

    public String getLoca() {
        return loca;
    }

    public void setLoca(String loca) {
        this.loca = loca;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getMoimname() {
        return moimname;
    }

    public void setMoimname(String moimname) {
        this.moimname = moimname;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getPermit() {
        return permit;
    }

    public void setPermit(int permit) {
        this.permit = permit;
    }

    public String getFav() {
        return fav;
    }

    public void setFav(String fav) {
        this.fav = fav;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public int getMoimcode() {
        return moimcode;
    }

    public void setMoimcode(int moimcode) {
        this.moimcode = moimcode;
    }


}
