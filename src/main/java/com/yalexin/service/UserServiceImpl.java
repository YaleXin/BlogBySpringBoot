package com.yalexin.service;

import com.yalexin.dao.UserRepository;
import com.yalexin.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Author：Yalexin
 * Email： 181303209@yzu.edu.cn
 */
@Service
public class UserServiceImpl implements UserService{
    @Autowired
    UserRepository userRepository;

    @Override
    public User checkUser(String username, String password) {
        User user = userRepository.findByUsernameAndPassword(username, password);
        return user;
    }
}
