package com.example.project_4t_tople.model;

import java.io.Serializable;

public class Detail_Todo implements Serializable {
    private String Detail_item_img;     // 사용자 img
    private String id;          // 사용자 ID
    private int sch_schnum;     // 일정 코드
    private int moimcode;       // 모임 코드
    private int amount;         // 회비
    private boolean ispay;      // 회비 납입 여부
    private String todo;        // 역할
    private String ex;          // 내용

    public Detail_Todo() {
    }

    public boolean isIspay() {
        return ispay;
    }

    public void setIspay(boolean ispay) {
        this.ispay = ispay;
    }

    public String getDetail_item_img() {
        return Detail_item_img;
    }

    public void setDetail_item_img(String detail_item_img) {
        Detail_item_img = detail_item_img;
    }

    public int getSch_schnum() {
        return sch_schnum;
    }

    public void setSch_schnum(int sch_schnum) {
        this.sch_schnum = sch_schnum;
    }

    public int getMoimcode() {
        return moimcode;
    }

    public void setMoimcode(int moimcode) {
        this.moimcode = moimcode;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getTodo() {
        return todo;
    }

    public void setTodo(String todo) {
        this.todo = todo;
    }

    public String getEx() {
        return ex;
    }

    public void setEx(String ex) {
        this.ex = ex;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
