package com.yalexin.vo;

/**
 * Author：Yalexin
 * Email： 181303209@yzu.edu.cn
 */
public class CommentQuery {
    private String nickname;
    private String email;

    public CommentQuery() {
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isAudited() {
        return audited;
    }

    public void setAudited(boolean audited) {
        this.audited = audited;
    }

    private boolean audited;
}
