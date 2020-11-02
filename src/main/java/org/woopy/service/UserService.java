package org.woopy.service;

import org.woopy.entity.User;

/**
 * @author woopy
 * @data 2020/8/17 - 15:15
 */
public interface UserService {

    User login(String username,String password);

    boolean updateName(int id,String username,String nickname);

    boolean updatePassword(int id,String orgirnPassword,String password);
}
