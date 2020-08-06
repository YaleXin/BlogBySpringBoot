package com.yalexin.vo;

/**
 * Author：Yalexin
 * Email： 181303209@yzu.edu.cn
 */
//@Component
//@ConfigurationProperties(prefix = "friend")
public class Friend {
    String info;
    String desc;
    String avatar;
    String addr;

    @Override
    public String toString() {
        return "Friend{" +
                "info='" + info + '\'' +
                ", desc='" + desc + '\'' +
                ", avatar='" + avatar + '\'' +
                ", addr='" + addr + '\'' +
                '}';
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }
}
