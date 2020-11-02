package org.woopy.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.woopy.entity.User;
import org.woopy.mapper.UserMapper;
import org.woopy.service.UserService;
import org.woopy.util.MD5Util;

/**
 * @author woopy
 * @data 2020/8/17 - 15:16
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserMapper userMapper;

    @Override
    public User login(String username, String password) {
        return userMapper.login(username, MD5Util.MD5Encode(password,"UTF-8"));
    }

    @Override
    public boolean updateName(int id, String username, String nickname) {
        User user = userMapper.selById(id);
        if (user != null){
            user.setUsername(username);
            user.setNickname(nickname);
            return userMapper.updateSel(user) > 0;
        }
        return false;
    }

    @Override
    public boolean updatePassword(int id,String orgirnPassword,String newpassword) {
        User user = userMapper.selById(id);
        if (user != null){
            String orgirnPasswordMd5 = MD5Util.MD5Encode(orgirnPassword,"UTF-8");
            if (orgirnPasswordMd5.equals(user.getPassword())) {
                user.setPassword(MD5Util.MD5Encode(newpassword, "UTF-8"));
                return userMapper.updateSel(user) > 0;
            }
        }
        return false;
    }

}
