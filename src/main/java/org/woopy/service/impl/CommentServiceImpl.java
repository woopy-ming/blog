package org.woopy.service.impl;


import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.woopy.entity.Blog;
import org.woopy.entity.Comment;
import org.woopy.entity.Tag;
import org.woopy.mapper.CommentMapper;
import org.woopy.service.CommentService;
import org.woopy.util.PageQueryUtil;
import org.woopy.util.PageResult;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by limi on 2017/10/22.
 */
@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentMapper commentMapper;

    /*@Override
    public List<Comment> listCommentByBlogId(Long blogId) {
        Sort sort = new Sort("createTime");
        List<Comment> comments = commentRepository.findByBlogIdAndParentCommentNull(blogId,sort);
        return eachComment(comments);
    }*/

    //@Cacheable(cacheNames = "blog",key = "'blogComment'+#p0")
    @Override
    public List<Comment> listCommentByBlogId(int blogId) {
        List<Comment> comments = commentMapper.findByBlogIdAndParentCommentNull(blogId);

        for (Comment c:comments) {
            c.setReplyComments(commentMapper.getreplyComments(blogId, c.getId()));
            for (Comment a : c.getReplyComments())
                a.setParentComment(commentMapper.findById(c.getId()));
        }

        /*System.out.println("循环前");
        for (Comment c:comments)
            System.out.println(c.toString());*/

        return eachComment(blogId,comments);
    }

    @Transactional
    @Override
    public int saveComment(Comment comment) {
        int parentCommentId = comment.getParentComment().getId();
        if (parentCommentId != -1) {
            comment.setParentComment(commentMapper.findById(parentCommentId));
        } else {
            comment.setParentComment(null);
        }
        comment.setCreateTime(new Date());

        return commentMapper.save(comment);
    }

    @Override
    public int getTotalCount() {
        return commentMapper.getTotalCount();
    }

    //@Cacheable(cacheNames = "comment",key = "'commentList'+#pageQueryUtil.page")
    @Override
    public PageResult getCommentsPage(PageQueryUtil pageQueryUtil) {
        List<Comment> comments = commentMapper.getAllList(pageQueryUtil);

        for (Comment comment : comments){
            int id = comment.getParentCommentId();
            if (id != 0){
                if (getByParentId(comments,id) != null)
                    getByParentId(comments,id).setReplyBoby("@"+comment.getNickname()+": \n  "+comment.getContent());
            }
        }

        int total = commentMapper.getTotalCount();
        PageResult pageResult = new PageResult(comments, total, pageQueryUtil.getLimit(), pageQueryUtil.getPage());
        return pageResult;
    }

    @Override
    public boolean checkDone(int[] ids) {
        return commentMapper.updateByStatus(ids) > 0;
    }

    @Override
    public boolean deleteBatch(int[] ids) {
        return commentMapper.deleteBatch(ids) > 0;
    }

    /**
     * 从原列表中得到parent_id对应的父节点
     * @param list
     * @param parent_id
     * @return
     */
    private Comment getByParentId(List<Comment> list,int parent_id) {
        int i = 0;
        for (; i < list.size() ; i++){
            if (list.get(i).getId() == parent_id)
                break;
        }
        if (i == list.size())
            return null;
        return list.get(i);
    }

    /**
     * 循环每个顶级的评论节点
     * @param comments
     * @return
     */
    private List<Comment> eachComment(int blogId,List<Comment> comments) {
        List<Comment> commentsView = new ArrayList<>();
        for (Comment comment : comments) {
            Comment c = new Comment();
            BeanUtils.copyProperties(comment,c);
            commentsView.add(c);
        }

        //合并评论的各层子代到第一级子代集合中
        combineChildren(blogId,commentsView);
        return commentsView;
    }

    /**
     *
     * @param comments root根节点，blog不为空的对象集合
     * @return
     */
    private void combineChildren(int blogId,List<Comment> comments) {

        for (Comment comment : comments) {
            List<Comment> replys1 = comment.getReplyComments();

            for(Comment reply1 : replys1) {
                //循环迭代，找出子代，存放在tempReplys中
                recursively(blogId,reply1);
            }
            //修改顶级节点的reply集合为迭代处理后的集合
            comment.setReplyComments(tempReplys);
            //清除临时存放区
            tempReplys = new ArrayList<>();
        }
    }

    //存放迭代找出的所有子代的集合
    private List<Comment> tempReplys = new ArrayList<>();
    /**
     * 递归迭代，剥洋葱
     * @param comment 被迭代的对象
     * @return
     */
    private void recursively(int blogId,Comment comment) {
        tempReplys.add(comment);//顶节点添加到临时存放集合
        comment.setReplyComments(commentMapper.getreplyComments(blogId,comment.getId()));
        for (Comment a : comment.getReplyComments())
            a.setParentComment(commentMapper.findById(comment.getId()));

        /*System.out.println("循环中");
        for (Comment b:comment.getReplyComments())
            System.out.println(b.toString());*/

        if (comment.getReplyComments().size()>0) {
            List<Comment> replys = comment.getReplyComments();
            for (Comment reply : replys) {
                tempReplys.add(reply);
                if (reply.getReplyComments().size()>0) {
                    recursively(blogId,reply);
                }
            }
        }
    }

}
