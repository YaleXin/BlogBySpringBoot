package com.yalexin.service;

import com.yalexin.entity.User;

public interface UserService {

    User checkUser(String username,String password);

}
