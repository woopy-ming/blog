package org.woopy.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.woopy.entity.Blog;
import org.woopy.util.PageQueryUtil;

import java.util.List;

/**
 * @author woopy
 * @data 2020/8/16 - 12:47
 */

@CacheConfig(cacheNames = "blog")
@Mapper
public interface BlogMapper {

    @Cacheable(key = "'blog'+#p0")
    @Select("select * from t_blog where blog_id = #{value}")
    Blog getBlogById(int id);

    int getTotalCount();

    //@Cacheable(key = "'adminbloglist'+#pageQueryUtil.page")
    List<Blog> findBlogList(PageQueryUtil pageQueryUtil);

    int save(Blog blog);

    int update(Blog blog);

    List<Blog> getBlogListSelStatus();

    int deleteBatch(int[] ids);

    @Update("update t_blog set is_deleted = 1 where blog_id = #{id}")
    int deleteById(int id);

    int updateBlogType(@Param("name") String name, @Param("id") int id, @Param("ids")int[] ids);

    List<Blog> selByTypeName(String name);

    List<Blog> selByTypeId(int id);

    List<Blog> selBylikeTagName(String name);

    List<String> findGroupYear();

    List<Blog> findByYear(String year);

    List<Blog> selBytitle(String text);
}
