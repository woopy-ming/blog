package org.woopy.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.woopy.entity.Type;
import org.woopy.util.PageQueryUtil;

import java.util.List;

/**
 * @author woopy
 * @data 2020/8/18 - 15:20
 */
@Mapper
@CacheConfig(cacheNames = "type")
public interface TypeMapper {

    List<Type> getAllList(PageQueryUtil pageQueryUtil);

    List<Type> getAllType();

    List<Type> getTypyTop(int size);

    int getTotalCount();

    Type selectById(int id);

    int updateById(Type type);

    Type selByName(String name);

    int save(Type type);

    int deleteBatch(int[] ids);



}
