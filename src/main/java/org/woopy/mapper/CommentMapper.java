package org.woopy.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.woopy.entity.Comment;
import org.woopy.util.PageQueryUtil;

import java.util.List;

/**
 * Created by limi on 2017/10/22.
 */
@Mapper
public interface CommentMapper{

    List<Comment> findByBlogIdAndParentCommentNull(int blogId);

    /*@Select("SELECT * FROM t_comment WHERE blog_id = #{id}")
    List<Comment> listCommentByBlogId(int id);*/

    Comment findByParentId(int id);

    int save(Comment comment);

    List<Comment> getreplyComments(@Param("blogId") int blogId,@Param("commontId") int commontId);

    Comment findById(int id);

    int getTotalCount();

    List<Comment> getAllList(PageQueryUtil pageQueryUtil);

    int updateByStatus(int[] ids);

    int deleteBatch(int[] ids);

}
