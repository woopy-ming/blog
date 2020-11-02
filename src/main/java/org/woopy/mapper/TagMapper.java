package org.woopy.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.woopy.entity.Tag;
import org.woopy.util.PageQueryUtil;

import java.util.List;

/**
 * @author woopy
 * @data 2020/8/18 - 17:03
 */
@Mapper
public interface TagMapper {

    Tag selByName(String name);

    Tag selById(int id);

    int batchInsertBlogTag(List<Tag> tagList);

    int getTotalCount();

    List<Tag> getAllList(PageQueryUtil pageUtil);

    int deleteBatch(int[] ids);

    int saveTag(Tag tag);

    List<Tag> getTagTop(int size);
}
