package org.woopy.service;


import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.woopy.entity.Comment;
import org.woopy.util.PageQueryUtil;
import org.woopy.util.PageResult;

import java.util.List;

/**
 * Created by limi on 2017/10/22.
 */
public interface CommentService {

    List<Comment> listCommentByBlogId(int blogId);

    int saveComment(Comment comment);

    int getTotalCount();

    PageResult getCommentsPage(PageQueryUtil pageQueryUtil);

    boolean checkDone(int[] ids);

    boolean deleteBatch(int[] ids);
}
