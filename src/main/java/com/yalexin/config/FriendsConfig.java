package com.yalexin.config;

import com.yalexin.vo.Friend;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * Author：Yalexin
 * Email： 181303209@yzu.edu.cn
 */
// 友情链接自动配置类
@Component
@ConfigurationProperties(prefix = "friendsconfig")
public class FriendsConfig {
    Friend[] friends;

    public Friend[] getFriends() {
        return friends;
    }

    public void setFriends(Friend[] friends) {
        this.friends = friends;
    }

    @Override
    public String toString() {
        return "Friends{" +
                "friends=" + Arrays.toString(friends) +
                '}';
    }
}
