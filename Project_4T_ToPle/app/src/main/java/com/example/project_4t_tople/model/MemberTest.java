package com.example.project_4t_tople.model;

import java.io.Serializable;

public class MemberTest implements Serializable {
    private String nickname, thumbnail_image;
    private int permit;
    public MemberTest() {
    }

    public MemberTest(String nickname, String thumbnail_image, int permit) {
        this.nickname = nickname;
        this.thumbnail_image = thumbnail_image;
        this.permit = permit;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getThumbnail_image() {
        return thumbnail_image;
    }

    public void setThumbnail_image(String thumbnail_image) {
        this.thumbnail_image = thumbnail_image;
    }

    public int getPermit() {
        return permit;
    }

    public void setPermit(int permit) {
        this.permit = permit;
    }
}
