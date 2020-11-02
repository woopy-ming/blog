package org.woopy.service;

import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;
import org.woopy.entity.Blog;
import org.woopy.util.PageQueryUtil;
import org.woopy.util.PageResult;

import java.util.List;
import java.util.Map;

/**
 * @author woopy
 * @data 2020/8/16 - 13:57
 */
public interface BlogService {

   Blog getBlogAndTagById(int blogId);

   Blog getBlogById(int blogId);

   int getTotalCount();

   int getCountByStatus();

   PageResult getBlogsPage(PageQueryUtil pageUtil);

   String saveBlog(Blog blog);

   String updateBlog(Blog blog);

   PageInfo getBlogList(int pageNum);

   boolean deleteBatch(int[] ids);

   PageInfo getBlogPageByTypeId(int typeId,int pageNum);

   PageInfo getBlogPageByTagIdAndName(int tagId,int pageNum);

   Map<String,List<Blog>> archivesBlog();

   PageInfo getBlogsBytitle(int pageNum,String text);
}
