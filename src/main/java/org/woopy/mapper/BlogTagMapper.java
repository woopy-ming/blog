package org.woopy.mapper;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.woopy.entity.BlogTag;

import java.util.List;

/**
 * @author woopy
 * @data 2020/8/18 - 17:09
 */
@Mapper
public interface BlogTagMapper {

    int batchInsert(List<BlogTag> blogTagList);

    @Delete("DELETE FROM t_blog_tags WHERE blogs_id = #{id}")
    int deleteByBlogId(int id);

    List<Integer> selByTagsId(int[] ids);

    List<Integer> selByTagSIdGetBlogId(int id);
}
