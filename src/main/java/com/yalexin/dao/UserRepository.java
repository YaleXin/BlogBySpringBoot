package com.yalexin.dao;

import com.yalexin.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Author：Yalexin
 * Email： 181303209@yzu.edu.cn
 */
public interface UserRepository extends JpaRepository<User,Long> {
    User findByUsernameAndPassword(String username, String password);
}
