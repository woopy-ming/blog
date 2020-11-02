package org.woopy.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.woopy.entity.Blog;
import org.woopy.entity.BlogTag;
import org.woopy.entity.Tag;
import org.woopy.entity.Type;
import org.woopy.mapper.BlogMapper;
import org.woopy.mapper.BlogTagMapper;
import org.woopy.mapper.TagMapper;
import org.woopy.mapper.TypeMapper;
import org.woopy.service.BlogService;
import org.woopy.util.MarkdownUtils;
import org.woopy.util.PageQueryUtil;
import org.woopy.util.PageResult;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author woopy
 * @data 2020/8/16 - 14:03
 */
@CacheConfig(cacheNames = "blog")
@Service
public class BlogServiceImpl implements BlogService {

    @Autowired
    RedisTemplate<Object,Object> myredisTemplate;

    @Autowired
    BlogMapper blogMapper;

    @Autowired
    TypeMapper typeMapper;

    @Autowired
    BlogTagMapper blogTagMapper;

    @Autowired
    TagMapper tagMapper;

    @Override
    public Blog getBlogAndTagById(int blogId) {
        //System.out.println("查询数据库中id为"+blogId+"的blog");
        Blog blog = blogMapper.getBlogById(blogId);
        Blog b = new Blog();
        BeanUtils.copyProperties(blog,b);
        List<String> tags = Arrays.asList(b.getBlogTags().split(","));
        b.setTags(tags);
        String content = b.getBlogContent();
        b.setBlogContent(MarkdownUtils.markdownToHtmlExtensions(content));
        return b;
    }

    @Override
    public Blog getBlogById(int blogId) {
        return blogMapper.getBlogById(blogId);
    }

    @Override
    public int getTotalCount() {
        int count = blogMapper.getTotalCount();
        myredisTemplate.opsForValue().set("blog:blogCount",count,30, TimeUnit.MINUTES);
        return count;
    }

    @Override
    public int getCountByStatus() {
        return blogMapper.getBlogListSelStatus().size();
    }

    @Override
    public PageResult getBlogsPage(PageQueryUtil pageUtil) {
        System.out.println("后台博客列表查询"+pageUtil.getPage());
        List<Blog> blogList = blogMapper.findBlogList(pageUtil);
        //System.out.println(blogList);
        int total = blogList.size();
        PageResult pageResult = new PageResult(blogList, total, pageUtil.getLimit(), pageUtil.getPage());
        return pageResult;
    }

    @Override
    @Transactional
    public String saveBlog(Blog blog) {
        Type type = typeMapper.selectById(blog.getBlogTypeId());
        if (type == null)
        {
            type.setId(0);
            type.setName("默认分类");
        }else {
            blog.setBlogTypeName(type.getName());
            type.setNum(type.getNum() + 1);
        }

        //处理标签数据
        String[] tags = blog.getBlogTags().split(",");
        if (tags.length > 6)
            return "标签数量限制为6";

        if (blogMapper.save(blog) > 0){
            //新增的tag对象
            List<Tag> tagListForInsert = new ArrayList<>();
            //所有的tag对象，用于建立关系数据
            List<Tag> allTagsList = new ArrayList<>();
            for (int i = 0; i < tags.length; i++)
            {
                Tag tag = tagMapper.selByName(tags[i]);
                if (tag == null){
                    //不存在，说明是新标签，需增加
                    Tag temp = new Tag();
                    temp.setName(tags[i]);
                    tagListForInsert.add(temp);
                }else{
                    allTagsList.add(tag);
                }
            }

            //新增标签数据
            if (!CollectionUtils.isEmpty(tagListForInsert))
                tagMapper.batchInsertBlogTag(tagListForInsert);

            typeMapper.updateById(type);
            List<BlogTag> blogTags = new ArrayList<BlogTag>();
            allTagsList.addAll(tagListForInsert);
            for (Tag tag : allTagsList){
                BlogTag blogTag = new BlogTag();
                blogTag.setBlogsId(blog.getBlogId());
                blogTag.setTagsId(tag.getId());
                blogTags.add(blogTag);
            }

            if (blogTagMapper.batchInsert(blogTags) > 0) {
                Set<Object> keys = myredisTemplate.keys("*bloglist*");
                myredisTemplate.delete(keys);
                myredisTemplate.opsForValue().increment("blog:blogCount");
                return "success";
            }
        }

        return "保存失败";
    }

    @Override
    @Transactional
    public String updateBlog(Blog blog) {
        Blog blogForUpdate = blogMapper.getBlogById(blog.getBlogId());
        if (blogForUpdate == null) {
            return "数据不存在";
        }
        blogForUpdate.setBlogTitle(blog.getBlogTitle());
        blogForUpdate.setBlogContent(blog.getBlogContent());
        blogForUpdate.setBlogCoverImage(blog.getBlogCoverImage());
        blogForUpdate.setBlogStatus(blog.getBlogStatus());
        blogForUpdate.setDescription(blog.getDescription());
        blogForUpdate.setFlag(blog.getFlag());
        Type blogCategory = typeMapper.selectById(blog.getBlogTypeId());
        if (blogCategory == null) {
            blogForUpdate.setBlogTypeId(0);
            blogForUpdate.setBlogTypeName("默认分类");
        } else {
            //设置博客分类名称
            blogForUpdate.setBlogTypeName(blogCategory.getName());
            blogForUpdate.setBlogTypeId(blogCategory.getId());
            //分类的排序值加1
            blogCategory.setNum(blogCategory.getNum() + 1);
        }
        //处理标签数据
        String[] tags = blog.getBlogTags().split(",");
        if (tags.length > 6) {
            return "标签数量限制为6";
        }
        blogForUpdate.setBlogTags(blog.getBlogTags());
        //新增的tag对象
        List<Tag> tagListForInsert = new ArrayList<>();
        //所有的tag对象，用于建立关系数据
        List<Tag> allTagsList = new ArrayList<>();
        for (int i = 0; i < tags.length; i++) {
            Tag tag = tagMapper.selByName(tags[i]);
            if (tag == null) {
                //不存在就新增
                Tag tempTag = new Tag();
                tempTag.setName(tags[i]);
                tagListForInsert.add(tempTag);
            } else {
                allTagsList.add(tag);
            }
        }
        //新增标签数据不为空->新增标签数据
        if (!CollectionUtils.isEmpty(tagListForInsert)) {
            tagMapper.batchInsertBlogTag(tagListForInsert);
        }
        List<BlogTag> blogTagRelations = new ArrayList<>();
        //新增关系数据
        allTagsList.addAll(tagListForInsert);
        for (Tag tag : allTagsList) {
            BlogTag blogTagRelation = new BlogTag();
            blogTagRelation.setBlogsId(blog.getBlogId());
            blogTagRelation.setTagsId(tag.getId());
            blogTagRelations.add(blogTagRelation);
        }
        //修改blog信息->修改分类排序值->删除原关系数据->保存新的关系数据
        typeMapper.updateById(blogCategory);
        blogTagMapper.deleteByBlogId(blog.getBlogId());
        blogTagMapper.batchInsert(blogTagRelations);
        if (blogMapper.update(blogForUpdate) > 0) {
            Set<Object> keys = myredisTemplate.keys("*bloglist*");
            myredisTemplate.delete(keys);
            return "success";
        }
        return "修改失败";
    }


    @Cacheable(key = "'bloglist'+#p0")
    @Override
    public PageInfo getBlogList(int pageNum) {
        System.out.println("首页博客列表查询");
        PageHelper.startPage(pageNum,5);
        List<Blog> blogs = blogMapper.getBlogListSelStatus();
        PageInfo<Blog> page = new PageInfo(blogs);
        return page;
    }

    @Override
    public boolean deleteBatch(int[] ids) {
        return blogMapper.deleteBatch(ids) > 0;
    }


    @Override
    public PageInfo getBlogPageByTypeId(int typeId, int pageNum) {
        PageHelper.startPage(pageNum,5);
        List<Blog> blogs = blogMapper.selByTypeId(typeId);
        PageInfo<Blog> page = new PageInfo(blogs);
        return page;
    }

    @Override
    public PageInfo getBlogPageByTagIdAndName(int tagId, int pageNum) {
        PageHelper.startPage(pageNum,5);
        Tag tag = tagMapper.selById(tagId);
        if (tag != null){
            List<Blog> blogs = blogMapper.selBylikeTagName(tag.getName());

            for (int i = 0; i < blogs.size(); i++) {
                String[] strings = blogs.get(i).getBlogTags().split(",");
                blogs.get(i).setTags(Arrays.asList(strings));
            }

            PageInfo<Blog> page = new PageInfo(blogs);
            return page;
        }
        return null;
    }

    @Cacheable(key = "'archivesBlog'")
    @Override
    public Map<String, List<Blog>> archivesBlog() {
        List<String> years = blogMapper.findGroupYear();
        Map<String,List<Blog>> map = new LinkedHashMap<>();
        for (String year : years){
            map.put(year,blogMapper.findByYear(year));
        }
        return map;
    }

    @Override
    public PageInfo getBlogsBytitle(int pageNum,String text) {
        PageHelper.startPage(pageNum,5);
        List<Blog> blogs = blogMapper.selBytitle(text);
        PageInfo<Blog> pageInfo = new PageInfo(blogs);
        return pageInfo;
    }


}
