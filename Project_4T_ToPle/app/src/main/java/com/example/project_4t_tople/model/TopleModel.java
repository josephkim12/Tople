package com.example.project_4t_tople.model;

import java.io.Serializable;

public class TopleModel implements Serializable {
    // Board
    private int listnum;          //게시글 번호
    private String id;             //작성자 아이디
    private int moimcode;         //글이 작성된 모임 코드
    private String name;        //작성자 이름_카카오 닉네임    nickname
    private String subject;       //제목
    private String content;       //내용
    private String filename;      //저장된 사진 이름
    private String thumb;         //사진의 썸네일 이름
    private String update_prof;  // 사진
    private String editdate;     //작성/수정된 날짜
    private int lev;             //공지 등의 분류 번호(레벨)

    // MyPage

    private String prod; //  모임소개
    private String loca; // 위치
    private String color; //  색상
    private String moimname; //   모임명
    private int count;  // 인원
    private int permit;   // 권한
    private String fav;  // 즐겨찾기, true or false
    private String pic; //  배너 사진
    // private int moimcode; //  number primary key  모임 코드

    // 모임유저
    // private String id;
    private String tel;                 // phone_num
    // private int permit;
    // private int moimcode;
    // private String fav;
    // private String name;
    private String gender;
    private String birth;               // birthday
    // private String loca;
    // private String thumb;
    // private String isShowDial;
    private String banner_pic;          // 모임 pic
    private String region;              // 사용자 지역

    public TopleModel() {
    }

    public TopleModel(int listnum, String id, int moimcode, String name, String subject, String content, String filename, String thumb, String update_prof, String editdate, int lev, String prod, String loca, String color, String moimname, int count, int permit, String fav, String pic, String tel, String gender, String birth, String banner_pic, String region) {
        this.listnum = listnum;
        this.id = id;
        this.moimcode = moimcode;
        this.name = name;
        this.subject = subject;
        this.content = content;
        this.filename = filename;
        this.thumb = thumb;
        this.update_prof = update_prof;
        this.editdate = editdate;
        this.lev = lev;
        this.prod = prod;
        this.loca = loca;
        this.color = color;
        this.moimname = moimname;
        this.count = count;
        this.permit = permit;
        this.fav = fav;
        this.pic = pic;
        this.tel = tel;
        this.gender = gender;
        this.birth = birth;
        this.banner_pic = banner_pic;
        this.region = region;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public String getUpdate_prof() {
        return update_prof;
    }

    public void setUpdate_prof(String update_prof) {
        this.update_prof = update_prof;
    }

    public String getEditdate() {
        return editdate;
    }

    public void setEditdate(String editdate) {
        this.editdate = editdate;
    }

    public int getLev() {
        return lev;
    }

    public void setLev(int lev) {
        this.lev = lev;
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

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
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

    public String getBanner_pic() {
        return banner_pic;
    }

    public void setBanner_pic(String banner_pic) {
        this.banner_pic = banner_pic;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }
}
