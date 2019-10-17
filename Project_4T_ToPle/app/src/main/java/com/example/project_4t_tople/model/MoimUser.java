package com.example.project_4t_tople.model;


import java.io.Serializable;

public class MoimUser implements Serializable {
    // 모임유저
    private String id;
    private String tel;
    private int permit;
    private int moimcode;
    private String moim;
    private String fav;
    private String name;
    private String gender;
    private String birth;
    private String loca;
    private String prof;
    private String thumb;
    private String isShowDial;
    // 모임정보
    private String moimname;
    private String prod;
    private String pic;
    private String color;
    private int count;



    public MoimUser() {
    }

    public MoimUser(String id, String tel, int permit, int moimcode, String moim, String fav, String name, String gender, String birth, String loca, String prof, String thumb, String isShowDial, String moimname, String prod, String pic, String color, int count) {
        this.id = id;
        this.tel = tel;
        this.permit = permit;
        this.moimcode = moimcode;
        this.moim = moim;
        this.fav = fav;
        this.name = name;
        this.gender = gender;
        this.birth = birth;
        this.loca = loca;
        this.prof = prof;
        this.thumb = thumb;
        this.isShowDial = isShowDial;
        this.moimname = moimname;
        this.prod = prod;
        this.pic = pic;
        this.color = color;
        this.count = count;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public int getPermit() {
        return permit;
    }

    public void setPermit(int permit) {
        this.permit = permit;
    }

    public int getMoimcode() {
        return moimcode;
    }

    public void setMoimcode(int moimcode) {
        this.moimcode = moimcode;
    }

    public String getMoim() {
        return moim;
    }

    public void setMoim(String moim) {
        this.moim = moim;
    }

    public String getFav() {
        return fav;
    }

    public void setFav(String fav) {
        this.fav = fav;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBirth() {
        return birth;
    }

    public void setBirth(String birth) {
        this.birth = birth;
    }

    public String getLoca() {
        return loca;
    }

    public void setLoca(String loca) {
        this.loca = loca;
    }

    public String getProf() {
        return prof;
    }

    public void setProf(String prof) {
        this.prof = prof;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public String getIsShowDial() {
        return isShowDial;
    }

    public void setIsShowDial(String isShowDial) {
        this.isShowDial = isShowDial;
    }

    public String getMoimname() {
        return moimname;
    }

    public void setMoimname(String moimname) {
        this.moimname = moimname;
    }

    public String getProd() {
        return prod;
    }

    public void setProd(String prod) {
        this.prod = prod;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}