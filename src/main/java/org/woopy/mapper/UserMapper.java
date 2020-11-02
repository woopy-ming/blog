package org.woopy.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.woopy.entity.User;

/**
 * @author woopy
 * @data 2020/8/17 - 15:11
 */
@Mapper
public interface UserMapper {

    @Select("SELECT * FROM t_user WHERE username = #{username} AND `password` = #{password}")
    User login(@Param("username") String username,@Param("password") String password);

    @Select("select * from t_user where id = #{id}")
    User selById(@Param("id") int id);

    int updateSel(User user);
}
